<%@page import="org.magic7.core.domain.MagicDimension"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" uri="/magicTag.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
	function addDimensionForButton() {
		window.location="showDimension?spaceId=${spaceId}&spaceName=${spaceName}&regionId=${regionId}&regionName=${regionName}&command=forButton";
	}
</script>
<title>按钮 list</title>
</head>
<body>
<div style="background-color: #11111;">编辑按钮</div>
<div style="text-align: right;padding-right: 10px;padding-bottom:5px">
	<input class="button" type="button" value="新增" onclick="addDimensionForButton()">
	<input class="button" type="button" value="删除" onclick="deleteItem()">
</div>
<table class="gridTable" style="width: 100%" border=1>
	<tr>
		<td>按钮名称</td>
		<td>按钮显示名</td>
		<td>按钮说明</td>
		<td>触发事件</td>
		<td>顺序</td>
	</tr>
	<c:forEach var="dimension" items="${dimensionsForButton }">
		<tr>
			<td><a href="showDimension?dimensionId=${dimension.id }&spaceName=${dimension.spaceName}&spaceId=${dimension.spaceId}&regionId=${dimension.spaceRegionId}&regionName=${dimension.spaceRegionName}&command=forButton">${dimension.name}</a></td>
			<td>${dimension.displayName}</td>
			<td>${dimension.description}</td>
			<td>${dimension.buttonTrigger}</td>
			<td>${dimension.seq}</td>
		</tr>
	</c:forEach>
</table>

</body>

</html>