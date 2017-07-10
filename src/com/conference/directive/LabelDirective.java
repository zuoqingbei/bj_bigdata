package com.conference.directive;

import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class LabelDirective implements TemplateDirectiveModel{

	@SuppressWarnings("rawtypes")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		String tableName=params.get("tableName")==null?"":params.get("tableName").toString();
		String filedName=params.get("filedName")==null?"":params.get("filedName").toString();
		String filedValue=params.get("filedValue")==null?"":params.get("filedValue").toString();
		List<Record> list=new ArrayList<Record>();
		if(StringUtils.isNotBlank(tableName)){
			StringBuffer sql=new StringBuffer();
			switch (tableName) {
			case "bigdata_fct_origin":
				//大单位
				sql.append(" select distinct from_dept as label_name from bigdata_fct_origin ");
				break;
			case "bigdata_d_dept":
				//办公室 部门
				/*sql.append(" select distinct name as label_name,id as label_id from bigdata_d_dept ");
				if(StringUtils.isNotBlank(filedName)&&StringUtils.isNotBlank(filedValue)){
					sql.append(" where "+filedName+"='"+filedValue+"' ");
				}else{
					sql.append(" where "+filedName+"!=0 ");
				}*/
				//部门
				sql.append(" select distinct name as label_name,id as label_id from bigdata_d_dept ");
				if(StringUtils.isNotBlank(filedName)&&StringUtils.isNotBlank(filedValue)){
					sql.append(" where "+filedName+"='"+filedValue+"' ");
				}else{
					sql.append(" where "+filedName+"!=0 ");
				}
				break;
			case "bigdata_d_creator":
				//发稿人
				sql.append(" select distinct name as label_name,id as label_id from bigdata_d_creator ");
				break;
			case "bigdata_d_ref":
				//方向 性质
				sql.append(" select distinct name as label_name,id as label_id from bigdata_d_ref ");
				if(StringUtils.isNotBlank(filedName)&&StringUtils.isNotBlank(filedValue)){
					sql.append(" where "+filedName+"='"+filedValue+"' ");
				}
				break;
			default:
				break;
			}
			list=Db.find(sql.toString());
		}
		env.setVariable("labelList", DEFAULT_WRAPPER.wrap(list));
		body.render(env.getOut());
	}

}
