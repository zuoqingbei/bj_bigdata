/*
 * Copyright (c) 2014. 骆驼CMS
 */

package com.conference.util.sys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.conference.common.BaseController;
import com.conference.util.StringKit;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 *jQuery DataTables 插件工具类，提供后台查询功能<br>
 * @author wlo_o
 * @description
 * 例子：<br>
 *Java代码：<br>
<pre>{@code
 	public void list(){
 		String aColumns[] = new String[]{"p.id","p.title","d.name as depart_name","p.level"};
		String sTables = "`exam_paper` p LEFT JOIN `depart` d on p.`depart_id` = d.`id` ";
		Map<String,Object> rs = DataTablesUtil.query(aColumns, sTables, this);
		renderJson(rs);
	}
}</pre>
Html代码:<br>
<pre>
{@code
<h3 class="heading"> 试卷 <a href="/admin/me/exam/question/add"><span class="label">新建试卷</span></a></h3>
<div class="row-fluid">
	<div class="span12">
		<table id="paper" class="table table-bordered table-striped">
			<thead>
				<tr>
					<th width="10px" ><input name="checkall" type="checkbox" class="checkall" value="ON" /></th>
					<th>试题名称</th>
					<th>学科</th>
					<th>模式</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
			<tr>
				<th class="checkers"><input type="checkbox" class="select_rows"/></th>
				<td colspan ="3"></td>
			</tr>
			</tbody>
		</table>
	</div>
</div>
<div class="hide">
    <div class="dt_gal_actions">
        <div class="btn-group">
            <button data-toggle="dropdown" class="btn dropdown-toggle">操作 <span class="caret"></span></button>
            <ul class="dropdown-menu">
                <li><a href="#" class="delete_rows_dt" data-tableid="paper">
                	<i class="icon-trash"></i> Delete</a>
                </li>
            </ul>
        </div>
    </div>
</div>
}
</pre>
JavaScript代码：<br>
<pre>
{@code
$(function(){
	var setting = $.extend(true,baseDataTablesSettings,{
		"sAjaxSource": "/admin/me/exam/question/list",
		"aoColumns": [{//元数据信息
			"mDataProp": null,
			"sDefaultContent": '&lt;input type="checkbox" class="select_rows" /&gt;',
			"bSortable":false},
			{ "mDataProp": "title" },
			{ "mDataProp": "depart_name" },
			{ "mDataProp": "level" },
			{ "mDataProp": null ,"bSortable":false,
				"sDefaultContent": "",
				"mRender":function(data,type,row){
					return '<a href="#" class="row-recommend" >'+row["id"]+'</a>';
				}
			},
			{ "mDataProp": null,
				"sDefaultContent": 
					'<a href="#" class="row-edit"><i class="icon-edit"></i></a>'+
					'<a href="#" class="row-trash"><i class="icon-trash"></i></a>'+
					'<a href="#" class="row-cog"><i class="icon-cog"></i></a>',
					"bSortable":false//禁用排序
			}],
        "fnDrawCallback":function(oSettings){
        	$("#paper tbody td.row-trash").confirmDialog({
        		oLanguage:{sMessage:"你确定要删除本条记录？"},
         		fnOk:function(me){
         			var aPos = oTable.fnGetPosition(me.parentNode.parentNode);
                    var aData = oTable.fnGetData(aPos[0]);    
        			$.post("/admin/me/exam/question/delete",{"id":oData.id},function(date){
                       _settings.fn.call(this,oLink,nTr,oData);
                    },"json");
         		}
        	});
         	$("#paper tbody td.icon-edit").live("click",function(){
                 var aData = oTable.fnGetData($(this).closest("tr")[0]);  
                 window.location = "/admin/me/exam/question/edit/"+aData.id;
         	});
         	$("#paper tbody td.icon-cog").live("click",function(){
         		var aPos = oTable.fnGetPosition(this.parentNode.parentNode);
                var aData = oTable.fnGetData(aPos[0]);         
                window.location = "/admin/me/exam/question/assembly/view"+aData.id;
         	});
         	//全选事件
         	$("#paper thead .checkall").click(function(){
         		$("#paper tbody input[class=select_rows]").prop("checked", this.checked);
    		});
        },
        //ajax返回数据方法
   "fnServerData" : function(sSource, aoData, fnCallback) {  
	   var condition="{";
	   for(var i=0;i<aoData.length;i++){
			condition+="\""+aoData[i].name+"\":"+"\""+aoData[i].value+"\",";
	   }
	   condition+="}"
	   var conditions = eval('(' + condition + ')');
       $.ajax({  
           "type" : 'get',  
           "url" : sSource,  
           "dataType" : "json",  
           "data" :conditions,  
           "success" : function(resp) {  
        	   //处理datatables自己方法
        	   fnCallback(resp);  
        	   //处理自己的结果
        	   $("#payTotal").html(resp.payTotal);
        	   $("#refundTotal").html(resp.refundTotal);
        	   $("#total").html(resp.total);
           }  
       });  
   }  
	})
	oTable = $('#paper').dataTable(setting);
});
}</pre>
 * <p> 
 * @date 2013-11-1
 * 
 */
