package com.conference.util.validate;


/** 
* 类说明 ：正则验证集合 都放在这里面,自己不用再写了 直接调用
*
* @author  作者 : longjunfeng E-mail：463527083@qq.com
* @version 创建时间：2016年7月28日 上午10:03:21 
*/
public class ValidatePatterns {
	
	
	//-------------------------------------时间正则区域--------------------------------------------------
    
	/**日期正则验证 2016-05-11样式验证
     */
    public static final String DATE_YYYY_MM_DD="^\\d{4}(\\-|\\/|\\.)\\d{1,2}\\1\\d{1,2}$";
    
    /**日期正则验证 2016-05-11样式验证或者为空
     */
    public static final String DATE_YYYY_MM_DD_OR_EMPTY="^\\d{4}(\\-|\\/|\\.)\\d{1,2}\\1\\d{1,2}|\\s $";
    
    /**时间正则验证 05:03样式
     */
    public static final String DATE_HH_MM="^(([0-1]\\d)|(2[0-4])):[0-5]\\d$"; 
    

    
    
    
    
    
    
    
    //-----------------------------------------------数字正则区域-------------------------------------
    
    
    /**正整数或者空正则
     */
    public static final String NUMBER_POSITIVE_EMPTY="^[0-9]*[1-9][0-9]*|\\s $"; 
    
//    "^\\d+$"　　                  //非负整数（正整数 + 0）  
//    "^[0-9]*[1-9][0-9]*$"　　 //正整数  
//    "^((-\\d+)|(0+))$"　　     //非正整数（负整数 + 0）  
//    "^-[0-9]*[1-9][0-9]*$"　　//负整数  
//    "^-?\\d+$"　　　　         //整数  
//    "^\\d+(\\.\\d+)?$"　　       //非负浮点数（正浮点数 + 0）  
//    "^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$"　　//正浮点数  
//    "^((-\\d+(\\.\\d+)?)|(0+(\\.0+)?))$"　　//非正浮点数（负浮点数 + 0）  
//    "^(-(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*)))$"　　//负浮点数  
//    "^(-?\\d+)(\\.\\d+)?$"　　//浮点数  
    
    
    // 匹配所有的手机号 老的
 	public final String PATTERN_MOBILENUM = "^0{0,1}1(3|5|8)[0-9]{9}$";
 	// 匹配移动手机号老的
 	public final String PATTERN_CMCMOBILENUM = "^1(3[4-9]|5[012789]|8[2378])\\d{8}$";
 	// 匹配电信手机号老的
 	public final String PATTERN_CTCMOBILENUM = "^1(3[3]|5[3]|8[09])\\d{8}$";
 	// 匹配联通手机号老的
 	public final String PATTERN_CUTMOBILENUM = "^1(3[0-2]|5[56]|8[56])\\d{8}$";
    
    
    
    public static void main(String[] args) {
		
	}
    
    
}