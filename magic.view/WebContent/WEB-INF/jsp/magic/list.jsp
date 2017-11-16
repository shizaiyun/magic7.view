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

<title>magic list</title>
<style type="text/css">
			span.title { 
					width:80px; 
					display: inline-block; 
					font-size:11px;
					color:#333333;
					}
			span.content { 
					width:160px; 
					display: inline-block;
					font-size:11px;
					}
			span.content input{ 
					width:150px; 
					display: inline-block;
					}
			span.content select{ 
					width:150px; 
					display: inline-block; 
					}
			div.item{
				margin-left:50px;
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
}

.s1 {
 background-color: yellow;
}

</style>

<script type="text/javascript">
$(document).ready(function() {
	loadGridTable(1);
  });


function loadGridTable(currentPage){
	var baseData = { 
			space:'${space }', 
			region:'${region }', 
			listView:'${listView }',
			currentPage:currentPage
			};
	var criteriaData = $('#queryForm').serializeObject();
	var postData = {
			base:baseData,
			criteria:criteriaData
	}
	$.ajax({
		url : "${pageContext.request.contextPath}/magic/getList", 
        type : 'post',
        data : JSON.stringify(postData),
        contentType : 'application/json;charset=utf-8',
        dataType : 'json',
        success : function(data) {
            var info = eval(data);
            if(info.code==0){
            	var totalCount = info.data.totalCount;
            	var pageSize = info.data.pageSize;
            	var rows = info.data.data;
            	assembleGridData(rows);
            	if(currentPage == 1){
            		initPagination(totalCount, pageSize);
            	}
            }else if(info.code==1){
            	alert(info.msg);
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
        	console.log("XMLHttpRequest.status:"+XMLHttpRequest.status);
        	console.log("XMLHttpRequest.readyState:"+XMLHttpRequest.readyState);
        	console.log("textStatus:"+textStatus);
      }
    });
}

function initPagination(totalCount, pageSize) {
    $('.M-box').pagination({
        totalData: totalCount,
        showData: pageSize,
        callback: function (index) {
        	 $('.now').text(index);
        	 loadGridTable(index);
        }
    },function(api){
        $('.now').text(api.getCurrent());
    });
}

function getTitles(){
	var titles = new Array();
	$('#gridTable tr:eq(0) th').each(function(){
		titles.push($(this).attr("id"));
	  });
	return titles;
}

function assembleGridData(datas){
	var titles = getTitles();
	$("#gridTable tbody tr").remove();
	  $(datas).each(function(){
		  var row = this;
          var objectId = row.objectId;
          var tr = "<tr id=\""+objectId+"\"><td><input type=\"radio\" name=\"objectId\" value=\""+objectId+"\" /></td>";
          var rowItems = row.rowItems;
	          $(rowItems).each(function(){
		        for (var i = 0; i < titles.length; i++) {
		        	var rowItem = this;
		        	if(rowItem.displayName==titles[i]){
		        		if(rowItem.strValue!=null){
		        			tr = tr+"<td id=\""+rowItem.rowId+"_"+rowItem.displayName+"\">"+rowItem.strValue+"</td>";
		        		}else{
		        			tr = tr+"<td id=\""+rowItem.rowId+"_"+rowItem.displayName+"\"></td>";
		        		}
		        		break;
		        	}
				}
		    });  
          tr = tr +"</tr>";
          $("#gridTable tbody").append(tr);
        });
}

function addItem(){
	var openNewLink = openWin('${pageContext.request.contextPath}'+'/magic/showDetail?space=${space}&objectId=', 'newwindow',1400,600);
	if(window.focus) {
        openNewLink.focus();
      }
}

function modifyItem(){
	var objectId = $("input[name='objectId']:checked").val();
	if(objectId== undefined ){
		alert("请选择需要修改的项");
		return;
	}
	var openNewLink = openWin('${pageContext.request.contextPath}'+'/magic/showDetail?space=${space}&objectId='+objectId, 'newwindow',1400,600);
	if(window.focus) {
        openNewLink.focus();
      }
}

function deleteItem(){
	var objectId = $("input[name='objectId']:checked").val();
	if(objectId== undefined ){
		alert("请选择需要删除的项");
		return;
	}
	var baseData = { 
			objectId:objectId
			};
	$.ajax({
		url : "${pageContext.request.contextPath}/magic/deleteObject", 
        type : 'post',
        data : JSON.stringify(baseData),
        contentType : 'application/json;charset=utf-8',
        dataType : 'json',
        success : function(data) {
        	var datas = eval(data);
        	alert(datas.message);
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
<div style="background-color: #555555;">案件清单</div>
<form action="" method="post" id="queryForm">
	<m:magicView space="${space }" region="${region }" view="${queryView }" destination="1"></m:magicView>
	<div style="text-align: center;">
		<input class="button" type="button" value="查询" onclick="loadGridTable(1)">
		<input class="button" type="reset" value="重置">
	</div>
</form>
<hr>
<div style="text-align: right;padding-right: 10px;padding-bottom:5px">
	<input class="button" type="button" value="新增" onclick="addItem()">
	<input class="button" type="button" value="修改" onclick="modifyItem()">
	<input class="button" type="button" value="删除" onclick="deleteItem()">
</div>
<table class="gridTable" style="width: 100%" id="gridTable">
	<m:magicListView space="${space }" region="${region }" view="${listView }" destination="0"></m:magicListView>
	<tbody></tbody>
</table>
<div class="M-box" align="center"></div>

</body>

</html>