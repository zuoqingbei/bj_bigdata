package com.conference.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;

import com.conference.admin.model.FctOriginImport;
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
