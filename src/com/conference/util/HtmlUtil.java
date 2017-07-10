package com.conference.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlUtil {
	/**
	 * 处理html中的图片src属性，供前端根据页面尺寸加载对应图片功能
	 * @param content
	 * @return
	 */
	public static final String formatContentImage(String content){
		if(StringKit.isBlank(content)){
			return content;
		}
		Document doc = Jsoup.parse(content);
        Elements media = doc.select("img");
        for (Element src : media) {
        	src.attr("data-url",src.attr("src"));
        	src.attr("src","data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7");
        }
        return doc.toString();
	}
	/**
	 * 如果URL 中存在http:// 去掉后返回
	 * @author Chengzl
	 * @date 2013-8-22上午09:19:10
	 * @return
	 */
	public static final String dealUrl(String httpUrl){
		if(StringKit.notBlank(httpUrl)&&httpUrl.contains("http://")){
			httpUrl=httpUrl.replaceFirst("http://", "");
		}
		return httpUrl;
	}

}
