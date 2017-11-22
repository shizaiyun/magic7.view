<%@page import="org.magic7.core.domain.MagicDimension"%>
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
		window.opener.location.reload();
		window.close();
	}
</script>
<title>编辑选择项</title>
</head>
<body>
	<div style="background-color: #11111;">编辑选择项</div>
	<form action="saveChoice" method="get" id="queryForm">
		<input type="hidden" name="choiceId" value="${choice.id}">
		<table style="width: 100%" border=1>
			<tr>
				<td>选择项名称：<input type="text" size=40 name="name" value="${choice.choiceName }"></input></td>
				<td>选择项编码：<input type="text" size=40 name="code" <c:if test="${not empty choice.choiceCode}">disabled</c:if> value="${choice.choiceCode }"></input></td>
			</tr>
		</table>
		<div style="text-align: center;">
			<input class="button" type="submit" value="保存" >
			<input class="button" type="button" onClick="back()" value="返回">
		</div>
	</form>
</body>
<hr>
<c:if test="${ not empty choice}">
	<jsp:include page="choiceItemList.jsp" flush="true" />
</c:if>
</html>