package com.conference.util.sys;

import com.conference.admin.model.SysUser;
import com.conference.common.security.SecurityContainer;

/**
 * 
 * 每次登陆后将用户account 放入线程环境中，让任何地方都能得到
 * zuoqb
 * 2017年7月27日14:41:04
 *
 */
public class ThreadSysUserUtil {
	
	private static final ThreadLocal<SysUser> tl = new ThreadLocal<SysUser>();
	private static final ThreadLocal<SecurityContainer> tlcontainer = new ThreadLocal<SecurityContainer>();
	
	
	public static void setThreadLocalSysUser(SysUser sysUser,SecurityContainer container) {
		tl.set(sysUser);
		tlcontainer.set(container);
	}
	
	public static void removeThreadLocalAccount() {
		tl.remove();
		tlcontainer.remove();
	}
	
	public static SysUser getSysUser() {
		SysUser result = tl.get();
		if (result == null)
			return null;
			//throw new IllegalStateException("需要事先使用 ThreadAcountUtil.setThreadLocalAccount(Account) 将 Account对象存入，才可以调用 ThreadAcountUtil.getAccount() 方法");
		return result;
	}
	
	public static SecurityContainer getSecurityContainer() {
		SecurityContainer result = tlcontainer.get();
		if (result == null)
			return null;
		//throw new IllegalStateException("需要事先使用 ThreadAcountUtil.setThreadLocalAccount(Account) 将 Account对象存入，才可以调用 ThreadAcountUtil.getAccount() 方法");
		return result;
	}
}
