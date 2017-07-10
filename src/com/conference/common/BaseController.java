/**
 * 
 */
package com.conference.common;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import oecp.framework.util.FreeMarkerUtil;
import oecp.framework.util.RandomGenerator;

import com.conference.util.ExUploadFile;
import com.conference.util.StringKit;
import com.conference.util.TwoDimensionCode;
import com.jfinal.core.Controller;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

/**
 * 抽象的基础控制类，主要包括  
 * 
 * @author yongtree
 */
public  class BaseController extends Controller implements Constant {

	

	
	
	
	
	/** 
	 * @Desc: 获取request中带有 什么什么.的参数,如  conference.name,类似与getModel
	 * @param modelName
	 * @return
	 * @return: Record
	 * @author: longjunfeng   
	 * @date: 2016年8月18日 上午8:56:41 
	 */
	public Record getRecord(String modelName) {
		String modelNameAndDot = modelName + ".";
		Record model = new Record();
		boolean exist = false;
		Map<String, String[]> parasMap = getRequest().getParameterMap();
		for (Entry<String, String[]> e : parasMap.entrySet()) {
			String paraKey = e.getKey();
			if (paraKey.toLowerCase().startsWith(modelNameAndDot.toLowerCase())) {
				String paraName = paraKey.substring(modelNameAndDot.length());
				String[] paraValue = e.getValue();
				Object value = StrKit.notBlank(paraValue[0]) ? (paraValue.length == 1 ? paraValue[0]
						: StringKit.array2str(paraValue, ","))
						: null;
				
				if(value!=null){
					model.set(paraName, value);
					exist = true;
				}
			}
		}
		if (exist) {
			return model;
		} else {
			return null;
		}
	}
	
	
	
	
//------------------------------------------------------特殊工具区域------------------------------------------------------	
	
	/**
	 * 返回注册 的跨域 JSONP
	 * @author longjunfeng
	 * @date   2015年12月18日上午9:29:33
	 */
	public void renderForReg(){
		if(isParaExists("jsonpCallback")){
			Enumeration<String> names=this.getAttrNames();
			Record record = new Record();
			while(names.hasMoreElements()){
				String name = names.nextElement();
				record.set(name, getAttr(name));
			}
			renderHtml(getPara("jsonpCallback")+"("+record.toJson()+")");
		}else{
			renderJson();
		}
	}
	
	
	protected String convertFreemarker(String t_name, Map data) {
		return FreeMarkerUtil.analysisTemplate(
				getRequest().getRealPath("/ftl"), t_name, "UTF-8", data);
	}
	

	
	
	
	
