<div class="sidebar-collapse">
    <ul class="nav metismenu" id="side-menu">
            <li class="nav-header">
                <div class="dropdown profile-element">
                    <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                        <span class="block m-t-xs font-bold"><@shiro.principal property='name'/></span>
                        <span class="text-muted text-xs block">管理<b class="caret"></b></span>
                    </a>
                    <ul class="dropdown-menu animated fadeInRight m-t-xs">
                        <li><a class="dropdown-item" href="${request.contextPath}/logout">返回登录</a></li>
                    </ul>
                </div>
                <div class="logo-element">
                </div>
            </li>
            <@shiro.hasPermission name="indexpage">
            <li onclick="">
                <a href="${request.contextPath}/index">
                    <i class="fa fa fa-home"></i>
                    <span class="nav-label">首页</span>
                </a>
            </li>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="shopgood">
            <li>
                <a href="javascript:;" onclick="opensollapse(this);">
                    <i class="fa fa fa-address-book"></i>
                    <span class="nav-label">商品</span>
                    <span class="fa arrow"></span>
                </a>
                <ul class="nav nav-second-level collapse">
                    <@shiro.hasPermission name="goodcategory">
                    <li class="">
                        <a href="${request.contextPath}/goodcategory/query">
                            商品分类
                        </a>
                    </li>
                    </@shiro.hasPermission>
                    <@shiro.hasPermission name="goodmanager">
                    <li class="">
                        <a href="${request.contextPath}/good/query">
                            商品管理
                        </a>
                    </li>
                    </@shiro.hasPermission>
                    <@shiro.hasPermission name="goodmaterial">
                        <li class="">
                            <a href="${request.contextPath}/mater/list">
                                产品材质
                            </a>
                        </li>
                    </@shiro.hasPermission>
                </ul>
            </li>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="live">
            <li>
                <a href="javascript:;" onclick="opensollapse(this);">
                    <i class="fa fa fa-address-book"></i>
                    <span class="nav-label">直播管理</span>
                    <span class="fa arrow"></span>
                </a>
                <ul class="nav nav-second-level collapse">
                    <@shiro.hasPermission name="livecategory">
                        <li class="">
                            <a href="${request.contextPath}/live/categoryquery">
                                直播间分类
                            </a>
                        </li>
                    </@shiro.hasPermission>
                    <@shiro.hasPermission name="livemanager">
                        <li class="">
                            <a href="${request.contextPath}/live/query">
                                直播间管理
                            </a>
                        </li>
                    </@shiro.hasPermission>
                </ul>
            </li>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="merch">
                <li>
                    <a href="javascript:;" onclick="opensollapse(this);">
                        <i class="fa fa fa-address-book"></i>
                        <span class="nav-label">商户</span>
                        <span class="fa arrow"></span>
                    </a>
                    <ul class="nav nav-second-level collapse">
                        <@shiro.hasPermission name="merchquery">
                            <li class="">
                                <a href="${request.contextPath}/merch/query">
                                    商户列表
                                </a>
                            </li>
                        </@shiro.hasPermission>
                        <@shiro.hasPermission name="merchapplyquery">
                            <li class="">
                                <a href="${request.contextPath}/merch/applyquery">
                                    商户认证信息记录
                                </a>
                            </li>
                        </@shiro.hasPermission>
                        <@shiro.hasPermission name="merchsell">
                            <li class="">
                                <a href="${request.contextPath}/sell/list">
                                    商户主营类名
                                </a>
                            </li>
                        </@shiro.hasPermission>
                        <@shiro.hasPermission name="merchbaseSetting">
                            <li class="">
                            <a href="${request.contextPath}/merch/baseSetting">
                                基础设置
                            </a>
                            </li>
                        </@shiro.hasPermission>
