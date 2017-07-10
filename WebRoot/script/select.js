//复选框
	 
	/* 是否全选标记 */
	var checkedAll = false;
	
	/* 全选/取消全选
	* formName 所在form的name值
	* checkboxName checkbox的name值
	* 注意：所有checkbox的name值都必须一样，这样才能达到全选的效果
	*/
	function selectAll(formName,checkboxName){
		var form = document.forms[0];
		var elements = form.elements[checkboxName];
		if(elements.length!=undefined){
			for (var i=0;i<elements.length;i++){
				var e = elements[i];
				if(checkedAll){
					e.checked = false;
					form.alls.checked = false;
				} else {
					e.checked = true;
					form.alls.checked = true;
				}
			}
		}else{
			if(checkedAll){
				elements.checked = false;
				form.alls.checked = false;
			} else {
				elements.checked = true;
				form.alls.checked = true;
			}
		}			
		
		if(checkedAll){
			checkedAll = false;
		} else {
			checkedAll = true;
		}
	} 
	/* 检查是否有checkbox被选中
	* formName 所在form的name值
	* checkboxName checkbox的name值
	* 注意：所有checkbox的name值都必须一样，这样才能达到全选的效果
	*/
	function checkAll(formName,checkboxName){
		var hasCheck = false;
		var form = document.forms[0];
		var elements = form.elements[checkboxName];
		if(elements.length!=undefined){
			for (var i=0;i<elements.length;i++){
				var e = elements[i];
				if(e.checked){
					hasCheck = true;
				}
			}
		}else{
		
		if(elements.checked){
					hasCheck = true;
				}
		}
		
		return hasCheck;
	}
	