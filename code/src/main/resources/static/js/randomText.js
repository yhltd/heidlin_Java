

function getList() {
    $('#this_column').val("");
    $ajax({
        type: 'post',
        url: '/randomText/getList',
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
            url: '/randomText/queryList',
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
                url: '/randomText/add',
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
                    url: '/randomText/update',
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
                url: '/randomText/delete',
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

    //上传excel
    $('#import-btn').click(function () {
        $('#file').trigger('click');
    });

    //判断文件名改变
    $('#file').change(function () {
        //显示
        $('#loading').modal('show');
        var url = null;
        if ($('#file').val() != '') {
            if ($('#file').val().substr(-5) == '.xlsx') {
                var nameArr = $('#file').val().split("\\")
                nameArr = nameArr[nameArr.length - 1].replace(".xlsx","")
                console.log(nameArr)
                var excel = document.getElementById("file").files[0]
                var oFReader = new FileReader();
                oFReader.readAsDataURL(excel);
                oFReader.onloadend = function (oFRevent) {
                    url = oFRevent.target.result;
                    $ajax({
                        type: 'post',
                        url: '/randomText/upload',
                        data: {
                            excel: url,
                            name: nameArr
                        },
                    }, false, '', function (res) {
                        console.log(res)
                        var thisBase = res.data
                        var nameArr = $('#file').val().split("\\")
                        nameArr = nameArr[nameArr.length - 1]
                        $('#file').val('');
                        downloadFileByBase64(nameArr, thisBase)
                        //隐藏
                        $('#loading').modal('hide');
                        swal(res.msg);
                        if (res.code == 200) {
                            getList();
                        }
                    })
                }
            } else {
                //隐藏
                $('#loading').modal('hide');
                swal("请选择正确的Excel文件！")
                $('#file').val('');
            }
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
                width: 70,
                formatter: function (value, row, index) {
                    return index + 1;
                }
            },{
                field: 'product',
                title: '产品名称',
                align: 'center',
                sortable: true,
                width: 150,
            }, {
                field: 'keyword',
                title: 'keyword',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'longword',
                title: 'longword',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'scenario',
                title: 'scenario',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'user',
                title: 'user',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'prop1',
                title: 'prop1',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'prop2',
                title: 'prop2',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'prop3',
                title: 'prop3',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'prop4',
                title: 'prop4',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'prop5',
                title: 'prop5',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'luckword',
                title: 'luckword',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'point1',
                title: 'point1',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'point2',
                title: 'point2',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'point3',
                title: 'point3',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'point4',
                title: 'point4',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'point5',
                title: 'point5',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'description',
                title: 'description',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'prop6',
                title: 'prop6',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'prop7',
                title: 'prop7',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'prop8',
                title: 'prop8',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'prop9',
                title: 'prop9',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'prop10',
                title: 'prop10',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'prop11',
                title: 'prop11',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'prop12',
                title: 'prop12',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'prop13',
                title: 'prop13',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'prop14',
                title: 'prop14',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'prop15',
                title: 'prop15',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'prop16',
                title: 'prop16',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'prop17',
                title: 'prop17',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'prop18',
                title: 'prop18',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'prop19',
                title: 'prop19',
                align: 'center',
                sortable: true,
                width: 100,
            }, {
                field: 'prop20',
                title: 'prop20',
                align: 'center',
                sortable: true,
                width: 100,
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

function dataURLtoBlob(dataurl, name) {//name:文件名
    var mime = name.substring(name.lastIndexOf('.') + 1)//后缀名
    var bstr = atob(dataurl), n = bstr.length, u8arr = new Uint8Array(n);
    while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
    }
    return new Blob([u8arr], {type: mime});
}

function downloadFile(url, name = '默认文件名') {
    var a = document.createElement("a")//创建a标签触发点击下载
    a.setAttribute("href", url)//附上
    a.setAttribute("download", name);
    a.setAttribute("target", "_blank");
    let clickEvent = document.createEvent("MouseEvents");
    clickEvent.initEvent("click", true, true);
    a.dispatchEvent(clickEvent);
}

//主函数
function downloadFileByBase64(name, base64) {
    var myBlob = dataURLtoBlob(base64, name);
    var myUrl = URL.createObjectURL(myBlob);
    downloadFile(myUrl, name)
}