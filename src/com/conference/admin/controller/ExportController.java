package com.conference.admin.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.conference.admin.model.Dept;
import com.conference.admin.model.FctEval;
import com.conference.admin.model.FctOrigin;
import com.conference.admin.model.FctZb;
import com.conference.common.BaseController;
import com.conference.util.excel.ExportFctZbTemplate;
import com.conference.util.excel.ExportSourceTemplate;
import com.conference.util.excel.ExportUseLevelTemplate;
import com.conference.util.excel.ExportUseModelTemplate;
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
	public void exportSourceDetail() throws IOException {
		String sqlWhere = SqlUtil.joinSqlForSource(getRequest());
		Page<Record> pager = FctOrigin.dao.originAnalysis(sqlWhere, 1, Integer.MAX_VALUE);
		List<Record> data = new ArrayList<Record>();
		for (Record p : pager.getList()) {
			List<Record> children = p.get("children");
			for (Record creator : children) {
				List<Record> source = creator.get("children");
				for (Record sou : source) {
					Record detail = new Record();
					detail.set("date", p.get("date"));
					detail.set("companyname", p.get("companyname"));
					detail.set("deptname", p.get("deptname"));
					detail.set("creatorName", creator.get("creatorName"));
					detail.set("sourceCodeName", sou.get("sourceCodeName"));
					detail.set("sourcenNum", sou.get("sourcenNum"));
					detail.set("zbnum", sou.get("zbnum"));
					detail.set("rate", sou.get("rate"));
					detail.set("useType", sou.get("useType"));
					detail.set("useLevel", sou.get("useLevel"));
					detail.set("hasPiShiNum", sou.get("hasPiShiNum"));
					data.add(detail);
				}
			}
		}
		String str = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String time = str.replaceAll("-", ".");
		String title = "素材分析明细导出(" + time + ").xlsx";
		ExportSourceTemplate template = new ExportSourceTemplate(data, title);
		getResponse().setContentType("application/vnd.ms-excel");
		setFileDownloadHeader(getRequest(), getResponse(), title);
		template.doExport(getResponse().getOutputStream(), null);
		renderNull();
	}

	/***
	 * 
	 * @time   2017年7月18日17:42:04
	 * @author zuoqb
	 * @todo   导出新闻分析列表
	 */
	public void exportFctZbDetail() throws IOException {
		String sqlWhere = SqlUtil.joinSqlForNews(getRequest());
		Page<Record> pager = FctZb.dao.fctZbAnalysis(sqlWhere, 1, Integer.MAX_VALUE);
		String str = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String time = str.replaceAll("-", ".");
		String title = "新闻分析明细导出(" + time + ").xlsx";
		ExportFctZbTemplate template = new ExportFctZbTemplate(pager.getList(), title);
		getResponse().setContentType("application/vnd.ms-excel");
		setFileDownloadHeader(getRequest(), getResponse(), title);
		template.doExport(getResponse().getOutputStream(), null);
		renderNull();
	}

	/***
	 * 
	 * @time   2017年7月26日08:52:15
	 * @author zuoqb
	 * @todo   导出新闻使用方式列表
	 */
	public void exportUseModel() throws IOException {
		String companyId = SqlUtil.getAttrByKey(getRequest(), SESSION_COMPANYID);
		String deptId = SqlUtil.getAttrByKey(getRequest(), SESSION_DEPTID);
		//大单位
		String selectDeptIds = "";
		if (StringUtils.isNotBlank(companyId) && StringUtils.isBlank(deptId)) {
			// 如果大单位不为空 而部门为空 根据大单位查询下属部门
			List<Record> list = Dept.dao.getDepts(companyId);
			for (int x = 0; x < list.size(); x++) {
				selectDeptIds += "," + list.get(x).get("id") + ",";
			}
		}
		// 部门
		if (StringUtils.isNotBlank(deptId)) {
			selectDeptIds = "," + deptId + ",";
		}
		String sqlWhere = SqlUtil.joinSqlForNews(getRequest());
		Page<Record> pager = FctZb.dao.fctZbUseModelGroupByDate(sqlWhere, selectDeptIds, 1, Integer.MAX_VALUE);
		List<Record> useTypes = FctEval.dao.fctEvalUseType(sqlWhere);
		List<Record> data = new ArrayList<Record>();
		for (Record p : pager.getList()) {
			List<Record> children = p.get("children");
			for (Record dept : children) {
				List<Record> usetype = dept.get("usetype");
				List<Record> creatorList = dept.get("creators");
				Record detail = new Record();
				detail.set("date", p.get("date"));
				//拼接发稿人
				String creatorStr = "";
				for (Record creator : creatorList) {
					creatorStr += creator.get("name") + "/";
				}
				if (StringUtils.isNotBlank(creatorStr)) {
					creatorStr = creatorStr.substring(0, creatorStr.length() - 1);
					creatorStr = "(" + creatorStr + ")";
				}
				detail.set("deptname", dept.get("deptname") + creatorStr);
				for (Record use : usetype) {
					String use_type_id = use.get("use_type_id") + "";
					List<Record> useLevel = use.get("uselevel");
					for (Record level : useLevel) {
						String mC = "";
						for (Record creator : creatorList) {
							List<Record> innerusetype = creator.get("usetype");
							String crNum = "";
							for (Record r : innerusetype) {
								List<Record> inneruseLevel = r.get("uselevel");
								for (Record l : inneruseLevel) {
									if ((level.getStr("na") + "").equals(l.getStr("na"))
											&& (use_type_id + "").equals((r.get("use_type_id") + ""))) {
										crNum += l.get("count") + "/";
									}
									;
								}
							}
							;
							mC += crNum;
						}
						if (StringUtils.isNotBlank(mC)) {
							mC = mC.substring(0, mC.length() - 1);
							mC = "(" + mC + ")";
						}
						detail.set(use_type_id + level.getStr("na"), level.get("count") + mC);
					}
				}
				data.add(detail);
			}
		}
		/*	String json=JsonKit.toJson(data);
			System.out.println(json);*/
		String str = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String time = str.replaceAll("-", ".");
		String title = "新闻使用方式明细导出(" + time + ").xlsx";
		ExportUseModelTemplate template = new ExportUseModelTemplate(data, useTypes, title);
		getResponse().setContentType("application/vnd.ms-excel");
		setFileDownloadHeader(getRequest(), getResponse(), title);
		template.doExport(getResponse().getOutputStream(), null);
		renderNull();
	}

	/***
	 * 
	 * @time   2017年7月26日08:52:15
	 * @author zuoqb
	 * @todo   导出新闻评价等级列表
	 */
	public void exportUseLevel() throws IOException {
		String companyId = SqlUtil.getAttrByKey(getRequest(), SESSION_COMPANYID);
		String deptId = SqlUtil.getAttrByKey(getRequest(), SESSION_DEPTID);
		//大单位
		String selectDeptIds = "";
		if (StringUtils.isNotBlank(companyId) && StringUtils.isBlank(deptId)) {
			// 如果大单位不为空 而部门为空 根据大单位查询下属部门
			List<Record> list = Dept.dao.getDepts(companyId);
			for (int x = 0; x < list.size(); x++) {
				selectDeptIds += "," + list.get(x).get("id") + ",";
			}
		}
		// 部门
		if (StringUtils.isNotBlank(deptId)) {
			selectDeptIds = "," + deptId + ",";
		}
		String sqlWhere = SqlUtil.joinSqlForNews(getRequest());
		Page<Record> pager = FctZb.dao.fctZbUseLevelGroupByDate(sqlWhere, selectDeptIds, 1, Integer.MAX_VALUE);
		List<Record> useTypes = FctEval.dao.fctEvalUseType(sqlWhere);
		List<Record> data = new ArrayList<Record>();
		for (Record p : pager.getList()) {
			List<Record> children = p.get("children");
			for (Record dept : children) {
				List<Record> usetype = dept.get("usetype");
				List<Record> creatorList = dept.get("creators");
				Record detail = new Record();
				detail.set("date", p.get("date"));
				//拼接发稿人
				String creatorStr = "";
				for (Record creator : creatorList) {
					creatorStr += creator.get("name") + "/";
				}
				if (StringUtils.isNotBlank(creatorStr)) {
					creatorStr = creatorStr.substring(0, creatorStr.length() - 1);
					creatorStr = "(" + creatorStr + ")";
				}
				detail.set("deptname", dept.get("deptname") + creatorStr);
				for (Record use : usetype) {
					//for(String alllevel:FctZb.levels){
					String use_level = use.get("use_level") + "";
					List<Record> useLevel = use.get("uselevel");
					for (Record level : useLevel) {
						String mC = "";
						for (Record creator : creatorList) {
							List<Record> innerusetype = creator.get("usetype");
							String crNum = "";
							for (Record r : innerusetype) {
								List<Record> inneruseLevel = r.get("uselevel");
								for (Record l : inneruseLevel) {
									if ((level.getStr("na") + "").equals(l.getStr("na"))
											&& (use_level + "").equals((r.get("use_level") + ""))) {
										crNum += l.get("count") + "/";
									}
									;
								}
							}
							;
							mC += crNum;
						}
						if (StringUtils.isNotBlank(mC)) {
							mC = mC.substring(0, mC.length() - 1);
							mC = "(" + mC + ")";
						}
						detail.set(use_level + level.getStr("na"), level.get("count") + mC);
					}
				}
				data.add(detail);
			}
		}
		/*	String json=JsonKit.toJson(data);
			System.out.println(json);*/
		String str = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String time = str.replaceAll("-", ".");
		String title = "新闻评价等级明细导出(" + time + ").xlsx";
		ExportUseLevelTemplate template = new ExportUseLevelTemplate(data, useTypes, title);
		getResponse().setContentType("application/vnd.ms-excel");
		setFileDownloadHeader(getRequest(), getResponse(), title);
		template.doExport(getResponse().getOutputStream(), null);
		renderNull();
	}
}
