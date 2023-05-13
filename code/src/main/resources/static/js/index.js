$(function () {
    $("#submit-btn").click(function () {
        if (checkForm('#login-form')) {
            let params = formToJson('#login-form')
            $ajax({
                type: 'post',
                url: 'user/login',
                data: {
                    username: params.username,
                    password: params.password
                }
            }, false, '', function (res) {
                if (res.code > 0) {
                    swal("", res.msg, "success");
                    setTimeout(function () {
                        window.location.href = "html/main.html";
                    }, 1500);
                } else {
                    swal("", res.msg, "error");
                }
            })
        }
    })
});