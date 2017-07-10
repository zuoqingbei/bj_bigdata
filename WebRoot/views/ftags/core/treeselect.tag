<#macro ztree id  name  labelName  title  url value=""  labelValue="" checked=false  extId=""  notAllowSelectRoot=false   notAllowSelectParent=false  allowClear=false  allowInput=false 
cssClass=""  cssStyle=""  nullmsg=""  disabled="" width="300" height="420"  isValueOfNameAndId=false  radio=false isMul="false" datatype="" errorMsg="" isHasBody=false callBackFun=""  isContainsDiv=true
>
<#-- 
标签说明:需要jquery/ztree/jbox插件才能用本标签.主要是用来进行树状数据的选择,如省市,多级部门等.
后台返回格式为：
//{
id:11,//元素的ID
name:北京市,//元素显示的名称
pId:0,//元素耳朵父级ID
isParent：true//是否是父元素，是的话就会根据url获取数据进行ajax加载子列表
//}

需要参数说明:
id:         (必填 )隐藏控件input的ID属性值
name:       (必填)隐藏控件input的name属性值
value:      (可选)隐藏控件input的value属性值
labelName:  (必填)显示控件input的name属性值
labelValue: (可选)显示控件input的value属性值
title:      (必填)弹出ztree页面的标题title
url:        (必填)树结构数据地址

isValueOfNameAndId:(可选) 隐藏控件input 的value值是否是id,name 默认是false,只显示id    用于单选的情况

datatype:       (可选)validform的验证条件
errorMsg:       (可选)validform的验证条件错误的信息
checked:    (可选)是否显示复选框，如果不需要返回父节点，请设置notAllowSelectParent为true,默认为false  
radio:      (可选)  是否显示单选框   默认是false
extId:      (可选)排除掉的树元素编号（不能选择的编号）
notAllowSelectRoot:    (可选)不允许选择根节点,默认为false
notAllowSelectParent:  (可选)不允许选择父节点,默认为false
allowClear: (可选)是否允许清除,默认为false
allowInput: (可选)是否允许文本框手动输入,默认为false
cssClass:   (可选)输入框样式css
isContainsDiv:(可选)  是否显示外部的div
cssStyle:   (可选)输入框样式style
datatype:   (可选)validateform的验证格式
nullmsg:    (可选)validateform的验证说明
disabled:   (可选)是否限制选择，如果限制，设置为disabled
width:      (可选)弹出框宽度,默认为300
height:     (可选)弹出框高度,默认为420 
isHasBody:  (可选)是否input按钮是自己写的
callBackFun:  (可选)选择或者完毕后的回调函数
isMul:医会通下拉是否是多选，默认单选false
 -->
<#if !isHasBody>
   <#if isContainsDiv>
     <div class="col-md-9">
   </#if>
	<input id="${id!}Id" name="${name!}" class="${cssClass!}" type="hidden" value="${value!}" readonly="readonly"/>
	<input id="${id!}Name" name="${labelName!}" type="text" value="${labelValue!}" placeholder="${title!} "
    <#if !allowInput>
      readonly="readonly"
    </#if>
	<#if datatype!="">
	  datatype="${datatype!}"
	  errorMsg="${errorMsg! }"
	</#if>
	 class="${cssClass!}" style="${cssStyle!}"/>
	 <#if isContainsDiv>
	 </div>
	 </#if>
</#if>
 
<script type="text/javascript">
//引入JS、CSS文件
importJSCSS("/script/jquery-jbox/2.3/Skins/GreyBlue/jbox.css");
importJSCSS("/script/jquery-jbox/2.3/jquery.jBox-2.3.min.js");
</script>

