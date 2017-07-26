package com.conference.directive;

import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.conference.admin.model.Dept;
import com.conference.admin.model.FctEval;
import com.conference.admin.model.FctZb;
import com.conference.common.Constant;
import com.conference.util.sys.SqlUtil;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
/**
 * 
 * @author zuoqb
 *新闻使用方式
 */
public class FctZbUseModelDirective implements TemplateDirectiveModel,Constant{
	@SuppressWarnings("rawtypes")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		int pageNum = 1;
		if (params.get("pageNum") != null){
			pageNum = new Integer(params.get("pageNum").toString());
		}
		Integer pageSize = params.get("pageSize") == null ? null : new Integer(params
				.get("pageSize").toString());
		if (pageSize == null){
			pageSize = 10;
		}
		String companyId=SqlUtil.getAttrByKey(request,SESSION_COMPANYID);
		String deptId=SqlUtil.getAttrByKey(request,SESSION_DEPTID);
		//大单位
		String selectDeptIds="";
		if(StringUtils.isNotBlank(companyId)&&StringUtils.isBlank(deptId)){
			//如果大单位不为空 而部门为空 根据大单位查询下属部门
			List<Record> list=Dept.dao.getDepts(companyId);
			for(int x=0;x<list.size();x++){
				selectDeptIds+=","+list.get(x).get("id")+",";
			}
		}
		//部门
		if(StringUtils.isNotBlank(deptId)){
			selectDeptIds=","+deptId+",";
		}
		String sqlWhere=SqlUtil.joinSqlForNews(request);
		Page<Record> pager=FctZb.dao.fctZbUseModelGroupByDate(sqlWhere,selectDeptIds,pageNum, pageSize);
		//String result=JsonKit.toJson(pager.getList());
		List<Record> useTypes=FctEval.dao.fctEvalUseType(sqlWhere);
		env.setVariable("page", DEFAULT_WRAPPER.wrap(pager));
		env.setVariable("useTypes", DEFAULT_WRAPPER.wrap(useTypes));
		env.setVariable("useTypesSize", DEFAULT_WRAPPER.wrap(useTypes.size()*3));
		body.render(env.getOut());
	}

}
