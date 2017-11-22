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
					<option <c:if test="${dimension.destination eq forData.code}">selected</c:if> value ="${forData.code }">存储</option>
					<option <c:if test="${dimension.destination eq forQuery.code}">selected</c:if> value ="${forQuery.code }">查询</option>
					<option <c:if test="${dimension.destination eq forButton.code}">selected</c:if> value ="${forButton.code }">按钮</option>
					<option <c:if test="${dimension.destination eq forTemp.code}">selected</c:if> value ="${forButton.code }">临时变量</option>
				</select>
			</td>
			<td valign="middle" nowrap="nowrap">页面元素类型：
				<select name="pageType" style="vertical-align: middle;" 
				onchange="choiceDiv.style.display='None';urlDive.style.display='None';if(this.selectedIndex==1) choiceDiv.style.display='Inline'; else if(this.selectedIndex==2) urlDive.style.display='Inline'; ">
					<option <c:if test="${dimension.pageType eq textEditor.code}">selected</c:if> value ="${textEditor.code }">文本框</option>
					<option <c:if test="${dimension.pageType eq dropDownList.code}">selected</c:if> value ="${dropDownList.code }" >下拉列表</option>
					<option <c:if test="${dimension.pageType eq popUp.code}">selected</c:if> value ="${popUp.code }">弹出框</option>
					<option <c:if test="${dimension.pageType eq textArea.code}">selected</c:if> value ="${textArea.code }">文本域</option>
					<option <c:if test="${dimension.pageType eq checkBox.code}">selected</c:if> value ="${checkBox.code }">复选框</option>
					<option <c:if test="${dimension.pageType eq radio.code}">selected</c:if> value ="${radio.code }">单选框</option>
					<option <c:if test="${dimension.pageType eq button.code}">selected</c:if> value ="${button.code}">按钮</option>
				</select>
				<div id="choiceDiv" style="display: none;">请输入选择项：
					<select name="choiceCode">
						<c:forEach var="choice" items="${choices }">
							<option value="${choice.choiceCode }">${choice.choiceName }</option>
						</c:forEach>
					</select>
				</div>
				<div id="urlDive" style="display: none;">请输入弹出框url地址：<textarea name="popUpUrl" style="vertical-align: middle;" rows="3" cols="30">${dimension.url }</textarea></div>
				<c:if test="${dimension.pageType eq dropDownList.code}"><script>choiceDiv.style.display='Inline';</script></c:if>
				<c:if test="${dimension.pageType eq popUp.code}"><script>urlDive.style.display='Inline';</script></c:if>
			</td>
		</tr>
		<tr>
			<td>数据类型：
				<select name="valueType">
					<option <c:if test="${dimension.valueType eq strValue.code}">selected</c:if> value ="${strValue.code}">字符串</option>
					<option <c:if test="${dimension.valueType eq dateValue.code}">selected</c:if> value ="${dateValue.code }">日期</option>
					<option <c:if test="${dimension.valueType eq booleanValue.code}">selected</c:if> value ="${booleanValue.code }">布尔值</option>
					<option <c:if test="${dimension.valueType eq numValue.code}">selected</c:if> value ="${numValue.code }">数字</option>
					<option <c:if test="${dimension.valueType eq listStr.code}">selected</c:if> value ="${listStr.code }">字符数组</option>
					<option <c:if test="${dimension.valueType eq attachment.code}">selected</c:if> value ="${attachment.code }">附件</option>
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