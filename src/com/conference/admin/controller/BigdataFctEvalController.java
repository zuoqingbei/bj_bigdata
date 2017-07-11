package com.conference.admin.controller;

import java.util.HashMap;
import java.util.List;

import com.conference.admin.model.BigDataFctEval;
import com.conference.common.BaseController;
/***
 * 评论表，一条新闻对应多条评论
 * @author Tom
 *
 */
public class BigdataFctEvalController extends BaseController{

	/***
	 * 第一个圈的百分比
	 */
	public void firstCircle(){
		HashMap<String,String> map = new HashMap<String,String>();
		//获取所有使用方式的数量并转化成double
		List<BigDataFctEval> all =  BigDataFctEval.dao.getAllCount();
		double total = Double.parseDouble(all.get(0)._getAttrValues()[0].toString());
		//获取使用方式的list
		List<BigDataFctEval> list =  BigDataFctEval.dao.getTypeNum();
		for(int i = 0 ; i < list.size() ; i ++){
			Object[] object = list.get(i)._getAttrValues();
			String type;
			try {
				//捕获处理type为null的空指针异常
				type = object[0].toString();
			} catch (Exception e) {
				type = "";
			}
			
			//分别获取各使用方式的数量
			List<BigDataFctEval> typeCount = BigDataFctEval.dao.getTypeCount("".equals(type) ? "'' OR use_type_id is NULL" : type);//判断是否为空
			double typeTotal = Double.parseDouble(typeCount.get(0)._getAttrValues()[0].toString());
			java.text.DecimalFormat df=new java.text.DecimalFormat("#.##");   
			//返回的value是百分比，精确到小数点后两位
			//如果type为空，则显示未知
			map.put("".equals(type) ? "未知" : type, df.format((typeTotal/total)*100));
		}
		renderJson(map);
	}
	/***
	 * 第二个圈的百分比
	 */
	public void secondCircle(){
		HashMap<String,String> map = new HashMap<String,String>();
		//获取所有使用方式的数量并转化成double
		List<BigDataFctEval> all =  BigDataFctEval.dao.getAllCount();
		double total = Double.parseDouble(all.get(0)._getAttrValues()[0].toString());
		for(int i = 0 ; i < 2 ; i ++){
			//分别获取各使用方式的数量
			List<BigDataFctEval> typeCount = BigDataFctEval.dao.getPishiTypeCount(i+"");
			double typeTotal = Double.parseDouble(typeCount.get(0)._getAttrValues()[0].toString());
			java.text.DecimalFormat df=new java.text.DecimalFormat("#.##");   
			//返回的value是百分比，精确到小数点后两位
			map.put(i==0 ? "无批示" : "有批示", df.format((typeTotal/total)*100));
		}
		renderJson(map);
	}
	/***
	 * 第三个圈的百分比
	 */
	public void thirdCircle(){
		HashMap<String,String> map = new HashMap<String,String>();
		//获取所有使用方式的数量并转化成double
		List<BigDataFctEval> all =  BigDataFctEval.dao.getAllCount();
		double total = Double.parseDouble(all.get(0)._getAttrValues()[0].toString());
		String arry[] = {"1","2","3"};
		String arryWord[] = {"优","良","中"};
		for(int i = 0 ; i < arry.length ; i ++){
			//分别获取各使用方式的数量
			List<BigDataFctEval> typeCount = BigDataFctEval.dao.getUseLevelCount(arry[i]);
			double typeTotal = Double.parseDouble(typeCount.get(0)._getAttrValues()[0].toString());
			java.text.DecimalFormat df=new java.text.DecimalFormat("#.##");   
			//返回的value是百分比，精确到小数点后两位
			map.put(arryWord[i], df.format((typeTotal/total)*100));
		}
		renderJson(map);
	}
}
