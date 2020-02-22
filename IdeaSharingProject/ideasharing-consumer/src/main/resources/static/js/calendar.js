//富文本编辑器简单模式初始化
var editor;
KindEditor.ready(function(K) {
    editor = K.create('textarea[name="content"]', {
        uploadJson:"/uploadJson",
        fileManagerJson:"/fileManagerJson",
        allowImageUpload : true,
        allowFileManager:true,
        resizeType : 0,//禁止拉伸，1可以上下拉伸，2上下左右拉伸
        filterMode: true,//true时过滤HTML代码，false时允许输入任何代码。
        allowPreviewEmoticons : false,
        cssData: 'body{font-family: 微软雅黑;font-size: 14px;padding:30px;}',
        afterFocus : function(){//获得焦点 删除默认文字信息
            if(editor.html() == '<span style="color:#9B9B9B;">在这里记录今天的心情</span>'){
                editor.html('');
            }
        },
        afterBlur: function(e){
            if(editor.html() == ''){
                editor.html('<span style="color:#9B9B9B;">在这里记录今天的心情</span>');
            }
            editor.sync();
        },//失去焦点，同步信息数据
        items : [
            'fontname','fontsize','bold', 'italic','strikethrough', 'insertunorderedlist', 'emoticons','undo','redo','cut','copy','paste',
            'forecolor','hilitecolor','',
            'subscript','superscript','code','table',
        ]
    });
});



var $a = function(id) {
    return "string" == typeof id ? document.getElementById(id) : id;
};

var Class = {
    create: function() {
        return function() {
            this.initialize.apply(this, arguments);
        }
    }
};

//继承函数
Object.extend = function(destination, source) {
    for (var property in source) {
        destination[property] = source[property];
    }
    return destination;
};

//选中指定日期后要触发的事件
Select = function(){
    $('table td').click(function(){

        //每次选中日期的时候都要清空
        $("#selectId").val('');

        $('table td').removeClass('AAA');
        $(this).addClass('AAA');
        $("#sysDiaryMsg").text("");
        var dateTime = $("#idCalendarYear").text() +"-"+ $("#idCalendarMonth").text() + '-' + $(this).text();
        $('#selectDay').val(dateTime) ;
        $("#date").text(dateTime);


        //载入日历和图片内容
        diaryOnLoad();
        imgOnLoad();

        //保存
        document.getElementById("saveDiaryBtn").onclick = function(){

            saveDiaryFun();
        }
        // $("#saveDiaryBtn").onclick(function () {
        //     saveDiaryFun();
        // });


    })
};

