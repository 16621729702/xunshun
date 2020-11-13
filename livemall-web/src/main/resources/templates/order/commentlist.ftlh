
<!DOCTYPE html>
<html>

	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>后台管理
		</title>
		<meta name="csrf-token" content="80gKX72QieZfLjXeVXTnJ9irvKrapI71lM3K1bgP">
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
									<form class="layui-form" action="${request.contextPath}/comments/commentquery" method="post" id="submitform">
										<input type="hidden" name="pagesize" value="${pagesize}" id="formpagesize">
										<input type="hidden" name="page" value="${page}" id="formpage">
										<div class="layui-inline">
											<label class="layui-form-label">商品名称:</label>
											<div class="layui-input-inline">
												<input type="text" name="goodname" lay-verify="" placeholder="商品名称搜索"
													   value="${goodname!}" autocomplete="off" class="layui-input">
											</div>
										</div>
										<div class="layui-inline">
											<label class="layui-form-label">评论内容:</label>
											<div class="layui-input-inline">
												<input type="text" name="content" lay-verify="" placeholder="评论内容关键字搜索"
													   value="${content!}" autocomplete="off" class="layui-input">
											</div>
										</div>
										<div class="layui-inline">
											<label class="layui-form-label">是否回复</label>
											<div class="layui-input-inline">
												<select name="isreplay" lay-verify="">
													<option selected value="">全部</option>
													<option ${(isreplay=='0')?string('selected','')} value="0" >未回复</option>
													<option ${(isreplay=='1')?string('selected','')} value="1" >已回复</option>
												</select>
											</div>
										</div>
										<div class="layui-inline">
											<label class="layui-form-label">商品质量</label>
											<div class="layui-input-inline">
												<select name="goodquality" lay-verify="">
													<option selected value="">全部</option>
													<option ${(goodquality=='1')?string('selected','')} value="1" >很差</option>
													<option ${(goodquality=='2')?string('selected','')} value="2" >一般</option>
													<option ${(goodquality=='3')?string('selected','')} value="3" >满意</option>
													<option ${(goodquality=='4')?string('selected','')} value="4" >非常满意</option>
													<option ${(goodquality=='5')?string('selected','')} value="5" >无可挑剔</option>
												</select>
											</div>
										</div>
										<div class="layui-inline">
											<label class="layui-form-label">服务态度</label>
											<div class="layui-input-inline">
												<select name="serviceattitude" lay-verify="">
													<option selected value="">全部</option>
													<option ${(serviceattitude=='1')?string('selected','')} value="1" >很差</option>
													<option ${(serviceattitude=='2')?string('selected','')} value="2" >一般</option>
													<option ${(serviceattitude=='3')?string('selected','')} value="3" >满意</option>
													<option ${(serviceattitude=='4')?string('selected','')} value="4" >非常满意</option>
													<option ${(serviceattitude=='5')?string('selected','')} value="5" >无可挑剔</option>
												</select>
											</div>
										</div>
										<div class="layui-inline" style="padding-bottom: 5px">
											<div class="layui-input-inline">
												<button class="btn btn-success" type="submit">搜索</button>
											</div>
										</div>
									</form>
									<div class="toolbar">
										<div class="pager-page-num">
											<label>
												每页显示
												<select onchange="changepagesize(this.value)">
													<option value="20" ${(pagesize=='20')?string('selected','')}>20</option>
													<option value="50" ${(pagesize=='50')?string('selected','')}>50</option>
													<option value="100" ${(pagesize=='100')?string('selected','')}>100</option>
													<option value="200" ${(pagesize=='200')?string('selected','')}>200</option>
												</select> 条
											</label>
										</div>
										<span class="pager-total-num">共有数据：
											<strong id="total-num">${totalsize!}</strong> 条
										</span>
									</div>
									<table class="layui-table">
										<thead>
										<tr>
											<th>
												<input type="checkbox" class="check_all">
											</th>
											<th>ID</th>
											<th>用户信息</th>
											<th>商品信息</th>
											<th>评论内容</th>
											<th>评论图片</th>
											<th>管理员回复</th>
											<th>商品质量</th>
											<th>服务态度</th>
											<th>评论时间</th>
											<th>操作</th>
										</tr>
										</thead>
										<tbody>
										<#if (returninfo)??>
											<#if (returninfo?size > 0)>
												<#list returninfo as list>
													<tr>
														<td><input type="checkbox" name="commentid" class="check_item" value="${list.id}"></td>
														<td>${list.id}</td>
														<td>${list.nickname!}/${list.mobile!}</td>
														<td>${list.title!}</td>
														<td>${list.content!}</td>
														<#if list.img?contains("http")>
															<td><img src="${list.img!}"></td>
															　　<#else >
															<td><img src="${request.contextPath}/image/IoReadImage/${list.img!}"></td>
														</#if>
														<td>${list.adminreplay!}</td>
														<td>${list.goodquality!}</td>
														<td>${list.serviceattitude!}</td>
														<td>${list.replaydate!}</td>
														<td>
															<a href="javascript:;" class="btn btn-outline btn-success load_iframe" data-url="${request.contextPath}/comments/replay?id=${list.id}" data-width="1000px" data-height="700px">
																回复
															</a>
														</td>
													</tr>
												</#list>
											</#if>
										</#if>
										</tbody>
									</table>
									<div class="datatable-footer" style="overflow: hidden">
										<div class="batch-btns" style="float: left">
											<button class="btn btn-default" type="button" onclick="batchoption('delete');"><i
														class="fa fa-trash-o"></i> 批量删除
											</button>
										</div>
										<div class="dataTables_paginate paging_simple_numbers">
											<ul class="pagination">
												<li onclick="submitform('1');"><span>&laquo;</span></li>
												<#list 1..totalpage as i>
													<li onclick="submitform('${i_index+1}');" name="otherpage" value="${i_index+1}"><span>${i_index+1}</span></li>
												</#list>
												<li onclick="submitform('${totalpage}');"><span>&raquo;</span></li>
											</ul>
											<div class="gotopage">
												前往
												<input type="number" class="gopagenum"/>
												页
												<input type="button" class="btn btn-default btn-sm btn-gotopage" value="确定" onclick="querypage()"/>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="footer"></div>
			</div>
		</div>
		<script>
			function changepagesize(pagesize){
				$("#formpagesize").val(pagesize);
				$("#submitform").submit();
			}
			function querypage(){
				var page = $(".gopagenum").val();
				$("#formpage").val(page);
				$("#submitform").submit();
			}
			function submitform(page){
				$("#formpage").val(page);
				$("#submitform").submit();
			}
			function batchoption(operate) {
				var ids = "";
				$('input[name="commentid"]:checked').each(function () {
					var idVal = $(this).attr("value");
					ids += idVal + ",";
				});
				ids = ids.substring(0, ids.lastIndexOf(','));
				if(ids==""){
					layer.open({
						title: '警告'
						, content: '请选择要操作的数据'
					});
				}else{
					$.ajax({
						url:"${request.contextPath}/comments/batchoption",
						data:{"operate":operate,"ids":ids},
						dataType:"json",
						success:function(result){
							if(result.code == 200){
								$("#submitform").submit();
							}else{
								layer.open({
									title: '警告'
									,content: result.msg
								});
							}
						}});
				}
			}
		</script>
	</body>
</html>
