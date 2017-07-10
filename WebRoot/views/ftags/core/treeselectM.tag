<#macro treeselectM  selectId  level datas="" dataUrl="" changeCallBack="" defaultValue="" isShowQXZ="false"   placeholder="请选择" labels="" inputClass="" showClear="true" >
 <!-- <input type="hidden" name="level1" id="listTreelist2-demo-level1">
        <input type="hidden" name="level2" id="listTreelist2-demo-level2">
        <input type="hidden" name="level3" id="listTreelist2-demo-level3">
        <input type="hidden" name="level1" id="listTreelist2-demo-level1-Name">
        <input type="hidden" name="level2" id="listTreelist2-demo-level2-Name">
        <input type="hidden" name="level3" id="listTreelist2-demo-level3-Name"> -->
<#-- 
1.插件说明：手机的mobiSrcoll 的tree选择控件,会往下面的hidden注入相应的level值

2.需要参数说明:
selectId:    (必填)，ul控件的ID
datas：                    (选填)，控件的选择数据jsonArray格式如:
level：                    (必填)，树控件有几层
          [
          {name:'中国',id:'cn',child:[{name:'山东',id:'sd',pId:'cn'}]},
          {name:'美国',id:'us',child:[{name:'纽约',id:'newyock',pId:'us'}]},
          ]
dataUrl:    (选填)，如果不传datas可通过url请求ajax获取数据,返回数据格式如上
placeholder (选填)，提示字
isShowQXZ:     (选填)，是否给数据添加请选择数据
labels:     (选填)，选择类型提示字
inputClass  (选填)，选择框样式
showClear   (选填)，是否显示清除按钮,默认选择
 -->
 
<ul id="${selectId!}" style="display:none">
</ul> 

<script type="text/javascript">
//引入JS、CSS文件
importJSCSS("/script/Jquery-Mobiscroll/css/mobiscroll.custom-3.0.0-beta4.min.css");
importJSCSS("/script/Jquery-Mobiscroll/js/mobiscroll.custom-3.0.0-beta4.js");

$(function(){
 var labelArray=[];
 var dataUrl='${dataUrl!}';
 if('${labels}'!=''){
	 labelArray='${labels}'.split(",");
 }
 var jsonList;
 if('${datas}'!=''){
	 jsonList=eval('${datas}');
 }else{
	 jsonList=getJsonData(dataUrl);
 }
 var btns=['set','cancel'];
 if('${showClear}'=='true'){
	 btns=['set','cancel','clear'];
 }
 var defaultValue;
 if('${defaultValue!}'&&'${defaultValue!}'!=''&&'${defaultValue!}'!='null'){
	 defaultValue='${defaultValue!}'.split(",");
 }
 ${selectId!}initMobiscroll('${selectId}','${placeholder!}',labelArray,jsonList,'${inputClass!}',btns,'${level!}',dataUrl,defaultValue);
})



/**
 * 初始化Mobiscroll树控件
 * @param id  html的对象
 * @param obj    html对象,把树结构放到哪里 
 */
function ${selectId!}initMobiscroll(id,placeholder,labels,jsonList,inputClass,btns,level,url,defaultValue){
//  1.初始化ul的树结构 
 ${selectId!}initUlTree(jsonList,$("#"+id),1);
 
 var option={
	     theme: "mobiscroll",      // Specify theme like: theme: 'ios' or omit setting to use default
	     lang: "zh",    // Specify language like: lang: 'pl' or omit setting to use default
	     display: "center",         // Specify display mode like: display: 'bottom' or omit setting to use default
	     mode: "list",                   // More info about mode: https://docs.mobiscroll.com/3-0-0_beta4/list#!opt-mode
//	      width: [90, 160, 170],                    // More info about width: https://docs.mobiscroll.com/3-0-0_beta4/list#!opt-width
	     inputClass:inputClass, 
	     circular:false,
	     buttons:btns, 
	     clearText:'清除',
	     placeholder: placeholder,         // More info about placeholder: https://docs.mobiscroll.com/3-0-0_beta4/list#!opt-placeholder
	     showInput: true,         // More info about placeholder: https://docs.mobiscroll.com/3-0-0_beta4/list#!opt-placeholder
	     labels: labels,  // More info about labels: https://docs.mobiscroll.com/3-0-0_beta4/list#!opt-labels
	     onItemTap:function(event,inst){
// 	       console.log($(event.target).attr("data-val"));
	     },
	     onChange:function(event,inst){
// 	         var selectLi=${selectId!}findSelectLiAndSetValue(event.valueText, id);
// 	         var $selectLi=$(selectLi);
// // 	         获取最后一级的值,如果是最后一级或已经加载了,就不加载数据
// 	         if($selectLi.attr("level")<level&&$selectLi.find("ul").length==0){
// 	        	 console.log(event.valueText);
// 	        	 var jsonList=getJsonData(url,{parentid:$selectLi.attr("value")});
// 	        	 var newUl=$("<ul></ul>");
// 	        	 ${selectId!}initUlTree(jsonList, newUl, $selectLi.attr("level")+1);
// 	        	 $selectLi.append(newUl);
// 	        	 inst.init();
// 	         }
	     },
	     onClose: function (event, inst) {
	    	 $("#"+id+"_dummy").val(event.valueText.trim());
	     },
	     onBeforeClose:function(event,inst){
	      var button=event.button;
	      var valueText=event.valueText;
	      //如果是确定 取消 清除
	      if(button=="set"){
	    	  ${selectId!}findSelectLiAndSetValue(valueText,id);
	      }else if(button=="cancel"){
	       
	      }else if(button=="clear"){
	       $("#"+id+"-level1").val("");
	       $("#"+id+"-level2").val("");
	       $("#"+id+"-level3").val("");
	      }
	      <#if changeCallBack?? &&changeCallBack!=''>
	      	${changeCallBack!}(event.valueText);
	      </#if>
	     }
	 };
 if(defaultValue){
	 option.defaultValue=defaultValue;
 }else{
	 option.defaultValue=null;
 }
//  2.Mobiscroll Treelist initialization
 var listTreelist2 = mobiscroll.treelist('#'+id, option);
 if(listTreelist2.getArrayVal()!=' '&&listTreelist2.getArrayVal()){
	 var arrayValue=listTreelist2.getArrayVal();
	 for(i=0;i<arrayValue.length;i++){
		 if(arrayValue[i]&&arrayValue[i]!=' '){
			 $("#"+id+"_dummy").val($("#"+id+"_dummy").val()+arrayValue[i]+" ");
		 }
	 }
 }else{
	 $("#"+id+"_dummy").val("");
 }
 
}

