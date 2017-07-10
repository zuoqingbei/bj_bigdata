<#macro mobiscroll  selectId hiddenId level  datas="" dataUrl=""  defaultValue="" display="bottom"   placeholder="请选择" labels="" inputClass="" showClear="true" isShowQXZ="false">
<#-- 
1.插件说明：手机的mobiSrcoll 的scroll插件
2.需要参数说明:
selectId:   (必填)，display值控件的ID
hiddenId:   (必填)，value值控件的ID
level:   	(必填)，有几级数据
datas：                   (选填)，控件的选择数据jsonArray格式如:
dataUrl:    (选填)，如果不传datas可通过url请求ajax获取数据,返回数据格式如上
defaultValue:  (选填)，默认选择的数据,逗号隔开或者数据
display:  	(选填)，弹出位置
placeholder (选填)，提示字
inputClass  (选填)，选择框样式
showClear   (选填)，是否显示清除按钮,默认选择
isShowQXZ:  (选填)，是否给数据添加请选择数据
 -->
<input id="${selectId!}" type="text" name="${selectId!}"  class="${inputClass!}" placeholder="${placeholder! }"/>
<input id="${hiddenId!}" type="text" name="${hiddenId!}" />


<script type="text/javascript">
//引入JS、CSS文件
importJSCSS("/script/Jquery-Mobiscroll/css/mobiscroll.custom-3.0.0-beta4.min.css");
importJSCSS("/script/Jquery-Mobiscroll/js/mobiscroll.custom-3.0.0-beta4.js");
</script>
<script type="text/javascript">
$(function(){
	${selectId!}MobiscrollPlugin.init();
})
//测试数据
// 					data=[{
// 					    label: 'one',
// 					    data: [{
// 					        value: 0,
// 					        display: 'a'
// 					    }, {
// 					        value: 1,
// 					        display: 'b'
// 					    }, {
// 					        value: 2,
// 					        display: 'c'
// 					    }, {
// 					        value: 3,
// 					        display: 'd'
// 					    }]
// 					},
// 					{
// 					    label: 'two',
// 					    data: [{
// 					        value: 0,
// 					        display: 'aa'
// 					    }, {
// 					        value: 10,
// 					        display: 'bb'
// 					    }, {
// 					        value: 20,
// 					        display: 'cc'
// 					    }, {
// 					        value: 30,
// 					        display: 'dd'
// 					    }]
// 					},
// 					{
// 					    label: 'three',
// 					    data: [{
// 					        value: 0,
// 					        display: 'aa'
// 					    }, {
// 					        value: 10,
// 					        display: 'bb'
// 					    }, {
// 					        value: 20,
// 					        display: 'cc'
// 					    }, {
// 					        value: 30,
// 					        display: 'dd'
// 					    }]
// 					}];


