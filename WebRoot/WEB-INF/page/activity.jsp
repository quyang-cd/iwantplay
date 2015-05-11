<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>   
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@ taglib uri="/WEB-INF/tld/JSTLFunction.tld" prefix="myfn" %>  
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
		<title>附近活动</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="keywords" content="玩什么,去哪玩,和谁玩,几点玩,不想太远,没有朋友,小区玩,乒乓球,足球,象棋,游泳,瑜伽">
		<meta http-equiv="description" content="玩什么,去哪玩,和谁玩,几点玩,不想太远,没有朋友,小区玩,乒乓球,足球,象棋,游泳,瑜伽">
		<link rel="stylesheet" href="http://libs.useso.com/js/jquery-mobile/1.4.2/jquery.mobile.min.css" />
		<link rel="stylesheet" href="css/activity.css" />
		<link rel="stylesheet" href="css/global.css" />
		<style>
			
		</style>
		<script src="http://libs.useso.com/js/jquery/1.9.1/jquery.min.js"></script>
		<script src="js/jquery.animate-colors-min.js"></script>
		<script src="http://libs.useso.com/js/jquery-mobile/1.4.2/jquery.mobile.min.js"></script>
		<script src="js/globle.js"></script>
		<script src="js/tool/md5.min.js"></script>
		<!-- 高德地图jsAPI -->
		<script src='http://webapi.amap.com/maps?v=1.2&key=dd20c0f37edcbcf3edc652d62defbcd7'></script>
		<script src="js/activity.js"></script>
		<script type="application/javascript" src="js/iscroll.js"></script> 
		<script>
		</script>
		<link rel="shortcut icon" href="<%=basePath%>css/images/favicon.ico" />
	</head>
	<script type="text/javascript">
		var mapObj,toolBar,locationInfo,geolocation; 
		var urlAfterLogin = '1';//1-发起活动，2-个人中心
		var oldLng = localStorage.getItem("lng");
		var oldLat = localStorage.getItem("lat");
		var curCity = localStorage.getItem("city");
		var reqLng = "${reqLng}";
		var reqLat = "${reqLat}";
		var pageNo = 0;
		var globalLng,globalLat;
		//初始化地图对象，加载地图  
		function mapInit(){  
		    mapObj = new AMap.Map("iCenter");  
		    if(reqLng != "" && reqLat != "") {
		    	localStorage.setItem("lng",reqLng);
	        	localStorage.setItem("lat",reqLat);
	        	globalLng = reqLng;
	        	globalLat = reqLat;
	        	//清空地图上的InfoWindow和Marker  
		        windowsArr = [];  
		        marker     = [];  
		        mapObj.clearMap();
	        	//定位成功后开始向服务器索取附近活动
	        	addmarker(0,new AMap.LngLat(reqLng,reqLat));
		    } else {
		    	mapObj.plugin('AMap.Geolocation', function () {
			        geolocation = new AMap.Geolocation({
			            enableHighAccuracy: true,//是否使用高精度定位，默认:true
			            timeout: 5000,          //超过10秒后停止定位，默认：无穷大
			            maximumAge: 0,           //定位结果缓存0毫秒，默认：0
			            convert: true,           //自动偏移坐标，偏移后的坐标为高德坐标，默认：true
			            showButton: true,        //显示定位按钮，默认：true
			            buttonPosition: 'RB',    //定位按钮停靠位置，默认：'LB'，左下角
			            buttonOffset: new AMap.Pixel(10, 20),//定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
			            showMarker: true,        //定位成功后在定位到的位置显示点标记，默认：true
			            showCircle: true,        //定位成功后用圆圈表示定位精度范围，默认：true
			            panToLocation: true,     //定位成功后将定位到的位置作为地图中心点，默认：true
			            zoomToAccuracy:true      //定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
			        });
			        mapObj.addControl(geolocation);
			        AMap.event.addListener(geolocation, 'complete', onComplete);//返回定位信息
			        AMap.event.addListener(geolocation, 'error', onError);      //返回定位出错信息
			        geolocation.getCurrentPosition();
			    });
		    }
		  	//加载输入提示插件  
		    mapObj.plugin(["AMap.Autocomplete"], function() {  
		        //判断是否IE浏览器  
		        if (navigator.userAgent.indexOf("MSIE") > 0) {  
		            document.getElementById("locationkeywords").onpropertychange = autoSearch;  
		        }  
		        else {  
		            document.getElementById("locationkeywords").oninput = autoSearch;  
		        }  
		    });
		  	//加载城市查询插件
		    mapObj.plugin(["AMap.CitySearch"], function() {
		        //实例化城市查询类
		        var citysearch = new AMap.CitySearch();
		        //自动获取用户IP，返回当前城市
		        citysearch.getLocalCity();
		        //citysearch.getCityByIp("123.125.114.*");
		        AMap.event.addListener(citysearch, "complete", function(result){
		            if(result && result.city && result.bounds) {
		                curCity = result.city;
		                localStorage.setItem("city",curCity);
		            }
		        });
		    });
		}  
		/*
		 *解析定位结果
		 */
		function onComplete (data) {
		    locationInfo = data.position;
        	localStorage.setItem("lng",locationInfo.lng);
        	localStorage.setItem("lat",locationInfo.lat);
        	//定位成功后开始向服务器索取附近活动
        	startGetActivitiesFromService(locationInfo.lng,locationInfo.lat);
		}
		/*
		 *解析定位错误信息
		 */
		function onError (data) {
			var lng = oldLng,lat = oldLat;
			if(lng == undefined || lat == undefined) {
				lng = '116.397428';
				lat = '39.90923';
			}
			startGetActivitiesFromService(lng,lat);
		}
		
		var changeUrlAfterLogin = function(url) {
			urlAfterLogin = "<%=basePath%>singleactivity/view?aid=" + url + "&ret=1";
		};
		
		/*
		*修改地图的高度
		*/
		var matchMapHeightAndScroll = function() {
			var dHeight = $(document).height()/3;
			$("#iCenter").css({"height":dHeight+"px"}); 
			$("#wrapper").css({"top":(dHeight+70)+"px"}); 
			$("#wrapper").css({"width":$("#iCenter").css('width')}); 
		};
		$(document).ready(function(){
			var openid = "${openid}";
			if(openid != "") {
				localStorage.setItem("openid",openid);
			}
			showLoading("定位中...");
			matchMapHeightAndScroll();
			mapInit();
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
			$(".ui-input-clear").click(function() {
				document.getElementById("result1").style.display = "none";
				//清空地图上的InfoWindow和Marker  
		        windowsArr = [];  
		        marker     = [];  
		        mapObj.clearMap();  
				addmarker(0,locationInfo);
			});
		}); 
		jQuery(window).resize(function(){
			$("#wrapper").css({"width":$("#iCenter").css('width')}); 
		});
		var startGetActivitiesFromService = function(lng,lat) {
			pageNo = 0;
			globalLng = lng;
			globalLat = lat;
			localStorage.setItem("lng",globalLng);
			localStorage.setItem("lat",globalLat);
			$("#activitiesListView").html('');
			/*if(typeof(EventSource)!=="undefined") {
			  	var source=new EventSource("activity/getArroundActivities?lng=" + 
			  			locationInfo.lng + "&lat=" + locationInfo.lat);
			  	source.onmessage = function(event)
			    {
			  		alert("test");
			  		document.getElementById("result").innerHTML+=event.data + "<br />";
			    };
			} else {
				toast("抱歉，系统问题...");
			}*/
			jQuery.ajax({
	            type: "POST",
	            cache: false, //无缓存 Jquery会给每个请求加一个随即数
	            url: "<%=basePath%>activity/getArroundActivities.json", //提交的url 请求下级地址
	            dataType : "json",
	            data: 
	            {
	            	lng : lng,
	            	lat : lat,
	            	pageNo : pageNo,
	            	openid : localStorage.getItem("openid")
           		},//提交的参数
	            success: function (activities) {    //请求成功的方法
	            	var arroundAS = activities.model.activities;//附近的活动
	            	for(var key in arroundAS){
	            		var type = arroundAS[key].activityname;
	                    //var time = getNameByTime(arroundAS[key].endtime);
	                    var addr = arroundAS[key].addr;
			    	 	if(addr.length > 10 && $(document).width() < 500) {
			        		addr = "..." + addr.substring(addr.length-10);
			        	}
	                    if("${islogin}" == '1') {
	                    	$("#activitiesListView").append("<li><a onclick='gotoSingleActivity("+arroundAS[key].id+")'><img src='css/images/marker/mini/"+(parseInt(key)+1)+".png' alt='France' class='ui-li-icon ui-corner-none'>" + type+
		                    		" <span class='round'>" + arroundAS[key].groupnum +"/"+arroundAS[key].numlimit+"</span><span class='ui-li-count'>"+arroundAS[key].atimeForShow+arroundAS[key].gathertime+"</span></a></li>");
	                    } else {
	                    	$("#activitiesListView").append("<li><a "+ 
	                    			"onclick='goToLoginPage(3,"+arroundAS[key].id+");'"
	                		+ " ><img src='css/images/marker/mini/"+(parseInt(key)+1)+".png' alt='France' class='ui-li-icon ui-corner-none'>" + type+
	                		" <span class='round'>" + arroundAS[key].groupnum +"/"+arroundAS[key].numlimit+"</span><span class='ui-li-count'>"+arroundAS[key].atimeForShow+arroundAS[key].gathertime+"</span></a></li>");
	                    }
	                    //添加坐标到地图
	                    var marker = new AMap.Marker({  
					        position:new AMap.LngLat(arroundAS[key].addrlng,arroundAS[key].addrlat),
					        icon:"css/images/marker/"+(parseInt(key)+1)+".png",
					        cursor:'move'   //鼠标悬停点标记时的鼠标样式  
					    });  
					    marker.setMap(mapObj);  
					    //marker.setAnimation('AMAP_ANIMATION_BOUNCE'); //设置点标记的动画效果，此处为弹跳效果
	                } 
	            	mapObj.setFitView();
	            	if(arroundAS.length <= 0) {
	            		$("#activitiesListView").append("<li>抱歉！未发现附近有任何活动</li>");
	            	} else {
	            		pageNo = pageNo + 1;
	            	}
	            	$("#activitiesListView").listview('refresh');
	            },
	            complete: function() {
	            	hideLoading();
	            }
	        });
		};
		var gotoSingleActivity = function(aid) {
			showLoading();
			location.href = "<%=basePath%>singleactivity/view?aid=" + aid + "&ret=1";
		};
		var goToActivitySponse = function() {
			location.href="<%=basePath%>activity/sponse";
		};
		var goToLoginPage = function(wheres,aid) {
			showLoading();
			location.href = "<%=basePath%>admin/index?urlAfterLogin=" + wheres + "&aid=" + aid;
		};
	</script>
	<script>
		var myScroll;
	    var pullUpEl;
	    var pullUpOffset;
	    function pullUpAction() {//下拉事件
	    	var newStart = pageNo * 3;
	    	if(newStart >= 26) {
	    		toast("输入地址换个地方试试吧！");
	    		return;
	    	}
	    	showLoading("更新中");
            setTimeout(function() {
            	jQuery.ajax({
    	            type: "POST",
    	            cache: false, //无缓存 Jquery会给每个请求加一个随即数
    	            url: "<%=basePath%>activity/getArroundActivities.json", //提交的url 请求下级地址
    	            dataType : "json",
    	            data: 
    	            {
    	            	lng : globalLng,
    	            	lat : globalLat,
    	            	pageNo : pageNo
               		},//提交的参数
    	            success: function (activities) {    //请求成功的方法
    	            	var arroundAS = activities.model.activities;//附近的活动
    	            	for(var key in arroundAS){
    	                    var type = arroundAS[key].activityname;
    	                    //var time = getNameByTime(arroundAS[key].endtime);
    	                    var addr = arroundAS[key].addr;
				    	 	if(addr.length > 10 && $(document).width() < 500) {
				        		addr = "..." + addr.substring(addr.length-10);
				        	}
    	                    if("${islogin}" == '1') {
    	                    	$("#activitiesListView").append("<li><a onclick='gotoSingleActivity("+arroundAS[key].id+")'><img src='css/images/marker/mini/"+(parseInt(key)+newStart+1)+".png' alt='France' class='ui-li-icon ui-corner-none'>" + type+
    		                    		" <span class='round'>" + arroundAS[key].groupnum +"/"+arroundAS[key].numlimit+"</span><span class='ui-li-count'>"+arroundAS[key].atimeForShow+arroundAS[key].gathertime+"</span></a></li>");
    	                    } else {
    	                    	$("#activitiesListView").append("<li><a "+ 
    	                    			"onclick='goToLoginPage(3,"+arroundAS[key].id+");'"
    	                		+ " ><img src='css/images/marker/mini/"+(parseInt(key)+newStart+1)+".png' alt='France' class='ui-li-icon ui-corner-none'>" + type+
    	                		" <span class='round'>" + arroundAS[key].groupnum +"/"+arroundAS[key].numlimit+"</span><span class='ui-li-count'>"+arroundAS[key].atimeForShow+arroundAS[key].gathertime+"</span></a></li>");
    	                    }
    	                    //添加坐标到地图
    	                    var marker = new AMap.Marker({  
    					        position:new AMap.LngLat(arroundAS[key].addrlng,arroundAS[key].addrlat),
    					        icon:"css/images/marker/"+(parseInt(key)+newStart+1)+".png",
    					        cursor:'move'   //鼠标悬停点标记时的鼠标样式  
    					    });  
    					    marker.setMap(mapObj);  
    					    //marker.setAnimation('AMAP_ANIMATION_BOUNCE'); //设置点标记的动画效果，此处为弹跳效果
    	                } 
    	            	mapObj.setFitView();
    	            	if(arroundAS.length <= 0) {
    	            		toast("没有更多活动了");
    	            	} else {
    	            		pageNo = pageNo + 1;
    	            	}
    	            	$("#activitiesListView").listview('refresh');
    	            	myScroll.refresh();
    	            },
    	            complete: function() {
    	            	hideLoading();
    	            }
    	        });
            }, 1000);
	    }
	    /**
	     * 初始化iScroll控件
	     */
	    function loaded() {
	    	pullUpEl = document.getElementById('pullUp');	
	    	pullUpOffset = pullUpEl.offsetHeight;
	    	var dHeight = $(document).height()/3;
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
	    			if (pullUpEl.className.match('loading')) {
	    				pullUpEl.className = '';
	    				pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉加载更多...';
	    			}
	    		},
	    		onScrollMove: function () {
	    			if (this.y < (this.maxScrollY - 40) && !pullUpEl.className.match('flip')) {
	    				pullUpEl.className = 'flip';
	    				pullUpEl.querySelector('.pullUpLabel').innerHTML = '松手开始更新...';
	    				this.maxScrollY = this.maxScrollY;
	    			} else if (this.y > (this.maxScrollY + 40) && pullUpEl.className.match('flip')) {
	    				pullUpEl.className = '';
	    				pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉加载更多...';
	    				this.maxScrollY = pullUpOffset;
	    			}
	    		},
	    		onScrollEnd: function () {
	    			if (pullUpEl.className.match('flip')) {
	    				pullUpEl.className = 'loading';
	    				pullUpEl.querySelector('.pullUpLabel').innerHTML = '加载中...';				
	    				pullUpAction();	// Execute custom function (ajax call?)
	    			}
	    		}
	    	});
	    }

	    //初始化绑定iScroll控件 
	    document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
	    document.addEventListener('DOMContentLoaded', loaded, false); 
    </script>
	<body>
		<div data-role="page">
			<div data-role="main" class="ui-content">
				<div>
     				<input type="search" name="locationkeywords" id="locationkeywords" value=""
     				placeholder="输入地名搜索" data-mini="true">
     				<div id="result1"></div>  
				</div>
	    		<div id='iCenter'></div>
	    		<div id="wrapper">
					<div id="scroller">
	    				<ul id="activitiesListView" data-role='listview' data-count-theme='a' data-inset='true'></ul>
	    				<div id="pullUp">
                            <span class="pullUpIcon"></span>
                            <span class="pullUpLabel"></span>
                        </div>
					</div>
				</div>
			</div>
			<div data-role="footer" data-position="fixed">
		        <div data-role="navbar" data-iconpos="right">
		            <ul>
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
		                <li><a data-icon="location" class="ui-btn-active">开始</a>
		                </li>
		            </ul>
		        </div><!-- /navbar -->
		    </div><!-- /footer -->
	    </div>
	</body>
</html>
