package com.conference.admin.model;

import java.util.List;

import com.conference.common.BaseModel;

/***
 * 评论表，一条新闻对应多条评论
 * 
 * @author Tom
 *
 */
public class BigDataFctEval extends BaseModel<BigDataFctEval> {

	private static final long serialVersionUID = 11122234567890L;
	public static final BigDataFctEval dao = new BigDataFctEval();

	/***
	 * 返回所有类别的使用方式
	 */
	public List<BigDataFctEval> getTypeNum() {
		String sql = "";
		sql += "SELECT DISTINCT use_type_id from bigdata_fct_eval";
		return BigDataFctEval.dao.find(sql);
	}
	
	/***
	 * 获取全部的数量
	 */
	public List<BigDataFctEval> getAllCount() {
		String sql = "";
		sql += "SELECT count(0) from bigdata_fct_eval";
		return BigDataFctEval.dao.find(sql);
	}
	
	/***
	 * 获取某种使用方式的数量
	 */
	public List<BigDataFctEval> getTypeCount(String type) {
		String sql = "";
		sql += "SELECT count(0) from bigdata_fct_eval WHERE use_type_id = "
				+ type;
		return BigDataFctEval.dao.find(sql);
	}
}
