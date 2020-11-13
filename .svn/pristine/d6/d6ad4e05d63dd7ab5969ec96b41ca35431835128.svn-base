
<!DOCTYPE html>
<html>

	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>后台管理
		</title>
		<meta name="csrf-token" content="lMY8vAepkU6MEKxKzJmV5tdU6RcR7dkvumVs61Li">
		<meta name="request-type" content="page">
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
		<link href="${request.contextPath}/web/css/plugins/sweetalert/sweetalert.css?v=33" rel="stylesheet">
		<style>
			.step-region{position:relative;margin-bottom:10px}.font18{font-size:18px}.ui-step{padding:14px 0 8px 0;zoom:1;margin:0 50px}.ui-step:after{content:"";display:table;clear:both}.ui-step li{float:left;position:relative;-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;text-align:center}.ui-step li::before,.ui-step li::after{position:absolute;left:0;top:27px;display:block;content:' ';width:50%;height:4px;background:#f2f3f7;z-index:1}.ui-step li::after{left:50%}.ui-step li:first-child::before{width:0}.ui-step li:last-child::after{width:0}.ui-step .ui-step-title{color:#333;font-size:12px;line-height:18px}.ui-step .ui-step-number{position:relative;display:inline-block;width:37px;height:37px;margin:10px 0;line-height:37px;background:#f2f3f7;color:#cac9c9;border-radius:100%;-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;z-index:2}.ui-step li.ui-step-done .ui-step-number{color:#fff}.ui-step .ui-step-meta{color:#ccc}.ui-step li.ui-step-done::before,.ui-step li.ui-step-done::after,.ui-step li.ui-step-done .ui-step-number{background:#54c952}.ui-step-2 li{width:50%}.ui-step-3 li{width:33.33333%}.ui-step-4 li{width:24.9999%}.ui-step-5 li{width:20%}.panel{padding:0;}.panel-default > .panel-heading{color:#333;background-color:#f5f5f5;border-color:#ddd;}.panel .panel-body{padding:15px;}.row{margin-top:15px;}
		</style>
		<script type="text/javascript">
			if(navigator.appName == 'Microsoft Internet Explorer'){
				if(navigator.userAgent.indexOf("MSIE 5.0")>0 || navigator.userAgent.indexOf("MSIE 6.0")>0 || navigator.userAgent.indexOf("MSIE 7.0")>0) {
					alert('您使用的 IE 浏览器版本过低, 推荐使用 Chrome 浏览器或 IE8 及以上版本浏览器.');
				}
			}
		</script>
	</head>
	<body>
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
				$("#left").load("${request.contextPath}/left");
				$(".footer").load("${request.contextPath}/footer");
				$('.sub-navbar-ul').attr('style', "max-height:" + document.body.clientHeight + "px;    overflow-y: auto;");
			});
		</script>
		<script src="${request.contextPath}/web/js/common.js?v=1.0.2"></script>
		<script src="${request.contextPath}/web/js/myconfig.js"></script>
		<div id="wrapper">
			<nav class="navbar-default navbar-static-side" role="navigation" id="left"></nav>
			<div id="page-wrapper" class="gray-bg">
				<div class="wrapper wrapper-content animated fadeInRight">
					<div class="row">
						<div class="col-lg-12">
							<div class="ibox">
								<div class="ibox-content">
									<div class="step-region">
										<ul class="ui-step ui-step-4">
											<li  ${(order.status=='未支付')?string('class="ui-step-done"','')} ${(order.status=='待发货')?string('class="ui-step-done"','')}  ${(order.status=='待收货')?string('class="ui-step-done"','')}  ${(order.status=='已完成')?string('class="ui-step-done"','')}>
												<div class="ui-step-number">1</div>
												<div class="ui-step-title">买家下单</div>
												<div class="ui-step-meta">
													${order.createtime!}
												</div>
											</li>
											<li  ${(order.status=='待发货')?string('class="ui-step-done"','')}  ${(order.status=='待收货')?string('class="ui-step-done"','')}  ${(order.status=='已完成')?string('class="ui-step-done"','')}>
												<div class="ui-step-number">2</div>
												<div class="ui-step-title">买家付款</div>
												<div class="ui-step-meta">
													${order.paytime!}
												</div>
											</li>
											<li  ${(order.status=='待收货')?string('class="ui-step-done"','')}  ${(order.status=='已完成')?string('class="ui-step-done"','')}>
												<div class="ui-step-number">3</div>
												<div class="ui-step-title">
													商家发货
												</div>
												<div class="ui-step-meta">
													${order.deliverytime!}
												</div>
											</li>
											<li  ${(order.status=='已完成')?string('class="ui-step-done"','')}>
												<div class="ui-step-number">4</div>
												<div class="ui-step-title">订单完成</div>
												<div class="ui-step-meta">
													${order.finishtime!}
												</div>
											</li>
										</ul>
									</div>

									<div class="panel panel-default">
										<div class="panel-heading">
											订单信息
										</div>
										<div class="panel-body">
											<div class="row">
												<div class="col-sm-3">订单编号: ${order.orderid!}</div>
												<div class="col-sm-2">订单状态: <span style="color: gray">${order.status!}</span></div>
												<div class="col-sm-2">付款方式: ${order.paystatus!}</div>
												<div class="col-sm-2">买家:  ${order.paynickname!}
												</div>
												<div class="col-md-3"></div>
											</div>

											<div class="row">
												<div class="col-sm-3">商品数量: ${order.goodnum!}</div>
												<div class="col-sm-2">商品总价: ￥${order.totalprice!}</div>
												<div class="col-sm-2">支付邮费: ￥${order.realexpressprice!}</div>
												<div class="col-md-3">实际支付: ￥${order.realpayprice!}</div>
											</div>
											<div class="row">
												<div class="col-sm-3">下单时间: ${order.createtime!}</div>
												<div class="col-sm-2">支付时间: ${order.paytime!}</div>
												<div class="col-sm-2">发货时间: ${order.deliverytime!}</div>
												<div class="col-md-3">完成时间: ${order.finishtime!}</div>
											</div>
											<div class="row">
												<div class="col-md-3">收货人: ${order.chargename!}</div>
												<div class="col-md-2">联系电话: ${order.chargephone!}</div>
												<div class="col-md-7">收货地址: ${order.chargeaddress!} &nbsp;<a style="color: #44abf7 !important" class="text-primary op js-clip" onclick="Copy('${order.chargeaddress!}')" >复制</a></div>
											</div>
											<div class="row">
												<div class="col-md-3">物流公司: ${order.expressname!}</div>
												<div class="col-md-2">物流单号: ${order.expresssn!}</div>
												<div class="col-md-7">用户备注: ${order.usercomment!}</div>
											</div>
											<div class="row">
												<div class="col-md-12">
													订单备注:  ${order.ordercomment!}&nbsp;<a style="color: #44abf7 !important" href="javascript:;" class="order_remark load_iframe"  data-url="${request.contextPath}/orders/comment?id=${order.id}&content=${order.ordercomment!}"  data-width="1000px" data-height="700px">修改</a>
												</div>
											</div>
										</div>
									</div>

									<div class="table-responsive m-t">
										<table class="table invoice-table" style="margin-bottom: 0px">
											<thead>
											<tr>
												<th>商品清单</th>
												<th>规格</th>
												<th>单价</th>
												<th>数量</th>
												<th>总价</th>
											</tr>
											</thead>
											<tbody>
											<tr>
												<td>
													<a href="" target="_blank">
														<img title="${order.goodname!}" src="${order.goodimg!}"  class="img_pop_show rounded img-sm"/>&nbsp;${order.goodname!}
													</a>
												</td>
												<td>
												</td>
												<td>
													￥${order.goodprice!}
												</td>
												<td>${order.goodnum!}</td>
												<td>${order.totalprice!}</td>
											</tr>
											</tbody>
										</table>
									</div>
									<table class="table invoice-total">
										<tbody>
										<tr>
											<td><strong>运费 :</strong></td>
											<td>￥${order.realexpressprice!}</td>
										</tr>
										<tr>
											<td><strong>实付金额 :</strong></td>
											<td>￥${order.realpayprice!}</td>
										</tr>
										</tbody>
									</table>
									<table class="table invoice-total">
										<tbody>
										<tr>
											<td>
