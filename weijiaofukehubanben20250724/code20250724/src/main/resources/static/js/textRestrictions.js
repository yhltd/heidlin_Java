let p_id = ""
let founder = ""
let currentPage = 1;
const pageSize = 50;
function getList() {
    //打开页面隐藏查看共享按钮
    $ajax({
        type: 'post',
        url: '/user/getPower',
    }, false, '', function (res) {
        var this_power = res.data
        if (this_power != '管理员') {
            $('#share-btn').hide();
            $('#share-del-btn').hide();
            $('#share-set-btn').hide();
            $('#share-setting').hide();
        }
    });
    loadData(currentPage);

    // 初始化分页按钮
    initPagination();
}
function loadData(pageNum) {
    currentPage = pageNum;
    $ajax({
        type: 'post',
        url: '/textRestrictions/admin/list',
        data: {pageNum, pageSize, orderBy: 'id'} // 明确参数
    }, false, '', function (res) {
        if (res.code == 200) {

            setTable(res.data.list,{
                pageNum: res.data.pageNum,
                pageSize: res.data.pageSize
            });
            updatePagination(res.data);
            $("#userTable").colResizable({
                liveDrag: true,
                gripInnerHtml: "<div class='grip'></div>",
                draggingClass: "dragging",
                resizeMode: 'fit'
            });
        }
    });
}
// 分页控制
function initPagination() {
    $('#prev-page').click(() => {
        if(currentPage > 1) {
            loadData(currentPage - 1);
        }
    });

    $('#next-page').click(() => {
        loadData(currentPage +1);
    });
}

