/**
 * lazy.js 1.0.1
 * author: 梦想oO天堂
 * site: www.lazy.kim
 * date: 2018-11-18
 * lazy.js依赖jquery,eayui,请先引入jquery和easyui的最新版本。
 */
 
var lazy = lazy || {};

lazy.easyui = {
	/**
	 * 自定义条件搜索，并更新结果集
	 * gridId datagrid、treegrid数据表格ID
	 * formId 带有查询条件的form表单ID
	 */
	search : function(gridId,formId) {
		if(gridId.substr(0,2)=="dg"){
			var queryParams = $('#'+gridId).datagrid('options').queryParams;
		}else{
			var queryParams = $('#'+gridId).treegrid('options').queryParams;
		}
		$.each($('#'+formId).serializeArray(), function() {
			queryParams[this['name']] = this['value'];
		});
		if(gridId.substr(0,2)=="dg"){
			$('#'+gridId).datagrid('reload');
		}else{
			$('#'+gridId).treegrid('reload');
		}
	},

	/**
	* 选择一行数据提交(适用于删除单行数据或修改单行数据属性值等操作)
    * gridId datagrid、treegrid数据表格ID
	* url 提交后台处理的url
	* primarykeyName 选中行数据的主键名
	*/
	selected : function(gridId, url, primarykeyName) {
		if(gridId.substr(0,2)=="dg"){
			var rows=$('#'+gridId).datagrid('getSelections');
			if(rows.length!=1){
				$.messager.alert('系统提示','请先选择一行数据！');
				return;
			}
			var row=rows[0];
		}else{
			var rows=$('#'+gridId).treegrid('getSelections');
			if(rows.length!=1){
				$.messager.alert('系统提示','请先选择一行数据！');
				return;
			}
			var row=rows[0];
		}
		$.messager.confirm('系统提示', '您确定要提交操作吗？', function (flag) {
			if (flag){
				$.post(url,{id:row[primarykeyName]},function(result){
					result = eval('(' + result + ')');
					if(!result.flag){
						$.messager.show({title:'错误提示',msg:result.msg,timeout:2000,showType:'slide'});
					}else{
						if(gridId.substr(0,2)=="dg"){
							$('#'+gridId).datagrid('reload');
						}else{
							$('#'+gridId).treegrid('reload');
						}
						$.messager.show({title:'成功提示',msg:result.msg,timeout:1000,showType:'slide'});
					}
				}) 
			}
		});
	},
	/**
	* 选择多行数据提交(适用对多行数据删除或修改多行数据属性值等操作)
	* gridId datagrid、treegrid数据表格ID
	* url 提交后台处理的url
	* primarykeyName 选中行数据的主键名
	*/
	selections : function(gridId, url, primaryKeyName) {
		var rows = $('#'+gridId).datagrid('getSelections');
		if (rows.length == 0) {
			$.messager.show({title:'错误提示',msg:'至少选择一行记录！',timeout:2000,showType:'slide'});
		}else{
			var id_arr = [];
			for (var i = 0; i < rows.length; i++) {
				id_arr.push(rows[i]+"."+ primaryKeyName);
			}
			ids=id_arr.join(',');
			$.post(url,{ids:ids},function(result){
				result = eval('(' + result + ')');
				if(!result.flag){
					$.messager.show({title:'错误提示',msg:result.msg,timeout:2000,showType:'slide'});
				}else{
					if(gridId.substr(0,2)=="dg"){
						$('#'+gridId).datagrid('reload');
					}else{
						$('#'+gridId).treegrid('reload');
					}
					$.messager.show({title:'成功提示',msg:result.msg,timeout:1000,showType:'slide'});
				}
			})	
		}
	},
	/**
	 * 查询并导出结果集
	* url 提交后台处理的url
	* formId 带有查询参数的formId
	 */
	exportSearch : function(url,formId) {
		x = $('#'+formId).serializeArray();
		$.each(x,function(i,field){
			if(i==0) {
				url = url + "?" + field.name + "=" + field.value ;
			} else {
				url = url + "&" + field.name + "=" + field.value ;
			}
			
		});
		window.open(url);
	},
	/**
	 * 依据一行选中数据的主键，导出结果集
	 * gridId datagrid、treegrid数据表格ID
	 * url 提交后台处理的url
	 * primarykeyName 选中行数据的主键名
	 */
	exportSelected : function(gridId,url,primarykeyName) {
		if(gridId.substr(0,2)=="dg"){
			var rows=$('#'+gridId).datagrid('getSelections');
			if(rows.length!=1){
				$.messager.alert('系统提示','请先选择一行数据！');
				return;
			}
			var row=rows[0];
		}else{
			var rows=$('#'+gridId).treegrid('getSelections');
			if(rows.length!=1){
				$.messager.alert('系统提示','请先选择一行数据！');
				return;
			}
			var row=rows[0];
		}
		window.open(url+"?id="+ row[primarykeyName]);
	},
	/**
	 * 打开填报数据对话框
	 * dialogId dialog对话框ID
	 * _func 打开填报数据对话框后，要执行的方法（设置默认值等...）
	 */
	openDialog : function(dialogId,_func) {
		$('#'+dialogId).dialog("open");
		_func();
	},
	/**
	 * 打开修改数据对话框
	 * gridId datagrid、treegrid数据表格ID
	 * dialogId dialog对话框ID
	 * formId form表单ID
	 */
	editDialog : function(gridId,dialogId,formId,_func) {
		if(gridId.substr(0,2)=="dg"){
			var rows=$('#'+gridId).datagrid('getSelections');
			if(rows.length!=1){
				$.messager.alert('系统提示','请先选择一行数据！');
				return;
			}
			var row=rows[0];
			$('#'+dialogId).dialog("open");
			$('#'+formId).form("load",row);
		}else{
			var rows=$('#'+gridId).treegrid('getSelections');
			if(rows.length!=1){
				$.messager.alert('系统提示','请先选择一行数据！');
				return;
			}
			var row=rows[0];
			$('#'+dialogId).dialog("open");
			$('#'+formId).form("load",row);
		}	
		_func();
	},
	/**
	 * 关闭对话框，并清除form表单内容
	 * dialogId dialog对话框ID
	 * formId form表单ID
	 */
	closeDialog : function(dialogId,formId) {
		$('#'+dialogId).dialog("close");
		if(formId!="" && formId!=null && formId!=undefined){
			$('#'+formId).form('clear');
		}
	},
	/**
	 * 提交数据，并关闭对话框，刷新数据表格 
	 * gridId datagrid、treegrid数据表格ID
	 * dialogId dialog对话框ID
	 * formId form表单ID
	 * formClearRest 关闭对话框后，form表单是清楚还是重置
	 */
	submitDialog : function(gridId,dialogId,formId,formClearRest) {
		$.post($('#'+formId).attr("action"), $('#'+formId).serialize(), function(rs) {
			var result = eval('(' + rs + ')');
			if(result.flag){
				$.messager.show({title:'成功提示',msg:result.msg,timeout:1000,showType:'slide'});
				if(gridId.substr(0,2)=="dg"){
					$('#'+gridId).datagrid('reload');
				}else{
					$('#'+gridId).treegrid('reload');
				}
			}else{
				$.messager.show({title:'错误提示',msg:result.msg,timeout:2000,showType:'slide'});
			}
			$('#'+dialogId).dialog("close");
			if(formClearRest=='reset'){
				$('#'+formId).form('reset');
			}else{
				$('#'+formId).form('clear');
			}
		})
	},
	/**
	 * 删除一行数据 
	 * gridId datagrid、treegrid数据表格ID
	 * url 提交后台处理的url
	 * primarykeyName 选中行数据的主键名
	 */
	delete : function(gridId,url,primarykeyName) {
		if(gridId.substr(0,2)=="dg"){
			var rows=$('#'+gridId).datagrid('getSelections');
			if(rows.length!=1){
				$.messager.alert('系统提示','请先选择一行数据！');
				return;
			}
			var row=rows[0];
		}else{
			var rows=$('#'+gridId).treegrid('getSelections');
			if(rows.length!=1){
				$.messager.alert('系统提示','请先选择一行数据！');
				return;
			}
			var row=rows[0];
		}
		$.messager.confirm('确定操作', '您确定要删除选择的数据吗？', function (flag) {
			if (flag){
				$.post(url,{id:row[primarykeyName]},function(result){
					result = eval('(' + result + ')');
					if(!result.flag){
						$.messager.show({title:'错误提示',msg:result.msg,timeout:2000,showType:'slide'});
					}else{
						if(gridId.substr(0,2)=="dg"){
							$('#'+gridId).datagrid('reload');
						}else{
							$('#'+gridId).treegrid('reload');
						}
						$.messager.show({title:'成功提示',msg:result.msg,timeout:1000,showType:'slide'});
					}
				}) 
			}
		});
	},
	/**
	 * 刷新数据表格的内容
	 * gridId datagrid、treegrid数据表格ID
	 */
	reload : function(gridId) {
		if(gridId.substr(0,2)=="dg"){
			$('#'+gridId).datagrid('reload');
		}else{
			$('#'+gridId).treegrid('reload');
		}
	},
	/**
	 * 使用url提交待修改的数据或分页查询，并更新结果集
	 * gridId datagrid、treegrid数据表格ID
	 * url 提交后台处理的url
	 */
	urlReload : function(gridId,url) {
		$.post(url,{},function(rs){
			var result = eval('(' + rs + ')');
			if(!result.flag){
				$.messager.show({title:'错误提示',msg:result.msg,timeout:2000,showType:'slide'});
			}else{
				if(gridId!="" && gridId!=null && gridId!=undefined){
					if(gridId.substr(0,2)=="dg"){
						$('#'+gridId).datagrid('reload');
					}else{
						$('#'+gridId).treegrid('reload');
					}
				}
				$.messager.show({title:'成功提示',msg:result.msg,timeout:1000,showType:'slide'});
			}
		})
	},
	/**
	 * 使用form提交待修改的数据，并更新结果集
	 * gridId datagrid、treegrid数据表格ID
	 * formId form表单ID
	 */
	formReload : function(gridId,formId) {
		$.post($('#'+formId).attr("action"), $('#'+formId).serialize(), function(rs) {
			var result = eval('(' + rs + ')');
			if(!result.flag){
				$.messager.show({title:'错误提示',msg:result.msg,timeout:2000,showType:'slide'});
			}else{
				if(gridId!="" && gridId!=null && gridId!=undefined){
					if(gridId.substr(0,2)=="dg"){
						$('#'+gridId).datagrid('reload');
					}else{
						$('#'+gridId).treegrid('reload');
					}
				}
				$.messager.show({title:'成功提示',msg:result.msg,timeout:1000,showType:'slide'});
			}
		})
	}
}

