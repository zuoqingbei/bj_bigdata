package com.conference.admin.model;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.conference.common.BaseModel;

public class SysMenu extends BaseModel<SysMenu>{
	private static final long serialVersionUID = -8811409081417389372L;
	public static final SysMenu dao=new SysMenu();
	/**
	 * 
	 * @time   2017年7月27日 上午10:02:48
	 * @author zuoqb
	 * @todo   根据用户查询相应菜单
	 */
	public List<SysMenu> getMenuByUser(Object userId,String parentId){
		StringBuffer sb=new StringBuffer();
		sb.append(" select sys_menu.* from bigdata_sys_role_menu sys_role_menu join bigdata_sys_menu  sys_menu on sys_role_menu.menu_id=sys_menu.id ");
		sb.append(" where sys_role_menu.role_id in ( ");
		sb.append(" select id from bigdata_sys_role where id in(select role_id from bigdata_sys_user_role where user_id='"+userId+"')  ");
		sb.append(" and del_flag=0 and useable=0 ");
		sb.append(" ) and sys_menu.is_show=0 and sys_menu.del_flag=0  ");
		String parentIdSql="";
		if(StringUtils.isNotBlank(parentId)){
			parentIdSql=" and sys_menu.parent_id='"+parentId+"' ";
		}else{
			parentIdSql=" and sys_menu.parent_id='0' ";
		}
		String orderBySql=" order by sys_menu.sort ";
		List<SysMenu> list=SysMenu.dao.find(sb.toString()+parentIdSql+orderBySql);
		for(SysMenu menu:list){
			menu.put("children",getMenuByUser(userId, menu.getStr("id")));
		}
		return list;
	}
}
