/*
 * Copyright (c) 2012 上海医师在线信息技术有限公司  All Rights Reserved.                	
*/                                                                
  

package com.conference.common;

import com.jfinal.kit.PropKit;

/**
 * 项目常量
 *
 * @author Chengzl
 * @date 2014-6-27 上午11:22:56 
 * @version 1.0
 *
 */
public interface Constant {
	
	//--------------------------------------------------系统信息--------------------------------------------------------
	/**是否使用线下模式
	 */
	public final static String OFFLINEMODE=PropKit.use("config.txt").get("sys.offlineMode");
	
	/**
	 *默认官网默认二级域名		site.default_link=c
	 */
	public final static String CORE_SITE_SECOND_DOMAIN=PropKit.use("config.txt").get("site.default_link");
	
	/**设置默认域名site.name=会议系统
	 */
	public final static String CORE_SITE_NAME=PropKit.use("config.txt").get("site.name");
	/**设置系统的英文名称
	 */
	public final static String CORE_SITE_ENNAME=PropKit.use("config.txt").get("site.enname");
	/**zsyxapi域名
	 */
	public final static String ZSYX_API_DOMAIN=PropKit.use("config.txt").get("zsyxapi.domain");
	/**
	 * 网站模板的跳转路径
	 */
	public final static String CORE_SITE_PATH=PropKit.use("config.txt").get("site.render_path");
	/**默认站点的主IP地址
	 */
	public final static String CORE_SITE_IP=PropKit.use("config.txt").get("site.ip");
	
	/**默认官网默认网站小logo
	 */
	public final static String CORE_SITE_LOGO=PropKit.use("config.txt").get("site.logo");
	
	/**默认官网默认网站大logo
	 */
	public final static String CORE_SITE_BIG_LOGO=PropKit.use("config.txt").get("site.big_logo");
	
	/**默认官网默认网站大logo（宽的）
	 */
	public final static String CORE_SITE_BANNER_LOGO=PropKit.use("config.txt").get("site.banner_logo");
	
	/**默认官网默认的关键字
	 */
	public final static String CORE_SITE_KEYWORD=PropKit.use("config.txt").get("site.keyword");
	
	/**默认官网默认的描述
	 */
	public final static String CORE_SITE_DESCRIPTION=PropKit.use("config.txt").get("site.description");
	
	/**默认官网默认的微信可信域名
	 */
	public final static String CORE_SITE_WX_OAUTH_URL=PropKit.use("config.txt").get("site.wx_oauth_url");
	
	/**默认官网默认的支付域名
	 */
	public final static String CORE_SITE_PAY_URL=PropKit.use("config.txt").get("site.pay_url");
	
	
	/**文件转换服务器
	 */
	public final static String CORE_FILESERVER=PropKit.use("config.txt").get("file.server");//文件转换服务器
	/**超级用户
	 */
	public final static String CORE_SUPER_ACCOUNT=PropKit.use("config.txt").get("super.account");//超级用户
	/**掌上医讯地址
	 */
	public final static String CORE_ZSYX_DOMAIN=PropKit.use("config.txt").get("passport.domain");//掌上医讯地址
	
	
	
	
	
