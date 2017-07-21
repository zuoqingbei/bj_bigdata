package com.conference.admin.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.conference.common.BaseModel;
import com.conference.util.sys.SqlUtil;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
/**
 * 
 * @author 新闻评价
 *
 */
public class FctEval extends BaseModel<FctEval>{
	private static final long serialVersionUID = -8811409081417389372L;
	public static final FctEval dao=new FctEval();
	
	/**
	 * 
	 * @time   2017年7月14日 上午9:24:48
	 * @author zuoqb
	 * @todo   使用方式 批示 评论级别统计
	 */
	public List<Record> fctEvalByFiled(String  sqlWhere,String filed){
		StringBuffer sql=new StringBuffer();
		sql.append(" select count(1) as count, ");
		sql.append(" case "+filed+" when '0' then '无' when '1' then '有' else "+filed+" end as name ");
		sql.append(" from bigdata_fct_eval fct_eval   ");
		sql.append(" where fct_eval.zb_id in(");
		sql.append(" select fct_zb.zb_id from bigdata_fct_zb fct_zb where 1=1 "+sqlWhere); 
		sql.append(" ) group by "+filed+" ");
		sql.append(" order by count("+filed+") ");
		return Db.find(sql.toString());
	}

}
