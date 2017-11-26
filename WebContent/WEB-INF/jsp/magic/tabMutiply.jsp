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

<title>tabMutiply</title>
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
    margin-right:10px;
    box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2), 0 6px 20px 0 rgba(0,0,0,0.19);
}

	h2 {
                border: solid cornflowerblue 1px;
                height: 25px;
                margin: 0;
                float: left;
                text-align: center;
                font-size: 10px;
            }
            
            .tab-content {
                border: solid cornflowerblue 1px;
                height: 100px;
            }
            
            .tab-content div{
                display: none;
            }
            
            .selected {
                background-color: cornflowerblue;
            }
            
            .tab-content .show{
                display: block;
            }
  
   .hiddenTr_displayNone{
 	display: none;
 }
 
  .hiddenTr_visibility{
 	visibility:hidden;
 }

 .toolbar{
 	background-color: #E7E7E7;
 	height: 30px;
 }       
 .toobar_title{
 	font-size: 12px;
    color: #333333;
    height: 30px;
    line-height: 30px;
 	padding-left: 10px;
 }    
 .toobar_button{
 	padding-right: 10px;
 	position: absolute;
    right: 5px;
    vertical-align: middle;
    margin-top: 2px;
 }         
</style>

<script type="text/javascript">
$(document).ready(function() {
	changeHiddenTrClass();
  });

function changeHiddenTrClass(){
	var length = $("#gridTable tr").length;
	if(length==2){
		$("#hiddenTr").removeClass("hiddenTr_displayNone");
		$("#hiddenTr").addClass("hiddenTr_visibility");
	}else{
		$("#hiddenTr").removeClass("hiddenTr_visibility");
		$("#hiddenTr").addClass("hiddenTr_displayNone");
	}
}

function addRow(){
	var baseData = { 
			space:'${space }', 
			region:'${region }', 
			objectId:'${objectId }'
			};
	
	$.ajax({
		url : "${pageContext.request.contextPath}/magic/addRow", 
        type : 'post',
        data : JSON.stringify(baseData),
        contentType : 'application/json;charset=utf-8',
        dataType : 'json',
        success : function(data) {
        	
        	var info = eval(data);
        	if(info.code==0){
        		var rowId = info.data;
        		var newContent = "";
            	$("#hiddenTr td:gt(0)").each(function(){ 
            		var td = $(this);
            		newContent = newContent + td[0].outerHTML;
            	}); 
            	$("#gridTable").append("<tr><td style=\"width: 10px;text-align: center;\"><input type=\"checkbox\" name=\"rowId\" value=\""+rowId+"\">"+newContent+"</td></tr>");
            	changeHiddenTrClass();
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

function deleteRow(){
	var rowIds = new Array();
	var rows = new Array();
	$("#gridTable tr:gt(1)").each(function(i){  
	    $(this).find("td input[name='rowId']").each(function(){  
	    	var content = $(this);
	    	if(content.is(':checked')){
	    		rowIds.push(content.val());
	    		rows.push(content);
	    	}
	    });  
	});
	
	if(rowIds.length==0){
		alert("请选择需要删除的项");
	}
	
	var postData = {
			rowIds:rowIds
	}
	
	$.ajax({
		url : "${pageContext.request.contextPath}/magic/deleteRows", 
        type : 'post',
        data : JSON.stringify(postData),
        contentType : 'application/json;charset=utf-8',
        dataType : 'json',
        success : function(data) {
        	 var info = eval(data);
             if(info.code==0){
            	 alert("删除成功");
            	 $(rows).each(function(i){
             		$(this).parent().parent().remove();
             	});
            	changeHiddenTrClass();
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

function getTitles(){
	var map = {};
	$('#gridTable tr:eq(0) th').each(function(){
		var name = $(this).attr("name");
		if(name != undefined){
			var textContent = $(this)[0].textContent;
			if(textContent.indexOf('*')==0){
				textContent = textContent.substring(1); 
			}
			map[name] = textContent;
		}
	  });
	return map;
}

function saveRows(){
	//验证
	var titlesMap = getTitles();
	var valid = true;
	$("#gridTable tr:gt(1)").each(function(i){
		$(this).find("td").each(function(){  
	    	var content = $(this).children();
	    	var name = content.attr("name");
	        var value= content.val();  
	        var required = content[0].required;
	        var title = titlesMap[name];
	        if(required&&value.length==0){
				alert(title+"是必填项");
				$(content).focus();
				valid = false;
				return false;
			}
	    });  
	});  
	
	if(valid){
		var rowDatas = new Array();
		$("#gridTable tr:gt(1)").each(function(i){  
		    var data = new Object();  
		    $(this).find("td").each(function(){  
		    	var content = $(this).children();
		        var name = content.attr("name");  
		        data[name]= content.val();  
		    });  
		    rowDatas[i]=data;  
		});  
		
		var postData = {
				rowDatas:rowDatas
		}
		$.ajax({
			url : "${pageContext.request.contextPath}/magic/saveRows", 
	        type : 'post',
	        data : JSON.stringify(postData),
	        contentType : 'application/json;charset=utf-8',
	        dataType : 'json',
	        success : function(data) {
	        	 var info = eval(data);
	             if(info.code==0){
	            	 alert("保存成功");
	            	 window.location.href = window.location.href;
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
}

function checkAll() {
	if($("#checkAll").is(':checked')){
		$("input[name='rowId']").each(function(i){
			if(!$(this).is(':checked')){
				$(this).attr("checked","true");
				$(this)[0].checked = true;
			}
		});
	}else{
		$("input[name='rowId']").each(function(i){
			if($(this).is(':checked')){
				$(this).removeAttr("checked");
				$(this)[0].checked = false;
			}
		});
	}
}
</script>
</head>
<body>
	<m:magicRegion space="${space }" region="${region }" objectId="${objectId }"></m:magicRegion>
</html>