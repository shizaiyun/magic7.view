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
<title>编辑选择项取值</title>
</head>
<body>
	<div style="background-color: #11111;">编辑选择项取值</div>
	<form action="saveChoiceItem" method="get" id="queryForm">
		<input type="hidden" name="itemId" value="${item.id}">
		<input type="hidden" name="choiceId" value="${choiceId}">
		<table style="width: 100%" border=1>
			<tr>
				<td>选择项取值名称：<input type="text" size=40 name="valueName" value="${item.valueName }"></input></td>
				<td>选择项取值编码：<input type="text" size=40 name="valueCode" value="${item.valueCode }"></input></td>
				<td>顺序：<input type="text" size=40 name="seq" value="${item.seq }"></input></td>
			</tr>
		</table>
		<div style="text-align: center;">
			<input class="button" type="submit" value="保存" >
			<input class="button" type="button" onClick="back()" value="返回">
		</div>
	</form>
</body>
</html>