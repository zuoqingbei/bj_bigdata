<#macro imgUpload pluginId  id  name value=""   isShowDelete=true defaultImg="" width="100" height="100" tip="" datatype="">
<#-- 
标签描述:
1.先生存图片列表,有几个循环几个, 有默认图,一个图一个hidden一个file,名字为id+1,id+2
2.给图片列表附上图片和hidden的path
3.点击图片上传,鼠标放上去有清除按钮,清除图片和hidden的path

 需要参数说明: 
pluginId		(必填 )插件ID
id:  			(必填 ,多个以逗号隔开)每个图片input=hidden的id,其他元素的ID为,id+file,id+img,id+deleteBtn;
name:			(必填 ,多个以逗号隔开 )每个图片input=hidden的name,其他元素的name为,id+file,id+img,id+deleteBtn
value: 			(可选 ,多个以逗号隔开 )每个图片input=hidden的值;
isShowDelete:	(可选 )是否有删除按钮;
defaultImg:		(可选 )默认图片;
width:			(可选 )图片宽度;
height:			(可选 )图片高度;
tip:			(可选 )提示文字;
datatype:		(可选 )验证类型;
 -->

<#--图片列表,有几个循环几个,  -->
<div class="imgPlg-div ${pluginId!}imgPlg-div">
<#list id?split(",") as one_id>
	<#assign nameAr=name?split(",") />
	<#assign valueAr=value?split(",") />
	<#assign defaultImgAr=defaultImg?split(",") />
	<#assign imgsrc=valueAr[one_id_index]! />
	<#assign oneDefaultImg=defaultImgAr[one_id_index]! />
	<#if (!oneDefaultImg??) || oneDefaultImg=='' >
		<#assign oneDefaultImg='/images/common/default_img4.png' />
	</#if>
	<#if (!imgsrc??) || imgsrc=='' >
		<#assign imgsrc=oneDefaultImg! />
	</#if>
	
	<div class="imgPlg-one-div " style="width: ${width!}px;"  id="${one_id!}_div">
	
		<div class="imgPlg-one-shade" id="${one_id!}_shade" style="width: 100%;height: ${height!}px;">
			<div>
				<h3>+</h3>
				<p>点击上传图片</p>
			</div>
		</div>
		<img alt="" src="${imgsrc!}" defaultImg="${oneDefaultImg!}" width="${width!}" height="${height!}" id="${one_id!}_img" name="${nameAr[one_id_index]!}_img" class="imgPlg-one-img">
		
		<input type="hidden" id="${one_id!}" name="${nameAr[one_id_index]!}" value="${valueAr[one_id_index]!}" <#if datatype?? && datatype!=''> datatype="${datatype!}" </#if> />
		<input type="file" id="${one_id!}_file" name="${nameAr[one_id_index]!}_file" class="hidden imgPlg-one-file" />
		<br>
		<button type="button"  id="${one_id!}_deleteBtn" name="${nameAr[one_id_index]!}_deleteBtn" onclick="" class="btn  default btn-sm  hidden imgPlg-one-deleteBtn">删除</button>
		<span class=" Validform_checktip clear" style="white-space: pre-wrap;">${tip! }</span>
	</div>
	
</#list>
</div>


<script type="text/javascript">
//引入JS、CSS文件
importJSCSS("/css/tagPlugin/imgUpload.css");
</script>
<script type="text/javascript">
$(function(){
	${pluginId!}Plugin.init();
})

//fileupload插件对象
var ${pluginId!}Plugin={
	init:function(){
		//1.imgPlg-one-img点击事件
		$(".${pluginId!}imgPlg-div .imgPlg-one-img,.${pluginId!}imgPlg-div .imgPlg-one-shade").on("click",function(e){
			var id=${pluginId!}Plugin.getInputId(this);
			$("#"+id+"_file").click();
		})
		//2.imgPlg-one-img的mouseover事件
		$(".${pluginId!}imgPlg-div .imgPlg-one-div").on("mouseover",function(e){
			var id=${pluginId!}Plugin.getInputId(this);
			$("#"+id+"_shade").show();
			if($("#"+id).val()||$("#"+id+"_file").val()){
				$("#"+id+"_deleteBtn").removeClass("hidden");
			}
		})
		$(".${pluginId!}imgPlg-div .imgPlg-one-div").on("mouseout",function(e){
			var id=${pluginId!}Plugin.getInputId(this);
			$("#"+id+"_shade").hide();
			$("#"+id+"_deleteBtn").addClass("hidden");
		})
		//3.button删除事件
		$(".${pluginId!}imgPlg-div .imgPlg-one-deleteBtn").on("click",function(e){
			${pluginId!}Plugin.deleteImg(${pluginId!}Plugin.getInputId(this));
		})
		//4.file选择事件
		$(".${pluginId!}imgPlg-div .imgPlg-one-file").on("change",function(e){
			var id=${pluginId!}Plugin.getInputId(this);
			common_fileToImg(this,id+'_img');
		})
	},
	deleteImg:function(id){
		//清除图片
		$("#"+id+"_img").attr("src",$("#"+id+"_img").attr("defaultImg"));
		//清除hidden
		$("#"+id).val("");
	},
	getInputId:function(obj){
		var id=$(obj).attr("id");
		id=id.substring(0,id.lastIndexOf('_'));
		return id;
	}
};
</script>
</#macro>