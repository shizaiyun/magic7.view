<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" uri="/magicTag.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
	function addItem() {
		window.location="showRegion?spaceId=${spaceId}&spaceName=${spaceName}";
	}
</script>
<title>view list</title>
</head>
<body>
<div style="background-color: #11111;">编辑视图</div>
<div style="text-align: right;padding-right: 10px;padding-bottom:5px">
	<input class="button" type="button" value="新增" onclick="addItem()">
	<input class="button" type="button" value="删除" onclick="deleteItem()">
</div>
<table class="gridTable" style="width: 100%" border=1>
	<tr>
		<td>视图名称</td>
	</tr>
	<c:forEach var="view" items="${views }">
		<tr>
			<td><a href="showView?regionId=${region.id}&spaceName=${region.spaceName}&spaceId=${region.spaceId}">${view.name}</a></td>
		</tr>
	</c:forEach>
</table>

</body>

</html>