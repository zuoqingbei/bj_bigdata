<#macro echarts url docId="echarts1" tagBody="false" title="" 
	chartType="bar"  showToolBox="true" showLegend="true" clickFunc="false" mapCountMax=50 color="#228AB4" 
	topic="macarons" xRotate=0 yRotate=0 subtext="" legend="">
<#-- 
需要参数说明
url:		必须，请求表格数据的URL,不用表格的数据可能要求的格式不同，具体格式见，getData方法
docId:		可选，图表控件的ID，如果不填写默认为echarts1
tagBody：	可选，可以自己设计控件样式和大小，控件的ID 要和传进来的 docId一样，默认ID为echarts1
title：		可选，图表的 标题
chartType：	可选，图表的类型，默认为bar
showToolBox:可选，是否显示工具条 boolean
showLegend: 可选，是否显示图例 boolean
mapCountMax: 可选，地图上的最大值
clickFunc:	可选，元素点击事件方法名，为false则没有
			方法eq:   function eConsole(param) {
                    var str = param.name + ":" + param.value;
                    alert(str)
                }
时间选择器的onchange方法名要和控件名称对应如：docId=reg onchange方法名就是refData+reg ，默认是refData
topic:echarts主题
xRotate,yRotate:标签旋转角度，默认为0，不旋转，正值为逆时针，负值为顺时针，可选为：-90 ~ 90
subtext:副标题
legend: 用逗号拼接并且必须与series参数的name对应
 -->

<#if (tagBody == "false")>
	<!-- 为ECharts准备一个具备大小（宽高）的Dom 默认标签ID为echarts1-->
	<div id="${docId}" style="height: 300px;width:95%;"></div>
<#else>
	<#nested> 
</#if>

<script type="text/javascript">
	importJSCSS("/script/echarts/dist/echarts.js");

	var myChart${docId};//图表实体变量
	var docId${docId}="${docId}";//图表document ID
	var topic${docId}="${topic}";//图标主题
	var chartType${docId} = "${chartType}";//图表类型
	var xAxisLabel${docId}="${xRotate}";//x轴旋转度数
	var yAxisLabel${docId}="${yRotate}";//y轴旋转度数
	var subtext${docId}="${subtext}";
	var legend${docId}="${legend}";
	var title${docId}="${title}";
	var ecConfig;
	
	$(document).ready(function() {
			//1. 路径配置
			require.config({
				paths : {
					echarts : '/script/echarts/dist'
				}
			});
			//2.准备参数
			var requireType = [ 'echarts', 'echarts/chart/bar',
					'echarts/chart/line', 'echarts/chart/k',
					'echarts/chart/scatter', 'echarts/chart/pie',
					'echarts/chart/radar', 'echarts/chart/force',
					'echarts/chart/chord', 'echarts/chart/gauge',
					'echarts/chart/funnel', 'echarts/chart/eventRiver',
					'echarts/chart/venn', 'echarts/chart/treemap',
					'echarts/chart/tree', 'echarts/chart/wordCloud',
					'echarts/chart/heatmap','echarts/chart/map' ];
			var userChartFun${docId} = function(ec) {
				// 基于准备好的dom，初始化echarts图表
				myChart${docId} = ec.init(document.getElementById(docId${docId}),topic${docId});
				ecConfig= require('echarts/config');
				initChart${docId}();
			};
			//3. 使用插件
			require(requireType, userChartFun${docId});
	});

	//2.1初始化注册用户统计表
	function initChart${docId}(condition) {
		var url = "${url}";
		var option = getData${docId}(url,condition);
		//3.为echarts对象加载数据 和配置
		finalInit${docId}(option);
	}

	//2.2刷新数据
	//后面如果要传入多个查询参数，可以传入 aa=1&bb=2 一个字符串 
	function refDataParam${docId}() {
		//获取刷新数据的搜索条件
		if(arguments.length!=1){
			return;
		}
		var paramsString=arguments[0];
		var params=paramsString.split("&");
		var condition="{";
		for(var i=0;i<params.length;i++){
			var tmp=params[i].split("=");
			condition+="\""+tmp[0]+"\":"+"\""+tmp[1]+"\",";
		}
		condition+="\"iDisplayLength\":"+"\"-1\",";
		condition+="}"
		
		initChart${docId}(condition);
	}
	
	
	//最后加载对象 和click方法
	function finalInit${docId}(option){
		//3.为元素添加clikc方法
		<#if (clickFunc !="false")>
			myChart${docId}.on(ecConfig.EVENT.CLICK, ${clickFunc});
		</#if>
		//4.为echarts对象加载数据 和配置
		myChart${docId}.setOption(option,true);
	}
	
	//从后台获取数据,返回keys 和values的map，根据表格类型不同，可能有变化
	function getData${docId}(url,conditions){
		var option;
		$.ajaxSettings.async = false;
		$.getJSON(url,conditions, function(data) {
			console.log(data)
			option=data;
		});
		return option;
	}
	

	//其他表格类型，在这里面添加function initChart..${docId}(condition){}
	
</script>
</#macro>