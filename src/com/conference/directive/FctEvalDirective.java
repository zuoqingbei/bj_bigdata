package com.conference.directive;

import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.conference.admin.model.FctEval;
import com.conference.common.Constant;
import com.conference.util.sys.SqlUtil;
import com.jfinal.plugin.activerecord.Record;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
/**
 * 
 * @author zuoqb
 *新闻使用方式 批示 评论级别统计
 */
public class FctEvalDirective implements TemplateDirectiveModel,Constant{
	@SuppressWarnings("rawtypes")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		String filed=params.get("filed")==null?"":params.get("filed").toString();
		String sqlWhere=SqlUtil.joinSqlForNews(request);
		List<Record> list=FctEval.dao.fctEvalByFiled(sqlWhere,filed);
		env.setVariable("list", DEFAULT_WRAPPER.wrap(list));
		body.render(env.getOut());
	}

}
