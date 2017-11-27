<%@page import="org.magic7.core.domain.MagicDimension"%>
<%@page import="org.magic7.core.domain.MagicSpaceRegion"%>
<%@ page import="org.magic7.core.domain.MagicSpace"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" src="<c:url value='/resources/js/jquery-3.2.1.min.js'/>"></script>
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
<title>视图项</title>
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
<div style="background-color: #11111;">编辑视图项</div>
<form action="saveViewItem" method="get" id="queryForm">
	<input type="hidden" name="regionId" value="${regionId }">
	<input type="hidden" name="regionName" value="${regionName }">
	<input type="hidden" name="spaceId" value="${spaceId }">
	<input type="hidden" name="spaceName" value="${spaceName}">
	<input type="hidden" name="viewId" value="${viewId}">
	<input type="hidden" name="viewName" value="${viewName}">
	<input type="hidden" name="itemId" value="${item.id}">
	<table style="width: 100%" border=1>
		<tr>
			<td width="33%">视图项名称：<input type="text" name="name" value="${item.name }"></input></td>
			<td width="33%">视图项对应维度：
				<select name="dimensionId">
					<c:forEach var="dimension" items="${dimensions }">
						<option <c:if test="${item.dimensionId eq dimension.id }">selected</c:if> value="${dimension.id }">${dimension.description }</option>
					</c:forEach>
				</select>
			</td>
			<td>分区顺序：<input type="text" name="seq" value="${item.seq }"></input></td>
		</tr>
		<tr>
			<td>是否必填：
				<select name="required">
					<option <c:if test="${item.required eq true}">selected</c:if> value ="true">是</option>
					<option <c:if test="${item.required eq false}">selected</c:if> value ="false">否</option>
				</select>
			</td>
			<td>是否可编辑：
				<select name="editable">
					<option <c:if test="${item.editable eq true}">selected</c:if> value ="true">是</option>
					<option <c:if test="${item.editable eq false}">selected</c:if> value ="false">否</option>
				</select>
			</td>
			<td>是否可见：
				<select name="visible">
					<option <c:if test="${item.visible eq true}">selected</c:if> value ="true">是</option>
					<option <c:if test="${item.visible eq false}">selected</c:if> value ="false">否</option>
				</select>
			</td>
		</tr>
		<c:if test="${destination eq forButton.code}">
		<tr>
			<td colspan="3">编辑触发器：
				<table border=1 width="100%">
					<tr>
						<td width="30%">触发器名称</td>
						<td width="50%">触发器执行序列</td>
					</tr>
					<tr>
						<td>
							<input type="text" name="businessTrigger" value="${item.businessTrigger}"></input>
						</td>
						<td>
							<table border=1 id="assemblers"  width="100%">
								<tr>
									<td width="25%" align="center">
										字段名称
									</td>
									<td width="25%" align="center">
										绑定行为
									</td>
									<td width="25%" align="center">
										执行顺序
									</td>
									<td width="25%" align="center">
										操作
									</td>
								</tr>
								<tr>
									<script>
										var dataDimensions = {};
										var javaCodeWithLnks = {};
										var targetDimensionHolder = null;
										var javaCodeLnkSelectHolder = null;
										
									</script>
									<c:forEach var="assembler" items="${assemblers }">
										<form action="saveAssembler${assembler.id }" method="get" id="saveAssembler${assembler.id }">
											<input type="hidden" value="save" name="command"/>
											<td>
												<select style="width:200px;" id="targetDimension${assembler.id }" name="targetDimension${assembler.id }" ></select>
											</td>
											<td>
												<select style="width:200px;" id="javaCodeLnkSelect${assembler.id }" name="choiceCode${assembler.id }" ></select>
											</td>
											<td>
												<input type="text" name="seq" ></input>
											</td>
											<td>
												<input type="button" name="save" value="保存" onclick="saveAssembler(saveAssembler${assembler.id })"></input>
												<input type="button" name="delete" value="删除" onclick="deleteAssembler(saveAssembler${assembler.id })"></input>
											</td>
											<script>
												dataDimensions = {};
												<c:forEach var="dataDimension" items="${dataDimensions }">
													dataDimensions[${dataDimension.id}]="${dataDimension.name}"+"|"+"${dataDimension.description}";
												</c:forEach>
												targetDimensionHolder = $("#targetDimension${assembler.id }");
												targetDimensionHolder.options[targetDimensionHolder.length] = new Option("","");
												for (i = 0 ;i <Object.getOwnPropertyNames(dataDimensions).length;i++) {  
													var tmpcityArray = dataDimensions[Object.getOwnPropertyNames(dataDimensions)[i]].split("|")  
													targetDimensionHolder.options[targetDimensionHolder.length] = new Option(tmpcityArray[1],Object.getOwnPropertyNames(dataDimensions)[i]);
													targetDimensionHolder.options[targetDimensionHolder.length-1].title=tmpcityArray[0]
												} 
												
												javaCodeWithLnks = {};
												javaCodeLnkSelectHolder = $("#javaCodeLnkSelect${assembler.id }");
												<c:forEach var="javaCodeWithLnk" items="${javaCodesWithLnk }">
													javaCodeWithLnks[${javaCodeWithLnk.id}]="${javaCodeWithLnk.name}"+"|"+"${javaCodeWithLnk.description}";
												</c:forEach>
												javaCodeLnkSelectHolder.options[javaCodeLnkSelectHolder.length] = new Option("","");
												for (i = 0 ;i <Object.getOwnPropertyNames(javaCodeWithLnks).length;i++) {  
													var tmpcityArray = javaCodeWithLnks[Object.getOwnPropertyNames(javaCodeWithLnks)[i]].split("|")  
													javaCodeLnkSelectHolder.options[javaCodeLnkSelectHolder.length] = new Option(tmpcityArray[0],Object.getOwnPropertyNames(javaCodeWithLnks)[i]);  
													javaCodeLnkSelectHolder.options[javaCodeLnkSelectHolder.length-1].title=tmpcityArray[1]
												}
												var clone = $("#assemblers tr:gt(0)").clone();
												$("#assemblers").append();
											</script>
										</form>
									</c:forEach>
									<form action="saveAssembler" method="get" id="saveAssembler">
										<input type="hidden" value="save" name="command"/>
										<td>
											<select style="width:200px;" id="targetDimension" name="targetDimension" ></select>
										</td>
										<td>
											<select style="width:200px;" id="javaCodeLnkSelect" name="choiceCode" ></select>
										</td>
										<td>
											<input type="text" name="seq" ></input>
										</td>
										<td>
											<input type="button" name="add" value="新增" onclick="addLine()"></input>
											<input type="button" name="save" value="保存" onclick="saveAssembler(saveAssembler)"></input>
											<input type="button" name="delete" value="删除" onclick="deleteAssembler(saveAssembler)"></input>
										</td>
									</form>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		</c:if>
	</table>
	<div style="text-align: center;">
		<input class="button" type="submit" value="保存" >
		<input class="button" type="button" onClick="back()" value="返回">
	</div>
