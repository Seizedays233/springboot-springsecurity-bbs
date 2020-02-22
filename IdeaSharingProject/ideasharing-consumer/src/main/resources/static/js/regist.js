
function usernameCheck(){

    var $username = $("#username").val();

    if( $username.length == 0 ){

        $("#nameTip").text("*用户名不能为空");
        return 1;

    }else if(  $username.length > 0 && $username.length < 3 || $username.length > 15){

        $("#nameTip").text("*用户名长度不符合规范（3-15个字符）");
        return 1;

    }else{

        //检查用户名是否与已注册的用户重复
        var duplicationNum = duplicateCheck();

        if (duplicationNum == 1){

            $("#nameTip").text("该名称已存在");
            return 1;

        }else {

            $("#nameTip").text('');
            return 0;
        }

    }
};

function emailCheck(){

    var $email = $("#email").val();
    //email正则表达式
    var pattern = /^[a-zA-Z0-9#_\^\$\.\*\+\-\?\=\]+@[a-zA-Z0-9]+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;

    if(!pattern.test($email)){

        $("#emailTip").text("*邮箱格式不正确");
        return 1;

    }else{

        $("#emailTip").text("");
        return 0;

    }
};


function phoneCheck(){

    var $phone = $("#phone").val();
    //电话号码正则表达式
    var pattern = /^1[0-9]{10}$/;

    if(!pattern.test($phone)){

        $("#phoneTip").text("*电话号码格式不正确");
        return 1;

    }else{

        $("#phoneTip").text("");
        return 0;

    }
};

function pwdCheck(){

    var $pwd = $("#pwd").val();
    var $confirmPwd = $("#confirmPwd").val();

    if($pwd.length == 0){

        $("#pwdTip").text("*密码不能为空");
        return 1;

    }else if( $pwd.length < 6 && $pwd.length > 0 || $pwd.length > 16){

        $("#pwdTip").text("*密码长度不符合规范(6-16个字符)");
        return 1;

    }else if ( $pwd != $confirmPwd && $confirmPwd != ''){

        $("#pwdTip").text("*两次密码输入不一致");
        return 1;

    }else{

        $("#pwdTip").text("");
        return 0;

    }
};

function pwdConfirmCheck(){

    var $confirmPwd = $("#confirmPwd").val();

    if($confirmPwd == ''){
        $("#pwdTip").text("*两次密码输入不一致");
        return 1;
    }else{
        return  pwdCheck();
    }
}

function submitCheck(){
    //检查输入格式
    var flag = usernameCheck() + emailCheck() + phoneCheck() + pwdConfirmCheck();
    //flag为0时表示检查通过 执行submit
    if(!flag){
        $('#registerForm').submit();

    }

    return false;
}


function duplicateCheck() {
    var dupNum = 0;
    $.ajax(
        {
            type:"get",
            url: "/duplicateCheck",
            data:{"username":$("#username").val()},
            async: false,
            success: function (num) {
                dupNum = num;
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(errorThrown);
            },
            datatype: 'text'
        }
    );

    return dupNum;
}

$('#regist-div').bind('keyup', function(event) {

    var theEvent = window.event;
    var code = theEvent.keyCode || theEvent.which || theEvent.charCode;

    if (code === 13) {

        //回车执行注册
        submitCheck();

    }

});