//创建Calendar构造函数
var Calendar = Class.create();
Calendar.prototype = {
    initialize: function(container, options) {
        this.Container = $a(container); //容器(table结构)
        this.Days = []; //日期对象列表
        this.SetOptions(options);
        this.Year = this.options.Year;
        this.Month = this.options.Month;
        this.Day=this.options.Day;
        this.SelectDay = this.options.SelectDay ? new Date(this.options.SelectDay) : null;
        this.onSelectDay = this.options.onSelectDay;
        this.onToday = this.options.onToday;
        this.onFinish = this.options.onFinish;
        this.Draw();
    },
    //设置默认属性
    SetOptions: function(options) {
        this.options = { //默认值
            Year: new Date().getFullYear(), //显示年
            Month: new Date().getMonth() + 1, //显示月
            Day: new Date().getDate(),
            onSelectDay: function() {}, //在选择日期触发
            onToday: function() {}, //在当天日期触发
            onFinish: function() {} //日历画完后触发
        };
        Object.extend(this.options, options || {});
    },
    //上一个月
    PreMonth: function() {
        //先取得上一个月的日期对象
        var dayPre = new Date(this.Year, this.Month - 2, 1);
        //再设置属性
        this.Year = dayPre.getFullYear();
        this.Month = dayPre.getMonth() + 1;
        //重新画日历
        this.Draw();
        //点击指定日期触发这个函数
        Select();
    },

    //下一个月
    NextMonth: function() {
        var dayNext = new Date(this.Year, this.Month, 1);
        this.Year = dayNext.getFullYear();
        this.Month = dayNext.getMonth() + 1;
        this.Draw();
        //点击指定日期触发这个函数
        Select();
    },


    //画日历
    Draw: function() {
        //用来保存日期列表
        var arr = [];
        //用当月第一天在一周中的日期值作为当月离第一天的天数 this.Month为当前月份数 系统从0开始 所以要-1
        for (var i = 1, firstDay = new Date(this.Year, this.Month - 1, 1).getDay(); i <= firstDay; i++) {
            arr.push(" ");
        }
        //用当月最后一天在一个月中的日期值作为当月的天数 0表示当月的最后一天
        for (var i = 1, monthDay = new Date(this.Year, this.Month, 0).getDate(); i <= monthDay; i++) {
            arr.push(i);
        }
        var frag = document.createDocumentFragment();
        this.Days = [];
        while (arr.length > 0) {
            //每个星期插入一个tr
            var row = document.createElement("tr");
            //每个星期有7天
            for (var i = 1; i <= 7; i++) {
                var cell = document.createElement("td");
                cell.innerHTML = " ";
                if (arr.length > 0) {
                    //shift用于将数组中第一个元素弹出 类似于pop
                    var d = arr.shift();
                    cell.innerHTML = d;
                    if (d > 0) {
                        this.Days[d] = cell;
                        //判断是否今日
                        if (this.IsSame(new Date(this.Year, this.Month - 1, d), new Date())) {
                            this.onToday(cell);
                        }
                    }
                }
                row.appendChild(cell);
            }
            frag.appendChild(row);
        }
        //先清空内容再插入(ie的table不能用innerHTML)
        while (this.Container.hasChildNodes()) {
            this.Container.removeChild(this.Container.firstChild);
        }
        this.Container.appendChild(frag);
        this.onFinish();
    },
    //判断是否同一日
    IsSame: function(d1, d2) {
        return (d1.getFullYear() == d2.getFullYear() && d1.getMonth() == d2.getMonth() && d1.getDate() == d2.getDate());
    }
};

var cale = new Calendar("idCalendar", {
    onToday: function(o) {
        o.className = "onToday";

    },
    onFinish: function() {
        //表头年月份显示
        $a("idCalendarYear").innerHTML = this.Year;
        $a("idCalendarMonth").innerHTML = this.Month;

    }
});

$a("idCalendarPre").onclick = function() {
    cale.PreMonth();
};

$a("idCalendarNext").onclick = function() {
    cale.NextMonth();
};


//点击指定日期触发函数
Select();

$("#addMemoText").click(function(){
    if($(this).val() == "在此处输入新的日程"){
        $(this).val("");
        $(this).removeClass("tip");
    }
});

$("#addMemoText").blur(function(){
    if($(this).val() == ""){
        $(this).val('在此处输入新的日程');
        $(this).addClass("tip");
    }
});

//添加日程
$("#addMemoBtn").click(function () {
    if($('#addMemoText').val() != '在此处输入新的日程'){
        addMemoFun();
        $("#addMemoText").val('在此处输入新的日程');
        $("#addMemoText").addClass("tip");
    }
});

//更新日程
$("#updateMemoBtn").click(function () {
    if($('#addMemoText').val() != '在此处输入新的日程'){
        updateMemoFun();
        $("#addMemoText").val('在此处输入新的日程');
        $("#addMemoText").addClass("tip");
    }
});

//删除日程
$("#deleteMemoBtn").click(function () {
    if(confirm("确认删除日程吗？")) {
        deleteMemoFun();
    }
});


//删除当天的日记（包括图片）
$("#deleteDiaryBtn").click(function () {
    if(confirm("删除后不可恢复，确认删除日记及照片吗？")) {
        deleteDiaryFun();
    }

});

//单击已有的memo以高亮表示
$("#memo li").click(function(){
    $("#memo li").removeClass("select");
    $(this).addClass("select");
});
$(".onToday").click();

//标记已完成的日程
$("#markMemoBtn").click(function () {
    var listId = $("#selectId").val();
    if($("#markMemoBtn").text() == "标记完成"){
        $("ol #" + listId).addClass("finishedMemo");
        $(this).text("取消标记")
    }else {
        $("ol #" + listId).removeClass("finishedMemo");
        $(this).text("标记完成");
    }
});