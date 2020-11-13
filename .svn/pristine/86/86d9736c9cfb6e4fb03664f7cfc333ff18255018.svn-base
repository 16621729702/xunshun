<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>欢迎登录-滴雨轩-后台管理系统</title>
		<!--head-->
		<link rel="stylesheet" type="text/css" href="${request.contextPath}/assets/themes/easyui-gray/easyui.css">
		<link rel="stylesheet" type="text/css" href="${request.contextPath}/assets/plugin/easyui/1.7.0/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="${request.contextPath}/assets/plugin/easyui/1.7.0/themes/color.css">
		<link rel="stylesheet" type="text/css" href="${request.contextPath}/assets/css/common.css">
		<link rel="stylesheet" type="text/css" href="${request.contextPath}/assets/css/login.css">
		<link href="${request.contextPath}/web/css/bootstrap.css" rel="stylesheet">
		<link href="${request.contextPath}/web/font-awesome/css/font-awesome.css" rel="stylesheet">
		<link href="${request.contextPath}/lib/Hui/css/iconfont.css" rel="stylesheet">
		<link href="${request.contextPath}/web/layui/css/layui.css?v=2212" rel="stylesheet">
		<script type="text/javascript" src="${request.contextPath}/assets/plugin/easyui/1.7.0/jquery.min.js"></script>
		<script type="text/javascript" src="${request.contextPath}/assets/plugin/easyui/1.7.0/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${request.contextPath}/assets/plugin/easyui/1.7.0/locale/easyui-lang-zh_CN.js"></script>
		<script src="${request.contextPath}/web/layui/layui.js"></script>
		<!--head end-->
		<!--login-->
		<!--login end-->
	</head>
	<body>
		<div class="login-register-bg">
			<div class="login-register-form">
				<header class="login-login1-header">
					<h1>欢迎登录-滴雨轩-后台管理系统</h1>
				</header>
				<form id="loginform" class="easyui-form" method="post"  data-options="novalidate:true">
					<div class="login-register-body login-login1-body">
						<div class="login-register-input-warpper">
							<input type="text" class="login-register-input easyui-validatebox" data-options="required:true,validType:'length[2,20]'" name="username" id="username" placeholder="请输入用户名" tabindex="1">
						</div>
						<div class="login-register-input-warpper">
							<input type="password" class="login-register-input easyui-validatebox" data-options="required:true" class="crm-register-input easyui-validatebox" name="password" id="password" placeholder="请输入密码" tabindex="1" onfocus="">
						</div>
<#--						<div class="login-register-input-warpper">-->
<#--							<input type="text" class="login-register-input easyui-validatebox" data-options="required:true,validType:'length[4,4]'" name="randomCode" id="randomCode" placeholder="请输入验证码" tabindex="3">-->
<#--						</div>-->
<#--						<div class="login-register-input-warpper">-->
<#--							<input type="button" class="login-register-input easyui-validatebox"  id="code_box" onclick="refreshCode()">-->
<#--						</div>-->
						<div class="login-register-footer">
							<a href="javascript:login();" class="pg-btn-submit dinline-block " tabindex="3" act="login_btn">登&nbsp;&nbsp;&nbsp;&nbsp;录</a>
						</div>
					</div>
				</form>
			</div>
		</div>

		<script type="text/javascript">
			window.onload=function (){
				refreshCode();
			}
			layui.use('layer', function(){
				var layer = layui.layer;
			});
			function login() {
				if ($("#username").val().length==0){
					layer.open({
						title: '警告',
						content: "操作提示,账号不能为空！"
					});
					return false;
				}
				if ($("#password").val().length==0){
					layer.open({
						title: '警告',
						content: "操作提示，密码不能为空！"
					});
					return false;
				}
				// if ($("#randomCode").val().length==0){
				// 	$.messager.alert("操作提示", "验证码不能为空！","error");
				// 	return false;
				// }else{
				// 	if($("#randomCode").val().toLocaleLowerCase()!=$("#code_box").val().toLocaleLowerCase()){
				// 		$.messager.alert("操作提示", "验证码错误！","error");
				// 		refreshCode();
				// 		return false;
				// 	}
				// }
				$.ajax({
					url:"${request.contextPath}/logincheck",
					data:$("#loginform").serialize(),
					dataType:"json",
					success:function(result){
						if(result.code == 200){
							location.href = "${request.contextPath}/index";
						}else{
							layer.open({
								title: '警告',
								content: result.msg
							});
						}
					}});
			}
		</script>

		<script>
			var code_box = document.getElementById("code_box");
			function refreshCode() {
				//62个字符 随机选择4位
				var code = '0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM',
						char = '',
						result = '';
				for (var i = 0; i < 4; i++) {
					//随机选择一位  （0,61） 写出0到61的随机的索引数字
					var code_index = Math.round(Math.random()*61);
					//得到随机的索引  取出随机地字符
					var char = code[code_index];
					//随机取出的字符 存在几个相同重复的问题 ，而且对于字母，不能区分大小写。
					// 避免重复的思路是：取出字符之后,和最后的result对比一下，看看里边是不是已经存在了，如果存在本次循环就终止，进行下一次
					if (result.toUpperCase().indexOf(char.toUpperCase()) > -1)
							//indexOf() == -1 说明结果里边没有要找的字符 那么 > -1 就是 里边有重复的字符
					{
						i --;
						//为什么会 --？ 因为如果条件成立，那么本轮循环就结束进行下一轮循环（自然i就加1了），那么本轮本应该取出的字符就没有了
						//到最后会少一个字符 缺席
						continue;//终止本轮循环 进行下一轮
					}
					result += char;
				}
				$("#code_box").val(result);
			}
		</script>
	</body>
</html>
