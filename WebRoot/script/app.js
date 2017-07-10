if(typeof app == undefined ) {
	var app = {url:'',comments:0,thumb:''};
}
if(typeof share_json == undefined ) {
	var share_json = {share: 'share'};
}


/**
 * 发送事件通知客户端打开某些应用的页面
 */
function app_open(action){
	if(UA.isAndroid()){
		window.android.app_open(action);	
	}else if(UA.isIOS()){
		var params={'action':action};
		document.location='doctorpda/app_open?json='+JSON.stringify(params);
	}
}

/**
 * 发送事件通知客户端做一些操作
 */
function app_do(action){
	if(UA.isAndroid()){
		window.android.app_do(action);	
	}else if(UA.isIOS()){
		var params={'action':action};
		document.location='doctorpda/app_do?json='+JSON.stringify(params);
	}
}

/**
 * 发送事件通知客户端打电话
 */
function app_call(phone){
	if(UA.isAndroid()){
		window.android.app_call(phone);	
	}else if(UA.isIOS()){
		var params={'phone':phone};
		document.location='doctorpda/app_call?json='+JSON.stringify(params);
	}
}

/**
 * 发送事件通知客户端打开相机
 */
function app_camera(){
	if(UA.isAndroid()){
		window.android.app_camera();	
	}else if(UA.isIOS()){
		document.location='doctorpda/app_camera';
	}
}

/**
 * 发送事件通知客户端打开相册
 */
function app_photo(){
	if(UA.isAndroid()){
		window.android.app_photo();	
	}else if(UA.isIOS()){
		document.location='doctorpda/app_photo';
	}
}

/**
 * 发送事件通知客户端使用移动的toast方式
 */
function app_alert(msg){
	try{
		if(UA.isAndroid()){
			window.android.alert(msg);	
		}else if(UA.isIOS()){
			var params={'msg':msg};
			document.location='doctorpda/app_alert?json='+JSON.stringify(params);
		}
	}catch (e){
		alert(msg);
	}
}

function app_alert_page(msg){
	if(UA.isAndroid()){
		window.android.alert(msg);	
	}else if(UA.isIOS()){
		alert(msg);
	}
}

function get_app_install(json_str,action){
  	if(UA.isAndroid()){
  		window.android.get_app_install(json_str,action);	
  	}else if(UA.isIOS()){
  		document.location='doctorpda/get_app_install?json='+json_str+'&action='+action;
  	}
  }

/**
 * 组装json字符串，客户端需要页面数据时调用
 */
function to_app_data(){
	try{
		if(UA.isAndroid()){
			window.android.to_app_data(JSON.stringify(app));
		}else if (UA.isIOS()){
			document.location='doctorpda/to_app_data?json='+JSON.stringify(app);
		}
	}catch (e) {
	}
}
//与to_app_data相同，需要调用两个方法时调用
function app_share(){
	if(UA.isAndroid()){
		window.android.app_share(JSON.stringify(share_json));
	}else if (UA.isIOS()){
		document.location='doctorpda/app_share?json='+JSON.stringify(share_json);
	}
}


/**
 * @description 页面请求app动作
 * @param type 	动作类型 
 * @param data  动作要传类型的JSON对象
 */
function to_app_action(type,data){
	var toAppJson={
		type:type,
		data:data
	};
	if(UA.isAndroid()){
		window.android.to_app_action(JSON.stringify(toAppJson));
	}else if (UA.isIOS()){
		document.location='doctorpda/to_app_action?json='+JSON.stringify(toAppJson);
	}
}

/**
 * @description APP请求页面动作,页面可以重写此方法，必须是再引入APP.js之后
 * @param type 	动作类型 
 * @param data  动作要传类型的JSON对象
 */
//function to_web_action(type,data){
//	
//}	
	
	
	
