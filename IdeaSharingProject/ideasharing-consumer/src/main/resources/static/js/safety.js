
$(".safety").addClass("active");

$(".btn-search").click(function(){
    $("#search-form").submit();

});

function changePwd() {
    //先验证密码格式及一致性
    var flag = pwdConfirmCheck();
    if(flag==0){
        $("#pwd-form").submit();
    }else {
        alert("flag不过")
    }
};