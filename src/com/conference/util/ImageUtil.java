package com.conference.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/** 
* 类说明 ：图片工具类
*
* @author  作者 : longjunfeng E-mail：463527083@qq.com
* @version 创建时间：2016年4月19日 下午1:50:08 
*/
public class ImageUtil {

	
	/** 将图片文件转化为字节数组字符串，并对其进行Base64编码处理  
	 * @param imgFilePath
	 * @return
	 * @author longjunfeng
	 * @date   2016年4月19日下午1:51:32
	 */
	public static String GetImageStr(String imgFilePath) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理  
		byte[] data = null;

		// 读取图片字节数组  
		try {
			InputStream in = new FileInputStream(imgFilePath);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 对字节数组Base64编码  
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串  
	}

	/** 对字节数组字符串进行Base64解码并生成图片
	 * @param imgStr
	 * @param imgFilePath
	 * @return
	 * @author longjunfeng
	 * @date   2016年4月19日下午1:52:10
	 */
	public static boolean GenerateImage(String imgStr, String imgFilePath) {// 对字节数组字符串进行Base64解码并生成图片  
		if (imgStr == null) // 图像数据为空  
			return false;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码  
			byte[] bytes = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {// 调整异常数据  
					bytes[i] += 256;
				}
			}
			String descPath = imgFilePath.substring(0,imgFilePath.lastIndexOf("/"));
			File descPathFils=new File(descPath);
			if(!descPathFils.exists()){
				descPathFils.mkdir();
			}
			// 生成jpeg图片  
			File f=new File(imgFilePath);
			if(!f.exists()){
				f.createNewFile();
			}
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(bytes);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
