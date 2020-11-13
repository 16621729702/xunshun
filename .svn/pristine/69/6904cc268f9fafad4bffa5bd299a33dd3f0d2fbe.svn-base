
<!DOCTYPE html>
<html>

	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>后台管理 |  </title>
		<meta name="csrf-token" content="lMY8vAepkU6MEKxKzJmV5tdU6RcR7dkvumVs61Li">
		<meta name="request-type" content="alert">
		<link href="${request.contextPath}/web/css/bootstrap.css" rel="stylesheet">
		<link href="${request.contextPath}/web/font-awesome/css/font-awesome.css" rel="stylesheet">
		<link href="${request.contextPath}/lib/Hui/css/iconfont.css" rel="stylesheet">
		<link href="${request.contextPath}/web/layui/css/layui.css?v=2212" rel="stylesheet">
		<link href="${request.contextPath}/web/css/animate.css" rel="stylesheet">
		<link href="${request.contextPath}/web/css/style.css?v=12223" rel="stylesheet">
		<link href="${request.contextPath}/web/css/main.css?v=666" rel="stylesheet">
		<link href="${request.contextPath}/lib/toastr/toastr.min.css" rel="stylesheet">
		<link href="${request.contextPath}/web/we7/resource/css/common.css" rel="stylesheet">
		<link href="${request.contextPath}/web/css/fix.css?v=121" rel="stylesheet">

		</script>
		<!-- Mainly scripts -->
		<script src="${request.contextPath}/web/js/jquery-1.11.1.min.js"></script>
		<script src="${request.contextPath}/web/js/popper.min.js"></script>
		<script src="${request.contextPath}/web/js/bootstrap.min.js"></script>
		<script src="${request.contextPath}/web/js/plugins/metisMenu/jquery.metisMenu.js"></script>
		<script src="${request.contextPath}/web/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
		<script src="${request.contextPath}/web/js/plugins/iCheck/icheck.min.js"></script>
		<!-- Custom and plugin javascript -->
		<script src="${request.contextPath}/web/js/inspinia.js"></script>
		<script src="${request.contextPath}/web/js/plugins/pace/pace.min.js"></script>
		<script src="${request.contextPath}/web/js/jquery.form.min.js"></script>
		<script src="${request.contextPath}/web/layui/layui.js"></script>
		<script src="${request.contextPath}/lib/toastr/toastr.min.js"></script>
		<script>
			$(document).ready(function () {
				layui.use(["layer", "form"], function () {
					var form = layui.form;
					var layer = layui.layer;
					var form = layui.form, $ = layui.jquery;
					form.render();
				});
				$('.sub-navbar-ul').attr('style', "max-height:" + document.body.clientHeight + "px;    overflow-y: auto;");
			});
		</script>
		<script src="${request.contextPath}/web/js/common.js?v=1.0.2"></script>
		<script src="${request.contextPath}/web/js/myconfig.js"></script>        </head>
	<body class="iframe-body">
		<div id="wrapper">
			<div class="col-lg-12">
				<div class="ibox">
					<div class="ibox-content">
						<table class="layui-table">
							<thead>
							<tr>
								<th>ID</th>
								<th>操作记录</th>
								<th>操作时间</th>
							</tr>
							</thead>
							<tbody>
							<#if (orderLogList)??>
							<#if (orderLogList?size > 0)>
							<#list orderLogList as list>
							<tr>
								<td>${list.id!}</td>
								<td>
									<pre>${list.operate!}</pre>
								</td>
								<td>${list.operatedate?string('yyyy-MM-dd HH:mm:ss')!}</td>
							</tr>
							</#list>
							</#if>
							</#if>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
