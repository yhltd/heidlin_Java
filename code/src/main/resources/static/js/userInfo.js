var idd;

function getList() {
    $('#name').val("");
    $('#department').val("");
    $ajax({
        type: 'post',
        url: '/user/getList',
    }, false, '', function (res) {
        if (res.code == 200) {
            setTable(res.data);
            $("#textSelectTable").colResizable({
                liveDrag: true,
                gripInnerHtml: "<div class='grip'></div>",
                draggingClass: "dragging",
                resizeMode: 'fit'
            });
            for (i=0;i<=res.data.id;i++){
                idd=i;
            }
        }
    })
}

function getListFounder() {
    $ajax({
        type: 'post',
        url: '/textRestrictions/getListFounder',
    }, false, '', function (res) {
        if (res.code == 200) {
            console.log(res.data)
            setTextTable(res.data);
            $("#userSelectTable").colResizable({
                liveDrag: true,
                gripInnerHtml: "<div class='grip'></div>",
                draggingClass: "dragging",
                resizeMode: 'fit'
            });
        }
    })
}

$(function () {
    getList();

    $('#select-btn').click(function () {
        var name = $('#name').val();
        var department = $('#department').val();
        $ajax({
            type: 'post',
            url: '/user/queryList',
            data: {
                name: name,
                department: department,
            }
        }, true, '', function (res) {
            if (res.code == 200) {
                setTable(res.data);
            }
        })
    });

    //刷新
    $("#refresh-btn").click(function () {
        getList();
    });

    //点击新增按钮显示弹窗
    $("#add-btn").click(function () {
        $('#add-modal').modal('show');
    });

    //新增弹窗里点击关闭按钮
    $('#add-close-btn').click(function () {
        $('#add-modal').modal('hide');
    });

    //新增弹窗里点击提交按钮
    $("#add-submit-btn").click(function () {
        let params = formToJson("#add-form");
        if (checkForm('#add-form')) {
            $ajax({
                type: 'post',
                url: '/user/getPower',
            }, false, '', function (res) {
                if (res.code == 200) {
                    console.log(res.data)
                    var this_power = res.data
                    if(this_power == '管理员'){
                        $('#add2-modal').modal('show');
                        getListFounder();
                    }else{
                        $ajax({
                            type: 'post',
                            url: '/user/add',
                            data: JSON.stringify({
                                addInfo: params,
                                textList:[],
                            }),
                            dataType: 'json',
                            contentType: 'application/json;charset=utf-8'
                        }, false, '', function (res) {
                            if (res.code == 200) {
                                swal("", res.msg, "success");
                                $('#add-form')[0].reset();
                                getList();
                                $('#add-close-btn').click();
                            } else {
                                swal("", res.msg, "error");
                            }
                        })
                    }
                }
            })
        }
    });

    //新增弹窗里点击提交按钮
    $("#add2-submit-btn").click(function () {
        let rows = getTableSelection("#textSelectTable");
        let params = formToJson("#add-form");
        console.log(rows)
        var textList = []
        for(var i=0; i<rows.length; i++){
            textList.push(rows[i].data)
        }
        console.log(textList)
        let user = formToJson("#add-form");
        var msg = confirm("确认要将当前选中的商品共享给新建的用户？");
        if (msg) {
            if (checkForm('#add-form')) {
                $ajax({
                    type: 'post',
                    url: '/user/add',
                    data: JSON.stringify({
                        addInfo: params,
                        textList: textList,
                    }),
                    dataType: 'json',
                    contentType: 'application/json;charset=utf-8'
                }, false, '', function (res) {
                    if (res.code == 200) {
                        swal("", res.msg, "success");
                        $('#add2-modal').modal('hide');
                        $('#add-modal').modal('hide');
                        getList();
                    } else {
                        swal("", res.msg, "error");
                    }
                })
            }
        }

    });

    //点击修改按钮显示弹窗
    $('#update-btn').click(function () {
        let rows = getTableSelection('#userTable');
        if (rows.length > 1 || rows.length == 0) {
            swal('请选择一条数据修改!');
            return;
        }
        $('#update-modal').modal('show');
        setForm(rows[0].data, '#update-form');
        $('#update-power').val(rows[0].data.power);
        $('#update-department').val(rows[0].data.department);
        $('#update-stateUpd').val(rows[0].data.stateUpd);
        $('#update-moneySel').val(rows[0].data.moneySel);
    });

    //修改弹窗点击关闭按钮
    $('#update-close-btn').click(function () {
        $('#update-form')[0].reset();
        $('#update-modal').modal('hide');
    });

    //修改弹窗里点击提交按钮
    $('#update-submit-btn').click(function () {
        var msg = confirm("确认要修改吗？");
        if (msg) {
            if (checkForm('#update-form')) {
                let params = formToJson('#update-form');
                $ajax({
                    type: 'post',
                    url: '/user/update',
                    data: {
                        updateJson: JSON.stringify(params)
                    },
                    dataType: 'json',
                    contentType: 'application/json;charset=utf-8'
                }, false, '', function (res) {
                    if (res.code == 200) {
                        swal("", res.msg, "success");
                        $('#update-close-btn').click();
                        $('#update-modal').modal('hide');
                        getList();
                    } else {
                        swal("", res.msg, "error");
                    }
                })
            }
        }
    });

    //点击删除按钮
    $('#delete-btn').click(function () {
        var msg = confirm("确认要删除吗？");
        if (msg) {
            let rows = getTableSelection("#userTable");
            if (rows.length == 0) {
                swal('请选择要删除的数据！');
                return;
            }
            let idList = [];
            $.each(rows, function (index, row) {
                idList.push(row.data.id)
            });
            $ajax({
                type: 'post',
                url: '/user/delete',
                data: JSON.stringify({
                    idList: idList
                }),
                dataType: 'json',
                contentType: 'application/json;charset=utf-8'
            }, false, '', function (res) {
                if (res.code == 200) {
                    swal("", res.msg, "success");
                    getList();
                } else {
                    swal("", res.msg, "error");
                }
            })
        }
    })
});

