package com.conference.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.jfinal.kit.JsonKit;

/**
 * AES(Advanced Encryption Standard)：高级加密标准，是下一代的加密算法标准，速度快，安全级别高
 * @author yangtao
 *
 */
public class AESUtil {
	
	/**
	 * 加密
	 * @param data
	 * @param seed
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("restriction")
	public static String encrypt(String data,String seed) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        int blockSize = cipher.getBlockSize();
        byte[] dataBytes = data.getBytes("utf-8");
        int plaintextLength = dataBytes.length;
        if (plaintextLength % blockSize != 0) {
            plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
        }
        byte[] plaintext = new byte[plaintextLength];
        System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

        SecretKeySpec keyspec = new SecretKeySpec(seed.getBytes("utf-8"), "AES");
        IvParameterSpec ivspec = new IvParameterSpec(seed.getBytes("utf-8"));
        cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
        byte[] encrypted = cipher.doFinal(plaintext);
        return new sun.misc.BASE64Encoder().encode(encrypted);
    }
	
	/**
	 * 解密
	 * @param data
	 * @param seed
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
    @SuppressWarnings("restriction")
	public static String decrypt(String data,String seed) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
        byte[] encrypted1 = new sun.misc.BASE64Decoder().decodeBuffer(data);

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec keyspec = new SecretKeySpec(seed.getBytes(), "AES");
        IvParameterSpec ivspec = new IvParameterSpec(seed.getBytes());

        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original,"utf-8");
        return originalString.trim();
    }
    
    
	
    /** 
     * @Desc: 把参数转换为,json格式的加密数据
     * @param dataMap
     * @param privatekey
     * @return
     * @return: String
     * @author: longjunfeng   
     * @date: 2016年11月1日 上午10:30:17 
     */
    public static String paramsToDecryptData(Map<String, Object> dataMap,String privatekey){
    	String data = JsonKit.toJson(dataMap);//把dataMap转换为json
		String dataEncrypt="";
		try {
			dataEncrypt = AESUtil.encrypt( data, privatekey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataEncrypt;
    }
    
    public static void main(String[] args) {
    }


}