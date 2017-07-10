<#macro fileUpload     hiddenPathsId  hiddenPathsName  hiddenPathsVal=""  hiddenNamesId=""    hiddenNamesName=""   hiddenNamesVal="" hiddenContentFileId=""    hiddenContentFileName=""   hiddenContentFileVal="" 
isMuti=false  fileTypeLimit="other" fileSizeLimit="500" fileCountLimit="20" fileCustomDir=""  datatype="" errorMsg=""  tip="" fileClass="" successFuc="" deleteFuc=""  isSaveCmsContentFile=false isFileByUrl=false otherParams="">
<#-- 
标签描述:例子:/views/modules/test/testFileUpload3.html
         对文件进行单个上传,多个上传,可以加 文件类型,大小,数量JS验证.全部采用/common/file2/upload 接口传送文件
必须JS/css:
/css/fileUpload/fileUpload.css
/static/global/plugins/font-awesome/css/font-awesome.min.css
/static/global/plugins/bootstrap/css/bootstrap.min.css
/static/global/plugins/jquery-file-upload/js/vendor/jquery.ui.widget.js
/static/global/plugins/jquery-file-upload/js/jquery.fileupload.js
/static/global/plugins/jquery-file-upload/js/jquery.fileupload-process.js
/script/fileUpload/fileUploadUtils.js
 需要参数说明: 
 
hiddenPathsId:  (必填 )文件上传后的路径保存的隐藏input的ID;
hiddenPathsName:(必填 )文件上传后的路径保存的隐藏input的Name;
hiddenPathsVal: (可选 )文件上传后的名称保存的隐藏input的值;

hiddenNamesId:  (可选 )文件上传后的名称保存的隐藏input的ID;不填就在hiddenPathsId后面加个_name
hiddenNamesName:(可选 )文件上传后的名称保存的隐藏input的Name;
hiddenNamesVal: (可选 )文件上传后的名称保存的隐藏input的值;

hiddenContentFileId:  (可选 )cms_content_file表的ID;不填就在hiddenPathsId后面加个_ContentFile
hiddenContentFileName:(可选 )cms_content_file表input的Name;
hiddenContentFileVal: (可选 )cms_content_file表input的值;

isMuti:         (必填 )是否多文件上传
fileTypeLimit:  (可选 )文件类型限制,多个以逗号隔开 images;video;doc;excel;ppt;voice;other
fileSizeLimit:  (可选)不填写,默认使用服务器大小限制
fileCountLimit: (可选)文件数量限制
fileCustomDir:  (可选)文件保存的路径
datatype:       (可选)validform的验证条件
errorMsg:       (可选)validform的验证条件错误的信息
fileClass		(可选)选择器的样式
successFuc		(可选)上传成功后回调掉事件,传入上传成功的 ExUploadFile 对象
deleteFuc		(可选)删除成功后回调掉事件,传入上传成功的 ExUploadFile 对象hiddenPathsId,hiddenNamesId
isSaveCmsContentFile (可选)是否保存到cmsContentFile表
isFileByUrl     (可选)是否添加超链接上传文件
otherParams     (可选)额外参数
 -->
 
<#if  hiddenNamesId==''>
<#local hiddenNamesId=hiddenPathsId+"_name" >
</#if> 

<#if  hiddenNamesName==''>
<#local hiddenNamesName=hiddenPathsName+"_name">
</#if> 

<#if  hiddenContentFileId==''>
<#local hiddenContentFileId=hiddenPathsId+"_ContentFile">
</#if> 

<#if  hiddenContentFileName==''>
<#local hiddenContentFileName=hiddenPathsId+"_ContentFileId">
</#if> 
        
<input type="hidden" name="${hiddenPathsName!}" id="${hiddenPathsId!}"  
<#if datatype!="">
datatype="${datatype!}"
errorMsg="${errorMsg!}"
</#if>
>
<input type="hidden" name="${hiddenNamesName!}" id="${hiddenNamesId!}"   >
<input type="hidden" name="${hiddenContentFileName!}" id="${hiddenContentFileId!}"   >

