package com.conference.admin.model;

import com.conference.common.BaseModel;

public class FctOrigin extends BaseModel<FctOrigin>{
	private static final long serialVersionUID = -8811409081417389372L;
	public static final FctOrigin dao=new FctOrigin();
	/**
	 * 素材量统计
	 * @param sqlWhere
	 * @return
	 */
	public FctOrigin statisticOrigin(String  sqlWhere){
		String sql="";
		sql+=" select count(1) as num from bigdata_fct_origin org left join bigdata_d_creator cre on org.creator_id=cre.id";
		sql+=" left join bigdata_d_dept dep on org.dept_id=dep.id ";
		sql+=sqlWhere;
		return FctOrigin.dao.findFirst(sql);
	}
	/**
	 * 同比环比计算
	 * @param sqlWhere
	 * @return
	 */
	public FctOrigin originHbTb(String  sqlWhere,String dateTime){
		StringBuffer sql=new StringBuffer();
		String fromSql=" from bigdata_fct_origin org left join bigdata_d_creator cre on org.creator_id=cre.id left join bigdata_d_dept dep on org.dept_id=dep.id "+sqlWhere;
		String selectSql=" select *,(a.currentmonth-b.premonth)/b.premonth*100 as tb,(a.currentmonth-c.preyear)/c.preyear*100 as hb from ";
		sql.append(selectSql);
		sql.append(" (select count(1) as currentMonth ").append(fromSql);
		sql.append(" and date_format(org.created_time,\"%Y-%m\")=\"2017-06\")a ");
		sql.append(" join ");
		
		sql.append(" (select count(1) as preMonth ").append(fromSql);
		sql.append(" and date_format(org.created_time,\"%Y-%m\")=\"2017-05\")b ");
		sql.append(" join ");
		
		sql.append(" (select count(1) as preYear ").append(fromSql);
		sql.append(" and date_format(org.created_time,\"%Y-%m\")=\"2016-06\")c ");
		sql.append(" on 1=1 ");
		return FctOrigin.dao.findFirst(sql.toString());
	}

}
