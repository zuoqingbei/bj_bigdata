package com.conference.admin.controller;

import com.conference.admin.model.FctOrigin;
import com.conference.common.BaseController;
import com.conference.common.interceptor.TestInterceptor;
import com.conference.util.sys.SqlUtil;
import com.jfinal.aop.Before;
@Before(TestInterceptor.class)
public class IndexController extends BaseController {
	public void index(){
		render("/bigdata/pages/index.html");
	}
	/**
	 * 素材数量
	 */
	public void statisticOriginAjax(){
		String sqlWhere=SqlUtil.joinSql(this);
		FctOrigin org=FctOrigin.dao.statisticOrigin(sqlWhere);
		renderJson(org);
	}
	/**
	 * 素材同比 环比
	 */
	public void originHbTbAjax(){
		String sqlWhere=SqlUtil.joinSql(this,false);
		FctOrigin org=FctOrigin.dao.originHbTb(sqlWhere,null);
		renderJson(org);
	}
}
