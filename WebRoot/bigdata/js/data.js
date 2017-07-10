/**
 * Created by Administrator on 2017/6/29.
 */
function loadData(){
	//params=$("#search_form").serialize();
	joinParam();
	//统计素材量
	statisticOrigin();
	//统计素材 同比 环比
	originHbTbAjax();
}
//统计素材量
function statisticOrigin(){
	$.post("/home/statisticOriginAjax",params,function(data){
		//console.log(data);
		$(".fac_orgigion_number").html(data.num)
	})
}
//统计素材 同比 环比
function originHbTbAjax(){
	$.post("/home/originHbTbAjax",params,function(data){
		//console.log(data);
		//$(".fac_orgigion_number").html(data.num)
	})
}










