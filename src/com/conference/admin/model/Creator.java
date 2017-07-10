package com.conference.admin.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.conference.common.BaseModel;

public class Creator extends BaseModel<Creator>{
	private static final long serialVersionUID = -8811409081417389372L;
	public static final Creator dao=new Creator();
	/**
	 * 
	 * @time   2017年7月6日 下午2:15:10
	 * @author zuoqb
	 * @todo   根据部门获取发稿人
	 */
	public List<Creator> getCreatorByDept(String  deptId){
		String sql="";
		sql+=" select * from bigdata_d_creator ";
		if(StringUtils.isNotBlank(deptId)){
			sql+=" where id in( select creator_id from bigdata_r_dept_creator where dept_id='"+deptId+"') ";
		}
		return Creator.dao.find(sql);
	}

}