	/**
	 * 得到上传的文件，适当扩展
	 * 
	 * @param parameterName
	 * @param customDir
	 *            自定义目录
	 * @param suffixes 文件后缀  例如jpg,png,gif
	 * @return 文件的相对路径
	 */
	@Deprecated
	protected ExUploadFile updateLocalFile(String parameterName, String customDir,String suffixes) {
		String newDir = getRequest().getSession().getServletContext().getRealPath("/") + "/upload/"+ customDir;
		File dir = new File(newDir);
		if ( !dir.exists()) {
			if (!dir.mkdirs()) {
				throw new RuntimeException("Directory " + newDir + " not exists and can not create directory.");
			}
		}
		//把所有文件放到newdir目录下，并得到parameterName文件名的文件
		UploadFile uf = getFile(parameterName, newDir);
		if (uf != null) {
			String filename = uf.getFileName();
			String suffix = filename.substring(filename.lastIndexOf(".")+1);
			filename = RandomGenerator.get16UniqueKey() + "." + suffix;
			File newFile = new File(newDir + "/" + filename);
			uf.getFile().renameTo(newFile);
			String path = "/upload/" + customDir + "/" + filename;
			//获得扩展信息的文件
			return new ExUploadFile(suffix, path, filename,uf.getOriginalFileName(), newDir, newFile);
		} else {
			return null;
		}
	}
	

	
	
	
	

	
	
	
	
	
	

	
	
	
	/** 
	 * @Desc: 生成本地二维码图片
	 * @param encoderContent
	 * @param errorCorrect	L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
	 * @param size			大小	取值范围1-40，值越大尺寸越大，可存储的信息越大	
	 * @return
	 * @return: String
	 * @author: longjunfeng   
	 * @date: 2016年8月2日 下午2:29:21 
	 */
	public String gen2DimensionCodeImageLocal(String encoderContent,String errorCorrect, int size) {
		try{
			String key=RandomGenerator.get16UniqueKey();
			String realpath= getRequest().getSession().getServletContext().getRealPath("/") + File.separator+"upload"+ File.separator+ "dimensionCode";
			File f1 = new File(realpath);
			if(!f1.exists()){
				f1.mkdirs();;
			}
			String filePath = realpath+ File.separator+key+".png";
			TwoDimensionCode handler = new TwoDimensionCode();
			handler.encoderQRCode(encoderContent, filePath, "png", errorCorrect, size);
			return File.separator+"upload"+ File.separator+ "dimensionCode"+ File.separator+key+".png";
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	
	
//-------------------------------------------------controller工具----------------------------------------------------------
	//处理cookie
	@Override
	public Controller setCookie(String name, String value, int maxAgeInSeconds,
			String path) {
		return setCookie(name, value, maxAgeInSeconds, path, null);
	}

	public Controller setCookie(String name, String value, int maxAgeInSeconds,String path, String domain) {
		if (value != null) {
			try {
				value = URLEncoder.encode(value, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAgeInSeconds);
		if (path == null) {
			path = "/";
		}
		cookie.setPath(path);
		if (domain != null) {
			cookie.setDomain(domain);
		}
		this.getResponse().addCookie(cookie);
		return this;
	}

	@Override
	public String getCookie(String name) {
		String value = super.getCookie(name);
		if (value != null) {
			try {
				value = URLDecoder.decode(value, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
		
		
	/**
	 * 退出登录清楚session数据
	 * @author Chengzl
	 * @date 2013-10-28上午10:11:17
	 */
	public void clearSession() {
		HttpSession session = getSession(false);
		if (session != null) {
			Enumeration<String> e = session.getAttributeNames();
			while (e.hasMoreElements()) {
				session.removeAttribute(e.nextElement());
			}
		}
	}
	
	/**
	 * ！！！最好用下面的setCommonMsg
	 * 获取返回的jsonMap
	 * @author Chengzl
	 * @date 2014-4-18上午08:58:24
	 * @param success
	 * @param msg
	 * @return
	 */
	@Deprecated
	public  Map getRenderMap(boolean success,String msg){
		Map map=new HashMap();
		map.put("success",success);
		map.put("msg", msg);
		return map;
	}
	
	

	/** 
	 * @Desc: 前台表单validaform插件的 ajaxurl 检验的返回json串封装
	 * @param status
	 * @param info
	 * @return: void
	 * @author: longjunfeng   
	 * @date: 2016年8月1日 上午11:39:51 
	 */
	public void renderJsonValidFormAjax(String status,String info){
		renderJson("{\"info\":\""+info+"\",\"status\":\""+status+"\"}");
	}
	
	
	/** 
	 * @Desc:  前台表单validaform插件的 ajaxurl 检验的返回json串封装,
	 * 			进行国际化改版
	 * @param status	y返回成功  ,n返回失败
	 * @param infoCode	国际化文件里面的code
	 * @return: void
	 * @author: longjunfeng   
	 * @date: 2016年11月22日 下午3:57:54 
	 */
	public void renderJsonValidFormAjaxI18n(String status,String infoCode){
		String msg=((Res)getAttr("_res")).get("msg." + infoCode);
		renderJson("{\"info\":\""+msg+"\",\"status\":\""+status+"\"}");
	}
	
	/**
	 * 输出错误信息JSON信息，后台返回都用此格式：
	 * 如 {status:success msg:成功 }
	 * @param status_code
	 * @author longjunfeng
	 * @return 
	 * @date   2015年12月17日下午5:21:15
	 */
	public Map<String, String> setCommonMsg(String status_code) {
		this.setAttr(Constant.STATUS, status_code);
		this.setAttr(Constant.MSG,I18n.use().get("msg." + status_code));
		Map<String, String> returnMsgMap=new HashMap<String, String>();
		returnMsgMap.put(Constant.STATUS, status_code);
		returnMsgMap.put(Constant.MSG, I18n.use().get("msg." + status_code));
		return returnMsgMap;
	}
	
	
	/**
	 * 输出错误信息JSON信息，只返回成功
	 * 如 {status:success msg:成功 }
	 * @param msg
	 * @author longjunfeng
	 * @return 
	 * @date   2015年12月17日下午5:23:14
	 */
	public Map<String, String> setCommonMsgSuccess(String msg) {
		this.setAttr(Constant.STATUS, "success");
		this.setAttr(Constant.MSG, msg);
		Map<String, String> returnMsgMap=new HashMap<String, String>();
		returnMsgMap.put(Constant.STATUS, "success");
		returnMsgMap.put(Constant.MSG, msg);
		return returnMsgMap;
	}
	
	/** 
	 * @Desc: 国际化返回成功信息
	 * @param status_code
	 * @return
	 * @return: Map<String,String>
	 * @author: longjunfeng   
	 * @date: 2016年11月17日 下午4:53:59 
	 */
	public Map<String, String> setCommonMsgSuccessI18n(String status_code) {
		String msg=((Res)getAttr("_res")).get("msg." + status_code);
		this.setAttr(Constant.STATUS, "success");
		this.setAttr(Constant.MSG,msg);
		Map<String, String> returnMsgMap=new HashMap<String, String>();
		returnMsgMap.put(Constant.STATUS, "success");
		returnMsgMap.put(Constant.MSG, msg);
		return returnMsgMap;
	}
	
	/** 
	 * @Desc: 国际化返回失败信息
	 * @param status_code
	 * @return
	 * @return: Map<String,String>
	 * @author: longjunfeng   
	 * @date: 2016年11月17日 下午4:54:17 
	 */
	public Map<String, String> setCommonMsgErrorI18n(String status_code) {
		String msg=((Res)getAttr("_res")).get("msg." + status_code);
		this.setAttr(Constant.STATUS, "error");
		this.setAttr(Constant.MSG,msg);
		Map<String, String> returnMsgMap=new HashMap<String, String>();
		returnMsgMap.put(Constant.STATUS, "error");
		returnMsgMap.put(Constant.MSG, msg);
		return returnMsgMap;
	}
	
	/**
	 *  输出错误信息JSON信息，只返回错误
	 *   如 {status:error msg:失败 }
	 * @param msg
	 * @author longjunfeng
	 * @return 
	 * @date   2015年12月18日上午10:05:49
	 */
	public Map<String, String> setCommonMsgError(String msg) {
		this.setAttr(Constant.STATUS, "error");
		this.setAttr(Constant.MSG, msg);
		Map<String, String> returnMsgMap=new HashMap<String, String>();
		returnMsgMap.put(Constant.STATUS, "error");
		returnMsgMap.put(Constant.MSG, msg);
		return returnMsgMap;
	}
	
	/**
	 *  输出提示信息JSON信息，只返回提示信息
	 *   如 {status:info msg:请先登录}
	 * @param msg
	 * @author longjunfeng
	 * @return 
	 * @date   2015年12月18日上午10:05:49
	 */
	public Map<String, String> setCommonMsgInfo(String msg) {
		this.setAttr(Constant.STATUS, "info");
		this.setAttr(Constant.MSG, msg);
		Map<String, String> returnMsgMap=new HashMap<String, String>();
		returnMsgMap.put(Constant.STATUS, "info");
		returnMsgMap.put(Constant.MSG, msg);
		return returnMsgMap;
	}

	/**
	 * 获取请求路径参数
	 * ?a=111&c=2222
	 * @return
	 * @author longjunfeng
	 * @date   2015年12月17日下午5:39:33
	 */
	public String getParasUrl() {
		String parasUrl = null;
		if (getParaMap() != null && getParaMap().size() > 0) {
			Set<String> keySet = getParaMap().keySet();
			parasUrl = "?";
			for (String key : keySet) {
				parasUrl = parasUrl + key + "=" + getParaMap().get(key)[0]+ "&";
			}
			parasUrl = parasUrl.substring(0, parasUrl.length() - 1);
		}
		return parasUrl;
	}

	/**
	 * 获取整个请求路径
	 * http://www.baidu.com?a=bbb
	 * @return
	 * @author longjunfeng
	 * @date   2015年12月17日下午5:39:44
	 */
	public String getRequestUrl() {
		return getRequest().getRequestURL()+ (getParasUrl() == null ? "" : getParasUrl());
	}
	
	/**获取微信JS权限的url
	 * @return
	 * @author longjunfeng
	 * @date   2015年12月24日上午8:40:18
	 */
	public String getWeixinJsUrl(){
		 return getUrl(this.getRequestUrl(),this.getRequest());
	}
	
	private String getUrl(String url,HttpServletRequest request){
		if(url.contains("from") && url.contains("isappinstalled"))
			return getUrl(request);
		else
			return url;
	}
	
	private String getUrl(HttpServletRequest request){
		StringBuffer url = request.getRequestURL();
		Map<String, String[]> parms = request.getParameterMap();
		url.append("?from=");
		url.append(parms.get("from")[0]);
		url.append("&isappinstalled=");
		url.append(parms.get("isappinstalled")[0]);
		return url.toString();
	}
	
	
	/**
	 * 获取IP地址
	 * @return
	 * @author longjunfeng
	 * @date   2015年12月17日下午5:44:09
	 */
	public String getIpAddr() {
        HttpServletRequest request = this.getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
	
	/**
	 * 获取当前域名htt://ww.doctorpda.cn
	 * @return
	 * @author longjunfeng
	 * @date   2015年12月17日下午5:45:52
	 */
	public String getDomain() {
		String path = getRequest().getContextPath();
		String basePath = null;
		if (getRequest().getServerPort() == 80) {
			basePath = getRequest().getScheme() + "://"+ getRequest().getServerName();
		} else {
			basePath = getRequest().getScheme() + "://"+ getRequest().getServerName() + ":"+ getRequest().getServerPort() + path;
		}
		return basePath;
	}
	
 
   
	
	
	


}
