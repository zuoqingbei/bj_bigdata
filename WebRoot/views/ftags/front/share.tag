<#macro share style="pop" popPos="scatter" url="" title="" description="" pic="" reason="" shareChannels="">
<#-- 
分析有2种形式,
1:弹出形式,弹出框然后点击按钮分析,弹出方式需要在插件外面写一个页面元素加上,class="-mob-share-open" data-theme="slide-right" 2个样式即可调用弹出框
2:按钮形式,直接一排按钮点击就分享,目前按钮的图片没有下载全
需要参数说明
style:		必填，展现方式，传pop为弹出式，botton为按钮组式.
popPos:		可选，pop弹出式的弹出方式,不传默认效果,如scatter 默认效果,slide-top上边划出,slide-bottom下面划出,slide-left左边划出,slide-right右侧划出
css:		可选，按钮组的样式
url:		可选，分享链接,如果不填默认取 session.share_sdk_url
title:		可选，分享标题,如果不填默认取 session.share_sdk_title
description:可选，分享内容,如果不填默认取 session.share_sdk_description
pic:		可选，分享图片使用逗号,隔开,如果不填默认取 session.share_sdk_pic
reason:		可选，分析原因只应用与QZone与朋友网,如果不填默认取 session.share_sdk_reason
shareChannels:	可选，分享渠道列表,不填默认有新浪微博/QQ空间/QQ好友/微信/豆瓣/facebook.如果填写则填写项如下:
新浪微博	weibo
腾讯微博	tencentweibo
微信		weixin
QQ空间	qzone
QQ好友	qq
人人网		renren
开心网		kaixin
豆瓣网		douban
Facebook	facebook
Twitter	twitter
Pocket	pocket
Google+	google
有道云笔记	youdao
明道		mingdao
朋友网		pengyou
Tumblr	tumblr
Instapaper	instapaper
Linkedin	linkedin
 -->

<!-- 展示窗口 -->
<#if style=='pop'>
<!-- pop -->
<div class="-mob-share-ui -mob-share-ui-theme-${popPos} ${css!}" style="display: none">
    <ul class="-mob-share-list">
    <#if shareChannels?? && shareChannels!=''>
    	<#list shareChannels?split(",") as channel>
    		<li class="-mob-share-${channel!}">
    		<p>
    		<#if channel=='weibo'>
    			新浪微博
    		<#elseif channel=='tencentweibo'>
    			腾讯微博
    		<#elseif channel=='qzone'>
    			QQ空间
    		<#elseif channel=='tencentweibo'>
    			腾讯微博
    		<#elseif channel=='qzone'>
    			QQ空间
    		<#elseif channel=='qq'>
    			QQ好友
    		<#elseif channel=='weixin'>
    			微信
    		<#elseif channel=='douban'>
    			豆瓣
    		<#elseif channel=='renren'>
    			人人网
    		<#elseif channel=='kaixin'>
    			开心网
    		<#elseif channel=='facebook'>
    			Facebook
    		<#elseif channel=='twitter'>
    			Twitter
    		<#elseif channel=='pocket'>
    			Pocket
    		<#elseif channel=='google'>
    			Google+
    		<#elseif channel=='youdao'>
    			有道云笔记
    		<#elseif channel=='mingdao'>
    			明道
    		<#elseif channel=='pengyou'>
    			朋友网
    		<#elseif channel=='tumblr'>
    			Tumblr
    		<#elseif channel=='instapaper'>
    			Instapaper
    		<#elseif channel=='linkedin'>
    			LinkedIn
    		</#if>
    		</p>
    		</li>
        </#list>  
    <#else>
    	<li class="-mob-share-weibo"><p>新浪微博</p></li>
        <li class="-mob-share-weixin"><p>微信</p></li>
        <li class="-mob-share-qzone"><p>QQ空间</p></li>
        <li class="-mob-share-qq"><p>QQ好友</p></li>
        <li class="-mob-share-tencentweibo"><p>腾讯微博</p></li>
        <li class="-mob-share-douban"><p>豆瓣</p></li>
        <li class="-mob-share-facebook"><p>Facebook</p></li>
        <li class="-mob-share-twitter"><p>Twitter</p></li>
    </#if>
    </ul>
    <div class="-mob-share-close">取消</div>
</div>
<div class="-mob-share-ui-bg"></div>
<#else>
<style>
.share_img{width: 25px;height: 25px;}
</style>
<!-- button -->
 <span class="${css!}  meeting_middle">分享：
     <#if shareChannels?? && shareChannels!=''>
    	<#list shareChannels?split(",") as channel>
    		<img src="/images/icon/${channel!}.png"   title="
    		<#if channel=='weibo'>
    			新浪微博
    		<#elseif channel=='tencentweibo'>
    			腾讯微博
    		<#elseif channel=='qzone'>
    			QQ空间
    		<#elseif channel=='tencentweibo'>
    			腾讯微博
    		<#elseif channel=='qzone'>
    			QQ空间
    		<#elseif channel=='qq'>
    			QQ好友
    		<#elseif channel=='weixin'>
    			微信
    		<#elseif channel=='douban'>
    			豆瓣
    		<#elseif channel=='renren'>
    			人人网
    		<#elseif channel=='kaixin'>
    			开心网
    		<#elseif channel=='facebook'>
    			Facebook
    		<#elseif channel=='twitter'>
    			Twitter
    		<#elseif channel=='pocket'>
    			Pocket
    		<#elseif channel=='google'>
    			Google+
    		<#elseif channel=='youdao'>
    			有道云笔记
    		<#elseif channel=='mingdao'>
    			明道
    		<#elseif channel=='pengyou'>
    			朋友网
    		<#elseif channel=='tumblr'>
    			Tumblr
    		<#elseif channel=='instapaper'>
    			Instapaper
    		<#elseif channel=='linkedin'>
    			LinkedIn
    		</#if>
    		" class="-mob-share-${channel!} share_img" />
        </#list>  
    <#else>
