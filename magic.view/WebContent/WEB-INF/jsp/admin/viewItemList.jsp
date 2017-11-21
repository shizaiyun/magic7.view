<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" uri="/magicTag.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
	function addViewItem() {
		window.open("showViewItem?spaceId=${spaceId}&spaceName=${spaceName}&regionName=${regionName}&regionId=${regionId}&viewId=${view.id}&viewName=${view.name}");
	}
</script>
<title>view item list</title>
</head>
<body>
<div style="background-color: #11111;">编辑视图项</div>
<div style="text-align: right;padding-right: 10px;padding-bottom:5px">
	<input class="button" type="button" value="新增视图项" onclick="addViewItem()">
	<input class="button" type="button" value="删除" onclick="deleteItem()">
</div>
<table class="gridTable" style="width: 100%" border=1>
	<tr>
		<td>视图项名称</td>
		<td>对应维度</td>
		<td>是否可编辑</td>
		<td>是否必填</td>
		<td>是否可见</td>
		<td>顺序</td>
	</tr>
	<c:forEach var="item" items="${items }">
		<tr>
			<td><a href="javascript:window.open('showViewItem?itemId=${item.id }')">${item.name}</a></td>
			<td>${item.dimension.description}</td>
			<td>${item.editable}</td>
			<td>${item.required}</td>
			<td>${item.visible}</td>
			<td>${item.seq}</td>
		</tr>
	</c:forEach>
</table>

</body>

</html>