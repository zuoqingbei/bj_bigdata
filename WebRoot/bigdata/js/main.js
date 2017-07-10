/**
 * Created by Administrator on 2017/6/1.
 */

// var currentPage;
$(function () {

    var $l = $("#l");
    var $r = $("#r");
    //切换页面
    function pageSwitch(selectedText) {
        if (selectedText === "总览") {
            $("#index").show(300).siblings(".switch").hide(500);
        } else if (selectedText === "素材分析") {
            $("#sourceAnalysis").show(300).siblings(".switch").hide(500);
        } else if (selectedText === "新闻分析") {
            $("#newsAnalysis").show(300, function () {
                chartsResize();
            }).siblings(".switch").hide(500);
        }else if(selectedText === "使用方式"){
            $("#usageMode").show(300, function () {
                chartsResize();
            }).siblings(".switch").hide(500);
        }else if(selectedText === "评价级别"){
            $("#level").show(300, function () {
                chartsResize();
            }).siblings(".switch").hide(500);
        }
    }
    //增加顶部tab标签
    function addTab(selectedText) {
        if ($r.find("li.tab>button").text().indexOf(selectedText) < 0) {//如果tab里不存在
            $r.find("li.tab>button").removeClass("selected");
            $r.find("li.tab").append('<button class="btn btn-default selected" ><span class="navText">' + selectedText + '</span><span class="glyphicon glyphicon-remove"></span></button>');
        }else{//如果tab里已经存在
            $r.find("li.tab>button:contains("+selectedText+")").addClass("selected").siblings().removeClass("selected");
        }
    }
    //左边的菜单
    //一级菜单
    $l.find("nav>ul>li").click(function () {
        $(this).find(".navText").next("ul").slideToggle();

        $(this).not($(this).has("ul")).addClass("selected").siblings().removeClass("selected")
            .find("ul>li").removeClass("selected");//不包含子菜单
        var selectedText = $(this).children(".navText").text();
        if(!$(this).find("ul").length>0){//不增加具有子菜单的一级菜单标题
            addTab(selectedText);
        }
        pageSwitch(selectedText);
    });

    //二级菜单
    $l.find("nav>ul>li>ul>li").click(function (e) {

        $l.find("nav li ").removeClass("selected");
        $(this).addClass("selected");

        var selectedText = $(this).children(".navText").text();
        addTab(selectedText);
        pageSwitch(selectedText);
        e.stopPropagation();//阻止冒泡
    });

    //顶部的选项卡
    $r.find("li.tab").on("click","button",function () {
        $(this).addClass("selected").siblings().removeClass("selected").removeClass("active");
        var selectedText = $(this).children(".navText").text();
        $l.find("nav li ").removeClass("selected");
        $l.find("nav span.navText:contains("+selectedText+")").not(".noSelected").parent().addClass("selected");
        pageSwitch(selectedText);

    });

    //顶部选项卡关闭
    $r.find("header .tab").on("click", " span.glyphicon.glyphicon-remove", function (e) {
        var selectedText;
        if($(this).parent().attr("class").indexOf("selected")>0){
            if($(this).parent().prev().length>0){
                $r.find("header .tab>button").removeClass("selected");
                $(this).parent().prev().addClass("selected").next().remove();
                selectedText = $r.find("header .tab>button.selected .navText").text()
            }else if($(this).parent().next().length>0){
                $r.find("header .tab>button").removeClass("selected");
                $(this).parent().next().addClass("selected").prev().remove();
                selectedText = $r.find("header .tab>button.selected .navText").text()
            }
        }else{//没有被激活则直接关闭
            $(this).parent().remove();
        }
        pageSwitch(selectedText);
        e.stopPropagation();//阻止冒泡到切换选项卡的动作
    });

    //悬停效果
    $l.find("nav>ul>li:not(:has(ul)),nav>ul>li>ul>li").hover(function () {
        $l.find("nav li ").removeClass("hover");
        $(this).addClass("hover");
    });

    //素材分析管理页面钻取功能
    var $sourceAnalysis = $("#sourceAnalysis");

    //点击部门
    $sourceAnalysis.find("table .department").click(function () {
        var $total = $(this).next().find(".total");
        if ($total.is(":visible")) {
            $total.hide().siblings().show();
        } else {
            $total.show().siblings().hide();
        }
    });

    //点击发稿人
    $sourceAnalysis.find("table .reporter-name").click(function () {
        var index = $(this).index();
        var $total = $(this).parents("td").next().find(".item .total ");

        if ($total.eq(index).is(":visible")) {
            $(this).parents("td").nextAll().find(".item:nth-child(" + (index + 1) + ")").children(".total").hide().siblings(".content").show();
            $(this).css("height", "5em")
        } else {
            $(this).parents("td").nextAll().find(".item:nth-child(" + (index + 1) + ")").children(".total").show().siblings(".content").hide();
            $(this).css("height", "1em")
        }
    });

    //整行悬停样式
    var tableHoverIn = {
        color: "#fff",
        backgroundColor: "rgba(200,100,0,.3)",
        textShadow: "1px 1px 1px black",
        fontWeight: "bold"
    }, tableHoverOut = {
        color: "#ddd",
        backgroundColor: "transparent",
        textShadow: "none",
        fontWeight: "normal"
    };

    //钻取后每条数据悬停样式
    $sourceAnalysis.find("table tr .total").hover(function () {
        var index = $(this).parents(".item").index();
        var className = $(this).attr("class");
        $(this).parents("tr").find("td .item:nth-child(" + (index + 1) + ")").children(".total").css(tableHoverIn);
        console.log($(this).attr("class"))
    }, function () {
        var index = $(this).parents(".item").index();
        var className = $(this).attr("class");
        $(this).parents("tr").find("td .item:nth-child(" + (index + 1) + ")").children(".total").css(tableHoverOut);
    });

    function sourceAnalysisHover($this,mouse) {
        var indexItem = $this.parents(".item").index();
        var indexDiv = $this.index();
        $this.parents("tr").find("td .item:nth-child(" + (indexItem + 1) + ")>.content>div:nth-child(" + (indexDiv + 1) + ")")
            .css(mouse);
    }
    $sourceAnalysis.find("table tr .content>div").hover(function () {
        sourceAnalysisHover($(this),tableHoverIn);
    }, function () {
        sourceAnalysisHover($(this),tableHoverOut);
    });

    //使用方式和评价级别
    var $usageModeAndLevel = $("#usageMode,#level");
    // 点击部门
    $usageModeAndLevel.find("table .department>.total").click(function () {
        $(this).parents("tr").find(".content").slideToggle();
    });
    //悬浮样式
    $usageModeAndLevel.find("table tr .total").hover(function () {
        $(this).parents("tr").find("td .total").css(tableHoverIn);
    }, function () {
        $(this).parents("tr").find("td .total").css(tableHoverOut)
    });
    $usageModeAndLevel.find("table tr .content>div").hover(function () {
        $(this).parents("tr").find("td .content>div:nth-child("+($(this).index()+1)+")").css(tableHoverIn);
    }, function () {
        $(this).parents("tr").find("td .content>div:nth-child("+($(this).index()+1)+")").css(tableHoverOut);
    });
});









