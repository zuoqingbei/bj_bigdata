package com.conference.util.validate;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/** 
* 类说明 ：java验证工具
		*@Null
		验证对象是否为空
		@NotNull
		验证对象是否为非空
		@AssertTrue
		验证 Boolean 对象是否为 true
		@AssertFalse
		验证 Boolean 对象是否为 false
		@Min
		验证 Number 和 String 对象是否大等于指定的值
		@Max
		验证 Number 和 String 对象是否小等于指定的值
		@DecimalMin
		验证 Number 和 String 对象是否大等于指定的值，小数存在精度
		@DecimalMax
		验证 Number 和 String 对象是否小等于指定的值，小数存在精度
		@Size
		验证对象（Array,Collection,Map,String）长度是否在给定的范围之内
		@Digits
		验证 Number 和 String 的构成是否合法
		@Past
		验证 Date 和 Calendar 对象是否在当前时间之前
		@Future
		验证 Date 和 Calendar 对象是否在当前时间之后
		@Pattern
		验证 String 对象是否符合正则表达式的规则
* @author  作者 : longjunfeng E-mail：463527083@qq.com
* @version 创建时间：2016年7月28日 上午10:03:21 
*/
public class ValidateUtils {
	
	private static Validator validator;  
     
    static {  
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();  
        validator = vf.getValidator();  
    }  
    
    
    /** 
     * @throws ValidationException  
     * @throws ValidationException   
     * @Description: 校验方法 
     * @param t 将要校验的对象 
     * @throws ValidationException  
     * void 
     * @throws  
     */   
    public static <T> void validate(T t) throws ValidationException{  
        Set<ConstraintViolation<T>> set =  validator.validate(t);  
        if(set.size()>0){  
            StringBuilder validateError = new StringBuilder();  
            for(ConstraintViolation<T> val : set){  
                validateError.append(val.getMessage() + " ;");  
            }  
//            System.out.println(validateError.toString());
            throw new ValidationException(validateError.toString());              
        }  
    }  
	
    
    public static void main(String[] args) {
		TestBean bean=new TestBean();
		bean.setName("11111");
//		bean.setMobile("11111");
		ValidateUtils.validate(bean);
		
	}
}
