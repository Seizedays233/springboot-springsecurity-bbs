
var $rid = $("#ridPosition").val();
if($rid != -1){
    window.location="#"+$rid;
}

window.onload = function (){
    fileOnLoad();
};
//富文本编辑器简单模式初始化
var editor;
KindEditor.ready(function(K) {
    var rid = $("#rid").val();
    editor = K.create('textarea[#reply-body]', {
        uploadJson:"/uploadJson",
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
        items : [
            'fontname','fontsize','bold', 'italic','strikethrough', 'insertunorderedlist', 'emoticons','undo','redo','cut','copy','paste',
            'forecolor','hilitecolor','',
            'subscript','superscript','code','table',
        ]
    });
});

$(".reply-btn").click(function () {
    var repBody = $("#reply-body").val();
    $.ajax({
        async: true,
        type: 'post',
        url: "/reply.do",
        datatype: 'json',
        data: {
            "repBody": repBody,
            "pid":$("#postId").val()
        },
        success: function (sendRepNum) {
            if (sendRepNum > 0) {
                alert("回复成功");
                window.location.reload();
            }else if(sendRepNum == -1){
                alert("您已被禁言 无法回复")
            }else {
                alert("需要登录后才能发言");
                window.location.href = "/login";
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(errorThrown);
        }
    })
});

$(".delete-rep-btn").click(function () {
    if(confirm("确认删除这条回复？")) {
        var rid = $(this).prev(".repId").val();
        var repName = $(this).next(".repuserName").val();
        $.ajax({
            async: true,
            type: 'get',
            url: "/deleteReply",
            datatype: 'json',
            data: {
                "rid": rid,
                "repName": repName
            },
            success: function (delRepNum) {
                if (delRepNum > 0) {
                    alert("删除成功");
                    window.location.reload();
                } else {
                    alert("删除失败");
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(errorThrown);
            }
        })
    }
});

$("#delete-post-btn").click(function(){

    if(confirm("确认删除？")){
        var pid = $("#postId").val();
        $.ajax({
            async: true,
            type: 'get',
            url: "/deletePost",
            datatype: 'json',
            data: {
                "pid": pid,
            },
            success: function (delRepNum) {
                if (delRepNum > 0) {
                    alert("删除成功");
                    window.location.href = "/bbs";
                } else {
                    alert("删除失败");
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(errorThrown);
            }
        })
    }
});

function fileOnLoad() {
    $.ajax({
        async: false,
        type: 'get',
        url: "/postFileOnLoad",
        datatype: 'json',
        data: {
            "pid":$("#postId").val()
        },
        success: function (fileData) {
            $(".fileList").empty();
            if (fileData.length > 0) {
                for (var i = 0; i<fileData.length; i++){
                    var fileObj = fileData[i];
                    var $li = $("<li></li>");
                    var $a = $("<a href='/downloadFile/"+fileObj.fid+"' class='download' > 下载 </a><a href='#' th:if=\"${post.ideaUser.username == actUsername}\" class='removeFile' id="+fileObj.fid+">删除</a>");
                    $li.text(fileObj.fileName);
                    $li.append($a);
                    $(".fileList").append($li);
                }
            }

            $(".removeFile").click(function () {
                var $fid = $(this).attr("id");
                alert("fid" + $fid);
                removeFile($fid)
            });

        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(errorThrown);
        }
    });
}