public class DataTablesUtil {
	//能处理的函数列表
	public static final String[] FORMULA = new String[]{"avg","count","max","min","sum","concat_ws","concat"};
	
	
	/**
	 * 查询内容
	 * @author wlo_o
	 * @param aColumns		列名字符串数组
	 * @param sTables		表名字符串
	 * @param oController	jFinal控制器
	 * @return
	 */
	public static Map<String,Object> query(String[] aColumns, String sTables, BaseController oController){
		StringBuffer sWhere=new StringBuffer();
		SqlUtil.packageWhereSql(oController, null, sWhere, null);
		return DataTablesUtil.queryU(aColumns, sTables, sWhere.toString(), null, null, oController,true);
	}
	
	/**
	 * 查询内容
	 * @author wlo_o
	 * @param aColumns		列名字符串数组
	 * @param sTables		表名字符串
	 * @param sWhereSql		默认条件
	 * @param oController	jFinal控制器
	 * @return
	 */
	public static Map<String,Object> query(String[] aColumns, String sTables,String sWhereSql, BaseController oController){
		StringBuffer sWhere=new StringBuffer();
		if(StringUtils.isNotBlank(sWhereSql)){
			sWhere=new StringBuffer(" AND "+sWhereSql);
		}
		SqlUtil.packageWhereSql(oController, null, sWhere, null);
		return DataTablesUtil.queryU(aColumns, sTables,  sWhere.toString(), null, null, oController,true);
	}
	
	
	/**
	 * 查询内容
	 * @param aColumns		列名字符串数组
	 * @param sTables		表名字符串
	 * @param sWhereSql 	额外追加的where条件
	 * @param sQroupBySql 	分组条件
	 * @param sOrderSql 	排序条件
	 * @param oController	jFinal控制器
	 * @return
	 * @author longjunfeng
	 * @date   2015年12月31日下午1:44:38
	 */
	public static Map<String,Object> query(String[] aColumns, String sTables,String sWhereSql, String sQroupBySql, String sOrderSql, BaseController oController){
		StringBuffer sWhere=new StringBuffer();
		if(StringUtils.isNotBlank(sWhereSql)){
			sWhere=new StringBuffer(" AND ").append(sWhereSql);
		}
		SqlUtil.packageWhereSql(oController, null, sWhere, null);
		return DataTablesUtil.queryU(aColumns, sTables,  sWhere.toString(), sQroupBySql, sOrderSql, oController,true);
	}
	
