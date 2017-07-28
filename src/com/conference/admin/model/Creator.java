package com.conference.admin.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.conference.common.BaseModel;

public class Creator extends BaseModel<Creator>{
	private static final long serialVersionUID = -8811409081417389372L;
	public static final Creator dao=new Creator();
	/**
	 * 
	 * @time   2017年7月6日 下午2:15:10
	 * @author zuoqb
	 * @todo   根据部门获取发稿人
	 */
	public List<Creator> getCreatorByDept(String  deptId){
		String sql="";
		sql+=" select * from bigdata_d_creator ";
		if(StringUtils.isNotBlank(deptId)){
			sql+=" where id in( select creator_id from bigdata_r_dept_creator where dept_id='"+deptId+"') ";
		}
		return Creator.dao.find(sql);
	}
	/**
	 * 
	 * @time   2017年7月27日 上午11:03:49
	 * @author zuoqb
	 * @todo   根据用户获取管理的发稿人
	 */
	public List<Creator> getCreatorByUser(String  userId){
		List<SysRole> roles=SysRole.dao.getRolesByUser(userId);
		SysUser sysUser=SysUser.dao.findById(userId);
		String sqlWhere="";
		for(SysRole role:roles){
			String deptWhere=" (";
			//data_scope 数据范围 0:本部门全部数据 1：本部门及下属数据 2：本人数据 3 本人及下属数据
			String daraScope=role.getStr("data_scope");
			String deptId=role.getStr("dept_id");
			switch (daraScope) {
			case "0":
				//角色对应本部门全部数据 
				deptWhere+=" d_dept.id='"+deptId+"' ";
				break;
			case "1":
				//角色对应本部门及下属数据 
				deptWhere+=" d_dept.id='"+deptId+"' or d_dept.pid='"+deptId+"' ";
				break;
			case "2":
				//本人数据 用户表对应部门
				deptWhere+=" d_dept.id='"+sysUser.get("dept_id")+"' ";
				break;
			default:
				//3 本人及下属数据
				deptWhere+=" d_dept.id='"+sysUser.get("dept_id")+"' or d_dept.pid='"+sysUser.get("dept_id")+"' ";
				break;
			}
			deptWhere+=" ) ";
			if(roles.indexOf(role)==roles.size()-1){
				sqlWhere+=deptWhere;
			}else{
				sqlWhere+=deptWhere+" or ";
			}
		}
		String sql="";
		sql+=" select d_creator.* from `bigdata_d_creator` d_creator left join bigdata_r_dept_creator r_dept_creator ";
		sql+=" on r_dept_creator.creator_id=d_creator.id ";
		sql+=" left join bigdata_d_dept d_dept on d_dept.id=r_dept_creator.dept_id ";
		if(StringUtils.isNotBlank(sqlWhere)){
			sql+=" where "+sqlWhere;
		}
		return Creator.dao.find(sql);
	}

}
