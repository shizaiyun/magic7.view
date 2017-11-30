<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" uri="/magicTag.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>核心概念列表</title>
<script>
	function addItem() {
		window.location="showSpace";
	}
</script>

</head>
<body>
<form action="" method="get" id="queryForm">
	按名称查询：<input type="text" name="spaceName" value="${spaceName }"></input>
	<div style="text-align: center;">
		<input class="button" type="submit" value="查询" >
		<input class="button" type="reset" value="重置">
	</div>
</form>
<hr>
<div style="text-align: right;padding-right: 10px;padding-bottom:5px">
	<input class="button" type="button" value="新增" onclick="addItem()">
	<input class="button" type="button" value="删除" onclick="deleteItem()">
</div>
<table class="gridTable" style="width: 100%" border=1>
	<tr>
		<td>核心概念名称</td>
		<td>核心概念说明</td>
		<td>核心概念分隔符</td>
		<td>是否有效</td>
	</tr>
	<c:forEach var="space" items="${spaces }">
		<tr>
			<td><a href="showSpace?spaceId=${space.id }">${space.name}</a></td>
			<td>${space.description}</td>
			<td>${space.partition}</td>
			<td>${space.valid}</td>
		</tr>
	</c:forEach>
</table>

</body>

</html>