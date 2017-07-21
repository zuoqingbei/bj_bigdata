package com.conference.directive;

import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.conference.admin.model.FctOrigin;
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
 *素材数量统计统计 类型type：0-总量统计 1-总量同比统计  环比统计 2-素材转化率 3-素材转化率同比环比
 */
public class SourceNumDirective implements TemplateDirectiveModel,Constant{
	@SuppressWarnings("rawtypes")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		String type=params.get("type")==null?"0":params.get("type").toString();
		Record result=new Record();
		String sqlWhere="";
		switch (type) {
		case "0":
			//0-总量统计
			sqlWhere=SqlUtil.joinSqlForSource(request);
			result=FctOrigin.dao.statisticOrigin(sqlWhere);
			result.set("num", SqlUtil.formatNumber(result.getLong("num")+""));
			break;
		case "1":
			//1-总量同比统计  环比统计
			sqlWhere=SqlUtil.joinSqlForSource(request,false);
			result=FctOrigin.dao.originHbTb(sqlWhere,SqlUtil. getAttrByKey(request,SESSION_STARTDATE), SqlUtil. getAttrByKey(request,SESSION_ENDDATE));
			break;
		case "2":
			// 2-素材转化率
			sqlWhere=SqlUtil.joinSqlForSource(request);
			result=FctOrigin.dao.statisticOriginRate(sqlWhere);
			break;
		case "3":
			//素材转化率同比环比
			sqlWhere=SqlUtil.joinSqlForSource(request,false);
			result=FctOrigin.dao.originRateHbTb(sqlWhere,SqlUtil. getAttrByKey(request,SESSION_STARTDATE), SqlUtil. getAttrByKey(request,SESSION_ENDDATE));
			break;
		default:
			break;
		}
		env.setVariable("data", DEFAULT_WRAPPER.wrap(result));
		body.render(env.getOut());
	}

}