function setTable(data) {
    if ($('#userTable').html != '') {
        $('#userTable').bootstrapTable('load', data);
    }

    $('#userTable').bootstrapTable({
        data: data,
        sortStable: true,
        classes: 'table table-hover text-nowrap table table-bordered',
        idField: 'id',
        pagination: true,
        pageSize: 15,//单页记录数
        clickToSelect: true,
        locale: 'zh-CN',
        toolbar: '#table-toolbar',
        toolbarAlign: 'left',
        theadClasses: "thead-light",//这里设置表头样式
        style:'table-layout:fixed',
        columns: [
            {
                field: '',
                title: '序号',
                align: 'center',
                width: 30,
                formatter: function (value, row, index) {
                    return index + 1;
                }
            }, {
                field: 'username',
                title: '用户名',
                align: 'center',
                sortable: true,
                width: 80,
            }, {
                field: 'password',
                title: '密码',
                align: 'center',
                sortable: true,
                width: 80,
            }, {
                field: 'power',
                title: '权限',
                align: 'center',
                sortable: true,
                width: 80,
            }, {
                field: 'name',
                title: '姓名',
                align: 'center',
                sortable: true,
                width: 80,
            }, {
                field: 'department',
                title: '部门',
                align: 'center',
                sortable: true,
                width: 100,
            },{
                field: 'change',
                title: '侵权词替换',
                align: 'center',
                sortable: true,
                width: 100,
            },

        ],
        onClickRow: function (row, el) {
            let isSelect = $(el).hasClass('selected')
            if (isSelect) {
                $(el).removeClass('selected')
            } else {
                $(el).addClass('selected')
            }
        }
    })
}

function setTextTable(data) {
    if ($('#textSelectTable').html != '') {
        $('#textSelectTable').bootstrapTable('load', data);
    }

    $('#textSelectTable').bootstrapTable({
        data: data,
        sortStable: true,
        classes: 'table table-hover text-nowrap table table-bordered',
        idField: 'id',
        clickToSelect: true,
        locale: 'zh-CN',
        toolbarAlign: 'left',
        theadClasses: "thead-light",//这里设置表头样式
        style:'table-layout:fixed',
        columns: [
            {
                field: '',
                title: '序号',
                align: 'center',
                width: 50,
                formatter: function (value, row, index) {
                    return index + 1;
                }
            }, {
                field: 'product',
                title: '产品名称',
                align: 'center',
                sortable: true,
                width: 150,
            },

        ],
        onClickRow: function (row, el) {
            let isSelect = $(el).hasClass('selected')
            if (isSelect) {
                $(el).removeClass('selected')
            } else {
                $(el).addClass('selected')
            }
        },
        rowStyle: function(row, index) {
            // 根据需要为行添加不同的class
            return {
                classes: 'selected'
            };
        }
    })
}