	/**
	* 查询内容
	 * @param aColumns		列名字符串数组
	 * @param sTables		表名字符串
	 * @param sWhereSql 	额外追加的where条件
	 * @param sQroupBySql 	分组条件
	 * @param sOrderSql 	排序条件
	 * @param oController	jFinal控制器
	 * @param isPage		是否分页
	 * @return
	 * @author longjunfeng
	 * @date   2016年1月4日下午1:42:35
	 */
	public static Map<String,Object> query(String[] aColumns, String sTables,String sWhereSql, String sQroupBySql, String sOrderSql, BaseController oController,Boolean isPage){
		StringBuffer sWhere=new StringBuffer();
		if(StringUtils.isNotBlank(sWhereSql)){
			sWhere=new StringBuffer(" AND "+sWhereSql);
		}
		SqlUtil.packageWhereSql(oController, null, sWhere, null);
		return DataTablesUtil.queryU(aColumns, sTables,  sWhere.toString(), sQroupBySql, sOrderSql, oController,isPage);
	}
	
	
	/**
	 * 
	 * @param aColumns		列名字符串数组
	 * @param sTables		表名字符串
	 * @param sWhereSql 	额外追加的where条件
	 * @param sQroupBySql 	分组条件
	 * @param sOrderSql 	排序条件
	 * @param oController	jFinal控制器
	 * @return
	 */
	private static Map<String,Object> queryU(String[] aColumns, String sTables,String sWhereSql, String sQroupBySql, String sOrderSql, Controller oController,Boolean isPage){
		//1.分页数据
		String sLimit = "";
		if(isPage&&!StringKit.isBlank(oController.getPara("iDisplayStart")) && !oController.getPara("iDisplayLength").equals("-1")){
			sLimit = "limit " + oController.getParaToInt("iDisplayStart") + ", " + oController.getParaToInt("iDisplayLength");
		}
		//2.排序
		String sOrder = "";
		String _order = oController.getPara("iSortCol_0");
//		if(StringKit.notBlank(_order) && !"0".equals(_order)){
		if(StringKit.notBlank(_order)){
			sOrder = "ORDER BY  ";
			int count = oController.getParaToInt("iSortingCols");
			if(count<aColumns.length){
				for(int i=0; i < count; i++){
					if("true".equals(oController.getPara("bSortable_" + oController.getPara("iSortCol_" + i)))){
						String column = aColumns[oController.getParaToInt("iSortCol_"+ i) ];
						column = getColumnAliases(column);
						sOrder += (column + ("asc".equals(oController.getPara("sSortDir_"+i))? " asc" : " desc") + ", ");
					}
				}
			}
			sOrder = sOrder.substring(0, sOrder.length() - 2);
			if("ORDER BY".equals(sOrder)){
				sOrder = "";
			}
		}
		if(StringKit.notBlank(sOrderSql)){
			if(StringKit.isBlank(sOrder)){
				sOrder = "ORDER BY  " + sOrderSql;
			}
		}
		//3.过滤	对所有的列加   （or col like"%%"）条件 
		String sWhere = "";
		if(!StringKit.isBlank(oController.getPara("sSearch"))){
			sWhere = "WHERE (";
			for(int i=0; i<aColumns.length; i++){
				if(!isFormula(aColumns[i])){ //字段未使用函数时拼装条件语句
					String column = getColumnName(aColumns[i]);
					//TODO 查询先排除掉时间--
					if(!column.contains("create_at")&&!column.contains("update_at")){
						sWhere += column + " LIKE '%" + oController.getPara("sSearch")+"%' OR ";
					}
				}
			}
			sWhere = sWhere.substring(0, sWhere.length()-3);
			sWhere += ")";
		}
		//列过滤	对特殊的列 加 and  col like '%%' 条件
		for(int i=0; i<aColumns.length; i++){
			if(StringKit.notBlank(oController.getPara("bSearchable_" + i)) 
					&& "true".equals(oController.getPara("bSearchable_" + i))
					&& StringKit.notBlank(oController.getPara("sSearch_"+i))){
				if(!isFormula(aColumns[i])){
					if(StringKit.isBlank(sWhere)){
						sWhere = "WHERE ";
					}else{
						sWhere += " AND ";
					}
					sWhere += getColumnName(aColumns[i]) + " LIKE '%" + oController.getPara("sSearch_"+i) + "%' ";
				}
			}
		}
		//4.添加自定义where
		if(!StringKit.isBlank(sWhereSql)){
			if(StringKit.isBlank(sWhere)){
				sWhere = "WHERE 1=1 ";
			}else{
				//sWhere += " AND ";
			}
			sWhere += sWhereSql;
		}
		String sColumns = " ";
		for(String col : aColumns){
			sColumns += col + ",";
		}
		//5.得到条件 进行查询
		sColumns = sColumns.substring(0, sColumns.length()-1);
		sQroupBySql = StringKit.notBlank(sQroupBySql)?"GROUP BY " + sQroupBySql : "";
		
//		String sQuery = "SELECT SQL_CALC_FOUND_ROWS "+ sColumns +" FROM " + sTables+ " " + sWhere+" " + sQroupBySql + " " + sOrder +" " + sLimit;
//		List<Record>  list = Db.find(sQuery);
//		Long iFilteredTotal = Db.queryLong("SELECT FOUND_ROWS()");//结果集数量有的数据库不支持
		
		String sQuery = "SELECT  "+ sColumns +" FROM " + sTables+ " " + sWhere+" " + sQroupBySql + " " + sOrder +" " + sLimit;
		List<Record>  list = Db.find(sQuery);
		
		Long iFilteredTotal =0l;
		if(StringUtils.isNotBlank(sQroupBySql)){
			iFilteredTotal = Db.queryLong("SELECT count(1) FROM  ( SELECT count(1) FROM  " + sTables+ " " + sWhere+" " + sQroupBySql+" ) datatable_table1");//结果集数量
		}else{
			iFilteredTotal = Db.queryLong("SELECT count(1) FROM " + sTables+ " " + sWhere+" " + sQroupBySql);//结果集数量
		}
		if(iFilteredTotal==null){
			iFilteredTotal=0l;
		}
		Map<String,Object> rs = new HashMap<String,Object>();
		String table = sTables.split(" ")[0];
		Long iTotal = Db.queryLong("SELECT COUNT(1) FROM " + table + "");//总记录
		rs.put("sEcho", oController.getParaToInt("sEcho"));
		rs.put("iTotalRecords", iTotal);
		rs.put("iTotalDisplayRecords", iFilteredTotal);//当前数量
		rs.put("aaData", list);
		return rs;
	}
	
	
	
