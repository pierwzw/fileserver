<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<title>fz-video</title>
	<style>
	body,div,dl,dt,dd,ul,ol,li,h1,h2,h3,h4,h5,h6,pre,code,form,fieldset,legend,input,button,textarea,p,blockquote,th,td { margin:0; padding:0; }
	body { background:#fff; color:#555; font-size:14px; font-family: Verdana, Arial, Helvetica, sans-serif; }
	td,th,caption { font-size:14px; }
	h1, h2, h3, h4, h5, h6 { font-weight:normal; font-size:100%; }
	address, caption, cite, code, dfn, em, strong, th, var { font-style:normal; font-weight:normal;}
	a { color:#555; text-decoration:none; }
	a:hover { text-decoration:underline; }
	img { border:none; }
	ol,ul,li { list-style:none; }
	input, textarea, select, button { font:14px Verdana,Helvetica,Arial,sans-serif; }
	table { border-collapse:collapse; }
	html {overflow-y: scroll;} 
	.clearfix:after {content: "."; display: block; height:0; clear:both; visibility: hidden;}
	.clearfix { *zoom:1; }
	body{
		font-family:  "微软雅黑";
	}
	#testBox{
		width: 953px;
		height: 537px;
		margin: 0 auto;
	}
</style>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/fz-video.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/font/iconfont.css">
</head>
<body>
	<div id="testBox">

	</div>
<script src="${pageContext.request.contextPath}/resources/js/fz-video.js"></script>
<script>
 	var test = new createVideo(
 		"testBox",
 		{
 			url 		: '/resources/static/俩俩相望-小昭.mp4',
 			autoplay	: true
 		}
 	);
</script>
</body>
</html>