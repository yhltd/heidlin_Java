let p_id = ""

function getList() {
    $('#this_column').val("");
    $ajax({
        type: 'post',
        url: '/textRestrictions/getList',
    }, false, '', function (res) {
        if (res.code == 200) {
            setTable(res.data)
            $("#userTable").colResizable({
                liveDrag: true,
                gripInnerHtml: "<div class='grip'></div>",
                draggingClass: "dragging",
                resizeMode: 'fit'
            });
        }
    })
}

function getUserList() {
    $('#this_column').val("");
    $ajax({
        type: 'post',
        url: '/user/getUserList',
    }, false, '', function (res) {
        if (res.code == 200) {
            console.log(res.data)
            setUserTable(res.data)
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
    // //点击新增按钮插一条空数据
    // $("#add-btn").click(function () {
    //     var params = [{
    //         username:'admin',
    //         password:'123123',
    //         name:'管理员',
    //         power:'管理员',
    //         department:'',
    //         change:'',
    //     }]
    //     $ajax({
    //         type: 'post',
    //         url: '/textRestrictions/add',
    //         data: JSON.stringify({
    //             addInfo: params,
    //         }),
    //         dataType: 'json',
    //         contentType: 'application/json;charset=utf-8'
    //     }, false, '', function (res) {
    //         if (res.code == 200) {
    //             swal("", res.msg, "success");
    //             getList();
    //         } else {
    //             swal("", res.msg, "error");
    //         }
    //     })
    // });




    //点击新增按钮显示弹窗
    $("#add-btn").click(function () {
        $ajax({
            type: 'post',
            url: '/user/getPower',
        }, false, '', function (res) {
            if (res.code == 200) {
                console.log(res.data)
                var this_power = res.data
                if(this_power == '管理员'){
                    $('#add-modal').modal('show');
                    getUserList();
                }else{
                    var params = []
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
                            $('#add-modal').modal('hide');
                            getList();
                        } else {
                            swal("", res.msg, "error");
                        }
                    })
                }
            }
        })

    });

    //新增弹窗里点击关闭按钮
    $('#add-close-btn').click(function () {
        $('#add-modal').modal('hide');
    });

    //新增弹窗里点击提交按钮
    $("#add-submit-btn").click(function () {
        let rows = getTableSelection("#userSelectTable");
        console.log(rows)
        var params = []
        for(var i=0; i<rows.length; i++){
            params.push(rows[i].data)
        }
        console.log(params)
        var msg = confirm("确认要将当前创建的商品共享给这些用户？");
        if (msg) {
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
                        $('#add-modal').modal('hide');
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
                        id: p_id,
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
                        if (res.code == 200) {
                            var thisBase = res.data
                            var nameArr = $('#file').val().split("\\")
                            nameArr = nameArr[nameArr.length - 1]
                            $('#file').val('');
                            downloadFileByBase64(nameArr, thisBase)
                            //隐藏
                            $('#loading').modal('hide');
                            swal("", res.msg, "success");
                            getList();
                        }else{
                            $('#loading').modal('hide');
                            swal("", res.msg, "error");
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
                formatter: function (value, row, index) {
                    var this_columntext = row.product
                    if(this_columntext == null){
                        this_columntext = ""
                    }
                    return '<input id="product' + row.id + '" oninput="javascript:columnUpd(' + row.id + ',' + '\'product\'' + ',' + row.textId + ')" value="' + this_columntext + '" class="form-control"  >'
                }
            }, {
                field: '',
                title: '操作',
                align: 'center',
                sortable: true,
                width:150,
                formatter: function (value, row, index) {
                    return '<button onclick="javascript:pass(' + row.id + ')" class="btn-sm btn-primary">打开模板</button> <button onclick="javascript:upload_file(' + row.id + ')" class="btn-sm btn-primary">上传</button>'                }
            }, {
                field: 'columntext',
                title: '列标/数量规定',
                align: 'center',
                sortable: true,
                width:150,
                formatter: function (value, row, index) {
                    var this_columntext = row.columntext1
                    var this_num = row.num1
                    if(this_columntext == null){
                        this_columntext = ""
                    }
                    if(this_num == null){
                        this_num = ""
                    }
                    return '<input id="columntext1' + row.id + '" oninput="javascript:columnUpd(' + row.id + ',' + '\'columntext1\'' + ')" value="' + this_columntext + '" class="form-control"  >' + "<HR style=\"FILTER: alpha(opacity=100,finishopacity=0,style=3)\" width=\"100%\" color=#dee2e6 SIZE=3>" + '<input id="num1' + row.id + '" type="number" oninput="javascript:columnUpd(' + row.id + ',' + '\'num1\'' + ')" value="' + this_num + '" class="form-control"  >'
                }
            }, {
                field: 'columntext',
                title: '列标/数量规定',
                align: 'center',
                sortable: true,
                width:150,
                formatter: function (value, row, index) {
                    var this_columntext = row.columntext2
                    var this_num = row.num2
                    if(this_columntext == null){
                        this_columntext = ""
                    }
                    if(this_num == null){
                        this_num = ""
                    }
                    return '<input id="columntext2' + row.id + '" oninput="javascript:columnUpd(' + row.id + ',' + '\'columntext2\'' + ')" value="' + this_columntext + '" class="form-control"  >' + "<HR style=\"FILTER: alpha(opacity=100,finishopacity=0,style=3)\" width=\"100%\" color=#dee2e6 SIZE=3>" + '<input id="num2' + row.id + '" type="number" oninput="javascript:columnUpd(' + row.id + ',' + '\'num2\'' + ')" value="' + this_num + '" class="form-control"  >'                }
            }, {
                field: 'columntext',
                title: '列标/数量规定',
                align: 'center',
                sortable: true,
                width:150,
                formatter: function (value, row, index) {
                    var this_columntext = row.columntext3
                    var this_num = row.num3
                    if(this_columntext == null){
                        this_columntext = ""
                    }
                    if(this_num == null){
                        this_num = ""
                    }
                    return '<input id="columntext3' + row.id + '" oninput="javascript:columnUpd(' + row.id + ',' + '\'columntext3\'' + ')" value="' + this_columntext + '" class="form-control"  >' + "<HR style=\"FILTER: alpha(opacity=100,finishopacity=0,style=3)\" width=\"100%\" color=#dee2e6 SIZE=3>" + '<input id="num3' + row.id + '" type="number" oninput="javascript:columnUpd(' + row.id + ',' + '\'num3\'' + ')" value="' + this_num + '" class="form-control"  >'                }
            }, {
                field: 'columntext',
                title: '列标/数量规定',
                align: 'center',
                sortable: true,
                width:150,
                formatter: function (value, row, index) {
                    var this_columntext = row.columntext4
                    var this_num = row.num4
                    if(this_columntext == null){
                        this_columntext = ""
                    }
                    if(this_num == null){
                        this_num = ""
                    }
                    return '<input id="columntext4' + row.id + '" oninput="javascript:columnUpd(' + row.id + ',' + '\'columntext4\'' + ')" value="' + this_columntext + '" class="form-control"  >' + "<HR style=\"FILTER: alpha(opacity=100,finishopacity=0,style=3)\" width=\"100%\" color=#dee2e6 SIZE=3>" + '<input id="num4' + row.id + '" type="number" oninput="javascript:columnUpd(' + row.id + ',' + '\'num4\'' + ')" value="' + this_num + '" class="form-control"  >'                }
            }, {
                field: 'columntext',
                title: '列标/数量规定',
                align: 'center',
                sortable: true,
                width:150,
                formatter: function (value, row, index) {
                    var this_columntext = row.columntext5
                    var this_num = row.num5
                    if(this_columntext == null){
                        this_columntext = ""
                    }
                    if(this_num == null){
                        this_num = ""
                    }
                    return '<input id="columntext5' + row.id + '" oninput="javascript:columnUpd(' + row.id + ',' + '\'columntext5\'' + ')" value="' + this_columntext + '" class="form-control"  >' + "<HR style=\"FILTER: alpha(opacity=100,finishopacity=0,style=3)\" width=\"100%\" color=#dee2e6 SIZE=3>" + '<input id="num5' + row.id + '" type="number" oninput="javascript:columnUpd(' + row.id + ',' + '\'num5\'' + ')" value="' + this_num + '" class="form-control"  >'                }
            }, {
                field: 'columntext',
                title: '列标/数量规定',
                align: 'center',
                sortable: true,
                width:150,
                formatter: function (value, row, index) {
                    var this_columntext = row.columntext6
                    var this_num = row.num6
                    if(this_columntext == null){
                        this_columntext = ""
                    }
                    if(this_num == null){
                        this_num = ""
                    }
                    return '<input id="columntext6' + row.id + '" oninput="javascript:columnUpd(' + row.id + ',' + '\'columntext6\'' + ')" value="' + this_columntext + '" class="form-control"  >' + "<HR style=\"FILTER: alpha(opacity=100,finishopacity=0,style=3)\" width=\"100%\" color=#dee2e6 SIZE=3>" + '<input id="num6' + row.id + '" type="number" oninput="javascript:columnUpd(' + row.id + ',' + '\'num6\'' + ')" value="' + this_num + '" class="form-control"  >'                }
            }, {
                field: 'columntext',
                title: '列标/数量规定',
                align: 'center',
                sortable: true,
                width:150,
                formatter: function (value, row, index) {
                    var this_columntext = row.columntext7
                    var this_num = row.num7
                    if(this_columntext == null){
                        this_columntext = ""
                    }
                    if(this_num == null){
                        this_num = ""
                    }
                    return '<input id="columntext7' + row.id + '" oninput="javascript:columnUpd(' + row.id + ',' + '\'columntext7\'' + ')" value="' + this_columntext + '" class="form-control"  >' + "<HR style=\"FILTER: alpha(opacity=100,finishopacity=0,style=3)\" width=\"100%\" color=#dee2e6 SIZE=3>" + '<input id="num7' + row.id + '" type="number" oninput="javascript:columnUpd(' + row.id + ',' + '\'num7\'' + ')" value="' + this_num + '" class="form-control"  >'                }
            }, {
                field: 'columntext',
                title: '列标/数量规定',
                align: 'center',
                sortable: true,
                width:150,
                formatter: function (value, row, index) {
                    var this_columntext = row.columntext8
                    var this_num = row.num8
                    if(this_columntext == null){
                        this_columntext = ""
                    }
                    if(this_num == null){
                        this_num = ""
                    }
                    return '<input id="columntext8' + row.id + '" oninput="javascript:columnUpd(' + row.id + ',' + '\'columntext8\'' + ')" value="' + this_columntext + '" class="form-control"  >' + "<HR style=\"FILTER: alpha(opacity=100,finishopacity=0,style=3)\" width=\"100%\" color=#dee2e6 SIZE=3>" + '<input id="num8' + row.id + '" type="number" oninput="javascript:columnUpd(' + row.id + ',' + '\'num8\'' + ')" value="' + this_num + '" class="form-control"  >'                }
            }, {
                field: 'columntext',
                title: '列标/数量规定',
                align: 'center',
                sortable: true,
                width:150,
                formatter: function (value, row, index) {
                    var this_columntext = row.columntext9
                    var this_num = row.num9
                    if(this_columntext == null){
                        this_columntext = ""
                    }
                    if(this_num == null){
                        this_num = ""
                    }
                    return '<input id="columntext9' + row.id + '" oninput="javascript:columnUpd(' + row.id + ',' + '\'columntext9\'' + ')" value="' + this_columntext + '" class="form-control"  >' + "<HR style=\"FILTER: alpha(opacity=100,finishopacity=0,style=3)\" width=\"100%\" color=#dee2e6 SIZE=3>" + '<input id="num9' + row.id + '" type="number" oninput="javascript:columnUpd(' + row.id + ',' + '\'num9\'' + ')" value="' + this_num + '" class="form-control"  >'                }
            }, {
                field: 'columntext',
                title: '列标/数量规定',
                align: 'center',
                sortable: true,
                width:150,
                formatter: function (value, row, index) {
                    var this_columntext = row.columntext10
                    var this_num = row.num10
                    if(this_columntext == null){
                        this_columntext = ""
                    }
                    if(this_num == null){
                        this_num = ""
                    }
                    return '<input id="columntext10' + row.id + '" oninput="javascript:columnUpd(' + row.id + ',' + '\'columntext10\'' + ')" value="' + this_columntext + '" class="form-control"  >' + "<HR style=\"FILTER: alpha(opacity=100,finishopacity=0,style=3)\" width=\"100%\" color=#dee2e6 SIZE=3>" + '<input id="num10' + row.id + '" type="number" oninput="javascript:columnUpd(' + row.id + ',' + '\'num10\'' + ')" value="' + this_num + '" class="form-control"  >'                }
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
    var aa = $.session.get('ee')
    if(aa!=''){
        $('#userTable').bootstrapTable('selectPage',aa * 1)
        $.session.set('ee', '')

    }else{
        return false
    }

}

function setUserTable(data) {
    if ($('#userSelectTable').html != '') {
        $('#userSelectTable').bootstrapTable('load', data);
    }

    $('#userSelectTable').bootstrapTable({
        data: data,
        sortStable: true,
        classes: 'table table-hover text-nowrap table table-bordered',
        idField: 'id',
        pagination: true,
        pageSize: 15,//单页记录数
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
            },{
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
            }
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


function pass(id) {
    $.session.set('id', id)
    console.log(pass)
    document.location.href = 'randomText.html'
    var ee = $("#userTable").bootstrapTable("getOptions").pageNumber;
    console.log(ee)
    $.session.set('ee', ee)
}


function upload_file(id) {
    p_id = id
    $('#file').trigger('click');
}


function columnUpd(id, column) {
    var this_value = $('#' + column + id).val();
    $ajax({
        type: 'post',
        url: '/textRestrictions/update',
        data: {
            column: column,
            id: id,
            value: this_value,
        },
    }, true, '', function (res) {
        // alert(res.msg);
        if (res.code == 200) {
            var obj = ""
            if (res.msg == '修改成功') {
                obj = document.getElementById("upd_1");
            } else{
                obj = document.getElementById("upd_2");
                getList();
            }
            obj.hidden = false
            setTimeout(function () {
                obj.hidden = true
            }, 3000);

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