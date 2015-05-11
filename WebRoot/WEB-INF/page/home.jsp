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
		<title>iwantplay</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="keywords" content="玩什么,去哪玩,和谁玩,几点玩,不想太远,没有朋友,小区玩,乒乓球,足球,象棋,游泳,瑜伽">
		<meta http-equiv="description" content="玩什么,去哪玩,和谁玩,几点玩,不想太远,没有朋友,小区玩,乒乓球,足球,象棋,游泳,瑜伽">
		<link rel="stylesheet" href="http://libs.useso.com/js/jquery-mobile/1.4.2/jquery.mobile.min.css" />
		<link rel="stylesheet" href="css/buttons.css" />
		<link rel="stylesheet" href="css/global.css" />
		<link rel="stylesheet" href="css/font-awesome.min.css" />
		<script src="http://libs.useso.com/js/jquery/1.9.1/jquery.min.js"></script>
		<script src="js/jquery.animate-colors-min.js"></script>
		<script src="http://libs.useso.com/js/jquery-mobile/1.4.2/jquery.mobile.min.js"></script>
		<script src="js/globle.js"></script>
		<script src="js/tool/md5.min.js"></script>
		<script>
			var urlAfterLogin = '1';//1-发起活动，2-个人中心
			var goToActivitySponse = function() {
				location.href="<%=basePath%>activity/sponse";
			};
			var goToLoginPage = function(wheres) {
				showLoading();
				location.href = "<%=basePath%>admin/index?urlAfterLogin=" + wheres;
			};
			$(document).ready(function(){
				var openid = "${openid}";
				if(openid != "") {
					localStorage.setItem("openid",openid);
				}
				var username = localStorage.getItem("username");
				var password = localStorage.getItem("password");
				$("#username").val(username);
				$("#password").val(password);
				$("#username").click(function() {
					$("#username").animate({'backgroundColor' : '#FFFFFF'}, 500);
				});
				$("#password").click(function() {
					$("#password").animate({'backgroundColor' : '#FFFFFF'}, 500);
				});
			}); 
		</script>
		<script type="text/javascript" name="baidu-tc-cerfication" src="http://apps.bdimg.com/cloudaapi/lightapp.js#a73ac96f86ebc87f455ae8d17bf444dd"></script>
<script type="text/javascript">window.bd && bd._qdc && bd._qdc.init({app_id: 'a30c943f10ba022c84123e89'});</script>
		<link rel="shortcut icon" href="<%=basePath%>css/images/favicon.ico" />
		<style>
		</style>
	</head>
	<body>
		<div data-role="page">
			<div data-role="main" class="ui-content">
				<div style="text-align:center;margin-top:20%;">
					<span style="cursor:pointer;" class="button-wrap button-download">
			            <a class="button button-circle button-flat button-flat-primary glow" 
			            onclick="showLoading();location.href='<%=basePath%>activity/view'"><span style="color:white;">我要玩</span></a>
			        </span>
		        </div>
			</div><!-- /content -->
			
			<div data-role="footer" data-position="fixed">
		        <div data-role="navbar" data-iconpos="right">
		            <ul id="footer">
		                <li>
		                	<c:if test="${islogin != '1'}"> 
		                		<a onclick="goToLoginPage(1);" data-icon="edit">发起活动</a>
							</c:if> 
							<c:if test="${islogin == '1'}">  
								<a onclick="showLoading();javascript:goToActivitySponse()" data-icon="edit">发起活动</a>
							</c:if> 
		                </li>
		                <li>
		                	<c:if test="${islogin != '1'}"> 
		                		<a onclick="goToLoginPage(2);" data-icon="user">个人中心</a>
							</c:if> 
							<c:if test="${islogin == '1'}">  
								<a onclick="showLoading();location.href='<%=basePath%>activity/person'" data-icon="user">个人中心</a>
							</c:if> 
		                </li>
		            </ul>
		        </div><!-- /navbar -->
		    </div><!-- /footer -->
		</div><!-- /page -->
	</body>
</html>