function prepend(arr, item) {
    return [item].concat(arr);

}

/**
 * 初始化ul的树结构 
 * @param jsonList json数组,里面的属性有 name-id-parent_id-child
 * @param obj	   html对象,把树结构放到哪里 
 * @param level	         第几级数据,开始第一级,以后递归
 */
function ${selectId!}initUlTree(jsonList,obj,level){
	if(!jsonList){
		return;
	}
	if('${isShowQXZ!}'=="true"){
		var qingxuanze={NAME:"请选择",ID:""};
		jsonList=prepend(jsonList, qingxuanze)
	}
//  li -->ul-->li -->ul
    for(var i=0; i<jsonList.length; i++){  
//           console.log(jsonList[i].NAME+"--" + jsonList[i].id+"--"+jsonList[i].pId+"--"+jsonList[i].child);
          var $oneObj=$("<li></li>");
          var name=jsonList[i].name||jsonList[i].NAME;
          $oneObj.html(jsonList[i].name||jsonList[i].NAME);
          if(name!="请选择"){
        	  $oneObj.attr("data-val",name);
          }else{
        	  $oneObj.attr("data-val"," ");
          }
          $oneObj.attr("value",jsonList[i].id||jsonList[i].ID);
          $oneObj.attr("id",jsonList[i].id||jsonList[i].ID);
          $oneObj.attr("pId",jsonList[i].pId||jsonList[i].PID||jsonList[i].parent_id||jsonList[i].PARENT_ID);
          $oneObj.attr("level",level);
          var child=jsonList[i].child||jsonList[i].CHILD;
          if(child&&child.length>0){
          		var $childUl=$("<ul></ul>");
          		${selectId!}initUlTree(jsonList[i].child||jsonList[i].CHILD,$childUl,level+1);
          		$oneObj.append($childUl);
          }
          obj.append($oneObj);
    }  
}

/**
 * 获得选择的li 并把相应级别的li复制到hidden 值上去,并返回最后一级的 对象
 * @param valueText 控件的选择name值
 * @param objId	   html控件对象
 */
function ${selectId!}findSelectLiAndSetValue(valueText,objId){
	var textArray=valueText.split(" ");
	//把value,拆分成level放到对应的 id-level 1 2 3中去
	var parent=$("#"+objId);
	for(var i=0; i<textArray.length; i++){  
		var name=textArray[i];
		var level=parent.find("li[data-val='"+name+"']")[0];
		$("#"+objId+"-level"+(i+1)).val($(level).attr("value"));
		if(name!="请选择"){
			 $("#"+objId+"-level"+(i+1)+"-Name").val(name);
        }else{
        	 $("#"+objId+"-level"+(i+1)+"-Name").val("");
        }
		$("#"+objId+"-level"+(i+1)+"-Name").val(name);
		parent=$(level);
		if(i==(textArray.length-1)){
			return $(level);
		}
	}
}

//获取数据库数据
function getJsonData(url,params){
	var jsonData;
	$.ajaxSetup({async: false});
	$.get(url,params,function(data){
		jsonData=data.treeDatas||data.TREEDATAS;
	})
	return jsonData;
}

</script>
</#macro>