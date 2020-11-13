
<!DOCTYPE html>
<html>

	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>后台管理
		</title>
		<meta name="csrf-token" content="mSPHGwm8smQiNFoE6dtoDhURnRKh3anEBq9CWFgq">
		<link href="${request.contextPath}/web/css/bootstrap.css" rel="stylesheet">
		<link href="${request.contextPath}/web/font-awesome/css/font-awesome.css"
			  rel="stylesheet">
		<link href="${request.contextPath}/lib/Hui/css/iconfont.css" rel="stylesheet">
		<link href="${request.contextPath}/web/layui/css/layui.css?v=2212"
			  rel="stylesheet">
		<link href="${request.contextPath}/web/css/animate.css" rel="stylesheet">
		<link href="${request.contextPath}/web/css/style.css?v=12223" rel="stylesheet">
		<link href="${request.contextPath}/web/css/main.css?v=666" rel="stylesheet">
		<link href="${request.contextPath}/lib/toastr/toastr.min.css" rel="stylesheet">
		<link href="${request.contextPath}/web/we7/resource/css/common.css"
			  rel="stylesheet">
		<link href="${request.contextPath}/web/css/fix.css?v=121" rel="stylesheet">

		<script src="${request.contextPath}/web/js/jquery-3.1.1.min.js"></script>
		<script src="${request.contextPath}/web/js/popper.min.js"></script>
		<script src="${request.contextPath}/web/js/bootstrap.min.js"></script>
		<script src="${request.contextPath}/web/js/plugins/metisMenu/jquery.metisMenu.js"></script>
		<script src="${request.contextPath}/web/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
		<script src="${request.contextPath}/web/js/inspinia.js"></script>
		<script src="${request.contextPath}/web/js/plugins/echarts/echarts.common.min.js"></script>
		<script src="${request.contextPath}/web/js/plugins/metisMenu/jquery.metisMenu.js"></script>
		<script src="${request.contextPath}/web/js/plugins/echarts/theme/macarons.js"></script>
		<script src="${request.contextPath}/web/js/plugins/echarts/theme/westeros.js"></script>
		<script src="${request.contextPath}/lib/toastr/toastr.min.js"></script>
		<script src="${request.contextPath}/web/js/require.js"></script>
		<script>
			$(document).ready(function(){
				$("#left").load("${request.contextPath}/left");
				$(".footer").load("${request.contextPath}/footer");
			})
		</script>
	</head>

	<body>


		<div id="wrapper">
			<!--左侧导航栏-->
			<nav class="navbar-default navbar-static-side" role="navigation" id="left"></nav>
			<div id="page-wrapper" class="gray-bg">
				<div class="wrapper wrapper-content animated fadeInRight">
					<div class="row">
						<div class="col-lg-3">
							<div class="ibox ">
								<div class="ibox-title">
									<h5>订单总数</h5>
								</div>
								<div class="ibox-content">
									<h1 class="no-margins">
										<a href="${request.contextPath}/orders/orderquery">
											${allnum!}
										</a>
									</h1>
									<small>总数</small>
								</div>
							</div>
						</div>
						<div class="col-lg-3">
							<div class="ibox ">
								<div class="ibox-title">
									<h5>待发货</h5>
								</div>
								<div class="ibox-content">
									<h1 class="no-margins">
										<a href="${request.contextPath}/orders/waitgetlist">
											${waitsendnum!}
										</a>
									</h1>
									<small>订单数量</small>
								</div>
							</div>
						</div>
						<div class="col-lg-3">
							<div class="ibox ">
								<div class="ibox-title">
									<h5>待收货</h5>
								</div>
								<div class="ibox-content">
									<h1 class="no-margins">
										<a href="${request.contextPath}/orders/waitgetlist">
											${waitgetnum!}
										</a>
									</h1>
									<small>订单数量</small>
								</div>
							</div>
						</div>
						<div class="col-lg-3">
							<div class="ibox ">
								<div class="ibox-title">
									<h5>待付款</h5>
								</div>
								<div class="ibox-content">
									<h1 class="no-margins">
										<a href="${request.contextPath}/orders/waitpaylist">
											${waitpaynum!}
										</a>
									</h1>
									<small>订单数量</small>
								</div>
							</div>
						</div>
						<div class="col-lg-3">
							<div class="ibox ">
								<div class="ibox-title">
									<h5>已完成</h5>
								</div>
								<div class="ibox-content">
									<h1 class="no-margins">
										<a href="${request.contextPath}/orders/successlist">
											${successnum!}
										</a>
									</h1>
									<small>订单数量</small>
								</div>
							</div>
						</div>
						<div class="col-lg-3">
							<div class="ibox ">
								<div class="ibox-title">
									<h5>已关闭</h5>
								</div>
								<div class="ibox-content">
									<h1 class="no-margins">
										<a href="${request.contextPath}/orders/closedlist">
											${closednum!}
										</a>
									</h1>
									<small>订单数量</small>
								</div>
							</div>
						</div>
					</div>

				</div>
				<div class="footer"></div></div>
		</div>


	</body>
</html>
