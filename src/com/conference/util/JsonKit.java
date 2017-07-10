/*
 * Copyright (c) 2014 骆驼CMS               	
*/                                                                
  

package com.conference.util;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

/** 
 * 处理JSON的包
 * @author yongtree  
 * @date Mar 4, 2014 5:09:07 PM 
 * @version 1.0
 *  
 */
public class JsonKit extends com.jfinal.kit.JsonKit {

	public static Map<String,Object> json2map(String jsonText){
		return JSONObject.parseObject(jsonText, Map.class);
	}
	
	public static List<Map<String,Object>> json2listmap(String jsonText){
		List<Map<String, Object>> listMap = JSON.parseObject(jsonText, new TypeReference<List<Map<String,Object>>>(){}); 
		return listMap;
	}
	
	
	public static void main(String[] args) {
		json2map("{\"id\":2,\"name\":\"test\"}");
	}
	
}