function updatePagination(pageInfo) {
    const { pageNum, pages, total } = pageInfo;
    currentPage = pageInfo.pageNum; // 更新当前页码
    $('#page-info').html(`
        <span class="page-info-text">
            第<span class="highlight">${pageNum}</span>页/共<span class="highlight">${pages}</span>页 | 
            共<span class="highlight">${total}</span>条
        </span>
    `);
    const $pageButtons = $('#page-buttons').empty();
    const maxVisiblePages = 5; // 最多显示10个页码

    // 计算起始页码
    let startPage = Math.max(1, pageNum - Math.floor(maxVisiblePages / 2));
    let endPage = Math.min(pages, startPage + maxVisiblePages - 1);

    // 调整页码范围（确保显示maxVisiblePages个页码）
    if (endPage - startPage + 1 < maxVisiblePages) {
        startPage = Math.max(1, endPage - maxVisiblePages + 1);
    }

    // 添加"..."表示前面还有页码
    if (startPage > 1) {
        $pageButtons.append(`<button class="page-btn first-page" data-page="1">1</button>`);
        if (startPage > 2) {
            $pageButtons.append('<span class="page-dots">⋯</span>');
        }
    }

    // 添加页码按钮
    for (let i = startPage; i <= endPage; i++) {
        const activeClass = i === pageNum ? 'active' : '';
        $pageButtons.append(`<button class="page-btn ${activeClass}" data-page="${i}">${i}</button>`);
    }

    // 添加"..."表示后面还有页码
    if (endPage < pages) {
        if (endPage < pages - 1) {
            $pageButtons.append('<span class="page-dots">⋯</span>');
        }
        $pageButtons.append(`<button class="page-btn last-page" data-page="${pages}">${pages}</button>`);
    }

    // 绑定页码按钮事件
    $('.page-btn').click(function() {
        loadData(parseInt($(this).data('page')));
    });

    // 控制按钮状态
    $('#prev-page').toggle(pageNum > 1).toggleClass('disabled', pageNum <= 1);
    $('#next-page').toggle(pageNum < pages).toggleClass('disabled', pageNum >= pages);
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

function setTable(data,pageInfo) {
    console.log('setTable pageInfo:', pageInfo);

    if ($('#userTable').html != '') {
        $('#userTable').bootstrapTable('load', data);
    }
    $('#userTable').bootstrapTable({
        data: data,
        sortStable: true,
        classes: 'table table-hover text-nowrap table table-bordered',
        idField: 'id',
        uniqueId: 'id',
        //pagination: true,
        // pageSize: 15,//单页记录数
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
                    const globalIndex = (currentPage - 1) * pageSize + index + 1;
                    console.log(`row ${index}: page=${currentPage}, size=${pageSize}, globalIndex=${globalIndex}`);
                    return globalIndex;
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
            },
            {
                field: 'duplicate_cfpd',
                title: '重复设置',
                align: 'center',
                width: 150,
                formatter: function(value, row, index) {
                    return `
            <div class="duplicate-setting-container">
                <select class="form-control duplicate-col" data-row="${row.id}">
                    <option value="">请选择列名</option>
                    <option value="product">产品名称</option>
                    <option value="col2">操作</option>
                    <option value="columntext1">列标/数量规定1</option>
                    <option value="columntext2">列标/数量规定2</option>
                    <option value="columntext3">列标/数量规定3</option>
                    <option value="columntext4">列标/数量规定4</option>
                    <option value="columntext5">列标/数量规定5</option>
                    <option value="columntext6">列标/数量规定6</option>
                    <option value="columntext7">列标/数量规定7</option>
                    <option value="columntext8">列标/数量规定8</option>
                    <option value="columntext9">列标/数量规定9</option>
                    <option value="columntext10">列标/数量规定10</option>
                </select>
                <input type="number" min="1" class="form-control duplicate-max" 
                       data-row="${row.id}" placeholder="最大重复" style="margin-top:5px;">
                <button class="btn btn-sm btn-primary apply-duplicate-btn" 
                        data-row="${row.id}" style="margin-top:5px;">
                    应用设置
                </button>
            </div>
        `;
                }
            }, {
                field: 'columntext1',
                title: '列标/数量规定1',
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
                field: 'columntext2',
                title: '列标/数量规定2',
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
                field: 'columntext3',
                title: '列标/数量规定3',
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
                field: 'columntext4',
                title: '列标/数量规定4',
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
                field: 'columntext5',
                title: '列标/数量规定5',
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
                field: 'columntext6',
                title: '列标/数量规定6',
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
                field: 'columntext7',
                title: '列标/数量规定7',
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
                field: 'columntext8',
                title: '列标/数量规定8',
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
                field: 'columntext9',
                title: '列标/数量规定9',
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
                field: 'columntext10',
                title: '列标/数量规定10',
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


// 1. 定义全局函数
window.processDuplicateSettings = function(rowId, column, maxAllowed) {
    console.log("执行 processDuplicateSettings", { rowId, column, maxAllowed });

    // 统一转换为数字比较（根据你的数据，id是数字类型）
    rowId = parseInt(rowId);

    $.ajax({
        type: 'post',
        url: '/textRestrictions/admin/listAll',
        data: JSON.stringify({ orderBy: 'id' }),
        dataType: 'json',
        contentType: 'application/json;charset=utf-8',
        success: function(res) {
            console.log("完整API响应:", res);

            if (res.code != 200) {
                Swal.fire("错误", "获取数据失败: " + (res.msg || "未知错误"), "error");
                return;
            }

            // 修改这里：直接从res.data获取数组，而不是res.data.list
            const allData = Array.isArray(res.data) ? res.data : [];
            console.log("获取到的完整数据:", allData);

            // 调试：打印前10个ID
            console.log("前10个ID:", allData.slice(0, 10).map(item => item.id));

            const currentRow = allData.find(row => row.id === rowId);

            if (!currentRow) {
                console.error("未找到行ID:", rowId, "在数据中");
                console.error("前50个ID:", allData.slice(0, 50).map(item => item.id));
                Swal.fire({
                    title: "数据错误",
                    html: `未找到ID为 <strong>${rowId}</strong> 的行<br>
                           请检查控制台查看完整数据`,
                    icon: "error"
                });
                return;
            }

            console.log("找到当前行数据:", currentRow);

            // 获取列的值
            let columnValue;
            if (column === 'product') {
                columnValue = currentRow.product;
            } else if (column.startsWith('columntext')) {
                const num = column.replace('columntext', '');
                columnValue = currentRow['columntext' + num];
            }

            console.log("检查的列值:", columnValue);

            if (!columnValue) {
                Swal.fire("警告", "请先填写要检查的列内容", "warning");
                return;
            }

            // 统计重复次数
            let count = 0;
            const duplicateRows = [];

            allData.forEach(row => {
                let compareValue;
                if (column === 'product') {
                    compareValue = row.product;
                } else if (column.startsWith('columntext')) {
                    const num = column.replace('columntext', '');
                    compareValue = row['columntext' + num];
                }

                if (compareValue && compareValue.toString().trim() === columnValue.toString().trim()) {
                    count++;
                    duplicateRows.push(row.id);
                }
            });

            console.log("重复统计结果:", {
                value: columnValue,
                count: count,
                duplicateIds: duplicateRows
            });

            // 检查是否超过最大允许次数
            if (count > maxAllowed) {
                Swal.fire({
                    title: "<span class='warning-title'>重复检查结果</span>",
                    html: `"<span class='warning-highlight'>${columnValue}</span>" 在 <span class='column-highlight'>${column}</span> 列中出现了 <span class='warning-highlight'>${count}</span> 次，超过了允许的 <span class='limit-highlight'>${maxAllowed}</span> 次`,                    icon: "warning",
                    showCancelButton: true,
                    confirmButtonText: "显示重复项",
                    cancelButtonText: "取消",
                    showDenyButton: true,
                    denyButtonText: "显示重复项并删除",
                    reverseButtons: true,
                    customClass: {
                        container: 'custom-swal-container',
                        popup: 'custom-warning-popup',
                        title: 'custom-warning-title',
                        htmlContainer: 'custom-warning-html',
                        confirmButton: 'custom-warning-confirm',
                        denyButton: 'custom-warning-deny',
                        cancelButton: 'custom-warning-cancel'
                    },
                    buttonsStyling: false,
                    background: '#fffefb'
                }).then((result) => {
                    if (result.isConfirmed) {
                        highlightDuplicates(duplicateRows, column, allData);
                        setTimeout(() => removeHighlights(), 5);
                    } else if (result.isDenied) {
                        highlightDuplicates(duplicateRows, column, allData);
                        setTimeout(() => deleteDuplicates(duplicateRows), 3000);
                    }
                });
            } else {
                Swal.fire({
                    title: "检查通过",
                    html: `"<span class='highlight-text'>${columnValue}</span>" 在 <span class='highlight-label'>${column}</span> 列中出现了 <span class='highlight-count'>${count}</span> 次，符合要求`,
                    icon: "success",
                    background: '#fff',
                    showConfirmButton: true,
                    confirmButtonText: '确认',
                    confirmButtonColor: '#1677ff',
                    customClass: {
                        popup: 'custom-swal-popup',
                        title: 'custom-swal-title',
                        htmlContainer: 'custom-swal-html'
                    }
                });
            }
        },
        error: function(xhr) {
            console.error("请求失败:", xhr.responseText);
            Swal.fire("错误", "请求失败: " + xhr.statusText, "error");
        }
    });
};
// 高亮显示重复行
function highlightDuplicates(duplicateRows, column, allData) {
    // 先移除之前的高亮
    $('.duplicate-highlight').removeClass('duplicate-highlight');
    $('.duplicate-highlight-cell').removeClass('duplicate-highlight-cell');
// 获取正确的分页信息
    // 硬编码正确的pageSize（根据实际UI显示设置）
    const pageSize = 50; // 已知实际每页显示50条
    const totalPages = Math.ceil(allData.length / pageSize);

    console.log('分页信息:', {
        pageSize: pageSize,
        totalData: allData.length,
        calculatedPages: totalPages
    });

    // 计算页码分布
    const pageDistribution = {};
    duplicateRows.forEach(id => {
        const row = allData.find(item => item.id === id);
        if (row) {
            const index = allData.indexOf(row);
            const pageNumber = Math.floor(index / pageSize) + 1;
            pageDistribution[pageNumber] = (pageDistribution[pageNumber] || 0) + 1;
        }
    });
    // 高亮当前页的重复项
    const $rows = $('#userTable').find('tbody tr');
    $rows.each(function() {
        const $row = $(this);
        const rowId = parseInt($row.data('uniqueid') || parseInt($row.attr('data-uniqueid')));

        if (duplicateRows.includes(rowId)) {
            $row.addClass('duplicate-highlight');

            // 高亮特定单元格
            if (column === 'product') {
                $row.find('[data-field="product"]').addClass('duplicate-highlight-cell');
            } else if (column.startsWith('columntext')) {
                const num = column.replace('columntext', '');
                $row.find(`[data-field="columntext${num}"]`).addClass('duplicate-highlight-cell');
            }
        }
    });

    let pageInfo = Object.entries(pageDistribution)
        .sort((a, b) => parseInt(a[0]) - parseInt(b[0]))
        .map(([page, count]) => `
        <div class="page-distribution-item">
            <span class="page-distribution-page">第${page}页：</span>
            <span class="page-distribution-count">${count}个</span>
        </div>`
        ).join('');  // 移除 <br>，改用 CSS 控制间距

        Swal.fire({
            html: `<div class="info-content">
                  <div style="margin-bottom: 1rem; color: #4a5568;">
                      共发现 <strong style="color: #1890ff;">${duplicateRows.length}</strong> 个重复项
                  </div>
                 <!-- HTML 使用 -->
<!--<div class="distribution-title">分布情况：</div>-->
                  ${pageInfo}
               </div>`,
            icon: "info",
            width: '420px',
            background: '#ffffff',
            customClass: {
                container: 'swal-info-container',
                popup: 'swal-info-popup',
                title: 'swal-info-title',
                htmlContainer: 'swal-info-html',
                confirmButton: 'swal-info-confirm'
            },
            showConfirmButton: true,
            confirmButtonText: '确定',
            confirmButtonColor: '#1890ff',
            buttonsStyling: false
        });

}

// 移除高亮
function removeHighlights() {
    $('.duplicate-highlight').removeClass('duplicate-highlight');
    $('.duplicate-highlight-cell').removeClass('duplicate-highlight-cell');
}

// 删除重复行
function deleteDuplicates(duplicateRows) {
    removeHighlights();

    if (duplicateRows.length > 1) {
        duplicateRows.sort((a, b) => a - b);
        const idsToDelete = duplicateRows.slice(1); // 保留第一个，删除后面的

        Swal.fire({
            title: "确认删除",
            text: `确定要删除 ${idsToDelete.length} 个重复项吗？`,
            icon: "warning",
            showCancelButton: true,
            confirmButtonText: "确定删除",
            cancelButtonText: "取消"
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    type: 'post',
                    url: '/textRestrictions/delete',
                    data: JSON.stringify({
                        idList: idsToDelete
                    }),
                    dataType: 'json',
                    contentType: 'application/json;charset=utf-8',
                    success: function(res) {
                        if (res.code == 200) {
                            Swal.fire("成功", `已删除 ${idsToDelete.length} 个重复项`, "success");
                            getList();
                        } else {
                            Swal.fire("错误", "删除失败: " + res.msg, "error");
                        }
                    },
                    error: function(xhr) {
                        Swal.fire("错误", "删除请求失败: " + xhr.statusText, "error");
                    }
                });
            }
        });
    }
}

// 2. 绑定按钮事件
$(document).on('click', '#userTable .apply-duplicate-btn', function() {
    const rowId = $(this).data('row');
    const column = $(this).closest('.duplicate-setting-container').find('.duplicate-col').val();
    const maxAllowed = $(this).closest('.duplicate-setting-container').find('.duplicate-max').val();

    if (!column || !maxAllowed) {
        swal("", "请选择列并设置最大重复次数", "warning");
        return;
    }
    window.processDuplicateSettings(rowId, column, maxAllowed);
});