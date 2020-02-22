Date.prototype.format = function (format)
{
    var o = {
        "M+": this.getMonth() + 1, //month
        "d+": this.getDate(),    //day
        "h+": this.getHours(),   //hour
        "m+": this.getMinutes(), //minute
        "s+": this.getSeconds(), //second
        "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter
        "S": this.getMilliseconds() //millisecond
    };
    if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
        (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o) if (new RegExp("(" + k + ")").test(format))
        format = format.replace(RegExp.$1,
            RegExp.$1.length == 1 ? o[k] :
                ("00" + o[k]).substr(("" + o[k]).length));
    return format;
};

$(".delete-post-btn").click(function () {
    if (confirm("确认删除？")){
        var pid = $(this).prev(".postId").val();
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


function removeFile($fid) {
    if(confirm("确认删除这个文件?")) {
        $.ajax({
            async: false,
            type: 'get',
            url: "/removeFile",
            datatype:"json",
            data: {
                "fid": $fid
            },
            success: function (resultMessage) {
                alert(resultMessage);
                fileOnLoad();
            },

            error: function (jqXHR, textStatus, errorThrown) {
                alert(errorThrown);
            }
        });
    }
}

$(".btn-search").click(function(){

    $("#search-form").submit();

});

$('#search-input').bind('keyup', function(event) {

    if (event.keyCode == 13) {

        //回车执行查询
        $('.btn-search').click();

    }

});



