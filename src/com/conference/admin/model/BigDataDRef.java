package com.conference.admin.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.conference.common.BaseModel;
/**
 * 
 * 参照维表，包含方向、性质等
 *
 */
public class BigDataDRef extends BaseModel<BigDataDRef>{
	private static final long serialVersionUID = -8811409081417389372L;
	public static final BigDataDRef dao=new BigDataDRef();
	/**
	 * 
	 * @time   2017年7月6日 下午2:15:10
	 * @author zuoqb
	 * @todo   根据类型获取方向性质
	 */
	public List<BigDataDRef> getBigDataDRefByType(String  type){
		String sql="";
		sql+=" select * from bigdata_d_ref ";
		if(StringUtils.isNotBlank(type)){
			sql+=" where type='"+type+"' ";
		}
		return BigDataDRef.dao.find(sql);
	}

}
