/**
 
 */
package com.conference.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;

/**
 * 用户代理字符串识别工具
 * @author ThinkGem
 * @version 2014-6-13
 */
public class UserAgentUtils {

	/**
	 * 获取用户代理对象
	 * @param request
	 * @return
	 */
	public static UserAgent getUserAgent(HttpServletRequest request){
		return UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
	}
	
	/**
	 * 获取设备类型
	 * @param request
	 * @return
	 */
	public static DeviceType getDeviceType(HttpServletRequest request){
		return getUserAgent(request).getOperatingSystem().getDeviceType();
	}
	
	/**
	 * 是否是PC
	 * @param request
	 * @return
	 */
	public static boolean isComputer(HttpServletRequest request){
		return DeviceType.COMPUTER.equals(getDeviceType(request));
	}

	/**
	 * 是否是手机
	 * @param request
	 * @return
	 */
	public static boolean isMobile(HttpServletRequest request){
		return DeviceType.MOBILE.equals(getDeviceType(request));
	}
	
	/**
	 * 是否是平板
	 * @param request
	 * @return
	 */
	public static boolean isTablet(HttpServletRequest request){
		return DeviceType.TABLET.equals(getDeviceType(request));
	}

	/**
	 * 是否是手机和平板
	 * @param request
	 * @return
	 */
	public static boolean isMobileOrTablet(HttpServletRequest request){
		DeviceType deviceType = getDeviceType(request);
		return DeviceType.MOBILE.equals(deviceType) || DeviceType.TABLET.equals(deviceType);
	}
	
	
	  /**
     * 是否是微信
     * @author yongtree
     * @date Mar 12, 20142:19:20 PM
     * @param request
     * @return
     */
    public static boolean isWeixin(HttpServletRequest request){
    	String userAgent = request.getHeader("user-agent");
    	if(userAgent.contains("MicroMessenger")){
    		return true;
    	}else{
    		return false;
    	}
    }
	
	/**
	 * 获取浏览类型
	 * @param request
	 * @return
	 */
	public static Browser getBrowser(HttpServletRequest request){
		return getUserAgent(request).getBrowser();
	}
	
	/**
	 * 是否IE版本是否小于等于IE8
	 * @param request
	 * @return
	 */
	public static boolean isLteIE8(HttpServletRequest request){
		Browser browser = getBrowser(request);
		return Browser.IE5.equals(browser) || Browser.IE6.equals(browser)
				|| Browser.IE7.equals(browser) || Browser.IE8.equals(browser);
	}
	
	/**
	 * 是否IE版本是否小于等于IE8
	 * @param request
	 * @return
	 */
	public static boolean isIE(HttpServletRequest request){
		Browser browser = getBrowser(request);
		return Browser.IE5.equals(browser) || Browser.IE6.equals(browser)
				|| Browser.IE7.equals(browser) || Browser.IE8.equals(browser)
				|| Browser.IE9.equals(browser) || Browser.IE10.equals(browser)
				|| Browser.IE11.equals(browser) || Browser.IE.equals(browser);
	}
	
	
	/** 
	 * @Desc: 是否是ajax请求
	 * @param request
	 * @return: void
	 * @author: longjunfeng   
	 * @date: 2016年9月29日 上午11:41:03 
	 */
	public static boolean isAjaxRequest(HttpServletRequest request){
		Enumeration<String> headers = request.getAttributeNames();
		while (headers.hasMoreElements()) {
			String string = (String) headers.nextElement();
			if(string.equals("x-requested-with")){
				return true;
			}
		}
		return false;
	}
	
}
