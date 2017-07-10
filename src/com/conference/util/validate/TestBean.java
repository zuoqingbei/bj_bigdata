package com.conference.util.validate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/** 
* 类说明 ：
*
* @author  作者 : longjunfeng E-mail：463527083@qq.com
* @version 创建时间：2016年7月28日 上午10:01:05 
*/
public class TestBean {

	@NotNull(message = "用户ID不能为空")  
	private String name;
	
	@NotNull(message = "手机号不能为空")  
	@Size(min=1, max=10, message="手机名必须是1-11位之间")
	private String mobile;
	
	private String email;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
	
}
