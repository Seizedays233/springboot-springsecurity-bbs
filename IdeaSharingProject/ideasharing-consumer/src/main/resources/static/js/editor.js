
window.onload = function (){
    var pbody = $("#hidden-body").val();
    if(null != pbody) {
        editor.html($("#hidden-body").val());
        editor.sync();
    }
    fileOnLoad();
};

//富文本编辑器简单模式初始化
var editor;
KindEditor.ready(function(K) {
    // createPid();
    var pid = $("#pid").val();
    editor = K.create('textarea[#sendPostBody]', {
        uploadJson:"/uploadJson/post." + pid,
        allowImageUpload : true,
        // height:"800px",
        resizeType : 0,//禁止拉伸，1可以上下拉伸，2上下左右拉伸
        filterMode: true,//true时过滤HTML代码，false时允许输入任何代码。
        allowPreviewEmoticons : false,
        cssData: 'body{font-family: 微软雅黑;font-size: 14px;padding:30px;}',
        afterFocus : function(){//获得焦点 删除默认文字信息
            if(editor.html() == '<span style="color:#9B9B9B;">帖子内容</span>'){
                editor.html('');
            }
        },
        afterBlur: function(e){
            if(editor.html() == ''){
                editor.html('<span style="color:#9B9B9B;">帖子内容</span>');
            }
            editor.sync();
        },//失去焦点，同步信息数据 superscript：表示上标；如同：X2
        afterUpload : function() {
            fileOnLoad();
        },
        items : [
            'fontname','fontsize','bold', 'italic','strikethrough', 'insertunorderedlist', 'emoticons','undo','redo','cut','copy','paste',
            'forecolor','hilitecolor','',
            'subscript','superscript','image','multimage','insertfile','code','table',
        ]
    });
});


$("#sendPostTitle").bind("input propertychange",function(event){
    $(".tips").text("您还可以输入" + (80- $("#sendPostTitle").val().length) +"个字");
});

$("#savePostButton").click(function () {
    savePost();
});

$("#sendPostButton").click(function () {
    sendPost();
});


function savePost() {
    var ptitle = $("#sendPostTitle").val();
    var pbody = $("#sendPostBody").val();
    var pid = $("#pid").val();
    if ((ptitle.length > 0 && ptitle.length <= 80) && (pbody.length > 0)) {
        $.ajax({
            async: false,
            type: 'post',
            url: "/savePost.do",
            dataType: 'json',
            data: {
                "ptitle": ptitle,
                "pbody": pbody,
                "pid":pid
            },
            success: function (message) {
                console.log(message);
                alert(message.msg);
                $("#pid").val(message.pid);
                fileOnLoad();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(errorThrown);
            }
        })
    }
}

function sendPost() {
    savePost();
    var ptitle = $("#sendPostTitle").val();
    var pbody = $("#sendPostBody").val();
    var pid = $("#pid").val();
    if ((ptitle.length > 0 && ptitle.length <= 80) && (pbody.length > 0)) {
        $.ajax({
            async: false,
            type: 'get',
            url: "/sendPost.do",
            dataType: 'json',
            data: {
                "pid":$("#pid").val()
            },
            success: function (sendPostNum) {
                if (sendPostNum > 0) {
                    alert("发送成功");
                    window.location.href = "/bbs";
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(errorThrown);
            }
        })
    }
}

$(".returnButton").click(function () {
    window.location.href = "/bbs";
});

function fileOnLoad() {
    $.ajax({
        async: false,
        type: 'get',
        url: "/postFileOnLoad",
        dataType: 'json',
        data: {
            "pid":$("#pid").val()
        },
        success: function (fileData) {
            $(".fileList").empty();
            if (fileData.length > 0) {
                for (var i = 0; i<fileData.length; i++){
                    var fileObj = fileData[i];
                    var $li = $("<li></li>");
                    var $a = $("<a href='/downloadFile/"+fileObj.fid+"' class='download' >下载</a><a href='#' class='removeFile' id="+fileObj.fid+">删除</a>");
                    $li.text(fileObj.fileName);
                    $li.append($a);
                    $(".fileList").append($li);
                }
            }
            $(".removeFile").click(function () {
                var $fid = $(this).attr("id");
                removeFile($fid)
            });

        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(errorThrown);
        }
    });
}



