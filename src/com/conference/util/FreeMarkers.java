package com.conference.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * FreeMarkers工具类
 * @author ThinkGem
 * @version 2013-01-15
 */
public class FreeMarkers {

	public static String renderString(String templateString, Map<String, ?> model) {
		try {
			StringWriter result = new StringWriter();
			Template t = new Template("name", new StringReader(templateString), new Configuration());
			t.process(model, result);
			return result.toString();
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static String renderTemplate(Template template, Object model) {
		try {
			StringWriter result = new StringWriter();
			template.process(model, result);
			return result.toString();
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}
	
	
	
	
	/**
	 * @desc	:freemaker标签中过去参数,没值就返回null
	 * @author	:作者 longjunfeng E-mail:463527083@qq.com
	 * @version :创建时间 2017年1月10日
	 * @param <T>
	 * @param name		参数名称
	 * @param t			参数的类型
	 * @param params	参数map
	 * @param defaultValue	默认值
	 * @return
	 */
	public static String getPara(String name,Map params,Object defaultValue){
		Object value = params.get(name) == null ? null : params.get(name);
		if(value==null){
			if(defaultValue==null){
				return null;
			}else{
				return defaultValue.toString();
			}
		}else{
			return  value.toString();
		}
	}
	
	
	/**
	 * @desc	:freemaker标签中过去参数,没值就返回null,默认数值类型为String
	 * @author	:作者 longjunfeng E-mail:463527083@qq.com
	 * @version :创建时间 2017年1月10日
	 * @param name	参数名称
	 * @param params	参数map
	 * @return
	 */
	public static String getPara(String name, Map params){
		return  getPara(name, params, null);
	}
	
	/**
	 * @desc	:获取int
	 * @author	:作者 longjunfeng E-mail:463527083@qq.com
	 * @version :创建时间 2017年1月10日
	 * @param name
	 * @param params
	 * @return
	 */
	public static Integer getParamToInt(String name, Map params){
		return getParamToInt(name, params, null);
	}
	
	public static Integer getParamToInt(String name,Map params,Object defaultValue){
		String value = getPara(name, params, defaultValue);
		if(value!=null){
			return Integer.parseInt(value.trim());
		}
		return null;
	}
	
	/**
	 * @desc	:获取boolean
	 * @author	:作者 longjunfeng E-mail:463527083@qq.com
	 * @version :创建时间 2017年1月10日
	 * @param name
	 * @param params
	 * @return
	 */
	public static Boolean getParamToBoolean(String name, Map params){
		return getParamToBoolean(name, params, null);
	}
	
	public static Boolean getParamToBoolean(String name,Map params,Object defaultValue){
		String value = getPara(name, params, defaultValue);
		if(value!=null){
			return Boolean.parseBoolean(value.trim());
		}
		return null;
	}
	
	
//	public static Configuration buildConfiguration(String directory) throws IOException {
//		Configuration cfg = new Configuration();
//		Resource path = new DefaultResourceLoader().getResource(directory);
//		cfg.setDirectoryForTemplateLoading(path.getFile());
//		return cfg;
//	}
	
	public static void main(String[] args) throws IOException {
//		// renderString
//		Map<String, String> model = com.google.common.collect.Maps.newHashMap();
//		model.put("userName", "calvin");
//		String result = FreeMarkers.renderString("hello ${userName}", model);
//		System.out.println(result);
//		// renderTemplate
//		Configuration cfg = FreeMarkers.buildConfiguration("classpath:/");
//		Template template = cfg.getTemplate("testTemplate.ftl");
//		String result2 = FreeMarkers.renderTemplate(template, model);
//		System.out.println(result2);
		
//		Map<String, String> model = com.google.common.collect.Maps.newHashMap();
//		model.put("userName", "calvin");
//		String result = FreeMarkers.renderString("hello ${userName} ${r'${userName}'}", model);
//		System.out.println(result);
	}
	
}
