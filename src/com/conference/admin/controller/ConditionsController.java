package com.conference.admin.controller;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.conference.admin.model.BigDataDRef;
import com.conference.admin.model.Creator;
import com.conference.admin.model.Dept;
import com.conference.admin.model.FctEval;
import com.conference.admin.model.FctOrigin;
import com.conference.admin.model.FctZb;
import com.conference.common.BaseController;
import com.conference.util.sys.SqlUtil;
import com.jfinal.plugin.activerecord.Record;
public class ConditionsController extends BaseController {
	/**
	 * 
	 * @time   2017年7月6日 下午2:18:42
	 * @author zuoqb
	 * @todo   获取大单位
	 */
	public void companyAjax(){
		renderJson(Dept.dao.getCompanys());
	}
	/**
	 * 
	 * @time   2017年7月6日 下午2:18:42
	 * @author zuoqb
	 * @todo   获取 部门
	 */
	public void deptAjax(){
		renderJson(Dept.dao.getDepts(getPara(ATTR_COMPANYID,"")));
	}
	/**
	 * 
	 * @time   2017年7月6日 下午2:28:55
	 * @author zuoqb
	 * @todo   根据部门获取发稿人
	 */
	public void creatorAjax(){
		renderJson(Creator.dao.getCreatorByDept(getPara(ATTR_DEPTID,"")));
	}
	/**
	 * 
	 * @time   2017年7月6日 下午2:28:55
	 * @author zuoqb
	 * @todo   根据类型获取方向 性质
	 */
	public void dRefAjax(){
		renderJson(BigDataDRef.dao.getBigDataDRefByType(getPara(ATTR_DIRECT,"方向")));
	}
	/**
	 * 
	 * @time   2017年7月14日 上午9:48:28
	 * @author zuoqb
	 * @todo   新闻使用方式 批示 评论级别统计
	 */
	public void fctEvalAjax(){
		String filed=getPara("filed","");
		String sqlWhere=SqlUtil.joinSqlForNews(getRequest());
		List<Record> list=FctEval.dao.fctEvalByFiled(sqlWhere,filed);
		renderJson(list);
	}
	/**
	 * 
	 * @time   2017年7月14日 上午11:20:02
	 * @author zuoqb
	 * @todo  新闻量Top6(按照部门分组)
	 */
	public void fctZbTop6Ajax(){
		String limit=getPara("limit","");
		String sqlWhere=SqlUtil.joinSqlForNews(getRequest());
		List<Record> list=FctZb.dao.fctZbTop6(sqlWhere, limit);
		renderJson(list);
	}
	
	/**
	 * 
	 * @time   2017年7月14日 上午11:20:02
	 * @author zuoqb
	 * @todo  素材量Top6(按照部门分组)
	 */
	public void fctOriginTop6Ajax(){
		String limit=getPara("limit","");
		String sqlWhere=SqlUtil.joinSqlForSource(getRequest());
		List<Record> list=FctOrigin.dao.fctOriginTop6(sqlWhere, limit);
		renderJson(list);
	}
	
	/**
	 * 
	 * @time   2017年7月14日 上午11:20:02
	 * @author zuoqb
	 * @todo  按照年份统计新闻 素材
	 */
	public void fctStaticYearAjax(){
		Calendar a=Calendar.getInstance();
		String limit=getPara("limit","5");
		int endYear=a.get(Calendar.YEAR);//得到年
		int startYear=endYear-Integer.valueOf(limit);
		String newsSqlWhere=SqlUtil.joinSqlForNews(getRequest());
		String sourceSqlWhere=SqlUtil.joinSqlForSource(getRequest());
		Map<String,Object> result=new HashMap<String, Object>();
		List<Record> org=FctOrigin.dao.fctOriginByYear(sourceSqlWhere, startYear,endYear);
		List<Record> zb=FctZb.dao.fctZbByYear(newsSqlWhere, startYear,endYear);
		Collections.reverse(org);
		Collections.reverse(zb);
		result.put(0+"", org);
		result.put(1+"", zb);
		renderJson(result);
	}
}
