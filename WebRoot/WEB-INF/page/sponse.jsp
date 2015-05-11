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
		<title>发起活动</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="keywords" content="玩什么,去哪玩,和谁玩,几点玩,不想太远,没有朋友,小区玩,乒乓球,足球,象棋,游泳,瑜伽">
		<meta http-equiv="description" content="玩什么,去哪玩,和谁玩,几点玩,不想太远,没有朋友,小区玩,乒乓球,足球,象棋,游泳,瑜伽">
		<link rel="stylesheet" href="http://libs.useso.com/js/jquery-mobile/1.4.2/jquery.mobile.min.css" />
		<link rel="stylesheet" href="css/buttons.css" />
		<link rel="stylesheet" href="css/global.css" />
		<link rel="stylesheet" href="css/font-awesome.min.css" />
		<style>
			.controlgroup-textinput{
			    padding-top:.22em;
			    padding-bottom:.22em;
			    width:50%;
			}
		</style>
		<script src="http://libs.useso.com/js/jquery/1.9.1/jquery.min.js"></script>
		<script src="js/jquery.animate-colors-min.js"></script>
		<script src="http://libs.useso.com/js/jquery-mobile/1.4.2/jquery.mobile.min.js"></script>
		<script src="js/globle.js"></script>
		<link href="css/mobiscroll.custom-2.6.2.min.css" rel="stylesheet" type="text/css" />
		<script src="js/mobiscroll.custom-2.6.2.min.js" type="text/javascript"></script>
		<link rel="shortcut icon" href="<%=basePath%>css/images/favicon.ico" />
	</head>
	<script type="text/javascript">
		/*var mapObj,marker;  
		function mapInit() { 
			if(localStorage.getItem("lng") == undefined || localStorage.getItem("lat") == undefined) {
				localStorage.setItem("lng",'116.397428');
				localStorage.setItem("lat",'39.90923');
			}
			//初始化集合地点
			$("#addrlng").val(localStorage.getItem("lng"));
			$("#addrlat").val(localStorage.getItem("lat"));
		    mapObj = new AMap.Map("iCenter", {  
		        center:new AMap.LngLat(localStorage.getItem("lng"),localStorage.getItem("lat")),  
		        level:13  
		    });  
		    marker = new AMap.Marker({  
		        position:mapObj.getCenter(),  
		        draggable:true, //点标记可拖拽  
		        cursor:'move',  //鼠标悬停点标记时的鼠标样式  
		    });
		    marker.setMap(mapObj);  
		  	//绑定marker拖拽事件
			mapObj.bind(marker,"dragend",function(e){
				$("#addrlng").val(e.lnglat.lng);
			    $("#addrlat").val(e.lnglat.lat);
				 geocoder(e.lnglat);
			});
		}  
		function geocoder(lnglatXY) {  
		    var MGeocoder;  
		    //加载地理编码插件  
		    mapObj.plugin(["AMap.Geocoder"], function() {          
		        MGeocoder = new AMap.Geocoder({   
		            radius: 1000,  
		            extensions: "all"  
		        });  
		        //返回地理编码结果   
		        AMap.event.addListener(MGeocoder, "complete", function(data) {
	        		$("#addr").val(data.regeocode.formattedAddress);
		        });   
		        //逆地理编码  
		        MGeocoder.getAddress(lnglatXY);   
		    });  
		}  		*/
		var yundongMap = {};
		var xiuxianMap = {};
		$(document).ready(function(){
			//初始化集合地点
			$("#addrlng").val(localStorage.getItem("lng"));
			$("#addrlat").val(localStorage.getItem("lat"));
			<c:forEach items="${yundongMap}" var="yundong">  
			   	yundongMap["${yundong.key}"] = "${yundong.value}";
			</c:forEach> 
			<c:forEach items="${xiuxianMap}" var="xiuxian">  
				xiuxianMap["${xiuxian.key}"] = "${xiuxian.value}";
			</c:forEach> 
			$("#atype0").change(function() {
				$("#atype1").parent().remove();
				$("#atype1").remove();
				$("#activitynamediv").remove();
				var atype1options = "<select id='atype1' data-mini='true' data-native-menu='false'>";
				if($(this).val() == '2') {
					atype1options = "<div id='activitynamediv'><input id='activityname' type='text' placeholder='请输入自定义活动' data-mini='true'/></div>";
					$(atype1options).insertAfter($(this).parent()).trigger('create');
					return;
				}
				if($(this).val() == '0') {
					for(var yundong in yundongMap) {
						atype1options += "<option value='"+yundong+"'>"+yundongMap[yundong]+"</option>";						
					}
				} else if($(this).val() == '1') {
					for(var xiuxian in xiuxianMap) {
						atype1options += "<option value='"+xiuxian+"'>"+xiuxianMap[xiuxian]+"</option>";						
					}
				} 
				atype1options += "</select>";
				$(atype1options).insertAfter($(this).parent());
				//可以设置主题：
				$("#atype1").selectmenu({
					theme:"e"
				});
			});
			//动态定义可选的日期
			var currentH = "${currentH}";
			currentH = parseInt(currentH);
			var atimeoptions = genAtimeByH(currentH);
			$(atimeoptions).insertAfter($("#atimeForInsert"));
			$("#atime").selectmenu({
				theme:"e"
			});
			$('#gathertime').mobiscroll().time({
		        theme: 'jqm',
		        lang: 'zh',
		        display: 'bubble',
		        animate: 'fade',
		        mode: 'scroller'
		    }); 
			$("#addr").click(function() {
				$(this).animate({'backgroundColor' : '#FFFFFF'}, 500);
			});
			$("gathertime").on("tap",function(){
				$(this).animate({'backgroundColor' : '#FFFFFF'}, 500);
			});
			$("#description").click(function() {
				$(this).animate({'backgroundColor' : '#FFFFFF'}, 500);
			});
		});
		/*
		*修改地图的高度
		*/
		var matchMapHeight = function() {
			var dHeight = $(document).height()/3;
			$("#iCenter").css({"height":dHeight+"px"}); 
		};
		var submitActivity = function() {
			if(validateForm()) {
				var atype = $("#atype0").val();
				if($("#atype0").val() != "2") {
					atype += $("#atype1").val();
				} else {
					atype += "0";
				}
				showLoading("提交中...");
				jQuery.ajax({
		            type: "POST",
		            cache: false, //无缓存 Jquery会给每个请求加一个随即数
		            url: "<%=basePath%>activity/submitActivity", //提交的url 请求下级地址
		            dataType : "json",
		            data: 
		            {
		            	atype : atype,
		            	atime : $("#atime").val(),
		            	addr : $("#addr").val(),
		            	description : $("#description").val(),
		            	lng : localStorage.getItem("lng"),
		            	lat : localStorage.getItem("lat"),
		            	addrlng : $("#addrlng").val(),
		            	addrlat : $("#addrlat").val(),
		            	activityname : $("#activityname").val(),
		            	gathertime : $("#gathertime").val(),
		            	numlimit : $("#numlimit").val()
            		},//提交的参数
		            success: function (result) {    //请求成功的方法
		            	var status = result.model.status;
		            	var aid = result.model.aid;
			            if(status == '-1') {
			            	toast("您暂时未登陆！！！");
			            	location.href="<%=basePath%>index/home";
			            } else if(status == '0') {
			            	toast("添加活动成功！！！");
			            	location.href="<%=basePath%>singleactivity/view?aid=" + aid + "&ret=2";
			            } else {
			            	toast("系统错误！！！");
			            	location.href="<%=basePath%>index/home";
			            }
		            },
		            complete: function() {
		            	hideLoading();
		            }
		        });
			}
		};
		var validateForm = function() {
			if($("#atype0").val() != "2" && $("#atype1").val() == undefined) {
				toast("请选择活动！");
				return false;
			}
			if($("#atype0").val() == "2" && $("#activityname").val() == "") {
				toast("请选择活动！");
				return false;
			}
			if($("#gathertime").val() == "") {
				$("#gathertime").animate({'backgroundColor' : 'rgb(255, 231, 240)'}, 1000);
				return false;
			}
			var h = parseInt($("#gathertime").val().substring(0,2));
			if($("#atime").val() == "1" || $("#atime").val() == "3" || $("#atime").val() == "4") {//上午的
				if(h>12 && h <24) {
					toast("集合时间不符");
					return false;
				}
			}
			if($("#atime").val() == "0" || $("#atime").val() == "2" || $("#atime").val() == "5") {//下午的
				if(h>0 && h <12) {
					toast("集合时间不符");
					return false;
				}
			}
			if($("#addr").val() == "") {
				$("#addr").animate({'backgroundColor' : 'rgb(255, 231, 240)'}, 1000);
				return false;
			}
			return true;
		};
		var mapChooser = function() {
			$("#iCenter").slideToggle(1000);
			//已知点坐标  
			var lnglatXY = new AMap.LngLat($("#addrlng").val(),$("#addrlat").val());
			geocoder(lnglatXY);
			if(localStorage.getItem("sponse_has_toast_change") != '1') {
				toast("拖动可更改坐标");
				localStorage.setItem("sponse_has_toast_change","1");
			}
		};
	</script>
	<body>
		<div data-role="page">
			<div data-role="main" class="ui-content">
				<form>
				    <ul data-role="listview" data-inset="true">
				        <li class="ui-field-contain">
					        <fieldset data-role="controlgroup" data-type="vertical">
							    <legend>什么活动:</legend>
							    <label for="atype0">活动类别</label>
							    <select name="atype0" id="atype0" data-mini="true" data-native-menu="false">
							    	<option>类别</option>
							        <option value="0">体育竞技</option>
							        <option value="1">休闲娱乐</option>
							        <option value="2">自定义</option>
							    </select>
							</fieldset>
				        </li>
				        <li class="ui-field-contain">
				            <label for="atime" id="atimeForInsert">时间:</label>
				        </li>
				        <li class="ui-field-contain">
				            <label for="gathertime">集合时间:</label>
					        <input name="gathertime" type="text" data-mini="true" placeholder="点击选择集合时间" id="gathertime"/>
				        </li>
				        <li class="ui-field-contain">
				            <label for="addr">集合地点:</label>
				            <%--<div data-role="controlgroup" data-type="horizontal" data-mini="true">
							    --%><input type="text" data-mini="true" name="addr" id="addr"
							    placeholder="小区三号楼超市旁" value="">
							    <%--<button onclick="mapChooser();" data-mini="true" type="button" data-icon="location" data-iconpos="notext">Submit</button>
								<div id="iCenter"></div> 
								--%><input type="hidden" name="addrlng" id="addrlng"/>
							    <input type="hidden" name="addrlat" id="addrlat"/>
				        </li>
				        <li class="ui-field-contain">
				        	<label for="numlimit">人数限制：</label>
    						<input type="range" name="numlimit" id="numlimit" data-mini="true" data-highlight="true" min="3" max="20" value="5">
				        </li>
				    </ul>
				</form>
				<div style="text-align:center;">
					<span class="button-wrap">
                   		<a onclick="submitActivity()" style="cursor:pointer;" class="button button-pill button-primary">
							<span style="color:white;">提交活动</span>
						</a>
					</span>
				</div>
			</div>
			<div data-role="footer" data-position="fixed">
		        <div data-role="navbar" data-iconpos="right">
		            <ul>
		                <li>
							<a data-icon="edit" class="ui-btn-active">发起活动</a>
		                </li>
		                <li>
							<a onclick="showLoading();location.href='<%=basePath%>activity/person'" data-icon="user">个人中心</a>
						</li>
		                <li>
		                	<a onclick="showLoading();location.href='<%=basePath%>activity/view'" data-icon="location">开始</a>
		                </li>
		            </ul>
		        </div><!-- /navbar -->
		    </div>
	    </div>
	</body>
</html>
