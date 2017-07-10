<#macro captcha type="image"    width="200" inputClass="">
<#-- 
说明：网易易盾验证码服务
需要参数说明
type:验证码类型 image-图片验证码 slide-滑动验证. 默认图文验证
width：     验证码组件显示宽度
 -->
 <body> 
 		<#if type!='slide'>
	   		<div style="margin-left:16px;">
	          <input id="validateCode"  class="${inputClass }" type="text" name="validateCode" onblur="checkvalidateCode(this.value)" value=""> 
	          <img   id="imageCodeImg" src="/validateCode" title="请输入验证码" style="display: inline-block; height: 30px; width: 80px; vertical-align: middle;" onclick="this.src='/validateCode?r=' + Math.random();" /> 
	          <span class="Validform_checktip" style=""></span>
	         </div>
 		<#else>
 			<div id="captcha_div" ></div>
 			<input type='hidden' name='openyunduncaptcha' value='true'/>
 		</#if>
 </body>
<script type='text/javascript' src='/script/commonJs/mobile_vc.js'></script>
<link rel="stylesheet" type="text/css" href="/script/validform/style.css"  /> 
<link rel="stylesheet" type="text/css" href="/script/validform/plugin/pwdstrength/style.css" />

<script type='text/javascript' src='/script/validform/Validform_v5.3.2_min.js'></script>
<script src="http://c.dun.163yun.com/js/c.js"></script>
<script> 
if('${type}'=="slide"){
	// 验证码组件初始化
      var opts = {
        "element": 'captcha_div', // 可以是验证码容器id，也可以是HTMLElement
        "captchaId": "5feffee1e9ea4b3dae507da4962be729", // 这里填入申请到的验证码id
        "width": '${width!}', // 验证码组件显示宽度
        "verifyCallback": function(ret){ // 用户只要有拖动/点击，就会触发这个回调
          if(ret['value']){ // true:验证通过 false:验证失败
            // 通过 ret["validate"] 可以获得二次校验数据
            // $("#${submitBtn!}").removeAttr("disabled"); // 用户完成拖动之后再启用提交按钮
            
            //对滑动验证码就行校验，校验成功之后又两个关键操作 1将canSendMoblie设置为true，可以进行手机发送短信的操作
            //2 将验证码放入session SESSION_SLIDE_VALIDATECODE中，这样在进行发送短信之前，可以再次校验下验证码是否超时
            $("#hidden_code").val(ins.getValidate());
            $("#sub_valid_btn").click();
          }else{
        	  //刷新验证码
        	  ins.refresh();
          }
        }
      }
      var ins=new NECaptcha(opts);
      setCssStyle();
     //生产隐藏表单  用于滑动验证码验证 只能通过表单提交 不支持Ajax
      var forms='<form action="/checkValidateCode" method="post" id="valid_form" style="display:none">';
      forms+="<input type='hidden' name='validate_type' value='${type}'/>";
      forms+="<input type='hidden' name='validateCode' id='hidden_code' value=''/><input type='submit'  id='sub_valid_btn'></form>";
      $("body").append(forms);
      $("#valid_form").Validform({
    		ajaxPost : true,
    		tiptype : 4,
    		callback : function(data) {
    			if (data.status == 'success') {
    				canSendMoblie=true;
    			}else{
    				canSendMoblie=false;
    			}
    		}
    	});
}
function setCssStyle(){
	$(".ncpt_contents").css({"bottom":"-5px"});
	$(".ncpt_slide_bg").css({"height":"36px"});
	$(".ncpt_slide_bg .ncpt_slide_fg").css({"margin-top":"7px"});
	$(".ncpt_slide_bg .ncpt_hint_txt").css({"line-height":"34px"});
}
  </script>
</#macro>