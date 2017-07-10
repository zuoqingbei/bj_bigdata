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
			String type = object[0].toString();
			//分别获取各使用方式的数量
			List<BigDataFctEval> typeCount = BigDataFctEval.dao.getTypeCount(type);
			double typeTotal = Double.parseDouble(typeCount.get(0)._getAttrValues()[0].toString());
			java.text.DecimalFormat df=new java.text.DecimalFormat("#.##");   
			//返回的value是百分比，精确到小数点后两位
			map.put(type, df.format((typeTotal/total)*100));
		}
		renderJson(map);
	}
}
