package com.conference.admin.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.conference.admin.model.FctOrigin;
import com.conference.admin.model.FctZb;
import com.conference.admin.model.RdcProductAgentDetail;
import com.conference.admin.model.SysUser;
import com.conference.common.RESTFulController;
import com.conference.common.interceptor.SessionInterceptor;
import com.conference.common.security.SecurityAuthorityAnnotation;
import com.conference.common.security.SecurityContainer;
import com.conference.util.MyPoiRender;
import com.conference.util.excel.ExportRdcProductAgentDetailTemplate;
import com.conference.util.sys.DataTablesUtil;
import com.conference.util.sys.SqlUtil;
import com.jfinal.aop.Before;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

@Before(SessionInterceptor.class)
public class TestController extends RESTFulController<FctOrigin> {
	@SecurityAuthorityAnnotation(needLogin = true)
	public void index() {
		render("/views/modules/test/test.html");
	}

	public void table() {
		render("/views/modules/test/testTable.html");
	}

	/**
	 * 用户获取列表数据，datatables插件专用。pageSize传-1则不分页
	 * @author zuoqb
	 * @date   2016-12-26 11:36:54
	 */
	@SuppressWarnings("unchecked")
	public void listData() throws Exception {
		String[] columns = new String[] { "name", "created_time" };

		String tables = getTableName() + " o   ";
		String sWhereSql = " 1=1 ";

		Map<String, Object> rs = DataTablesUtil.query(columns, tables, sWhereSql, null, null, this, true);
		List<Record> data = (List<Record>) rs.get("aaData");
		rs.put("aaData", data);
		renderJson(rs);
	}

	public void excelData() {
		String[] columns = new String[] { "name", "created_time" };
		String tables = getTableName() + " o ";
		Map<String, Object> rs = DataTablesUtil.query(columns, tables, null, this);

		String[] excelColumn = new String[] { "name", "created_time" };
		String[] excelColumnName = new String[] { "直播名称", "简介" };
		render(MyPoiRender.excel((List<Object>) rs.get("aaData"), "直播导出表.xls", "直播导出表", excelColumnName, excelColumn));
	}

	public void exportDetail() throws IOException {
		List<RdcProductAgentDetail> list = new ArrayList<RdcProductAgentDetail>();
		RdcProductAgentDetail model = new RdcProductAgentDetail();
		model.setTime("2015");
		model.setDept("设计部");
		model.setAppYx("500");
		model.setAppLh("215");
		model.setAppNor("99");
		model.setIosYx("233");
		model.setIosLh("454");
		model.setIosNor("555");
		list.add(model);
		String str = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String time = str.replaceAll("-", ".");
		String title = "产品代表明细导出(" + time + ").xlsx";
		ExportRdcProductAgentDetailTemplate template = new ExportRdcProductAgentDetailTemplate(list, title);
		getResponse().setContentType("application/vnd.ms-excel");
		setFileDownloadHeader(getRequest(), getResponse(), title);
		template.doExport(getResponse().getOutputStream(), null);
	}

	public static void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) {
		final String userAgent = request.getHeader("USER-AGENT");
		try {
			String finalFileName = null;
			if (StringUtils.contains(userAgent, "MSIE")) {
				finalFileName = URLEncoder.encode(fileName, "UTF8");
			} else if (StringUtils.contains(userAgent, "Mozilla")) {
				finalFileName = new String(fileName.getBytes(), "ISO8859-1");
			} else {
				finalFileName = URLEncoder.encode(fileName, "UTF8");
			}
			response.setHeader("Content-Disposition", "attachment; filename=\"" + finalFileName + "\"");
		} catch (Exception e) {
		}
	}

	public void testEcharts3() {
		render("/bigdata/test/testEcharts3.html");
	}

	public void monthConUser() {
		String companyId = getPara("companyId");
		List<Record> list = new ArrayList<Record>();
		Record r = new Record();
		r.set("count", 100);
		r.set("name", "1月");
		list.add(r);

		Record r1 = new Record();
		r1.set("count", 50);
		r1.set("name", "5月");
		list.add(r1);
		renderJson(list);
	}

	/**
	 * 
	 * @time   2016年12月12日 下午4:02:33
	 * @author zuoqb
	 * @todo   模拟多条折线图 柱状图数据 
	 * @param  
	 * @return_type   void
	 */
	public void monthConUser2() {
		List<Record> list = new ArrayList<Record>();
		List<Record> list1 = new ArrayList<Record>();
		Random ran = new Random();
		for (int x = 0; x < 12; x++) {
			Record r = new Record();
			Record r1 = new Record();
			r.set("count", ran.nextInt(100));
			r.set("name", x + 1 + "月");
			r1.set("count", ran.nextInt(100));
			r1.set("name", x + 1 + "月");
			list.add(r);
			list1.add(r1);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(0 + "", list);
		result.put(1 + "", list1);
		renderJson(result);
	}

	public void page() {
		render("/bigdata/test/page.html");
	}

	public void useType() {
		String sqlWhere = SqlUtil.joinSqlForNews(getRequest());
		Page<Record> page = FctZb.dao.fctZbUseModelGroupByDate(sqlWhere, null, 1, 10);
		String result = JsonKit.toJson(page, 13);
		renderJson(result);
	}

	public void useLevel() {
		String sqlWhere = SqlUtil.joinSqlForNews(getRequest());
		Page<Record> page = FctZb.dao.fctZbUseLevelGroupByDate(sqlWhere, null, 1, 10);
		String result = JsonKit.toJson(page, 13);
		renderJson(result);
	}

	public void userInfo() {
		SysUser sysUser = SysUser.dao.findById("1");
		SecurityContainer s = new SecurityContainer(sysUser, this);
		renderJson(s);
	}
}