//控件对象
var ${selectId!}MobiscrollPlugin={
		datas:'${datas!}',//默认数据
		dataUrl:'${dataUrl!}',//请求数据的url
		selectId:'${selectId!}',
		hiddenId:'${hiddenId!}',
		level:'${level!}',
		lastSelectVal:'${defaultValue!}',
		display:'${display!}',
		showClear:'${showClear!}',
		inst:'',
		wheels:'',
		theme:'mobiscroll',
		showLabel: true,
		isShowQXZ: '${isShowQXZ!}',
		init:function(){
			//1.先看datas里面有没有值,没有值去ajax获取数据
			var that=this;
			this.wheels=this.getJsonData();
			//2.初始化控件
			var inst=$("#"+that.selectId).mobiscroll().scroller({
		        theme: that.theme, 
		        display: that.display,
		        showLabel: that.showLabel,
		        lang:'zh',  
		        circular:false,
		        buttons: that.getBtns(that.showClear),
		        wheels: that.wheels,
		        onInit: function (event, inst) {
		        	//设置默认值
		        	if(!(that.lastSelectVal instanceof  Array) && that.lastSelectVal!=''){
		        		that.lastSelectVal=that.lastSelectVal.split(",")
		        	}
		        	if((that.lastSelectVal instanceof  Array)){
		        		inst.setArrayVal(that.lastSelectVal);
		        	}
		        },
		        onChange: function (event, inst) {
		        	var valueAr=inst._tempWheelArray;
		        	var displayAr=that.getDisplay(valueAr);
		        	var changeWheelLevel=that.getWheelChangeLevel(valueAr);
		        	if(changeWheelLevel!=-1){
		        		that.changeWheels(changeWheelLevel,valueAr,inst);
		        	}
		        },
		        onSet: function (event, inst) {
		        	var valueAr=inst._tempWheelArray;
		        	var filterValueAr=new Array();
		        	for(var i=0;i<valueAr.length;i++){
		        		if(valueAr[i]!=''){
		        			filterValueAr.push(valueAr[i]);
		        		}
		        	}
		        	var displayAr=that.getDisplay(filterValueAr);
		        	//把value给value input
		        	//把display给label input
		        	$("#"+that.hiddenId).val(filterValueAr);
		        	$("#"+that.selectId).val(displayAr);
		        }
		    });
			this.inst=inst;
		},
		//获取wheel值对应的display值
		getDisplay:function (valueAr){
			var displayAr=new Array();
			for(var i=0;i<valueAr.length;i++){
				var wheelData=${selectId!}MobiscrollPlugin.wheels[0][i].data;
				for(var j=0;j<wheelData.length;j++){
					try{
						if(wheelData[j].value==valueAr[i] && valueAr[i]!=''){
							displayAr.push(wheelData[j].display);
							break;
						}
					}catch(e){
						console.log("错误"+wheelData.length+"--"+wheelData[j]+"--J="+j+"--dataL:"+wheelData)
					}
				}
			}
			return displayAr;
		},
		//获取第几个wheel的值改变了,如果父级的wheel值改变了,就要把子的wheel选项替换
		getWheelChangeLevel:function (SelectValue){
			//默认wheel改变.-1表示没改变
			var level=-1;
			for(var i=0;i<SelectValue.length;i++){
				if(SelectValue[i]!=this.lastSelectVal[i]){
					level=i;
					this.lastSelectVal=[];
					for(var j=0;j<SelectValue.length;j++){
						this.lastSelectVal[j]=SelectValue[j];
					}
					return level;
				}
			}
			return level;
		},
		//根据父级ID的改变,改变子集的wheel内容
		changeWheels:function (changeWheelLevel,valueAr,inst){
			//获取该level下的所有子wheel,并替换
			var parentId=valueAr[changeWheelLevel];
			var nowSelectedData=new Array();
			for(var i=0;i<=changeWheelLevel;i++){
				nowSelectedData.push(valueAr[i]);
			}
			//0 1 2 3最多4级
			for(var i=changeWheelLevel+1;i<valueAr.length;i++){
				var tempData=this.getJsonData(parentId,i);
				if(tempData.data.length==0){
					tempData.data=[];
				}else{
					parentId=tempData.data[0].value;
				}
				console.log("更新第几级:"+i+"-父ID:"+parentId);
				if(i==1){
					inst.changeWheel({
					    1: {
					        data: tempData.data
					    }
					});
				}else if (i==2){
					inst.changeWheel({
					    2: {
					        data: tempData.data
					    }
					});
				}else if (i==3){
					inst.changeWheel({
					    3: {
					        data: tempData.data
					    }
					});
				}
				//设置value
				if(tempData.data[0]){
					nowSelectedData.push(tempData.data[0].value);
				}
			}
			inst.setVal(nowSelectedData);
		},
		//通过ajax获取子数据
		getJsonData:function (parentId,level){
			var tempWheel;
			var that=this;
			//如果不传parentId,就是初始化数据,加载所有wheel的数据
			$.ajaxSetup({async: false});
			if(parentId == null){
				$.get(this.dataUrl,{ totalLevel:this.level,from:'mobiscroll'},function(data){
					if(that.isShowQXZ=="true"){
						for(var i=0;i<data.length;i++){
							var newData=new Array();
							newData.push({ value: "", display: '请选择' });
							newData=newData.concat(data[i].data);
							data[i].data=newData;
						}
					}
					tempWheel=[data];
				})
			}else{
				$.get(this.dataUrl,{ totalLevel:this.level,parentId:parentId,level:level,from:'mobiscroll'},function(data){
					if(that.isShowQXZ=="true"){
						var newData=new Array();
						newData.push({ value: "", display: '请选择' });
						newData=newData.concat(data.data);
						data.data=newData;
					}
					tempWheel=data;
				})
			}
			return tempWheel;
		},
		//获取按钮组
		getBtns:function (isShowClear){
			var that=this;
			var btnAr=new Array();
			btnAr.push('set');
			if(isShowClear){
				btnAr.push( { 
			          text: '清除',
			          handler: function (event, inst) { 
			        	  $("#"+that.hiddenId).val("");
				          $("#"+that.selectId).val("");
				          inst.cancel();
			          } 
			      });
			}
			btnAr.push('cancel');
			return btnAr;
		},
		//获取缓存数据 TODO
		getCacheData:function (parentId,level){
			return "";
		}
};

</script>
</#macro>