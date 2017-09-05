package com.conference.admin.controller;

import com.conference.common.BaseController;
import com.conference.common.interceptor.SessionInterceptor;
import com.jfinal.aop.Before;

@Before(SessionInterceptor.class)
public class IndexController extends BaseController {
	public void index() {
		render("/bigdata/pages/index.html");
	}

	/**
	 * 
	 * @time   2017年7月12日 上午10:06:00
	 * @author zuoqb
	 * @todo   总览
	 */
	public void allView() {
		render("/bigdata/pages/allView.html");
	}

	/**
	 * 
	 * @time   2017年7月12日 上午10:06:00
	 * @author zuoqb
	 * @todo   素材分析
	 */
	public void sourceAnalysis() {
		render("/bigdata/pages/sourceAnalysis.html");
	}

	/**
	 * 
	 * @time   2017年7月12日 上午10:06:00
	 * @author zuoqb
	 * @todo   新闻分析
	 */
	public void newsAnalysis() {
		render("/bigdata/pages/newsAnalysis.html");
	}

	/**
	 * 
	 * @time   2017年7月12日 上午10:06:00
	 * @author zuoqb
	 * @todo   使用方式
	 */
	public void usageMode() {
		render("/bigdata/pages/usageMode.html");
	}

	/**
	 * 
	 * @time   2017年7月12日 上午10:06:00
	 * @author zuoqb
	 * @todo   评价等级
	 */
	public void level() {
		render("/bigdata/pages/level.html");
	}
}
