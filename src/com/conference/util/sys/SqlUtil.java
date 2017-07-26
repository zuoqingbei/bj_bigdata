package com.conference.util.sys;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.jsoup.helper.StringUtil;

import com.conference.admin.model.Dept;
import com.conference.common.BaseController;
import com.conference.common.Constant;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;


/** 
 * 类说明 ：表格查询工具
 * @author 	作者 : longjunfeng E-mail：463527083@qq.com
 * @version 创建时间：2015年12月31日 上午9:07:11 
 */
public class SqlUtil implements Constant{
	
	public static String ORDER_NAME="table_order_feild";
	public static String ORDER_BY= " order by ";
	public static String WHERE=" where";
	public static String AND=" and ";

	/**
	 * 
	 * @time   2017年7月11日 下午2:38:18
	 * @author zuoqb
	 * @todo   在标签中拼接筛选条件(以新闻为主表)
	 */
	public static String joinSqlForNews(HttpServletRequest request,String tableShortName,boolean containDate){
		StringBuffer sql=new StringBuffer();
		if(StringUtils.isBlank(tableShortName)){
			tableShortName="fct_zb";
		}
		String companyId=getAttrByKey(request,SESSION_COMPANYID);
		String deptId=getAttrByKey(request,SESSION_DEPTID);
		String creatorId=getAttrByKey(request,SESSION_CREATORID);
		String direct=getAttrByKey(request,SESSION_DIRECT);
		String startDate=getAttrByKey(request,SESSION_STARTDATE);
		String endDate=getAttrByKey(request,SESSION_ENDDATE);
		//大单位
		if(StringUtils.isNotBlank(companyId)&&StringUtils.isBlank(deptId)){
			//如果大单位不为空 而部门为空 根据大单位查询下属部门
			List<Record> list=Dept.dao.getDepts(companyId);
			sql.append(" and (");
			for(int x=0;x<list.size();x++){
				if(x==list.size()-1){
					sql.append(tableShortName+".source_dept_ids like '%"+list.get(x).get("id")+"%'  ");
				}else{
					sql.append(tableShortName+".source_dept_ids like '%"+list.get(x).get("id")+"%' or ");
				}
			}
			sql.append(" ) ");
		}
		//部门
		if(StringUtils.isNotBlank(deptId)){
			sql.append(" and "+tableShortName+".source_dept_ids like '%,"+deptId+",%' ");
		}
		//发稿人
		if(StringUtils.isNotBlank(creatorId)){
			sql.append(" and "+tableShortName+".creator_ids like '%,"+creatorId+",%' ");
		}
		//direct 方向
		if(StringUtils.isNotBlank(direct)){
			sql.append(" and "+tableShortName+".direction_ids like '%,"+direct+",%' ");
		}
		if(containDate){
			//在统计同比环比时不需要时间
			//起始日期
			if(StringUtils.isNotBlank(startDate)){
				sql.append(" and date_format("+tableShortName+".created_time,\"%Y-%m-%d\")>='"+startDate+"' ");
			}
			//截止日期
			if(StringUtils.isNotBlank(endDate)){
				sql.append(" and date_format("+tableShortName+".created_time,\"%Y-%m-%d\")<='"+endDate+"' ");
			}
		}
		return sql.toString();
	}
	/*public static String joinSqlForNews(HttpServletRequest request,String tableShortName,boolean containDate){
		StringBuffer sql=new StringBuffer();
		if(StringUtils.isBlank(tableShortName)){
			tableShortName="fct_zb";
		}
		String companyId=getAttrByKey(request,SESSION_COMPANYID);
		String deptId=getAttrByKey(request,SESSION_DEPTID);
		String creatorId=getAttrByKey(request,SESSION_CREATORID);
		String direct=getAttrByKey(request,SESSION_DIRECT);
		String startDate=getAttrByKey(request,SESSION_STARTDATE);
		String endDate=getAttrByKey(request,SESSION_ENDDATE);
		//大单位
		if(StringUtils.isNotBlank(companyId)&&StringUtils.isBlank(deptId)){
			//如果大单位不为空 而部门为空 根据大单位查询下属部门
			List<Record> list=Dept.dao.getDepts(companyId);
			sql.append(" and (");
			for(int x=0;x<list.size();x++){
				if(x==list.size()-1){
					sql.append(tableShortName+".source_dept_ids='"+list.get(x).get("id")+"'  ");
				}else{
					sql.append(tableShortName+".source_dept_ids='"+list.get(x).get("id")+"' or ");
				}
			}
			sql.append(" ) ");
		}
		//部门
		if(StringUtils.isNotBlank(deptId)){
			sql.append(" and "+tableShortName+".source_dept_ids ='"+deptId+"' ");
		}
		//发稿人
		if(StringUtils.isNotBlank(creatorId)){
			sql.append(" and "+tableShortName+".creator_ids='"+creatorId+"' ");
		}
		//direct 方向
		if(StringUtils.isNotBlank(direct)){
			sql.append(" and "+tableShortName+".direction_ids like '%,"+direct+",%' ");
		}
		if(containDate){
			//在统计同比环比时不需要时间
			//起始日期
			if(StringUtils.isNotBlank(startDate)){
				sql.append(" and date_format("+tableShortName+".created_time,\"%Y-%m-%d\")>='"+startDate+"' ");
			}
			//截止日期
			if(StringUtils.isNotBlank(endDate)){
				sql.append(" and date_format("+tableShortName+".created_time,\"%Y-%m-%d\")<='"+endDate+"' ");
			}
		}
		return sql.toString();
	}*/
	public static String joinSqlForNews(HttpServletRequest request){
		return joinSqlForNews(request, null, true);
	}
	public static String joinSqlForNews(HttpServletRequest request,boolean containDate){
		return joinSqlForNews(request, null, containDate);
	}
	
	
	
	
	
	
	/**
	 * 
	 * @time   2017年7月11日 下午2:38:18
	 * @author zuoqb
	 * @todo   在标签中拼接筛选条件(以素材为主表)
	 */
	public static String joinSqlForSource(HttpServletRequest request,String tableShortName,boolean containDate){
		StringBuffer sql=new StringBuffer();
		if(StringUtils.isBlank(tableShortName)){
			tableShortName="fct_origin";
		}
		String companyId=getAttrByKey(request,SESSION_COMPANYID);
		String deptId=getAttrByKey(request,SESSION_DEPTID);
		String creatorId=getAttrByKey(request,SESSION_CREATORID);
		String direct=getAttrByKey(request,SESSION_DIRECT);
		String startDate=getAttrByKey(request,SESSION_STARTDATE);
		String endDate=getAttrByKey(request,SESSION_ENDDATE);
		//大单位
		if(StringUtils.isNotBlank(companyId)){
			sql.append(" and "+tableShortName+".from_dept='"+companyId+"' ");
		}
		//部门
		if(StringUtils.isNotBlank(deptId)){
			sql.append(" and "+tableShortName+".dept_id='"+deptId+"' ");
		}
		//发稿人
		if(StringUtils.isNotBlank(creatorId)){
			sql.append(" and "+tableShortName+".creator_id='"+creatorId+"' ");
		}
		//direct 方向 暂时不知道来源
		if(StringUtils.isNotBlank(direct)){
			//sql.append(" and "+tableShortName+".direction_ids like '%,"+direct+",%' ");
		}
		if(containDate){
			//在统计同比环比时不需要时间
			//起始日期
			if(StringUtils.isNotBlank(startDate)){
				sql.append(" and date_format("+tableShortName+".created_time,\"%Y-%m-%d\")>='"+startDate+"' ");
			}
			//截止日期
			if(StringUtils.isNotBlank(endDate)){
				sql.append(" and date_format("+tableShortName+".created_time,\"%Y-%m-%d\")<='"+endDate+"' ");
			}
		}
		return sql.toString();
	}
	
