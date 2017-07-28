package com.conference.admin.model;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import oecp.framework.util.MD5Util;
import oecp.framework.util.RandomGenerator;

import com.conference.common.BaseModel;
import com.jfinal.kit.PropKit;

public class SysUser extends BaseModel<SysUser>{
	private static final long serialVersionUID = -8811409081417389372L;
	public static final SysUser dao=new SysUser();
	public static final int INVALID=-1;//无效  
	public static final int NO_ACTIVE=0;//未激活
	public static final int VALID=1;//有效（已激活）
	
	public static final String DEFAULT_PASSWORD="123456";//默认密码
	/**
	 * 
	 * @time   2017年7月27日 下午1:47:35
	 * @author zuoqb
	 * @todo  判断是否是超级管理员
	 */
	public static boolean isSuperSysUser(SysUser sysUser){
		String superAccount=PropKit.use("config.txt").get("super.sysuser");
		try {
			//邮箱 电话都是1348898564@qq.com的是超管
			if(superAccount.equals(sysUser.getStr("email"))&&superAccount.equals(sysUser.getStr("mobile"))){
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
	/**
	 * 
	 * @time   2017年7月27日 下午2:58:22
	 * @author zuoqb
	 * @todo   根据用户名密码检查用户
	 */
	public static SysUser getSysUser(String account,String password){
		return dao.findFirst("select * from bigdata_sys_user where (mobile=? or login_name=? or email=?) and password=?  ",account, account, account,MD5Util.getStringMD5(password));
	}
	public static SysUser getSysUserByLoginId(String login_id){
		return dao.findFirst("select * from bigdata_sys_user where mobile=? or email=? or login_name=?", login_id,login_id,login_id);
	}
	/** 
	 * @Desc: 判断一下此账号是否可以登录或者注册   ，账户被禁用是不可以的
	 * @param loginId	
	 * @return
	 * @return: boolean	true 可以登录或者注册 false 不可以
	 * @author: zuoqb
	 * @date:2017年7月27日15:04:19
	 */
	public  boolean canRegOrLogin(String loginId){
		if(StringUtils.isBlank(loginId)){
			return true;
		}
		SysUser account=SysUser.dao.findFirst("select * from bigdata_sys_user where (mobile=? or email=? or login_name=?) and login_flag=?",loginId,loginId, loginId,SysUser.VALID);
	    if(account==null){
	    	account=SysUser.dao.findFirst("select * from bigdata_sys_user where (mobile=? or email=? or login_name=?) and login_flag=?",loginId,loginId, loginId,SysUser.INVALID);
	    	if(account!=null){
				return false;
			}
	    }
	    return true;
	}
	
	/**
	 * 根据手机号或昵称登录 
	 * @param account	手机号或者昵称
	 * @param password	本身就是md5 的密码
	 * @return
	 * @author zuoqb
	 * @date   2017年7月27日15:26:16
	 */
	public static SysUser getSysUserMD5Pwd(String account,String password){
		return dao.findFirst("select * from bigdata_sys_user where (mobile=? or login_name=? or email=?) and password=? and login_flag=?",account, account, account,password,SysUser.VALID);
	}
	/**
	 * 
	 * @time   2017年7月27日 下午3:46:58
	 * @author zuoqb
	 * @todo   TODO
	 */
	public static SysUser saveOrupdateSysUser(SysUser account){
		if(account.get("id")!=null){
			account.update();
			//更新其他附属数据
		}else{
			String id=RandomGenerator.get16UniqueKey();
			account.set("id", id);
			account.set("login_flag", SysUser.VALID);
			account.set("create_date", new Date());
			account.save();
		}
		return account;
	}
	/**
	 * 
	 * @time   2017年7月27日 下午3:46:54
	 * @author zuoqb
	 * @todo   TODO
	 */
	public  SysUser getByLoginId(String login_id,String field){
		return dao.findFirst("select * from bigdata_sys_user where "+field+"=?",login_id);
	}
	
	/**
	 * 
	 * @time   2017年7月27日 下午3:47:08
	 * @author zuoqb
	 * @todo   * 根据登录关键字段判断用户是否存在
	 * @param field		手机/用户名/邮箱
	 * @param value
	 */
	public  boolean isExistSysUser(String field,String value,Object accountId){
		if(accountId==null){
			return dao.findFirst("select count(1) cun from bigdata_sys_user where  login_flag='"+SysUser.VALID+"' and "+field+"=?",value).getLong("cun")>0;
		}else{
			return dao.findFirst("select count(1) cun from bigdata_sys_user where login_flag='"+SysUser.VALID+"'  and "+field+"=? and id !=?",value,accountId).getLong("cun")>0;
		}
	}
}