<#--												<#if order.status =='1'>-->
<#--													<button class="btn btn-success load_iframe" data-width="1000px" data-height="700px"  data-url="" type="button"><i class="fa fa-truck"></i> 去发货</button>-->
<#--													<button class="btn btn-danger load_iframe" data-width="1000px" data-height="700px"  data-url="" type="button"><i class="fa fa-money"></i> 立即退款</button>-->
<#--													<#else>-->
<#--												<button class="btn btn-success load_iframe" data-url="${request.contextPath}/orders/editpage?id=${order.id!}" data-width="1000px" data-height="700px" type="button"><i class="fa fa-edit"></i> 修改订单</button>-->
<#--												</#if>-->
												<button class="btn btn-info load_iframe" data-url="${request.contextPath}/orders/orderlog?orderid=${order.orderid!}" data-width="1000px" data-height="700px" type="button"><i class="fa fa-file-text-o"></i> 订单记录</button>
											</td>
										</tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="footer"></div>    
			</div>
		</div>
		<script>
			layui.use(['laydate', 'element'], function () {
				var laydate = layui.laydate, element = layui.element;
				//日期时间选择器
				laydate.render({
					elem: '.start_time'
					, type: 'datetime'
				});
				laydate.render({
					elem: '.end_time'
					, type: 'datetime'
				});
			});

			function Copy(str) {
				var save = function(e) {
					e.clipboardData.setData('text/plain', str);
					e.preventDefault();
				};
				document.addEventListener('copy', save);
				document.execCommand('copy');
				document.removeEventListener('copy', save);
				layer.alert('复制成功');
			}
		</script>

	</body>
</html>
