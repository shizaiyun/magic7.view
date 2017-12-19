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
<title>编辑业务内涵</title>
<%
	request.setAttribute("main", MagicSpaceRegion.RegionType.MAIN);
	request.setAttribute("tab", MagicSpaceRegion.RegionType.TAB);
%>
</head>
<body>
<div style="background-color: #11111;">编辑业务内涵</div>
<form action="saveRegion" method="get" id="queryForm">
	<input type="hidden" name="regionId" value="${region.id }">
	<input type="hidden" name="spaceId" value="${spaceId }">
	<input type="hidden" name="spaceName" value="${spaceName}">
	<input type="hidden" id="codeIds" name="codeIds" value="${spaceName}">
	
	<table style="width: 100%" border=1>
		<tr>
			<td>业务内涵名称：<input type="text" name="name" value="${region.name }"></input></td>
			<td>业务内涵说明：<input type="text" name="description" value="${region.description }"></input></td>
			<td>业务内涵分隔符：<input type="text" name="partition" value="${region.partition }"></input></td>
		</tr>
		<tr>
			<td>显示顺序：<input type="text" name="seq" value="${region.seq }"></input></td>
			<td>是否多行数据：
				<select name="multiply">
					<option <c:if test="${region.multiply eq true}">selected</c:if> value ="true">是</option>
					<option <c:if test="${region.multiply eq false}">selected</c:if> value ="false">否</option>
				</select>
			</td>
			<td>页面类型：
				<select name="regionType">
					<option <c:if test="${region.regionType eq main.code}">selected</c:if> value ="${main.code}">主页面</option>
					<option <c:if test="${region.regionType eq tab.code}">selected</c:if> value ="${tab.code }">TAB页面</option>
				</select>
			</td>
		</tr>
		<tr>
			<td colspan="3" valign="middle">
				<div style="float: left;width: 30%">
				<div style="float:inherit ;"><div>可关联的JAVA代码:</div>
				<select style="width:200px;" ondblclick="changeJavaCodeSelect(this.selectedIndex)" id="javaCodeSelect" name="choiceCode" multiple="multiple" size="10">
				</select>
				</div>
				<div style="float: right;padding-right: 5%;"><div>已关联的JAVA代码:</div>
				<select style="width:200px;" ondblclick="changeJavaCodeLnkSelect(this.selectedIndex)" id="javaCodeLnkSelect" name="choiceCode" multiple="multiple" size="10">
				</select>
				</div>
				</div>
			</td>
		</tr>
	</table>
	<div style="text-align: center;">
		<input class="button" type="submit" value="保存" >
		<input class="button" type="button" onClick="back()" value="返回">
	</div>
</form>
<c:if test="${not empty region  }">
<hr>
<jsp:include page="dimensionList.jsp" flush="true" />
<hr>
<jsp:include page="dimensionListForQuery.jsp" flush="true" />
<hr>
<jsp:include page="dimensionListForButton.jsp" flush="true" />
<hr>
<jsp:include page="viewList.jsp" flush="true" />
</c:if>
</body>
<script>
var javaCodeWithLnks = {};
<c:forEach var="javaCodeWithLnk" items="${javaCodesWithLnk }">
	javaCodeWithLnks[${javaCodeWithLnk.id}]="${javaCodeWithLnk.name}"+"|"+"${javaCodeWithLnk.description}";
</c:forEach>
var selectedCodeId = "";

var javaCode = {};
<c:forEach var="javaCode" items="${javaCodes }">
	if(javaCodeWithLnks[${javaCode.id}]==null)
	javaCode[${javaCode.id}]="${javaCode.name}"+"|"+"${javaCode.description}";
</c:forEach>
for (i = 0 ;i <Object.getOwnPropertyNames(javaCode).length;i++) {  
	var tmpcityArray = javaCode[Object.getOwnPropertyNames(javaCode)[i]].split("|")  
	document.all.javaCodeSelect.options[document.all.javaCodeSelect.length] = new Option(tmpcityArray[0],Object.getOwnPropertyNames(javaCode)[i]);
	document.all.javaCodeSelect.options[document.all.javaCodeSelect.length-1].title=tmpcityArray[1]
} 
for (i = 0 ;i <Object.getOwnPropertyNames(javaCodeWithLnks).length;i++) {  
	var tmpcityArray = javaCodeWithLnks[Object.getOwnPropertyNames(javaCodeWithLnks)[i]].split("|")  
	document.all.javaCodeLnkSelect.options[document.all.javaCodeLnkSelect.length] = new Option(tmpcityArray[0],Object.getOwnPropertyNames(javaCodeWithLnks)[i]);  
	document.all.javaCodeLnkSelect.options[document.all.javaCodeLnkSelect.length-1].title=tmpcityArray[1]
	selectedCodeId += Object.getOwnPropertyNames(javaCodeWithLnks)[i]+",";
	codeIds.value=selectedCodeId;
}

function changeJavaCodeSelect(index) {
	document.all.javaCodeLnkSelect.options[document.all.javaCodeLnkSelect.length] = new Option(javaCodeSelect.options[index].text,javaCodeSelect.options[index].value);  
	document.all.javaCodeLnkSelect.options[document.all.javaCodeLnkSelect.length-1].title=javaCodeSelect.options[index].title;
	javaCodeSelect.options.remove(index) 
	for (i = 0 ;i <javaCodeLnkSelect.length;i++) { 
		selectedCodeId+=javaCodeLnkSelect.options[i].value+",";
	}
	codeIds.value=selectedCodeId;
}
function changeJavaCodeLnkSelect(index) {
	javaCodeSelect.options[javaCodeSelect.length] = new Option(javaCodeLnkSelect.options[index].text,javaCodeLnkSelect.options[index].value);  
	javaCodeSelect.options[javaCodeSelect.length-1].title=javaCodeLnkSelect.options[index].title;
	javaCodeLnkSelect.options.remove(index);
	selectedCodeId = "";
	for (i = 0 ;i <javaCodeLnkSelect.length;i++) { 
		selectedCodeId+=javaCodeLnkSelect.options[i].value+",";
	}
	codeIds.value=selectedCodeId;
}
</script>
</html>