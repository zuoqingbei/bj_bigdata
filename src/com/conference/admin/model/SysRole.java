package com.conference.admin.model;

import java.util.ArrayList;
import java.util.List;

import com.conference.common.BaseModel;

public class SysRole extends BaseModel<SysRole>{
	private static final long serialVersionUID = -8811409081417389372L;
	public static final SysRole dao=new SysRole();
	/**
	 * 
	 * @time   2017年7月27日 上午10:02:48
	 * @author zuoqb
	 * @todo   根据用户查询相应角色
	 */
	public List<SysRole> getRolesByUser(Object userId){
		String sql="select * from bigdata_sys_role where id in(select role_id from bigdata_sys_user_role where user_id='"+userId+"') and del_flag=0 and useable=0 ";
		return SysRole.dao.find(sql);
	}
	
	/**
	 * 
	 * @time   2017年7月27日 下午2:08:35
	 * @author zuoqb
	 * @todo   根据用户获取,用户可分配的角色类型
	 */
	public List<RoleTypeEnum> getRoleTypesWithEnum(SysUser sysUser){
		List<RoleTypeEnum> roleTypes = new ArrayList<RoleTypeEnum>();
		if(SysUser.isSuperSysUser(sysUser)){
			for (RoleTypeEnum one : RoleTypeEnum.values()) {
				roleTypes.add(one);
			}
			return roleTypes;
		}else{
			List<SysRole> roles = SysRole.dao.getRolesByUser(sysUser.get("id"));
			for (SysRole coreRole : roles) {
				if(coreRole.get("role_type")==null){
					continue;
				}
				String[] typeL = coreRole.getStr("role_type").split(",");
				for (String string : typeL) {
					for (RoleTypeEnum one : RoleTypeEnum.values()) {
						if(string.equals(one.value+"")){
							roleTypes.add(one);
						}
					}
				}
			}
			//默认加上普通用户角色类型枚举
			if(sysUser!=null){
				roleTypes.add(RoleTypeEnum.NOMAL_ROLE);
			}
			return roleTypes;
		}
	}
	/**
	 * 类说明 ：后台登录,需要的角色类型,如果用户分配的角色的类型中有该枚举值,就可以登录该后台.
	 * @author Administrator
	 *
	 */
	public enum RoleTypeEnum {
		NOMAL_ROLE				(0,"普通用户个人中心","/home/"), 
		/**
		 * @fieldName: SUPER_ADMIN_ROLE 超级管理后台
		 * @fieldType: RoleTypeEnum
		 * @Description: TODO
		 */
		SUPER_ADMIN_ROLE		(1,"超级管理后台","/admin/"),
		
		
        ;
        private Integer value;	//角色类型值
        private String name;	//角色类型名称
        private String path;	//角色能登录的后台路径
        
        private RoleTypeEnum(Integer value,String name,String path){
        	this.value=value;
        	this.name=name;
        	this.path=path;
        }
        
        public Integer getValue(){
        	return this.value;
        }
        
        public String getName(){
        	return this.name;
        }
        
        public String getPath(){
        	return this.path;
        }
        
        
	}
}