	//缓存名称
	public final static String REDIS_NAME=PropKit.use("config.txt").get("sys.redisName","cmda_redis");
	public final static String RC="_rc";//验证码
	public final static String MC="_mc";//手机验证码
	public final static String MC_TIMEOUT="_mc_time_out";//手机验证码的失效时间
	
	
	public static final int DEFAULT_PAGE_LIMIT = 10;
	
	
	//cookie值
	public final static String COOKIE_LOGIN_ID="login_id";
	public final static String COOKIE_SECRET_TOKEN="secret_token";
	public final static String COOKIE_REMEMBER_ME="cookie_remember_me";
	public final static String COOKIE_USER_ID = "user_id";
	public final static String COOKIE_EMAIL = "email";
	public final static String COOKIE_NICKNAME = "nickname";
	public final static String COOKIE_AVATAR = "avatar";
	public final static String COOKIE_LOGOUT = "logout";
	public final static String COOKIE_SOCIAL_SOURCE = "cookie_social_source";//第三方用户来源
	public final static String COOKIE_SOCIAL_SOURCE_SELF = "cookie_social_source_self";//用户来源 是本地
	public final static String COOKIE_SOCIAL_SOURCE_ZSYX = "cookie_social_source_zsyx";//用户来源 是掌上医讯
	public final static String COOKIE_SOCIAL_SOURCE_WX = "cookie_social_source_wx";//用户来源 微信
	
	
	//controller返回码
	public final static String STATUS="status";
	public final static String MSG="msg";
	//session值
	public final static String SESSION_SECURITYCONTAINER = "s_container";//用户登录的所有信息都放入当中
	public final static String SESSION_USER = "login_user";
	public final static String SESSION_IS_CON_ADMIN = "session_is_con_admin";//是否是会议后台管理人员
	public final static String SESSION_IS_SERVICE_ADMIN = "session_is_service_admin";//是否是现场服务管理人员
	public final static String SESSION_USER_PROFILE = "login_user_profile";
	public final static String JSESSIONID="JSESSIONID";
	public final static String SESSION_VALIDATECODE="validateCode";//验证码字段
	public final static String SESSION_SLIDE_VALIDATECODE="slide_validateCode";//滑动验证码字段
	public final static String SESSION_MOBILE_VC="session_mobilevc";//手机短信验证码字段
	public static final String SESSION_MOBILE_VC_TIMEOUT = "session_mobile_vc_timeout";//短信验证码失效
	public final static String SESSION_BK_USER = "bk_login_user";// FIXME LJF 要废弃！！！
	//account出 吧conuser放入session 把conuser放入session中,许多地方都修改conuser信息,导致conuser信息错乱,这个session值不可取
	public final static String SESSION_ACCOUNT_CONUSER = "session_conuser";
	public final static String SESSION_APP_ACCESSTOKEN = "session_app_accesstoken";//APP登录后的唯一session值
	
	
	//request 的attr值
	public final static String GLOBAL_ATTR_SITE="global_core_site";
	public final static String ATTR_USER="login_user";
	public final static String ATTR_USER_PROFILE="login_user_profile";
	public final static String ATTR_TO="to";
	public final static String LOGIN_CONNECT_TYPE_WEIBO="weibo";
	public final static String LOGIN_CONNECT_TYPE_QQ="qq";
	public final static String CONFERENCE="conference";
	public final static String CON_THEME_DIR="theme_dir";
	public final static String SIGN_USER = "sign_user";
	public final static String CON_TOPIC = "conTopic";//专题
	public final static String SECOND_COMPANY = "second_company";//分会
	public final static String SECOND_COMPANY_URL = "second_company_url";//分会
	public final static String TOPIC_BASE_PATH="t_base_path";//分会官网
	public final static String CONFERENCE_BASE_PATH="base_path";//会议官网
	
	
	
	//模板路径
	public final static String CON_TPL_DIR="template_dir";			//会议模板attr值名称
	public final static String TOPIC_TPL_DIR="topic_template_dir";	//专题模板attr值名称
	public final static String INDEX_TPL_DIR="site_path";			//平台模板attr值名称
	public final static String CON_TPL_WEB_BASE_PATH="/resources/tpl/";//会议
	public final static String TOPIC_DIR_BASE_PATH="/resources/topic/";//专题
	public final static String INDEX_DIR_BASE_PATH="/resources/index/";//平台首页
	public final static String TPL_INDEX_M_DEFAULT_DIR="newYht";		//平台手机默认模板
	public final static String TPL_CON_M_DEFAULT_DIR="conweb";			//会议手机默认模板
	public final static String TPL_TOPIC_M_DEFAULT_DIR="default";		//专题手机默认模板
	public final static String MOBILE_VIEW_PLACEHOLDER="${pla}";//placeholder占位符
	public final static String TPL_VIEW_PLACEHOLDER="${tpl}";//模板占位符
	public final static String MOBILE_VIEW_FOLDER="/m/";
	
	public final static String CMDA_TYPE_ID="society_2";  //中国医师协会类型id
	public final static String CMDA_SEC_TYPE_ID="society_2_1";//中国医师协会分会类型id
	
	public final static String APP_LOGIN_USER_PREFFIX="app_login_";
	
	//文件约束配置
	public final static String file_count_limit=PropKit.use("config.txt").get("file.count_limit","20");
	public final static String file_img_size_limit=PropKit.use("config.txt").get("file.img_size_limit","10");
	public final static String file_video_size_limit=PropKit.use("config.txt").get("file.video_size_limit","500");
	public final static String file_doc_size_limit=PropKit.use("config.txt").get("file.doc_size_limit","50");
	public final static String file_excel_size_limit=PropKit.use("config.txt").get("file.excel_size_limit","50");
	public final static String file_ppt_size_limit=PropKit.use("config.txt").get("file.ppt_size_limit","50");
	public final static String file_voice_size_limit=PropKit.use("config.txt").get("file.voice_size_limit","50");
	public final static String file_other_size_limit=PropKit.use("config.txt").get("file.other_size_limit","50");
	
	public final static String file_img_type=",jpg,gif,jpeg,bmp,ico,tif,tiff,jiff,png,";
	public final static String file_video_type=",wmv,mp4,rmvb,bmp,avi,tif,rm,mpeg,mpg,dat,mov,qt,asf,";
	public final static String file_doc_type=",doc,docx,pdf,docx,";
	public final static String file_excel_type=",xls,xlsx,";
	public final static String file_ppt_type=",ppt,pptx,pps,";
	public final static String file_voice_type=",wav,aif,au,mp3,ram,wma,mmf,amr,aac,flac,";
	public final static String file_other_type="*";
	
	
	//i18n字段
	public final static String I18N_ENGLISH="en_US";//英文
	public final static String I18N_CHINESE="zh_CN";//中文
	
	public final static Integer ADMIN_CON_SUPER=5;//超级会议管理员id
}
