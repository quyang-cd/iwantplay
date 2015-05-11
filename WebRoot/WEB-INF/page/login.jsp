<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>iwantplay</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf8" />
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="keywords" content="玩什么,去哪玩,和谁玩,几点玩,不想太远,没有朋友,小区玩,乒乓球,足球,象棋,游泳,瑜伽">
		<meta http-equiv="description" content="玩什么,去哪玩,和谁玩,几点玩,不想太远,没有朋友,小区玩,乒乓球,足球,象棋,游泳,瑜伽">
		<link rel="stylesheet" href="http://libs.useso.com/js/jquery-mobile/1.4.2/jquery.mobile.min.css" />
		<link rel="stylesheet" href="css/global.css" />
		<link rel="stylesheet" href="css/font-awesome.min.css" />
		<style>
			#buttonGroup .ui-controlgroup-controls {
				width:100%;
			}
		</style>
		<script src="http://libs.useso.com/js/jquery/1.9.1/jquery.min.js"></script>
		<script src="js/jquery.animate-colors-min.js"></script>
		<script src="http://libs.useso.com/js/jquery-mobile/1.4.2/jquery.mobile.min.js"></script>
		<script src="js/globle.js"></script>		
		<script src="js/tool/md5.min.js"></script>
		<script>
			var urlAfterLogin = "${urlAfterLogin}";//1-发起活动，2-个人中心
			var islogin = localStorage.getItem("islogin");
			if(islogin == '1') {
				location.href="index.html";
			}
			//用户登录与否来决定底部框的显示
			//用户注册
			var doRegUser = function() {
				if(validateForm()) {
					showLoading("注册中...");
					var passwordMd5 = MD5($("#password").val());
					jQuery.ajax({
			            type: "POST",
			            cache: false, //无缓存 Jquery会给每个请求加一个随即数
			            url: "<%=basePath%>admin/reg", //提交的url 请求下级地址
			            dataType : "json",
			            data: 
			            {
			            	username : $("#username").val(),
			            	password : passwordMd5
	            		},//提交的参数
			            success: function (result) {    //请求成功的方法
			            	var status = result.model.status;
			            	if(status == "0") {
			            		localStorage.setItem("username",$("#username").val());
			            		localStorage.setItem("password",$("#password").val());
			            		setCookie("SESSION_LOGIN_USERNAME",$("#username").val(),5*365*24*60*60);
			            		setCookie("SESSION_LOGIN_PASSWORD",passwordMd5,5*365*24*60*60);
			            		if(urlAfterLogin == '1') {
			            			location.href="<%=basePath%>activity/sponse";
			            		} else if(urlAfterLogin == '2') {
			            			location.href="<%=basePath%>activity/person";
			            		} else if(urlAfterLogin == '3') {
			            			location.href = "<%=basePath%>singleactivity/view?aid=${aid}&ret=1";
			            		}
			            	} else {
			            		$("#username").animate({'backgroundColor' : 'rgb(255, 231, 240)'}, 1000);
			            		$("#password").animate({'backgroundColor' : 'rgb(255, 231, 240)'}, 1000);
			            	}
			            },
			            complete: function() {
			            	hideLoading();
			            }
			        });
				}
			};
			//用户登录
			var doLoginUser = function() {
				if(validateForm()) {
					showLoading("登录中...");
					var passwordMd5 = MD5($("#password").val());
					jQuery.ajax({
			            type: "POST",
			            cache: false, //无缓存 Jquery会给每个请求加一个随即数
			            url: "<%=basePath%>admin/login", //提交的url 请求下级地址
			            dataType : "json",
			            data: 
			            {
			            	username : $("#username").val(),
			            	password : passwordMd5
	            		},//提交的参数
			            success: function (result) {    //请求成功的方法
			            	var status = result.model.status;//附近的活动
			            	if(status == '0') {
			            		localStorage.setItem("username",$("#username").val());
			            		localStorage.setItem("password",$("#password").val());
			            		setCookie("SESSION_LOGIN_USERNAME",$("#username").val());
			            		setCookie("SESSION_LOGIN_PASSWORD",passwordMd5);
			            		if(urlAfterLogin == '1') {
			            			location.href="<%=basePath%>activity/sponse";
			            		} else if(urlAfterLogin == '2') {
			            			location.href="<%=basePath%>activity/person";
			            		} else if(urlAfterLogin == '3') {
			            			location.href = "<%=basePath%>singleactivity/view?aid=${aid}&ret=1";
			            		}
			            	} else if(status == '1'){
			            		$("#username").animate({'backgroundColor' : 'rgb(255, 231, 240)'}, 1000);
			            		$("#password").animate({'backgroundColor' : 'rgb(255, 231, 240)'}, 1000);
			            	} else if(status == '2'){
			            		$("#password").animate({'backgroundColor' : 'rgb(255, 231, 240)'}, 1000);
			            	} else if(status == '3'){
			            		$("#username").animate({'backgroundColor' : 'rgb(255, 231, 240)'}, 1000);
			            	}
			            },
			            complete: function() {
			            	hideLoading();
			            }
			        });
				}
			};
			var validateForm = function() {
				if($("#username").val() == "") {
					$("#username").animate({'backgroundColor' : 'rgb(255, 231, 240)'}, 1000);
					return false;
				}
				if($("#password").val() == "") {
					$("#password").animate({'backgroundColor' : 'rgb(255, 231, 240)'}, 1000);
					return false;
				}
				return true;
			};
			var goToActivitySponse = function() {
				location.href = "sponse.html";
			};
			var goToPerson = function() {
				location.href = "person.html";
			};
			$(document).ready(function(){
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
		<style>
		</style>
	</head>
	<body>
		<div data-role="page" id="loginPage">
			<div data-role="header" style="overflow:hidden;">
				<h1 id="title">
				认证
				</h1>
				<a onclick="$.mobile.back();" data-icon="arrow-l" class="ui-btn-left">返回</a>
			    <a onclick="showLoading();location.href='<%=basePath%>index/home'" data-icon="home" class="ui-btn-right">主页</a>
			</div><!-- /header -->
			<div data-role="main" class="ui-content">
				<form>
				    <ul data-role="listview" data-inset="true">
				        <li class="ui-field-contain">
				            <label for="username">用户名:</label>
				            <input type="text" data-mini="true" name="username" id="username" value="" placeholder="用户名" data-theme="a">
		         		</li>
		         		<li class="ui-field-contain">
				            <label for="password">密码:</label>
				            <input type="password" data-mini="true" name="password" id="password" value="" placeholder="密码" data-theme="a">
						</li>
						<li class="ui-field-contain">	
				            <fieldset id="buttonGroup" data-role="controlgroup" data-type="vertical" data-mini="true">
				            	<button type="button" onclick="javascript:doLoginUser();" class="ui-btn ui-corner-all ui-shadow ui-btn-b ui-btn-icon-left ui-icon-check">登录</button>
				            	<button type="button" onclick="javascript:doRegUser();" class="ui-btn ui-corner-all ui-shadow ui-btn-c ui-btn-icon-left ui-icon-plus">注册</button>
				            </fieldset>
			         	</li>
		         	</ul>
		        </form>
			</div><!-- /content -->
		</div><!-- /page -->
	</body>
</html>