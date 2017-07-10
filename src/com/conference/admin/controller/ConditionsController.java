package com.conference.admin.controller;

import com.conference.admin.model.BigDataDRef;
import com.conference.admin.model.Creator;
import com.conference.admin.model.Dept;
import com.conference.common.BaseController;
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
		renderJson(Dept.dao.getDepts(getPara("companyId","")));
	}
	/**
	 * 
	 * @time   2017年7月6日 下午2:28:55
	 * @author zuoqb
	 * @todo   根据部门获取发稿人
	 */
	public void creatorAjax(){
		renderJson(Creator.dao.getCreatorByDept(getPara("deptId","")));
	}
	/**
	 * 
	 * @time   2017年7月6日 下午2:28:55
	 * @author zuoqb
	 * @todo   根据类型获取方向 性质
	 */
	public void dRefAjax(){
		renderJson(BigDataDRef.dao.getBigDataDRefByType(getPara("type","方向")));
	}
}
