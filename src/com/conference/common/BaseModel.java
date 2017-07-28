package com.conference.common;

import java.math.BigDecimal;
import java.util.List;

import com.conference.util.Reflections;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.TableMapping;

/** 
 * 类说明 ：基本的model实体的父类
 * 主要功能:
 * 1.id生成策略
 * 2.保存更新预处理,如果表包含 create_by create_at  update_at 等固定的字段,可以自动的进行设置这些字段的信息
 * 3.区域字段,如果符合 /script/commonJs/city.js 插件里面的区域规则,可进行区域信息获取分隔
 * @author  作者 : longjunfeng E-mail：463527083@qq.com
 * @version 创建时间：2016年1月7日 下午4:31:53 
 */
public class BaseModel<T extends Model<?>> extends Model<T>{
	
	protected BaseModel(){}
	private static final long serialVersionUID = 1L;
	
	/**
	 * 方法简介：生成ID策略,默认为null自增
	 * 如果要修改主键生成策略,请重写此方法
	 * @description 
	 * @return
	 * @author 		作者：longjunfeng E-mail:463527083@qq.com
	 * @date  		2016年3月28日 下午2:56:07
	 * @version 	1.0
	 */
	public Object genId(){
//		1.生成UUID方式
//		return RandomGenerator.get16UniqueKey();
//		2.根据数据库的生成方式
		return null;
	}
	
	/** 
	 * @Desc: 是否是新增数据,根据ID是否为NULL
	 * @return
	 * @return: boolean
	 * @author: longjunfeng   
	 * @date: 2016年8月10日 下午4:25:50 
	 */
	public boolean isNew(){
		Object id = this.get("id");
		if(id==null){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * del_flag 字段为未删除
	 */
	public static final Integer DEL_FLAG_NOT_DELETE=0;
	/**
	 * del_flag 字段为已删除
	 */
	public static final Integer DEL_FLAG_DELETE=1;
	
	
	/**
	 * 是1
	 */
	public static final Integer YES=1;
	/**
	 * 否0
	 */
	public static final Integer NO=0;
	
	
	
	

	
	
	
	
	/**将menu sourcelist 根据父子 进行排序
	 * @param list
	 * @param sourcelist
	 * @param parentId
	 * @param cascade	是否级联
	 * @author longjunfeng
	 * @date   2016年4月19日上午10:15:45
	 */
	public  void sortList(List<T> list, List<T> sourcelist, String parentId, boolean cascade){
		for (int i=0; i<sourcelist.size(); i++){
			T e = sourcelist.get(i);
			if (e.get("parent_id")!=null&& e.get("parent_id").toString().equals(parentId)){
				list.add(e);
				if (cascade){
					// 判断是否还有子节点, 有则继续获取子节点
					for (int j=0; j<sourcelist.size(); j++){
						T child = sourcelist.get(j);
						if (child.get("parent_id")!=null
								&&  child.get("parent_id").toString().equals(e.get("id").toString())){
							list.get(list.size()-1).set("del_flag", "true");
							sortList(list, sourcelist, e.get("id").toString(), true);
							break;
						}else{
							list.get(list.size()-1).set("del_flag", "true");
						}
					}
				}
			}
		}
	}
	
	
	@Override
	public Integer getInt(String attr) {
		Object obj = get(attr);
		if (obj == null) {
			return null;
		} else if (obj instanceof Integer) {
			return (Integer) obj;
		} else if (obj instanceof BigDecimal) {
			return ((BigDecimal) obj).intValue();
		} else if (obj instanceof String) {
			try {
				return Integer.parseInt((String) obj);
			} catch (Exception e) {
				throw new RuntimeException("String can not cast to Integer : " + attr.toString());
			}
		} else {
			try {
				return Integer.parseInt(obj.toString());
			} catch (Exception e) {
				throw new RuntimeException("Object can not cast to Integer : " + attr.toString());
			}
		}
	}
	
	
	/**
	 * Find model.
	 * 
	 * @param where
	 *            an SQL statement that may contain one or more '?' IN parameter
	 *            placeholders
	 * @param paras
	 *            the parameters of sql
	 * @return the list of Model
	 */
	public List<T> findByWhere(String where, Object... paras) {
		return findByWhereAndColumns(where, "*", paras);
	}

	/**
	 * Find model.
	 * 
	 * @param where
	 *            an SQL statement that may contain one or more '?' IN parameter
	 *            placeholders
	 * @param columns
	 * @param paras
	 *            the parameters of sql
	 * @return the list of Model
	 */
	public List<T> findByWhereAndColumns(String where, String columns, Object... paras) {
		StringBuffer sqlBuffer=new StringBuffer();
		sqlBuffer.append(" SELECT ")
		.append(columns)
		.append(" FROM ")
		.append(TableMapping.me().getTable(getOriginClass()).getName())
		.append(" ")
		.append(where);
		return find(sqlBuffer.toString(), paras);
	}

	/**
	 * Find first model. I recommend add "limit 1" in your sql.
	 * 
	 * @param where
	 *            an SQL statement that may contain one or more '?' IN parameter
	 *            placeholders
	 * @param paras
	 *            the parameters of sql
	 * @return Model
	 */
	public T findFirstByWhere(String where, Object... paras) {
		return findFirstByWhereAndColumns(where, "*", paras);
	}

	/**
	 * Find first model. I recommend add "limit 1" in your sql.
	 * 
	 * @param where
	 *            an SQL statement that may contain one or more '?' IN parameter
	 *            placeholders
	 * @param columns
	 * @param paras
	 *            the parameters of sql
	 * @return Model
	 */
	public T findFirstByWhereAndColumns(String where, String columns, Object... paras) {
		StringBuffer sqlBuffer=new StringBuffer();
		sqlBuffer.append(" SELECT ")
		.append(columns)
		.append(" FROM ")
		.append(TableMapping.me().getTable(getOriginClass()).getName())
		.append(" ")
		.append(where);
		return findFirst(sqlBuffer.toString(), paras);
	}
	
	
	/**
	 * @desc	:查找全部
	 * @author	:作者 longjunfeng E-mail:463527083@qq.com
	 * @version :创建时间 2017年1月3日
	 * @return
	 */
	public List<T> findAll() {
		StringBuffer sqlBuffer=new StringBuffer();
		sqlBuffer.append(" SELECT ")
		.append("*")
		.append(" FROM ")
		.append(TableMapping.me().getTable(getOriginClass()).getName());
		return find(sqlBuffer.toString());
	}
	
	/**
	 * @desc	:获取model最原始的类泛型，防止被代理
	 * @author	:作者 longjunfeng E-mail:463527083@qq.com
	 * @version :创建时间 2017年1月3日
	 * @return
	 */
	public Class<T> getOriginClass(){
		return (Class<T>) Reflections.getUserClass(this);
	}
	

}
