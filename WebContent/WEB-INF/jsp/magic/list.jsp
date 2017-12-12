<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" uri="/magicTag.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="<c:url value='/resources/js/jquery-3.2.1.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/magic7.util.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/jquery.pagination.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/My97DatePicker/WdatePicker.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/style/common.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/style/normalize.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/style/pagination.css'/>">

<script type="text/javascript" src="<c:url value='/resources/js/bootstrap-3.3.2.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/bootstrap-multiselect.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/style/bootstrap-3.3.2.min.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/style/bootstrap-multiselect.css'/>">
<title>magic list</title>
<style type="text/css">
			span.title { 
					width:120px; 
					display: inline-block; 
					font-size:11px;
					color:#333333;
					}
			span.content { 
					width:160px; 
					display: inline-block;
					font-size:11px;
					}
			span.content + input{ 
					width:150px; 
					display: inline-block;
					}
			span.content select{ 
					width:150px; 
					display: inline-block; 
					}
			div.item{
				margin-left:20px;
				margin-right:50px;
				margin-top:2px;
				margin-bottom:3px;
				display:inline-block; 
			}
			
		table.gridTable {
			font-family: verdana,arial,sans-serif;
			font-size:11px;
			color:#333333;
			border-width: 1px;
			border-color: #666666;
			border-collapse: collapse;
			width: 98%;
		}
		table.gridTable th {
			border-width: 1px;
			padding: 8px;
			border-style: solid;
			border-color: #666666;
			background-color: #dedede;
		}
		table.gridTable td {
			border-width: 1px;
			padding: 8px;
			border-style: solid;
			border-color: #666666;
			background-color: #ffffff;
		}
		
.button {
    background-color: #555555;
    border: none;
    color: white;
    padding: 5px 10px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 12px;
    border-radius: 4px;
    box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2), 0 6px 20px 0 rgba(0,0,0,0.19);
    margin-right: 5px;
}

.s1 {
 background-color: yellow;
}
.queryArea_title {
 background-color: #55555;
}
</style>
<script type="text/javascript">
$(document).ready(function() {
	var currentPage = '${currentPage}';
	var totalCount = '${totalCount}';
	var pageSize = '${pageSize}';
	var currentpage = '${currentPage}';
	initPagination(totalCount, pageSize,currentpage);
		 
	 $("#queryContent select").each(function(){
		 if($(this).attr("multiple")=="multiple"){
			 $(this).multiselect({
			 });
		 };
		});
  });


function loadGridTable(currentPage){
	document.getElementById("currentPage").value=currentPage;
	document.getElementById("queryForm").submit();
}

function initPagination(totalCount, pageSize,currentPage) {
    $('.M-box').pagination({
        totalData: totalCount,
        showData: pageSize,
        current:currentPage,
        pageCount:2,
        callback: function (index) {
        	 $('.now').text(index);
        	 loadGridTable(index);
        }
    },function(api){
        $('.now').text(api.getCurrent());
    });
    $('.now').text(currentPage);
}

function resetForm(){
	$("#queryContent input").each(function(){
		$(this).val("");
	});
	$("#queryContent select option:selected").each(function(){
		$(this).attr("selected",false);
	});
	
	$("#queryContent select").each(function(){
		 if($(this).attr("multiple")=="multiple"){
			 $(this).multiselect("destroy");
			 $(this).multiselect().val([]).multiselect("refresh")
		 };
		});
}

function checkAll() {
	if($("#checkAll").is(':checked')){
		$("input[name='objectId']").each(function(i){
			if(!$(this).is(':checked')){
				$(this).attr("checked","true");
				$(this)[0].checked = true;
			}
		});
	}else{
		$("input[name='objectId']").each(function(i){
			if($(this).is(':checked')){
				$(this).removeAttr("checked");
				$(this)[0].checked = false;
			}
		});
	}
}

function addItem(){
	var openNewLink = openWin('${pageContext.request.contextPath}'+'/magic/showDetail?objectId=&${queryString}', 'newwindow',1400,600);
	if(window.focus) {
        openNewLink.focus();
      }
}

function modifyItem(objectId){
	var openNewLink = openWin('${pageContext.request.contextPath}'+'/magic/showDetail?objectId='+objectId+"&${queryString}", 'newwindow',1400,600);
	if(window.focus) {
        openNewLink.focus();
      }
}

function deleteItem(objectId){
	var objectIds = new Array();
	$("#gridTable tr:gt(1)").each(function(i){  
	    $(this).find("td input[name='objectId']").each(function(){  
	    	var content = $(this);
	    	if(content.is(':checked')){
	    		objectIds.push(content.val());
	    	}
	    });  
	});
	
	if(objectIds.length==0){
		alert("请选择需要删除的项");
		return;
	}
	
	var postData = {
			objectIds:objectIds
	}
	
	$.ajax({
		url : "${pageContext.request.contextPath}/magic/deleteObjects", 
        type : 'post',
        data : JSON.stringify(postData),
        contentType : 'application/json;charset=utf-8',
        dataType : 'json',
        success : function(data) {
        	var datas = eval(data);
        	alert("删除成功");
        	loadGridTable(1);
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
        	console.log("XMLHttpRequest.status:"+XMLHttpRequest.status);
        	console.log("XMLHttpRequest.readyState:"+XMLHttpRequest.readyState);
        	console.log("textStatus:"+textStatus);
        }
    });
}

</script>
</head>
<body>
	<form action="${pageContext.request.contextPath}/magic/showList?${queryString}" method="post" id="queryForm">
		<input type="hidden" name="currentPage" id="currentPage" value="${currentPage}" />
			<m:magicQueryView space="${space }"  view="${queryView }"  parmMap="${parmMap }"></m:magicQueryView>
	</form>
	<hr>
<c:if test="${mainlistView != null }">
		<m:magicListView space="${space }" view="${mainlistView }" rows="${rows }" destination="0"></m:magicListView>
</c:if>
<c:if test="${mainlistView == null }">
	请配置mainlistView!!!
</c:if>
</body>

</html>