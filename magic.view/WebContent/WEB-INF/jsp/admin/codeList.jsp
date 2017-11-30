<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" uri="/magicTag.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
	function addCode() {
		window.open('showCode');
	}
</script>
<title>微观关系列表</title>
</head>
<body>
<div style="background-color: #11111;">编辑微观关系</div>
<div style="text-align: right;padding-right: 10px;padding-bottom:5px">
	<input class="button" type="button" value="新增" onclick="addCode()">
	<input class="button" type="button" value="删除" onclick="deleteItem()">
</div>
<table class="gridTable" style="width: 100%" border=1>
	<tr>
		<td>关系名称</td>
		<td>关系描述</td>
		<td>关系代码类型</td>
		<td>参数及顺序</td>
	</tr>
	<c:forEach var="code" items="${codes }">
		<tr>
			<td><a href="javascript:window.open('showCode?codeId=${code.id }')">${code.name}</a></td>
			<td>${code.description}</td>
			<td>${code.codeTypeName}</td>
			<td>${code.parameterNames}</td>
		</tr>
	</c:forEach>
</table>

</body>

</html>