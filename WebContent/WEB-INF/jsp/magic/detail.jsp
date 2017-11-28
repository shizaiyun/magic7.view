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

<title>space detail</title>
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


.mainArea_title {
 	background-color: #555555;
}  

	h2 {
                border: solid cornflowerblue 1px;
                height: 28px;
                margin: 0;
                float: left;
                background-color: #E4F2FD;
			    color: #949694;
			    text-align: center;
			    border-right: 1px solid #95C9E1;
			    line-height: 28px;
			    font-weight: bold;
			    font-size: 12px;
            }
            
     .tab-content {
         border: solid cornflowerblue 1px;
         height: 100px;
     }
     
     .tab-content div{
         display: none;
     }
     
     .selected {
         background:#fff;
     }
     
     .tab-content .show{
          display: block; 
     }
 /* 竖TAB样式 */
#lib_Tab{margin:0px;padding:0px;margin-bottom:15px; overflow:hidden;}
.lib_tab_class{border:1px solid #95C9E1;}
.lib_Menu {line-height:28px;position:relative; float:left; width:130px; height:240px;border-right:1px solid #95C9E1;}
.lib_Menu ul{margin:0px;padding:0px;list-style:none; position:absolute; top:15px; left:5px; margin-left:10px; height:25px;text-align:center;}
.lib_Menu li{display:block;cursor:pointer;width:114px;color:#949694;font-weight:bold; margin-bottom:2px;height:25px;line-height:25px; background-color:#E4F2FD}
.lib_Menu li.hover{padding:0px;background:#fff;width:116px;border:1px solid #95C9E1; border-right:0;color:#739242;height:25px;line-height:25px;}
.lib_Content{margin-top:0px; border-top:none;padding:20px; border-left:0; margin-left:130px;}
         
</style>

<script type="text/javascript">
$(document).ready(function() {
	
  });

function openWin(url){
	window.open('${pageContext.request.contextPath}/'+url, 'newwindow', 'height=100, width=400, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no');
}


function changeTab(tab,space,region,view,buttonView,objectId) {
	var tabs = document.getElementsByClassName('tab-head')[0].getElementsByTagName('h2');
	var contents = document.getElementsByClassName('tab-content')[0].getElementsByTagName('div');
        for(var i = 0, len = tabs.length; i < len; i++) {
            if(tabs[i] === tab) {
                tabs[i].className = 'selected';
                contents[i].className = 'show';
                $("#tabContent_"+region).attr("src", "${pageContext.request.contextPath}/magic/showTabDetail?space="+space+"&region="+region+"&view="+view+"&buttonView="+buttonView+"&objectId="+objectId);
            } else {
                tabs[i].className = '';
                contents[i].className = '';
            }
        }
    }

function changeVerticalTab(tab,space,region,objectId) {
	var tabs = document.getElementById('lib_Tab').getElementsByTagName('li');
	var contents = document.getElementsByClassName('lib_Content')[0].getElementsByTagName('div');
        for(var i = 0, len = tabs.length; i < len; i++) {
            if(tabs[i] === tab) {
                $("#tabContent_"+region).attr("src", "${pageContext.request.contextPath}/magic/showTabDetail?space="+space+"&region="+region+"&objectId="+objectId);
                tabs[i].className = 'hover';
                contents[i].style.display = 'block';
            } else {
                tabs[i].className = '';
                contents[i].style.display = 'none';
            }
        }
    }

</script>
</head>
<body>
<m:magicDetail space="${space }" objectId="${objectId }" mainlistView="${mainlistView}" mainViewAndMainButtonView="${mainViewAndMainButtonView }" regionViewAndRegionButtonView="${regionViewAndRegionButtonView }"></m:magicDetail>
<hr>

</html>