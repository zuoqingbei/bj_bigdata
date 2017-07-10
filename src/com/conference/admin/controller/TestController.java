package com.conference.admin.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.conference.admin.model.FctOrigin;
import com.conference.admin.model.RdcProductAgentDetail;
import com.conference.common.RESTFulController;
import com.conference.common.interceptor.TestInterceptor;
import com.conference.util.MyPoiRender;
import com.conference.util.excel.ExportRdcProductAgentDetailTemplate;
import com.conference.util.sys.DataTablesUtil;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Record;
@Before(TestInterceptor.class)
public class TestController extends  RESTFulController<FctOrigin> {
	public void index(){
		render("/views/modules/test/test.html");
	}
	public void table(){
		render("/views/modules/test/testTable.html");
	}
	/**
	 * 用户获取列表数据，datatables插件专用。pageSize传-1则不分页
	 * @author zuoqb
	 * @date   2016-12-26 11:36:54
	 */
	@SuppressWarnings("unchecked")
	public  void listData() throws Exception{
		String[] columns=new String[]{
				"name",
						"created_time"};
		
		String tables =getTableName() +" o   ";
		String sWhereSql=" 1=1 ";
		
		Map<String,Object > rs = DataTablesUtil.query(columns, tables,sWhereSql,null,null, this, true);
		List<Record> data=(List<Record>) rs.get("aaData");
		rs.put("aaData", data);
		renderJson(rs); 
	}
	public void excelData(){
		String[] columns=new String[]{
			"name",
					"created_time"};
		String tables =getTableName() +" o ";
		Map<String,Object > rs = DataTablesUtil.query(columns, tables,null, this);
		
		String[] excelColumn=new String[]{
			"name",
					"created_time"
		};
		String[] excelColumnName=new String[]{
			"直播名称",
					"简介"
		};
		render(MyPoiRender.excel((List<Object>)rs.get("aaData"),"直播导出表.xls","直播导出表",excelColumnName,excelColumn));
	}
	
	
	public void exportDetail() throws IOException{
		List<RdcProductAgentDetail> list=new ArrayList<RdcProductAgentDetail>();
		RdcProductAgentDetail model=new RdcProductAgentDetail();
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
		String title="产品代表明细导出(" + time + ").xlsx";
		ExportRdcProductAgentDetailTemplate template = new ExportRdcProductAgentDetailTemplate(list,title);
		getResponse().setContentType("application/vnd.ms-excel");
		setFileDownloadHeader(getRequest(), getResponse(),title );
		template.doExport(getResponse().getOutputStream(), null);
	}
	public static void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) {
		final String userAgent = request.getHeader("USER-AGENT");
		try {
			String finalFileName = null;
			if(StringUtils.contains(userAgent, "MSIE")){
				finalFileName = URLEncoder.encode(fileName,"UTF8");
			}else if(StringUtils.contains(userAgent, "Mozilla")){
				finalFileName = new String(fileName.getBytes(), "ISO8859-1");
			}else{
				finalFileName = URLEncoder.encode(fileName,"UTF8");
			}
			response.setHeader("Content-Disposition", "attachment; filename=\"" + finalFileName + "\"");
		} catch (Exception e) {
		}
	}
}