<#--                        <@shiro.hasPermission name="merchintoSetting">-->
<#--                            <li class="">-->
<#--                            <a href="${request.contextPath}/merch/intoSetting">-->
<#--                                入驻设置-->
<#--                            </a>-->
<#--                            </li>-->
<#--                        </@shiro.hasPermission>-->
                        <@shiro.hasPermission name="merchpageSetting">
                        <li class="">
                            <a href="${request.contextPath}/merch/pageSetting">
                                页面设置
                            </a>
                        </li>
                        </@shiro.hasPermission>
                    </ul>
                </li>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="video">
            <li>
                <a href="javascript:;" onclick="opensollapse(this);">
                    <i class="fa fa fa-address-book"></i>
                    <span class="nav-label">短视频</span>
                    <span class="fa arrow"></span>
                </a>
                <ul class="nav nav-second-level collapse">
                    <@shiro.hasPermission name="query">
                        <li class="">
                            <a href="${request.contextPath}/video/query">
                                短视频列表
                            </a>
                        </li>
                    </@shiro.hasPermission>
                    <@shiro.hasPermission name="categoryquery">
                        <li class="">
                            <a href="${request.contextPath}/video/categoryquery">
                                短视频分类
                            </a>
                        </li>
                    </@shiro.hasPermission>
                </ul>
            </li>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="coupons">
            <li>
                <a href="javascript:;" onclick="opensollapse(this);">
                    <i class="fa fa fa-address-book"></i>
                    <span class="nav-label">营销</span>
                    <span class="fa arrow"></span>
                </a>
                <ul class="nav nav-second-level collapse">
                    <@shiro.hasPermission name="couponsquery">
                        <li class="">
                        <a href="${request.contextPath}/marker/couponsquery">
                            优惠券
                        </a>
                    </li>
                    </@shiro.hasPermission>
                </ul>
            </li>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="orders">
                <li>
                    <a href="javascript:;" onclick="opensollapse(this);">
                        <i class="fa fa fa-address-book"></i>
                        <span class="nav-label">订单</span>
                        <span class="fa arrow"></span>
                    </a>
                    <ul class="nav nav-second-level collapse">
                        <@shiro.hasPermission name="orderquery">
                            <li class="">
                                <a href="${request.contextPath}/orders/orderquery">
                                    全部订单
                                </a>
                            </li>
                            <li class="">
                                <a href="${request.contextPath}/orders/waitsendlist">
                                    待发货
                                </a>
                            </li>
                            <li class="">
                                <a href="${request.contextPath}/orders/waitgetlist">
                                    待收货
                                </a>
                            </li>
                            <li class="">
                                <a href="${request.contextPath}/orders/waitpaylist">
                                    待付款
                                </a>
                            </li>
                            <li class="">
                                <a href="${request.contextPath}/orders/successlist">
                                    已完成
                                </a>
                            </li>
                            <li class="">
                                <a href="${request.contextPath}/orders/closedlist">
                                    已关闭
                                </a>
                            </li>
                            <li class="">
                                <a href="${request.contextPath}/orders/refundlist">
                                    退款中
                                </a>
                            </li>
                            <li class="">
                                <a href="${request.contextPath}/orders/refundedlist">
                                    已退款
                                </a>
                            </li>
                        </@shiro.hasPermission>
                        <@shiro.hasPermission name="commentquery">
                            <li class="">
                                <a href="${request.contextPath}/comments/commentquery">
                                    评论管理
                                </a>
                            </li>
                        </@shiro.hasPermission>
                    </ul>
                </li>
            </@shiro.hasPermission>
        <@shiro.hasPermission name="withdrawal">
            <li>
                <a href="javascript:;" onclick="opensollapse(this);">
                    <i class="fa fa fa-address-book"></i>
                    <span class="nav-label">提现审核</span>
                    <span class="fa arrow"></span>
                </a>
                <ul class="nav nav-second-level collapse">
                        <li class="">
                            <a href="${request.contextPath}/withdrawal/list">
                                商户提现审核列表
                            </a>
                        </li>
                </ul>
            </li>
        </@shiro.hasPermission>
            <@shiro.hasPermission name="member">
            <li>
                <a href="javascript:;" onclick="opensollapse(this);">
                    <i class="fa fa fa-address-book"></i>
                    <span class="nav-label">会员</span>
                    <span class="fa arrow"></span>
                </a>
                <ul class="nav nav-second-level collapse">
                    <@shiro.hasPermission name="memberquery">
                    <li class="">
                        <a href="${request.contextPath}/member/query">
                            会员列表
                        </a>
                    </li>
                    </@shiro.hasPermission>
                    <@shiro.hasPermission name="memberlevelquery">
                    <li class="">
                        <a href="${request.contextPath}/member/levelquery">
                            会员等级列表
                        </a>
                    </li>
                    </@shiro.hasPermission>
                </ul>
            </li>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="knowledge">
            <li>
                <a href="javascript:;" onclick="opensollapse(this);">
                    <i class="fa fa fa-address-book"></i>
                    <span class="nav-label">知识库</span>
                    <span class="fa arrow"></span>
                </a>
                <ul class="nav nav-second-level collapse">
                    <@shiro.hasPermission name="knowledgeinfoquery">
                    <li class="">
                        <a href="${request.contextPath}/help/infoquery">
                            知识库信息列表
                        </a>
                    </li>
                    </@shiro.hasPermission>
                    <@shiro.hasPermission name="knowledgecategoryquery">
                    <li class="">
                        <a href="${request.contextPath}/help/categoryquery">
                            帮助分类列表
                        </a>
                    </li>
                    </@shiro.hasPermission>
                    <@shiro.hasPermission name="knowledgefeedbackquery">
                    <li class="">
                        <a href="${request.contextPath}/help/feedbackquery">
                            意见反馈列表
                        </a>
                    </li>
                    </@shiro.hasPermission>
                </ul>
            </li>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="sysadmin">
                <li>
                    <a href="javascript:;" onclick="opensollapse(this);">
                        <i class="fa fa fa-address-book"></i>
                        <span class="nav-label">系统管理</span>
                        <span class="fa arrow"></span>
                    </a>
                    <ul class="nav nav-second-level collapse">
                        <@shiro.hasPermission name="admin">
                            <li class="">
                                <a href="${request.contextPath}/admin/query">
                                    后台用户管理
                                </a>
                            </li>
                        </@shiro.hasPermission>
                        <@shiro.hasPermission name="role">
                            <li class="">
                                <a href="${request.contextPath}/role/query">
                                   角色管理
                                </a>
                            </li>
                        </@shiro.hasPermission>
                        <@shiro.hasPermission name="permission">
                            <li class="">
                                <a href="${request.contextPath}/permission/query">
                                    权限项管理
                                </a>
                            </li>
                        </@shiro.hasPermission>
                    </ul>
                </li>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="basic">
            <li onclick="">
                <a href="javascript:;" onclick="opensollapse(this);">
                    <i class="fa fa fa-home"></i>
                    <span class="nav-label">基础</span>
                    <span class="fa arrow"></span>
                </a>
                <ul class="nav nav-second-level collapse">
                    <@shiro.hasPermission name="basicdictquery">
                        <li class="">
                            <a href="${request.contextPath}/dict/query">
                                字典表
                            </a>
                        </li>
                    </@shiro.hasPermission>
                    <@shiro.hasPermission name="basicagreementquery">
                        <li class="">
                            <a href="${request.contextPath}/agreement/query">
                                隐私协议和用户协议管理
                            </a>
                        </li>
                    <li class="">
                        <a href="${request.contextPath}/consult/query">
                            咨询管理
                        </a>
                    </li>
                    </@shiro.hasPermission>
                </ul>
            </li>
            </@shiro.hasPermission>
        <@shiro.hasPermission name="versioncontroller">
            <li onclick="">
                <a href="${request.contextPath}/version/query">
                    <i class="fa fa fa-home"></i>
                    <span class="nav-label">版本管理</span>
                </a>
            </li>
        </@shiro.hasPermission>
            <@shiro.hasPermission name="setting">
                <li onclick="">
                    <a href="${request.contextPath}/setting/index">
                        <i class="fa fa fa-home"></i>
                        <span class="nav-label">设置</span>
                    </a>
                </li>
            </@shiro.hasPermission>

        </ul>
    </div>
