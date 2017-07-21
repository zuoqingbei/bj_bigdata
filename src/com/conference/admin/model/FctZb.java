package com.conference.admin.model;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.conference.common.BaseModel;
import com.conference.util.sys.SqlUtil;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class FctZb extends BaseModel<FctZb>{
	private static final long serialVersionUID = -8811409081417389372L;
	public static final FctZb dao=new FctZb();
	/**
	 * 新闻量统计
	 * @param sqlWhere
	 * @return
	 */
	public Record statisticFctZb(String  sqlWhere){
		String sql="";
		sql+=" select count(1) as num from bigdata_fct_zb fct_zb where 1=1 "+sqlWhere;
		return Db.findFirst(sql);
	}
	/**
	 * 新闻量同比环比计算
	 * @param sqlWhere
	 * @return
	 */
	public Record fctZbHbTb(String  sqlWhere,String startDate, String endDate){
		StringBuffer sql=new StringBuffer();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(StringUtils.isBlank(startDate)){
			startDate=sdf.format(new Date());
		}
		if(StringUtils.isBlank(endDate)){
			endDate=sdf.format(new Date());
		}
		String hb=SqlUtil.getHuanBiDate(startDate, endDate);
		String tb=SqlUtil.getTongBiDate(startDate, endDate);
		startDate=startDate.substring(0,7);
		String hbDate=startDate=hb.split(",")[0].substring(0,7);
		String tbDate=startDate=tb.split(",")[0].substring(0,7);
		String selectSql=" select *,truncate((a.currentmonth-b.premonth)/b.premonth*100,2) as tb,truncate((a.currentmonth-c.preyear)/c.preyear*100,2) as hb from ";
		String fromSql=" from bigdata_fct_zb fct_zb where 1=1 "+sqlWhere;
		sql.append(selectSql);
		sql.append(" (select count(1) as currentMonth ").append(fromSql);
		sql.append(" and date_format(fct_zb.created_time,\"%Y-%m\")=\""+startDate+"\" )a ");
		sql.append(" join ");
		
		sql.append(" (select count(1) as preMonth ").append(fromSql);
		sql.append(" and date_format(fct_zb.created_time,\"%Y-%m\")=\""+hbDate+"\")b ");
		sql.append(" join ");
		
		sql.append(" (select count(1) as preYear ").append(fromSql);
		sql.append(" and date_format(fct_zb.created_time,\"%Y-%m\")=\""+tbDate+"\")c ");
		sql.append(" on 1=1 ");
		return Db.findFirst(sql.toString());
	}
	/**
	 * 
	 * @time   2017年7月14日 上午11:13:46
	 * @author zuoqb
	 * @todo   新闻量Top6(按照部门分组)
	 */
	/*public List<Record> fctZbTop6(String  sqlWhere,String limit){
		String sql="";
		sql+=" select distinct d_dept.name, ";
		sql+=" (select count(1) from bigdata_fct_zb fct_zb where fct_zb.source_dept_ids like concat('%,',d_dept.id,',%')  )as count  ";
		sql+=" from bigdata_d_dept d_dept where d_dept.pid!=0  "+sqlWhere+" limit "+limit;
		return Db.find(sql);
	}*/
	public List<Record> fctZbTop6(String  sqlWhere,String limit){
		String sql="";
		sql+=" select distinct d_dept.name, ";
		sql+=" (select count(1) from bigdata_fct_zb fct_zb where fct_zb.source_dept_ids=d_dept.id "+sqlWhere+" )as count  ";
		sql+=" from bigdata_d_dept d_dept where d_dept.pid!=0   limit "+limit;
		return Db.find(sql);
	}
	/**
	 * 
	 * @time   2017年7月14日 上午11:13:46
	 * @author zuoqb
	 * @todo   新闻量Top6(按照年份分组)
	 */
	public List<Record> fctZbByYear(String  sqlWhere,int startYear,int endYear){
		String sql="";
		sql+=" select ifnull(m.count,'0') as count,d.years as name from (select * from bigdata_date where years>'"+startYear+"' and years<='"+endYear+"') d left join (  ";
		sql+=" select count(1) as count,date_format(fct_zb.created_time,'%Y') as name ";
		sql+="  from bigdata_fct_zb fct_zb where 1=1 "+sqlWhere;
		sql+=" group by date_format(fct_zb.created_time,'%Y')  ) m ";
		sql+="  on d.years=m.name order by d.years desc ";
		return Db.find(sql);
	}
	
	
	/**
	 * 
	 * @time   2017年7月17日 上午10:43:31
	 * @author zuoqb
	 * @todo   新闻分析-列表数据
	 */
	public Page<Record> fctZbAnalysis(String  sqlWhere,int pageNum,int pageSize){
		//第一步统计基础信息（评价以及标题等（一对多的部门发稿人前方记者除外））
		StringBuffer selectSql=new StringBuffer();
		StringBuffer fromSql=new StringBuffer();
		selectSql.append(" select a.* ");
		fromSql.append(" from (select   fct_zb.zb_id,date_format(fct_zb.created_time,'%Y%m%d') as date,fct_zb.zb_name, ");
		fromSql.append(" (select  group_concat(distinct  fct_eval.use_type_id separator '+') from bigdata_fct_eval fct_eval where fct_eval.zb_id=fct_zb.zb_id ) as useType, ");
		fromSql.append(" (select  group_concat(distinct  fct_eval.use_level_id separator '+') from bigdata_fct_eval fct_eval where fct_eval.zb_id=fct_zb.zb_id ) as useLevel,  ");
		fromSql.append(" (select  group_concat(distinct  fct_eval.have_pishi separator '+') from bigdata_fct_eval fct_eval where fct_eval.zb_id=fct_zb.zb_id ) as hasPiShi,  ");
		fromSql.append(" (select  group_concat(distinct  fct_eval.office_id separator '+') from bigdata_fct_eval fct_eval where fct_eval.zb_id=fct_zb.zb_id ) as office ");
		fromSql.append("  from bigdata_fct_zb fct_zb  ");
		fromSql.append("  left JOIN bigdata_r_zb_origin zb_origin on zb_origin.zb_id=fct_zb.zb_id ");
		fromSql.append("  join bigdata_fct_origin fct_origin on fct_origin.id=zb_origin.origin_id ");
		fromSql.append(sqlWhere);
		fromSql.append(" group by fct_zb.zb_id order by fct_zb.created_time desc)a ");
		Page<Record> pager=Db.paginate(pageNum, pageSize, selectSql.toString(), fromSql.toString());
		for(Record r:pager.getList()){
			List<Record> children=fctZbAnalysisExtra(r.get("zb_id"));
			HashMap<String, Object> deptMap=new HashMap<String, Object>();
			HashMap<String, Object> creatorNameMap=new HashMap<String, Object>();
			HashMap<String, Object> souceCodeNameMap=new HashMap<String, Object>();
			for(Record c:children){
				String deptName=c.getStr("deptName");
				deptMap.put(deptName, deptName);
				String creatorName=c.getStr("creatorName");
				creatorNameMap.put(creatorName,creatorName);
				String souceCodeName=c.getStr("souceCodeName");
				souceCodeNameMap.put(souceCodeName, souceCodeName);
			}
			r.set("deptName", joinStrByMap(deptMap));
			r.set("creatorName", joinStrByMap(creatorNameMap));
			r.set("souceCodeName", joinStrByMap(souceCodeNameMap));
			//是否批示处理
			String  hasPiShi=r.getStr("hasPiShi");
			if(StringUtils.isBlank(hasPiShi)||"0".equals(hasPiShi)){
				hasPiShi="暂无批示";
			}else if(hasPiShi.indexOf("1")!=-1){
				hasPiShi="部分批示";
			}
			r.set("hasPiShi",hasPiShi);
			r.set("children", children);
		}
		return pager;
	}
	/**
	 * 
	 * @time   2017年7月18日 下午1:33:29
	 * @author zuoqb
	 * @todo   根据新闻主键查询扩展信息  一对多的部门发稿人前方记者
	 */
	public List<Record> fctZbAnalysisExtra(Object  zbId){
		StringBuffer sql=new StringBuffer();
		sql.append(" select ");
		sql.append(" zb_origin.id,d_dept.name as deptName,d_creator.name as creatorName,d_creator1.name as souceCodeName ");
		sql.append(" from  bigdata_r_zb_origin zb_origin  ");
		sql.append(" right JOIN bigdata_fct_origin fct_origin on fct_origin.id=zb_origin.origin_id ");
		sql.append(" left join bigdata_d_dept d_dept on d_dept.id=fct_origin.dept_id ");
		sql.append(" left join bigdata_d_creator d_creator on d_creator.id=fct_origin.creator_id ");
		sql.append(" left join bigdata_d_creator d_creator1  on  d_creator1.id=fct_origin.sourceCode");
		sql.append(" where zb_origin.zb_id='"+zbId+"' ");
		return Db.find(sql.toString());
	}
	@SuppressWarnings("rawtypes")
	public String joinStrByMap(HashMap<String, Object> hashMap){
		Collection temp = hashMap.values();
		String str = "";
		for(Object o: temp){
			str += o+"+";
		}
		if(StringUtils.isNotBlank(str)&&str.length()>0){
			str=str.substring(0,str.length()-1);
		}
		return str;
	}
}
