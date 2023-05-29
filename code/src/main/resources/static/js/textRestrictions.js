

function getList() {
    $('#this_column').val("");
    $ajax({
        type: 'post',
        url: '/textRestrictions/getList',
    }, false, '', function (res) {
        if (res.code == 200) {
            setTable(res.data);
            $("#userTable").colResizable({
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
        var this_column = $('#this_column').val();
        $ajax({
            type: 'post',
            url: '/textRestrictions/queryList',
            data: {
                this_column: this_column,
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
                url: '/textRestrictions/add',
                data: JSON.stringify({
                    addInfo: params,
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
                    url: '/textRestrictions/update',
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
                url: '/textRestrictions/delete',
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
                width: 50,
                formatter: function (value, row, index) {
                    return index + 1;
                }
            }, {
                field: 'product',
                title: '产品名称',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'columntext',
                title: '列标/数量规定',
                align: 'center',
                sortable: true,
                width:150,
                formatter: function (value, row, index) {
                    var this_columntext = row.columntext.split("<br><br>")[0]
                    var this_num = row.num.split("<br><br>")[0]
                    if(this_columntext == undefined){
                        this_columntext = ""
                    }
                    if(this_num == undefined){
                        this_num = ""
                    }
                    return this_columntext + "<HR style=\"FILTER: alpha(opacity=100,finishopacity=0,style=3)\" width=\"100%\" color=#dee2e6 SIZE=3>" + this_num
                }
            }, {
                field: 'columntext',
                title: '列标/数量规定',
                align: 'center',
                sortable: true,
                width:150,
                formatter: function (value, row, index) {
                    var this_columntext = row.columntext.split("<br><br>")[1]
                    var this_num = row.num.split("<br><br>")[1]
                    if(this_columntext == undefined){
                        this_columntext = ""
                    }
                    if(this_num == undefined){
                        this_num = ""
                    }
                    return this_columntext + "<HR style=\"FILTER: alpha(opacity=100,finishopacity=0,style=3)\" width=\"100%\" color=#dee2e6 SIZE=3>" + this_num
                }
            }, {
                field: 'columntext',
                title: '列标/数量规定',
                align: 'center',
                sortable: true,
                width:150,
                formatter: function (value, row, index) {
                    var this_columntext = row.columntext.split("<br><br>")[2]
                    var this_num = row.num.split("<br><br>")[2]
                    if(this_columntext == undefined){
                        this_columntext = ""
                    }
                    if(this_num == undefined){
                        this_num = ""
                    }
                    return this_columntext + "<HR style=\"FILTER: alpha(opacity=100,finishopacity=0,style=3)\" width=\"100%\" color=#dee2e6 SIZE=3>" + this_num
                }
            }, {
                field: 'columntext',
                title: '列标/数量规定',
                align: 'center',
                sortable: true,
                width:150,
                formatter: function (value, row, index) {
                    var this_columntext = row.columntext.split("<br><br>")[3]
                    var this_num = row.num.split("<br><br>")[3]
                    if(this_columntext == undefined){
                        this_columntext = ""
                    }
                    if(this_num == undefined){
                        this_num = ""
                    }
                    return this_columntext + "<HR style=\"FILTER: alpha(opacity=100,finishopacity=0,style=3)\" width=\"100%\" color=#dee2e6 SIZE=3>" + this_num
                }
            }, {
                field: 'columntext',
                title: '列标/数量规定',
                align: 'center',
                sortable: true,
                width:150,
                formatter: function (value, row, index) {
                    var this_columntext = row.columntext.split("<br><br>")[4]
                    var this_num = row.num.split("<br><br>")[4]
                    if(this_columntext == undefined){
                        this_columntext = ""
                    }
                    if(this_num == undefined){
                        this_num = ""
                    }
                    return this_columntext + "<HR style=\"FILTER: alpha(opacity=100,finishopacity=0,style=3)\" width=\"100%\" color=#dee2e6 SIZE=3>" + this_num
                }
            }, {
                field: 'columntext',
                title: '列标/数量规定',
                align: 'center',
                sortable: true,
                width:150,
                formatter: function (value, row, index) {
                    var this_columntext = row.columntext.split("<br><br>")[5]
                    var this_num = row.num.split("<br><br>")[5]
                    if(this_columntext == undefined){
                        this_columntext = ""
                    }
                    if(this_num == undefined){
                        this_num = ""
                    }
                    return this_columntext + "<HR style=\"FILTER: alpha(opacity=100,finishopacity=0,style=3)\" width=\"100%\" color=#dee2e6 SIZE=3>" + this_num
                }
            }, {
                field: 'columntext',
                title: '列标/数量规定',
                align: 'center',
                sortable: true,
                width:150,
                formatter: function (value, row, index) {
                    var this_columntext = row.columntext.split("<br><br>")[6]
                    var this_num = row.num.split("<br><br>")[6]
                    if(this_columntext == undefined){
                        this_columntext = ""
                    }
                    if(this_num == undefined){
                        this_num = ""
                    }
                    return this_columntext + "<HR style=\"FILTER: alpha(opacity=100,finishopacity=0,style=3)\" width=\"100%\" color=#dee2e6 SIZE=3>" + this_num
                }
            }, {
                field: 'columntext',
                title: '列标/数量规定',
                align: 'center',
                sortable: true,
                width:150,
                formatter: function (value, row, index) {
                    var this_columntext = row.columntext.split("<br><br>")[7]
                    var this_num = row.num.split("<br><br>")[7]
                    if(this_columntext == undefined){
                        this_columntext = ""
                    }
                    if(this_num == undefined){
                        this_num = ""
                    }
                    return this_columntext + "<HR style=\"FILTER: alpha(opacity=100,finishopacity=0,style=3)\" width=\"100%\" color=#dee2e6 SIZE=3>" + this_num
                }
            }, {
                field: 'columntext',
                title: '列标/数量规定',
                align: 'center',
                sortable: true,
                width:150,
                formatter: function (value, row, index) {
                    var this_columntext = row.columntext.split("<br><br>")[8]
                    var this_num = row.num.split("<br><br>")[8]
                    if(this_columntext == undefined){
                        this_columntext = ""
                    }
                    if(this_num == undefined){
                        this_num = ""
                    }
                    return this_columntext + "<HR style=\"FILTER: alpha(opacity=100,finishopacity=0,style=3)\" width=\"100%\" color=#dee2e6 SIZE=3>" + this_num
                }
            }, {
                field: 'columntext',
                title: '列标/数量规定',
                align: 'center',
                sortable: true,
                width:150,
                formatter: function (value, row, index) {
                    var this_columntext = row.columntext.split("<br><br>")[9]
                    var this_num = row.num.split("<br><br>")[9]
                    if(this_columntext == undefined){
                        this_columntext = ""
                    }
                    if(this_num == undefined){
                        this_num = ""
                    }
                    return this_columntext + "<HR style=\"FILTER: alpha(opacity=100,finishopacity=0,style=3)\" width=\"100%\" color=#dee2e6 SIZE=3>" + this_num
                }
            }
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