/** 图片延迟加载 */
var scrollLoad = (function (options) {
    var defaults = (arguments.length == 0) ? { src: 'data-src', time: 300} : { src: options.src || 'data-src', time: options.time ||300};
    var camelize = function (s) {
        return s.replace(/-(\w)/g, function (strMatch, p1) {
            return p1.toUpperCase();
        });
    };
    this.getStyle = function (element, property) {
        if (arguments.length != 2) return false;
        var value = element.style[camelize(property)];
        if (!value) {
            if (document.defaultView && document.defaultView.getComputedStyle) {
                var css = document.defaultView.getComputedStyle(element, null);
                value = css ? css.getPropertyValue(property) : null;
            } else if (element.currentStyle) {
                value = element.currentStyle[camelize(property)];
            }
        }
        return value == 'auto' ? '' : value;
    };
    var _init = function () {
        var offsetPage = window.pageYOffset ? window.pageYOffset : window.document.documentElement.scrollTop,
            offsetWindow = offsetPage + Number(window.innerHeight ? window.innerHeight : document.documentElement.clientHeight),
            docImg = document.images,
            _len = docImg.length;
        if (!_len) return false;
        for (var i = 0; i < _len; i++) {
            var attrSrc = docImg[i].getAttribute(defaults.src),
                o = docImg[i], tag = o.nodeName.toLowerCase(),
                src = docImg[i].getAttribute('src');
            if (o) {
                postPage = o.getBoundingClientRect().top + window.document.documentElement.scrollTop + window.document.body.scrollTop; postWindow = postPage + Number(this.getStyle(o, 'height').replace('px', ''));
                if ((postPage > (offsetPage-200) && postPage < offsetWindow) || (postWindow > offsetPage && postWindow < offsetWindow)) {
                    if (tag === 'img' && attrSrc !== null && src != attrSrc) {
                        o.setAttribute('src', attrSrc);
                        o.onload=function(){
                        	var width_tmp = this.style.width;
                        	var height_tmp = this.style.height;
                            this.style.width = 'auto';  
                            this.style.height = 'auto';
                            this.style.background='none';
                            if(this.width >= window.innerWidth){
                            	this.style.width =width_tmp;
                            	this.style.height = height_tmp;
                            }
                        }
                    }
                    o = null;
                }
            }
        };
        window.onscroll = function () {
            setTimeout(function () {
                _init();
            }, defaults.time);
        }
    };
    return _init();
});
scrollLoad();
/**
 * 加载事件
 * @param func
 */
function addLoadEvent(func) {
	var oldonload = window.onload;
	if (typeof window.onload != 'function') {
		window.onload = func;
	} else {
	window.onload = function() {
		oldonload();
		func();
    }
  }
}
/**
 * @description 事件绑定，兼容各浏览器
 * @param target 事件触发对象 
 * @param type   事件
 * @param func   事件处理函数
 */
function addEvents(target, type, func) {
    if (target.addEventListener)    //非ie 和ie9
        target.addEventListener(type, func, false);
    else if (target.attachEvent)   //ie6到ie8
        target.attachEvent('on' + type, func);
    else target['on' + type] = func;   //ie5
};

/**
 * @description 事件移除，兼容各浏览器
 * @param target 事件触发对象
 * @param type   事件
 * @param func   事件处理函数
 */
function removeEvents(target, type, func){
    if (target.removeEventListener)
        target.removeEventListener(type, func, false);
    else if (target.detachEvent)
        target.detachEvent('on' + type, func);
    else target['on' + type] = null;
};
var fireEvent = function(element,event){
  if (document.createEventObject){
    var evt = document.createEventObject();
    return element.fireEvent('on'+event,evt)
  } else {
    var evt = document.createEvent( 'HTMLEvents' );
    evt.initEvent(event, true, true);  
    return !element.dispatchEvent(evt);
  }
};
/**
 * 链接处理，所有链接使用app_open打开，调用客户端的原生打开方式。
 * 特殊处理，链接中带有 data-opentype="native" data-id="1" [data-goto="/community/"] 的调用原生页面打开
 */
//addLoadEvent(function(e){
//	var links = document.getElementsByTagName('a');
//	for(var i=0;i<links.length;i++){
//		addEvents(links[i],'click',function(e){
//			var src = this.href;
//			if(this.getAttribute('data-id') 
//					&& this.getAttribute('data-opentype') 
//					&& 'native' == this.getAttribute('data-opentype')){
//				var gotoPage = this.getAttribute('data-goto');
//				if(gotoPage){
//					location.href=gotoPage;
//					return false;
//				}else{
//					src="/content/"+this.getAttribute('data-id');
//				}
//			}
//			e.preventDefault();
//			e.stopPropagation();
//			app_open(src);
//			return false;
//		});
//	}
//});
