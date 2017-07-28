package com.conference.admin.controller;

import java.util.Map;

import com.conference.common.BaseController;
import com.conference.common.service.SysUserService;

public class LoginController  extends BaseController {
	public void submit(){
		String login_id="zuoqb";
		String password="123456";
		Map<String,String>  result=SysUserService.service.login(login_id, password, this);
		redirect("/home");
	}
	public void index(){
		render("/bigdata/pages/login/login.html");
	}
	public void logout(){
		SysUserService.service.logout(this);
		redirect("/login/index");
	}
}
