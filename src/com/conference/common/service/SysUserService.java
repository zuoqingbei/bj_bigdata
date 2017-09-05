package com.conference.common.service;

import java.util.HashMap;
import java.util.Map;

import oecp.framework.util.MD5Util;

import org.apache.commons.lang.StringUtils;

import com.conference.admin.model.SysUser;
import com.conference.common.BaseController;
import com.conference.common.BaseService;
import com.conference.common.Constant;
import com.conference.common.security.SecurityService;
import com.conference.util.HttpRequestDeviceUtils;
import com.conference.util.StringKit;
import com.jfinal.kit.StrKit;

/**
 * 主要负责 登录;登出;注册;修改密码;找回密码
 * @author zuoqb
 * 2017年7月27日14:53:32
 */
public class SysUserService extends BaseService<SysUser> {
	public static final SysUserService service = new SysUserService();

	public SysUserService() {
		super(SysUser.dao);
	}//调用父级构造函数，将dao传入service

	public static final String REGTYPE_MOBILE = "1";//手机注册
	public static final String REGTYPE_EMAIL = "2";//邮箱注册
	public static final String REGTYPE_NICKNAME = "3";//昵称注册

	//-----------------------------------------主要方法区---------------------------------------------------	

	//-------------------------------------------------登录方法区域Start TODO--------------------------------------
	/**
	 * 根据账户和密码进行登录
	 * 1.登录完成 吧account 和profile 放入session ATTR_USER  ATTR_USER_PROFILE
	 * @param login_id	登录账户
	 * @param password	普通密码
	 * @param c
	 * @return	true 登录成功 ,false登录数百
	 * @author longjunfeng
	 * @date   2015年12月17日下午4:45:34
	 */
	public Map<String, String> login(String login_id, String password, BaseController c) {
		Map<String, String> returnMsg = new HashMap<String, String>();
		//1.进行普通登录合法性检验
		SysUser account = nomalLoginCheck(login_id, password, c, returnMsg);
		if (account == null) {
			return returnMsg;
		}
		//2.进行登录处理
		try {
			SysUserService.service.dealLogin(account, login_id, account.getStr("password"), c);
		} catch (Exception e) {
			e.printStackTrace();
		}
		returnMsg.put(Constant.STATUS, (String) c.getAttr(Constant.STATUS));
		returnMsg.put(Constant.MSG, (String) c.getAttr(Constant.MSG));
		returnMsg.put("login_id", login_id);
		returnMsg.put("secret_token", account.getStr("password"));
		returnMsg.put("sessionId", c.getSession().getId());
		return returnMsg;
	}

	/**
	 * 根据cookie中的loginid和密码 进行登录
	 * 如果is_remember_me==true或者 isForce==true
	 *  则进行自动登录
	 * @param c
	 * @param isForce	是否强制自动根据cookie登录,true强制进行登录,不管是否remember_me;false 则照常进行登录
	 * @return	account ！=null 表示登录成功 null表示 登录失败
	 * @author zuoqb
	 * @date   2017年7月27日14:55:40
	 */
	public SysUser autoLogin(BaseController c, Boolean isForce) {
		String is_remember_me = c.getCookie(BaseController.COOKIE_REMEMBER_ME); //记住我
		String login_id = c.getCookie(BaseController.COOKIE_LOGIN_ID); //登录名
		String secret_token = c.getCookie(BaseController.COOKIE_SECRET_TOKEN);//密码
		boolean mobile = HttpRequestDeviceUtils.isMobileDevice(c.getRequest());//判断是否手机站点
		if (StringKit.notBlank(is_remember_me) || "true".equals(is_remember_me) || isForce || mobile) {
			//判断自动登陆
			if (StrKit.notBlank(login_id) && StrKit.notBlank(secret_token)) {
				SysUser sysUser = SysUser.getSysUserMD5Pwd(login_id, secret_token);
				if (sysUser != null) {
					dealLogin(sysUser, c);
				}
				return sysUser;
			}
		}
		return null;
	}

	/** 
	 * 登录后续处理,在SecurityService里面进行操作
	 * @Desc: 	1.写cookie
				2.进行用户信息/权限信息加载
				3.进行session写操作
				4.记住我操作
	 * @param account		登录用户
	 * @param login_id		登录ID，可以不传
	 * @param password		登录密码，可以不传
	 * @param c				controller
	 * @return
	 * @author: zuoqb
	 * @date: 2017年7月27日15:23:49
	 */
	public SysUser dealLogin(SysUser sysUser, String login_id, String password, BaseController c) {
		return dealLogin(sysUser, c);
	}

