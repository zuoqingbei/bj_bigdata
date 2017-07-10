<#macro echarts url docId="echarts1" tagBody="false" title="" 
	chartType="bar"  showToolBox="true" showLegend="true" clickFunc="false" mapCountMax=50 color="#228AB4" 
	titleColor="#000" titleFontSize=16	topic="macarons" xRotate=0 yRotate=0 subtext="" legend="" 
	xAxisName="" yAxisName="" number=1>
<#-- 
需要参数说明
url:		必须，请求表格数据的URL,不用表格的数据可能要求的格式不同，具体格式见，getData方法
docId:		可选，图表控件的ID，如果不填写默认为echarts1
tagBody：	可选，可以自己设计控件样式和大小，控件的ID 要和传进来的 docId一样，默认ID为echarts1
title：		可选，图表的 标题   文字
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
topic:可选，echarts主题(目前支持：blue,dark,gray,green,helianthus,infographic,macarons,shine),
默认macarons，可自定义。自定义js放到theme文件下，js名称以主题名称命名，里面的方法名称function get+“主题名”+Theme()
titleColor:图表的 标题 文字颜色color（可以为空，默认#000）
titleFontSize:图表的 标题 字号fontSize（可以为空，默认16）
subtext:可选，副标题
xRotate,yRotate:可选，标签旋转角度，默认为0，不旋转，正值为逆时针，负值为顺时针，可选为：-90 ~ 90
legend:可选，用逗号拼接并且必须与series参数的name对应
xAxisName:可选，x轴名称(适用于存在坐标轴)
yAxisName:可选，y轴名称(适用于存在坐标轴)
number:可选，默认1 折线图或者柱状图数量（只适用于line bar）,如果是多个折线或者柱子，
数据格式要求返回一个Map格式json，key-柱子下标 0--number，value：对应数据list
（备注：每个list的数据结构和数量相同）
color:可选， 线的颜色，多个用“，”拼接
 -->

<#if (tagBody == "false")>
	<!-- 为ECharts准备一个具备大小（宽高）的Dom 默认标签ID为echarts1-->
	<div id="${docId}" style="height: 300px;width:95%;"></div>
<#else>
	<#nested> 
</#if>

