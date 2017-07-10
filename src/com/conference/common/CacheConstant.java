package com.conference.common;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

/**
 * 项目中的缓存名称
 *
 * @author Chengzl
 * @date 2014-6-27 上午11:30:24 
 * @version 1.0
 *
 */
public class CacheConstant {
	
	public  static Prop HUANXIN_PROP = PropKit.use("huanxinConfig.properties");//环信配置文件
	
	
	public final static String CACHE_CATE_NAME="category";//分类缓存名称
	public final static String CACHE_CATE_KEY="first_cat";//分类缓存名称
	

	public final static String CACHE_NAV_NAME="navlist";//导航缓存名称
	public final static String CACHE_NAV_KEY="nav";//导航缓存名称
	
	public final static String CACHE_MEDIA_NAME="media";//导航缓存名称
	public final static String CACHE_MEDIA_KEY="media";//导航缓存名称

	public final static String CACHE_NOTICE_USER_NAME="notice_user";//报名用户
	public final static String CACHE_NOTICE_USER_KEY="notice_user";//报名用户
	
	
	public final static String CACHE_BASEDATA="base_data";//基础数据缓存--  职称、职别
	
	public final static String CONTENT_LIST="content_list";//俱乐部列表
	
	public final static String CACHE_CON_TOP="con_top";

}
