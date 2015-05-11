<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>   
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>${activity.addr}</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="keywords" content="玩什么,去哪玩,和谁玩,几点玩,不想太远,没有朋友,小区玩,乒乓球,足球,象棋,游泳,瑜伽">
		<meta http-equiv="description" content="玩什么,去哪玩,和谁玩,几点玩,不想太远,没有朋友,小区玩,乒乓球,足球,象棋,游泳,瑜伽">
		<link rel="stylesheet" href="http://libs.useso.com/js/jquery-mobile/1.4.2/jquery.mobile.min.css" />
		<script src="http://libs.useso.com/js/jquery/1.9.1/jquery.min.js"></script>
		<script src="http://libs.useso.com/js/jquery-mobile/1.4.2/jquery.mobile.min.js"></script>
		<script src="js/globle.js"></script>
		<script type="application/javascript" src="js/iscroll.js"></script>  
		<style>
			.controlgroup-textinput{
			    padding-top:.22em;
			    padding-bottom:.22em;
			}
		</style>
		<link rel="stylesheet" href="css/singleactivitymsg.css" />
		<link rel="shortcut icon" href="<%=basePath%>css/images/favicon.ico" />
	</head>
	<script>
		var myScroll;
	    /**
	     * 初始化iScroll控件
	     */
	    function loaded() {
	    	myScroll = new iScroll('wrapper', {
	    		scrollbarClass: 'myScrollbar', /* 重要样式 */
	    		useTransition: false, /* 此属性不知用意，本人从true改为false */
	    		snap:false,checkDOMChange:true,vScrollbar:true,
	    		onBeforeScrollStart : function(e) {
    				var nodeType = e.explicitOriginalTarget ? e.explicitOriginalTarget.nodeName.toLowerCase() : (e.target ? e.target.nodeName.toLowerCase() : '');
    				if(nodeType != 'select' && nodeType != 'option' && nodeType != 'input' && nodeType != 'textarea'){
    					e.preventDefault();
    				}
    			},
    			onRefresh: function () {
	    		}
	    	});
	    	setTimeout(function () { myScroll.refresh(); }, 2000);
	    }
	    //初始化绑定iScroll控件 
	    document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
	    document.addEventListener('DOMContentLoaded', loaded, false); 
    </script>
	<script type="text/javascript">
		var groupLength = 0;
		$(document).ready(function(){ 
			var atype1 = "${activity.atype}";
			$("#title").text(getNameByType(atype1.substring(1)) + "-${atime }");
			initWorker();
			groupLength = parseInt("${fn:length(group)}");
			$("#search-control-group").keydown(function(e){
				if(e.keyCode==13){
					sendMsg();
				}
			});
			$("#wrapper").css({"width":$("#iCenter").css('width')}); 
		}); 
		//声明一个worker;
	    var worker;
	    //初始化调用方法
	    function initWorker(){
	    	if(typeof(EventSource)!=="undefined") {
	    	  	var source=new EventSource("worker/singleactivitymsg?aid=${activity.id}");
	    	  	source.onmessage=function(event) {
	    	  		var result = event.data;
	    	  		var msgchanges = result.substring(4);
	    	  		//处理消息变化
					var msgjson = eval('('+msgchanges+')');
					var flag = false;
					var lastId;
					for(var i = 0; i<msgjson.length; i++) {
						if(document.getElementById(msgjson[i].id)) {
							continue;
						}
						var contentHtml = "";
						flag = true;
				     	if(msgjson[i].type == 0) {
				     		contentHtml += "<div id='chattime'>"+msgjson[i].time+"<input id='"+msgjson[i].id+"' type='hidden'/></div>";
				     	} else {
				     		if("${username}" == msgjson[i].username) {
				     			contentHtml += "<div data-role='controlgroup' style='text-align:right;' data-type='horizontal' data-mini='true'>";
				     			contentHtml += "<a href='#' class='ui-btn ui-btn-b ui-corner-all'>"+msgjson[i].content+"</a>";
				     			contentHtml += "<a href='#' class='ui-btn ui-btn-b ui-corner-all'>"+msgjson[i].username+"</a>";
				     			contentHtml += "<input id='"+msgjson[i].id+"' type='hidden'/>";
				     			contentHtml += "</div>";
				     		} else if("${activity.leader}" == msgjson[i].username && "${username}" != msgjson[i].username) {
				     			contentHtml += "<div data-role='controlgroup' data-type='horizontal' data-mini='true'>";
				     			contentHtml += "<a style='background-color:rgb(216, 213, 218);' href='#' class='ui-btn ui-corner-all'>"+msgjson[i].username+"</a>";
				     			contentHtml += "<a href='#' class='ui-btn ui-corner-all'>"+msgjson[i].content+"</a>";
				     			contentHtml += "<input id='"+msgjson[i].id+"' type='hidden'/>";
				     			contentHtml += "</div>";
				     		} else {
				     			contentHtml += "<div data-role='controlgroup' data-type='horizontal' data-mini='true'>";
				     			contentHtml += "<a href='#' class='ui-btn ui-corner-all'>"+msgjson[i].username+"</a>";
				     			contentHtml += "<a href='#' class='ui-btn ui-corner-all'>"+msgjson[i].content+"</a>";
				     			contentHtml += "<input id='"+msgjson[i].id+"' type='hidden'/>";
				     			contentHtml += "</div>";
				     		}
				     	}
				     	$("#scroller").prepend(contentHtml).trigger('create');
				     	if(i == msgjson.length - 1) {
				     		lastId = msgjson[i].id;
				     	}
				  	}
					if(flag) {
		            	myScroll.refresh();
		            	myScroll.scrollTo(0, -5, 400);
					}
	    	    };
	    	} else {
	    	}
	    }
	    var sendMsg = function() {
	    	var username = "${currentusername}";
	    	var aid = "${activity.id}";
	    	var msg = $("#search-control-group").val();
	    	if(msg.length <= 0) {
	    		toast("说点吧");
	    		return;
	    	}
	    	showLoading("传送中...");
	    	jQuery.ajax({
	            type: "POST",
	            cache: false, //无缓存 Jquery会给每个请求加一个随即数
	            url: "<%=basePath%>singleactivitymsg/sendmsg", //提交的url 请求下级地址
	            dataType : "json",
	            data: 
	            {
	            	username : username,
	            	msg : msg,
	            	aid : aid
        		},
        		success: function(result) {
        			toast("发送完成，正在传话...");
        		},
	            complete: function() {
	            	$("#search-control-group").val('');
	            	hideLoading();
	            }
	        });
	    };
	    jQuery(window).resize(function(){
			$("#wrapper").css({"width":$("#iCenter").css('width')}); 
		});
	</script>
	<body>
		<div data-role="page">
			<div data-role="header" style="overflow:hidden;">
				<h1 id="title">
					${atime }-${activity.addr}			
				</h1>
			    <a onclick="showLoading();location.href='<%=basePath%>singleactivity/view?aid=${activity.id }'" data-icon="arrow-l" class="ui-btn-left">返回</a>
			    <a onclick="showLoading();location.href='<%=basePath%>index/home'" data-icon="home" class="ui-btn-right">主页</a>
			</div><!-- /header -->
			<div data-role="main" class="ui-content">
				<div id="iCenter"></div>
				<div id="wrapper">
					<div id="scroller">
					</div>
				</div>			
		  	</div>
		  	<div data-role="footer" data-position="fixed" style="text-align:right;">
		        <div id="buttoncontrolgroup_1" data-role="controlgroup" data-type="horizontal">
		        	<button>${currentusername }</button>
					<input type="text" id="search-control-group" data-wrapper-class="controlgroup-textinput ui-btn">
			    	<button class="ui-btn-b" onclick="sendMsg();">发送</button>
				</div>
		    </div>
		</div>
	</body>
</html>
