package com.conference.util.sys;

import java.util.HashMap;
import java.util.Map;

/** 
 * @Desc: 		用于把变量放到线程变量中去,方便各个地方进行取值
 * @ClassName: 	ThreadUtil 
 * @author:	 	longjunfeng
 * @date: 		2016年8月3日 上午11:02:54  
 */
public class ThreadUtil {
	
	private static final ThreadLocal<Map<String ,Object>> tl = new ThreadLocal<Map<String ,Object>>();
	
	public static void setThreadLocalAttr(String name ,Object value) {
		Map<String ,Object> map = tl.get();
		if(map==null){
			map=new HashMap<String, Object>();
		}
		map.put(name, value);
		tl.set(map);
	}
	
	public static void removeAllAttr() {
		tl.remove();
	}
	
	public static void removeAttr(String name) {
		Map<String ,Object> map = tl.get();
		if(map==null){
			map=new HashMap<String, Object>();
		}
		map.remove(name);
		tl.set(map);
	}
	
	public static Object get(String name) {
		Map<String ,Object> map = tl.get();
		if(map==null)
			return null;
		return map.get(name);
	}
	
}
