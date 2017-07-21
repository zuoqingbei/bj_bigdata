/**
 * Created by Administrator on 2017/6/1.
 */

// var currentPage;
$(function () {

    parentAutoH();

    //素材分析管理页面钻取功能
    var $sourceAnalysis = $("#sourceAnalysis");
    //点击部门
    $sourceAnalysis.find("table .department").click(function () {
//            console.log("------------------------------123")
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
            var lineNum = $(this).parents("td").next().find(".item:eq("+index+")").children(".content").children().length;
            $(this).css("line-height",lineNum*1.8+"em");//设置发稿人的行高使上下居中
            console.log(lineNum,this)
        } else {
            $(this).parents("td").nextAll().find(".item:nth-child(" + (index + 1) + ")").children(".total").show().siblings(".content").hide();
            $(this).css("line-height", 1.42857143);
        }
        //表格高度增加后撑开整个页面，避免出现滚动条
        parentAutoH();
    });
    //钻取前每个发稿人悬停样式
    $sourceAnalysis.find("table tr .total").hover(function () {
        var index = $(this).parents(".item").index();
        var className = $(this).attr("class");
        $(this).parents("tr").find("td .item:nth-child(" + (index + 1) + ")").children(".total").addClass("active");
    }, function () {
        var index = $(this).parents(".item").index();
        var className = $(this).attr("class");
        $(this).parents("tr").find("td .item:nth-child(" + (index + 1) + ")").children(".total").removeClass("active");
    });
    //钻取后每条数据悬停动作
    $sourceAnalysis.find("table tr .content>div").hover(function () {
        sourceAnalysisHover($(this), "in");
    }, function () {
        sourceAnalysisHover($(this), "out");
    });
    function sourceAnalysisHover($this, classOpt) {
        var indexItem = $this.parents(".item").index();
        var indexDiv = $this.index();
        var $elm = $this.parents("tr").find("td .item:nth-child(" + (indexItem + 1) + ")>.content>div:nth-child(" + (indexDiv + 1) + ")");
        (classOpt === "in") ? $elm.addClass("active") : $elm.removeClass("active");
    }

    //使用方式和评价级别
    var $usageModeAndLevel = $("#usageMode,#level");
    // 点击部门
    $usageModeAndLevel.find("table .department>.total").click(function () {
        $(this).parents("tr").find(".content").slideToggle(function(){
            if($(this).is(":visible")){
                $(this).prev().addClass("active")
            }else {
                $(this).prev().removeClass("active")
            }
            parentAutoH();
        });
    });

    //悬浮样式
    $usageModeAndLevel.find("table tr .content>div").hover(function () {
        $(this).parents("tr").find("td .content>div:nth-child(" + ($(this).index() + 1) + ")").addClass("active");
    }, function () {
        $(this).parents("tr").find("td .content>div:nth-child(" + ($(this).index() + 1) + ")").removeClass("active");
    });

});

laydate({
    elem: '#date1',
    format: 'YYYY/MM', // 分隔符可以任意定义，该例子表示只显示年月
    festival: true //显示节日
});

// 自动撑开父页面高度
function parentAutoH() {
    $("iframe",parent.document).height($("html").height());
}





