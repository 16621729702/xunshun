$(function() {
	//去掉加载页面时，遮挡的div
	$("body").css("visibility", "visible");
	//选项卡
	window.mainTabs = $('#main-tabs').tabs({
		fit : true,
		border : false,
		tools : "#tabTools"
	});
	//菜单树
	window.menuTree = $("#menu-tree").tree({
		animate:true,
		lines:true,
		onBeforeSelect: treeBeforeSelect, 
		onSelect: treeSelect
	});
});

/**
 * 菜单事件
 */
function treeBeforeSelect(node) {
    if(node.children && node.children.length > 0) {
        if(node.state == "open") {
            $(this).tree("collapse", node.target);
        }else {
            $(this).tree("expand", node.target);
        }
        return false;
    } 
}
/**
 * 菜单事件
 */
function treeSelect(node) {
	addMainTab(node.text, node.url, node.iframe);
}
/**
 * 增加一个主窗口
 * iframe为true时，加载整个jsp页面；
 * iframe为false时，只加载<body></body>元素中的div片段，但须保证所有页面元素的ID标签唯一，对代码编写的质量要求也高
 */
function addMainTab(text, url, iframe) {
	if(!mainTabs.tabs("exists", text)) {
		if(iframe){
			mainTabs.tabs('add',{
			    title: text,
			    content:'<iframe src="' + url + '" class="easyui-panel" data-options="fit:true,border:false" frameborder="0"></iframe>',
			    closable:true
			});
		}else{
			mainTabs.tabs('add',{
			    title: text,
			    href:url,
			    closable:true
			});
		}
    }else {
    	mainTabs.tabs("select", text);
    	//刷新
    	mainTabs.tabs('getSelected').panel('panel').find('iframe').attr("src", url);
    }
}
/**
 * 全屏
 */
function fullScreen() {
	if($("#fullScreen").find(".fa-compress").length > 0) {
		$("#layout").layout('expand', 'west').layout('expand', 'north');
		$("#fullScreen").find(".l-btn-icon").addClass("fa-arrows-alt").removeClass("fa-compress");
	}else {
		$("#layout").layout('collapse', 'north').layout('collapse', 'west');
		$("#fullScreen").find(".l-btn-icon").addClass("fa-compress").removeClass("fa-arrows-alt");
		$(".layout-expand").hide();
	}
}