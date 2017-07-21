package com.conference.common.interceptor;

import com.conference.common.BaseController;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class SessionInterceptor extends BaseController implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		dealAttrToSession(inv.getController());
		inv.invoke();
	}

}
