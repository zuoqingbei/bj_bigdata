package com.conference.common.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.conference.admin.model.SysRole.RoleTypeEnum;

/**
 * 类说明 ：对action的访问权限的注解
* 1.登录权限
* 2.角色权限
* 3.可访问url权限
 * @author Administrator
 *
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface SecurityAuthorityAnnotation {
	
	/** 
	 * 
	 * @time   2017年7月27日 下午2:27:52
	 * @author zuoqb
	 * @Desc: 是否需要登录
	 * @return: boolean	true需要 false不需要	默认需要登录
	 */
	boolean needLogin() default true;
	
	/**
	 * 
	 * @time   2017年7月27日 下午2:28:14
	 * @author zuoqb
	 * @todo   哪些角色类型可以访问
	 * @return: RoleTypeEnum[]	在RoleTypeEnum枚举里面选择	默认为空
	 */
	RoleTypeEnum[] needRoles() default {};
	
	
	/**
	 * 
	 * @time   2017年7月27日 下午2:29:28
	 * @author zuoqb
	 * @todo  角色中需要带有访问该url的权限
	 * @return: boolean	boolean	true需要 false不需要		默认不需要
	 */
	boolean needUrl() default false;
	
}
