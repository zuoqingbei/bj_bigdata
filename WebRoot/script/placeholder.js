/**
 * input或者textarea框中有默认字体，鼠标点击输入框字体消失，失去焦点，默认字头又出现，如果输入框输入值，则默认值，则不再出现

 * @author 李秀芬
 * 使用例子 
 * <input type="text" placeholder="aaa" id="aaa" />
 * <textarea type="text" placeholder="aaa" id="aaab" ></textarea>
 */
 ;(function($) {
   jQuery.fn.extend({
	   "placeholder" : function (options) {  

         var defaults = {  

             grayFontColor: "#ccc",  

             normalFontColor: "#999",  

             pFont: "14px", 
			  
             activeBorder: "#080",  

             posL:8,  

             zIndex: "99" 
        },  

         opts = $.extend(defaults, options);  
        
	
		$(this).parent().css("position", "relative");  
	
		var isIE = $.browser.msie,  
	     //判断ie的版本
		 version = $.browser.version;
	
		 var $this = $(this),  
	            iX = $this.position().left,
                iY = $this.position().top,    
				iH = $this.outerHeight(),
				iW = $this.outerWidth(),
				msg = $this.attr("placeholder"), 
				
			 oInput = $("<label>", {
	
			 "text": msg,  
	
			 "css": {  
	
				 "position": "absolute",                   
	             "left": iX + "px",  

                 "top": iY + "px",  

				 "width": iW - opts.posL + "px",  
	
				 "padding-left": opts.posL + "px",  
	
				 "height": iH + "px",  
	
				 "line-height": iH + "px",  
	
				 "color": opts.grayFontColor,  
	
				 "font-size": opts.pFont,  
	
				 "z-index": opts.zIndex,  
	
				"cursor": "text" 
	
			}  
	
			}).insertBefore($this);  
	
	
	
		 var value = $this.val();  
	
		 if (value.length > 0) {  
	
		 	oInput.hide();  
	
		 };  
	
		$this.on("focus", function () {  
	
			var value = $(this).val();  
	
			 if (value.length > 0) {  
	
				oInput.hide();  
	
			}  
	
			 oInput.css("color", opts.normalFontColor);  
	
			 //不同浏览器绑定字符发生变化事件
	
			 if(isIE && version < 9){  
	
				 var myEvent = "propertychange";  
	
			 }else{  
	
				 var myEvent = "input";  
	
			 }  
	
	
	
			 $(this).on(myEvent, function () {  
	
				 var value = $(this).val();  
	
				if (value.length == 0) {  
	
					 oInput.show();  
	
				 } else {  
	
					 oInput.hide();  
	
				 }  
	
			});  
	
	
	
		 }).on("blur", function () {  
	
			 var value = $(this).val();  
	
			 if (value.length == 0) {  
	
				 oInput.css("color", opts.grayFontColor).show();  
	
			 }  
	
		 });  
	
		 //  
	
		 oInput.on("click", function () {  
	
			 $this.trigger("focus");  
	
			 $(this).css("color", opts.normalFontColor)  
	
		 });  
	
		
         

    } })
})(jQuery)
$(function(){
	//如果支持placeholder ,则直接返回
	 if ("placeholder" in document.createElement("input")) return;  
	 if($.browser.msie) {   

            $(":input[placeholder]").each(function(){  

                 $(this).placeholder();  

             });  

        } 	
	
})