/**
 * 
 */
package com.conference.common;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.conference.util.JsonKit;
import com.conference.util.Reflections;
import com.conference.util.sys.DataTablesUtil;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;



/** 
 * 类说明 ：
 * 1.类似restful的controller
 * 不过不是通过http方法判断get save delete的
 * 而是直接规定好所有save get delete的名字，让其规范
 * 可重写此类里面的所有方法,不要自己随便起名字
 * 2.要写使用不用写的简单 form save delete 方法,请在controller中
 * public BKBlockContentController(){
		super.setDao(CoreBlockContent.dao);
	}
	调用构造方法,并把dao实体传进来
 * 
 * @author 	作者 : longjunfeng E-mail：463527083@qq.com
 * @version 创建时间：2015年12月29日 上午8:39:42 
 */
@SuppressWarnings("all")
public abstract  class RESTFulController<T extends BaseModel> extends BaseController  {

	private T dao;//dao
	
	private String tableName;//dao对应的表名
	
	private Class<T> mClass;//dao对应的class
	
	/** 
	 * @Desc: 获取dao
	 * @return
	 * @return: T
	 * @author: longjunfeng   
	 * @date: 2016年10月31日 下午3:26:58 
	 */
	protected T getDao(){
		if(dao!=null){
			return dao;
		}
		ParameterizedType type = Reflections.getParameterizedType(getClass());
		Class<T> mClazz = (Class<T>) type.getActualTypeArguments()[0];
		try {
			Field field = mClazz.getDeclaredField("dao");
			dao = (T) field.get(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dao;
	}
	
	
	/**
	 * @desc	:获取该controller的表名
	 * @author	:作者 Administrator E-mail:463527083@qq.com
	 * @version :创建时间 2016年12月13日
	 * @return
	 */
	protected String getTableName(){
		if(getDao()==null){
			return null;
		}
		if(StringUtils.isBlank(tableName)){
			ParameterizedType type = Reflections.getParameterizedType(getClass());
			Class<T> mClazz = (Class<T>) type.getActualTypeArguments()[0];
			Table table = TableMapping.me().getTable(mClazz);
			if(table!=null){
				tableName=table.getName();
			}
		}
		return tableName;
	}
	
	/**
	 * @desc	:获取model的 class对象
	 * @author	:作者 longjunfeng E-mail:463527083@qq.com
	 * @version :创建时间 2016年12月29日
	 * @return
	 */
	protected Class<T> getModelClass(){
		if(getDao()==null){
			return null;
		}
		if(mClass==null){
			ParameterizedType type = Reflections.getParameterizedType(getClass());
			mClass = (Class<T>) type.getActualTypeArguments()[0];
		}
		return mClass;
	}
	
	
	
	
	/**
	 * 实体首页
	 * @author longjunfeng
	 * @date   2016年2月17日上午8:39:20
	 */
	public abstract void index() throws Exception;
	
	
	
	/**
	 * 实体列表页面
	 * @author longjunfeng
	 * @date   2015年12月29日上午8:44:15
	 */
	public  void list(){
		render("list.html");
	}
	
	/**
	 * 用户获取列表数据，datatables插件专用。pageSize传-1则不分页
	 * @author longjunfeng
	 * @date   2015年12月31日上午9:19:00
	 */
	public  void listData() throws Exception{
		String[] columns=new String[]{"*"};
		Table table = TableMapping.me().getTable(getModelClass());
		String tables =table.getName();
		Map<String,Object > rs = DataTablesUtil.query(columns, tables,null, this);
		renderJson(rs); 
	}
	
	/**
	 * 获取当个实体信息（到info实体信息页面）
	 * @author longjunfeng 
	 * @throws Exception 
	 * @date   2015年12月29日上午8:43:39
	 */
	public  void info() throws Exception {
		T model = (T) getDao().findById(getPara(0));
		if(model==null){
			model=(T) new BaseModel();
		}
		setAttr("model", model);
		setAttr("modelJson", JsonKit.toJson(model));
		render("info.html");
	}
	
	/**
	 * 到单个实体修改页面（form实体填写表单页面）
	 * @author longjunfeng
	 * @date   2015年12月29日上午8:44:41
	 */
	public  void form() throws Exception{
		T model = (T) getDao().findById(getPara(0));
		if(model==null){
			model=(T) new BaseModel();
		}
		setAttr("model", model);
		setAttr("modelJson", JsonKit.toJson(model));
		render("form.html");
	}
	
	
	
	/**
	 * 根据ID删除实体，多个ID以逗号隔开
	 * @author longjunfeng
	 * @date   2015年12月29日上午8:45:21
	 */
	public void delete(){
		try {
			//model需重写deleteById方法，满足自己需求
			String id=getPara(0);
			//删除单个
			if(StringUtils.isNotBlank(id)){
				getDao().deleteById(id);
			}
			//删除多个
			else{
				String[] ids=getParaValues("ids");
				getDao().deleteById(ids);
			}
			//预留钩子
			setCommonMsgSuccess("删除成功!");
		} catch (Exception e) {
			e.printStackTrace();
			setCommonMsgError("删除失败!"+e.getMessage());
		}
		renderJson();
	}
	
	
	/**
	 * echarts表格用的数据方法
	 * 返回数据格式为LIST <NAME > <COUNT>2个字段
	 * @author longjunfeng
	 * @date   2016年7月6日上午9:14:49
	 */
	public void echartsData(){
		
	}
	
	/**
	 * excel数据导出方法
	 * 返回数据格式为 excel文件
	 * @author longjunfeng
	 * @date   2016年7月6日上午9:15:45
	 */
	public void excelData(){
		
	}
	
	/**
	 * 树形结构数据请求方法,为ztree插件准备数据
	 * 返回数据格式为 map集合
	 * 	map.put("id", model.get("id"));
		map.put("pId", model.get("parent_id"));
		map.put("pIds",model.get("parent_ids"));
		map.put("name", model.get("hos_name"));
		map.put("isParent", true);
	 * @author longjunfeng
	 * @date   2016年7月6日上午9:16:16
	 */
	public void treeData(){
		
	}

}