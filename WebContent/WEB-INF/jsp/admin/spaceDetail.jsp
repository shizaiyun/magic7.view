<%@ page import="org.magic7.core.domain.MagicSpace"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
	function back() {
		window.location="listSpaces";
	}
</script>
<title>编辑核心</title>
<%
	request.setAttribute("horizontal", MagicSpace.TabLayout.HORIZONTAL);
	request.setAttribute("vertical", MagicSpace.TabLayout.VERTICAL);
%>
</head>
<body>
<div style="background-color: #11111;">编辑核心</div>
<form action="saveSpace" method="get" id="queryForm">
	<input type="hidden" name="spaceId" value="${space.id }">
	<table style="width: 100%" border=1>
		<tr>
			<td>核心概念名称：<input type="text" name="name" value="${space.name }"></input></td>
			<td>核心概念说明：<input type="text" name="description" value="${space.description }"></input></td>
			<td>核心概念分隔符：<input type="text" name="partition" value="${space.partition }"></input></td>
		</tr>
		<tr>
			<td>核心概念布局：
				<select name="tabLayout">
					<option <c:if test="${space.tabLayout eq horizontal.name }">selected</c:if> value ="${horizontal.name }">水平分布</option>
					<option <c:if test="${space.tabLayout eq vertical.name }">selected</c:if> value ="${vertical.name }">垂直分布</option>
				</select>
			</td>
			<td>是否有效：
				<select name="valid">
					<option <c:if test="${space.valid eq true}">selected</c:if> value ="true">是</option>
					<option <c:if test="${space.valid eq false}">selected</c:if> value ="false">否</option>
				</select>
			</td>
		</tr>
	</table>
	<div style="text-align: center;">
		<input class="button" type="submit" value="保存" >
		<input class="button" type="button" onClick="back()" value="返回">
	</div>
</form>
<hr>
<c:if test="${not empty space }">
<jsp:include page="regionList.jsp" flush="true"></jsp:include>
</c:if>
</body>

</html>