<!--     	<img src="/images/icon/qq.png" 	 	title="QQ好友" class="-mob-share-qq share_img"/>
		<img src="/images/icon/weixin.png" 	title="微信" class="-mob-share-weixin share_img"/>
	    <img src="/images/icon/weibo.png"  	title="新浪微博" class="-mob-share-weibo share_img"/> -->
	    <span title="QQ好友" class="meeting_img meeting_qq meeting_middle -mob-share-qq share_img"></span>
	    <span title="微信" class="meeting_img meeting_weixin meeting_middle -mob-share-weixin share_img"></span>
	    <span title="新浪微博" class="meeting_img meeting_weibo meeting_middle -mob-share-weibo share_img"></span>
    </#if>
     
 </span>
</#if>

<style>
.meeting_img{ width:25px; height: 25px; display: inline-block; cursor: pointer;}
.meeting_qq{  background:url(/images/icon/qq0.png) 0 0 / 25px 25px  no-repeat }
.meeting_qq:hover{  background:url(/images/icon/qq.png) 0 0 / 25px 25px  no-repeat }
.meeting_weixin{  background:url(/images/icon/weixin0.png) 0 0 / 25px 25px  no-repeat }
.meeting_weixin:hover{  background:url(/images/icon/weixin.png) 0 0 / 25px 25px  no-repeat }
.meeting_weibo{  background:url(/images/icon/weibo0.png) 0 0 / 25px 25px  no-repeat }
.meeting_weibo:hover{  background:url(/images/icon/weibo.png) 0 0 / 25px 25px  no-repeat }
</style>
<script type="text/javascript">
importJSCSS("http://f1.webshare.mob.com/code/mob-share.js?appkey=54d178623e6");
importJSCSS("http://f1.webshare.mob.com/code/mob-share.css");
importJSCSS("/script/commonJs/ua.min.js");
// url:		可选，分享链接,如果不填默认取 session.share_sdk_url
// title:		可选，分享标题,如果不填默认取 session.share_sdk_title
// description:可选，分享内容,如果不填默认取 session.share_sdk_description
// pic:		可选，分享图片使用逗号,隔开,如果不填默认取 session.share_sdk_pic
// reason:		可选，分析原因只应用与QZone与朋友网,如果不填默认取 session.share_sdk_reason
var app;
var sharePlugin={
	<#if url ?? && url!=''>
	url:'${url!}',
	<#else>
	url:'${session.share_sdk_url!}',
	</#if>
	<#if title ?? && title!=''>
	title:'${title!}',
	<#else>
	title:'${session.share_sdk_title!}',
	</#if>
	<#if description ?? && description!=''>
	description:'${description!}',
	<#else>
	description:'${session.share_sdk_description!}',
	</#if>
	<#if pic ?? && pic!=''>
	pic:'${pic!}',
	<#else>
	pic:'${session.share_sdk_pic!}',
	</#if>
	<#if reason ?? && reason!=''>
	reason:'${reason!}',
	<#else>
	reason:'${session.share_sdk_reason!}',
	</#if>
	initShareSdk:function(){//sharesdk分享方法
		mobShare.config( {
		    debug: false, // 开启调试，将在浏览器的控制台输出调试信息
		    appkey: '54d178623e6', // appkey
		    params: {
		        url: this.url, // 分享链接
		        title: this.title, // 分享标题
		        description: this.description, // 分享内容
		        pic: this.pic, // 分享图片，使用逗号,隔开
		        reason:this.reason//只应用与QZone与朋友网
		    },
		    /**
		     * 分享时触发的回调函数
		     * 分享是否成功，目前第三方平台并没有相关接口，因此无法知道分享结果
		     * 所以此函数只会提供分享时的相关信息
		     * @param {String} plat 平台名称
		     * @param {Object} params 实际分享的参数 { url: 链接, title: 标题, description: 内容, pic: 图片连接 }
		     */
		    callback: function( plat, params ) {
		    	
		    }
		});
	},
	initDoctorAppShare:function(){//掌上医讯分享方法
		//掌上医讯app分享参数
		app={
			url: this.url, // 分享链接
	        title: this.pic, // 分享标题
	        description: this.description, // 分享内容
			thumb:this.pic
		};
		if(navigator.userAgent.indexOf("doctorpda")>0){
			try{
				  if(UA.isAndroid()){
				   	window.android.to_app_data(JSON.stringify(app));
				  }else if (UA.isIOS()){
				   	document.location='doctorpda/to_app_data?json='+JSON.stringify(app);
				  }
			}catch (e) {
			}
		}
	}
}

//初始化插件 share sdk
$(function(){
	sharePlugin.initShareSdk();
})
//掌上医讯
window.onload=function (){
	sharePlugin.initDoctorAppShare();
} 
</script>
</#macro>