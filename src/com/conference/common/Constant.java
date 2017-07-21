/*
 * Copyright (c) 2012 上海医师在线信息技术有限公司  All Rights Reserved.                	
*/                                                                
  

package com.conference.common;

import com.jfinal.kit.PropKit;

/**
 * 项目常量
 *
 */
public interface Constant {
	
	//--------------------------------------------------系统信息--------------------------------------------------------
	/**是否使用线下模式
	 */
	public final static String OFFLINEMODE=PropKit.use("config.txt").get("sys.offlineMode");
	//cookie值
	public final static String COOKIE_LOGIN_ID="login_id";
	public final static String COOKIE_SECRET_TOKEN="secret_token";
	public final static String COOKIE_REMEMBER_ME="cookie_remember_me";
	
	//controller返回码
	public final static String STATUS="status";
	public final static String MSG="msg";
	//session值
	public final static String SESSION_SECURITYCONTAINER = "s_container";//用户登录的所有信息都放入当中
	public final static String SESSION_COMPANYID = "s_companyId";//大单位
	public final static String SESSION_DEPTID = "s_deptid";
	public final static String SESSION_CREATORID = "s_creatorid";
	public final static String SESSION_DIRECT = "s_direct";
	public final static String SESSION_STARTDATE = "s_startdate";
	public final static String SESSION_ENDDATE = "s_enddate";
	public final static String SESSION_PAGESIZE = "s_page_size";
	public final static String SESSION_PAGENUM = "s_page_num";
	
	//request 的attr值
	public final static String ATTR_COMPANYID="companyId";//大单位
	public final static String ATTR_DEPTID="deptId";//部门
	public final static String ATTR_CREATORID="creatorId";//发稿人
	public final static String ATTR_DIRECT="direct";//方向
	public final static String ATTR_STARTDATE="startDate";//开始时间
	public final static String ATTR_ENDDATE="endDate";//结束时间
	public final static String ATTR_PAGENUM="pageNum";
	public final static String ATTR_PAGESIZE="pageSize";
	
	
	
	
	
}
