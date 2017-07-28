/**
 * Created by Administrator on 2017/6/1.
 */
var $l = $("#l");
var $r = $("#r");

$(function () {

    //左边一级菜单
    $l.find("nav>ul>li").click(function () {
        $(this).find(".navText~ul").slideToggle();
        $(this).not($(this).has("ul")).addClass("active").siblings().removeClass("active")
            .find("ul>li").removeClass("active");//不包含子菜单
        if (!$(this).find("ul").length > 0||$(this).attr("data")!="") {//不增加具有子菜单的一级菜单标题
            addTab(this);
        }
        var className = $(this).find("span.glyphicon").attr("class");
        if (className && className.indexOf("up") > -1) {
            $(this).find("span.glyphicon").attr("class", "glyphicon glyphicon-menu-down");
        } else {
            $(this).find("span.glyphicon").attr("class", "glyphicon glyphicon-menu-up");
        }

    });

    //左边二级菜单
    $l.find("nav>ul>li>ul>li").click(function (e) {
        $l.find("nav li").removeClass("active");
        $(this).addClass("active");
        addTab(this);
        e.stopPropagation();//阻止冒泡
    });

    //顶部的选项卡切换
    $r.find("ul.nav").on("click", "li", function () {
        $(this).addClass("active").siblings().removeClass("active").removeClass("active");
        pageSwitch( $(this).attr("selectedId"), $(this).attr("data"));
    });

    //顶部选项卡关闭
    $r.find("header .nav-tabs").on("click", "li>a>span.glyphicon.glyphicon-remove", function (e) {
    	var selectedId="";
    	var data="";
        if ($(this).parents("li").attr("class").indexOf("active") > -1) {
            $(this).parents("li").removeClass("active");
            if ($(this).parents("li").prev().length > 0) {
                $(this).parents("li").prev().addClass("active");
                selectedId= $(this).parents("li").prev().attr("selectedId");
                data=$(this).parents("li").prev().attr("data");
            } else if ($(this).parents("li").next().length > 0) {
                $(this).parents("li").next().addClass("active");
                selectedId= $(this).parents("li").next().attr("selectedId");
                data=$(this).parents("li").next().attr("data");
            } else {
                $(this).parents("li").addClass("active");
                selectedId= $(this).parents("li").attr("selectedId");
                data=$(this).parents("li").attr("data");
            }
            if($r.find("header .nav-tabs>li").length >1){
            	pageSwitch(selectedId,data);
            }
        }
        if ($r.find("header .nav-tabs>li").length !== 1) {//如果只剩一个则不能关闭
            $(this).parents("li").remove();
        }
        e.stopPropagation();//阻止冒泡到切换选项卡的动作
        
    });

});

/*//切换页面
function pageSwitch(selectedText) {
    if (selectedText === "总览") {
        $("iframe.switch").attr("src",ctx_path+"/home/allView")
    } else if (selectedText === "素材分析") {
        $("iframe.switch").attr("src",ctx_path+"/home/sourceAnalysis")
    } else if (selectedText === "新闻分析") {
        $("iframe.switch").attr("src",ctx_path+"/home/newsAnalysis")
    } else if (selectedText === "使用方式") {
        $("iframe.switch").attr("src",ctx_path+"/home/usageMode")
    } else if (selectedText === "评价级别") {
        $("iframe.switch").attr("src",ctx_path+"/home/level")
    }
}
*/
//切换页面
function pageSwitch(id,href) {
	  $l.find("nav li ").removeClass("active");
	  $("#left_menu_"+id).addClass("active");
	  if(href!=""){
		  $("iframe.switch").attr("src",href);
	  }
}

//通过左侧菜单增加、激活顶部tab标签
function addTab(obj) {
	var selectedId=$(obj).attr("selectedId");
	var selectedHref=$(obj).attr("data");
	var selectedText=$(obj).attr("li-name");
	var topId="top_tab_"+selectedId;
	var topDom=$("#"+topId).attr("data");
	if(topDom==null||topDom==undefined){//如果tab里不存在
		$r.find("ul.nav-tabs>li").removeClass("active");
		$r.find("ul.nav-tabs").append('<li id="'+topId+'" selectedId="'+selectedId+'" data="'+selectedHref+'" class="active" ><a><span class="navText">' + selectedText + '</span><span class="glyphicon glyphicon-remove"></span></a></li>');
	}else{
		$r.find("ul.nav-tabs>li").removeClass("active");
		$("#"+topId).addClass("active");
	};
}