lazy.ajax = {
	/**
	 * 使用post方式提交ajax请求，返回JSON格式数据 { "result": { "flag": "true","msg": "this is a message."}}
	 * @param url
	 * @param data    var data = {id:"1" ,name:"admin"};
	 * @param _func   function(){//数据返回成功后，要执行的方法。}
	 * 示例： 
	 *		lazy.ajax.useUrl('system/user/save',{id:'1',name:'admin'},function(){
	 *			//数据返回成功后，要执行的方法。
	 *		});
	 */
	useUrl : function(url,data,_func){
		$.post(url,data,function(rs){
			var result = eval('(' + rs + ')');
			if(result.flag){
				$.messager.show({title:'成功提示',msg:result.msg,timeout:1000,showType:'slide'});
				_func(result);
			}else{
				$.messager.show({title:'错误提示',msg:result.msg,timeout:2000,showType:'slide'});
			}
		},'json'
		);
	},
	/**
	 * 使用form方式提交ajax请求，返回JSON格式数据  { "result": { "flag": "true","msg": "this is a message."}}
	 * @param formId
	 * @param _func   function(){//数据返回成功后，要执行的方法。}
	 * 示例： 
	 *		lazy.ajax.useForm('fm-system-user-save',function(){
	 *			//数据返回成功后，要执行的方法。
	 *		});
	 */
	useForm : function(formId,_func){
		$.post($('#'+formId).attr("action"), $('#'+formId).serialize(), function(rs) {
			var result = eval('(' + rs + ')');
			if(result.flag){
				$.messager.show({title:'成功提示',msg:result.msg,timeout:1000,showType:'slide'});
				_func();
			}else{
				$.messager.show({title:'错误提示',msg:result.msg,timeout:2000,showType:'slide'});
			}
			},'json'
		);
	}
}

