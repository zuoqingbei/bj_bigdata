/*
 * Copyright (c) 2014. 骆驼CMS
 */

package com.conference.common;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.conference.util.sys.SqlUtil;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;


public abstract class BaseService <T extends Model<T>>{
	
	/**
	 * 持久层对象
	 */
	protected T dao;
	
	public  BaseService(){};
	protected  BaseService(T dao){
		this.dao=dao;
	};
	
	/**
	 * 增加一个model如果id为空则增加，不为空则为更新
	 * @param model
	 * @return
	 * @author longjunfeng
	 * @date   2015年11月20日下午2:35:21
	 */
	public T save(T model){
		crudPre(model);
		if(model.get("id")==null||StringUtils.isBlank(model.get("id").toString())){
			model.save();
		}else{
			model.update();
		}
		return model;
	}
	
	/**
	 * 增加一个model如果id为空则增加，不为空则为更新
	 * @param model
	 * @param ids	如果主键不是ID，或者主键是多个
	 * @return
	 * @author longjunfeng
	 * @date   2015年11月20日下午2:36:10
	 */
	public T save(T model,Object... ids){
		crudPre(model);
		for (Object object : ids) {
			if(model.get(object.toString())==null||StringUtils.isBlank(model.get(object.toString()).toString())){
				model.save();
				break;
			}else{
				model.update();
				break;
			}
		}
		return model;
	}
	
	
	/**
	 * 逻辑删除
	 * @param model
	 * @param delColum
	 * @param delValue
	 * @return
	 * @author longjunfeng
	 * @date   2015年11月20日下午2:41:14
	 */
	public Boolean delete(T model,String delColum,Object delValue){
		crudPre(model);
		model.set(delColum, delValue);
		return model.update();
	}
	
	
	/**
	 * 物理删除
	 * @param model
	 * @return
	 * @author longjunfeng
	 * @date   2015年11月20日下午3:24:52
	 */
	public Boolean delete(T model){
		return model.delete();
	}
	
	/**
	 * 物理删除
	 * @author longjunfeng
	 * @date   2015年11月20日下午3:27:15
	 */
	public Boolean deleteById(Object idValue){
		return dao.deleteById(idValue);
	}
	/**
	 * 物理删除
	 * @author longjunfeng
	 * @date   2015年11月20日下午3:27:15
	 */
	public Boolean deleteById(Object... idValues){
		
		return dao.deleteById(idValues);
	}
	
	
	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public T findById(Object id) {
		return dao.findById(id);
	}
	
	/**
	 * 查找list
	 * @author longjunfeng
	 * @date   2015年11月20日下午3:29:31
	 */
	public List<T> find(String sql){
		return dao.find(sql);
	}
	
	/**
	 * 查找list
	 * @author longjunfeng
	 * @date   2015年11月20日下午3:29:31
	 */
	public List<T> find(String sql,Object... paras){
		
		return dao.find(sql, paras);
	}
	
	/**
	 * 查找list用缓存
	 * @author longjunfeng
	 * @date   2015年11月20日下午3:32:18
	 */
	public List<T> findByCache(String sql,String cacheName,Object key){
		
		return dao.findByCache(cacheName, key, sql);
	}
	
	/**
	 * 查找list用缓存
	 * @author longjunfeng
	 * @date   2015年11月20日下午3:32:18
	 */
	public List<T> findByCache(String sql,String cacheName,Object key,Object... paras){
		return dao.findByCache(cacheName, key, sql, paras);
	}
	
	
	/**
		查找首个
	 * @author longjunfeng
	 * @date   2015年11月20日下午3:35:39
	 */
	public T findFirst(String sql){
		return dao.findFirst(sql);
	}
	
	/**
		查找首个
	 * @author longjunfeng
	 * @date   2015年11月20日下午3:35:39
	 */
	public T findFirst(String sql,Object... paras){
		return dao.findFirst(sql, paras);
	}
	
	
	
	
	/**
	 * 分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param select
	 * @param sqlExceptSelect
	 * @return
	 * @author longjunfeng
	 * @date   2015年11月20日下午3:40:48
	 */
	public Page<T> paginate(int pageNumber,int pageSize,String select,String sqlExceptSelect){
		return dao.paginate(pageNumber, pageSize, select, sqlExceptSelect);
	}

