/**
 * Created by Administrator on 2017/6/2.
 */

var charts = [];

var chart01 = echarts.init($("#chart01")[0]);
var opt01 = {
    title : {
        show:false,
        text: '饼图',
        subtext: '纯属虚构',
        x:'center'
    },
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    legend: {
        orient : 'vertical',
        x : 'left',
        data:['EMAIL','APP','WEB','OTHER']
    },
    toolbox: {
        show : true,
        feature : {
            mark : {show: true},
            dataView : {show: false, readOnly: false},
            magicType : {
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
            restore : {show: false},
            saveAsImage : {show: true}
        }
    },
    calculable : true,
    textStyle:{
        color:"#ddd"
    },
    series : [
        {
            name:'访问来源',
            type:'pie',
            radius : '55%',
            center: ['60%', '55%'],
            data:[
                {value:335, name:'EMAIL'},
                {value:310, name:'APP'},
                {value:234, name:'WEB'},
                {value:135, name:'OTHER'}
            ]
        }
    ]
};
chart01.setOption(opt01);
var chart02 = echarts.init($("#chart02")[0]);
chart02.setOption(opt01);
var chart03 = echarts.init($("#chart03")[0]);
chart03.setOption(opt01);
opt04 = {
    title : {
        show:false,
        text: '世界人口总量',
        subtext: '数据来自网络'
    },
    tooltip : {
        trigger: 'axis'
    },
    legend: {
        show:false,
        data:['2011年', '2012年']
    },
    toolbox: {
        show : true,
        feature : {
            mark : {show: true},
            dataView : {show: false, readOnly: false},
            magicType: {show: false, type: ['line', 'bar']},
            restore : {show: false},
            saveAsImage : {show: true}
        }
    },
    calculable : true,
    textStyle:{
        color:"#ddd"
    },
    xAxis : [
        {
            type : 'value',
            boundaryGap : [0, 0.01]
        }
    ],
    yAxis : [
        {
            type : 'category',
            data : ['A部门','B部门','C部门','D部门','E部门','F部门']
        }
    ],
    series : [
        {
            name:'2011年',
            type:'bar',
            data:[18203, 23489, 29034, 104970, 131744, 630230]
        }
    ]
};
var chart04 = echarts.init($("#chart04")[0]);
chart04.setOption(opt04);
var chart05 = echarts.init($("#chart05")[0]);
chart05.setOption(opt04);
opt06 = {
    title : {
        show:false,
        text: '未来一周气温变化',
        subtext: '纯属虚构'
    },
    tooltip : {
        trigger: 'axis'
    },
    legend: {
        data:['素材量','新闻量']
    },
    toolbox: {
        show : true,
        feature : {
            mark : {show: false},
            dataView : {show: false, readOnly: false},
            magicType : {show: false, type: ['line', 'bar']},
            restore : {show: false},
            saveAsImage : {show: true}
        }
    },
    calculable : true,
    textStyle:{
        color:"#ddd"
    },
    xAxis : [
        {
            type : 'category',
            boundaryGap : false,
            data : ['2013','2014','2015','2016','2017']
        }
    ],
    yAxis : [
        {
            type : 'value',
            axisLabel : {
                formatter: '{value} °C'
            }
        }
    ],
    series : [
        {
            name:'素材量',
            type:'line',
            data:[11, 11, 15, 13, 12],
            markPoint : {
                data : [
                    {type : 'max', name: '最大值'},
                    {type : 'min', name: '最小值'}
                ]
            },
            markLine : {
                data : [
                    {type : 'average', name: '平均值'}
                ]
            }
        },
        {
            name:'新闻量',
            type:'line',
            data:[1, -2, 2, 5, 3, 2, 0],
            markPoint : {
                data : [
                    {name : '周最低', value : -2, xAxis: 1, yAxis: -1.5}
                ]
            },
            markLine : {
                data : [
                    {type : 'average', name : '平均值'}
                ]
            }
        }
    ]
};
var chart06 = echarts.init($("#chart06")[0]);
chart06.setOption(opt06);

var chartNewsPie01 = echarts.init($("#chartNewsPie01")[0]);
chartNewsPie01.setOption(opt01);
var chartNewsPie02 = echarts.init($("#chartNewsPie02")[0]);
chartNewsPie02.setOption(opt01);
var chartNewsPie03 = echarts.init($("#chartNewsPie03")[0]);
chartNewsPie03.setOption(opt01);

charts.push(chartNewsPie01,chartNewsPie02,chartNewsPie03);

function chartsResize() {
    // console.log("----------chartsResize");
    chartNewsPie01.resize();
    for(var i=0; i<charts.length; i++){
        // console.log("~~~~~~~~~~chartsResize",charts[i].getOption().title[0].text);
        charts[i].resize();
    }
    // console.log("+++++++++++++++chartsResize");
}



