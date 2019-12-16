package com.conference.admin.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.conference.common.BaseModel;
import com.conference.util.sys.SqlUtil;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class FctZb extends BaseModel<FctZb> {
	private static final long serialVersionUID = -8811409081417389372L;
	public static final FctZb dao = new FctZb();
	public static final String[] levels={"优","良","中"};

	/**
	 * 新闻量统计
	 * 
	 * @param sqlWhere
	 * @return
	 */
	public Record statisticFctZb(String sqlWhere) {
		String sql = "";
		sql += " select count(1) as num from bigdata_fct_zb fct_zb where 1=1 "
				+ sqlWhere;
		return Db.findFirst(sql);
	}

	/**
	 * 新闻量同比环比计算
	 * 
	 * @param sqlWhere
	 * @return
	 */
	public Record fctZbHbTb(String sqlWhere, String startDate, String endDate) {
		StringBuffer sql = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StringUtils.isBlank(startDate)) {
			startDate = sdf.format(new Date());
		}
		if (StringUtils.isBlank(endDate)) {
			endDate = sdf.format(new Date());
		}
		String hb = SqlUtil.getHuanBiDate(startDate, endDate);
		String tb = SqlUtil.getTongBiDate(startDate, endDate);
		startDate = startDate.substring(0, 7);
		String hbDate = startDate = hb.split(",")[0].substring(0, 7);
		String tbDate = startDate = tb.split(",")[0].substring(0, 7);
		String selectSql = " select *,truncate((a.currentmonth-b.premonth)/b.premonth*100,2) as tb,truncate((a.currentmonth-c.preyear)/c.preyear*100,2) as hb from ";
		String fromSql = " from bigdata_fct_zb fct_zb where 1=1 " + sqlWhere;
		sql.append(selectSql);
		sql.append(" (select count(1) as currentMonth ").append(fromSql);
		sql.append(" and date_format(fct_zb.created_time,\"%Y-%m\")=\""
				+ startDate + "\" )a ");
		sql.append(" join ");

		sql.append(" (select count(1) as preMonth ").append(fromSql);
		sql.append(" and date_format(fct_zb.created_time,\"%Y-%m\")=\""
				+ hbDate + "\")b ");
		sql.append(" join ");

		sql.append(" (select count(1) as preYear ").append(fromSql);
		sql.append(" and date_format(fct_zb.created_time,\"%Y-%m\")=\""
				+ tbDate + "\")c ");
		sql.append(" on 1=1 ");
		return Db.findFirst(sql.toString());
	}

	/**
	 * 
	 * @time 2017年7月14日 上午11:13:46
	 * @author zuoqb
	 * @todo 新闻量Top6(按照部门分组)
	 */
	/*
	 * public List<Record> fctZbTop6(String sqlWhere,String limit){ String
	 * sql=""; sql+=" select distinct d_dept.name, "; sql+=
	 * " (select count(1) from bigdata_fct_zb fct_zb where fct_zb.source_dept_ids like concat('%,',d_dept.id,',%')  )as count  "
	 * ;
	 * sql+=" from bigdata_d_dept d_dept where d_dept.pid!=0  "+sqlWhere+" limit "
	 * +limit; return Db.find(sql); }
	 */
	public List<Record> fctZbTop6(String sqlWhere, String limit) {
		String sql = "";
		sql += " select distinct d_dept.name, ";
		sql += " (select count(1) from bigdata_fct_zb fct_zb where fct_zb.source_dept_ids=d_dept.id "
				+ sqlWhere + " )as count  ";
		sql += " from bigdata_d_dept d_dept where d_dept.pid!=0   limit "
				+ limit;
		return Db.find(sql);
	}

	/**
	 * 
	 * @time 2017年7月14日 上午11:13:46
	 * @author zuoqb
	 * @todo 新闻量Top6(按照年份分组)
	 */
	public List<Record> fctZbByYear(String sqlWhere, int startYear, int endYear) {
		String sql = "";
		sql += " select ifnull(m.count,'0') as count,d.years as name from ("+SqlUtil.dealYearSql(startYear, endYear)+") d left join (  ";
		sql += " select count(1) as count,date_format(fct_zb.created_time,'%Y') as name ";
		sql += "  from bigdata_fct_zb fct_zb where 1=1 " + sqlWhere;
		sql += " group by date_format(fct_zb.created_time,'%Y')  ) m ";
		sql += "  on d.years=m.name order by d.years desc ";
		return Db.find(sql);
	}

	/**
	 * 
	 * @time 2017年7月17日 上午10:43:31
	 * @author zuoqb
	 * @todo 新闻分析-列表数据
	 */
	public Page<Record> fctZbAnalysis(String sqlWhere, int pageNum, int pageSize) {
		// 第一步统计基础信息（评价以及标题等（一对多的部门发稿人前方记者除外））
		StringBuffer selectSql = new StringBuffer();
		StringBuffer fromSql = new StringBuffer();
		selectSql.append(" select a.* ");
		fromSql.append(" from (select   fct_zb.zb_id,date_format(fct_zb.created_time,'%Y%m%d') as date,fct_zb.zb_name, ");
		fromSql.append(" (select  group_concat(distinct  fct_eval.use_type_id separator '+') from bigdata_fct_eval fct_eval where fct_eval.zb_id=fct_zb.zb_id ) as useType, ");
		fromSql.append(" (select  group_concat(distinct  fct_eval.use_level_id separator '+') from bigdata_fct_eval fct_eval where fct_eval.zb_id=fct_zb.zb_id ) as useLevel,  ");
		fromSql.append(" (select  group_concat(distinct  fct_eval.have_pishi separator '+') from bigdata_fct_eval fct_eval where fct_eval.zb_id=fct_zb.zb_id ) as hasPiShi,  ");
		fromSql.append(" (select  group_concat(distinct  fct_eval.office_id separator '+') from bigdata_fct_eval fct_eval where fct_eval.zb_id=fct_zb.zb_id ) as office ");
		fromSql.append("  from bigdata_fct_zb fct_zb  ");
		fromSql.append("  left join bigdata_r_zb_origin zb_origin on zb_origin.zb_id=fct_zb.zb_id ");
		fromSql.append("  join bigdata_fct_origin fct_origin on fct_origin.id=zb_origin.origin_id ");
		fromSql.append(sqlWhere);
		fromSql.append(" group by fct_zb.zb_id order by fct_zb.created_time desc)a ");
		Page<Record> pager = Db.paginate(pageNum, pageSize,
				selectSql.toString(), fromSql.toString());
		for (Record r : pager.getList()) {
			List<Record> children = fctZbAnalysisExtra(r.get("zb_id"));
			HashMap<String, Object> deptMap = new HashMap<String, Object>();
			HashMap<String, Object> creatorNameMap = new HashMap<String, Object>();
			HashMap<String, Object> souceCodeNameMap = new HashMap<String, Object>();
			for (Record c : children) {
				String deptName = c.getStr("deptName");
				deptMap.put(deptName, deptName);
				String creatorName = c.getStr("creatorName");
				creatorNameMap.put(creatorName, creatorName);
				String souceCodeName = c.getStr("souceCodeName");
				souceCodeNameMap.put(souceCodeName, souceCodeName);
			}
			r.set("deptName", joinStrByMap(deptMap));
			r.set("creatorName", joinStrByMap(creatorNameMap));
			r.set("souceCodeName", joinStrByMap(souceCodeNameMap));
			Record d=Db.findFirst("SELECT d_creator.`name` FROM `bigdata_fct_zb`  fct_zb LEFT JOIN bigdata_d_creator d_creator on d_creator.id=fct_zb.source_code where fct_zb.zb_id='"+r.get("zb_id")+"'");
			if(d!=null){
				r.set("souceCodeName", d.get("name"));
			}else{
				r.set("souceCodeName", "");
			}
			// 是否批示处理
			String hasPiShi = r.getStr("hasPiShi");
			if (StringUtils.isBlank(hasPiShi) || "0".equals(hasPiShi)) {
				hasPiShi = "暂无批示";
			} else if (hasPiShi.indexOf("1") != -1) {
				hasPiShi = "部分批示";
			}
			r.set("hasPiShi", hasPiShi);
			r.set("children", children);
		}
		return pager;
	}

	/**
	 * 
	 * @time 2017年7月18日 下午1:33:29
	 * @author zuoqb
	 * @todo 根据新闻主键查询扩展信息 一对多的部门发稿人前方记者
	 */
	public List<Record> fctZbAnalysisExtra(Object zbId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		sql.append(" zb_origin.id,d_dept.name as deptName,d_creator.name as creatorName,d_creator1.name as souceCodeName ");
		sql.append(" from  bigdata_r_zb_origin zb_origin  ");
		sql.append(" right join bigdata_fct_origin fct_origin on fct_origin.id=zb_origin.origin_id ");
		sql.append(" left join bigdata_d_dept d_dept on d_dept.id=fct_origin.dept_id ");
		sql.append(" left join bigdata_d_creator d_creator on d_creator.id=fct_origin.creator_id ");
		sql.append(" left join bigdata_d_creator d_creator1  on  d_creator1.id=fct_origin.sourceCode");
		sql.append(" where zb_origin.zb_id='" + zbId + "' ");
		return Db.find(sql.toString());
	}



	/**
	 * 按照日期分组 统计新闻（使用方式）
	 */
	public Page<Record>  fctZbUseModelGroupByDate(String sqlWhere,String selectDeptIds, int pageNum, int pageSize) {
		//使用方式
		List<Record> useTypes=FctEval.dao.fctEvalUseType(sqlWhere);
		String selectSql=" select a.* ";
		StringBuffer fromSql = new StringBuffer();
		fromSql.append(" from ( select date_format(fct_zb.created_time,'%Y%m%d') as date,count(1) as count,group_concat(distinct fct_zb.source_dept_ids separator '') as source_dept_ids  from bigdata_fct_zb  fct_zb  ");
		fromSql.append(" where 1=1 " + sqlWhere);
		fromSql.append(" group by date_format(fct_zb.created_time,'%Y%m%d') order by ");
		fromSql.append(" date_format(fct_zb.created_time,'%Y%m%d') desc )a ");
		Page<Record> pager = Db.paginate(pageNum, pageSize,
				selectSql.toString(), fromSql.toString());
		// 处理多部门信息 进行手动拆分
		Map<String, Record> map = Dept.dao.getAllDeptsMap();
		for (Record r : pager.getList()) {
			//处理部门
			String deptIds = r.getStr("source_dept_ids");
			List<Record> finList = new ArrayList<Record>();
			if (StringUtils.isNotBlank(deptIds)) {
				String[] ids = removeRepeated(deptIds.split(","),selectDeptIds);
				for(String deptId:ids){
					Record record=new Record();
					Record dept=map.get(deptId);
					String date=r.get("date")+"";
					record.set("date", date);
					record.set("deptName", dept.get("name"));
					record.set("deptId", deptId);
					//根据时间 部门统计不同使用方式下 评价等级数量
					List<Record> useTypeNum=useLevelByTypeUseModel(sqlWhere,date , deptId, useTypes);
					record.set("useType", useTypeNum);
					
					//根据时间 部门统计发稿人
					List<Record>  creators=creatorByDeptUseModel(sqlWhere, date, deptId, useTypes);
					record.set("creators", creators);
					finList.add(record);
				}
			};
			r.set("children", finList);
		}
		return pager;
	}
	/**
	 * 
	 * @time   2017年7月25日 上午11:25:01
	 * @author zuoqb
	 * @todo   根据时间 部门统计发稿人
	 */
	public List<Record> creatorByDeptUseModel(String sqlWhere,String date,String deptId,List<Record> useType){
		List<Record> creator=new ArrayList<Record>();
		StringBuffer sb=new StringBuffer();
		sb.append(" select distinct d_creator.`name`,d_creator.id from bigdata_fct_origin  fct_origin  ");
		sb.append(" left join bigdata_d_creator d_creator on d_creator.id=fct_origin.creator_id ");
		sb.append(" where fct_origin.id in( ");
		sb.append(" select distinct r_zb_origin.origin_id from bigdata_r_zb_origin r_zb_origin where r_zb_origin.zb_id in( ");
		sb.append(" select zb_id from bigdata_fct_zb fct_zb  ");
		sb.append(" where 	date_format(fct_zb.created_time,'%Y%m%d')='"+date+"'");
		sb.append(" and fct_zb.source_dept_ids like '%,"+deptId+",%'");
		sb.append(sqlWhere);
		sb.append(" )) ");
		creator=Db.find(sb.toString());
		for(Record r:creator){
			List<Record> c= useLevelByTypeUseModel(sqlWhere, date, deptId, r.get("id")+"", useType);
			r.set("useType", c);
		}
		return creator;
		
	}
	
	/**
	 * 
	 * @time   2017年7月25日 上午11:25:01
	 * @author zuoqb
	 * @todo   根据时间 部门 发稿人以及使用方式统计使用级别数量
	 */
	public List<Record> useLevelByTypeUseModel(String sqlWhere,String date,String deptId,String creatorId,List<Record> useType){
		List<Record> list=new ArrayList<Record>();
		for(Record r:useType){
			Record mRecord=new Record();
			mRecord.set("use_type_id", r.getStr("use_type_id"));
			String sql="";
			sql+=" select ifnull(a.count,0) as count,c.* from (select fct_eval.use_level_id,count(1) as count, ";
			sql+=" case fct_eval.use_level_id when '优' then '0' when '良' then '1' else '2' end as sor ";
			sql+=" from bigdata_fct_eval fct_eval ";
			sql+=" where fct_eval.zb_id in(select fct_zb.zb_id from bigdata_fct_zb fct_zb where date_format(fct_zb.created_time,'%Y%m%d')='"+date+"' and fct_zb.source_dept_ids like '%,"+deptId+",%' and fct_zb.creator_ids like '%,"+creatorId+",%'  "+sqlWhere+" ) ";
			sql+=" and fct_eval.use_type_id='"+r.getStr("use_type_id")+"' ";
			
			sql+=" group by fct_eval.use_level_id )a  ";
			sql+=" right join ( ";
			sql+=" select b.* from (select '优' as na from dual  ";
			sql+=" union select '良' as na from dual  ";
			sql+=" union select '中' as na from dual )b ";
			sql+=" )c on c.na=a.use_level_id ";
			sql+=" order by a.sor ";
			List<Record> l=Db.find(sql);
			mRecord.set("useLevel", l);
			list.add(mRecord);
		}
		return list;
	}
	
	/**
	 * 
	 * @time   2017年7月25日 上午11:25:01
	 * @author zuoqb
	 * @todo   根据时间 部门以及使用方式统计使用级别数量
	 */
	public List<Record> useLevelByTypeUseModel(String sqlWhere,String date,String deptId,List<Record> useType){
		List<Record> list=new ArrayList<Record>();
		for(Record r:useType){
			Record mRecord=new Record();
			mRecord.set("use_type_id", r.getStr("use_type_id"));
			String sql="";
			sql+=" select ifnull(a.count,0) as count,c.* from (select fct_eval.use_level_id,count(1) as count, ";
			sql+=" case fct_eval.use_level_id when '优' then '0' when '良' then '1' else '2' end as sor ";
			sql+=" from bigdata_fct_eval fct_eval ";
			sql+=" where fct_eval.zb_id in(select fct_zb.zb_id from bigdata_fct_zb fct_zb where date_format(fct_zb.created_time,'%Y%m%d')='"+date+"' and fct_zb.source_dept_ids like '%,"+deptId+",%' "+sqlWhere+" ) ";
			sql+=" and fct_eval.use_type_id='"+r.getStr("use_type_id")+"' ";
			sql+=" group by fct_eval.use_level_id )a  ";
			sql+=" right join ( ";
			sql+=" select b.* from (select '优' as na from dual  ";
			sql+=" union select '良' as na from dual  ";
			sql+=" union select '中' as na from dual )b ";
			sql+=" )c on c.na=a.use_level_id ";
			sql+=" order by a.sor ";
			List<Record> l=Db.find(sql);
			mRecord.set("useLevel", l);
			list.add(mRecord);
		};
		return list;
	}
	
	/**********以上是使用新闻方式统计 END***********/
	
	
	
	
	/**********以上下是使用级别统计 START***********/
	/**
	 * 按照日期分组 统计新闻（评价级别）
	 */
	public Page<Record>  fctZbUseLevelGroupByDate(String sqlWhere,String selectDeptIds, int pageNum, int pageSize) {
		//使用方式
		List<Record> useTypes=FctEval.dao.fctEvalUseType(sqlWhere);
		String selectSql=" select a.* ";
		StringBuffer fromSql = new StringBuffer();
		fromSql.append(" from ( select date_format(fct_zb.created_time,'%Y%m%d') as date,count(1) as count,group_concat(distinct fct_zb.source_dept_ids separator '') as source_dept_ids  from bigdata_fct_zb  fct_zb  ");
		fromSql.append(" where 1=1 " + sqlWhere);
		fromSql.append(" group by date_format(fct_zb.created_time,'%Y%m%d') order by ");
		fromSql.append(" date_format(fct_zb.created_time,'%Y%m%d') desc )a ");
		Page<Record> pager = Db.paginate(pageNum, pageSize,
				selectSql.toString(), fromSql.toString());
		// 处理多部门信息 进行手动拆分
		Map<String, Record> map = Dept.dao.getAllDeptsMap();
		for (Record r : pager.getList()) {
			//处理部门
			String deptIds = r.getStr("source_dept_ids");
			List<Record> finList = new ArrayList<Record>();
			if (StringUtils.isNotBlank(deptIds)) {
				String[] ids = removeRepeated(deptIds.split(","),selectDeptIds);
				for(String deptId:ids){
					Record record=new Record();
					Record dept=map.get(deptId);
					String date=r.get("date")+"";
					record.set("date", date);
					record.set("deptName", dept.get("name"));
					record.set("deptId", deptId);
					// 根据时间 部门以及使用级别统计使用方式数量
					List<Record> useTypeNum=useLevelByTypeUseLevel(sqlWhere,date , deptId, useTypes);
					record.set("useType", useTypeNum);
					
					//根据时间 部门统计发稿人
					List<Record>  creators=creatorByDeptUseLevel(sqlWhere, date, deptId, useTypes);
					record.set("creators", creators);
					finList.add(record);
				}
			};
			r.set("children", finList);
		}
		return pager;
	}
	/**
	 * 
	 * @time   2017年7月25日 上午11:25:01
	 * @author zuoqb
	 * @todo   根据时间 部门以及使用级别统计使用方式数量
	 */
	public List<Record> useLevelByTypeUseLevel(String sqlWhere,String date,String deptId,List<Record> useType){
		List<Record> list=new ArrayList<Record>();
		String rightJoin="";
		for(int x=0;x<useType.size();x++){
			if(x==useType.size()-1){
				rightJoin+=" select '"+useType.get(x).getStr("use_type_id")+"' as na from dual ";
			}else{
				rightJoin+=" select '"+useType.get(x).getStr("use_type_id")+"' as na from dual union ";
			}
		};
		
		for(String level:levels){
			Record mRecord=new Record();
			mRecord.set("use_level",level);
			String sql="";
			sql+="  select ifnull(a.count,0) as count,c.* from (select fct_eval.use_type_id,count(1) as count ";
			sql+=" from bigdata_fct_eval fct_eval ";
			sql+=" where fct_eval.zb_id in(select fct_zb.zb_id from bigdata_fct_zb fct_zb where date_format(fct_zb.created_time,'%Y%m%d')='"+date+"' and fct_zb.source_dept_ids like '%,"+deptId+",%' "+sqlWhere+" ) ";
			sql+=" and fct_eval.use_level_id='"+level+"' ";
			sql+=" group by fct_eval.use_level_id )a  ";
			sql+=" right join ( ";
			sql+=" select b.* from (  ";
			sql+= rightJoin;
			sql+="  )b ";
			sql+=" )c on c.na=a.use_type_id ";
			List<Record> l=Db.find(sql);
			mRecord.set("useLevel", l);
			list.add(mRecord);
		};
		return list;
	}
	/**
	 * 
	 * @time   2017年7月25日 上午11:25:01
	 * @author zuoqb
	 * @todo   根据时间 部门统计发稿人(评价等级)
	 */
	public List<Record> creatorByDeptUseLevel(String sqlWhere,String date,String deptId,List<Record> useType){
		List<Record> creator=new ArrayList<Record>();
		StringBuffer sb=new StringBuffer();
		sb.append(" select distinct d_creator.`name`,d_creator.id from bigdata_fct_origin  fct_origin  ");
		sb.append(" left join bigdata_d_creator d_creator on d_creator.id=fct_origin.creator_id ");
		sb.append(" where fct_origin.id in( ");
		sb.append(" select distinct r_zb_origin.origin_id from bigdata_r_zb_origin r_zb_origin where r_zb_origin.zb_id in( ");
		sb.append(" select zb_id from bigdata_fct_zb fct_zb  ");
		sb.append(" where 	date_format(fct_zb.created_time,'%Y%m%d')='"+date+"'");
		sb.append(" and fct_zb.source_dept_ids like '%,"+deptId+",%'");
		sb.append(sqlWhere);
		sb.append(" )) ");
		creator=Db.find(sb.toString());
		for(Record r:creator){
			List<Record> c= useLevelByTypeUseLevel(sqlWhere, date, deptId, r.get("id")+"", useType);
			r.set("useType", c);
		}
		return creator;
		
	}
	/**
	 * 
	 * @time   2017年7月25日 上午11:25:01
	 * @author zuoqb
	 * @todo   根据时间 部门 发稿人以及使用方式统计使用级别数量
	 */
	public List<Record> useLevelByTypeUseLevel(String sqlWhere,String date,String deptId,String creatorId,List<Record> useType){
		String rightJoin="";
		for(int x=0;x<useType.size();x++){
			if(x==useType.size()-1){
				rightJoin+=" select '"+useType.get(x).getStr("use_type_id")+"' as na from dual ";
			}else{
				rightJoin+=" select '"+useType.get(x).getStr("use_type_id")+"' as na from dual union ";
			}
		};
		List<Record> list=new ArrayList<Record>();
		for(String level:levels){
			Record mRecord=new Record();
			mRecord.set("use_level",level);
			String sql="";
			sql+="  select ifnull(a.count,0) as count,c.* from (select fct_eval.use_type_id,count(1) as count ";
			sql+=" from bigdata_fct_eval fct_eval ";
			sql+=" where fct_eval.zb_id in(select fct_zb.zb_id from bigdata_fct_zb fct_zb where date_format(fct_zb.created_time,'%Y%m%d')='"+date+"' and fct_zb.source_dept_ids like '%,"+deptId+",%'  and fct_zb.creator_ids like '%,"+creatorId+",%'  "+sqlWhere+" ) ";
			sql+=" and fct_eval.use_level_id='"+level+"' ";
			sql+=" group by fct_eval.use_level_id )a  ";
			sql+=" right join ( ";
			sql+=" select b.* from (  ";
			sql+= rightJoin;
			sql+="  )b ";
			sql+=" )c on c.na=a.use_type_id ";
			List<Record> l=Db.find(sql);
			mRecord.set("useLevel", l);
			list.add(mRecord);
		};
		return list;
	}
	/**
	 * 
	 * @time 2017年7月25日 上午9:58:42
	 * @author zuoqb
	 * @todo 对数组去重 排序
	 */
	public String[] removeRepeated(String[] s,String selectDeptIds) {
		TreeSet<String> tr = new TreeSet<String>();
		for (int i = 0; i < s.length; i++) {
			if(StringUtils.isNotBlank((s[i]))){
				if(StringUtils.isBlank(selectDeptIds)){
					tr.add(s[i]);
				}else if(selectDeptIds.indexOf(s[i])!=-1){
					tr.add(s[i]);
				}
			}
		}
		String[] s2 = new String[tr.size()];
		for (int i = 0; i < s2.length; i++) {
			s2[i] = tr.pollFirst();// 从TreeSet中取出元素重新赋给数组
		};
		Arrays.sort(s2);
		return s2;
	}
	@SuppressWarnings("rawtypes")
	public String joinStrByMap(HashMap<String, Object> hashMap) {
		Collection temp = hashMap.values();
		String str = "";
		for (Object o : temp) {
			str += o + "+";
		}
		if (StringUtils.isNotBlank(str) && str.length() > 0) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}
	
}
