package com.conference.directive;

import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.conference.admin.model.FctZb;
import com.conference.common.Constant;
import com.conference.util.JsonKit;
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
 *新闻分析列表数据
 */
public class FctZbListDirective implements TemplateDirectiveModel,Constant{
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
		String sqlWhere=SqlUtil.joinSqlForNews(request);
		Page<Record> pager=FctZb.dao.fctZbAnalysis(sqlWhere, pageNum, pageSize);
		String result=JsonKit.toJson(pager.getList());
		System.out.println(result);
		env.setVariable("page", DEFAULT_WRAPPER.wrap(pager));
		body.render(env.getOut());
	}

}
