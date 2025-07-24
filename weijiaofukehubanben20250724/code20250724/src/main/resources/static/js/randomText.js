

function getList() {
    $('#this_column').val("");
    $ajax({
        type: 'post',
        url: '/randomText/getList',
        data: {
            id: $.session.get('id'),
        },
    }, false, '', function (res) {
        if (res.code == 200) {
            setForm(res.data[0], '#update-form');
        }
    })
}

$(function () {
    getList();

    //修改弹窗里点击提交按钮
    $('#update-submit-btn').click(function () {
        var msg = confirm("确认保存吗？");
        if (msg) {
            if (checkForm('#update-form')) {
                let params = {
                    id:document.getElementById("id").value.replaceAll("\n","<br><br>"),
                    title:document.getElementById("update-title").value.replaceAll("\n","<br><br>"),
                    titleColor:document.getElementById("update-titleColor").value.replaceAll("\n","<br><br>"),
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


    //关闭按钮
    $('#update-close-btn').click(function () {
        document.location.href = 'textRestrictions.html'
    });

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