<script type="text/javascript">
	importJSCSS("/script/echarts/dist/echarts.js");
	importJSCSS("/script/echarts/dist/theme/${topic}.js");
	var myChart${docId};//图表实体变量
	var ecConfig;
	//所以这里要更具chartType扩展 initChart方法，每个chartType 一个初始化表格的方法
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
					'echarts/chart/heatmap','echarts/chart/map'];
			var userChartFun${docId} = function(ec) {
				// 基于准备好的dom，初始化echarts图表
				myChart${docId} = ec.init(document.getElementById("${docId}"),get${topic}Theme());
				ecConfig= require('echarts/config');
				echarts${docId!}Plugin.initChart();
			};
			//3. 使用插件
			require(requireType, userChartFun${docId});
	});
	var echarts${docId!}Plugin={
			//1.0 初始化设置
			initSetting:function(){
				var setting={
						myChart:null,//图表实体变量 与3.0不同 这边不能进行初始化（无效参数）
						docId:"${docId}",//图表document ID
						chartType:"${chartType}",//图表类型
						url:"${url}",
						title:"${title}",
						titleColor:"${titleColor}",
						titleFontSize:"${titleFontSize}",
						subtext:"${subtext}",
						showLegend:${showLegend},
						legend:"${legend}",
						showToolBox:${showToolBox},
						xAxisLabel:"${xRotate}",//x轴旋转度数
						xAxisName:"${xAxisName}",
						yAxisLabel:"${yRotate}",//y轴旋转度数
						yAxisName:"${yAxisName}",
						number:"${number}",//线 柱数量
						color:'${color!}'.split(","),//线 柱颜色
						grid:{x:55,y:50,x2:35,y2:30,}
					}
				return setting;
			},
			//2.1初始化注册用户统计表
			initChart:function (condition) {
				//根据chartType调用不同的图表初始化方法
				try{
					if(this.initSetting().chartType=='line'||this.initSetting().chartType=='bar')
						this.initChartLineAndBar(condition);
					else if(this.initSetting().chartType=='pie')
						this.initChartPie(condition);
					else if(this.initSetting().chartType=='map')
						this.initChartMap(condition);
					else if(this.initSetting().chartType=='pieRose')
						this.initChartPieRose(condition);
					else if(this.initSetting().chartType=='gauge')
						this.initChartGauge(condition);
				}catch(e){
				}
			},

			
			
			//最后加载对象 和click方法
			finalInit:function (option){
				//3.为元素添加clikc方法
				<#if (clickFunc !="false")>
					myChart${docId}.on(ecConfig.EVENT.CLICK, ${clickFunc});
				</#if>
				//4.为echarts对象加载数据 和配置
				myChart${docId}.setOption(option,true);
			},
			
			//从后台获取数据,返回keys 和values的map，根据表格类型不同，可能有变化
			getData:function (conditions){
				var chartType=this.initSetting().chartType;
				var number=this.initSetting().number;
				var finalMap=new Map(); 
				var keys = new Array();
				var values = new Array();
				var otherValues = new Array();
				var keysMap = new Map();
				var valuesMap = new Map();
				var otherValuesMap =new Map();
				$.ajaxSettings.async = false;
				$.getJSON(this.initSetting().url,conditions, function(data) {
					if(data instanceof Array){
						for (var i = 0; i < data.length; i++) {
							if(chartType=='line'||chartType=='bar'){
								keys.push(data[i].NAME);
								values.push(data[i].COUNT);
							}else if(chartType=='pie'||chartType=='pieRose'){
								keys.push(data[i].NAME)
								var onedata={value:data[i].COUNT,name:data[i].NAME};
								values.push(onedata)
							}else if(chartType=='map'){
								keys.push(data[i].NAME)
								var onedata={value:data[i].COUNT,name:data[i].NAME};
								values.push(onedata);
								if(data[i].NAME=="未填"||data[i].NAME=="未知"){
									otherValues.push(onedata);
								}
							}
						}
						keysMap.put(0,keys);
						valuesMap.put(0,values);
						otherValuesMap.put(0,otherValues)
						finalMap.keys=keysMap;
						finalMap.values=valuesMap;
						finalMap.otherValues=otherValuesMap;
					}else{
						//返回的Map类型数据
						for(var num=0;num<parseInt(number);num++){
							//清空Map  Array
							keys = new Array();
							values = new Array();
							otherValues = new Array();
							for (var i = 0; i < data[num].length; i++) {
								if(chartType=='line'||chartType=='bar'){
									keys.push(data[num][i].NAME);
									values.push(data[num][i].COUNT);
								}else if(chartType=='pie'||chartType=='pieRose'){
									keys.push(data[num][i].NAME)
									var onedata={value:data[num][i].COUNT,name:data[num][i].NAME};
									values.push(onedata)
								}else if(chartType=='map'){
									keys.push(data[num][i].NAME)
									var onedata={value:data[num][i].COUNT,name:data[num][i].NAME};
									values.push(onedata);
									if(data[num][i].NAME=="未填"||data[num][i].NAME=="未知"){
										otherValues.push(onedata);
									}
								}
							}
							keysMap.put(num,keys);
							valuesMap.put(num,values);
							otherValuesMap.put(num,otherValues);
							
						}
						finalMap.keys=keysMap;
						finalMap.values=valuesMap;
						finalMap.otherValues=otherValuesMap;
					}
				});
				return finalMap;
			},
			
		//------------------------------------------------------不同图表不同初始化方法区----------------------------------------------------	
		
			
			
			//折线和柱形图
			initChartLineAndBar:function (condition){
				//1.获取数据
				var conditions = eval('(' + condition + ')');
				if(parseInt(this.initSetting().number)<1){
					this.initSetting().number=1;
				}
				var valuesMap = this.getData(conditions);
				var keys = valuesMap.keys.get(0);
				var values = valuesMap.values;
				if(keys.length==0){
					$("#${docId}").html("<div style='text-align: center;font-size: 30px;color: #aaa;line-height: 200px;'>暂无数据</div>");
					return;
				}
				//2.设置echarts参数
				var option = {
					//animation:false,
					addDataAnimation:true,
					grid  : this.initSetting().grid,
					title : {
						text:this.initSetting().title$,
						subtext:this.initSetting().subtext,
						textStyle:{
							  color: this.initSetting().titleColor,
							  fontSize:this.initSetting().titleFontSize,
						} 
					},
					color : this.initSetting().color,
					tooltip : {
						trigger : 'item'
					},
					legend : {
						data : this.initSetting().legend.split(",")
					},
					toolbox : {
						show : this.initSetting().showToolBox,
						feature : {
							mark : {
								show : true
							},
							dataView : {
								show : true,
								readOnly : false
							},
							magicType : {
								show : true,
								type : [ 'bar', 'line' ]
							},
							restore : {
								show : true
							},
							saveAsImage : {
								show : true
							}
						}
					},
					calculable : true,
					xAxis : [ {
						type : 'category',
						name:this.initSetting().xAxisName,
						axisLabel : {
							rotate:this.initSetting().xAxisLabel
						},
						boundaryGap : true,
						data : keys
					} ],
					yAxis : [ {
						type : 'value',
						name:this.initSetting().yAxisName,
						axisLabel : {
							formatter : '{value} ',
							rotate:this.initSetting().yAxisLabel
						}
					} ],
					series : []
				};
				for(var num=0;num<parseInt(this.initSetting().number);num++){
					var item={
							name : this.initSetting().legend.split(",")[num],
							type : this.initSetting().chartType,
							data : values.get(num),
							barMaxWidth : 40,
							markPoint : {
								data : [ {
									type : 'max',
									name : '最大值'
								}, {
									type : 'min',
									name : '最小值'
								} ]
							},
							markLine : {
								data : [ {
									type : 'average',
									name : '平均值'
								} ]
							}
						};
					option.series.push(item);
				}
				//3.为echarts对象加载数据 和配置
				this.finalInit(option);
			},
			//饼行图
			initChartPie:function (condition){
				//1.获取数据
				var conditions = eval('(' + condition + ')');
				var valuesMap = this.getData(conditions);
				var keys = valuesMap.keys.get(0);
				var values = valuesMap.values.get(0);
				
				if(keys.length==0){
					//alert("暂无数据");
					//return;
				}
				//2.设置echarts参数
				option = {
						//animation:false,
						addDataAnimation:true,
					    title : {
					        text: this.initSetting().title,
					        subtext:this.initSetting().subtext,
					        textStyle:{
								  color: this.initSetting().titleColor,
								  fontSize:this.initSetting().titleFontSize,
							},
					        x:'center'
					    },
					    tooltip : {
					        trigger: 'item',
					        formatter: "{a} <br/>{b} : {c} ({d}%)"
					    },
					  /*   legend: {
					    	show : showLegend${docId},
					        orient : 'horizontal',
					        x : '20',
					        y : 'bottom',
					        data:keys
					    },  */
					    toolbox: {
					        show : this.initSetting().showToolBox,
					        feature : {
					            mark : {show: true},
					            dataView : {show: true, readOnly: false},
					            magicType : {
					                show: true, 
					                type: ['pie', 'funnel'],
					                option: {
					                    funnel: {
					                        x: '25%',
					                        width: '50%',
					                        funnelAlign: 'left',
					                        max: 1548
					                    }
					                }
					            },
					            restore : {show: true},
					            saveAsImage : {show: true}
					        }
					    },
					    calculable : true,
					    series : [
					        {
					            name:title,
					            type:this.initSetting().chartType,
					            radius : '55%',
					            center: ['50%', '45%'],
					            data:values
					        }
					    ]
					};
				//3.为echarts对象加载数据 和配置
				this.finalInit(option);
			},
			
			
			//地图
			initChartMap:function (condition){
				//1.获取数据
				var conditions = eval('(' + condition + ')');
				var valuesMap = this.getData(conditions);
				var keys = valuesMap.keys.get(0);
				var values = valuesMap.values.get(0);
				var otherValues = valuesMap.otherValues.get(0);
				if(keys.length==0){
					//alert("暂无数据");
					//return;
				}
				
				//2.设置echarts参数
				option = {
						//animation:false,
						addDataAnimation:true,
					    title : {
					        text: this.initSetting().title$,
					        subtext:this.initSetting().subtext,
					        textStyle:{
								  color: this.initSetting().titleColor,
								  fontSize:this.initSetting().titleFontSize,
							},
					        x:'center'
					    },
					    tooltip : {
					        trigger: 'item'
					    },
					    /* legend: {
					        orient: 'vertical',
					        x:'left',
					        data:keys
					    }, */
					    dataRange: {
					    	  orient: 'horizontal',
					    	  y:'top',
					          min: 0,
					          max: ${mapCountMax!},
					          text:['高','低'],           // 文本，默认为数值文本
					          splitNumber:0
					    }, 
					    toolbox: {
					        show: this.initSetting().showToolBox,
					        orient : 'vertical',
					        x: 'right',
					        y: 'center',
					        feature : {
					            mark : {show: true},
					            dataView : {show: true, readOnly: false},
					            restore : {show: true},
					            saveAsImage : {show: true}
					        }
					    },
					   /*  roamController: {
					        show: true,
					        x: 'right',
					        mapTypeControl: {
					            'china': true
					        }
					    }, */
					    series : [
					        {
					            name: this.initSetting().legend,
					            type: 'map',
					            mapType: 'china',
					            mapLocation: {
					                x: 90
					            },
					            roam: false,
					            itemStyle:{
					                normal:{label:{show:true}},
					                emphasis:{label:{show:true}}
					            },
					            data:values
					        },
					        {
					            name:"其他",
					            type:'pie',
					            roseType : 'area',
					            tooltip: {
					                trigger: 'item',
					                formatter: "{a} <br/>{b} : {c} ({d}%)"
					            },
					            center: [document.getElementById("${docId}").offsetWidth - 250, 125],
					            radius: [20, 70],
					            data:otherValues
					        }
					    ]
					};
				//3.为echarts对象加载数据 和配置
				this.finalInit(option);
			},
			
			
			//玫瑰饼形图
			initChartPieRose:function (condition){
				//1.获取数据
				var conditions = eval('(' + condition + ')');
				var valuesMap = this.getData(conditions);
				var keys = valuesMap.keys.get(0);
				var values = valuesMap.values.get(0);
				
				if(keys.length==0){
					//alert("暂无数据");
					//return;
				}
				//2.设置echarts参数
				 var option = {
								//animation:false,
								addDataAnimation:true,
		                	    title : {
		                	        text: this.initSetting().title,
		                	        subtext: this.initSetting().subtext,
		                	        textStyle:{
		      						  color: this.initSetting().titleColor,
		      						  fontSize:this.initSetting().titleFontSize,
		      						},
		                	        x:'center'
		                	    },
		                	    tooltip : {
		                	        trigger: 'item',
		                	        formatter: "{a} <br/>{b} : {c} ({d}%)"
		                	    },
		                	    legend: {
		                	    	show : this.initSetting().showLegend,
		                	        x : 'center',
		                	        y : 'bottom',
		                	        data:keys
		                	    },
		                	    toolbox: {
		                	        show : this.initSetting().showToolBox,
		                	        feature : {
		                	            mark : {show: true},
		                	            dataView : {show: true, readOnly: false},
		                	            magicType : {
		                	                show: true, 
		                	                type: ['pie', 'funnel']
		                	            },
		                	            restore : {show: true},
		                	            saveAsImage : {show: true}
		                	        }
		                	    },
		                	    calculable : true,
		                	    series : [
		                	        {
		                	            name:'面积模式',
		                	            type:'pie',
		                	           /*  radius : [30, 110],
		                	            center : ['50%', 200], */
		                	            roseType : 'area',
		                	            x: '50%',               // for funnel
		                	            max: 10,                // for funnel
		                	            sort : 'ascending',     // for funnel
		                	            data:values
		                	        }
		                	    ]
		                	};
				
				//3.为echarts对象加载数据 和配置
				this.finalInit(option);
			},
			
			//仪表盘
			initChartGauge:function (condition){
				//1.获取数据
				var conditions = eval('(' + condition + ')');
				var url = "${url}";
				//2.设置echarts参数
				 var option ={
					    tooltip : {
					        formatter: "{a} <br/>{b} : {c}%"
					    },
					    toolbox: {
					        feature: {
					        	 restore: {show: true},
					             saveAsImage: {show: true}
					        }
					    },
					    series: [
					        {
					            name: '业务指标',
					            type: 'gauge',
					            center : ['50%', '50%'], //仪表盘所处的位置 左  上
					            min: 0,
					            max: 100,
					            splitNumber: 10,
					            radius: '80%',//缩放
					            axisLine: {            // 坐标轴线
					                lineStyle: {       // 属性lineStyle控制线条样式
					                    width: 5//外表宽度
					                }
					            },
					            axisTick: {            // 坐标轴小标记
					                length: 15,        // 属性length控制线长
					                lineStyle: {       // 属性lineStyle控制线条样式
					                    color: 'auto'
					                }
					            },
					           //外轮数值 可进行特殊化设置
					            axisLabel: {
					                formatter:function(v){
					                    return v;
					                }
					            },
					            splitLine: {           // 分隔线
					                length: 20,         // 属性length控制线长
					                lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
					                    color: 'auto'
					                }
					            },
					            pointer: {
					                width:5//指针宽度
					            },
					            title: {
					                show: true,
					                offsetCenter: [0, '-40%'],       // x, y，单位px 标题位置
					                 textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
					                    fontWeight: 'bolder',
					                    fontSize: 16,
					                    fontStyle: 'italic'
					                }
					            },
					            detail: {formatter:'{value}%'},//鼠标放入显示数值
					            data: [{value: 5, name: '完成率'}]
					        }
					    ]
					};
				//3.为echarts对象加载数据 和配置
				this.finalGaugeInit(url,option,conditions);
			},
			//仪表盘数据动态加载 特殊
			finalGaugeInit:function (url,option,conditions){
				myChart${docId}.setOption(option,true);
				setInterval(function () {
					$.ajaxSettings.async = true;
					$.getJSON(url,conditions, function(data) {
							option.series[0].data[0].value = parseInt(data.RESULT);
						    myChart${docId}.setOption(option,true);
						}
					);
				},5000);
			}
			
			//其他表格类型，在这里面添加function initChart..${docId}(condition){}
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
		echarts${docId!}Plugin.initChart(condition);
	}
	
</script>
</#macro>