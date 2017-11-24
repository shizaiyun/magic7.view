<%@page import="org.magic7.core.domain.MagicCodeLib"%>
<%@page import="org.magic7.core.domain.MagicDimension"%>
<%@page import="org.magic7.core.domain.MagicSpaceRegion"%>
<%@ page import="org.magic7.core.domain.MagicSpace"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="<c:url value='/resources/js/jquery-3.2.1.min.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/js/javaEditor/lib/codemirror.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/js/javaEditor/doc/docs.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/js/javaEditor/addon/hint/show-hint.css'/>">
<script type="text/javascript" src="<c:url value='/resources/js/javaEditor/lib/codemirror.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/javaEditor/mode/clike/clike.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/javaEditor/addon/edit/matchbrackets.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/javaEditor/addon/hint/show-hint.js'/>"></script>

<style>.CodeMirror {border: 2px inset #dee;}</style>

<script>
	function back() {
		window.opener.location.reload();
		window.close();
	}
</script>
<%
	request.setAttribute("java", MagicCodeLib.CodeType.JAVA.getCode());
	request.setAttribute("js", MagicCodeLib.CodeType.JS.getCode());
%>
<title>编辑代码</title>
</head>
<body>
	<div style="background-color: #11111;">编辑代码</div>
	<form action="saveCode" method="get" id="queryForm">
		<input type="hidden" name="codeId" value="${code.id}">
		<table style="width: 100%" border=1>
			<tr>
				<td>代码名称：<input type="text" size=40 name="name" value="${code.name }"></input></td>
				<td>代码类型：<input type="text" size=40 name="name" value="${java }"></input></td>
				<td>使用说明：<input type="text" size=40 name="description" value="${code.description }"></input></td>
				<td>参数名及顺序：<input type="text" size=40 name="seq" value="${code.parameterNames }"></input></td>
			</tr>
			<tr>
				<td colspan=4><div id="textDiv">代码内容：<textarea id="java-code" name="code" rows="100">${code.code }</textarea></div></td>
			</tr>
		</table>
		<div style="text-align: center;">
			<input class="button" type="submit" value="保存" >
			<input class="button" type="button" onClick="back()" value="返回">
		</div>
	</form>
	
	<script>
      var javaEditor = CodeMirror.fromTextArea(document.getElementById("java-code"), {
        lineNumbers: true,
        matchBrackets: true,
        mode: "text/x-java"
      });
      var mac = CodeMirror.keyMap.default == CodeMirror.keyMap.macDefault;
      CodeMirror.keyMap.default[(mac ? "Cmd" : "Ctrl") + "-Space"] = "autocomplete";
      var cssValue = $('.CodeMirror-scroll').css('height');
      $('.CodeMirror-scroll').css('height',"600");
      $('.CodeMirror-scroll').css('width',"1800");
      $('.CodeMirror').css('height',"600");
      $('.CodeMirror').css('width',"1800");
      $('#textDiv').css('height',"600");
    </script>
</body>
</html>