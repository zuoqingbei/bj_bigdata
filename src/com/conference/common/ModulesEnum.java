package com.conference.common;


/**
 * 类说明 ：平台模块枚举
 *
 * @author 作者 : longjunfeng E-mail：463527083@qq.com
 * @version 创建时间：2016年3月15日 上午9:58:41
 */
public enum ModulesEnum {

	INDEX("/"), 					// 平台模块
	COMMON("/common/"), 			// 通用服务模块,如ajax模块
	CONFERENCE("/c/"), 				// 会议前台模块
	CONFERENCE_ADMIN("/admin/"), 	// 会议后台模块
	SERVICE("/service"),			// 现场service模块
	USERCENTER("/account/"), 		// 个人中心模块
	BK("/bk/"), 					// 超级后台模块
	TOPIC("/topic/"), 				// 老分会模块(废弃)
	TOPIC_ADMIN("/super/"); 		// 老分会后台管理模块(废弃)
	
	
	/**
	 * 模块路径
	 */
	private final String path;

	ModulesEnum(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
	
	public static void main(String[] args) {
        
        System.out.println("===========");
        for(ModulesEnum gender : ModulesEnum.values()){
            System.out.println(gender.path);
        }
       
    }
}
