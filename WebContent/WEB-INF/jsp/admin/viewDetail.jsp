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
<title>编辑视图</title>
<%
	request.setAttribute("forQuery", MagicDimension.Destination.FOR_QUERY);
	request.setAttribute("forData", MagicDimension.Destination.FOR_DATA);
	request.setAttribute("forButton", MagicDimension.Destination.FOR_BUTTON);
	request.setAttribute("forTemp", MagicDimension.Destination.FOR_TEMP);
	
	request.setAttribute("button", MagicDimension.PageType.BUTTON);
	request.setAttribute("checkBox", MagicDimension.PageType.CHECK_BOX);
	request.setAttribute("dropDownList", MagicDimension.PageType.DROP_DOWN_LIST);
	request.setAttribute("popUp", MagicDimension.PageType.POP_UP);
	request.setAttribute("radio", MagicDimension.PageType.RADIO);
	request.setAttribute("textArea", MagicDimension.PageType.TEXT_AREA);
	request.setAttribute("textEditor", MagicDimension.PageType.TEXT_EDITOR);
	
	request.setAttribute("strValue", MagicDimension.ValueType.STR_VALUE);
	request.setAttribute("dateValue", MagicDimension.ValueType.DATE_VALUE);
	request.setAttribute("booleanValue", MagicDimension.ValueType.BOOLEAN_VALUE);
	request.setAttribute("numValue", MagicDimension.ValueType.NUM_VALUE);
	request.setAttribute("listStr", MagicDimension.ValueType.LIST_STR_VALUE);
	request.setAttribute("attachment", MagicDimension.ValueType.ATTACHMENT_VALUE);
%>
</head>
<body>
	<div style="background-color: #11111;">编辑维度</div>
	<form action="saveView" method="get" id="queryForm">
		<input type="hidden" name="regionId" value="${regionId }">
		<input type="hidden" name="regionName" value="${regionName }">
		<input type="hidden" name="spaceId" value="${spaceId }">
		<input type="hidden" name="spaceName" value="${spaceName}">
		<input type="hidden" name="viewId" value="${view.id}">
		<table style="width: 100%" border=1>
			<tr>
				<td>视图名称：<input type="text" size=40 name="name" value="${view.name }"></input></td>
				<td>视图用途：
					<select name="destination">
						<option <c:if test="${view.destination eq forData.code}">selected</c:if> value ="${forData.code }">存储</option>
						<option <c:if test="${view.destination eq forQuery.code}">selected</c:if> value ="${forQuery.code }">查询</option>
						<option <c:if test="${view.destination eq forButton.code}">selected</c:if> value ="${forButton.code }">按钮</option>
					</select>
				</td>
			</tr>
		</table>
		<div style="text-align: center;">
			<input class="button" type="submit" value="保存" >
			<input class="button" type="button" onClick="back()" value="返回">
		</div>
	</form>
</body>
<hr>
<jsp:include page="viewItemList.jsp" flush="true" />
</html>