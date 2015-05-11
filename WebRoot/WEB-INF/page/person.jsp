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
		<title>个人中心</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="keywords" content="玩什么,去哪玩,和谁玩,几点玩,不想太远,没有朋友,小区玩,乒乓球,足球,象棋,游泳,瑜伽">
		<meta http-equiv="description" content="玩什么,去哪玩,和谁玩,几点玩,不想太远,没有朋友,小区玩,乒乓球,足球,象棋,游泳,瑜伽">
		<link rel="stylesheet" href="http://libs.useso.com/js/jquery-mobile/1.4.2/jquery.mobile.min.css" />
		<link rel="stylesheet" href="css/global.css" />
		<script src="http://libs.useso.com/js/jquery/1.9.1/jquery.min.js"></script>
		<script src="js/jquery.animate-colors-min.js"></script>
		<script src="http://libs.useso.com/js/jquery-mobile/1.4.2/jquery.mobile.min.js"></script>
		<script src="js/globle.js"></script>
		<script type="text/javascript" src="http://qzonestyle.gtimg.cn/qzone/openapi/qc_loader.js" 
			data-appid="101115797" data-redirecturi="http://iwantplay.ngrok.com/iwantplay/index/qcback" charset="utf-8"></script>
		<style>
			/* map style */
			#iCenter{
				width:100%;
				border:1px solid #BEBEBE;
			}
		</style>
		<link rel="shortcut icon" href="<%=basePath%>css/images/favicon.ico" />
	</head>
	<script type="text/javascript">
		$(document).ready(function(){ 
			$("#title").text("hi," + localStorage.getItem("username"));
		}); 
		var exit = function() {
			showLoading("拜拜....");
			jQuery.ajax({
	            type: "POST",
	            cache: false, //无缓存 Jquery会给每个请求加一个随即数
	            url: "<%=basePath%>admin/logout", //提交的url 请求下级地址
	            dataType : "json",
	            data: 
	            {
        		},//提交的参数
	            success: function (result) {    //请求成功的方法
	            	var status = result.model.status;//附近的活动
	            	if(status == '0') {
	            		delCookie("SESSION_LOGIN_USERNAME");
	            		delCookie("SESSION_LOGIN_PASSWORD");
            			location.href="<%=basePath%>index/home";
	            	} else {
	            		toast("我们不想让你走");
	            	}
	            },
	            complete: function() {
	            	hideLoading();
	            }
	        });
		};
	</script>
	<body>
		<div data-role="page">
			<div data-role="header" style="overflow:hidden;">
				<h1 id="title">个人中心</h1>
			    <a onclick="exit();" data-icon="power" class="ui-btn-right">退出</a>
			</div><!-- /header -->
			<div data-role="main" class="ui-content">
				<ul data-role="listview" data-inset="true" data-divider-theme="a">
				    <li data-role="list-divider">参与的活动
				    <span class='ui-li-count' style="background-color:#2a2a2a;border-color: #1d1d1d;color:#fff;text-shadow: 0 1px 0 #111;">${fn:length(partinActivities)}</span>
				    </li>
				    <c:forEach items="${partinActivities}" var="activity"> 
				    	 <li><a onclick="showLoading();location.href='<%=basePath%>singleactivity/view?aid=${activity.id }&ret=2'">
				    	 <script>
				    	 	var atype = "${activity.atype}";
				    	 	var type = "${activity.activityname}";
				    	 	var addr = "${activity.addr}";
				    	 	if(addr.length > 15 && $(document).width() < 500) {
				        		addr = "..." + addr.substring(addr.length-15);
				        	}
				    	 	document.write(type + " <span class='round'>${activity.groupnum}/${activity.numlimit}</span>"
				    	 			+ "<span class='ui-li-count'>${activity.atimeForShow}${activity.gathertime}</span>");
				    	 </script>
				    	 </a></li>
					</c:forEach>
					<c:if test="${partinActivities== null || fn:length(partinActivities) == 0}">
						<li>最近未发现有您参与的活动</li>
					</c:if>
				    <li data-role="list-divider">发起的活动
				    <span class='ui-li-count' style="background-color:#2a2a2a;border-color: #1d1d1d;color:#fff;text-shadow: 0 1px 0 #111;">${fn:length(sponseActivities)}</span>
				    </li>
				    <c:forEach items="${sponseActivities}" var="activity"> 
				    	 <li><a onclick="showLoading();location.href='<%=basePath%>singleactivity/view?aid=${activity.id }&ret=2'">
				    	 <script>
				    	 	var atype = "${activity.atype}";
				    	 	var type = "${activity.activityname}";
				    	 	var addr = "${activity.addr}";
				    	 	if(addr.length > 15 && $(document).width() < 500) {
				        		addr = "..." + addr.substring(addr.length-15);
				        	}
				    	 	document.write(type + " <span class='round'>${activity.groupnum}/${activity.numlimit}</span>"
				    	 			+ "<span class='ui-li-count'>${activity.atimeForShow}${activity.gathertime}</span>");
				    	 </script>
				    	 </a></li>
					</c:forEach>
				    <c:if test="${sponseActivities== null || fn:length(sponseActivities) == 0}">
						<li>最近未发现有您发起的活动</li>
					</c:if>
				</ul>
		  	</div>
		  	<div data-role="footer" data-position="fixed">
		        <div data-role="navbar" data-iconpos="right">
		            <ul>
		                <li>
							<a onclick="showLoading();location.href='<%=basePath%>activity/sponse'" data-icon="edit">发起活动</a>
		                </li>
		                <li>
							<a data-icon="user" class="ui-btn-active">个人中心</a>
						</li>
		                <li>
		                	<a onclick="showLoading();location.href='<%=basePath%>activity/view'" data-icon="location" >开始</a>
		                </li>
		            </ul>
		        </div><!-- /navbar -->
		    </div>
		</div>
	</body>
</html>
