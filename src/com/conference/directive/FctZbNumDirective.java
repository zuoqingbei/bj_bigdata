package com.conference.directive;

import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.conference.admin.model.FctZb;
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
 *新闻数量统计统计 类型type：0-总量统计 1-同比统计  环比统计
 */
public class FctZbNumDirective implements TemplateDirectiveModel,Constant{
	@SuppressWarnings("rawtypes")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		String type=params.get("type")==null?"0":params.get("type").toString();
		Record result=new Record();
		switch (type) {
		case "0":
			String sqlWhere=SqlUtil.joinSqlForNews(request);
			result=FctZb.dao.statisticFctZb(sqlWhere);
			result.set("num", SqlUtil.formatNumber(result.getLong("num")+""));
			break;
		case "1":
			String sqlWhere1=SqlUtil.joinSqlForNews(request,false);
			result=FctZb.dao.fctZbHbTb(sqlWhere1,SqlUtil. getAttrByKey(request,SESSION_STARTDATE), SqlUtil. getAttrByKey(request,SESSION_ENDDATE));
			break;
		default:
			break;
		}
		env.setVariable("data", DEFAULT_WRAPPER.wrap(result));
		body.render(env.getOut());
	}

}
