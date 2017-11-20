<%@page import="org.magic7.core.domain.MagicSpaceRegion"%>
<%@ page import="org.magic7.core.domain.MagicSpace"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
	function back() {
		window.location="showSpace?spaceId=${spaceId}";
	}
</script>
<title>编辑分区</title>
<%
	request.setAttribute("main", MagicSpaceRegion.RegionType.MAIN);
	request.setAttribute("tab", MagicSpaceRegion.RegionType.TAB);
%>
</head>
<body>
<div style="background-color: #11111;">编辑分区</div>
<form action="saveRegion" method="get" id="queryForm">
	<input type="hidden" name="regionId" value="${region.id }">
	<input type="hidden" name="spaceId" value="${spaceId }">
	<input type="hidden" name="spaceName" value="${spaceName}">
	<table style="width: 100%" border=1>
		<tr>
			<td>分区名称：<input type="text" name="name" value="${region.name }"></input></td>
			<td>分区说明：<input type="text" name="description" value="${region.description }"></input></td>
			<td>分区分隔符：<input type="text" name="partition" value="${region.partition }"></input></td>
		</tr>
		<tr>
			<td>分区顺序：<input type="text" name="seq" value="${region.seq }"></input></td>
			<td>是否多行数据：
				<select name="multiply">
					<option <c:if test="${region.multiply eq true}">selected</c:if> value ="true">是</option>
					<option <c:if test="${region.multiply eq false}">selected</c:if> value ="false">否</option>
				</select>
			</td>
			<td>分区类型：
				<select name="regionType">
					<option <c:if test="${region.regionType eq main.code}">selected</c:if> value ="${main.code}">主页面</option>
					<option <c:if test="${region.regionType eq tab.code}">selected</c:if> value ="${tab.code }">TAB页面</option>
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
<jsp:include page="dimensionList.jsp" flush="true" />
</body>

</html>