package com.conference.admin.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.conference.common.BaseModel;
import com.conference.util.sys.SqlUtil;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class FctOrigin extends BaseModel<FctOrigin>{
	private static final long serialVersionUID = -8811409081417389372L;
	public static final FctOrigin dao=new FctOrigin();
	/**
	 * 素材量统计
	 * @param sqlWhere
	 * @return
	 */
	public Record statisticOrigin(String  sqlWhere){
		String sql="";
		sql+=" select count(1) as num from bigdata_fct_origin fct_origin where 1=1 "+sqlWhere;
		return Db.findFirst(sql);
	}
	/**
	 * 同比环比计算
	 * @param sqlWhere
	 * @return
	 */
	public Record originHbTb(String  sqlWhere,String startDate, String endDate){
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
		String fromSql=" from bigdata_fct_origin fct_origin where 1=1 "+sqlWhere;
		sql.append(selectSql);
		sql.append(" (select count(1) as currentMonth ").append(fromSql);
		sql.append(" and date_format(fct_origin.created_time,\"%Y-%m\")=\""+startDate+"\" )a ");
		sql.append(" join ");
		
		sql.append(" (select count(1) as preMonth ").append(fromSql);
		sql.append(" and date_format(fct_origin.created_time,\"%Y-%m\")=\""+hbDate+"\")b ");
		sql.append(" join ");
		
		sql.append(" (select count(1) as preYear ").append(fromSql);
		sql.append(" and date_format(fct_origin.created_time,\"%Y-%m\")=\""+tbDate+"\")c ");
		sql.append(" on 1=1 ");
		return Db.findFirst(sql.toString());
	}

	/**
	 * 素材转化率
	 * @param sqlWhere
	 * @return
	 */
	public Record statisticOriginRate(String  sqlWhere){
		StringBuffer sql=new StringBuffer();
		sql.append(" select a.source_num,b.news_num,truncate(b.news_num/a.source_num*100,2) as rate from( ");
		sql.append(" select count(1) as source_num from bigdata_fct_origin fct_origin  ");
		sql.append("  where 1=1 ");
		sql.append(sqlWhere);
		sql.append(" )a ");
		sql.append(" join ");
		sql.append(" (select count(1) as news_num from bigdata_fct_origin fct_origin right join bigdata_r_zb_origin zb_origin ");
		sql.append("  on fct_origin.id=zb_origin.origin_id ");
		sql.append("  where 1=1 ");
		sql.append(sqlWhere);
		sql.append(" )b ");
		return Db.findFirst(sql.toString());
	}
	
	/**
	 * 素材转化率同比环比计算
	 * @param sqlWhere
	 * @return
	 */
	public Record originRateHbTb(String  sqlWhere,String startDate, String endDate){
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
		String fromSql=" from bigdata_fct_origin fct_origin where 1=1 "+sqlWhere;
		String fromSqlJoin=" from bigdata_fct_origin fct_origin right join bigdata_r_zb_origin zb_origin on fct_origin.id=zb_origin.origin_id where 1=1 "+sqlWhere;
		sql.append(selectSql);
		sql.append(" (select count(1) as currentMonth ").append(fromSqlJoin);
		sql.append(" and date_format(fct_origin.created_time,\"%Y-%m\")=\""+startDate+"\" )a ");
		sql.append(" join ");
		
		sql.append(" (select count(1) as preMonth ").append(fromSqlJoin);
		sql.append(" and date_format(fct_origin.created_time,\"%Y-%m\")=\""+hbDate+"\")b ");
		sql.append(" join ");
		
		sql.append(" (select count(1) as preYear ").append(fromSqlJoin);
		sql.append(" and date_format(fct_origin.created_time,\"%Y-%m\")=\""+tbDate+"\")c ");
		sql.append(" on 1=1 ");
		return Db.findFirst(sql.toString());
	}
	
	
	/**
	 * 
	 * @time   2017年7月14日 上午11:13:46
	 * @author zuoqb
	 * @todo   素材量Top6(按照部门分组)
	 */
	public List<Record> fctOriginTop6(String  sqlWhere,String limit){
		String sql="";
		sql+=" select count(1) as count,d_dept.name as name from bigdata_fct_origin fct_origin left join bigdata_d_dept d_dept on fct_origin.dept_id=d_dept.id ";
		sql+=" where 1=1 "+sqlWhere;
		sql+=" group by fct_origin.dept_id,d_dept.name order by count(fct_origin.dept_id)  limit "+limit;
		return Db.find(sql);
	}
	/**
	 * 
	 * @time   2017年7月14日 上午11:13:46
	 * @author zuoqb
	 * @todo   新闻量Top6(按照年份分组)
	 */
	public List<Record> fctOriginByYear(String  sqlWhere,int startYear,int endYear){
		String sql="";
		sql+=" select ifnull(m.count,'0') as count,d.years as name from (select * from bigdata_date where years>'"+startYear+"' and years<='"+endYear+"') d left join (  ";
		sql+=" select count(1) as count,date_format(fct_origin.created_time,'%Y') as name ";
		sql+="  from `bigdata_fct_origin` fct_origin where 1=1 "+sqlWhere;
		sql+=" group by date_format(fct_origin.created_time,'%Y')  ) m ";
		sql+="  on d.years=m.name order by d.years desc ";
		return Db.find(sql);
	}
	/**
	 * 
	 * @time   2017年7月17日 上午10:43:31
	 * @author zuoqb
	 * @todo   素材分析-列表数据
	 */
	public Page<Record> originAnalysis(String  sqlWhere,int pageNum,int pageSize){
		//第一步统计部门
		StringBuffer selectSql=new StringBuffer();
		StringBuffer fromSql=new StringBuffer();
		selectSql.append(" select a.* ");
		fromSql.append(" from (select count(1) as count,dept_id,from_dept,d_dept.`name` as deptname,d.name as companyname, ");
		fromSql.append(" date_format(created_time,'%Y%m') as date ");
		fromSql.append(" from bigdata_fct_origin fct_origin  ");
		fromSql.append(" left join bigdata_d_dept d_dept on d_dept.id=fct_origin.dept_id  ");
		fromSql.append(" left join bigdata_d_dept d on d.id=fct_origin.from_dept where 1=1 ");
		fromSql.append(sqlWhere);
		fromSql.append(" group by dept_id,date_format(fct_origin.created_time,'%y%m'),from_dept ");
		fromSql.append("  order by date_format(fct_origin.created_time,'%Y%m') desc )a ");
		Page<Record> pager=Db.paginate(pageNum, pageSize, selectSql.toString(), fromSql.toString());
		for(Record r:pager.getList()){
			List<Record> children=originAnalysisCreator(sqlWhere,r.get("date"), r.get("from_dept"), r.get("dept_id"));
			r.set("children", children);
		}
		return pager;
	}
	/**
	 * 
	 * @time   2017年7月17日 上午11:06:45
	 * @author zuoqb
	 * @todo   根据日期 大单位 部门统计发稿人
	 */
	public List<Record> originAnalysisCreator(String  sqlWhere,Object  date,Object  fromDept,Object  deptId){
		StringBuffer sql=new StringBuffer();
		sql.append(" select fct_origin.creator_id,d_creator.name as creatorName,count(DISTINCT fct_origin.sourceCode)AS sourceCodeNum,");
		sql.append(" count(fct_origin.id) as orgNum, ");
		sql.append(" count(fct_zb.zb_id) as zbnum,format(count(fct_zb.zb_id)/count(fct_origin.id)*100,2) as rate, ");
		sql.append(" (select  group_concat(distinct  fct_eval.use_type_id separator '+') from bigdata_fct_eval fct_eval where fct_eval.zb_id=fct_zb.zb_id ) as useType, ");
		sql.append(" (select  group_concat(distinct  fct_eval.use_level_id separator '+') from bigdata_fct_eval fct_eval where fct_eval.zb_id=fct_zb.zb_id ) as useLevel, ");
		sql.append(" (select count(1) from bigdata_fct_eval fct_eval where fct_eval.zb_id=fct_zb.zb_id and fct_eval.have_pishi='1') as hasPiShiNum ");
		sql.append(" from bigdata_fct_origin fct_origin  ");
		sql.append(" left join bigdata_r_zb_origin r_zb_origin on r_zb_origin.origin_id=fct_origin.id ");
		sql.append(" left join bigdata_fct_zb fct_zb on fct_zb.zb_id=r_zb_origin.zb_id ");
		sql.append(" left join bigdata_d_creator d_creator on d_creator.id=fct_origin.sourceCode ");
		sql.append("  where ");
		sql.append(" date_format(fct_origin.created_time,'%Y%m')='"+date+"' ");
		sql.append(" and fct_origin.from_dept='"+fromDept+"' ");
		sql.append(" and fct_origin.dept_id='"+deptId+"' ");
		sql.append(sqlWhere);
		sql.append(" group by fct_origin.creator_id ");
		List<Record> list=Db.find(sql.toString());
		
		//根据发稿人统计素材转化率 评价等级等信息
		StringBuffer sb=new StringBuffer();
		sb.append(" select fct_origin.sourceCode,d_creator.name as sourceCodeName, count(fct_origin.id) as sourcenNum,");
		sb.append(" count(fct_zb.zb_id) as zbnum,format(count(fct_zb.zb_id)/count(fct_origin.id)*100,2) as rate, ");
		sb.append(" (select  group_concat(distinct  fct_eval.use_type_id separator '+') from bigdata_fct_eval fct_eval where fct_eval.zb_id=fct_zb.zb_id ) as useType, ");
		sb.append(" (select  group_concat(distinct  fct_eval.use_level_id separator '+') from bigdata_fct_eval fct_eval where fct_eval.zb_id=fct_zb.zb_id ) as useLevel, ");
		sb.append(" (select count(1) from bigdata_fct_eval fct_eval where fct_eval.zb_id=fct_zb.zb_id and fct_eval.have_pishi='1') as hasPiShiNum ");
		sb.append(" from bigdata_fct_origin fct_origin  ");
		sb.append(" left join bigdata_r_zb_origin r_zb_origin on r_zb_origin.origin_id=fct_origin.id ");
		sb.append(" left join bigdata_fct_zb fct_zb on fct_zb.zb_id=r_zb_origin.zb_id ");
		sb.append(" left join bigdata_d_creator d_creator on d_creator.id=fct_origin.sourceCode ");
		sb.append("  where ");
		sb.append(" date_format(fct_origin.created_time,'%Y%m')='"+date+"' ");
		sb.append(" and fct_origin.from_dept='"+fromDept+"' ");
		sb.append(" and fct_origin.dept_id='"+deptId+"' ");
		sb.append(sqlWhere);
		String creatorSql="";
		for(Record r:list){
			creatorSql=" and fct_origin.creator_id='"+r.get("creator_id")+"' group by fct_origin.sourceCode ";
			List<Record> children=Db.find(sb.toString()+creatorSql);
			r.set("children", children);
		}
		return list;
	}
}