	/** 
	 * @Desc: 直接放account登录
	 * @param account
	 * @param c
	 * @author: zuoqb
	 * @date: 2017年7月27日15:22:03
	 */
	public SysUser dealLogin(SysUser sysUser, BaseController c) {
		if (sysUser != null) {
			SecurityService.login(sysUser, c);
		}
		return sysUser;
	}

	/**
	 * 
	 * @time   2017年7月27日 下午3:22:53
	 * @author zuoqb
	 * @todo   登出
	 */
	public void logout(BaseController c) {
		SecurityService.logout(c);
	}

	//-------------------------------------------------登录方法区域end TODO--------------------------------------

	//-------------------------------------------------注册方法区域Start TODO--------------------------------------
	/**
	 * 注册用户 直接传参数 
	 * @param realname	真实姓名
	 * @param nickname	用户昵称 可当账户
	 * @param email		用户邮箱 可当账户
	 * @param mobile	用户电话 可当账户
	 * @param password	用户密码
	 * @param c			basecontroller
	 * @param isAutoLogin	是否自动登录
	 * @param checkMobileVc 是否验证短信验证码
	 * @param isMd5Pwd	密码是否已经是md5的
	 * @param regType		1.手机注册  2.邮箱注册 默认手机号注册
	 * @return
	 * @author zuoqb
	 * @date   2017年7月27日15:31:32
	 */
	public Map<String, String> dealReg(String realname, String nickname, String email, String mobile, String password,
			BaseController c, Boolean isAutoLogin, Boolean checkMobileVc, boolean isMd5Pwd, String regType) {
		SysUser user = new SysUser();
		user.set("name", realname);
		user.set("login_name", nickname);
		user.set("email", email);
		user.set("mobile", mobile);
		user.set("password", password);
		return dealReg(user, c, isAutoLogin, checkMobileVc, isMd5Pwd, regType);
	}

	/** 注册用户 传accout参数
	 * @Desc: 根据手机或者邮箱注册用户	
	 * @param account		用户实体
	 * @param c				
	 * @param isAutoLogin	是否注册后自动登录
	 * @param checkVc 		是否检查验证码
	 * @param isMd5Pwd		密码是否已经是MD5形式
	 * @param regType		1.手机注册  2.邮箱注册 默认手机号注册
	 * @return: Map<String,String>	status信息和accountId
	 * @author: zuoqb
	 * @date: 2017年7月27日15:31:50
	 */
	public Map<String, String> dealReg(SysUser account, BaseController c, Boolean isAutoLogin, Boolean checkVc,
			boolean isMd5Pwd, String regType) {
		Map<String, String> returnMsg = new HashMap<String, String>();
		if (account == null) {
			returnMsg.put(Constant.STATUS, (String) c.getAttr(Constant.STATUS));
			returnMsg.put(Constant.MSG, (String) c.getAttr(Constant.MSG));
			return returnMsg;
		}
		String password = account.getStr("password");
		password = StringKit.isBlank(password) ? SysUser.DEFAULT_PASSWORD : password;
		//1.进行用户合法性验证
		boolean isOk = checkRegInfo(c, account, checkVc, regType);
		if (!isOk) {
			returnMsg.put(Constant.STATUS, (String) c.getAttr(Constant.STATUS));
			returnMsg.put(Constant.MSG, (String) c.getAttr(Constant.MSG));
			return returnMsg;
		}
		//2.保存account用户
		if (isMd5Pwd) {
			account.set("password", password.toLowerCase());
		} else {
			account.set("password", MD5Util.getStringMD5(password).toLowerCase());
		}
		account = SysUser.saveOrupdateSysUser(account);
		//4.自动登录
		if (isAutoLogin) {
			dealLogin(account, null, null, c);
		}
		returnMsg.put(Constant.STATUS, (String) c.getAttr(Constant.STATUS));
		returnMsg.put(Constant.MSG, (String) c.getAttr(Constant.MSG));
		returnMsg.put("accountId", account.getStr("id"));
		return returnMsg;
	}

