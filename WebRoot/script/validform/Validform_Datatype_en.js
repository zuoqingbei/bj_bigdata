//validaFrom扩展英文提示
(function () {
    if ($.Datatype) {
        $.extend($.Tipmsg, {
        	tit: "prompt message",
	        w:{
		        "*": "can not be empty! ",
		        "*6-16": "please fill in 6 to 16 bit arbitrary characters! ",
		        "n": "please fill in the numbers! ",
		        "double": "please fill in the numbers or decimal numbers! ",
		        "n6-16": "please fill in 6 to 16 digits! ",
		        "s": "can't enter special characters! ",
		        "s6-18": "please fill in 6 to 18 characters! ",
		        "p": "please fill in the zip code! ",
		        "m": "please fill in the phone number! ",
		        "e": "email address format is incorrect! ",
		        "url": "please fill in the url! ",
		        "date": "please fill in the correct date! ",
		        "zh": "please fill in chinese! ",
		        "en": "please fill in english! ",
		        "dword": "please fill in the double byte character! ",
		        "money": "please fill in the value of money! ",
		        "ipv4": "please fill in the ip address! ",
		        "ipv6": "please fill in the ipv6 address! ",
		        "num": "please fill in the value! ",
		        "qq": "please fill in the qq number! ",
		        "unequal": "value can not be equal! ",
		        "notvalued": "can not contain a specific value! ",
		        "numrange": "please enter the number of legal size",
		        "idcard": "the id card number is not correct! ",
		        "tel": "fixed phone format is not correct! ",
		        "c": "fax or phone format is not correct! ",
		        "integer": "please fill in an integer greater than zero",
		        "ppt_suffix": "please upload the ppt format file",
		        "photo_suffix": "please upload the picture!",
		        "doc_suffix": "please upload doc or pdf format file",
		        "doc_only_suffix":"please upload doc format file",
		        "excel_suffix": "please upload the excel format file",
		        "file_size": "upload file is too large!",
		        "empty":"",
		        "email": "please fill in the mailbox! ",
		        "intl_tel": "please fill in the correct phone number! ",
		        "dword": "please fill in the double byte character! "
	        },
	        def: "please fill in the correct information! ",
	        undef:"datatype not defined!",
	        reck: "two times the content of the input is not consistent! ",
	        r: "through information verification! ",
	        c:" is testing information... ",
	        s: "please {fill in| select}{0| information}! ",
	        v: "the information is not verified, please... ",
	        p:" is submitting data... "
        });
    } else {
        setTimeout(arguments.callee, 10);
    }
})();

