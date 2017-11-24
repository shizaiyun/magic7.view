<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" uri="/magicTag.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
	function addView() {
		window.open('showCode');
	}
</script>
<title>view list</title>
</head>
<body>
<div style="background-color: #11111;">编辑选择项</div>
<div style="text-align: right;padding-right: 10px;padding-bottom:5px">
	<input class="button" type="button" value="新增" onclick="addCode()">
	<input class="button" type="button" value="删除" onclick="deleteItem()">
</div>
<table class="gridTable" style="width: 100%" border=1>
	<tr>
		<td>代码名称</td>
		<td>代码签名</td>
		<td>代码内容</td>
	</tr>
	<c:forEach var="code" items="${codes }">
		<tr>
			<td><a href="javascript:window.open('showCode?codeId=${code.id }')">${code.name}</a></td>
			<td>${code.signature}</td>
			<td>${code.code}</td>
		</tr>
	</c:forEach>
</table>

</body>

</html>