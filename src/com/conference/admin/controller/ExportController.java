package com.conference.admin.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.conference.admin.model.FctOrigin;
import com.conference.admin.model.FctZb;
import com.conference.common.BaseController;
import com.conference.util.excel.ExportFctZbTemplate;
import com.conference.util.excel.ExportSourceTemplate;
import com.conference.util.sys.SqlUtil;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class ExportController extends BaseController {
	/***
	 * 
	 * @time   2017年7月18日 上午9:03:53
	 * @author zuoqb
	 * @todo   导出素材分析列表
	 */
	public void exportSourceDetail() throws IOException{
		String sqlWhere=SqlUtil.joinSqlForSource(getRequest());
		Page<Record> pager=FctOrigin.dao.originAnalysis(sqlWhere, 1, Integer.MAX_VALUE);
		List<Record> data=new ArrayList<Record>();
		for(Record p:pager.getList()){
			List<Record> children=p.get("children");
			for(Record creator:children){
				List<Record> source=creator.get("children");
				for(Record sou:source){
					Record detail=new Record();
					detail.set("date",p.get("date") );
					detail.set("companyname", p.get("companyname")  );
					detail.set("deptname", p.get("deptname") );
					detail.set("creatorName", creator.get("creatorName") );
					detail.set("sourceCodeName", sou.get("sourceCodeName") );
					detail.set("sourcenNum", sou.get("sourcenNum") );
					detail.set("zbnum", sou.get("zbnum") );
					detail.set("rate", sou.get("rate") );
					detail.set("useType", sou.get("useType") );
					detail.set("useLevel", sou.get("useLevel") );
					detail.set("hasPiShiNum", sou.get("hasPiShiNum") );
					data.add(detail);
				}
			}
		}
		String str = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String time = str.replaceAll("-", ".");
		String title="素材分析明细导出(" + time + ").xlsx";
		ExportSourceTemplate template = new ExportSourceTemplate(data,title);
		getResponse().setContentType("application/vnd.ms-excel");
		setFileDownloadHeader(getRequest(), getResponse(),title );
		template.doExport(getResponse().getOutputStream(), null);
		renderNull();
	}
	
	/***
	 * 
	 * @time   2017年7月18日17:42:04
	 * @author zuoqb
	 * @todo   导出新闻分析列表
	 */
	public void exportFctZbDetail() throws IOException{
		String sqlWhere=SqlUtil.joinSqlForNews(getRequest());
		Page<Record> pager=FctZb.dao.fctZbAnalysis(sqlWhere, 1, Integer.MAX_VALUE);
		String str = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String time = str.replaceAll("-", ".");
		String title="新闻分析明细导出(" + time + ").xlsx";
		ExportFctZbTemplate template = new ExportFctZbTemplate(pager.getList(),title);
		getResponse().setContentType("application/vnd.ms-excel");
		setFileDownloadHeader(getRequest(), getResponse(),title );
		template.doExport(getResponse().getOutputStream(), null);
		renderNull();
	}
}
