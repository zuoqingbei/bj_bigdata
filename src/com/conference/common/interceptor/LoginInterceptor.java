package com.conference.common.interceptor;

import com.conference.admin.model.SysUser;
import com.conference.common.BaseController;
import com.conference.common.security.SecurityInterceptor;
import com.conference.common.service.SysUserService;
import com.conference.util.sys.ThreadSysUserUtil;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class LoginInterceptor extends BaseController implements Interceptor {

	@Override
	public void intercept(Invocation ai) {
		String action_key = ai.getActionKey();
		BaseController c = (BaseController) ai.getController();
		
		SysUser sysUser = c.getLoginUser();
		//过滤不需要的url
		if(SecurityInterceptor.isPass(action_key)){
			ai.invoke();
			return;
		}
		//1.自动登录,自动登录只提供前台功能,后台不需要自动登录
		if(sysUser==null){
			sysUser=SysUserService.service.autoLogin(c,false);
		}
		//2.把登录用户放入线程中去
		ThreadSysUserUtil.setThreadLocalSysUser(sysUser,c.getSecurityContainer());
		ai.invoke();
		//3.把登录用户线程中 移除
		ThreadSysUserUtil.removeThreadLocalAccount();
	}

}
