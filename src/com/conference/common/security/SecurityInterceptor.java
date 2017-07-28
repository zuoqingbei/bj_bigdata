/*
 * Copyright (c) 2012 <a href="http://www.med626.com.cn">上海医师在线 信息技术有限公司</a>  All Rights Reserved.                	 
 */

package com.conference.common.security;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.conference.admin.model.SysMenu;
import com.conference.admin.model.SysRole.RoleTypeEnum;
import com.conference.admin.model.SysUser;
import com.conference.common.BaseController;
import com.conference.util.HttpRequestDeviceUtils;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;




/** 
 * 类说明 ：权限拦截器
 * 1.是否需要进行权限过滤,对不需要权限的路径过滤,需要过滤的就进行过滤
 * 2.如果未登录去登录页面
 * 3.判断请求路径,是否该用户角色能登录
 * 4.如果已登录,且验证不通过的跳到403页面.
 * @author 	zuoqb
 * @version 2017年7月27日14:31:11
 */
public class SecurityInterceptor implements Interceptor {
	
	private static final Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);
	
	/**
	 * 不需要登录路径
	 */
	public final static List<String> passUrls= new ArrayList<String>(){
		private static final long serialVersionUID = 1L;
	{
		//登录地址
		add("/login");
		
	}};
	
	public void intercept(Invocation ai) {
		String action_key = ai.getActionKey();
		BaseController c = (BaseController) ai.getController();
		String to= c.getRequest().getRequestURL() + (c.getParasUrl()==null?"":c.getParasUrl());
		if("index".equals(ai.getMethodName()) ){
			action_key=action_key+"/";
		}
//		获取方法上的注解
		SecurityAuthorityAnnotation anno = ai.getMethod().getAnnotation(SecurityAuthorityAnnotation.class);
//		没找到就再获取类上的注解
		if(anno==null){
			anno=ai.getController().getClass().getAnnotation(SecurityAuthorityAnnotation.class);
		}
		SecurityContainer container = c.getSecurityContainer();
		
//		1.对不需要权限的路径过滤,写死的,类的方法上加了不需要权限的(还未做).需要过滤的就进行过滤
		if(!isNeedSecurity(action_key, anno)){
			ai.invoke();
			return;
		}
//		2.如果未登录去登录页面
		if(container==null||container.getSysUser()==null){
			toLoginPage(c, action_key, to);
			return;
		}
//		3.1超级管理员直接为TRUE
		boolean canGo=false;
		if(SysUser.isSuperSysUser(container.getSysUser())){
			ai.invoke();
			return;
		}
		
//		3.2判断请求路径,是否该用户角色能登录
		List<RoleTypeEnum> roleTypes = container.getRoleTypes();
		for (RoleTypeEnum roleTypeEnum : roleTypes) {
			if(action_key.startsWith(roleTypeEnum.getPath())){
				canGo=true;
			}
		}
		
//		3.3对注解进行处理
		canGo=dealSecurityAuthorityAnnotation(anno,container,action_key);
		if(!canGo){
			logger.info(container.getSysUser()+"没有权限访问:"+c.getRequestUrl());
		}
		
//		4.如果已登录,且验证不通过的跳到403页面.
		if(!canGo){
			c.renderError(403);
		}
		ai.invoke();
	}
	
	
	
	



	/**1.是否需要进行权限过滤,对不需要权限的路径过滤,需要过滤的就进行过滤
	 * @return	true 需要权限过滤 	false 不需要权限过滤
	 * @author longjunfeng
	 * @date   2016年7月4日下午3:03:49
	 */
	public boolean isNeedSecurity(String action_key,SecurityAuthorityAnnotation anno){
		//1.是否是不需要处理的url
		if(isPass(action_key)){
			return false;
		}
		//2.是否类或者方法上有权限的注解
		if(anno!=null){
			return true;
		}
		//3.是否不在包含的需要角色访问的菜单路径之中,包含在就需要登录,不包含就不需要登录
		boolean allPass=true;
		for (RoleTypeEnum one : RoleTypeEnum.values()) {
			if(action_key.startsWith(one.getPath())){
				allPass=false;
				break;
			}
		}
		if(allPass){
			return false;
		}
		return true;
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @time   2017年7月27日 下午1:46:02
	 * @author zuoqb
	 * @todo   跳转到登录页面
	 */
	public void toLoginPage(BaseController c,String action_key ,String to){
		boolean isAjax = HttpRequestDeviceUtils.isAjaxRequet(c.getRequest());
		if(isAjax){
			c.renderJson(c.setCommonMsgError("登录过期，请刷新页面重新登录！"));
			return;
		}
		String loginUrl="/login/index";
		if(StringUtils.isNotBlank(loginUrl)){
			c.redirect(loginUrl, true);
		}
	}
	
	/**
	 * 是否放行，不需要登录检测
	 * @param action_key
	 * @return
	 * @author longjunfeng
	 * @date   2015年12月17日上午9:12:53
	 */
	public static boolean isPass(String action_key){
		for (String string : passUrls) {
			if(action_key.startsWith(string)){
				return true;
			}
		}
		return false;
	}
	
	
	
	
	/** 
	 * @Desc: 用户是否有权限访问 带有权限注解的方法或者类
	 * @param anno
	 * @param container
	 * @param action_key
	 * @return
	 * @return: boolean			true 通过可以访问  false 未通过不可以访问
	 * @author: longjunfeng   
	 * @date: 2016年10月11日 上午10:58:04 
	 */
	private boolean dealSecurityAuthorityAnnotation(SecurityAuthorityAnnotation anno, SecurityContainer container,String action_key) {
		try {
	//		1.根据注解判断有没有权限
			if(anno==null){
				return true;
			}
	//		1.1判断登录
			if(anno.needLogin()){
				if(container==null||container.getSysUser()==null){
					return false;
				}
			}
	//		1.2判断角色 未登录返回false,不包含角色返回false
			if(anno.needRoles()!=null&&anno.needRoles().length>0){
				if(container==null||container.getSysUser()==null||container.getRoleTypes()==null){
					return false;
				}
				boolean isHasRole=false;
				for (RoleTypeEnum role : anno.needRoles()) {
					if(container.getRoleTypes().contains(role)){
						isHasRole=true;
						break;
					}
				}
				if(!isHasRole){
					return false;
				}
			}
	//		1.4判断菜单权限
			if(anno.needUrl()){
				if(container==null||container.getSysUser()==null||container.getMenus()==null){
					return false;
				}
				boolean isHasMenu=false;
				for (SysMenu menu : container.getMenus()) {
					if(action_key.equals(menu.getStr("href"))){
						isHasMenu=true;
						break;
					}
				}
				if(!isHasMenu){
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}
}
