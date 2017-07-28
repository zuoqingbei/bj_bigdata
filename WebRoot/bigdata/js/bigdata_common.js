/**
 * Created by zuoqb on 2017/07/06
 */
//请求参数
var params={};
//拼接参数
function joinParam(){
	var startDate=$("input[name='startDate_select']").val()=="请选择起始日期"?"":$("input[name='startDate_select']").val();
	startDate=startDate.replace(new RegExp("/","gm"),"-");
	var endDate=$("input[name='endDate_select']").val()=="请选择截止日期"?"":$("input[name='endDate_select']").val();
	if(endDate==undefined){
		endDate="";
	}
	endDate=endDate.replace(new RegExp("/","gm"),"-");
	params={
			"companyId":$("select[name='companyId_select'] :selected").attr("data"),//大单位
			"deptId":$("select[name='deptId_select'] :selected").attr("data"),//部门
			"creatorId":$("select[name='creatorId_select'] :selected").attr("data"),//发稿人
			"direct":$("select[name='direct_select'] :selected").attr("data"),//方向
			"startDate":startDate,//开始时间
			"endDate":endDate,//结束时间
	};
	//设置隐藏值
	setHiddenValue();
	return params;
}
//设置隐藏值
function setHiddenValue(){
	$("#companyId").val(params.companyId);
	$("#deptId").val(params.deptId);
	$("#creatorId").val(params.creatorId);
	$("#direct").val(params.direct);
	$("#startDate").val(params.startDate);
	$("#endDate").val(params.endDate);
}
//查询
function submitForm(){
	joinParam();
	setHiddenValue();
	$("#pageNum").val("1");
	$("#search_from").attr("action",searchAction).submit();
}
//导出
function exportExcel(action){
	joinParam();
	setHiddenValue();
	$("#search_from").attr("action",action).submit();
}
//分页 页码点击
function paginateMethod(pageNum){
		$("#pageNum").val(pageNum);
		joinParam();
		setHiddenValue();
		$("#search_from").submit();
}
//加载筛选条件 
function loadConditions(isFirst){
	//只有在进行切换下拉时拼接参数 否则就是表单提交 参数需要从attr中获取
	if(isFirst==undefined||!isFirst){
		joinParam();
	}
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
		$("select[name='companyId_select']").html(company.join(''));
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
		$("select[name='deptId_select']").html(depts.join(''));
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
		$("select[name='creatorId_select']").html(creator.join(''));
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
		$("select[name='direct_select']").html(direct.join(''));
	});
};






function Map() {  
    this.elements = new Array();  
    //获取MAP元素个数  
    this.size = function() {  
        return this.elements.length;  
    };  
    //判断MAP是否为空  
    this.isEmpty = function() {  
        return (this.elements.length < 1);  
    };  
    //删除MAP所有元素  
    this.clear = function() {  
        this.elements = new Array();  
    };  
    //向MAP中增加元素（key, value)   
    this.put = function(_key, _value) {  
        this.elements.push( {  
            key : _key,  
            value : _value  
        });  
    };  
    //删除指定KEY的元素，成功返回True，失败返回False  
    this.remove = function(_key) {  
        var bln = false;  
        try {  
            for (i = 0; i < this.elements.length; i++) {  
                if (this.elements[i].key == _key) {  
                    this.elements.splice(i, 1);  
                    return true;  
                }  
            }  
        } catch (e) {  
            bln = false;  
        }  
        return bln;  
    };  
    //获取指定KEY的元素值VALUE，失败返回NULL  
    this.get = function(_key) {  
        try {  
            for (i = 0; i < this.elements.length; i++) {  
                if (this.elements[i].key == _key) {  
                    return this.elements[i].value;  
                }  
            }  
        } catch (e) {  
            return null;  
        }  
    };  
    //获取指定索引的元素（使用element.key，element.value获取KEY和VALUE），失败返回NULL  
    this.element = function(_index) {  
        if (_index < 0 || _index >= this.elements.length) {  
            return null;  
        }  
        return this.elements[_index];  
    };  
    //判断MAP中是否含有指定KEY的元素  
    this.containsKey = function(_key) {  
        var bln = false;  
        try {  
            for (i = 0; i < this.elements.length; i++) {  
                if (this.elements[i].key == _key) {  
                    bln = true;  
                }  
            }  
        } catch (e) {  
            bln = false;  
        }  
        return bln;  
    };  
    //判断MAP中是否含有指定VALUE的元素  
    this.containsValue = function(_value) {  
        var bln = false;  
        try {  
            for (i = 0; i < this.elements.length; i++) {  
                if (this.elements[i].value == _value) {  
                    bln = true;  
                }  
            }  
        } catch (e) {  
            bln = false;  
        }  
        return bln;  
    };  
    //获取MAP中所有VALUE的数组（ARRAY）  
    this.values = function() {  
        var arr = new Array();  
        for (i = 0; i < this.elements.length; i++) {  
            arr.push(this.elements[i].value);  
        }  
        return arr;  
    };  
    //获取MAP中所有KEY的数组（ARRAY）  
    this.keys = function() {  
        var arr = new Array();  
        for (i = 0; i < this.elements.length; i++) {  
            arr.push(this.elements[i].key);  
        }  
        return arr;  
    };  
}  

//引入没有引入的JS、css
function importJSCSS(name){
	if(!isIncludeJSCSS(name)){
		var baseName=name.split("?")[0];
		if(baseName.endWith("css")||baseName.endWith("CSS")){
			document.write('<link href="'+name+'" rel="stylesheet" type="text/css" />');
		}else{
			document.write("<script language=javascript src='"+name+"'>\<\/script>");
		}
	}
}
//判断是否引入某个JS、css
function isIncludeJSCSS(name){
    var js= /js$/i.test(name);
    var es=document.getElementsByTagName(js?'script':'link');
    for(var i=0;i<es.length;i++) 
    if(es[i][js?'src':'href'].indexOf(name)!=-1)return true;
    return false;
}	
String.prototype.startWith=function(str){  
    if(str==null||str==""||this.length==0||str.length>this.length)  
      return false;  
    if(this.substr(0,str.length)==str)  
      return true;  
    else  
      return false;  
    return true;  
}  
String.prototype.endWith=function(str){  
    if(str==null||str==""||this.length==0||str.length>this.length)  
      return false;  
    if(this.substring(this.length-str.length)==str)  
      return true;  
    else  
      return false;  
    return true;  
}  







