package com.conference.common.security;

import org.apache.commons.lang3.StringUtils;

import com.conference.admin.model.SysUser;
import com.conference.common.BaseController;
import com.conference.common.Constant;



/** 
 * 类说明 ：权限service
 * 1.用来进行登录操作
 * 2.一些权限工具
 * @author zuoqb
 * @version 2017年7月27日15:14:47
 */
public class SecurityService {

	
	/**统一的登录口,所有的登录信息放入SecurityContainer 中,写入session或者放入线程中去
	 *  1.写cookie
	 *  2.记住我操作
		3.进行用户信息/权限信息加载
		4.进行session写操作
	* @author zuoqb
	* @version 2017年7月27日15:14:47
	 */
	public static SecurityContainer login(SysUser account,BaseController c){
//		1.写cookie
//		取出loginId   手机号、邮箱、昵称
		String login_id=null;
		if(StringUtils.isNotBlank(account.getStr("mobile"))){
			login_id=account.getStr("mobile");
		}else if(StringUtils.isNotBlank(account.getStr("email"))){
			login_id=account.getStr("email");
		}else if(StringUtils.isNotBlank(account.getStr("login_name"))){
			login_id=account.getStr("login_name");
		}
		String password=account.getStr("password");
		c.setCookie(BaseController.COOKIE_LOGIN_ID, 	login_id,10800, "/");// 保存账户名 3小时
		c.setCookie(BaseController.COOKIE_SECRET_TOKEN, password,10800, "/");// 保存MD5密码3小时
		c.setCookie(BaseController.COOKIE_REMEMBER_ME, 	c.getPara("remember_me", "false"),3600, "/");// 是否记住我1小时
		c.setCookie(BaseController.JSESSIONID, 			c.getSession().getId()+"",-1, "/");
//		2.如果是记住我，设置cookie
		if (c.getParaToBoolean("remember_me", false)) {
			c.setCookie(BaseController.COOKIE_LOGIN_ID, 	 login_id,3600* 24 * 30, "/");
			c.setCookie(BaseController.COOKIE_SECRET_TOKEN, password,3600* 24 * 30, "/");
			c.setCookie(BaseController.COOKIE_REMEMBER_ME, c.getPara("remember_me", "false"),3600* 24 *3600, "/");// 是否记住我1小时
		}
		
//		3.进行用户信息/权限信息加载
		SecurityContainer container = new SecurityContainer(account, c);
//		4.进行session写操作
		c.setSessionAttr(BaseController.SESSION_SECURITYCONTAINER, container);//下面的session以后都取消,都统一放入这个值里面
		return container;
	}
	
	
	
	/**
	 * 登出
	 * 清除本系统登录信息
	 * @param c
	* @author zuoqb
	* @version 2017年7月27日15:14:47
	 */
	public static void logout(BaseController c){
		c.clearSession();
		c.removeCookie(Constant.COOKIE_LOGIN_ID, "/");
		c.removeCookie(Constant.COOKIE_SECRET_TOKEN, "/");
		c.removeCookie(Constant.COOKIE_REMEMBER_ME, "/");
		c.removeCookie(Constant.JSESSIONID, "/");
	}
	
}
