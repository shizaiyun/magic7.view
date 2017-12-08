<%@page import="org.magic7.core.domain.MagicSpaceRegionView"%>
<%@page import="org.magic7.core.domain.MagicDimension"%>
<%@page import="org.magic7.core.domain.MagicSpaceRegion"%>
<%@ page import="org.magic7.core.domain.MagicSpace"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" src="<c:url value='/resources/js/jquery-3.2.1.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/ckeditor/ckeditor.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/ckeditor/adapters/jquery.js'/>"></script>
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
	
	request.setAttribute("sys_default", MagicSpaceRegionView.ViewType.DEFAULT);
	request.setAttribute("customer_define", MagicSpaceRegionView.ViewType.CUSTOMER_DEFINE);
%>
</head>
<body>
	<div style="background-color: #11111;">编辑维度</div>
	<form action="saveView" method="post" id="queryForm">
		<input type="hidden" name="regionName" value="${regionName }">
		<input type="hidden" name="spaceId" value="${spaceId }">
		<input type="hidden" name="spaceName" value="${spaceName}">
		<input type="hidden" name="viewId" value="${view.id}">
		<c:if test="${view.destination ne forData.code }">
		<input type="hidden" name="viewType" value="${view.viewType}">
		
		</c:if>
		<table style="width: 100%" border=1>
			<tr>
				<td>视图名称：<input type="text" size=40 name="name" value="${view.name }"></input></td>
				<td>视图显示名称：<input type="text" size=40 name="displayName" value="${view.displayName }"></input></td>
				<td>
					业务内涵：
					<select name="regionId">
						<c:forEach var="region" items="${regions }">
							<option <c:if test="${view.spaceRegionId eq region.id}">selected</c:if> value ="${region.id }">${region.description }</option>
						</c:forEach>
					</select>
				</td>
				
			</tr>
			<tr>
				<td>视图用途：
					<select name="destination">
						<option <c:if test="${view.destination eq forData.code}">selected</c:if> value ="${forData.code }">存储</option>
						<option <c:if test="${view.destination eq forQuery.code}">selected</c:if> value ="${forQuery.code }">查询</option>
						<option <c:if test="${view.destination eq forButton.code}">selected</c:if> value ="${forButton.code }">按钮</option>
					</select>
				</td>
				<c:if test="${view.destination eq forData.code }">
				<td>视图类型：
					<select name="viewType">
						<option <c:if test="${view.viewType eq sys_default.code}">selected</c:if> value ="${sys_default.code }">系统默认</option>
						<option <c:if test="${view.viewType eq customer_define.code}">selected</c:if> value ="${customer_define.code }">用户自定义</option>
					</select>
				</td>
				<td>
					用户自定义页面名称：<a target="_blank" href="<%=request.getContextPath() %>/uploadFile/${view.customerPageName }">${view.customerPageName }</a>
					<a id='html_editor_href' href="javascript:showdiv()">打开编辑器</a>
					<div id="html_editor_div" style="display: none;">
						<textarea id="html_editor" name="customerPage">${customerPage }</textarea>
					</div>
				</td>
				</c:if>
			</tr>
		</table>
		<div style="text-align: center;">
			<input class="button" type="submit" value="保存" >
			<input class="button" type="button" onClick="back()" value="返回">
		</div>
	</form>
	<form action="uploadCustomerPage.do" method="post" enctype="multipart/form-data">
		<div>上传用户自定义页面：<input type="file" id="file" name="uploadFile"></div>
		<input type="hidden" name="viewId" value="${view.id}">
		<div><input type="submit"  name="提交"></div>
	</form>
</body>
<hr>
<jsp:include page="viewItemList.jsp" flush="true" />
<script>
	$("#html_editor").ckeditor();
	function showdiv(){
		clicktext = document.getElementById('html_editor_href');
      var target=document.getElementById('html_editor_div');
      if (target.style.display=="block"){
          target.style.display="none";
          clicktext.innerHTML="打开编辑器";
      } else {
          target.style.display="block";
          clicktext.innerHTML='关闭编辑器';
      }
	}
</script>
</html>