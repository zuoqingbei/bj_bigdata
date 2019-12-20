package com.conference.common.security;

import java.io.Serializable;
import java.util.List;

import com.conference.admin.model.Creator;
import com.conference.admin.model.Dept;
import com.conference.admin.model.SysMenu;
import com.conference.admin.model.SysRole;
import com.conference.admin.model.SysRole.RoleTypeEnum;
import com.conference.admin.model.SysUser;
import com.conference.common.BaseController;

public class SecurityContainer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3020142175012611500L;
	/**
	 * 登录用户sysUser
	 */
	private SysUser sysUser;
	/**
	 * 登录用户所属大单位
	 */
	private Dept belongCompany;
	/**
	 * 登录用户所属部门
	 */
	private Dept belongDept;
	/**
	 * 用户拥有的角色
	 */
	private List<SysRole> roles;
	/**
	 * 用户拥有的菜单
	 */
	private List<SysMenu> menus;
	
	/**
	 * 用户管理的发稿人
	 */
	private List<Creator> mangeCreator;
	
	/**
	 * 角色能登录哪些后台枚举
	 */
	private List<RoleTypeEnum> roleTypes;
	public  SecurityContainer(){}
	
	
	/**
	 * 安全信息存放容器初始化,把account,profile,机构,
	 * @param account
	 * @param c
	 */
	public  SecurityContainer(SysUser sysUser,BaseController c){
		this.sysUser=sysUser;
		if(sysUser!=null){
			this.belongCompany=Dept.dao.findById(sysUser.get("company_id"));
			this.belongDept=Dept.dao.findById(sysUser.get("dept_id"));
			this.roles=SysRole.dao.getRolesByUser(sysUser.get("id"));
			this.menus=SysMenu.dao.getMenuByUser(sysUser.get("id"),null);
			this.mangeCreator=Creator.dao.getCreatorByUser(sysUser.get("id")+"");
			this.roleTypes=SysRole.dao.getRoleTypesWithEnum(sysUser);
		}
	}
	

	public SysUser getSysUser() {
		return sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public Dept getBelongDept() {
		return belongDept;
	}

	public void setBelongDept(Dept belongDept) {
		this.belongDept = belongDept;
	}

	public List<SysRole> getRoles() {
		return roles;
	}

	public void setRoles(List<SysRole> roles) {
		this.roles = roles;
	}

	public List<SysMenu> getMenus() {
		return menus;
	}

	public void setMenus(List<SysMenu> menus) {
		this.menus = menus;
	}

	public List<Creator> getMangeCreator() {
		return mangeCreator;
	}

	public void setMangeCreator(List<Creator> mangeCreator) {
		this.mangeCreator = mangeCreator;
	}


	public Dept getBelongCompany() {
		return belongCompany;
	}


	public void setBelongCompany(Dept belongCompany) {
		this.belongCompany = belongCompany;
	}


	public List<RoleTypeEnum> getRoleTypes() {
		return roleTypes;
	}


	public void setRoleTypes(List<RoleTypeEnum> roleTypes) {
		this.roleTypes = roleTypes;
	}
	

}