<script type="text/javascript">
if(('${base_domain!}'=='c.med626.com.cn'||'${base_domain!}'=='c.doctorpda.cn')
		&&('${url}'=='/bk/company2/treeData')){
	//医会通
	 $("#${id}Button, #${id}Name").click(yhtCompanySelect);
}else{
	 $("#${id}Button, #${id}Name").click(cmdaCompanySelect);
}
 //医师协会机构选择
 function  cmdaCompanySelect(){
	  // 是否限制选择，如果限制，设置为disabled
	  if ($("#${id}Button").hasClass("disabled")){
	   return true;
	  }
	  
	  var option={
		   ajaxData:{selectIds: $("#${id}Id").val()},buttons:{"确定":"ok",
			   <#if allowClear>
		   			"清除":"clear", 
		   	   </#if>
		   		"关闭":true}, 
		   		//v 按钮的值 ,	h JBOX的内容document 
		   		submit:function(v, h, f){
		   			
		    if (v=="ok"){
		     var tree = h.find("iframe")[0].contentWindow.tree;
		     //h.find("iframe").contents();
		     var ids = [], names = [], nodes = [];
		     
		     <#if checked>
		     nodes = tree.getCheckedNodes(true);
		     <#else>
		     nodes = tree.getSelectedNodes();
		     </#if>
		     
		     for(var i=0; i<nodes.length; i++) {
		    	
		    	 //单选框不能选择父节点
	    	   <#if radio && notAllowSelectParent>
			      if (nodes[i].isParent){
			       top.$.jBox.tip("不能选择父节点（"+nodes[i].name+"）请重新选择。");
			       return false;
			     } 
		    	</#if> 
		    	 
		    	 
		      <#if checked && notAllowSelectParent>
		      if (nodes[i].isParent){
		       continue; // 如果为复选框选择，则过滤掉父节点
		      }
		      </#if>
		      
		      <#if notAllowSelectRoot>
		      if (nodes[i].level == 0){
		       top.$.jBox.tip("不能选择根节点（"+nodes[i].name+"）请重新选择。");
		       return false;
		      }
		      </#if>
		      
		      <#if notAllowSelectParent>
		      if (nodes[i].isParent){
		       top.$.jBox.tip("不能选择父节点（"+nodes[i].name+"）请重新选择。");
		       return false;
		      }
		      </#if>
		      
		      ids.push(nodes[i].id);
		      names.push(nodes[i].name);
		      
		      <#if !checked> // 如果为非复选框选择，则返回第一个选择 
		      break; 
		      </#if>
		     }
		     <#if isValueOfNameAndId>
		     $("#${id}Id").val(ids[0]+","+names[0]);
		     <#else>
		     $("#${id}Id").val(ids.join(",").replace(/u_/ig,""));
		     </#if>
		    
		     $("#${id}Name").val(names.join(","));
		     <#if callBackFun?? && callBackFun!=''>
		     ${callBackFun}(ids.join(",").replace(/u_/ig,""),names.join(","));
		     </#if>
		    }
		    <#if allowClear>
		    else if (v=="clear"){
		     $("#${id}Id").val("");
		     $("#${id}Name").val("");
		     
		     <#if callBackFun?? && callBackFun!=''>
		     ${callBackFun}();
		     </#if>
		    }
		    </#if>
		    
		    if(typeof ${id}TreeselectCallBack == 'function'){
		     	${id}TreeselectCallBack(v, h, f);
		    }
		   }, 
		   loaded:function(h){
		    $(".jbox-content", top.document).css("overflow-y","hidden");
		   }
		  };
	  
	  try{
		  top.$.jBox.open("iframe:/common/a/ztreeContent?url="+encodeURIComponent("${url}")+"&module=${module!}&checked=${checked?string}&radio=${radio?string}&extId=${extId!}", "选择${title!}", ${width!}, ${height!}, option);
	  }catch(e){
		  $.jBox.open("iframe:/common/a/ztreeContent?url="+encodeURIComponent("${url}")+"&module=${module!}&checked=${checked?string}&radio=${radio?string}&extId=${extId!}", "选择${title!}", ${width!}, ${height!}, option);
	  }
	  
	}
 //医会通机构选择
 function yhtCompanySelect(){
	var ids=$("#${id!}Id").val();
	var names=$("#${id!}Name").val();
	var url='${base_path!}/bk/companyYht/companySelector?isMulti=${isMul!}&inputId=${id!}Id&nameInputId=${id!}Name&ids='+ids+"&names="+names;
	layer.open({
		  type: 2,
		  area: ['1000px', '550px'],
		  content: url,
		  title: "机构选择",
		  maxmin :true,
		  btn: ['确认', 
		        <#if allowClear>
		 			"清除", 
		 	   </#if>
		        '取消'],
		  yes: function(index, layero){
			  var body = layer.getChildFrame('body', index);
			  var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
			  iframeWin.submitMethod();
			  
			  <#if callBackFun?? && callBackFun!=''>
			     ${callBackFun}();
			  </#if>
			  <#if allowClear>
				  $(".layui-layer-btn2").trigger("click");
			  <#else>
			      $(".layui-layer-btn1").trigger("click");
		   	  </#if>
		  }
	<#if allowClear>
	,
		  btn2: function(index, layero){
			  $("#${id!}Id").val('');
			  $("#${id!}Name").val('');
		  }
	</#if>
	}); 
}
</script>

</#macro>