<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" uri="/magicTag.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
	function addView() {
		window.open('showChoiceItem?choiceId=${choice.id}');
	}
</script>
<title>view list</title>
</head>
<body>
<div style="background-color: #11111;">编辑选择项取值</div>
<div style="text-align: right;padding-right: 10px;padding-bottom:5px">
	<input class="button" type="button" value="新增" onclick="addView()">
	<input class="button" type="button" value="删除" onclick="deleteItem()">
</div>
<table class="gridTable" style="width: 100%" border=1>
	<tr>
		<td>选择项取值名称</td>
		<td>选择项取值编码</td>
		<td>顺序</td>
	</tr>
	<c:forEach var="item" items="${items }">
		<tr>
			<td><a href="javascript:window.open('showChoiceItem?itemId=${item.id }&choiceId=${choice.id}')">${item.valueName}</a></td>
			<td>${item.valueCode}</td>
			<td>${item.seq}</td>
		</tr>
	</c:forEach>
</table>

</body>

</html>