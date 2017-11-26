<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="m" uri="/magicTag.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="<c:url value='/resources/js/jquery-3.2.1.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/jquery.validate.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/validate.messages_cn.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/magic7.util.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/jquery.pagination.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/My97DatePicker/WdatePicker.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/style/common.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/style/normalize.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/style/pagination.css'/>">

<title>tabSingle</title>
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
			span.content input{ 
					width:150px; 
					display: inline-block;
					}
			span.content select{ 
					width:150px; 
					display: inline-block; 
					}
			div.item{
				margin-left:30px;
				margin-right:20px;
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
 
 .Wdate_readonly{
 	background: #F7F7F7 url(../resources/js/My97DatePicker/skin/datePicker.gif) no-repeat right;;
 }
</style>

<script type="text/javascript">
$(document).ready(function() {
	$('input[required]').after('<span style="color:red">*</span>');
	$('select[required]').after('<span style="color:red">*</span>');
	$('textarea[required]').after('<span style="color:red">*</span>');
  });

function openWin(url){
	window.open('${pageContext.request.contextPath}/'+url, 'newwindow', 'height=100, width=400, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no');
}

function changeTab(tab) {
	var tabs = document.getElementsByClassName('tab-head')[0].getElementsByTagName('h2');
	var contents = document.getElementsByClassName('tab-content')[0].getElementsByTagName('div');
        for(var i = 0, len = tabs.length; i < len; i++) {
            if(tabs[i] === tab) {
                tabs[i].className = 'selected';
                contents[i].className = 'show';
            } else {
                tabs[i].className = '';
                contents[i].className = '';
            }
        }
    }
function validForm(){
	var valid = true;
	$('#${region }_Form .content').each(function(i){ 
		var contentObj =  $(this);
		var inputObj = contentObj[0].children[0];
		var required =  inputObj.required;
		var value =  inputObj.value;
		var title = contentObj[0].previousSibling.textContent
		if(required&&value.length==0){
			alert(title+"是必填项");
			$(inputObj).focus();
			valid = false;
			return false;
		}
	});
	return valid;
}   
function saveItem(){
	/* if($("#${region }_Form").valid()) {
	} */
	if(validForm()){
		var rowData = $('#${region }_Form').serializeObject();
		$.ajax({
			url : "${pageContext.request.contextPath}/magic/saveRow", 
	        type : 'post',
	        data : JSON.stringify(rowData),
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
	        }
	    });
	}
	
}

function submitItem(){
	if(validForm()){
		var rowData = $('#${region }_Form').serializeObject();
		$.ajax({
			url : "${pageContext.request.contextPath}/magic/submitRow", 
	        type : 'post',
	        data : JSON.stringify(rowData),
	        contentType : 'application/json;charset=utf-8',
	        dataType : 'json',
	        success : function(data) {
	        	var info = eval(data);
	        	if(info.code==0){
	        		alert("提交成功");
	        		window.location.href = window.location.href;
	            }else if(info.code==1){
	            	alert(info.msg);
	            }
	        }
	    });
	}
	
}

function deleteItem(){
	var baseData = { 
			objectId:'${objectId }'
			};
	$.ajax({
		url : "${pageContext.request.contextPath}/magic/deleteObject", 
        type : 'post',
        data : JSON.stringify(baseData),
        contentType : 'application/json;charset=utf-8',
        dataType : 'json',
        success : function(data) {
        	var info = eval(data);
        	if(info.code==0){
        		alert("删除成功");
        		closeDialog();
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


function closeDialog(){
	window.parent.close();
	window.parent.opener.location.href = window.parent.opener.location.href;
}

</script>
</head>
<body>
<form action="" method="post" id="${region }_Form">
	<m:magicRegion space="${space }" region="${region }" objectId="${objectId }"></m:magicRegion>
</form>
<hr>
</html>