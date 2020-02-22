


function selectFun(){
    $.ajax(
        {
            type:"get",
            url: "/MyCalendar/selectMemo",
            data:{"date":$("#selectDay").val()},
            async: true,
            success: callBack,
            error: errorFun,
            datatype: 'json'
        }
    );
}

function callBack(memoArray) {
    if(memoArray.length == 0){
        $("#memo").text("当日没有日程安排")

    }else {
        $("#memo").empty();
        for(var i=0; i<memoArray.length; i++){
            var memoObj = memoArray[i];
            var $li = $("<li id=" + memoObj.mid + "></li>");
            $li.text(memoObj.content);
            $("#memo").append($li);
        }
    }

    $("#memo li").click(function(){
        $("#memo li").removeClass("select");
        $(this).addClass("select");
        $("#selectId").val($(this).attr('id'));
        if($(this).hasClass("finishedMemo")){
            $("#markMemoBtn").text("取消标记")
        }else{
            $("#markMemoBtn").text("标记完成")
        }
    });
}

function addMemoFun() {
    $.ajax(
        {
            type:"get",
            url: "/MyCalendar/addMemo",
            data:{"date":$("#selectDay").val(),
                  "content":$("#addMemoText").val()
            },
            async: true,
            success: callBack2,
            error: errorFun,
            datatype: 'text'
        }
    );

}

function callBack2(addNum){
    if(addNum > 0) {
        alert("成功增加" + addNum + "条日程");
        selectFun();
    }
}

function deleteMemoFun() {
    $.ajax(
        {
            type: "get",
            url: "/MyCalendar/deleteMemo",
            data: {
                "date": $("#selectDay").val(),
                "id": $("#selectId").val()
            },
            async: true,
            success: callBack3,
            error: errorFun,
            datatype: 'text'
        }
    );

}

function callBack3(deleteNum) {
    if(deleteNum > 0){
        alert("删除成功");
        selectFun();
    }

}

function updateMemoFun() {
    $.ajax(
        {
            type: "get",
            url: "/MyCalendar/updateMemo",
            data: {
                "date": $("#selectDay").val(),
                "id": $("#selectId").val(),
                "content":$("#addMemoText").val()
            },
            async: true,
            success: callBack4,
            error: errorFun,
            datatype: 'text'
        }
    );
}

function callBack4(updateNum) {
    if(updateNum > 0){
        alert("修改完成");
        selectFun();
    }
}

function errorFun(jqXHR, textStatus, errorThrown) {
    alert(errorThrown);
}

$("#imgUploadBtn").click(function () {
    $("#imgBtn").click();
});

//检查上传的图片是否满足要求
function checkImage() {

    var fileName=$("#imgBtn").val();
    fileName=fileName.replace("C:\\fakepath\\","");
    var flag=true;

    if(fileName==""){
        flag=false;
        alert("请选择图片");
    }else{
        var size = $("#imgBtn")[0].files[0].size;
        if(size/1000>10000){
            flag=false;
            alert("图片大小不能超过10M");
        }
    }

    if(flag){
        imgUpload();
    }else{//清除input type=file的显示内容
        $("#imgBtn").val("");
    }
    return flag;

}

//上传图片并保存到指定路径
function imgUpload() {
    var formdata=new FormData();
    formdata.append("imgFile",$('#imgBtn').get(0).files[0]);
    formdata.append("date",$("#selectDay").val());
    formdata.append("content",$("#diaryText").val());
    $.ajax({
        type: 'POST',
        url: "/MyCalendar/imageUpload",
        async: false,
        datatype: 'json',
        data:  formdata,
        processData:false,
        contentType:false,
        cache:false,
        success: function (data) {
            alert(data.result_msg);
            imgOnLoad();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(errorThrown);
        }
    })
}

//显示日记内容
function diaryOnLoad() {
    $.ajax({
        async: false,
        type: 'get',
        url: "/MyCalendar/diaryOnLoad",
        data: {
            "date": $("#selectDay").val(),
        },
        datatype:'json',
        success: function (diaryData) {
            $("#diaryText").empty();
            $("#diaryText").append(diaryData.content);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(errorThrown);
        }
    })
}

//显示选中日期对应的图片
function imgOnLoad() {
    $.ajax({
        async: false,
        type: 'get',
        url: "/MyCalendar/imgOnLoad",
        datatype: 'json',
        data: {"date": $("#selectDay").val()},

        success: function (imgData) {
            $(".cover").attr("src", imgData.imageName);

        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(errorThrown);
        }
    })
}

//保存日记到数据库
function saveDiaryFun(){
    $.ajax({
        async: false,
        type: 'post',
        url: "/MyCalendar/upLoadDiary",
        datatype: 'json',
        data: {
            "date": $("#selectDay").val(),
            "content":$("#mul_input").val()
        },

        success: function (addNum) {
            if(addNum > 0){
                alert("保存成功");
                diaryOnLoad();
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(errorThrown);
        }
    })
}

//从数据库中删除日记
function deleteDiaryFun() {
    $.ajax({
        async: false,
        type: 'get',
        url: "/MyCalendar/deleteDiary",
        datatype: 'json',
        data: {
            "date": $("#selectDay").val()
        },

        success: function (deleteNum) {
            if(deleteNum > 0){
                alert("删除成功");
                diaryOnLoad();
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(errorThrown);
        }
    })
}



