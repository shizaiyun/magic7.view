<%@page import="org.magic7.core.service.MagicServiceFactory"%>
<%@page import="org.magic7.core.service.MagicService"%>
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
		window.location="showRegion?regionId=${regionId}&spaceName=${spaceName}&spaceId=${spaceId}";
	}
</script>
<title>编辑维度</title>
<%
	request.setAttribute("forQuery", MagicDimension.Destination.FOR_QUERY);
	request.setAttribute("forData", MagicDimension.Destination.FOR_DATA);
	request.setAttribute("forButton", MagicDimension.Destination.FOR_BUTTON);
	
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
	
	request.setAttribute("precise", MagicDimension.QueryType.PRECISE);
	request.setAttribute("in", MagicDimension.QueryType.IN);
	request.setAttribute("vague", MagicDimension.QueryType.VAGUE);
	request.setAttribute("bigger", MagicDimension.QueryType.BIGGER);
	request.setAttribute("smaller", MagicDimension.QueryType.SMALLER);
	
	MagicService service = MagicServiceFactory.getMagicService();
	request.setAttribute("choices", service.listChoice(null, null));
%>
</head>
<body>
<div style="background-color: #11111;">编辑维度</div>
<form action="saveDimensionForButton" method="get" id="queryForm">
	<input type="hidden" name="regionId" value="${regionId }">
	<input type="hidden" name="regionName" value="${regionName }">
	<input type="hidden" name="spaceId" value="${spaceId }">
	<input type="hidden" name="spaceName" value="${spaceName}">
	<input type="hidden" name="dimensionId" value="${dimension.id}">
	<table style="width: 100%" border=1>
		<tr>
			<td width="33%">维度名称：<input type="text" name="name" value="${dimension.name }"></input></td>
			<td width="33%">维度显示名：<input type="text" name="displayName" value="${dimension.displayName }"></input></td>
			<td width="33%">维度说明：<input type="text" name="description" value="${dimension.description }"></input></td>
		</tr>
		<tr>
			<td>触发事件：<input type="text" name="buttonTrigger" value="${dimension.buttonTrigger }"></input></td>
			<td>分区顺序：<input type="text" name="seq" value="${dimension.seq }"></input></td>
		</tr>
	</table>
	<div style="text-align: center;">
		<input class="button" type="submit" value="保存" >
		<input class="button" type="button" onClick="back()" value="返回">
	</div>
</form>
<hr>
</body>

</html>