</form>
<hr>
</body>
<script>

var dataDimensions = {};
<c:forEach var="dataDimension" items="${dataDimensions }">
	dataDimensions[${dataDimension.id}]="${dataDimension.name}"+"|"+"${dataDimension.description}";
</c:forEach>
document.all.targetDimension.options[document.all.targetDimension.length] = new Option("","");
for (i = 0 ;i <Object.getOwnPropertyNames(dataDimensions).length;i++) {  
	var tmpcityArray = dataDimensions[Object.getOwnPropertyNames(dataDimensions)[i]].split("|")  
	document.all.targetDimension.options[document.all.targetDimension.length] = new Option(tmpcityArray[1],Object.getOwnPropertyNames(dataDimensions)[i]);
	document.all.targetDimension.options[document.all.targetDimension.length-1].title=tmpcityArray[0]
} 

var javaCodeWithLnks = {};
<c:forEach var="javaCodeWithLnk" items="${javaCodesWithLnk }">
	javaCodeWithLnks[${javaCodeWithLnk.id}]="${javaCodeWithLnk.name}"+"|"+"${javaCodeWithLnk.description}";
</c:forEach>
document.all.javaCodeLnkSelect.options[document.all.javaCodeLnkSelect.length] = new Option("","");
for (i = 0 ;i <Object.getOwnPropertyNames(javaCodeWithLnks).length;i++) {  
	var tmpcityArray = javaCodeWithLnks[Object.getOwnPropertyNames(javaCodeWithLnks)[i]].split("|")  
	document.all.javaCodeLnkSelect.options[document.all.javaCodeLnkSelect.length] = new Option(tmpcityArray[0],Object.getOwnPropertyNames(javaCodeWithLnks)[i]);  
	document.all.javaCodeLnkSelect.options[document.all.javaCodeLnkSelect.length-1].title=tmpcityArray[1]
}
var clone = $("#assemblers tr:last").clone();
function addLine() {
	$("#assemblers tbody:last").append(clone);
}

function saveAssembler(formId) {
	
}
</script>
</html>