lazy.kit = {
	/**
     *	获取项目的URL地址 
	 */
	getBaseUrl : function() {
		//获取当前网址，如： http://localhost:8083/share/index.jsp
		var curWwwPath=window.document.location.href;
		//获取主机地址之后的目录，如： /share/meun.jsp
		var pathName=window.document.location.pathname;
		var pos=curWwwPath.indexOf(pathName);
		//获取主机地址，如： http://localhost:8083
		var localhostPath=curWwwPath.substring(0,pos);
		//获取带"/"的项目名，如：/share
		var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
		return (localhostPath + projectName);
	},
	/**
	 * 获取时间戳
	 */
	getTimeStamp : function(){
		return Date.parse(new Date()); 
	},
	getDate : function(dateStr){
		var now ;
		if (dateStr=="" || dateStr==undefined){
			now = new Date();
		}else{
			now = new Date(dateStr);
		}
		y = now.getFullYear(), m = now.getMonth()+1,d = now.getDate();
		if (m < 10)
			m = "0" + m;
		if (d < 10)
			d = "0" + d;
		return (y + "-" + m + "-" + d);
	},
	getFullDate : function(dateStr) {
		var now ;
		if (dateStr=="" || dateStr==undefined){
			now = new Date();
		}else{
			now = new Date(dateStr);
		}
		y = now.getFullYear(), 
		m = now.getMonth()+1, 
		w = now.getDay(), 
		d = now.getDate();
		if (m < 10)
			m = "0" + m;
		if (d < 10)
			d = "0" + d;
		return (y + "年" + m + "月" + d + "日");
	},
	getFullDateWeek : function(dateStr) {  
		var now ;
		if (dateStr=="" || dateStr==undefined){
			now = new Date();
		}else{
			now = new Date(dateStr);
		}
		y = now.getFullYear(), 
		m = now.getMonth()+1, 
		w = now.getDay(), 
		d = now.getDate();
		if (m < 10)
			m = "0" + m;
		if (d < 10)
			d = "0" + d; 
		var day = new Array();  
		day[0] = "星期日";  
		day[1] = "星期一";  
		day[2] = "星期二";  
		day[3] = "星期三";  
		day[4] = "星期四";  
		day[5] = "星期五";  
		day[6] = "星期六";  
		return( y + '年' + m + '月'+ d +'日 '+day[now.getDay()]);  
	},
	getFirstDate : function() {
		var now = new Date();
		var year = now.getFullYear();
		return year+"-01-01";
	},
	getMonthFirstDate : function() {
		var now = new Date();
		var year = now.getFullYear();
		var month = now.getMonth()+1;
		if (month<10) {
			month = "0" + month;
		}
		return year+"-"+month+"-01";
	},
	getMonthLastDate : function() {
		var now = new Date();
		var year = now.getFullYear();
		var month = now.getMonth()+1;
		if (month<10) {
			month = "0" + month;
		}
		myDate = new Date(year,month,0);
		return year+"-"+month+"-"+myDate.getDate();
	},
	getLastDate : function() {
		var now = new Date();
		var year = now.getFullYear();
		return year+"-12-31";
	},
	getDateTime : function() {
		var now = new Date(), yyyy = now.getFullYear(), mm = now.getMonth()+1, dd = now
				.getDate(), hh = now.getHours(), nn = now.getMinutes(), ss = now
				.getSeconds();

		if (mm < 10)
			mm = "0" + mm;
		if (dd < 10)
			dd = "0" + dd;
		if (hh < 10)
			hh = "0" + hh;
		if (nn < 10)
			nn = "0" + nn;
		if (ss < 10)
			ss = "0" + ss;
		return yyyy + "-" + mm + "-" + dd + " " + hh + ":" + nn + ":" + ss;
	},
	getTime : function(){
		var now = new Date(), h = now.getHours(), m = now.getMinutes(), s = now.getSeconds();
		if (h < 10)
			h = "0" + h;
		if (m < 10)
			m = "0" + m;
		if (s < 10)
			h = "0" + s;
		return (h + ":" + m + ":" + s);
	},
	
	/**
	 * 去除字符串两边的空格
	 * @param str
	 */
	trim: function(str) {
		return str.replace(/^(\s|\u00A0)+/,'').replace(/(\s|\u00A0)+$/,''); 
	},
				
	/**
	 * 获取文件的后缀名
	 * @param {Object} fileName
	 */
	getFileExt : function(fileName) {
		if(fileName.lastIndexOf(".") == -1)
			return fileName;
		var pos = fileName.lastIndexOf(".") + 1;
		return fileName.substring(pos, fileName.length).toLowerCase();
	},

	/**
	 * 获取文件名称
	 * @param {Object} fileName
	 */
	getFileName: function(fileName) {
		var pos = fileName.lastIndexOf(".");
		if(pos == -1) {
			return fileName;
		} else {
			return fileName.substring(0, pos);
		}
	},
	/*验证是否为图片*/
	isImage: function(fileName) {
		return /(gif|jpg|jpeg|png|GIF|JPG|PNG)$/ig.test(fileName);
	},

	/*验证是否为音频*/
	isAudio: function(fileName) {
		return /(mp3|wma|wav|acc|flac)$/ig.test(fileName);
	},
	
	/*验证是否为视频*/
	isVideo: function(fileName) {
		return /(mp4|mkv|mpg|flv|f4v|rmvb|avi)$/ig.test(fileName);
	},

	/*验证是否为Office文档*/
	isOffice: function(fileName) {
		return /(doc|docx|xls|xlsx|pdf|txt|ppt|pptx)$/ig.test(fileName);
	},
	
	/*验证是否为Office文档和图片*/
	isOfficeAndImage: function(fileName) {
		return /(doc|docx|xls|xlsx|pdf|txt|ppt|pptx|gif|jpg|jpeg|png|GIF|JPG|PNG)$/ig.test(fileName);
	},
	
	/*验证是否为自定义格式的文档*/
	isDocument: function(format, fileName) {
		var reg = new RegExp("("+format+")","i","g");
		return reg.test(fileName);
	},
	
	/*验证是否为手机号码*/
	isMobile : function(shouji){
		var reg=/^(?:13\d|15\d|18\d)\d{5}(\d{3}|\*{3})$/;
		return reg.test(shouji);  
	},

	/*验证是否为电话号码*/
	isPhone : function(zuoji){
		var reg=/^((0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;
		return reg.test(zuoji); 
	},
	
	/*验证是否为手机短号*/
	isDh : function(dianhua){
		var reg=/^\d{6}$/;   //验证短号(6位数字)
		return reg.test(dianhua);
	},
	/*验证是否为身份证号码*/
	isIdCard: function(str) {
		var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/; 
		return reg.test(str);
	},
	/*验证是否为16位或19位银行卡或信用卡号*/
	isBankCard: function(str) {
		var reg = /^\d{16}|\d{19}$/;
		return reg.test(str);
	},

	/*验证是否为数字*/
	isNum : function(num){ 
		var reg = /^\d+(\.\d+)?$/;
		return reg.test(num);
	},
	
	/*验证是否为整数*/
	isInt: function(str) {
		var reg = /^-?\d+$/; 
		return reg.test(str);
	},
	
	/*验证是否为中文汉字*/
	isChinese: function(str) {
		var reg = /^([\u4e00-\u9fa5]|[\ufe30-\uffA0])*$/;
		return reg.test(str)
	},
	isBlank : function(value){
		if(value==null||value==undefined||String(value).replace(/(^\s*)|(\s*$)/g,"").length<=0){
			return true;
		}else{
			return false;
		}
	},
	notBlank : function(value){
		if(value==null||value==undefined||String(value).replace(/(^\s*)|(\s*$)/g,"").length<=0){
			return false;
		}else{
			return true;
		}
	},
		///////////////////////////////////////判断浏览器类型构造函数开始/////////////////////////////////////
	/**
	 * 浏览器类型判断
	 */
    getBrowserType : function() {
        var Browser_Name = navigator.appName;
        var Browser_Version = parseFloat(navigator.appVersion);
        var Browser_Agent = navigator.userAgent;
        var Actual_Version, Actual_Name;
        var is_IE = (Browser_Name == "Microsoft Internet Explorer"); //判读是否为ie浏览器
        var is_NN = (Browser_Name == "Netscape"); //判断是否为netscape浏览器
        var is_op = (Browser_Name == "Opera"); //判断是否为Opera浏览器
        if(is_NN) {
            //upper 5.0 need to be process,lower 5.0 return directly
            if(Browser_Version >= 5.0) {
                if(Browser_Agent.indexOf("Netscape") != -1) {
                    var Split_Sign = Browser_Agent.lastIndexOf("/");
                    var Version = Browser_Agent.lastIndexOf(" ");
                    var Bname = Browser_Agent.substring(0, Split_Sign);
                    var Split_sign2 = Bname.lastIndexOf(" ");
                    Actual_Version = Browser_Agent.substring(Split_Sign + 1, Browser_Agent.length);
                    Actual_Name = Bname.substring(Split_sign2 + 1, Bname.length);
                }
                if(Browser_Agent.indexOf("Firefox") != -1) {
                    var Version = Browser_Agent.lastIndexOf("Firefox");
                    Actual_Version = Browser_Agent.substring(Version + 8, Browser_Agent.length);
                    Actual_Name = Browser_Agent.substring(Version, Version + 7);
                }
                if(Browser_Agent.indexOf("Safari") != -1) {
                    if(Browser_Agent.indexOf("Chrome") != -1) {
                        var Split_Sign = Browser_Agent.lastIndexOf(" ");
                        var Version = Browser_Agent.substring(0, Split_Sign);;
                        var Split_Sign2 = Version.lastIndexOf("/");
                        var Bname = Version.lastIndexOf(" ");
                        Actual_Version = Version.substring(Split_Sign2 + 1, Version.length);
                        Actual_Name = Version.substring(Bname + 1, Split_Sign2);
                    } else {
                        var Split_Sign = Browser_Agent.lastIndexOf("/");
                        var Version = Browser_Agent.substring(0, Split_Sign);;
                        var Split_Sign2 = Version.lastIndexOf("/");
                        var Bname = Browser_Agent.lastIndexOf(" ");
                        Actual_Version = Browser_Agent.substring(Split_Sign2 + 1, Bname);
                        Actual_Name = Browser_Agent.substring(Bname + 1, Split_Sign);
                    }
                }
                if(Browser_Agent.indexOf("Trident") != -1) {
                    Actual_Version = Browser_Version;
                    Actual_Name = Browser_Name;
                }
            } else {

                Actual_Version = Browser_Version;
                Actual_Name = Browser_Name;
            }
        } else if(is_IE) {
            var Version_Start = Browser_Agent.indexOf("MSIE");
            var Version_End = Browser_Agent.indexOf(";", Version_Start);
            Actual_Version = Browser_Agent.substring(Version_Start + 5, Version_End)
            Actual_Name = Browser_Name;

            if(Browser_Agent.indexOf("Maxthon") != -1 || Browser_Agent.indexOf("MAXTHON") != -1) {
                var mv = Browser_Agent.lastIndexOf(" ");
                var mv1 = Browser_Agent.substring(mv, Browser_Agent.length - 1);
                mv1 = "遨游版本:" + mv1;
                Actual_Name += "(Maxthon)";
                Actual_Version += mv1;
            }
        } else if(Browser_Agent.indexOf("Opera") != -1) {
            Actual_Name = "Opera";
            var tempstart = Browser_Agent.indexOf("Opera");
            var tempend = Browser_Agent.length;
            Actual_Version = Browser_Version;
        } else {
            Actual_Name = "Unknown Navigator"
            Actual_Version = "Unknown Version"
        }
        /*------------------------------------------------------------------------------
         --Your Can Create new properties of navigator(Acutal_Name and Actual_Version) --
         --Userage:                                                                     --
         --1,Call This Function.                                                        --
         --2,use the property Like This:navigator.Actual_Name/navigator.Actual_Version;--
         ------------------------------------------------------------------------------*/
        navigator.Actual_Name = Actual_Name;
        navigator.Actual_Version = Actual_Version;

        /*---------------------------------------------------------------------------
         --Or Made this a Class.                                                     --
         --Userage:                                                                  --
         --1,Create a instance of this object like this:var browser=new browserinfo;--
         --2,user this instance:browser.Version/browser.Name;                        --
         ---------------------------------------------------------------------------*/
        this.Name = Actual_Name;
        this.Version = Actual_Version;
        this.isFirefox = function() {
            if(Actual_Name.indexOf("Firefox") == -1)
                return false;
            else
                return true;
        }
        this.isSafari = function() {
            if(Actual_Name.indexOf("Safari") == -1)
                return false;
            else
                return true;
        }
        this.isChrome = function() {
                if(Actual_Name.indexOf("Chrome") == -1)
                    return false;
                else
                    return true;
            }
            //判读是否为ie浏览器
        this.isIE = function() {
                if(Browser_Name == "Microsoft Internet Explorer")
                    return false;
                else
                    return true;
            }
            //判读是否为ie6浏览器
        this.isCurrIE6 = function() {
                if(Browser_Agent.toLowerCase().indexOf("msie 6.0") != -1)
                    return false;
                else
                    return true;
            }
            //判读是否为ie7浏览器
        this.isCurrIE6 = function() {
                if(Browser_Agent.toLowerCase().indexOf("msie 7.0") != -1)
                    return false;
                else
                    return true;
            }
            //判读是否为ie8浏览器
        this.isCurrIE9 = function() {
                if(Browser_Agent.toLowerCase().indexOf("msie 8.0") != -1)
                    return false;
                else
                    return true;
            }
            //判读是否为ie9浏览器
        this.isCurrIE10 = function() {
                if(Browser_Agent.toLowerCase().indexOf("msie 9.0") != -1)
                    return false;
                else
                    return true;
            }
            //判读是否为ie11浏览器
        this.isCurrIE11 = function() {
            if((Browser_Name == "Netscape") && (parseFloat(Browser_Version) >= 5.0) && (Browser_Agent.indexOf("Trident") != -1))
                return false;
            else
                return true;
        }
		return Browser_Name;
    }
    ///////////////////////////////////////判断浏览器类型构造函数结束///////////////////////
}

