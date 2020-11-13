var VM = new Vue({
    el: '#diywrapper',
    data: {
        static_url: '',
        richText: '',//富文本
        toolGroups: {},
        //左侧工具栏图标class
        iconfont: 'iconfont',
        toolicon: 'tool-icon',
        //页面元素内容
        content: {
            //页面导航条
            'navigationBar': {
                'name': '页面名称',//页面名称
                'title': '页面标题',//页面标题
                'frontColor': '#ffffff',//前景颜色
                'backgroundColor': '#ff4466',//背景颜色值
                'share_img': '',//背景颜色值
            },
            'items': [],//工具条列表
        },
        //当前编辑的页面元素类型
        currentType: 'index',
        //当前选中的元素
        currentIndex: -1,
        //当前编辑元素内容
        currentContent: {},
        //元素默认内容
        defaultContent: defaultContent,
        defaultOpeneds: ['basic', 'help', 'goods', 'newindex'],//默认展开
        //组件按钮启用状态
        phoneBtnStyle: {
            'hide': 'color: rgb(153, 153, 153)',
            'arrowup': 'color: rgb(153, 153, 153)',
            'arrowdown': 'color: rgb(153, 153, 153)',
            'copy': 'color: rgb(153, 153, 153)',
        },
        //商品排序
        goodsSorts: [
            {
                value: 'sort',
                label: '按商品排序'
            },
            {
                value: 'stateon_at',
                label: '按上架时间'
            },
            {
                value: 'sale_num',
                label: '按销量'
            },
            {
                value: 'marketprice',
                label: '按售价'
            },
            {
                value: 'browse_num',
                label: '按访问量'
            },
        ],
    },
    created: function () {
        this.static_url = $('.static_url').val();
    },
    mounted: function () {
        this.toolGroups = JSON.parse($('.tools').val());
        var content = $('.content').val();

        if (content) {
            this.content = JSON.parse(content);
        }

        //图标加域名
        this.defaultContent.notice.icon = this.static_url + this.defaultContent.notice.icon;
        //初始化富文本编辑器
        require(['util'], function (util) {
            util.editor('rich-text', {
                uniacid: window.sysinfo.uniacid,
                global: '',
                height: 200,
                dest_dir: '',
                image_limit: 33554432,
                allow_upload_video: true,
                audio_limit: 33554432,
                callback: ''
            });
        });
    },
    watch: {
        //监听切换组件
        currentIndex: function (val) {
            this.phoneBtnStyle = {
                'hide': 'color: rgb(153, 153, 153)',
                'arrowup': 'color: rgb(153, 153, 153)',
                'arrowdown': 'color: rgb(153, 153, 153)',
                'copy': 'color: rgb(153, 153, 153)',
            };
            //非顶部导航
            if (val != -1) {
                this.phoneBtnStyle.hide = "#fff";
                this.phoneBtnStyle.copy = "#fff";
                var length = this.content.items.length;

                if (length > 1) {
                    if (val != 0) {
                        this.phoneBtnStyle.arrowup = "#fff";
                    }
                    if (val < length - 1) {
                        this.phoneBtnStyle.arrowdown = "#fff";
                    }
                }
            }
        },
        richText: function (val) {

        }
    },
    methods: {
        //返回
        goBack: function () {
            history.go(-1);
        },
        //全屏
        fullScreen: function () {
            screenfull.toggle($('#container')[0]).then(function () {
                console.log('Fullscreen mode: ' + (screenfull.isFullscreen ? 'enabled' : 'disabled'))
            });
        },
        /**
         * 添加组件
         * @param type
         */
        addTool: function (type) {
            var item = JSON.parse(JSON.stringify(this.defaultContent[type]));
            if (item) {
                if (this.checkAddTool(type)) {
                    var index = this.content.items.length;

                    this.$set(this.content.items, index, item);

                    this.currentType = type;
                    this.currentIndex = index;
                    this.formatData(index);

                    let msg = document.getElementById('phone-content') // 获取对象
                    msg.scrollTop = msg.scrollHeight // 滚动高度
                }
            }
        },
        //检测添加组件
        checkAddTool: function (type) {
            var items = this.content.items;
            //只能有一个组件
            var hasOneTypes = ['float_btn'];
            for (var i in hasOneTypes) {
                for (var j in items) {
                    if (items[j].type == hasOneTypes[i]
                        && type == hasOneTypes[i]
                    ) {
                        this.$message({
                            message: items[i].name + '组件最多添加一个！',
                            type: 'error'
                        });
                        return false;
                    }
                }
            }

            return true;
        },
        /**
         * 编辑元素
         * @param index
         */
        editTool: function (index) {
            var item = Object.assign({}, this.content.items[index]);
            if (item) {
                this.currentIndex = index;
                this.currentType = item.type;
                this.currentContent = item;
                this.formatData(index);
            }
        },
        /**
         * 隐藏组件
         */
        hideTools: function () {
            if (this.phoneBtnStyle.hide != '#fff') {
                return;
            }

            var index = this.currentIndex;
            this.$set(this.content.items[index], 'hide', !this.content.items[index].hide);
        },
        /**
         * 复制组件
         */
        copyTools: function () {
            if (this.phoneBtnStyle.copy != '#fff') {
                return;
            }

            var index = this.currentIndex;
            var item = Object.assign({}, this.content.items[index]);

            var index = this.content.items.length;
            this.$set(this.content.items, index, item);

            this.currentType = item.type;
            this.currentIndex = index;
        },
        /**
         * 上移组件
         */
        arrowupTools: function () {
            if (this.phoneBtnStyle.arrowup != '#fff') {
                return;
            }

            var index = this.currentIndex;
            var indexPre = this.currentIndex - 1;

            var item = Object.assign({}, this.content.items[index]);
            var itemPre = Object.assign({}, this.content.items[indexPre]);

            this.$set(this.content.items, index, itemPre);
            this.$set(this.content.items, indexPre, item);

            this.currentIndex = indexPre;
            this.currentType = item.type;
            this.currentContent = item;

            this.formatData(index);
            this.formatData(indexPre);
        },
        /**
         * 下移组件
         */
        arrowdownTools: function () {
            if (this.phoneBtnStyle.arrowdown != '#fff') {
                return;
            }

            var index = this.currentIndex;
            var indexNext = this.currentIndex + 1;

            var item = Object.assign({}, this.content.items[index]);
            var itemNext = Object.assign({}, this.content.items[indexNext]);

            this.$set(this.content.items, index, itemNext);
            this.$set(this.content.items, indexNext, item);

            this.currentIndex = indexNext;
            this.currentType = item.type;
            this.currentContent = item;

            this.formatData(index);
            this.formatData(indexNext);
        },
        /**
         * 格式化组件数据
         * @param index
         */
        formatData: function (index) {
            if (this.content.items[index].padding_top_buttom != undefined) {
                this.content.items[index].padding_top_buttom = parseInt(this.content.items[index].padding_top_buttom);
            }
            if (this.content.items[index].padding_left_right != undefined) {
                this.content.items[index].padding_left_right = parseInt(this.content.items[index].padding_left_right);
            }
            if (this.content.items[index].padding != undefined) {
                this.content.items[index].padding = parseInt(this.content.items[index].padding);
            }

            if (this.content.items[index].type == 'rich_text') {
                $("#rich-text").show();
                document.getElementById('ueditor_0').contentWindow.document.body.innerHTML = this.content.items[index].content;
            } else {
                $("#rich-text").hide();
            }
        },
        /**
         * 删除元素
         */
        delTool: function (index) {
            var that = this;
            this.$confirm('确定删除该组件吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                that.$delete(this.content.items, index);
                that.setCurrentLast();
                this.$message({
                    message: '删除成功',
                    type: 'success'
                });
            }).catch(() => {

            });
        },
        //设置当前组件为最后一个组件
        setCurrentLast: function () {
            var items = this.content.items;
            if (items.length > 0) {
                this.currentIndex = items.length - 1;
                this.currentType = items[this.currentIndex].type;
                this.currentContent = items[this.currentIndex];
            } else {
                this.currentIndex = -1;
                this.currentType = 'index';
                this.currentContent = this.content.navigationBar;
            }
        },
        //显示编辑区域
        showEditBox: function (type, index) {
            this.currentType = type;
            this.currentIndex = index;
        },
        //上传图片
        uploadImg: function (column, index, subColumn) {
            var that = this;
            require(["util"], function (util) {
                var btn = $('.uploadSource');
                var ipt = btn.parent().prev();
                var val = ipt.val();
                var img = ipt.parent().next().children();
                options = {
                    'global': false,
                    'class_extra': '',
                    'direct': true,
                    'multiple': false,
                    'fileSizeLimit': 33554432
                };
                util.image(val, function (url) {
                    if (subColumn) {
                        console.log(that.content.items[that.currentIndex]);
                        that.content.items[that.currentIndex][column][index][subColumn] = url.url;
                    } else {
                        if (that.currentType == 'index') {
                            eval("that." + column + '="' + url.url + '";');
                        } else {
                            that.content.items[that.currentIndex][column] = url.url;
                        }
                    }
                }, options);
            });
        },
        //上传音频
        uploadAudio: function (column, index, subColumn) {
            var that = this;
            require(["util"], function (util) {
                var btn = $('.uploadSourceAudio');
                var ipt = btn.parent().prev();
                var val = ipt.val();
                util.audio(val, function (url) {
                    if (subColumn) {
                        console.log(that.content.items[that.currentIndex]);
                        that.content.items[that.currentIndex][column][index][subColumn] = url.url;
                        that.content.items[that.currentIndex][column][index]['filename'] = url.filename;
                    } else {
                        if (that.currentType == 'index') {
                            eval("that." + column + '="' + url.url + '";');
                        } else {
                            that.content.items[that.currentIndex][column] = url.url;
                            that.content.items[that.currentIndex]['filename'] = url.filename;
                        }
                    }
                }, "", {"direct": true, "multiple": false, "fileSizeLimit": 33554432});
            });
        },
        //上传视频
        uploadVideo: function (column, index, subColumn) {
            var that = this;
            require(["util"], function (util) {
                var btn = $('.uploadSourceVideo');
                var ipt = btn.parent().prev();
                var val = ipt.val();
                util.audio(val, function (url) {
                    if (subColumn) {
                        console.log(that.content.items[that.currentIndex]);
                        that.content.items[that.currentIndex][column][index][subColumn] = url.url;
                        that.content.items[that.currentIndex][column][index]['filename'] = url.filename;
                    } else {
                        if (that.currentType == 'index') {
                            eval("that." + column + '="' + url.url + '";');
                        } else {
                            that.content.items[that.currentIndex][column] = url.url;
                            that.content.items[that.currentIndex]['filename'] = url.filename;
                        }
                    }
                }, {"direct": true, "multi": false, "type": "video", "fileSizeLimit": 33554432});
            });
        },
        //保存
        save: function () {
            var that = this;
            this.$confirm('确定保存该页面吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                var url = $(".saveUrl").val();
                $.ajax({
                    url: url,
                    type: 'POST',
                    data: {content: that.content},
                    dataType: "json",
                    success: function (result) {
                        if (result.errno == 0) {
                            layer.msg(result.message, {time: 2000, icon: 1});
                            setTimeout(function () {
                                window.location = result.redirect;
                            }, 1000);
                        } else {
                            layer.msg(result.message, {time: 2000, icon: 5});
                        }
                    }
                });
            }).catch(() => {

            });
        },
        /**组件函数**/

        //添加公告内容
        noticeAddText: function () {
            var text = Object.assign({}, this.defaultContent.notice.text[0]);

            var index = this.currentIndex;
            this.content.items[index].text.push(text);
        },
        //删除公告内容
        noticeDelText: function (index) {
            if (this.content.items[this.currentIndex].text.length <= 1) {
                this.$message({
                    message: '最少一条公告内容！',
                    type: 'warning'
                });
                return;
            } else {
                this.$delete(this.content.items[this.currentIndex].text, index);
            }
        },
        //预览文本内容
        showRichText: function () {
            var content = document.getElementById('ueditor_0').contentWindow.document.body.innerHTML;
            var index = this.currentIndex;
            this.$set(this.content.items[index], 'content', content);
        },
        //添加子元素
        addSubTool: function (type, column) {
            var item = Object.assign({}, this.defaultContent[type][column][0]);

            var index = this.currentIndex;
            this.content.items[index][column].push(item);
        },
        //删除子元素
        delSubTool: function (type, column, subIndex) {
            if (this.content.items[this.currentIndex][column].length <= 1) {
                this.$message({
                    message: '最少一条元素',
                    type: 'warning'
                });
                return;
            } else {
                this.$delete(this.content.items[this.currentIndex][column], subIndex);
            }
        },
        //修改元素
        editToolElement: function (column, value) {
            var index = this.currentIndex;
            this.$set(this.content.items[index], column, value);
        },
        /*魔方*/
        //点击选择魔方块
        selectMagiCube: function (selectLine, selectColumn, event) {
            var index = this.currentIndex;
            var item = this.content.items[index];
            if (item.select_cube == '') {
                var cube = selectLine + ':' + selectColumn;
                this.$set(this.content.items[index], 'select_cube', cube);
                this.content.items[index].select_cubes.push(cube);
            } else {
                var select_cube = this.content.items[index].select_cube;
                var select_end_cube = this.content.items[index].select_end_cube;
                select_cube_arr = select_cube.split(':');
                select_end_cube_arr = select_end_cube.split(':');

                var item = {
                    top: Math.min(select_cube_arr[0], select_end_cube_arr[0]) - 1,
                    left: Math.min(select_cube_arr[1], select_end_cube_arr[1]) - 1,
                    width: (Math.abs(select_cube_arr[1] - select_end_cube_arr[1]) + 1),
                    height: (Math.abs(select_cube_arr[0] - select_end_cube_arr[0]) + 1),
                    image: '/addons/shimmer_liveshop/static/web/admin/diypage/images/nopic.png'
                };
                this.content.items[index].items.push(item);

                this.clearMsetMagicCube();
                console.log(88888888888);
                console.log(this.content.items[index].items);
            }
        },
        //鼠标移动到魔方
        mouseoverMagiCube: function (selectLine, selectColumn, event) {
            var index = this.currentIndex;
            var item = this.content.items[index];
            if (item.select_cube != '') {
                this.resetSelectCute(selectLine, selectColumn);
            }
        },
        clearMsetMagicCube: function () {
            var index = this.currentIndex;

            this.$set(this.content.items[index], 'select_cubes', []);
            this.$set(this.content.items[index], 'select_cube', '');
            this.$set(this.content.items[index], 'select_end_cube', '');
        },
        /**
         * 重置魔方选择
         * @param selectLine
         * @param selectColumn
         */
        resetSelectCute(selectLine, selectColumn) {
            var index = this.currentIndex;
            var item = this.content.items[index];
            var currentCute = selectLine + ':' + selectColumn;
            var select_cube_arr = item.select_cube.split(':');
            var select_cube_line = select_cube_arr[0];
            var select_cube_column = select_cube_arr[1];

            var start_line = 1;
            var end_line = 4;
            var start_column = 1;
            var end_column = 4;

            if (selectLine >= select_cube_line) {
                start_line = select_cube_line;
                end_line = selectLine;
            } else {
                start_line = selectLine;
                end_line = select_cube_line;
            }

            if (selectColumn >= select_cube_column) {
                start_column = select_cube_column;
                end_column = selectColumn;
            } else {
                start_column = selectColumn;
                end_column = select_cube_column;
            }

            var select_cubes = [];

            for (var i = start_line; i <= end_line; i++) {
                for (var j = start_column; j <= end_column; j++) {
                    var cuteTmp = i + ':' + j;
                    if (this.content.items[index].select_cubes.indexOf(cuteTmp) === -1) {
                        select_cubes.push(cuteTmp);
                    }
                }
            }
            this.$set(this.content.items[index], 'select_cubes', select_cubes);
            this.$set(this.content.items[index], 'select_end_cube', currentCute);
        },
        //鼠标移开魔方
        mouseoutMagiCube: function (selectLine, selectColumn, event) {
            var index = this.currentIndex;
            var item = this.content.items[index];
            if (item.select_cube != '') {
                this.resetSelectCute(selectLine, selectColumn);
            }
        },
        /**
         * 选中魔方
         * @param subIndex
         */
        selectMagicCubeItem: function (subIndex) {
            this.content.items[this.currentIndex]['select_item_index'] = subIndex;
        },
        /**
         * 删除魔方
         * @param subIndex
         */
        delMagicCubeItem: function (subIndex) {
            this.$delete(this.content.items[this.currentIndex]['items'], subIndex);
            this.content.items[this.currentIndex]['select_item_index'] = this.content.items[this.currentIndex]['items'].length - 1;
        },
        /**
         * 设置魔方风格
         * @param style
         */
        setMagicCubeStyle: function (style) {
            this.content.items[this.currentIndex]['style'] = style;
            if (style == 0) {
                this.$set(this.content.items[this.currentIndex], 'items', []);
                this.clearMsetMagicCube();
            } else {
                var items = defauultMagicCubeStyle[style];
                this.content.items[this.currentIndex]['items'] = items;
            }
        },
        /*end魔方*/
        /*热区*/
        /**
         * 设置热区
         */
        setHotzone: function () {
            var item = this.content.items[this.currentIndex];
            // if (!item.image) {
            //     this.$message({
            //         message: '请上传图片后再设置热区！',
            //         type: 'error'
            //     });
            //     return;
            // }
            this.content.items[this.currentIndex].showDialog = true;
        },
        dragStart: function () {
            console.log(88888888888);
            console.log(33);
        },
        /*end热区*/
        /*商品*/
        //商品来源切换
        goodsChangeFrom: function (val) {
            if (val == 'upload') {
                this.$set(this.content.items[this.currentIndex], 'items', []);
            } else if (val == 'category') {
                this.setGoodsCategoryItems(1);
            }
        },
        //设置选择分类下面的商品列表
        setGoodsCategoryItems: function (val) {
            var items = [];
            val = parseInt(val);
            for (var i = 0; i < val; i++) {
                items.push({
                    'id': 0,
                    'title': '这里是商品标题',
                    'sale_num': 1,
                    'thumb': '/addons/shimmer_liveshop/static/web/admin/diypage/images/nopic.png',
                    'marketprice': '20.00',
                    'productprice': '25.00',
                });
            }

            this.$set(this.content.items[this.currentIndex], 'items', items);
            this.$set(this.content.items[this.currentIndex], 'limit', val);
        }
        /*end商品*/
        /**end组件函数**/
    }
});

/**
 * 添加商品
 * @param ids
 * @param titles
 * @param data
 */
function addGoodsItem(ids, titles, data) {
    var items = VM.content.items[VM.currentIndex]['items'];
    if (!items) {
        items = [];
    }

    items = items.concat(data);

    VM.content.items[VM.currentIndex]['items'] = items;
}