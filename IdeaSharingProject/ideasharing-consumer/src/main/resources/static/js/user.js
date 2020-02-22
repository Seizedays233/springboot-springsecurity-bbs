
$(".user-data").addClass("active");

$(".change-photo").click(function(){
    $("#hidden").click();
});

$(".btn-search").click(function(){
    $("#search-form").submit();

});

function nickNameCheck() {
    var $nickName = $("#nickName").val();

    if( $nickName.length == 0 ){

        $("#nickNameTip").text("*用户名不能为空");
        return 1;

    }else if( $nickName.length > 0 && $nickName.length < 3 || $nickName.length > 15){

        $("#nickNameTip").text("*昵称长度控制在3-15个字符");
        return 1;

    }else{

        //检查用户名是否与已注册的用户重复
        var duplicationNum = dupNickNameCheck();

        if (duplicationNum > 0){

            $("#nickNameTip").text("*该名称已被占用");
            return 1;

        }else {

            $("#nickNameTip").text('');
            return 0;
        }

    }

}

function dupNickNameCheck() {
    var dupNum = 0;
    $.ajax(
        {
            type:"get",
            url: "/duplicateNick",
            data:{"nickName":$("#nickName").val()},
            async: false,
            success: function (num) {
                dupNum = num;
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(errorThrown);
            },
        }
    );

    return dupNum;
}



function saveUserData() {
    //检查email和phone格式
    var flag = emailCheck() + phoneCheck() + nickNameCheck();
    if(flag === 0){
        $.ajax(
            {
                type:"post",
                url: "/updateUser",
                data:{
                    "nickname":$("#nickName").val(),
                    "email":$("#email").val(),
                    "phone":$("#phone").val(),
                    "signature":$("#signature").val(),
                    "sex":$("#sex option:selected").val()
                },
                async: true,
                datatype:"json",
                success: function (reMsg) {

                    if(!("tips" in reMsg)){
                        alert(reMsg.saveStatus);
                        window.location="/user";

                    }else {
                        var msg = reMsg.tips;
                        alert(reMsg[msg]);
                        $("#"+ msg).text(reMsg[msg]);
                    }

                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert(errorThrown);
                },
            }
        );
    }

}


//检查上传的图片是否满足要求
function checkImage() {

    var fileName=$("#hidden").val();
    fileName=fileName.replace("C:\\fakepath\\","");
    var flag=true;

    if(fileName==""){
        flag=false;
        alert("请选择图片");
    }else{
        var size = $("#hidden")[0].files[0].size;
        if(size/1000>10000){
            flag=false;
            alert("图片大小不能超过10M");
        }
    }

    if(flag){
        imgUpload();
    }

    return flag;

}

//上传图片并保存到指定路径
function imgUpload() {
    var formdata=new FormData();
    formdata.append("imgFile",$('#hidden').get(0).files[0]);
    $.ajax({
        type: 'POST',
        url: "/profileUpload",
        async: false,
        datatype: 'json',
        data:  formdata,
        processData:false,
        contentType:false,
        cache:false,
        success: function (data) {
            alert(data.result_msg);
            if (data.relativePath != null){
                location.reload();
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
        alert(errorThrown);
        }
    })
}
