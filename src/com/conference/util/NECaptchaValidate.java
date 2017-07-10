package com.conference.util;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 
 * @className NECaptchaValidate.java
 * @time   2017年1月13日 上午10:25:36
 * @author zuoqb
 * @todo   网易易盾二次验证工具类
 * 用户使用网易易盾验证组件时，提交表单(post-form)到产品应用后台，会携带一个验证码相关的参数，参数名为 NECaptchaValidate。
 * 产品应用需要将该参数传给易盾验证码后台做二次校验，以确保该次验证是有效并且是最近完成的
 */
public class NECaptchaValidate {
	private static final String CAPTCHA_ID="5feffee1e9ea4b3dae507da4962be729";//验证码id 在captcha.tag中也使用了此数据
	private static final String SECRET_ID="77c26cb94da1ca1924218700e52b6615";//密钥对id
	private static final String SECRET_KEY="34d69f519fd473ba5de985fb6473b6c3";//产品私钥
	private static final String VERSION="v1";//版本信息，固定值v1
	private static final String VALIDATE_URL="http://c.dun.163yun.com/api/v1/verify";//验证接口
	/**
	 * 
	 * @time   2017年1月13日 上午10:53:59
	 * @author zuoqb
	 * @todo   二次校验
	 * @param  @param nECaptchaValidate
	 * @param  @return
	 * @return_type   boolean
	 */
	public static boolean validateNECaptcha(String nECaptchaValidate){
		Map<String, String> params=new HashMap<String, String>();
		params.put("captchaId",CAPTCHA_ID);
		params.put("validate",nECaptchaValidate);//提交二次校验的验证数据，即NECaptchaValidate值
		params.put("user","");//用户信息，值可为空
		params.put("secretId",SECRET_ID);
		params.put("version",VERSION);
		params.put("timestamp",getTime(new Date())+"");
		params.put("nonce",random(1, 1000)+"");//随机正整数，与 timestamp 联合起来，用于防止重放攻击
		String signature=genSignature(params);
		params.put("signature",signature);
		String returnJson=HttpClientUtil.sendPostRequest(VALIDATE_URL, params);
		//失败返回结构{"c":0,"e":400,"m":"BAD_REQUEST"}
		//成功返回结构{"msg":0,"result":true,"c":1,"error":0}
		if(returnJson==null||returnJson.indexOf("BAD_REQUEST")!=-1){
			//验证失败
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @time   2017年1月13日 上午10:55:18
	 * @author zuoqb
	 * @todo   生成签名信息
	 * @param  接口请求参数名和参数值map，不包括signature参数名
	 * @return_type   String
	 */
	public static String genSignature(Map<String, String> params){
		try {
			 // 1. 参数名按照ASCII码表升序排序
		    String[] keys = params.keySet().toArray(new String[0]);
		    Arrays.sort(keys);

		    // 2. 按照排序拼接参数名与参数值
		    StringBuilder sb = new StringBuilder();
		    for (String key : keys) {
		        sb.append(key).append(params.get(key));
		    }
		    // 3. 将secretKey拼接到最后
		    sb.append(SECRET_KEY);

		    // 4. MD5是128位长度的摘要算法，转换为十六进制之后长度为32字符
		    return DigestUtils.md5Hex(sb.toString().getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	 * @time   2017年1月13日 上午10:39:39
	 * @author zuoqb
	 * @todo   获取Linux时间戳
	 * @return_type   long
	 */
	public static long getTime(Date date){
		long timestamp = date.getTime()/1000;
		return timestamp;
	}
	/**
	 * 
	 * @time   2017年1月13日 上午10:43:23
	 * @author zuoqb
	 * @todo   获取随机数
	 * @param  @param min
	 * @param  @param max
	 * @return_type   int
	 */
	public static int random(int min,int max){
        Random random = new Random();
        int num = random.nextInt(max)%(max-min+1) + min;
        return num;
	}
}
