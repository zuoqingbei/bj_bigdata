/**
 * Created by zuoqb on 2017/07/06
 */
var params={};
//拼接参数
function joinParam(){
	params={
			"companyId":$("select[name='companyId'] :selected").attr("data"),//大单位
			"deptId":$("select[name='deptId'] :selected").attr("data"),//部门
			"creatorId":$("select[name='reporter'] :selected").attr("data"),//发稿人
			"direct":$("select[name='direct'] :selected").attr("data"),//方向
			"startDate":$("input[name='startDate']").val(),//开始时间
			"endDate":$("input[name='endDate']").val(),//结束时间
	};
	return params;
}
//加载筛选条件
function loadConditions(){
	joinParam();
	console.log(params)
	conditionsCompanys();
	conditionsDepts();
	conditionsCreator();
	conditionsdRef();
};
//大单位
function conditionsCompanys(){
	$.post("/conditions/companyAjax",params,function(data){
		var company=['<option data="">请选择单位</option>'];
		$.each(data,function(index,item){
			if(params.companyId==item.id){
				company.push('<option data="'+item.id+'" selected>'+item.name+'</option>');
			}else{
				company.push('<option data="'+item.id+'">'+item.name+'</option>');
			}
		});
		$("select[name='companyId']").html(company.join(''));
	});
};
//获取部门
function conditionsDepts(){
	$.post("/conditions/deptAjax",params,function(data){
		var depts=['<option data="">请选择部门</option>'];
		$.each(data,function(index,item){
			if(params.deptId==item.id){
				depts.push('<option data="'+item.id+'" selected>'+item.name+'</option>');
			}else{
				depts.push('<option data="'+item.id+'">'+item.name+'</option>');
			}
		});
		$("select[name='deptId']").html(depts.join(''));
	});
};
//发稿人
function conditionsCreator(){
	$.post("/conditions/creatorAjax",params,function(data){
		var creator=['<option data="">请选择发稿人</option>'];
		$.each(data,function(index,item){
			if(params.creatorId==item.id){
				creator.push('<option data="'+item.id+'" selected>'+item.name+'</option>');
			}else{
				creator.push('<option data="'+item.id+'">'+item.name+'</option>');
			}
		});
		$("select[name='creatorId']").html(creator.join(''));
	});
};
//获取方向 
function conditionsdRef(){
	$.post("/conditions/dRefAjax",params,function(data){
		var direct=['<option data="">请选择方向</option>'];
		$.each(data,function(index,item){
			if(params.direct==item.id){
				direct.push('<option data="'+item.id+'" selected>'+item.name+'</option>');
			}else{
				direct.push('<option data="'+item.id+'">'+item.name+'</option>');
			}
		});
		$("select[name='direct']").html(direct.join(''));
	});
};









