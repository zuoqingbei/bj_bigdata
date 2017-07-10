package com.conference.common;


import com.conference.admin.controller.ConditionsController;
import com.conference.admin.controller.IndexController;
import com.conference.admin.controller.TestController;
import com.conference.admin.model.BigDataDRef;
import com.conference.admin.model.Creator;
import com.conference.admin.model.Dept;
import com.conference.admin.model.FctOrigin;
import com.conference.directive.LabelDirective;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.FreeMarkerRender;
import com.jfinal.render.ViewType;
public class ConferenceConfig extends JFinalConfig{
	
	
	
	
	public ConferenceConfig() {
		super();
		configDirective();
	}
	/**
	 * 配置常量
	 */
	@Override
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用getProperty(...)获取值
		loadPropertyFile("config.txt");
		me.setDevMode(getPropertyToBoolean("devMode", true));
		me.setViewType(ViewType.FREE_MARKER);
		me.setFreeMarkerTemplateUpdateDelay(0);//html页面缓存时间为10分钟
		me.setMaxPostSize(104857600*5);
	}
	
	/**
	 * 配置路由
	 */
	@Override
	public void configRoute(Routes me) {
		me.add("/common/test",TestController.class);
		me.add("/home",IndexController.class);
		me.add("/conditions",ConditionsController.class);//筛选条件数据
		
	}
	/**
	 * 配置插件
	 */
	
	@Override
	public void configPlugin(Plugins me) {
		DruidPlugin druidPlugin = new DruidPlugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password").trim());
		me.add(druidPlugin);
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		arp.setDialect(new MysqlDialect());
		arp.setShowSql(true);
		me.add(arp);
		
		//设置不区分大小写
		arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));
		
		//建立表的映射
		arp.addMapping("bigdata_fct_origin",FctOrigin.class);//原始素材
		arp.addMapping("bigdata_d_dept",Dept.class);//大单位 部门
		arp.addMapping("bigdata_d_creator",Creator.class);//发稿人
		arp.addMapping("bigdata_d_ref",BigDataDRef.class);//参照维表，包含方向、性质等
	}
	
	/**
	 * 配置全局拦截器
	 */
	@Override
	public void configInterceptor(Interceptors me) {
	}
	
	/**
	 * 配置标签
	 */
	private void configDirective() {
		FreeMarkerRender.getConfiguration().setSharedVariable("_label_dic", new LabelDirective());
	}
	
	/**
	 * 配置处理器
	 */
	@Override
	public void configHandler(Handlers me) {
		//配置项目路径 页面中可以使用
		me.add(new ContextPathHandler("ctx_path")); 
	}
	
	@Override
	public void afterJFinalStart(){
		
	};
	
	
	public static void main(String[] args) {
		JFinal.start("WebRoot", 8080, "/", 5);
	}

}
