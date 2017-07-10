/*
 * Copyright (c) 2014. 骆驼CMS
 */

package com.conference.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfinal.kit.PropKit;


/** 
 * 类说明 ：会议个 分会域名检测 是否可用
 * @author 	作者 : longjunfeng E-mail：463527083@qq.com
 * @version 创建时间：2016年1月22日 下午2:12:41 
 */
public class DomainUtil {
	
	/**
	 * 会议域名是否可用
	 * @param userDomain
	 * @return
	 * @author longjunfeng	true可用 false不可以
	 * @date   2016年1月22日下午2:14:15
	 */
	public static boolean isConferenceDomainCanUse(String userDomain){
		String[] retentionDomains=PropKit.use("config.txt").get("retention.domain").split(",");
		for (String retentionDomain : retentionDomains) {
			if(retentionDomain.equalsIgnoreCase(userDomain)){
				return false;
			}
		}
		return true;
	}
	
	/**分会域名是否可用
	 * @param userDomain
	 * @return
	 * @author longjunfeng
	 * @date   2016年1月22日下午2:14:50
	 */
	public static boolean isConTopicDomainCanUse(String userDomain){
		String[] retentionDomains=PropKit.use("config.txt").get("retention.topic.domain").split(",");
		for (String retentionDomain : retentionDomains) {
			if(retentionDomain.equalsIgnoreCase(userDomain)){
				return false;
			}
		}
		return true;
	}
	
	
	/** 
	 * @Desc: 获取url的主域名
	 * @param url
	 * @return
	 * @return: String
	 * @author: longjunfeng   
	 * @date: 2016年11月16日 上午11:14:20 
	 */
	public static String analysDomain(String url){
		Pattern p = Pattern.compile("[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)",Pattern.CASE_INSENSITIVE);
		Matcher matcher = p.matcher(url);
		matcher.find();
		String domain = matcher.group();
		System.out.println(domain); 
		return domain;
	}
	
	public static void main(String[] args) {
		String urlString="http://www.linuxidc.com/entry/4545/0/";
		analysDomain(urlString);
	}
	
}
