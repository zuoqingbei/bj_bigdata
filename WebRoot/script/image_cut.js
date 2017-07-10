    /**
     * Author:姜修峰
     * 使用说明：
     * 默认裁剪的图片id(img的id) 是old,默认裁剪后显示的三张图片的id(img的id) 是big middle small 如：
     * <img id="old" src="/images/chooseimg.jpg" border="0"/>
     * 在html中放入下面隐藏的值,供后台裁剪
     * <input type="hidden" id="imgSrcPath"  name="imgSrcPath" value=""/>
     * <input type="hidden" id="cutImageW"  name="imgWidth" value=""/>
     * <input type="hidden" id="cutImageH"  name="imgHeight" value=""/>
     * <input type="hidden" id="cutImageX"  name="imgTop" value=""/>
     * <input type="hidden" id="cutImageY"  name="imgLeft" value=""/>
     * <input type="hidden" id="imgScale"  name="imgScale" value=""/>
     * 
     * 调用的时候只要写imgcut(oldimageW,oldimageH);oldimageW是原图实际宽度,oldimageH是原图的实际高度
     * 只需要修改imageW,imageW,imageW,minHeight,middleminWidth,middleminHeight,smallminWidth,smallminHeight的数值
     * 即你裁剪和裁剪后图片的高和宽
     */
    var imageW = 300;//默认框的大小
	var imageH = 440;
	var minWidth = 180;//裁剪第一个框的大小
	var minHeight =264;
	var middleminWidth = 50;//裁剪第二个框的大小
	var middleminHeight =73;
	var smallminWidth = 30;//裁剪第三个框的大小
	var smallminHeight = 44;
	var oldimgW;
	var oldimgH;
	var scale;
	function preview(img, selection){//preview方法可得到当前图像的坐标(起点XY和终点XY)以及宽度和高度，为JQuery imageSelect插件提供的内置方法
		showCut(selection.width,selection.height,selection.x1,selection.y1,oldimgW,oldimgH);
	}
	function showCut(w,h,x,y,oldimageW,oldimageH){  
		var bigscaleX = minWidth / w;
		var bigscaleY = minHeight / h;
		var middlescaleX =middleminWidth / w;
		var middlescaleY =middleminHeight / h;
		var smallscaleX = smallminWidth / w;
		var smallscaleY = smallminHeight / h;
		$('#big').css({ width: Math.round(bigscaleX * oldimageW * scale) + 'px', height: Math.round(bigscaleY * oldimageH * scale) + 'px', marginLeft: '-' + Math.round(bigscaleX * x ) + 'px', marginTop: '-' + Math.round(bigscaleY * y) + 'px' });
		$('#middle').css({ width: Math.round(middlescaleX * oldimageW * scale) + 'px', height: Math.round(middlescaleY * oldimageH * scale) + 'px', marginLeft: '-' + Math.round(middlescaleX * x ) + 'px', marginTop: '-' + Math.round(middlescaleY * y) + 'px' });
		$('#small').css({ width: Math.round(smallscaleX * oldimageW * scale) + 'px', height: Math.round(smallscaleY * oldimageH * scale) + 'px', marginLeft: '-' + Math.round(smallscaleX * x ) + 'px', marginTop: '-' + Math.round(smallscaleY * y) + 'px' });
		$('input#cutImageW').val(w);
		$('input#cutImageH').val(h);
		$('input#cutImageX').val(x);
		$('input#cutImageY').val(y);
	}
	var errMsg="";
	function imgcut(oldimageW,oldimageH) {
		oldimgW=oldimageW;
		oldimgH=oldimageH;
         var scale1 = imageW/oldimageW;
         var scale2 = imageH/oldimageH;
         scale=scale1>scale2?scale2:scale1;
         $('#imgScale').val(scale);
		$('#old').css({width:Math.round(scale*oldimageW)+'px',height:Math.round(scale*oldimageH)+'px'});
		if(oldimageW<minWidth||oldimageH<minHeight) {
			errMsg="源图尺寸小于缩略图，请重设缩略图大小。"
			alert(errMsg);
			return;
		}
		if(oldimageW==minWidth||oldimageH==minHeight) {
			errMsg="源图和缩略图尺寸一致，请重设缩略图大小。"
			alert(errMsg);
			return;
		}
		var minSelW = Math.round(minWidth*scale);
		var minSelH = Math.round(minHeight*scale);
		var cutwidth=minWidth;
		var cutheight=minHeight;
		//确定裁剪框的大小
		if($('#old').width()<cutwidth&&$('#old').height()<cutheight){
			var wscale=cutwidth/$('#old').width();
			var hscale=cutheight/$('#old').height();
			var cutscale=wscale>hscale?hscale:wscale;
			cutwidth=$('#old').width()*cutscale;
			cutheight=$('#old').height()*cutscale;
		}else if($('#old').height()<cutheight){
			cutheight=$('#old').height();
			cutwidth=cutheight*(minWidth/minHeight);
		}else if($('#old').width()<cutwidth){
			cutwidth=$('#old').width();
			cutheight=cutwidth*(minHeight/minWidth);
		}
		var showoldscale=imageW+':'+imageH;
		$('img#old').imgAreaSelect({selectionOpacity:0,outerOpacity:'0.5',selectionColor:'blue',onSelectChange:preview,minWidth:minSelW,minHeight:minSelH,aspectRatio:showoldscale,x1:0,y1:0,x2:cutwidth,y2:cutheight});
		showCut(cutwidth,cutheight,0,0,oldimageW,oldimageH);
	};