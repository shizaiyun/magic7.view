<%@page import="org.magic7.core.domain.MagicDimension"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" uri="/magicTag.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
	function addDimension() {
		window.location="showDimensionForQuery?spaceId=${spaceId}&spaceName=${spaceName}&regionId=${regionId}&regionName=${regionName}";
	}
</script>
<title>space list</title>
</head>
<body>
<div style="background-color: #11111;">编辑查询维度</div>
<div style="text-align: right;padding-right: 10px;padding-bottom:5px">
	<input class="button" type="button" value="新增" onclick="addDimension()">
	<input class="button" type="button" value="删除" onclick="deleteItem()">
</div>
<table class="gridTable" style="width: 100%" border=1>
	<tr>
		<td>维度名称</td>
		<td>维度显示名</td>
		<td>维度说明</td>
		<td>数据类型</td>
		<td>查询类型</td>
	</tr>
	<c:forEach var="dimension" items="${dimensions }">
		<tr>
			<td><a href="showDimensionForQuery?dimensionId=${dimension.id }&spaceName=${dimension.spaceName}&spaceId=${dimension.spaceId}&regionId=${dimension.spaceRegionId}&regionName=${dimension.spaceRegionName}">${dimension.name}</a></td>
			<td>${dimension.displayName}</td>
			<td>${dimension.description}</td>
			<td>${dimension.valueTypeName}</td>
			<td>${dimension.queryTypeName}</td>
		</tr>
	</c:forEach>
</table>

</body>

</html>