	/**
	 * 获取真实的字段名，字段两端添加 `
	 * 例 getColumnName("p.id as pk") 返回 p.`id`
	 * @param column
	 * @return
	 */
	public static String getColumnName(String column){
		String isFormulaRegex = ".*(?i)("+StringUtils.join(FORMULA, "|")+").*\\(";
		column = column.replaceAll(isFormulaRegex, "").replace(")", "");//替换掉部分公式。提取字段名
		if(column.indexOf(".") != -1){//判断有表名别名
			String[] _column_info = column.split("\\.");
			String _column_name = _column_info[1].trim();
			String regex = "";
			if(_column_name.toLowerCase().indexOf(" as ") != -1 || _column_name.indexOf(" ") != -1){//判断有字段别名
				if(_column_name.toLowerCase().indexOf(" as ") != -1){
					regex = " +[a|A][s|S] +";
				}else if(_column_name.indexOf(" ") != -1){
					regex = "\\s+";
				}
				_column_info[1] = _column_name.split(regex)[0];	//去除别名，只获取字段名称
			}
			column = _column_info[0] + ".`" + _column_info[1].trim()+"`";
		}else{
			String regex = "";
			if(column.toLowerCase().indexOf(" as ") != -1 || column.trim().indexOf(" ") != -1){//判断有字段别名
				if(column.toLowerCase().indexOf(" as ") != -1){
					regex = " +[a|A][s|S] +";
				}else if(column.indexOf(" ") != -1){
					regex = "\\s+";
				}
				column = column.split(regex)[0];
			}
			column = "`"+column.trim()+"`";
		}
		return column;
	}
	/**
	 * 获取字段名或别名，用于order by 排序<br>
	 * 是函数则获取别名<br>
	 * 不是函数则调用getColumnName 获取字段名<br>
	 * <br>
	 * 例如:<br>
	 * getColumnAliases("Sum(num) as total") 返回 <b>total</b><br>
	 * getColumnAliases("sum(num) total") 返回<b>total</b><br>
	 * getColumnAliases("sum(num)") 返回<b>sum(num)</b><br>
	 * getColumnAliases("a.num") 返回<b>a.`num`</b><br>
	 * getColumnAliases("num") 返回<b>num</b><br>
	 * @author wlo_o
	 * @param column
	 * @return
	 */
	public static String getColumnAliases(String column){
		String aliases = "";
		if(isFormula(column)){//判断字段有使用函数
			aliases = column.toLowerCase().replaceAll(".*(?i)("+StringUtils.join(FORMULA, "|")+").*\\(.*\\)", "").replaceAll("(?i)as", "");
			aliases = aliases.trim();
			if(StringKit.isBlank(aliases)){//为空说明没有别名，直接返回函数
				aliases = column;
			}
		}else{
			aliases = getColumnName(column);
		}
		return aliases;
	}
	