	//-------------------------------------------------注册方法区域end TODO--------------------------------------

	//-------------------------------------------------账户信息修改区域Start TODO--------------------------------------
	/** 用过手机或者邮箱 验证码找回密码
	 * @Desc: 
	 * @param c
	 * @param validateCode		手机或者邮箱 验证码
	 * @param imgValidataCode	图片验证码（手机验证码需要用到）
	 * @param mobile			手机号
	 * @param email				邮箱
	 * @param password			新密码
	 * @param regType			找回密码类型1.手机  2.邮箱 
	 * @return: boolean
	 * @author: zuoqb
	 * @date: 2017年7月27日15:43:31
	 */
	public boolean findPwd(BaseController c, String validateCode, String imgValidataCode, String mobile, String email,
			String password, String regType) {
		//		1.判断手机号或者邮箱是否都为null
		if (StringUtils.isBlank(mobile) && StringUtils.isBlank(email)) {
			c.setCommonMsgError("手机号或邮箱不能为空");
			c.renderJson();
			return false;
		}
		//		2.判断图片验证码是否正确
		//		3.判断手机或邮箱验证码是否正确
		String checkTo = mobile;
		if (regType != null && regType.equals("2")) {
			checkTo = email;
		}
		if (!checkImageCodeAndVc(c, checkTo, validateCode, true)) {
			return false;
		}
		//		4.更新用户密码
		SysUser account = null;
		if (regType != null && regType.equals("2")) {
			account = SysUser.dao.getByLoginId(email, "email");
		} else {
			account = SysUser.dao.getByLoginId(mobile, "mobile");
		}
		if (account == null) {
			c.setAttr(Constant.STATUS, "account_not_find");
			c.setAttr(Constant.MSG, "账户不存在！");
			c.renderForReg();
			return false;
		}
		account.set("password", MD5Util.getStringMD5(password)).update();
		c.setCommonMsgSuccess("修改成功！");
		dealLogin(account, checkTo, account.getStr("password"), c);
		return true;
	}

	/**
	 * 
	 * @time   2017年7月27日 下午3:43:38
	 * @author zuoqb
	 * @todo   通过老密码修改新密码
	 */
	public boolean changePwd(BaseController c) {
		SysUser ac = c.getLoginUser();
		String oldPwd = c.getPara("oldPassword");
		String newPwd = c.getPara("newPassword");
		//1.进行合法性验证，
		if (StringKit.isBlank(oldPwd) || StringKit.isBlank(newPwd)) {
			return false;
		}
		if (!MD5Util.getStringMD5(oldPwd).equals(ac.get("password"))) {
			return false;
		}
		//2.更新用户密码
		ac.set("password", MD5Util.getStringMD5(newPwd)).update();
		//3.进行重新登录
		SysUserService.service.dealLogin(ac, ac.getStr("mobile"), ac.getStr("password"), c);
		return true;
	}

	/**
	 * 
	 * @time   2017年7月27日 下午3:43:49
	 * @author zuoqb
	 * @todo   更改账户手机号
	 */
	public SysUser updateMobile(BaseController c, SysUser account, String newMobile) {
		return updateMobile(c, account, newMobile, true);
	}

	/**更改账户手机号
	 * @param c
	 * @param account
	 * @param newMobile
	 * @param isAutoLogin	是否自动登录
	 * @return
	 * @author zuoqb
	 * @date   2017年7月27日15:44:16
	 */
	public SysUser updateMobile(BaseController c, SysUser account, String newMobile, boolean isAutoLogin) {
		if (account == null) {
			return null;
		}
		String oldMobile = account.getStr("mobile");
		//		1.修改account手机
		account.set("mobile", newMobile).update();
		//		2.进行重新登录
		if (isAutoLogin) {
			SysUserService.service.dealLogin(account, newMobile, account.getStr("password"), c);
		}
		return account;
	}

	//-------------------------------------------------账户信息修改区域end TODO--------------------------------------

	//-------------------------------------------------验证区域start TODO--------------------------------------

