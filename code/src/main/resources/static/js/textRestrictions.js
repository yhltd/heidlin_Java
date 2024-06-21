let p_id = ""
let founder = ""

function getList() {
    //打开页面隐藏查看共享按钮
    $ajax({
        type: 'post',
        url: '/user/getPower',
    }, false, '', function (res) {
        var this_power = res.data
        if(this_power != '管理员') {
            $('#share-btn').hide();
            $('#share-del-btn').hide();
            $('#share-set-btn').hide();
            $('#share-setting').hide();
        }
    })
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

function getUserList11() {
    $('#this_column').val("");
    $ajax({
        type: 'post',
        url: '/user/getUserAll',
    }, false, '', function (res) {
        if (res.code == 200) {
            console.log(res.data)
            setUsers(res.data)
        }
    })
    // $ajax({
    //     type: 'post',
    //     url: '/user/getTextR',
    // }, false, '', function (res) {
    //     console.log("11111111111", res.data)
    //     if (res.code == 200) {
    //         console.log(res.data)
    //         setUsers(res.data)
    //     }
    // })
}

function getUserList1() {
    $('#this_column').val("");
    $ajax({
        type: 'post',
        url: '/user/getUserList',
    }, false, '', function (res) {
        if (res.code == 200) {
            console.log(res.data)
            setUserTable1(res.data)
        }
    })
}

function getUserList2() {
    $('#this_column').val("");
    $ajax({
        type: 'post',
        url: '/user/getUserList',
    }, false, '', function (res) {
        if (res.code == 200) {
            console.log(res.data)
            setUserTable2(res.data)
        }
    })
}

function getUserList3() {
    $('#this_column').val("");
    $ajax({
        type: 'post',
        url: '/user/getUserList',
    }, false, '', function (res) {
        if (res.code == 200) {
            console.log(res.data)
            setUserTable3(res.data)
        }
    })
}

// function getUserList4() {
//     $('#this_column').val("");
//     $ajax({
//         type: 'post',
//         url: '/textRestrictions/getUserList',
//     }, false, '', function (res) {
//         if (res.code == 200) {
//             console.log(res.data)
//             setUserTable4(res.data)
//         }
//     })
// }

function getListFounder1() {
    let rows = getTableSelection("#userSelectTable1");
    var params = []
    // var this_id = []
    for(var i=0; i<rows.length; i++){
        params.push(rows[i].data)
        // this_id.push(rows[i].data.id)
        var this_id = rows[i].data.id
    }
    $ajax({
        type: 'post',
        url: '/textRestrictions/getListByUser',
        data:{
            this_id: this_id,
        }
    }, false, '', function (res) {
        if (res.code == 200) {
            console.log(res.data)
            setTextTable1(res.data)
            $("#textSelectTable1").colResizable({
                liveDrag: true,
                gripInnerHtml: "<div class='grip'></div>",
                draggingClass: "dragging",
                resizeMode: 'fit'
            });
        }
    })
}

function getListFounder2() {
        let rows = getTableSelection("#userSelectTable2");
        var params = []
        for(var i=0; i<rows.length; i++){
        params.push(rows[i].data)
        var this_id = rows[i].data.id
    }
    $ajax({
        type: 'post',
        url: '/textRestrictions/getListByUser',
        data:{
            this_id: this_id,
        }
    }, false, '', function (res) {
        if (res.code == 200) {
            console.log(res.data)
            setTextTable2(res.data)
            $("#textSelectTable2").colResizable({
                liveDrag: true,
                gripInnerHtml: "<div class='grip'></div>",
                draggingClass: "dragging",
                resizeMode: 'fit'
            });
        }
    })
}

function getListFounder3() {
    let rows = getTableSelection("#userSelectTable3");
    var params = []
    var founder = []
    for(var i=0; i<rows.length; i++){
        params.push(rows[i].data)
        founder.push(rows[i].data.id)
    }
    $ajax({
        type: 'post',
        url: '/textRestrictions/getList',
        data:{
            founder: founder,
        }
    }, false, '', function (res) {
        if (res.code == 200) {
            console.log(res.data)
            setTextTable3(res.data)
            $("#textSelectTable3").colResizable({
                liveDrag: true,
                gripInnerHtml: "<div class='grip'></div>",
                draggingClass: "dragging",
                resizeMode: 'fit'
            });
        }
    })
}

// function getListFounder4() {
//     let rows = getTableSelection("#userSelectTable4");
//     var params = []
//     for(var i=0; i<rows.length; i++){
//         params.push(rows[i].data)
//         var founder = rows[i].data.id
//     }
//     $ajax({
//         type: 'post',
//         url: '/textRestrictions/getShareByFounder',
//         data:{
//             founder: founder
//         }
//     }, false, '', function (res) {
//         if (res.code == 200) {
//             console.log(res.data)
//             setTextTable4(res.data)
//             $("#textSelectTable4").colResizable({
//                 liveDrag: true,
//                 gripInnerHtml: "<div class='grip'></div>",
//                 draggingClass: "dragging",
//                 resizeMode: 'fit'
//             });
//         }
//     })
// }

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

    // //点击设置按钮显示弹窗
    // $("#share-setting").click(function () {
    //     $ajax({
    //         type: 'post',
    //         url: '/user/getPower',
    //     }, false, '', function (res) {
    //         if (res.code == 200) {
    //             console.log("设置", res.data)
    //             var this_power = res.data
    //             if(this_power == '管理员'){
    //                 $('#setting-modal').modal('show');
    //                 getUserList11();
    //             }else{
    //                 var params = []
    //                 $ajax({
    //                     type: 'post',
    //                     url: '/textRestrictions/add',
    //                     data: JSON.stringify({
    //                         addInfo: params,
    //                     }),
    //                     dataType: 'json',
    //                     contentType: 'application/json;charset=utf-8'
    //                 }, false, '', function (res) {
    //                     if (res.code == 200) {
    //                         swal("", res.msg, "success");
    //                         $('#setting-modal').modal('hide');
    //                         getList();
    //                     } else {
    //                         swal("", res.msg, "error");
    //                     }
    //                 })
    //             }
    //         }
    //     })
    //
    // });

    //点击查询共享按钮显示弹窗
    $("#share-btn").click(function () {
        $ajax({
            type: 'post',
            url: '/user/getPower',
        }, false, '', function (res) {
            if (res.code == 200) {
                console.log(res.data)
                var this_power = res.data
                if(this_power == '管理员'){
                    $('#share-modal').modal('show');
                    getUserList1();
                }else{
                    var params = []
                    $ajax({
                        type: 'post',
                        url: '/user/queryList',
                        data: JSON.stringify({
                            addInfo: params,
                        }),
                        dataType: 'json',
                        contentType: 'application/json;charset=utf-8'
                    }, false, '', function (res) {
                        if (res.code == 200) {
                            swal("", res.msg, "success");
                            $('#share-modal').modal('hide');
                            getList();
                        } else {
                            swal("", res.msg, "error");
                        }
                    })
                }
            }
        })

    });

    // 点击删除共享按钮显示弹窗
    $("#share-del-btn").click(function () {
        $ajax({
            type: 'post',
            url: '/user/getPower',
        }, false, '', function (res) {
            if (res.code == 200) {
                console.log(res.data)
                var this_power = res.data
                if(this_power == '管理员'){
                    $('#share-del-modal').modal('show');
                    getUserList2();
                }else{
                    var params = []
                    $ajax({
                        type: 'post',
                        url: '/user/queryList',
                        data: JSON.stringify({
                            addInfo: params,
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
            }
        })

    });

    //点击设置共享按钮显示弹窗
    $("#share-set-btn").click(function () {
        $ajax({
            type: 'post',
            url: '/user/getPower',
        }, false, '', function (res) {
            if (res.code == 200) {
                console.log(res.data)
                var this_power = res.data
                if(this_power == '管理员'){
                    $('#share-set-modal').modal('show');
                    getUserList3();
                }else{
                    var params = []
                    $ajax({
                        type: 'post',
                        url: '/user/queryList',
                        data: JSON.stringify({
                            addInfo: params,
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
            }
        })
    });

    //点击设置账号产品按钮显示弹窗
    $("#share-setting").click(function () {
        // //查询子账号
        // $ajax({
        //     type: 'post',
        //     url: '/user/userXx',
        // }, false, '', function (res) {
        //     console.log("res", res)
        //         if (res.code == 200){
        //             setUserAccount(res.data)
        //             $('#small-account').modal('show');
        //         }else {
        //             confirm("网络错误，请重试")
        //         }
        // })
        //查询管理员产品
        $ajax({
            type: 'post',
            url: '/user/getTextR',
        }, false, '', function (res) {
            if (res.code == 200){
                setAdminPc(res.data)
                $('#admin-prodect').modal('show');
            }else {
                confirm("网络错误，请重试")
            }
        })
        $('#account-product-modal').modal('show');
    });

    //关闭设置账号产品弹窗
    $("#close-all").click(function () {
        // var ids = [];
        // var userName = null;
        // $ajax({
        //     type: 'post',
        //     url: '/user/info',
        //     data : {
        //         ids :JSON.stringify(ids),
        //         userName: userName
        //     },
        // }, false, '', function (res) {
        //     if (res.code == 200) {
        //         $('#account-product-modal').modal('hide');
        //         $('#admin-prodect').modal('hide');
        //         $('#pu-have-prodect').modal('hide');
        //         $('#small-account').modal('hide');
        //     }else {
        //         $('#account-product-modal').modal('hide');
        //         $('#admin-prodect').modal('hide');
        //         $('#pu-have-prodect').modal('hide');
        //         $('#small-account').modal('hide');
        //     }
        // })
        $('#yy-account-product').modal('hide');
        $('#Wyy-account-product').modal('hide');
        $('#account-product-modal').modal('hide');
        $('#admin-prodect').modal('hide');
    })

    //点击查询产品按钮
    $("#add-small-account").click(function () {
    let rows = getTableSelection("#select-admin-product");
    if (rows.length == 0){
        swal({
            title : "请选择管理员产品",
            type : "error",
            confirmButtonText : "确定",
            closeOnConfirm : false
        });
    }else if (rows.length == 1){
        $ajax({
            type: 'post',
            url: '/user/getAccountProduct',
            data:{
                id : rows[0].data.id
            }
        }, false, '', function (res) {
            console.log("res", res)
            if (res.code == 200){
                setYyAccount(res.data.userList)
                $('#yy-account-product').modal('show');
                setWyyAccount(res.data.userInfoList)
                $('#Wyy-account-product').modal('show');
            }else {
                swal("", res.msg, "error");
            }
        })
    }else {
            swal({
            title : "请选择一个管理员产品",
            type : "error",
            confirmButtonText : "确定",
            closeOnConfirm : false
            });
        }
    })


    // //设置账号产品弹窗里点击查询该子账号按钮
    // $("#add-small-account").click(function () {
    //     let rows = getTableSelection("#select-samll-account");
    //     if (rows.length == 0){
    //         swal({
    //             title : "请选择要查询的子账号",
    //             type : "error",
    //             confirmButtonText : "确定",
    //             closeOnConfirm : false
    //         });
    //     }else if (rows.length == 1){
    //         $ajax({
    //             type: 'post',
    //             url: '/user/getCpByUserId',
    //             data:{
    //                 userId : rows[0].data.id
    //             }
    //         }, false, '', function (res) {
    //             if (res.code == 200){
    //                 setPuPc(res.data)
    //                 $('#pu-have-prodect').modal('show');
    //             }else {
    //                 swal("", res.msg, "error");
    //             }
    //         })
    //     }else {
    //         swal({
    //             title : "请选择一个子账号查询",
    //             type : "error",
    //             confirmButtonText : "确定",
    //             closeOnConfirm : false
    //         });
    //     }
    // })

    //单击添加至该子账号产品内按钮
    $("#add-small-product").click(function () {
            let rows = getTableSelection("#select-Wyy-product");
            let adminPId = getTableSelection("#select-admin-product");
            if (rows.length == 0){
                swal({
                    title : "请选择要共享的子账号！",
                    type : "error",
                    confirmButtonText : "确定",
                    closeOnConfirm : false
                });
            }else {
                var ids = [];
                for (let i = 0; i < rows.length; i++) {
                    ids.push(rows[i].data.id)
                }
                console.log("ids", ids)
                var msg = confirm("确定共享产品到已选中的子账号中么？");
                if (msg){
                    $ajax({
                        type: 'post',
                        url: '/user/info',
                        data : {
                            ids :JSON.stringify(ids),
                            id : adminPId[0].data.id
                        },
                    }, false, '', function (res) {
                        if (res.code == 200) {
                            swal("", res.msg, "success");
                            $('#yy-account-product').modal('hide');
                            $('#Wyy-account-product').modal('hide');
                            $('#account-product-modal').modal('hide');
                            $('#admin-prodect').modal('hide');

                        }else {
                            swal("", res.msg, "error");
                        }
                    })
                }
            }
    })
    //单击删除该子账号产品内按钮
    $("#delete-small-product").click(function () {
            let rows = getTableSelection("#select-yy-product");
            let adminPId = getTableSelection("#select-admin-product");
            if (rows.length == 0){
                swal({
                    title : "请选择要取消的子账号",
                    type : "error",
                    confirmButtonText : "确定",
                    closeOnConfirm : false
                });
            }else {
                var ids = [];
                for (let i = 0; i < rows.length; i++) {
                    ids.push(rows[i].data.id)
                }
                console.log("ids", ids)
                var msg = confirm("确定取消共享已选中的子账号么？");
                if (msg){
                    $ajax({
                        type: 'post',
                        url: '/user/deleteCp',
                        data : {
                            ids :JSON.stringify(ids),
                            id : adminPId[0].data.id
                        },
                    }, false, '', function (res) {
                        if (res.code == 200) {
                            swal("", res.msg, "success");
                            $('#yy-account-product').modal('hide');
                            $('#Wyy-account-product').modal('hide');
                            $('#account-product-modal').modal('hide');
                            $('#admin-prodect').modal('hide');
                        }else {
                            swal("", res.msg, "error");
                        }
                    })
                }
            }
    })


    //新增弹窗里点击关闭按钮
    $('#add-close-btn').click(function () {
        $('#add-modal').modal('hide');
    });

    //设置弹窗里点击关闭按钮
    $('#setting-close-btn').click(function () {
        $('#setting-modal').modal('hide');
    });

    //新增弹窗里点击关闭按钮
    $('#share-close-btn').click(function () {
        $('#share-modal').modal('hide');
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

    //设置弹窗里点击提交按钮
    $("#setting-submit-btn").click(function () {
        let rows = getTableSelection("#selectUser");
        console.log(rows)
        var params = []
        for(var i=0; i<rows.length; i++){
            params.push(rows[i].data)
        }
    });

    //删除弹窗里点击提交按钮
    $("#share-del-submit-btn").click(function () {
        let rows = getTableSelection("#userSelectTable2");
        console.log(rows)
        var params = []
        var founder = []
        for(var i=0; i<rows.length; i++){
            params.push(rows[i].data)
            founder.push(rows[i].data.founder)
        }
        $ajax({
            type: 'post',
            url: '/textRestrictions/getList',
            data: JSON.stringify({
                addInfo: params,
                founder: founder
            }),
            dataType: 'json',
            contentType: 'application/json;charset=utf-8'
        }, false, '', function (res) {
            if (res.code == 200) {
                swal("", res.msg, "success");
                $('#share-del-modal1').modal('show');
                getListFounder2();
            } else {
                swal("", res.msg, "error");
            }
        })
    });

    $("#share-set-submit-btn").click(function () {
        let rows = getTableSelection("#userSelectTable3");
        console.log(rows)
        var textList = []
        // var id = []
        for(var i=0; i<rows.length; i++){
            textList.push(rows[i].data.textList)
            // id.push(rows[i].data.founder)
        }
        $ajax({
            type: 'post',
            url: '/textRestrictions/queryList',
            data: JSON.stringify({
                textList: textList,
                // founder: id
            }),
            dataType: 'json',
            contentType: 'application/json;charset=utf-8'
        }, false, '', function (res) {
            // if (res.code == 200) {
            //     swal("", res.msg, "success");
                $('#share-set-modal1').modal('show');
                getListFounder3();
            // } else {
            //     swal("", res.msg, "error");
            // }
        })
    });

    //新增弹窗里点击提交按钮
    $("#share-del-submit-btn1").click(function () {
        let rows = getTableSelection("#textSelectTable2");
        console.log(rows)
        let idList = [];
        $.each(rows, function (index, row) {
            idList.push(row.data.id)
        });

        var msg = confirm("确认要删除当前选择用户的产品列表？");
        if (msg) {
            $ajax({
                type: 'post',
                url: '/textRestrictions/deleteById',
                data: JSON.stringify({
                    idList: idList
                }),
                dataType: 'json',
                contentType: 'application/json;charset=utf-8'
            }, false, '', function (res) {
                if (res.code == 200) {
                    swal("", res.msg, "success");
                    $('#share-del-modal').modal('hide');
                    $('#share-del-modal1').modal('hide');
                    getList();
                } else {
                    swal("", res.msg, "error");
                }
            })
        }
    });

    $("#share-set-submit-btn1").click(function () {
        let rows = getTableSelection("#textSelectTable3");
        console.log(rows)
        var textList = []
        for(var i=0; i<rows.length; i++){
            textList.push(rows[i].data)
        }

        let rows1 = getTableSelection("#userSelectTable3");
        console.log(rows1)
        let id = [];
        // var founder = ""
        for(var i=0; i<rows1.length; i++){
            id.push(rows1[i].data.id)
        }

        console.log(textList)
        console.log(id)
        var msg = confirm("确认要将当前创建的商品共享给这些用户？");
        if (msg) {
            if (checkForm('#add-form')) {
                $ajax({
                    type: 'post',
                    url: '/textRestrictions/insertShare',
                    data: JSON.stringify({
                        textList: textList,
                        id: id
                    }),
                    dataType: 'json',
                    contentType: 'application/json;charset=utf-8'
                }, false, '', function (res) {
                    if (res.code == 200) {
                        swal("", res.msg, "success");
                        $('#share-set-modal').modal('hide');
                        $('#share-set-modal1').modal('hide');
                        getList();
                    } else {
                        swal("", res.msg, "error");
                    }
                })
            }
        }
    });

    //新增弹窗里点击提交按钮
    $("#share-submit-btn").click(function () {
        let rows = getTableSelection("#userSelectTable1");
        if (rows.length > 1 || rows.length == 0) {
            swal('请选择一个子账号查询!');
            return;
        }
        var params = []
        for(var i=0; i<rows.length; i++){
            params.push(rows[i].data)
            var this_id = rows[i].data.id
        }
        var msg = confirm("确认查看当前选中子账号的产品列表？");
        if (msg) {
            if (checkForm('#add-form')) {
                $ajax({
                    type: 'post',
                    url: '/textRestrictions/getListByUser',
                    data: JSON.stringify({
                        this_id: this_id,
                    }),
                    dataType: 'json',
                    contentType: 'application/json;charset=utf-8'
                }, false, '', function (res) {
                    if (res.code == 200) {
                        swal("", res.msg, "success");
                        $('#share-modal').modal('hide');
                        $('#share1-modal').modal('show');
                        getListFounder1();
                    } else {
                        swal("", res.msg, "error");
                    }
                })
            }
        }
    });

    // 点击设置子账号按钮显示弹窗
    // $("#set-del-share-btn").click(function () {
    //     $('#set-del-share-modal').modal('show');
    //     getUserList4();
    // });

    //设置子账号弹窗里点击查看共享按钮
    // $("#sel-submit-btn").click(function () {
    //     let rows = getTableSelection("#userSelectTable4");
    //     if (rows.length > 1 || rows.length == 0) {
    //         swal('请选择一个子账号查看!');
    //         return;
    //     }
    //     var params = []
    //     for(var i=0; i<rows.length; i++){
    //         params.push(rows[i].data)
    //         var founder = rows[i].data.id
    //     }
    //     console.log(founder)
    //     var msg = confirm("确认查看当前选中子账号的产品列表？");
    //     if (msg) {
    //         $ajax({
    //             type: 'post',
    //             url: '/textRestrictions/getShareByFounder',
    //             data: JSON.stringify({
    //                 id: founder
    //             }),
    //             dataType: 'json',
    //             contentType: 'application/json;charset=utf-8'
    //         }, false, '', function (res) {
    //             if (res.code == 200) {
    //                 swal("", res.msg, "success");
    //                 getListFounder4();
    //             } else {
    //                 swal("", res.msg, "error");
    //             }
    //         })
    //     }
    // });

    //设置子账号弹窗里点击删除按钮
    // $("#del-submit-btn").click(function () {
    //     let rows = getTableSelection("#textSelectTable4");
    //     if (rows.length == 0) {
    //         swal('请选择需要删除的产品!');
    //         return;
    //     }
    //     let founder = [];
    //     let id = [];
    //     $.each(rows, function (index, row) {
    //         founder.push(row.data.founder)
    //         id.push(row.data.id)
    //     });
    //     console.log(founder)
    //     var msg = confirm("确认删除选中子账号的产品？");
    //     if (msg) {
    //         $ajax({
    //             type: 'post',
    //             url: '/textRestrictions/deleteByFounder',
    //             data: JSON.stringify({
    //                 founder: founder,
    //                 id: id
    //             }),
    //             dataType: 'json',
    //             contentType: 'application/json;charset=utf-8'
    //         }, false, '', function (res) {
    //             // if (res.code == 200) {
    //             //     swal("", res.msg, "success");
    //                 getListFounder4();
    //             // } else {
    //             //     swal("", res.msg, "error");
    //             // }
    //         })
    //     }
    // });

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

function setYyAccount(data) {
    if ($('#select-yy-product').html != '') {
        $('#select-yy-product').bootstrapTable('load', data);
    }
    $('#select-yy-product').bootstrapTable({
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
                field: 'username',
                title: '拥有该产品的子账号',
                align: 'center',
                sortable: true,
                width: 80,
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
    })
}

function setWyyAccount(data) {
    if ($('#select-Wyy-product').html != '') {
        $('#select-Wyy-product').bootstrapTable('load', data);
    }
    $('#select-Wyy-product').bootstrapTable({
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
                field: 'username',
                title: '未拥有该产品的子账号',
                align: 'center',
                sortable: true,
                width: 80,
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
    })
}

function setUserAccount(data) {
    if ($('#select-samll-account').html != '') {
        $('#select-samll-account').bootstrapTable('load', data);
    }
    $('#select-samll-account').bootstrapTable({
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
                field: 'username',
                title: '子账号',
                align: 'center',
                sortable: true,
                width: 80,
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
    })
}



function setAdminPc(data) {
    if ($('#select-admin-product').html != '') {
        $('#select-admin-product').bootstrapTable('load', data);
    }

    $('#select-admin-product').bootstrapTable({
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
                field: 'product',
                title: '管理员产品',
                align: 'center',
                sortable: true,
                width: 80,
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
    })
}



function setPuPc(data) {
    if ($('#select-pu-product').html != '') {
        $('#select-pu-product').bootstrapTable('load', data);
    }
    $('#select-pu-product').bootstrapTable({
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
            },
            {
                field: 'product',
                title: '子账号产品',
                align: 'center',
                sortable: true,
                width: 80,
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
    })
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

function setUsers(data) {
    $('#selectUser').bootstrapTable({
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
                width: 40,
                formatter: function (value, row, index) {
                    return index + 1;
                }
            }, {
                field: 'username',
                title: '子账号',
                align: 'center',
                sortable: true,
                width: 80,
            },
            {
                field: '',
                title: '操作',
                align: 'center',
                sortable: true,
                width:40,
                formatter: function (value, row) {
                    return '<button onclick="javascript:getCpById(' + row.id + ')" class="btn-sm btn-primary">产品详细</button>'}
            },
        ],
    })
}

function getCpById(id){
    $ajax({
        type: 'post',
        url: '/user/getCpById',
        data : {
            id : id
        }
    }, false, '', function (res) {
        $('#setting-product').modal('show');
        $('#selectCpByUser').bootstrapTable({
            data: res.data,
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
                    width: 40,
                    formatter: function (value, row, index) {
                        return index + 1;
                    }
                }, {
                    field: 'product',
                    title: '产品',
                    align: 'center',
                    sortable: true,
                    width: 80,
                },
                {
                    field: '',
                    title: '操作',
                    align: 'center',
                    sortable: true,
                    width:40,
                    formatter: function (value, row) {
                        return '<button onclick="javascript:deleteCpById(' + row.id + ')" class="btn btn-danger">删除</button>'}
                },
            ],
        })
    })
    //点击添加产品
    $("#add-product").click(function () {
        $('#show-products').modal('show');
    })
    $ajax({
            type: 'post',
            url: '/user/getTextR',
        }, false, '', function (res) {
        setAdmin(res.data)
    })
            $("#add-prodect-submit").click(function () {
                let rows = getTableSelection("#selectAllByAdmin");
                var ids = [];
                for (let i = 0; i < rows.length; i++) {
                    ids.push(rows[i].data.id)
                }
                console.log("ids", ids)
                var msg = confirm("确定添加已选中的产品到该子账号中么？");
                if (msg){
                    $ajax({
                        type: 'post',
                        url: '/user/info',
                        data : {
                            ids :JSON.stringify(ids),
                            userId: id
                        },
                    }, false, '', function (res) {
                        if (res.code == 200) {
                            swal("", res.msg, "success");
                            $('#setting-modal').modal('hide');
                            $('#setting-product').modal('hide');
                            $('#show-products').modal('hide');
                        }else {
                            swal("", res.msg, "error");
                        }
                    })
                }
            })
}

function setAdmin(data) {
    if ($('#selectAllByAdmin').html != '') {
        $('#selectAllByAdmin').bootstrapTable('load', data);
    }
    $('#selectAllByAdmin').bootstrapTable({
        data: data,
        sortStable: true,
        theadClasses: "thead-light",//这里设置表头样式
        columns: [
            {
                field: '',
                title: '序号',
                align: 'center',
                width: 40,
                formatter: function (value, row, index) {
                    return index + 1;
                }
            }, {
                field: 'product',
                title: '管理员产品',
                align: 'center',
                sortable: true,
                width: 80,
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
    })
}


function deleteCpById(id){
    console.log("----id",id)
    var msg = confirm("确认要删除吗？");
    if (msg){
        $ajax({
            type: 'post',
            url: '/user/isDelete',
            data : {
                id : id
            }
        }, false, '', function (res) {
            if (res.code == 200) {
                confirm("删除成功")
            } else {
                confirm("删除失败")
            }
        })
    }

}



function setUserTable1(data) {
    if ($('#userSelectTable1').html != '') {
        $('#userSelectTable1').bootstrapTable('load', data);
    }

    $('#userSelectTable1').bootstrapTable({
        data: data,
        sortStable: true,
        classes: 'table table-hover text-nowrap table table-bordered',
        idField: 'id',
        pagination: true,
        pageSize: 10,//单页记录数
        clickToSelect: true,
        locale: 'zh-CN',
        // toolbarAlign: 'left',
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
    })
}

function setUserTable2(data) {
    if ($('#userSelectTable2').html != '') {
        $('#userSelectTable2').bootstrapTable('load', data);
    }

    $('#userSelectTable2').bootstrapTable({
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
    })
}

function setUserTable3(data) {
    if ($('#userSelectTable3').html != '') {
        $('#userSelectTable3').bootstrapTable('load', data);
    }

    $('#userSelectTable3').bootstrapTable({
        data: data,
        sortStable: true,
        classes: 'table table-hover text-nowrap table table-bordered',
        idField: 'id',
        clickToSelect: true,
        locale: 'zh-CN',
        toolbarAlign: 'left',
        theadClasses: "thead-light",//这里设置表头样式
        style:'table-layout:fixed;with:10px',
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
    })
}

// function setUserTable4(data) {
//     if ($('#userSelectTable4').html != '') {
//         $('#userSelectTable4').bootstrapTable('load', data);
//     }
//
//     $('#userSelectTable4').bootstrapTable({
//         data: data,
//         sortStable: true,
//         classes: 'table table-hover text-nowrap table table-bordered',
//         idField: 'id',
//         clickToSelect: true,
//         locale: 'zh-CN',
//         toolbarAlign: 'left',
//         theadClasses: "thead-light",//这里设置表头样式
//         style:'table-layout:fixed',
//         columns: [
//             {
//                 field: '',
//                 title: '序号',
//                 align: 'center',
//                 width: 30,
//                 formatter: function (value, row, index) {
//                     return index + 1;
//                 }
//             }, {
//                 field: 'username',
//                 title: '用户名',
//                 align: 'center',
//                 sortable: true,
//                 width: 80,
//             },{
//                 field: 'name',
//                 title: '姓名',
//                 align: 'center',
//                 sortable: true,
//                 width: 80,
//             }, {
//                 field: 'department',
//                 title: '部门',
//                 align: 'center',
//                 sortable: true,
//                 width: 100,
//             }
//         ],
//         onClickRow: function (row, el) {
//             let isSelect = $(el).hasClass('selected')
//             if (isSelect) {
//                 $(el).removeClass('selected')
//             } else {
//                 $(el).addClass('selected')
//             }
//         },
//     })
// }

function setTextTable1(data) {
    if ($('#textSelectTable1').html != '') {
        $('#textSelectTable1').bootstrapTable('load', data);
    }

    $('#textSelectTable1').bootstrapTable({
        data: data,
        sortStable: true,
        classes: 'table table-hover text-nowrap table table-bordered',
        idField: 'id',
        pagination: true,
        pageSize: 8,//单页记录数
        clickToSelect: true,
        locale: 'zh-CN',
        // toolbarAlign: 'left',
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
    })
}

function setTextTable2(data) {
    if ($('#textSelectTable2').html != '') {
        $('#textSelectTable2').bootstrapTable('load', data);
    }

    $('#textSelectTable2').bootstrapTable({
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
        // rowStyle: function(row, index) {
        //     // 根据需要为行添加不同的class
        //     return {
        //         classes: 'selected'
        //     };
        // }
    })
}

function setTextTable3(data) {
    if ($('#textSelectTable3').html != '') {
        $('#textSelectTable3').bootstrapTable('load', data);
    }

    $('#textSelectTable3').bootstrapTable({
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

// function setTextTable4(data) {
//     if ($('#textSelectTable4').html != '') {
//         $('#textSelectTable4').bootstrapTable('load', data);
//     }
//
//     $('#textSelectTable4').bootstrapTable({
//         data: data,
//         sortStable: true,
//         classes: 'table table-hover text-nowrap table table-bordered',
//         idField: 'id',
//         clickToSelect: true,
//         locale: 'zh-CN',
//         toolbarAlign: 'left',
//         theadClasses: "thead-light",//这里设置表头样式
//         style:'table-layout:fixed',
//         columns: [
//             {
//                 field: '',
//                 title: '序号',
//                 align: 'center',
//                 width: 50,
//                 formatter: function (value, row, index) {
//                     return index + 1;
//                 }
//             }, {
//                 field: 'product',
//                 title: '产品名称',
//                 align: 'center',
//                 sortable: true,
//                 width: 150,
//             },
//         ],
//         onClickRow: function (row, el) {
//             let isSelect = $(el).hasClass('selected')
//             if (isSelect) {
//                 $(el).removeClass('selected')
//             } else {
//                 $(el).addClass('selected')
//             }
//         },
//     })
// }

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


