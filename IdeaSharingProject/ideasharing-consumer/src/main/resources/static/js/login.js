function usernameCheck(){

    var $username = $("#inputUsername").val();

    if( $username.length == 0 ){

        $("#nameTip").text("*请填写用户名");
        return 1;

    }else{

        $("#nameTip").text('');
        return 0;

    }
};

function loginCheck() {
    var flag = usernameCheck();
    if(!flag){
        $("#loginForm").submit();

    }else{
        return false;
    }

}

$('#login-div').bind('keyup', function(event) {

    var theEvent = window.event;
    var code = theEvent.keyCode || theEvent.which || theEvent.charCode;

    if (code === 13) {
        //回车执行登录
        loginCheck();
    }

});
