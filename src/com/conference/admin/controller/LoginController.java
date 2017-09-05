package com.conference.admin.controller;

import java.util.Map;

import com.conference.common.BaseController;
import com.conference.common.service.SysUserService;

public class LoginController  extends BaseController {
	public void submit(){
		String login_id=getPara("username");
		String password=getPara("password");
		String rememberMe=getPara("rememberMe");
		this.setCookie(BaseController.COOKIE_REMEMBER_ME, 	rememberMe,10800, "/");
		Map<String,String>  result=SysUserService.service.login(login_id, password, this);
		renderJson(result);
	}
	public void index(){
		render("/bigdata/pages/login/login.html");
	}
	public void logout(){
		SysUserService.service.logout(this);
		redirect("/login/index");
	}
}
