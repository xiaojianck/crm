<%--
  Created by IntelliJ IDEA.
  User: 39281
  Date: 2019/6/14
  Time: 13:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <base href="${pageContext.request.contextPath}/">
    <title>Title</title>
</head>
<body>
$(function(){
$("#remark").focus(function(){
if(cancelAndSaveBtnDefault){
//设置remarkDiv的高度为130px
$("#remarkDiv").css("height","130px");
//显示
$("#cancelAndSaveBtn").show("2000");
cancelAndSaveBtnDefault = false;
}
});

$("#cancelBtn").click(function(){
//显示
$("#cancelAndSaveBtn").hide();
//设置remarkDiv的高度为130px
$("#remarkDiv").css("height","90px");
cancelAndSaveBtnDefault = true;
});

$(".remarkDiv").mouseover(function(){
$(this).children("div").children("div").show();
});

$(".remarkDiv").mouseout(function(){
$(this).children("div").children("div").hide();
});

$(".myHref").mouseover(function(){
$(this).children("span").css("color","red");
});

$(".myHref").mouseout(function(){
$(this).children("span").css("color","#E6E6E6");
});


//页面加载完毕后，展现关联的备注信息列表
showRemarkList();

$("#remarkBody").on("mouseover",".remarkDiv",function(){
$(this).children("div").children("div").show();
})
$("#remarkBody").on("mouseout",".remarkDiv",function(){
$(this).children("div").children("div").hide();
})

//为保存按钮绑定事件，执行备注的添加操作
$("#saveRemarkBtn").click(function () {

$.ajax({

url : "workbench/activity/saveRemark.do",
data : {

"noteContent" : $.trim($("#remark").val()),
"activityId" : "${a.id}"

},
type : "post",
dataType : "json",
success : function (data) {

/*

data
{"success":true/false,"ar":{备注}}

*/

if(data.success){

//添加备注成功

//清空文本域中的信息
$("#remark").val("");

//创建一个div，做before操作

var html = "";
html += '<div id="'+data.ar.id+'" class="remarkDiv" style="height: 60px;">';
    html += '<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">';
    html += '<div style="position: relative; top: -40px; left: 40px;" >';
        html += '<h5>'+data.ar.noteContent+'</h5>';
        html += '<font color="gray">市场活动</font> <font color="gray">-</font> <b>${a.name}</b> <small style="color: gray;"> '+(data.ar.createTime)+' 由'+(data.ar.createBy)+'</small>';
        html += '<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">';
            html += '<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: red;"></span></a>';
            html += '&nbsp;&nbsp;&nbsp;&nbsp;';
            html += '<a class="myHref" href="javascript:void(0);" onclick="delete123(\''+data.ar.id+'\')"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #FF0000;"></span></a>';
            html += '</div>';
        html += '</div>';
    html += '</div>';

//在指定元素的上方追加新元素
$("#remarkDiv").before(html);

}else{

alert("添加备注失败");

}

}

})

})

//为更新按钮绑定事件，执行备注的修改操作
$("#updateRemarkBtn").click(function () {

var id = $("#remarkId").val();
var noteContent = $("#noteContent").val();

$.ajax({

url : "workbench/activity/updateRemark.do",
data : {

"id" : id,
"noteContent" : noteContent

},
type : "post",
dataType : "json",
success : function (data) {

/*

data
{"success":true/false,"ar":{备注}}

*/

if(data.success){

//修改备注成功

//更新备注信息，更新时间，更新人
$("#e"+id).html(data.ar.noteContent);
$("#s"+id).html(data.ar.editTime+" 由"+data.ar.editBy);

//关闭修改备注的模态窗口
$("#editRemarkModal").modal("hide");


}else{

alert("修改备注失败");

}


}

})

})


});

function showRemarkList() {

$.ajax({

url : "workbench/activity/getRemarkById.do",
data : {

"activityId" : "${a.id}"

},
type : "get",
dataType : "json",
success : function (data) {

/*

data

List<ActivityRemark> arList ...

    [{备注1},{2},{3}..]

    */

    var html = "";

    $.each(data,function (i,n) {

    html += '<div id="'+n.id+'" class="remarkDiv" style="height: 60px;">';
        html += '<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">';
        html += '<div style="position: relative; top: -40px; left: 40px;" >';
            html += '<h5 id="e'+n.id+'">'+n.noteContent+'</h5>';
            html += '<font color="gray">市场活动</font> <font color="gray">-</font> <b>${a.name}</b> <small id="s'+n.id+'" style="color: gray;"> '+(n.editFlag==0?n.createTime:n.editTime)+' 由'+(n.editFlag==0?n.createBy:n.editBy)+'</small>';
            html += '<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">';
                html += '<a class="myHref" href="javascript:void(0);" onclick="editRemark(\''+n.id+'\')"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: red;"></span></a>';
                html += '&nbsp;&nbsp;&nbsp;&nbsp;';
                html += '<a class="myHref" href="javascript:void(0);" onclick="deleteReamrk(\''+n.id+'\')"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #FF0000;"></span></a>';
                html += '</div>';
            html += '</div>';
        html += '</div>';

    })

    //在指定元素的上方追加新元素
    $("#remarkDiv").before(html);


    }

    })

    }

    function deleteReamrk(id) {

    $.ajax({

    url : "workbench/activity/deleteRemark.do",
    data : {

    "id" : id

    },
    type : "post",
    dataType : "json",
    success : function (data) {

    /*

    data
    {"success":true/false}

    */

    if(data.success){

    //删除成功后
    //刷新列表？？？不行，会不断的追加元素
    //showRemarkList();

    //找到删除的元素对应的div，将div移除掉
    $("#"+id).remove();

    }else{

    alert("删除备注失败");

    }


    }

    })


    }

    function editRemark(id) {

    //根据e+id的方式找到指定的h5标签，取得标签对中的内容，这个内容就是需要编辑的备注信息
    var noteContent = $("#e"+id).html();

    //为修改备注操作模态窗口中的文本域，赋予该备注信息
    $("#noteContent").val(noteContent);

    //为修改额闭住操作模态窗口中的隐藏域id赋值
    $("#remarkId").val(id);

    $("#editRemarkModal").modal("show");

    }

</body>
</html>