<input  class="form-control input-inline input-large ${fileClass!}" style="display: inline-block;" <#if isMuti>multiple="multiple"</#if> type="file"  id="${hiddenNamesId!}File"  >
<#if isFileByUrl>
<input  class="form-control input-inline input-large ${fileClass!}" placeholder="复制附件超链接也可添加" style="display: inline-block;" type="text"  id="${hiddenNamesId!}Input"  onblur="${hiddenPathsId!}Plugin.addFileByUrl(this.value,this)"  />
</#if>
<span class="Validform_checktip  help-inline" >${tip!}</span>
<div class="file-content" id="${hiddenNamesId!}File-content" >
</div> 

<script type="text/javascript">
//引入JS、CSS文件
importJSCSS("/css/fileUpload/fileUpload.css");
importJSCSS("/static/global/plugins/font-awesome/css/font-awesome.min.css");
importJSCSS("/static/global/plugins/bootstrap/css/bootstrap.min.css");
importJSCSS("/static/global/plugins/jquery-file-upload/js/vendor/jquery.ui.widget.js");
importJSCSS("/static/global/plugins/jquery-file-upload/js/jquery.fileupload.js");
importJSCSS("/static/global/plugins/jquery-file-upload/js/jquery.fileupload-process.js");
importJSCSS("/script/fileUpload/fileUploadUtils.js");
</script>

<script type="text/javascript">
$(function(){
	${hiddenPathsId!}Plugin.initUpload();
	${hiddenPathsId!}Plugin.inintExistData();
})

