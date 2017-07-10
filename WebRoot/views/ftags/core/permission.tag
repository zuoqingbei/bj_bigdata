<#macro permission  domId=""  type=1 name="">
<#-- 
需要参数说明
docId:		必填，要控制的Dom对象id 或者类名（必须唯一）
type: 必填，后台类型 1超级后台  3会议后台  4现场服务后台 目前支持1 3
name:必填，权限名称 如果不填默认可以看
引用
  <#include "/views/ftags/core/permission.tag" /> 
		     <@permission domId="ccc" type=3 name="admin:test">
		      <div id="userStatfff" class="ccc" >123</div>
		    </@permission>
 -->
	<#nested> 

<script type="text/javascript">
	//所以这里要更具chartType扩展 initChart方法，每个chartType 一个初始化表格的方法
	$(document).ready(function() {
		permission${domId!}Plugin.getPermission();
	});
	var permission${domId!}Plugin={
			//1.0 初始化设置
			initSetting:function(){
				var setting={
						domId:"${domId}",//document ID
						url:"/common/a/getAccountMenu?type=${type}",
						type:"${type}",//后台类型
						name:"${name}"
					}
				return setting;
			},
			
			getPermission:function(){
				if(this.getData()){
					$("."+this.initSetting().domId).css("display","block");
					$("#"+this.initSetting().domId).css("display","block");
				}else{
					$("."+this.initSetting().domId).remove();
					$("#"+this.initSetting().domId).remove();
				}
			},
			//从后台获取数据,返回keys 和values的map，根据表格类型不同，可能有变化
			getData:function (){
				if(this.initSetting().name==""||this.initSetting().name==null){
					return true;
				}else{
					var hasPermission=false;
					$.ajaxSettings.async = false;
					$.getJSON(this.initSetting().url, function(data) {
						for (var i = 0; i < data.menus.length; i++) {
							if(data.menus[i].PERMISSION!=null&&data.menus[i].PERMISSION==permission${domId!}Plugin.initSetting().name){
								hasPermission=true;
								break;
							}
						}
					});
					return hasPermission;
				}
			},
			
	}
	
</script>
</#macro>