/**
 * Created by Administrator on 2017/6/2.
 */
var toolbox = {
    show: true,
    top: "-1%",
    right: "8%",
    iconStyle: {
        normal: {
            color: "#ddd",
            borderColor: "#ddd"
        },
        emphasis: {
            color: "#5cb85c",
            borderColor: "#5cb85c"
        }
    },
    feature: {
        mark: {show: true},
        dataView: {show: false, readOnly: false},
        magicType: {
            show: false,
            type: ['pie', 'funnel'],
            option: {
                funnel: {
                    x: '25%',
                    width: '50%',
                    funnelAlign: 'left',
                    max: 1548
                }
            }
        },
        restore: {show: false},
        saveAsImage: {show: true}
    }
};
var axisLine = {lineStyle: {color: "rgba(255,255,255,.5)"}};
var charts = [];

var opt07 = {
    color: ["#5cb85c", "#ff4c5b", "#fc3", "#39f"],
    title: {
        show: false,
        text: '饼图',
        subtext: '纯属虚构',
        x: 'center'
    },
    tooltip: {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    grid: {
        bottom: "50%"
    },
    legend: {
        show: false,
        // orient : 'vertical',
        y: 'bottom',
        width: "20%",
        textStyle: {
            color: "#eee"
        },
        inactiveColor: "#666",
        data: ['EMAIL', 'APP', 'WEB', 'OTHER']
    },
    toolbox: toolbox,
    calculable: true,
    textStyle: {
        color: "#ddd"
    },
    series: [
        {
            name: '访问来源',
            type: 'pie',
            radius: '55%',
            center: ['55%', '55%'],
            data: [
                {value: 335, name: 'EMAIL'},
                {value: 310, name: 'APP'},
                {value: 234, name: 'WEB'},
                {value: 135, name: 'OTHER'}
            ]
        }
    ]
};
var chartNewsPie01 = echarts.init($("#chartNewsPie01")[0]);
chartNewsPie01.setOption(opt07);
var chartNewsPie02 = echarts.init($("#chartNewsPie02")[0]);
chartNewsPie02.setOption(opt07);
var chartNewsPie03 = echarts.init($("#chartNewsPie03")[0]);
chartNewsPie03.setOption(opt07);

charts.push(chartNewsPie01, chartNewsPie02, chartNewsPie03);

function chartsResize() {
    // console.log("----------chartsResize");
    chartNewsPie01.resize();
    for (var i = 0; i < charts.length; i++) {
        // console.log("~~~~~~~~~~chartsResize",charts[i].getOption().title[0].text);
        charts[i].resize();
    }
    // console.log("+++++++++++++++chartsResize");
}



