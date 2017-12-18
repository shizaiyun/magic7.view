<%@page import="org.magic7.core.domain.MagicDimension.PersistenceType"%>
<%@page import="org.magic7.core.domain.MagicDimension.ValueType"%>
<%@page import="org.magic7.core.domain.MagicDimension.PageType"%>
<%@page import="org.magic7.core.domain.MagicDimension.Destination"%>
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
	Destination[] destionations = MagicDimension.Destination.values();
	for(Destination dest:destionations)
		request.setAttribute(dest.getName(), dest);
	request.setAttribute("destionations", destionations);
	
	PageType[] pageTypes = MagicDimension.PageType.values();
	for(PageType pageType:pageTypes)
		request.setAttribute(pageType.getName(), pageType);
	request.setAttribute("pageTypes", pageTypes);
	
	ValueType[] valueTypes = MagicDimension.ValueType.values();
	for(ValueType valueType:valueTypes)
		request.setAttribute(valueType.getName(), valueType);
	request.setAttribute("valueTypes", valueTypes);
	
	PersistenceType[] persistenceTypes = MagicDimension.PersistenceType.values();
	for(PersistenceType persistenceType:persistenceTypes)
		request.setAttribute(persistenceType.getName(), persistenceType);
	request.setAttribute("persistenceTypes", persistenceTypes);
	
	MagicService service = MagicServiceFactory.getMagicService();
	request.setAttribute("choices", service.listChoice(null, null));
%>
</head>
<body>
<div style="background-color: #11111;">编辑维度</div>
<form action="saveDimension" method="get" id="queryForm">
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
			<td>分区顺序：<input type="text" name="seq" value="${dimension.seq }"></input></td>
			<td>用途：
				<select name="destination">
					<c:forEach var="dest" items="${destionations }">
						<option <c:if test="${dimension.destination eq dest.code}">selected</c:if> value ="${dest.code }">${dest.name }</option>
					</c:forEach>
				</select>
			</td>
			<td valign="middle" nowrap="nowrap">页面元素类型：
				<select name="pageType" style="vertical-align: middle;" 
				onchange="choiceDiv.style.display='None';urlDive.style.display='None';if(this.selectedIndex==1) choiceDiv.style.display='Inline'; else if(this.selectedIndex==2) urlDive.style.display='Inline'; ">
					<c:forEach var="pageType" items="${pageTypes }">
						<option <c:if test="${dimension.pageType eq pageType.code}">selected</c:if> value ="${pageType.code }">${pageType.name }</option>
					</c:forEach>
				</select>
				<div id="choiceDiv" style="display: none;">请输入选择项：
					<select name="choiceCode">
						<c:forEach var="choice" items="${choices }">
							<option <c:if test="${dimension.choiceCode eq choice.choiceCode }">selected</c:if> value="${choice.choiceCode }">${choice.choiceName }</option>
						</c:forEach>
					</select>
				</div>
				<div id="urlDive" style="display: none;">请输入弹出框url地址：<textarea name="popUpUrl" style="vertical-align: middle;" rows="3" cols="30">${dimension.url }</textarea></div>
				<c:if test="${not empty dimension.pageType and dimension.pageType eq Drop_Down_List.code}"><script>choiceDiv.style.display='Inline';</script></c:if>
				<c:if test="${not empty dimension.pageType and dimension.pageType eq Risk_Pop_Up.code}"><script>urlDive.style.display='Inline';</script></c:if>
			</td>
		</tr>
		<tr>
			<td>数据类型：
				<select name="valueType">
					<c:forEach var="valueType" items="${valueTypes }">
						<option <c:if test="${dimension.valueType eq valueType.code}">selected</c:if> value ="${valueType.code}">${valueType.name }</option>
					</c:forEach>
				</select>
			</td>
			<td>是否必填：
				<select name="required">
					<option <c:if test="${dimension.required eq true}">selected</c:if> value ="true">是</option>
					<option <c:if test="${dimension.required eq false}">selected</c:if> value ="false">否</option>
				</select>
			</td>
			<td>是否可编辑：
				<select name="editable">
					<option <c:if test="${dimension.editable eq true}">selected</c:if> value ="true">是</option>
					<option <c:if test="${dimension.editable eq false}">selected</c:if> value ="false">否</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>是否可见：
				<select name="visible">
					<option <c:if test="${dimension.visible eq true}">selected</c:if> value ="true">是</option>
					<option <c:if test="${dimension.visible eq false}">selected</c:if> value ="false">否</option>
				</select>
			</td>
			<td>是否链接到其他字段：
				<select name="lnk" disabled>
					<option <c:if test="${dimension.lnk eq true}">selected</c:if> value ="true">是</option>
					<option <c:if test="${dimension.lnk eq false}">selected</c:if> selected value ="false">否</option>
				</select>
			</td>
			<td>存储类型：
				<select name="persistenceType">
					<c:forEach var="persistenceType" items="${persistenceTypes }">
						<option <c:if test="${dimension.persistenceType eq persistenceType.code}">selected</c:if> value ="${persistenceType.code }">${persistenceType.name }</option>
					</c:forEach>
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
</body>

</html>