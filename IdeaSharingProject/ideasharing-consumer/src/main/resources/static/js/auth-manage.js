$(".authority").addClass("active");

$(".role-span").click(function () {
    $(this).hide();
    $(this).siblings(".role-input").show();
});

$(".auth-span").click(function () {
    $(this).hide();
    $(this).siblings(".auth-input").show();
});

$(".saveBtn").click(function(){
    var $uid =  $(this).parent().siblings(".uid").text();
    var $authority = $(this).parent().siblings(".auth").children('.auth-input').val();
    var $role = $(this).parent().siblings(".role").children('.role-input').val();
    var $ustate = $(this).parent().siblings(".user-ustate").children(".ustate").val();
    var saveNum = saveAuth($uid, $authority, $role, $ustate);
    alert("成功更新了 " + saveNum + "个用户权限");
    if (saveNum > 0) {
        $(this).parent().siblings(".role").children('.role-span').text($role);
        $(this).parent().siblings(".auth").children('.auth-span').text($authority);
    }
    $(this).parent().siblings(".au").children('.user-input').hide();
    $(this).parent().siblings(".au").children('.user-span').show();
});

function saveAuth($uid, $authority, $role,$ustate) {
    var saveNum;
    $.ajax({
        async: false,
        type: 'get',
        url: "/auth/update",
        datatype: 'json',
        data: {
            "uid": $uid,
            "authority":$authority,
            "role":$role,
            "ustate":$ustate
        },
        success: function (updateNum) {
            saveNum = updateNum;
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(errorThrown);
        }
    });

    return saveNum;
}
