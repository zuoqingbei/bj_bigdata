<#macro upPageM  pageSize=10 pageNumber=1 totalRow=0 tmplId=""  dataUrl="" pageDivId="" searchForm="searchForm" tagBody="false"  table_order_feild="" >
<#-- 
1.插件说明：上拉刷新分页信息插件
2.需要参数说明:
pageSize：              可选，page分页对象,的页面长度
pageNumber：        可选，page分页对象的第几页
totalRow：              可选，page分页对象,一共多少行数据
tmplId：                    必须，加载数据的模板tmplId,用jQuery templ加载,且tmplId是控件的前缀
dataUrl：                 必须，加载数据的ajaxUrl
pageDivId：           必须，加载数分页list的div容器
searchForm:   可选，指定要查询的form表单，将表单里面的所有内容元素 全部放入分页 查询 参数中取，默认表名searchForm
table_order_feild: 可选，如果需要自动化排序，需要将table_order_feild字段传进来
tagBody：                  可选，可以自己设计控件样式和大小，控件的ID 要和传进来的 docId一样，默认ID为echarts1
 -->
 
<#if (tagBody == "false")>

<#else>
	<#nested> 
</#if>

<script type="text/javascript">
//引入JS、CSS文件
importJSCSS("/script/jquery-tmpl-master/jquery.tmpl.min.js");
$(function(){
    var canLoadMore=1;//是否可以加载更多,防止一次加载太多次
	$(window).scroll(function () {
		var scrollTop = $(this).scrollTop();
	    var scrollHeight = $(document).height();
	    var windowHeight = $(this).height();
	    if ((scrollTop + windowHeight == scrollHeight)&&!${tmplId!}pagePlugin.isLast&&canLoadMore) {
	    	canLoadMore=0;
	    	setTimeout(function(){
	    		${tmplId!}pagePlugin.addNextPage();
	    		canLoadMore=1;
	    	},1000);   
	    }
	});
})

var ${tmplId!}pagePlugin={
	pageSize : ${pageSize!10},
	totalRow : ${totalRow!0},
	pageNumber : ${pageNumber!1},
    dataUrl:'${dataUrl!}',
    tmplId:'${tmplId!}',
    pageDivId:'${pageDivId!}',
    isLast:0,
  	//加载下一页
    addNextPage:function(){
//     	1.显示正在加载中
    	if(this.pageNumber==1){
    		this.clearPage(this.pageDivId);
		}
    	var msgDiv=$("<center id='pageMsg' class='page-msg'>正在加载...</center>");
    	$("#"+this.pageDivId).append(msgDiv);
//     	2.准备请求参数
    	//获取表单的所有值
    	var params = serializeForm("${searchForm}"); 
    	var url=this.dataUrl;
    	if(url.indexOf("?")!=-1){
    		url+="&"+params;
    	}else{
    		url+="?"+params;
    	}
//     	3.ajax请求,填写分页数据
		var that=this;
    	$.get(url,{p:this.pageNumber,pageSize:this.pageSize},function(data){
    		that.pageSize=data.pageSize;
    		that.totalRow=data.totalRow;
    		that.pageNumber++;
    		that.addOnePageData(data.list,that.tmplId,that.pageDivId);
    		if(data.totalPage<that.pageNumber){
    			msgDiv.remove();
    			if(that.totalRow==0){
    				that.isEmpty(that.pageDivId);
    			}
    			that.isLast=1;
    		}else{
    			msgDiv.remove();
    			msgDiv=$("<center id='pageMsg' class='page-msg'>加载完成!</center>");
    	    	$("#"+that.pageDivId).append(msgDiv);
    			setTimeout(function(){
    				msgDiv.hide('slow');
    				msgDiv.remove('slow');
    			}, 2000);
    		}
    	})
    },
  //加载上一页
    addBeforePage:function(){
    	//TODO
    },
  //清除页面内容
    clearPage:function(pageDivId){
    	$("#"+pageDivId).empty();
    },
  //填写一页的数据到页面,页面需要自己定制可重写此方法
    addOnePageData:function(jsonList,tmplId,pageDivId){
    	//模板化数据
   	　　    $("#"+tmplId).tpl(jsonList,{
   	    	dateFormat: function (name,dateFormat) {
   	        	return common_date_formatCSTDate(this.data[name],dateFormat);
   	     	}
   	     }).appendTo('#'+pageDivId);
    },
    //没有数据了
    isEmpty:function(pageDivId){
    	var msgDiv=$("<center id='pageMsg' class='page-msg'>已经是最后数据</center>");
    	$("#"+pageDivId).append(msgDiv);
    }
}



</script>
</#macro>