	/**
	 * 获取字段名或别名，用于order by 排序<br>
	 * 是函数则获取别名<br>
	 * 不是函数则调用getColumnName 获取字段名<br>
	 * 与上面方法不同，去除表别名和 '
	 * <br>
	 * 例如:<br>
	 * getColumnAliases("Sum(num) as total") 返回 <b>total</b><br>
	 * getColumnAliases("sum(num) total") 返回<b>total</b><br>
	 * getColumnAliases("sum(num)") 返回<b>sum(num)</b><br>
	 * getColumnAliases("a.num") 返回<b>num</b><br>
	 * getColumnAliases("num") 返回<b>num</b><br>
	 * @author wlo_o
	 * @param column
	 * @return
	 */
	public static String getColumnAliasesNotableName(String column){
		String aliases = "";
		if(isFormula(column)){//判断字段有使用函数
			aliases = column.toLowerCase().replaceAll(".*(?i)("+StringUtils.join(FORMULA, "|")+").*\\(.*\\)", "").replaceAll("(?i)as", "");
			aliases = aliases.trim();
			if(StringKit.isBlank(aliases)){//为空说明没有别名，直接返回函数
				aliases = column;
			}
		}else{
			aliases = getColumnName(column);
		}
		aliases=aliases.replace("`", "");
		if(aliases.indexOf(".")!=-1){
			return aliases.split("\\.")[1];
		}
		return aliases;
	}
	
	public static String[] getColumnAliasesNotableNames(String[] columns){
		String[] columns2=new String[columns.length];
		for (int i = 0; i < columns.length; i++) {
			columns2[i]=DataTablesUtil.getColumnAliasesNotableName(columns[i]);
		}
		return columns2;
	}
	
	/**
	 * 判断字段是否包含函数
	 * @author wlo_o
	 * @return
	 */
	public static boolean isFormula(String column){
		String isFormulaRegex = ".*(?i)("+StringUtils.join(FORMULA, "|")+").*\\(.*\\).*";
		Pattern p = Pattern.compile(isFormulaRegex);
		Matcher m = p.matcher(column);
		return m.matches();
	}
	
	public static void main(String[] args) {
		DataTablesUtil util = new DataTablesUtil();
//		String col1 = "Sum(a.name) as total";
//		String col2 = "min ( a.name ) num";
//		System.out.println(util.getColumnName(col1));
//		System.out.println(col1);
//		System.out.println(util.getColumnName(col2));
//		System.out.println(col2);

		System.out.println(util.getColumnAliases("CONCAT_WS('  ',a.update_user_name,a.update_at) as update_at"));
		System.out.println(util.getColumnName("CONCAT_WS('  ',a.update_user_name,a.update_at) as update_at"));
	}
}