	public static String joinSqlForSource(HttpServletRequest request){
		return joinSqlForSource(request, null, true);
	}
	public static String joinSqlForSource(HttpServletRequest request,boolean containDate){
		return joinSqlForSource(request, null, containDate);
	}
	public static void main(String[] args) {
		System.out.println(getHuanBiDate("2014-01-01", "2014-06-30"));
	}
	public static String formatNumber( String num ){
        num = num.replaceAll(",", ""); // 去掉所有逗号
        java.text.DecimalFormat df = new java.text.DecimalFormat("##,###,###");
        return df.format( Double.parseDouble( num ) );
    }
	
	/**
	 * 获得环比的待比较时间范围，逗号分隔，返回待环比日期
	 * @param startDate 如："2014-01-01"，默认是一个月的第一天
	 * @param endDate 如："2014-06-30"，默认与startDate在同一年
	 * @return
	 * @author Euray
	 */
	public static String getHuanBiDate(String startDate, String endDate) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(StringUtils.isBlank(startDate)){
			startDate=sdf.format(new Date());
		}
		if(StringUtils.isBlank(endDate)){
			endDate=sdf.format(new Date());
		}
	    String[] starts = startDate.split("-");
	    String[] ends = endDate.split("-");
	    int startYear = Integer.parseInt(starts[0]);
	    int startMonth = Integer.parseInt(starts[1]);
	    int endMonth = Integer.parseInt(ends[1]);
	    int endDay = Integer.parseInt(ends[2]);
	    Calendar end = Calendar.getInstance();
	    end.set(startYear, endMonth - 1, 1);
	    int days = end.getActualMaximum(Calendar.DAY_OF_MONTH);
	    String returnDate = "";
	    int subNum = endMonth - startMonth + 1;//当前选择了几个月
	    if (startMonth > subNum) { //待环比日期在同一年，还是不同一年
	        int startMonthH = startMonth - subNum;
	        int endMonthH = startMonth - 1;
	        Calendar endH = Calendar.getInstance();
	        endH.set(startYear, endMonthH - 1, 1);
	        int daysH = endH.getActualMaximum(Calendar.DAY_OF_MONTH);
	        returnDate = startYear + (startMonthH < 10 ? "-0" : "-") + startMonthH + "-01," + startYear
	                + (endMonthH < 10 ? "-0" : "-") + endMonthH + "-" + (endDay < days ? ends[2] : daysH);
	    } else {
	        int startMonthH = 12 + startMonth - subNum;
	        if (startMonth > 1) {
	            int endMonthH = startMonth - 1;
	            Calendar endH = Calendar.getInstance();
	            endH.set(startYear, endMonthH - 1, 1);
	            int daysH = endH.getActualMaximum(Calendar.DAY_OF_MONTH);
	            returnDate = (startYear - 1) + (startMonthH < 10 ? "-0" : "-") + startMonthH + "-01," + startYear
	                    + (endMonthH < 10 ? "-0" : "-") + endMonthH + "-" + (endDay < days ? ends[2] : daysH);
	        } else {
	            returnDate = (startYear - 1) + (startMonthH < 10 ? "-0" : "-") + startMonthH + "-01," + (startYear - 1)
	                    + "-12-" + (endDay < 31 ? ends[2] : 31);
	        }
	    }
	    return returnDate;
	}
	 
	/**
	 * 获得同比的待比较时间范围，逗号分隔，返回待同比日期
	 * @param startDate 如："2014-01-01"，默认是一个月的第一天
	 * @param endDate 如："2014-06-30"，默认与startDate在同一年
	 * @return
	 * @author Euray
	 */
	public static String getTongBiDate(String startDate, String endDate) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(StringUtils.isBlank(startDate)){
			startDate=sdf.format(new Date());
		}
		if(StringUtils.isBlank(endDate)){
			endDate=sdf.format(new Date());
		}
	    String[] starts = startDate.split("-");
	    String[] ends = endDate.split("-");
	    int startYear = Integer.parseInt(starts[0]);
	    int endMonth = Integer.parseInt(ends[1]);
	    int endDay = Integer.parseInt(ends[2]);
	    Calendar endDateT = Calendar.getInstance();
	    endDateT.set(startYear - 1, endMonth - 1, 1);
	    int days = endDateT.getActualMaximum(Calendar.DAY_OF_MONTH);
	    String returnDate = "";
	    if (endDay < days) {
	        returnDate = (startYear - 1) + "-" + starts[1] + "-01," + (startYear - 1) + "-" + ends[1] + "-" + ends[2];
	    } else {
	        returnDate = (startYear - 1) + "-" + starts[1] + "-01," + (startYear - 1) + "-" + ends[1] + "-" + days;
	    }
	    return returnDate;
	}
	
	
	
	
	/**
	 * 
	 * @time   2017年7月11日 下午3:24:55
	 * @author zuoqb
	 * @todo  从session中获取参数
	 */
	public static String getAttrByKey(HttpServletRequest request,String key){
		HttpSession session=request.getSession();
		String value=session.getAttribute(key)==null?"":session.getAttribute(key)+"";
		return value;
	}
	
	
	
	
	
	/**
	 * 去除order 和group条件
	 * @author longjunfeng
	 * @date   2015年11月6日上午10:36:19
	 */
	public static Page<Record> page(BaseController c,String recordsStr,String selectSql,String fromSql,String specialWhere,List<Object> specialParams){
		String[] st = recordsStr.split(",");
		return page(c, Arrays.asList(st), selectSql, fromSql, specialWhere,specialParams, "", "");
	}
	
	public static Page<Record> page(BaseController c,String recordsStr,String selectSql,String fromSql,String specialWhere,List<Object> specialParams,String orderSql,String groupSql){
		String[] st = recordsStr.split(",");
		return page(c, Arrays.asList(st), selectSql, fromSql, specialWhere,specialParams, orderSql, groupSql);
	}
	
	/**
	 * @desc	:分页,去除recordsStr参数和orderSql,groupSql参数
	 * @author	:作者 longjunfeng E-mail:463527083@qq.com
	 * @version :创建时间 2016年12月23日
	 * @param c
	 * @param selectSql
	 * @param fromSql
	 * @param specialWhere
	 * @param specialParams
	 * @return
	 */
	public static Page<Record> page(BaseController c,String selectSql,String fromSql,String specialWhere,List<Object> specialParams){
		return page(c, "", selectSql, fromSql, specialWhere,specialParams);
	}
	
	/**
	 * @desc	:分页,去除recordsStr参数
	 * @author	:作者 longjunfeng E-mail:463527083@qq.com
	 * @version :创建时间 2016年12月23日
	 * @param c
	 * @param selectSql		select语句					需要传 select
	 * @param fromSql		从哪些表						需要传 from
	 * @param specialWhere	特殊的where条件要自己写完，再传入	 	不需传 where
	 * @param specialParams	特殊where的条件值
	 * @param orderSql		排序							不需传 order by 
	 * @param groupSql		分组							需要传 group by
	 * @return
	 */
	public static Page<Record> page(BaseController c,String selectSql,String fromSql,String specialWhere,List<Object> specialParams,String orderSql,String groupSql){
		return page(c, "", selectSql, fromSql, specialWhere,specialParams);
	}
	
	
	/**
	 * 分页封装方法
	 * 
	 * @param c				BaseController	用于一些get和set操作
	 * @param recordsStr	要从search条件中，获取哪些表要查的字段  有哪些表别名
	 * @param selectSql		select语句					需要传 select
	 * @param fromSql		从哪些表						需要传 from
	 * @param specialWhere	特殊的where条件要自己写完，再传入	 	不需传 where
	 * @param specialParams	特殊where的条件值
	 * @param orderSql		排序							不需传 order by 
	 * @param groupSql		分组							需要传 group by
	 * @return
	 * @author longjunfeng
	 * @date   2015年11月6日上午10:32:05
	 */
	public static Page<Record> page(BaseController c,List<String> recordsStr,String selectSql,String fromSql,String specialWhere,List<Object> specialParams,String orderSql,String groupSql){
		//1.获取前台数据
		int page=c.getParaToInt("p",1);
		int pageSize=c.getParaToInt("pageSize",10);
		try {
			if(c.getAttr("p")!=null){
				page=c.getAttrForInt("p");
			}
			if(c.getAttr("pageSize")!=null){
				pageSize=c.getAttrForInt("pageSize");
			}
		} catch (Exception e) {
		}
		//2.1把查询字段在set到页面
		if(recordsStr!=null){
			for (String string : recordsStr) {
				Record re = c.getRecord(string);
				if(re!=null){
					c.setAttr(string, re);
				}
			}
		}
		//2.2获取查询字段
		Map<String, Object> nomalParas = getNomalParamters(c);
		//3.准备查询字段 语句	selectSql
		
		//4.准备from  语句  fromSql
		
		//5.1准备有表别名的where和param 语句
		List<Object> params = new ArrayList<Object>();
		StringBuffer sWhere=new StringBuffer(" where 1=1 ");
		//5.2准备where语句
		if(nomalParas.keySet().size()>0)
			SqlUtil.packageNomalWhereSql(sWhere, params,nomalParas, c);
		//5.3准备特殊where条件需要自己写
		if(StringUtils.isNotBlank(specialWhere)){
			sWhere.append(" and ( ");
			sWhere.append(specialWhere);
			sWhere.append(" ) ");
			if(specialParams!=null){
				params.addAll(specialParams);
			}
		}
		//6.准备order
		StringBuffer orderBuffer=new StringBuffer();
		orderBuffer.append(orderSql==null?" ":orderSql);
		if(nomalParas.keySet().size()>0)
			orderBuffer = packageOrder(c,nomalParas.keySet(), orderBuffer, null);
		
		if(StringUtils.isNotBlank(orderBuffer.toString()))
			orderBuffer.insert(0, ORDER_BY);
		//7.查询返回
		StringBuffer sqlExt=new StringBuffer().append(fromSql).append(sWhere.toString()).append(StringUtils.isBlank(groupSql)?"":" group by " +groupSql).append(orderBuffer);
		if(pageSize==-1){
			StringBuffer noPagesql=new StringBuffer().append(selectSql).append(sqlExt);
			List<Record> list = Db.find(noPagesql.toString(), params.toArray());
			Page<Record> results=new Page<Record>(list, 1, -1, 1, -1);
			return results;
		}
		Page<Record> results=Db.paginate(page,pageSize,selectSql,sqlExt.toString(), params.toArray());
		return results;
	}
	
	
	/**
	 * 准备查询条件字段
	 * @author longjunfeng
	 * @date   2015年11月6日下午4:45:08
	 */
	public static void packageNomalWhereSql(StringBuffer sWhere,List<Object> params,Map<String, Object> nomalParas,BaseController c){
		if(c!=null){
			Map<String, Object> canshu =new HashMap<String, Object>();
			for(Entry<String, Object> aa: nomalParas.entrySet()) {
				if(aa.getValue()!=null){
					if(isWhereCondition(aa.getKey())){
						canshu.put(aa.getKey(), aa.getValue());
						c.setAttr(aa.getKey(), aa.getValue());//设置参数到页面
					}
				}
			}
			packageWhereSql(canshu, null, sWhere, params);
		}
	}
	
	
	
	/** 
	 * @Desc: 列表查询时候设置where 参数
	 * @param c
	 * @param tableAlias
	 * @param sWhere
	 * @param params
	 * @return: void
	 * @author: longjunfeng   
	 * @date: 2016年9月26日 下午5:49:01 
	 */
	public static void packageWhereSql(BaseController c,String tableAlias,StringBuffer sWhere,List<Object> params){
		Map<String, Object> queryParams = getNomalParamters(c);
		packageWhereSql(queryParams, tableAlias, sWhere, params);
	}
	
	/**列表查询时候设置where 参数
	 *  需要加_like,_lt,_gt...等后缀，才会生成查询条件
	 * 在查询form表单中设置查询字段如，od.username_like的值为“小王”，就会生成 一个 od.username like'%小王%' 的 where条件
	 * 符号对应如下
	 * like：like
	 * nlike：not like
	 * e 或者 eq:=  
	 * ne :!= 
	 * lt:< 
	 * lte:<= 
	 * gt:>
	 * gte:>=
	 * in: in中的条件要以逗号隔开或者是 list数组
	 * @param attrSet		model的变量set集合
	 * @param tableAlias	查询表别名
	 * @param sWhere		where条件变量	
	 * @param params		查询参数变量
	 * @param Controller	用来把查询参数再返回到页面
	 * @author longjunfeng
	 * @date   2015年11月5日下午3:28:34
	 */
	public static void packageWhereSql(Map<String, Object> attrSet,String tableAlias,StringBuffer sWhere,List<Object> params){
		String colum;
		String condition;
		for(Entry<String, Object> entry: attrSet.entrySet()) {
			//1.过滤不需要字段
			if(!isWhereCondition(entry.getKey())){
				continue;
			}
			//2.获取字段名colum 和字段值condition
			int _len = entry.getKey().lastIndexOf("_");
			colum=entry.getKey().substring(0, _len);
			condition=entry.getKey().substring(_len+1, entry.getKey().length()).toLowerCase();
			//3.生成sql语句
			if(entry.getValue() != null&&StringUtils.isNotBlank(((String)entry.getValue()))){
				sWhere=getOneColWhere(sWhere, tableAlias, params, colum, condition, (String)entry.getValue());
			}
		}
		sWhere.append(" ");
	}
	
	
	
	
	
	
	/**
	 * 获取order 条件，前台传入参数入 o.id_desc 就会生成 order o.id desc
	 * @param orderSql
	 * @return
	 * @author longjunfeng
	 * @date   2015年12月1日下午6:49:50
	 */
	public static StringBuffer packageOrder(BaseController c,Set<String> keys,StringBuffer orderSql,String tableAlias){
		StringBuffer returnBuffer=new StringBuffer();
		String colum;
		String condition;
		//如果order sql 不为空，
		if(StringUtils.isNotBlank(orderSql.toString())){
			returnBuffer.append(orderSql);
		}
		for (String key : keys) {
			//1.过滤不需要字段
			if(!isOrderCondition(key)){
				continue;
			}
			//2.获取字段colum 和字段值condition
			//model的字段，最后以_分割，_前是model的数据库字段，_后是查询的sql条件
			int _len = key.lastIndexOf("_");
			colum=key.substring(0, _len);
			condition=key.substring(_len+1, key.length());
			//3.把排序条件传回页面
			if(StringUtils.isBlank(tableAlias)){
				c.setAttr(ORDER_NAME, key);
			}else{
				c.setAttr(ORDER_NAME, tableAlias+"."+key);
			}
			//4.将传进来的order条件生成 sql
			// 前面有and 字段，就添加个and字段
			if(returnBuffer.toString().toLowerCase().contains(AND)){
				returnBuffer.append(AND);
			}
			if(StringUtils.isNotBlank(tableAlias)){
				returnBuffer.append(tableAlias);
				returnBuffer.append(".");
			}
			returnBuffer.append(colum).append(" ").append(condition).append(" ");
		}
		returnBuffer.append(" ");
		return returnBuffer;
	}
	
	
	/** 
	 * @Desc: 生成单个字段where条件
	 * @param sWhere
	 * @param params
	 * @return: void
	 * @author: longjunfeng   
	 * @return 
	 * @date: 2016年9月26日 下午4:56:36 
	 */
	private static StringBuffer getOneColWhere(StringBuffer sWhere,String tableAlias,List<Object> params,String colum,String condition,String value){
		value=value.trim();
		sWhere.append(" and (");
		if(StringUtils.isNotBlank(tableAlias)){
			sWhere.append(tableAlias);
			sWhere.append(".");
		}
		sWhere.append(colum);
		//1.like
		if(condition.equals("like")){
			sWhere.append(" like ");
			if(params!=null){
				sWhere.append("? ");
				params.add("%"+value+"%");
			}else{
				sWhere.append("'%"+value+"%'");
			}
		}
		//2.not like
		else if(condition.equals("nlike")){
			sWhere.append(" NOT LIKE ");
			if(params!=null){
				sWhere.append("? ");
				params.add("%"+value+"%");
			}else{
				sWhere.append("'%"+value+"%'");
			}
		}
		//3.=
		else if(condition.equals("e")||condition.equals("eq")){
			if("null".equals(value)||"NULL".equals(value)){
				sWhere.append(" IS NULL or ");
				if(StringUtils.isNotBlank(tableAlias)){
					sWhere.append(tableAlias);
					sWhere.append(".");
				}
				sWhere.append(colum);
				sWhere.append(" ='' ");
			}else{
				sWhere.append("=");
				if(params!=null){
					sWhere.append("? ");
					params.add(value);
				}else{
					sWhere.append("'"+value+"'");
				}
			}
		}
		//4. !=
		else if(condition.equals("ne")){
			if("null".equals(value)||"NULL".equals(value)){
				sWhere.append(" IS NOT NULL or ");
				if(StringUtils.isNotBlank(tableAlias)){
					sWhere.append(tableAlias);
					sWhere.append(".");
				}
				sWhere.append(colum);
				sWhere.append(" !='' ");
			}else{
				sWhere.append("!=");
				if(params!=null){
					sWhere.append("? ");
					params.add(value);
				}else{
					sWhere.append("'"+value+"'");
				}
			}
		}
		//4. <
		else if(condition.equals("lt")){
			sWhere.append("<");
			if(params!=null){
				sWhere.append("? ");
				params.add(value);
			}else{
				sWhere.append("'"+value+"'");
			}
		}
		//5. <=
		else if(condition.equals("lte")){
			sWhere.append("<=");
			if(params!=null){
				sWhere.append("? ");
				params.add(value);
			}else{
				sWhere.append("'"+value+"'");
			}
		}
		//6. >
		else if(condition.equals("gt")){
			sWhere.append(">");
			if(params!=null){
				sWhere.append("? ");
				params.add(value);
			}else{
				sWhere.append("'"+value+"'");
			}
		}
		//7. >=
		else if(condition.equals("gte")){
			sWhere.append(">=");
			if(params!=null){
				sWhere.append("? ");
				params.add(value);
			}else{
				sWhere.append("'"+value+"'");
			}
		}
		//8. in
		else if(condition.equals("in")){
			sWhere.append(" in ");
			//如果in条件是数组
			if(value.toString().indexOf(",")==-1){
				sWhere.append("("+ListIntoComma(Arrays.asList(value))+")");
			}
			//如果in条件是逗号隔开
			else{
				String in=value.toString();
				sWhere.append("( '"+in.replace(",", "','")+"')");
			}
		}
		sWhere.append(" ) ");
		return sWhere;
	}
	
	/**
	 * 根据参数名判断是否是 where字段
	 * @param key
	 * @return
	 */
	private static boolean isWhereCondition(String key){
		if(StringUtils.isBlank(key)){
			return false;
		}
		//model的字段，最后以_分割，_前是model的数据库字段，_后是查询的sql条件
		//以这些结尾的是查询条件
		if(	!(key.toLowerCase().endsWith("_like")
				||key.toLowerCase().endsWith("_like")
				||key.toLowerCase().endsWith("_nlike")
				||key.toLowerCase().endsWith("_e")
				||key.toLowerCase().endsWith("_eq")
				||key.toLowerCase().endsWith("_ne")
				||key.toLowerCase().endsWith("_lt")
				||key.toLowerCase().endsWith("_lte") 
				||key.toLowerCase().endsWith("_gt") 
				||key.toLowerCase().endsWith("_gte")
				||key.toLowerCase().endsWith("_in")
				)){
			return false;
		}
		return true;
		
	}
	
	
	/** 
	 * @Desc: 判断是否是排序条件
	 * @param key
	 * @return
	 * @return: boolean
	 * @author: longjunfeng   
	 * @date: 2016年9月26日 下午4:55:51 
	 */
	private static boolean isOrderCondition(String key){
		if(StringUtils.isBlank(key)){
			return false;
		}
		//model的字段，最后以_分割，_前是model的数据库字段，_后是查询的sql条件
		//以这些结尾的是查询条件
		if(!(key.toLowerCase().endsWith("_desc")||key.toLowerCase().endsWith("_asc"))){
			return false;
		}
		return true;
		
	}
	
	
	/** 
	 * @Desc: 获取param里面的和attr里面的参数,用于以后的where条件生成
	 * @param c
	 * @return
	 * @return: Map<String,String[]>
	 * @author: longjunfeng   
	 * @date: 2016年9月26日 下午4:56:08 
	 */
	private static Map<String, Object> getNomalParamters(BaseController c){
		//1.获取param里面的字段
		Map<String, String[]> nomalParas = c.getParaMap();
		Map<String, Object> returnParas = new HashMap<String, Object>();
		for(Entry<String, String[]> entry: nomalParas.entrySet()) {
			returnParas.put(entry.getKey(), entry.getValue()[0]);
		}
		//2.获取attr里面的字段
		Enumeration<String> attrs = c.getAttrNames();
	    while(attrs.hasMoreElements()){
	    	String key=attrs.nextElement();
	         returnParas.put(key, c.getAttr(key));
	    }
		return returnParas;
	}
	
	
	/**
	 * 列表拼装字符串，字符串两端添加单引号
	 * @author wlo_o
	 * @param list
	 * @return
	 */
	public static final <E> String ListIntoComma(Collection<E> list){
		return ListIntoComma(list,true);
	}
	/**
	 * 
	 * @time   2017年3月8日 上午9:06:05
	 * @author zuoqb
	 * @todo  拼接in语句
	 * @param  @param ObjList 要组装的list
	 * @param  @param key list中对象的key
	 * @param  @return
	 */
	public static String commonSqlGenerator(List<Record> ObjList,String key){
    	String sql=" and 1=1  ";
    	sql+=" in (";
    	for(Record obj:ObjList){
    		sql+="'"+obj.get(key)+"',";
    	}
    	sql=sql.substring(0,sql.length()-1);
    	sql+=")	";
    	return sql;
    }
	/**
	 * 
	 * @time   2017年3月8日 上午9:06:05
	 * @author zuoqb
	 * @todo  拼接in语句
	 * @param  @param sTable 表名
	 * @param  @param filed 属性-字段名
	 * @param  @param ObjList 要组装的list
	 * @param  @param key list中对象的key
	 * @param  @return
	 */
	public static String commonSqlGenerator(String sTable,String filed,List<Record> ObjList,String key){
    	String sql=" and 1=1  ";
    	if(ObjList==null||ObjList.size()==0){
    		return sql;
    	}
    	sql+=" and ";
    	if(sTable!=null&&StringUtils.isNotBlank(sTable)){
    		sql+=sTable+".";
    	}
    	if(filed!=null&&StringUtils.isNotBlank(filed)){
    		sql+=filed;
    	}
    	sql+=" in (";
    	for(Record obj:ObjList){
    		sql+="'"+obj.get(key)+"',";
    	}
    	sql=sql.substring(0,sql.length()-1);
    	sql+=")	";
    	return sql;
    }
	/**
	 * 列表拼装字符串
	 * @author wlo_o
	 * @param list
	 * @param addSingleQuotes true:添加单引号 false:不添加单引号
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static final <E> String ListIntoComma(Collection<E> list,Boolean addSingleQuotes ){
		String rs = "";
		Collection<E> tmp = null;
		if(addSingleQuotes){
			ArrayList new_list = new ArrayList();
			String str = "";
			for(Object obj : list){
				str = obj.toString();
				if(StrKit.notBlank(str)){//排除空字段
					new_list.add("'" + obj.toString() + "'");
				}
			}
			tmp = new_list;
		}else{
			tmp = list;
		}
		rs = StringUtil.join(tmp, ",");
		return rs;
	}
	public static String dealNull(String input){
		if(StringUtils.isBlank(input)||"null".equals(input)){
			return "暂无";
		}
		return input;
	}
}