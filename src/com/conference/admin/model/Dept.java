package com.conference.admin.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.conference.common.BaseModel;

public class Dept extends BaseModel<Dept>{
	private static final long serialVersionUID = -8811409081417389372L;
	public static final Dept dao=new Dept();
	/**
	 * 
	 * @time   2017年7月6日 下午2:15:10
	 * @author zuoqb
	 * @todo   根据大单位获取部门
	 */
	public List<Dept> getDepts(String  pid){
		String sql="";
		List<Dept> list;
		if(StringUtils.isBlank(pid)){
			sql+=" select * from bigdata_d_dept  where pid!=0 and pid is not null ";
		}else{
			sql+=" select * from bigdata_d_dept  where pid='"+pid+"' ";
		}
		list=Dept.dao.find(sql);
		//循环查询子集
	/*	for(Dept d:list){
			d.set("children", getDepts(d.get("pid")+""));
		}*/
		return list;
	}
	/**
	 * 
	 * @time   2017年7月6日 下午2:14:53
	 * @author zuoqb
	 * @todo   一级 大单位
	 */
	public List<Dept> getCompanys(){
		String sql="";
		sql+=" select * from bigdata_d_dept  where pid=0 or pid is null ";
		List<Dept> list=Dept.dao.find(sql);
		return list;
	}
	

}
