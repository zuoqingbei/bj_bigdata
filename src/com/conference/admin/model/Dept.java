package com.conference.admin.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.conference.common.BaseModel;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class Dept extends BaseModel<Dept>{
	private static final long serialVersionUID = -8811409081417389372L;
	public static final Dept dao=new Dept();
	/**
	 * 
	 * @time   2017年7月6日 下午2:15:10
	 * @author zuoqb
	 * @todo   根据大单位获取部门
	 */
	public List<Record> getDepts(String  pid){
		String sql="";
		List<Record> list;
		if(StringUtils.isBlank(pid)){
			sql+=" select * from bigdata_d_dept  where pid!=0 and pid is not null ";
		}else{
			sql+=" select * from bigdata_d_dept  where pid='"+pid+"' ";
		}
		list=Db.find(sql);
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
	public List<Record> getCompanys(){
		String sql="";
		sql+=" select * from bigdata_d_dept  where pid=0 or pid is null ";
		List<Record> list=Db.find(sql);
		return list;
	}
	public List<Dept> getAllCompanys(){
		String sql="";
		sql+=" select * from bigdata_d_dept  where pid=0 or pid is null ";
		List<Dept> list=Dept.dao.find(sql);
		return list;
	}
	public List<Dept> getAllDepts(){
		String sql="";
		sql+=" select * from bigdata_d_dept  where pid!=0 and pid is not null";
		List<Dept> list=Dept.dao.find(sql);
		return list;
	}
	/**
	 * 
	 * @time   2017年7月25日 上午9:48:46
	 * @author zuoqb
	 * @todo   获取全部部门
	 */
	public List<Record> getAllDeptsList(){
		String sql=" select * from bigdata_d_dept order by id  ";
		List<Record> list=Db.find(sql);
		return list;
	}
	public Map<String,Record> getAllDeptsMap(){
		List<Record> list=getAllDeptsList();
		Map<String,Record> map=new HashMap<String,Record>();
		for(Record r:list){
			map.put(r.get("id")+"", r);
		}
		return map;
	}
}
