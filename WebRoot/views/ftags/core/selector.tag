<#macro selector   dataUrl  addUrl  inputId  nameInputId param="" controlId="" width="600" height="450" coloums="名称" title="选择页面" isMulti="false" callBackFun="">
<#-- 
说明：以弹出和分页的形式，对要选择的信息进行展示选取，可以多选单选。
需要参数说明
dataUrl:    必须，请求表格数据的URL,不用表格的数据可能要求的格式不同，具体格式见，getData方法
inputId：           必须，input控件ID
nameInputId 可选，nameInput的ID,不填写的话,默认在inputId之前加上一个name前缀
controlId:  可选,弹出控件的元素ID,没写的话默认nameInputId
coloums:    必须,表格的列名称
addUrl:     可选，添加实体的url
isMulti:    可选，是否是多选
title       可选,弹出框的标题
width       可选,页面宽度
height      可选,页面高度
callBackFun 可选,选择完成回调函数
param:参数 可选 请求数据的筛选条件；传入控件id，多个用逗号拼接
 -->
<script type="text/javascript">
 var ${controlId!}addUrl;
$(function(){
	//如果controlId没有 就默认 nameInputId
	var controlId='${controlId!}';
	if(!controlId){
		controlId="${nameInputId!('name'+inputId)}";
	}
	${controlId!}addUrl='${addUrl}';
	$("#"+controlId).on("click",${controlId!}selector_click);
})
//适合用于级联操作 当需要在弹出选择之前进行校验的，可以在校验之后传入新的添加url：finalAddUrl 对原有没影响 
//使用：去掉“选择”按钮上的id，绑定自己的校验逻辑方法，写一个隐藏button 假设id为authorBtn_check
//校验之后var addUrl="parent.window.location.href=${base_path!}/admin/conuser/form/"+参数;
//$("#authorBtn_check").unbind().one("click",authorBtnselector_click(addUrl));
function ${controlId!}selector_click(finalAddUrl){
		var ids=$("#${inputId!}").val(); 
		var names=$("#${nameInputId!('name'+inputId)}").val(); 
		var addurl=encodeURIComponent('${addUrl!}');
		var param='${param!}'.split(",");
		var paramStr="";
		for(var x=0;x<param.length;x++){
			if(param[x]!=""){
				paramStr+=param[x]+":"+$("#"+param[x]).val()+",";
			}
		}
		if(paramStr!=""){
			paramStr=paramStr.substr(0,paramStr.length-1);
		}
		if(typeof finalAddUrl!="string"){
			finalAddUrl=${controlId!}addUrl;
		}
		var url="?dataUrl=${dataUrl!}&addUrl="+finalAddUrl+"&ids="+ids+"&names="+names+"&inputId=${inputId!}&nameInputId=${nameInputId!!('name'+inputId)}";
		url+="&isMulti=${isMulti!}&title=${title!}&coloums=${coloums!}&callBackFun=${callBackFun!}&param="+paramStr;
		showPopWin("/common/a/selector"+url,${width!},${height!},'${title!}');
	}
</script>
</#macro>