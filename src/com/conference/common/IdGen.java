/**
 
 */
package com.conference.common;

import java.security.SecureRandom;
import java.util.UUID;

import oecp.framework.util.RandomGenerator;

import com.conference.util.Encodes;

/**
 * 封装各种生成唯一性ID算法的工具类.
 * 数据库主键生成策略类
 */
public class IdGen  {

	private static SecureRandom random = new SecureRandom();
	/**
	 * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * 使用SecureRandom随机生成Long. 
	 */
	public static long randomLong() {
		return Math.abs(random.nextLong());
	}

	
	/**
	 * 基于Base62编码的SecureRandom随机生成bytes.
	 */
	public static String randomBase62(int length) {
		byte[] randomBytes = new byte[length];
		random.nextBytes(randomBytes);
		return Encodes.encodeBase62(randomBytes);
	}
	
	/**随机生成随机的16位ID
	 * @return
	 * @author longjunfeng
	 * @date   2016年4月8日上午9:26:57
	 */
	public static String random16UniqueKey(){
		return RandomGenerator.get16UniqueKey();
	}
	
	public static void main(String[] args) {
		System.out.println(IdGen.uuid());
		System.out.println(IdGen.uuid().length());
	}

}