	/**
	 * 分页查询
	 * @author longjunfeng
	 * @date   2015年11月20日下午3:40:48
	 */
	public Page<T> paginate(int pageNumber,int pageSize,String select,String sqlExceptSelect,Object... paras){
		return dao.paginate(pageNumber, pageSize, select, sqlExceptSelect, paras);
	}
	
	/**
	 * 分页查询通过缓存
	 * @author longjunfeng
	 * @date   2015年11月20日下午3:42:28
	 */
	public Page<T> paginateByCache(String cacheName,Object key,int pageNumber,int pageSize,String select,String sqlExceptSelect){
		return dao.paginateByCache(cacheName, key, pageNumber, pageSize, select, sqlExceptSelect);
	}
	
	/**
	 * 分页查询通过缓存
	 * @author longjunfeng
	 * @date   2015年11月20日下午3:42:28
	 */
	public Page<T> paginateByCache(String cacheName,Object key,int pageNumber,int pageSize,String select,String sqlExceptSelect,Object... paras){
		return dao.paginateByCache(cacheName, key, pageNumber, pageSize, select, sqlExceptSelect, paras);
	}
	
	
	
	
	/**
	 * 后台查询页面的分页查询，主要功能有：
	 * 1.分装了自动where sql 生成，主要在前台传来参数 如 username_like=小王，
	 * 	   就会自动生成sql where username like '小王'，并把查询参数绑定回request。
	 * 	 如果有表别名的参数，则需要在参数前面加<别名.username_like>,加上以后会生成  别名.username like '小王'
	 * 2. 点击表头字段 自动排序功能！！！在开发中
	 * 
	 * 注：特殊sql 和特殊order,group需要自己手写传入，
	 * 	     如果不分页查询所有结果，request中的pageSize参数传-1
	 * @param c				basecontroller ,用户sqlutil查询参数用
	 * @param recordsStr	表的别名，如果你的fromsql中有表别名，则需要放入此参数，如：from user u left join user_ext ue 则需传入 "u,ue",sqlutil就会找这2个别名的查询参数
	 * @param selectSql		select 条件
	 * @param fromSql		from 那个表
	 * @param specialWhere	特别的where 条件，不需要加where 字段
	 * @param specialParams	特别的where 条件的参数，
	 * @param orderSql		order条件
	 * @param groupSql		group条件
	 * @return	Page
	 * @author longjunfeng
	 * @date   2015年11月20日下午2:58:18
	 */
	public Page<Record> page(BaseController c,String recordsStr,String selectSql,String fromSql,String specialWhere,List<Object> specialParams,String orderSql,String groupSql){
		return SqlUtil.page(c, recordsStr, selectSql, fromSql, specialWhere, specialParams, orderSql, groupSql);
	}
	
	public Page<Record> page(BaseController c,String recordsStr,String selectSql,String fromSql){
		return SqlUtil.page(c, recordsStr, selectSql, fromSql, null, null, null, null);
	}
	
	public Page<Record> page(BaseController c,String recordsStr,String selectSql,String fromSql,String specialWhere,List<Object> specialParams){
		return SqlUtil.page(c, recordsStr, selectSql, fromSql, specialWhere, specialParams, null, null);
	}
	
	public Page<Record> page(BaseController c,String recordsStr,String selectSql,String fromSql,String orderSql,String groupSql){
		return SqlUtil.page(c, recordsStr, selectSql, fromSql, null, null, orderSql, groupSql);
	}
	
	
	
	
	
	
	
	
	/**增删改 预前处理！！！未加
	 * @param model
	 * @author longjunfeng
	 * @date   2015年11月20日下午2:42:07
	 */
	public void crudPre(T model){
		//1.设置update时间	设置update人员
		//2.设置createat时间，设置createat人员
	}
	
}