	/** 检验图片验证码和 手机邮箱验证码
	 * @Desc: 
	 * @param c	
	 * @param checkTo		发送验证码的手机或者邮箱
	 * @param checkVc		发送的验证码
	 * @param checkMobileVc	是否检验手机或者邮箱验证码
	 * @return
	 * @return: boolean		true检验成功， false失败
	 * @author: zuoqb
	 * @date: 2017年7月27日15:34:38
	 */
	public boolean checkImageCodeAndVc(BaseController c, String checkTo, String checkVc, Boolean checkMobileVc) {
		//如果不是来自手机	检测图片验证码是否正确
		//如果图片验证码不为空再进行判断  如果为空则不判断
		String validataCode = c.getPara(Constant.SESSION_VALIDATECODE);
		if (StrKit.notBlank(validataCode)) {
			if (!HttpRequestDeviceUtils.isMobileDevice(c.getRequest())) {
				if (!validataCode.toLowerCase().equals(
						c.getSessionAttr(Constant.SESSION_VALIDATECODE).toString().toLowerCase())) {
					c.setCommonMsgError("验证码错误");
					c.renderForReg();
					return false;
				}
			}
		}
		if (checkMobileVc) {
			//检验手机验证码是否正确
			if (!checkVC(c, checkTo, checkVc)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @time   2017年7月27日 下午3:52:08
	 * @author zuoqb
	 * @todo  判断手机号或昵称是否已存在 true 存在 false 不存在
	 */

	public boolean check_login_id(String fieldName, String value, Object account_id) {
		boolean flag = false;
		if (StringUtils.isBlank(value)) {
			return flag;
		}
		if ("nickname".equals(fieldName)) {
			//昵称需要特殊处理，如果用户把昵称输成了手机号或者邮箱，就会和手机号与邮箱冲突，到时候账户就冲突
			//所以昵称检验的时候要对手机或邮箱都进行检验 
			flag = SysUser.dao.isExistSysUser("nickname", value, account_id);
			if (flag) {
				return flag;
			}
			flag = SysUser.dao.isExistSysUser("mobile", value, account_id);
			if (flag) {
				return flag;
			}
			flag = SysUser.dao.isExistSysUser("email", value, account_id);
			if (flag) {
				return flag;
			}
		} else if ("mobile".equals(fieldName)) {
			//这里需要检验手机的格式
			flag = SysUser.dao.isExistSysUser("mobile", value, account_id);
		} else if ("email".equals(fieldName)) {
			//这里需要检验邮箱的格式
			flag = SysUser.dao.isExistSysUser("email", value, account_id);
		}
		return flag;
	}

	//-------------------------------------------------验证区域end TODO--------------------------------------

	/** 
	 * @Desc: 	检测用户注册信息是否正确    注册方式目前有 邮箱注册、手机注册、
	 * @param c
	 * @param account	用户注册实体
	 * @param isCheckVc	是否检验发送的验证码（后台注册或者批量导入不需要验证码检验）
	 * @param regType	1.手机注册  2.邮箱注册  3.昵称注册        
	 * @return
	 * @return: boolean	true检验通过 false检验不通过
	 * @author: zuoqb
	 * @date: 2017年7月27日15:32:37
	 */
	private boolean checkRegInfo(BaseController c, SysUser account, Boolean isCheckVc, String regType) {
		String mobile = account.get("mobile") == null ? null : account.getStr("mobile");
		String email = account.get("email") == null ? null : account.getStr("email");
		String nickname = account.get("login_name") == null ? null : account.getStr("login_name");
		String account_id = StringKit.notBlank(account.getStr("id")) ? account.getStr("id") : null;

		//1.判断手机号、邮箱、昵称是否都是为空
		if (StringUtils.isBlank(mobile) && StringUtils.isBlank(email) && StringUtils.isBlank(nickname)) {
			c.setCommonMsgError("手机号、邮箱、昵称不能全为空");
			c.renderJson();
			return false;
		}

		//2.判断手机号、邮箱、昵称是否被禁用
		if (!SysUser.dao.canRegOrLogin(mobile)) {
			c.setCommonMsgError("手机号被禁用,不能再次注册！");
			c.renderJson();
			return false;
		}
		if (!SysUser.dao.canRegOrLogin(email)) {
			c.setCommonMsgError("邮箱被禁用,不能再次注册！");
			c.renderJson();
			return false;
		}
		if (!SysUser.dao.canRegOrLogin(nickname)) {
			c.setCommonMsgError("昵称被禁用,不能再次注册！");
			c.renderJson();
			return false;
		}

		//3.判断手机号、邮箱、昵称是否重复
		if (check_login_id("mobile", mobile, account_id)) {
			c.setCommonMsgError("手机号账户已存在！");
			c.renderJson();
			return false;
		}
		if (check_login_id("email", email, account_id)) {
			c.setCommonMsgError("邮箱已存在！");
			c.renderJson();
			return false;
		}
		if (check_login_id("nickname", nickname, account_id)) {
			c.setCommonMsgError("昵称已存在！");
			c.renderJson();
			return false;
		}
		//4.检验图片验证码、手机/邮箱验证码 是否正确
		String checkTo = mobile;
		String checkVc = c.getPara("mobile_vc");
		if (regType != null && regType.equals(REGTYPE_EMAIL)) {
			checkTo = email;
		}
		if (!checkImageCodeAndVc(c, checkTo, checkVc, isCheckVc)) {
			return false;
		}
		return true;
	}

	/**
	 * 检验手机验证码是否正确
	 * @param c
	 * @param mobile
	 * @param mobile_vc
	 * @return
	 * @author zuoqb
	 * @date   2017年7月27日15:36:05
	 */
	private boolean checkVC(BaseController c, String mobileOrEmail, String validateCode) {
		if (StringKit.isBlank(validateCode)) {
			c.setAttr(Constant.STATUS, "mobile_vc_is_null");
			c.setAttr(Constant.MSG, "验证码不能为空！");
			c.renderForReg();
			return false;
		}
		Map mobileMap = (Map) c.getSessionAttr(Constant.SESSION_MOBILE_VC);
		if (mobileMap == null) {
			c.setAttr(Constant.STATUS, "mobile_vc_not_correct");
			c.setAttr(Constant.MSG, "验证码不存在！");
			c.renderForReg();
			return false;
		}
		if (mobileMap != null) {
			if (!mobileOrEmail.equals(mobileMap.get("mobile"))) {
				c.setAttr(Constant.STATUS, "mobile_vc_not_correct");
				c.setAttr(Constant.MSG, "尚未获取验证码或验证码已失效，请重新获取！");
				c.renderForReg();
				return false;
			}
			if (!validateCode.equals(mobileMap.get("mobile_vc"))) {
				c.setAttr(Constant.STATUS, "mobile_vc_not_correct");
				c.setAttr(Constant.MSG, "验证码不正确或超时，请重新获取！");
				c.renderForReg();
				return false;
			}
		}
		return true;
	}

	/**
	 * 普通登录检测
	 * @param login_id	:邮箱 手机 用户名
	 * @param password
	 * @param c
	 * @param returnMsg
	 * @return	true  检测通过
	 * @author longjunfeng
	 * @date   2016年1月22日上午9:02:19
	 */
	private SysUser nomalLoginCheck(String login_id, String password, BaseController c, Map<String, String> returnMsg) {
		//		1.检测参数是否完整
		if (StrKit.isBlank(login_id) || StrKit.isBlank(password)) {
			c.setCommonMsgError("参数不全");
			returnMsg.put(Constant.STATUS, (String) c.getAttr(Constant.STATUS));
			returnMsg.put(Constant.MSG, (String) c.getAttr(Constant.MSG));
			return null;
		}
		//		2.检测账户密码是否正确,账户是否存在
		SysUser account = SysUser.getSysUser(login_id, password);
		if (account == null) {
			SysUser temp = SysUser.getSysUserByLoginId(login_id);
			if (temp != null) {
				c.setCommonMsgError("参数不全");
			} else {
				c.setCommonMsgError(login_id + "用户没有注册！");
			}
			returnMsg.put(Constant.STATUS, (String) c.getAttr(Constant.STATUS));
			returnMsg.put(Constant.MSG, (String) c.getAttr(Constant.MSG));
			return null;
		}
		//		3.检测账账户是否禁止
		if (!SysUser.dao.canRegOrLogin(login_id)) {
			c.setCommonMsgError("用户已被禁用！");
			returnMsg.put(Constant.STATUS, (String) c.getAttr(Constant.STATUS));
			returnMsg.put(Constant.MSG, (String) c.getAttr(Constant.MSG));
			return null;
		}
		return account;
	}

	public static void main(String[] args) {
		System.out.println(MD5Util.getStringMD5("123456"));
		;
	}

}