<div class="navfoot">
    <ul class="nav metismenu"  >
        <li >
            <a href="${request.contextPath}/logout">
                <i class="fa fa fa-home"></i>
                <span class="nav-label">返回登录</span>
            </a>
        </li>
    </ul>
</div>
<style>
    .navfoot{
        position: fixed;
        bottom: 0;
    }
</style>
    <script>
        function GetUrlRelativePath() {
            var url = document.location.toString();
            var arrUrl = url.split("//");
            var start = arrUrl[1].indexOf("/");
            var relUrl = arrUrl[1].substring(start);// stop省略，截取从start开始到结尾的所有字符
            console.log(relUrl)
            if (relUrl.indexOf("?") != -1) {
                relUrl = relUrl.split("?")[0];
            }
            return relUrl;
        }
        $(document).ready(function() {
            var url = GetUrlRelativePath();
            $(document.body).find('a').each(function(i, obj) {
                var str = $(obj).attr('href');
                if (url==str) {
                    $(obj).parent().addClass("active");
                    var second = $(obj).parent().parent()
                    if(second.hasClass("nav nav-second-level collapse")){
                        second.addClass("in")
                        second.parent().addClass("active")
                    }
                }
            });
        });
        function opensollapse(obj) {
            if($(obj).next().hasClass("in")){
                $(obj).next().removeClass("in");
            }else{
                $("ul").removeClass("in");
                $(obj).next().addClass("in");
            }

        }
    </script>