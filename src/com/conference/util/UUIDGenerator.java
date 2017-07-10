package com.conference.util;

import java.util.UUID;

/** 
* 类说明 ：
*
* @author  作者 : longjunfeng E-mail：463527083@qq.com
* @version 创建时间：2016年9月2日 上午9:29:31 
*/
public class UUIDGenerator {

	public UUIDGenerator() {
	}

	/** 
	 * 获得一个UUID 
	 * @return String UUID 
	 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		//去掉“-”符号 
		return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
	}
	
	/** 
	 * 获得一个UUID 
	 * @return String UUID 
	 */
	/** 
	 * @Desc: 获得一个制定长度的UUID 
	 * @param longCount
	 * @return
	 * @return: String
	 * @author: longjunfeng   
	 * @date: 2016年9月2日 上午9:32:24 
	 */
	public static String getUUID(Integer longCount) {
		return getUUID().substring(0, longCount);
	}

	/** 
	 * 获得指定数目的UUID 
	 * @param number int 需要获得的UUID数量 
	 * @return String[] UUID数组 
	 */
	public static String[] getUUIDBatch(int number) {
		if (number < 1) {
			return null;
		}
		String[] ss = new String[number];
		for (int i = 0; i < number; i++) {
			ss[i] = getUUID();
		}
		return ss;
	}
	
	

	public static void main(String[] args) {
//		String[] ss = getUUID(4);
//		for (int i = 0; i < ss.length; i++) {
//			System.out.println(ss[i]);
//		}
		System.out.println(getUUID(3).toString());
	}
	
	
}