//fileupload插件对象
var ${hiddenPathsId!}Plugin={
		//配置信息
		fileTypeLimit:"${fileTypeLimit!}",//多个以逗号隔开 images;video;doc;excel;ppt;voice;other
		fileSizeLimit:"${fileSizeLimit!}",//文件大小限制,单位M
		fileCountLimit:<#if !isMuti>1<#else>"${fileCountLimit!}"</#if>,//文件数量限制
		
		//页面htmlID
		hiddenPathsId:"${hiddenPathsId!}",
		hiddenPathsVal:"${hiddenPathsVal!}",
		hiddenNamesId:"${hiddenNamesId!}",
		hiddenNamesVal:"${hiddenNamesVal!}",
		hiddenContentFileId:"${hiddenContentFileId!}",
		hiddenContentFileVal:"${hiddenContentFileVal!}",
		inputFileId:"${hiddenNamesId!}File",
		
		otherParams:'${otherParams!}',//额外参数
		
		//通过file文件上传文件地址
		url:'/common/file2/upload?type=${fileTypeLimit!}&limitSize=${fileSizeLimit!}&isSaveCmsContentFile=${isSaveCmsContentFile?string}&fileCustomDir=${fileCustomDir!}',
		//通过path字符串上传文件地址
		url2:'/common/file2/uploadByPath?type=${fileTypeLimit!}&limitSize=${fileSizeLimit!}&isSaveCmsContentFile=${isSaveCmsContentFile?string}&fileCustomDir=${fileCustomDir!}',
				
		initUpload:function(){
			var that=this;
			//上传
			$('#'+this.inputFileId).fileupload({	
			 	url: this.url+"&"+this.otherParams,
			 	dataType:'json',
// 			 	formData:{param1:"p1"},
			  	add: function (e, data) {
			  		var $this = $(this);
			        //1.进行文件合法性检验
			        var msg="";
			        for(var x in data.files){
			        	msg+=file_check_file_legal(data.files[x], that.fileTypeLimit, that.fileSizeLimit,that.fileCountLimit,that.inputFileId);
			        }
			        if(msg!=""){
			        	$.Showmsg(msg);
			        	return false;
			        }
			  		//2.把文件列表生成html中
			 		//3.把生成的html列表放入data.context中去
			  		//4.绑定上传事件,取消事件,删除事件
			        data=file_createFileDiv(data,that.inputFileId);
			        data.context = data;
// 			        data.formData = { relativePath: "/attach", relativePath: "/attach", relativePath: "/attach"};//测试放参数
			        data.process(function () {
			            return $this.fileupload('process', data);
			        })
			        //5.进行上传
			        var jqXHR= data.submit();
			     },
			 	done:function(e, data){
			 		var json = $.parseJSON(data.jqXHR.responseText);
			 		if(json.status=="success"){			
			 			//返回ExUploadFile json数组
			 			files=json.files;
			 			for (var x in files){
			 				//把值放入hidden 
			 				addFileDataToHidden(files[x].path,files[x].originalFileName,files[x].cms_content_file_id
			 						,that.hiddenPathsId,that.hiddenNamesId,that.hiddenContentFileId)
			 				
			 				//额外回调函数
			 				<#if successFuc!=''>
			 				${successFuc!}(files[x]);
			 				</#if>
			 			}
			 			//删除按钮，取消按钮事件
			 			var filePath=files[0].path;
			 			var onefileJson=files[0];
			 			data.progressHtml.children().html('上传完成');
			 			setTimeout(function(){
			 				data.progressHtml.remove();
			 			},1000);
			 			data.btnCancel.remove();
			 			data.btnDelete.on('click',function(){
			 				file_delete_file_path(filePath,that.hiddenPathsId,that.hiddenNamesId,that.hiddenContentFileId);
			 				<#if deleteFuc!=''>
			 				${deleteFuc!}(onefileJson,that.hiddenPathsId,that.hiddenNamesId,that.hiddenContentFileId);
			 				</#if>
			 			})
			 			var btnDownLoad=$("<input type='button'  value='下载' onclick=\"window.open('"+filePath+"')\" target=\"_blank\">");
			 			btnDownLoad.appendTo(data.file_one_options);
			 		}else{
			 			$.Showmsg(json.msg);
			 		}
			 	},
			    progress: function (e, data) {
			        var progress = parseInt(data.loaded / data.total * 100, 10);
			        //百分比
			 		data.progressHtml.children().css( 'width',progress + '%');
			 		//上传网速
			 		data.progressHtml.children().html(common_file_formatBitrate(data.bitrate));
			 		if(progress>=100){
			 			data.progressHtml.children().html("正在云上传..");
			 		}
			     }
			 });
		},
		inintExistData:function(){
  		 //根据已有的上传文件生成DIV
  		 if(this.hiddenPathsVal){
  			 var hiddenPathsValArray=this.hiddenPathsVal.split(",");
  			 var hiddenNamesValArray=this.hiddenNamesVal.split(",");
  			 var hiddenContentFileValArray=this.hiddenContentFileVal.split(",");
  			 for(var i=0;i<hiddenPathsValArray.length;i++){
  				 if(hiddenNamesValArray[i]){
  					 file_createExistFileDiv(hiddenPathsValArray[i],hiddenNamesValArray[i],hiddenContentFileValArray[i],this.inputFileId,this.hiddenPathsId,this.hiddenNamesId,this.hiddenContentFileId);
  				 }else{
  					 file_createExistFileDiv(hiddenPathsValArray[i],hiddenPathsValArray[i],hiddenContentFileValArray[i],this.inputFileId,this.hiddenPathsId,this.hiddenNamesId,this.hiddenContentFileId);
  				 }
  			 }
  		 }
		},
 		//通过文件超链接添加附件
 		addFileByUrl:function(webPath,obj){
 			 var that=this; 
 			 //1.获取超链接值,传进来的
 			 //2.进行合法性验证,文件格式
 			 if(!webPath){
				 return;
			 }
 			 var file={name:webPath,size:1};
 			 var msg=file_check_file_legal(file, this.fileTypeLimit, this.fileSizeLimit,this.fileCountLimit,this.inputFileId);
 			 if(msg!=""){
 				$.Showmsg(json.msg);
 				return;
 			 }
 			 //3.进行上传
 			 $.post(this.url2+"&"+this.otherParams,{url:webPath},function(data){
 				//4.添加到div中去
 				var files=data.files;
			 	for (var x in files){
			 		var filePath=files[x].path;
			 		var fileName=files[x].fileName;
			 		var cms_content_file_id=files[x].cms_content_file_id;
			 		file_createExistFileDiv(filePath,fileName,cms_content_file_id,that.inputFileId,that.hiddenPathsId,that.hiddenNamesId,that.hiddenContentFileId);
			 	}
			 	//5.完成清除值
			 	$(obj).val("");
 			 })
 		}
};

</script>
</#macro>