//仅在登录状态才会调用这个js文件

$.ajax({
    async: false,
    type: 'get',
    url: "/auth",
    datatype: 'json',

    success: function (msg) {
        $(".nickName").text(msg.nickName);
        $(".img-profile").attr("src", msg.profilePath);
    },
    error: function (jqXHR, textStatus, errorThrown) {
        alert(errorThrown);
    }
});

