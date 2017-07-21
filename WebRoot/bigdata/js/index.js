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
        var selectedText = $(this).children(".navText").text();
        if (!$(this).find("ul").length > 0) {//不增加具有子菜单的一级菜单标题
            addTab(selectedText);
        }
        pageSwitch(selectedText);
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

        var selectedText = $(this).children(".navText").text();
        addTab(selectedText);
        pageSwitch(selectedText);
        e.stopPropagation();//阻止冒泡
    });

    //顶部的选项卡切换
    $r.find("ul.nav").on("click", "li", function () {
        $(this).addClass("active").siblings().removeClass("active").removeClass("active");
        var selectedText = $(this).find("a>.navText").text();
        $l.find("nav li ").removeClass("active");
        $l.find("nav span.navText:contains(" + selectedText + ")").not(".noSelected").parent().addClass("active");
        pageSwitch(selectedText);
    });

    //顶部选项卡关闭
    $r.find("header .nav-tabs").on("click", "li>a>span.glyphicon.glyphicon-remove", function (e) {
        var selectedText;
        if ($(this).parents("li").attr("class").indexOf("active") > -1) {
            $(this).parents("li").removeClass("active");
            if ($(this).parents("li").prev().length > 0) {
                $(this).parents("li").prev().addClass("active");
            } else if ($(this).parents("li").next().length > 0) {
                $(this).parents("li").next().addClass("active");
            } else {
                $(this).parents("li").addClass("active");
            }
            selectedText = $r.find("header .nav-tabs>li.active .navText").text()
        }
        if ($r.find("header .nav-tabs>li").length !== 1) {//如果只剩一个则不能关闭
            $(this).parents("li").remove();
        }
        pageSwitch(selectedText);
        e.stopPropagation();//阻止冒泡到切换选项卡的动作
    });

});

//切换页面
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

//通过左侧菜单增加、激活顶部tab标签
function addTab(selectedText) {
    if ($r.find("ul.nav-tabs>li").text().indexOf(selectedText) < 0) {//如果tab里不存在
        $r.find("ul.nav-tabs>li").removeClass("active");
        $r.find("ul.nav-tabs").append('<li class="active" ><a><span class="navText">' + selectedText + '</span><span class="glyphicon glyphicon-remove"></span></a></li>');
    } else {//如果tab里已经存在
        $r.find("ul.nav-tabs>li:contains(" + selectedText + ")").addClass("active").siblings().removeClass("active");
    }
}


