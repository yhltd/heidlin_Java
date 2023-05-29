

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
        let params = {
            product:document.getElementById("add-product").value.replaceAll("\n","<br><br>"),
            keyword:document.getElementById("add-keyword").value.replaceAll("\n","<br><br>"),
            longword:document.getElementById("add-longword").value.replaceAll("\n","<br><br>"),
            scenario:document.getElementById("add-scenario").value.replaceAll("\n","<br><br>"),
            user:document.getElementById("add-user").value.replaceAll("\n","<br><br>"),
            prop1:document.getElementById("add-prop1").value.replaceAll("\n","<br><br>"),
            prop2:document.getElementById("add-prop2").value.replaceAll("\n","<br><br>"),
            prop3:document.getElementById("add-prop3").value.replaceAll("\n","<br><br>"),
            prop4:document.getElementById("add-prop4").value.replaceAll("\n","<br><br>"),
            prop5:document.getElementById("add-prop5").value.replaceAll("\n","<br><br>"),
            luckword:document.getElementById("add-luckword").value.replaceAll("\n","<br><br>"),
            point1:document.getElementById("add-point1").value.replaceAll("\n","<br><br>"),
            point2:document.getElementById("add-point2").value.replaceAll("\n","<br><br>"),
            point3:document.getElementById("add-point3").value.replaceAll("\n","<br><br>"),
            point4:document.getElementById("add-point4").value.replaceAll("\n","<br><br>"),
            point5:document.getElementById("add-point5").value.replaceAll("\n","<br><br>"),
            description:document.getElementById("add-description").value.replaceAll("\n","<br><br>"),
            prop6:document.getElementById("add-prop6").value.replaceAll("\n","<br><br>"),
            prop7:document.getElementById("add-prop7").value.replaceAll("\n","<br><br>"),
            prop8:document.getElementById("add-prop8").value.replaceAll("\n","<br><br>"),
            prop9:document.getElementById("add-prop9").value.replaceAll("\n","<br><br>"),
            prop10:document.getElementById("add-prop10").value.replaceAll("\n","<br><br>"),
            prop11:document.getElementById("add-prop11").value.replaceAll("\n","<br><br>"),
            prop12:document.getElementById("add-prop12").value.replaceAll("\n","<br><br>"),
            prop13:document.getElementById("add-prop13").value.replaceAll("\n","<br><br>"),
            prop14:document.getElementById("add-prop14").value.replaceAll("\n","<br><br>"),
            prop15:document.getElementById("add-prop15").value.replaceAll("\n","<br><br>"),
            prop16:document.getElementById("add-prop16").value.replaceAll("\n","<br><br>"),
            prop17:document.getElementById("add-prop17").value.replaceAll("\n","<br><br>"),
            prop18:document.getElementById("add-prop18").value.replaceAll("\n","<br><br>"),
            prop19:document.getElementById("add-prop19").value.replaceAll("\n","<br><br>"),
            prop20:document.getElementById("add-prop20").value.replaceAll("\n","<br><br>"),
        }
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
                let params = {
                    id:document.getElementById("id").value.replaceAll("\n","<br><br>"),
                    product:document.getElementById("update-product").value.replaceAll("\n","<br><br>"),
                    keyword:document.getElementById("update-keyword").value.replaceAll("\n","<br><br>"),
                    longword:document.getElementById("update-longword").value.replaceAll("\n","<br><br>"),
                    scenario:document.getElementById("update-scenario").value.replaceAll("\n","<br><br>"),
                    user:document.getElementById("update-user").value.replaceAll("\n","<br><br>"),
                    prop1:document.getElementById("update-prop1").value.replaceAll("\n","<br><br>"),
                    prop2:document.getElementById("update-prop2").value.replaceAll("\n","<br><br>"),
                    prop3:document.getElementById("update-prop3").value.replaceAll("\n","<br><br>"),
                    prop4:document.getElementById("update-prop4").value.replaceAll("\n","<br><br>"),
                    prop5:document.getElementById("update-prop5").value.replaceAll("\n","<br><br>"),
                    luckword:document.getElementById("update-luckword").value.replaceAll("\n","<br><br>"),
                    point1:document.getElementById("update-point1").value.replaceAll("\n","<br><br>"),
                    point2:document.getElementById("update-point2").value.replaceAll("\n","<br><br>"),
                    point3:document.getElementById("update-point3").value.replaceAll("\n","<br><br>"),
                    point4:document.getElementById("update-point4").value.replaceAll("\n","<br><br>"),
                    point5:document.getElementById("update-point5").value.replaceAll("\n","<br><br>"),
                    description:document.getElementById("update-description").value.replaceAll("\n","<br><br>"),
                    prop6:document.getElementById("update-prop6").value.replaceAll("\n","<br><br>"),
                    prop7:document.getElementById("update-prop7").value.replaceAll("\n","<br><br>"),
                    prop8:document.getElementById("update-prop8").value.replaceAll("\n","<br><br>"),
                    prop9:document.getElementById("update-prop9").value.replaceAll("\n","<br><br>"),
                    prop10:document.getElementById("update-prop10").value.replaceAll("\n","<br><br>"),
                    prop11:document.getElementById("update-prop11").value.replaceAll("\n","<br><br>"),
                    prop12:document.getElementById("update-prop12").value.replaceAll("\n","<br><br>"),
                    prop13:document.getElementById("update-prop13").value.replaceAll("\n","<br><br>"),
                    prop14:document.getElementById("update-prop14").value.replaceAll("\n","<br><br>"),
                    prop15:document.getElementById("update-prop15").value.replaceAll("\n","<br><br>"),
                    prop16:document.getElementById("update-prop16").value.replaceAll("\n","<br><br>"),
                    prop17:document.getElementById("update-prop17").value.replaceAll("\n","<br><br>"),
                    prop18:document.getElementById("update-prop18").value.replaceAll("\n","<br><br>"),
                    prop19:document.getElementById("update-prop19").value.replaceAll("\n","<br><br>"),
                    prop20:document.getElementById("update-prop20").value.replaceAll("\n","<br><br>"),
                }
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
            if ($('#file').val().substr(-5) == '.xlsx' || $('#file').val().substr(-4) == '.xls') {
                var nameArr = $('#file').val().split("\\")
                var houzhui = ""
                if($('#file').val().substr(-5) == '.xlsx'){
                    nameArr = nameArr[nameArr.length - 1].replace(".xlsx","")
                    houzhui = ".xlsx"
                }
                if($('#file').val().substr(-4) == '.xls'){
                    nameArr = nameArr[nameArr.length - 1].replace(".xls","")
                    houzhui = ".xls"
                }
                console.log(nameArr)
                var excel = document.getElementById("file").files[0]
                var oFReader = new FileReader();
                oFReader.readAsDataURL(excel);
                oFReader.onloadend = function (oFRevent) {
                    url = oFRevent.target.result;
                    var data = {
                        excel: url,
                        name: nameArr,
                        houzhui: houzhui,
                    }
                    $ajax({
                        type: 'post',
                        url: '/randomText/upload',
                        data: JSON.stringify({
                            addInfo: data,
                        }),
                        dataType: 'json',
                        contentType: 'application/json;charset=utf-8'

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
                width: 30,
                formatter: function (value, row, index) {
                    return index + 1;
                }
            },{
                field: 'product',
                title: '产品名称',
                align: 'center',
                sortable: true,
                width: 150,
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