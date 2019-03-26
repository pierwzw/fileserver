<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>文件上传与下载</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.js" language="javascript"></script>
</head>
<body>
<form action="${pageContext.request.contextPath}/fileUpload" method="post" enctype="multipart/form-data">
    文件：<input type="file" name="file"/><br/>
    <input type="submit" value="提交"/><br/>
</form>
<button id='button' name='button' type="button">刷新文件列表</button>
<ul>
</ul>
<p><span></span></p>
<%--<a href="${pageContext.request.contextPath}/fileDownload">文件下载</a>--%>

<script>
    function GetListItems() {
        $.ajax({
            type: "POST",
            url: "/display",
            contentType: "application/json; charset=utf-8",
            data: "{}",
            dataType: "json",
            success: function (result) {
                display(result);
            },
            "error": function (result) {
                $("span").empty()
                $("span").append(result.responseText)
            }
        });
    }
    function display(json){
        $("ul").empty()
        if (JSON.stringify(json) == "null"){
            $("ul").append("没有文件")
            return
        }
        $.each(json, function(key, value) {
            var previewUrl
            if (key.endWith("mp3")){
                previewUrl = "<a href=/play/audio?filename="+key+">"
            } else if (key.endWith("mp4")){
                previewUrl = "<a href=/play/video?filename="+key+">"
            } else {
                previewUrl = "<a href=/preview?filename="+key+">"
            }
            var itemHTML = ["<li>",
                "<div>",
                "<a href=/fileDownload?filename="+key+">" + key +"</a>" ,
                value,
                previewUrl + "预览" +"</a>" ,
                "</div>",
                "</li>"].join('\n');
            $("ul").append(itemHTML);
        });
    }
    /*function display(list){
        if (list.length==0){
            $("ul").append("没有文件");
            return
        }
        $("ul").empty()
        for(var n in map){
            var itemHTML = ["<li>",
                "<div>",
                "<a href=/fileDownload?filename="+list[n]+">" + list[n] +"</a>",
                "</div>",
                "</li>"].join('\n');
            $("ul").append(itemHTML);
        }
    }*/
    /*function DisplayListItems(list) {
        $.each(list, function(index, element) {
            var itemHTML = ["<li>",
                "<div>",
                "<div>",
                element.Title,
                "</div>",
                "<div>",
                element.Description,
                "</div>",
                "</div>",
                "</li>"].join('\n');
            $("ul").append(itemHTML);
        });
    }*/
    $(document).ready(function(){
        /*$("#button").click(function () {*/
            GetListItems()
        /*});*/

    });
    $("button").click(function(){
        GetListItems()
    });

    String.prototype.endWith=function(s){
        if(s==null||s==""||this.length==0||s.length>this.length)
            return false;
        if(this.substring(this.length-s.length)==s)
            return true;
        else
            return false;
        return true;
    }

</script>
</body>
</html>