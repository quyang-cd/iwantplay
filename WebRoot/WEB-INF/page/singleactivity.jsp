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
		<link rel="stylesheet" href="css/singleactivity.css" />
		<link rel="stylesheet" href="css/global.css" />
		<script src="http://libs.useso.com/js/jquery/1.9.1/jquery.min.js"></script>
		<script src="js/jquery.animate-colors-min.js"></script>
		<script src="http://libs.useso.com/js/jquery-mobile/1.4.2/jquery.mobile.min.js"></script>
		<script src="js/globle.js"></script>
		<!-- 高德地图jsAPI -->
		<script src='http://webapi.amap.com/maps?v=1.2&key=dd20c0f37edcbcf3edc652d62defbcd7'></script>
		<script type="application/javascript" src="js/iscroll.js"></script>  
		<script src="js/timeago.js"></script>
		<style>
			
		</style>
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
		var mapObj,marker; 
		var groupLength = 0;
		var groupNumLimit = "${activity.numlimit}";
		//初始化地图对象，加载地图  
		function mapInit(){  
		    mapObj = new AMap.Map("iCenter",{  
		        center:new AMap.LngLat("${activity.lng}","${activity.lat}") //地图中心点  
	        });  
		    addMarker();
	        mapObj.setFitView();
		}  
		//添加带文本的点标记覆盖物  
		function addMarker(){   
			//加入集合地点
			var addrMarker = new AMap.Marker({  
		        position:new AMap.LngLat("${activity.addrlng}","${activity.addrlat}"),
		        icon:"http://webapi.amap.com/images/marker_sprite.png",
		        cursor:'move'   //鼠标悬停点标记时的鼠标样式  
		    });  
			addrMarker.setMap(mapObj);  
		    //自定义点标记内容     
		    var markerContent = document.createElement("div");  
		    markerContent.className = "markerContentStyle";  		      
		    //点标记中的图标  
		    var markerImg= document.createElement("img");  
	     	markerImg.className="markerlnglat";  
	     	markerImg.src="css/images/marker/marker_leader.png";     
	     	markerContent.appendChild(markerImg);  
	     	//点标记中的文本  
	     	var markerSpan = document.createElement("span");  
	     	markerSpan.innerHTML = "${activity.description}";  
	     	markerContent.appendChild(markerSpan);  
	     	marker = new AMap.Marker({  
	        	map:mapObj,  
	        	position:new AMap.LngLat("${activity.lng}","${activity.lat}"), //基点位置  
	        	draggable:false,  //是否可拖动  
	        	icon:"css/images/marker/marker_leader.png"
	    	});  
	    	marker.setMap(mapObj);  //在地图上添加点  
		}  
		$(document).ready(function(){ 
			var openid = "${openid}";
			if(openid != "") {
				localStorage.setItem("openid",openid);
			}
			var lng = localStorage.getItem("lng");
			var lat = localStorage.getItem("lat");
			if(lng == '' || lat == '') {
				location.href = "<%=basePath%>activity/view";
			}
			matchMapHeightAndScroll();
			mapInit();
			var atype1 = "${activity.atype}";
			$("#title").text("${activity.activityname}");
			initWorker();
			//添加点到地图中
			<c:forEach items="${group}" var="ag" varStatus="status"> 
				//添加坐标到地图
               var marker = new AMap.Marker({  
			        position:new AMap.LngLat("${ag.ulng}","${ag.ulat}"),
			        icon:"css/images/marker/${status.index+1}.png",
			        cursor:'move'   //鼠标悬停点标记时的鼠标样式  
			    });  
			    marker.setMap(mapObj);  
			</c:forEach>
			groupLength = parseInt("${fn:length(group)}");
			mapObj.setFitView(); 
			var hasBecomeGroup = "${hasBecomeGroup}";
			if(hasBecomeGroup == '1') {
				$("#buttoncontrolgroup_2").hide();
				$("#buttoncontrolgroup_1").show();
			} else {
				$("#buttoncontrolgroup_1").hide();
				$("#buttoncontrolgroup_2").show();
			}
			jQuery("span.timeago").timeago(); 
		}); 
		/*
		*修改地图的高度
		*/
		var matchMapHeightAndScroll = function() {
			var dHeight = $(document).height()/5;
			$("#iCenter").css({"height":dHeight+"px"}); 
			$("#wrapper").css({"top":(dHeight+160)+"px"}); 
			$("#wrapper").css({"width":$("#iCenter").css('width')}); 
		};
		//声明一个worker;
	    var worker;
	    //初始化调用方法
	    function initWorker(){
	    	window.setInterval(function () {
	    		getGroupsMsg();
            }, 3000);
	    }
	    var getGroupsMsg = function() {
	    	$.get("worker/singleactivitygroup", 
                  {"aid": "${activity.id}"}, 
                  function (data) {
                	  	var msgjson = JSON.parse(data);
	   	    	  		var msgn = msgjson.msgsize;
	   	    	  		$("#msgnSpan").text("("+msgn+ ")");
	   	    	  		var groupjson = msgjson.agc;
	   	    	  		//处理参加人员变化
	   	    	  		var flag = false;
	   					for(var i=0; i<groupjson.length; i++)
	   				  	{
	   						if(document.getElementById(groupjson[i].username)) {
	   							continue;
	   						}
	   						flag = true;
	   						groupLength = groupLength + 1;
	   				     	$("#" + groupjson[i].username).text(groupjson[i].msg);
	                       	$("#partinDevider").after("<li style='cursor:pointer;' onclick='locatePerson("+groupjson[i].ulng+","+groupjson[i].ulat+")'><img src='css/images/marker/mini/"+groupLength+".png' alt='France' class='ui-li-icon ui-corner-none'>"
	       							+groupjson[i].username +"<input type='hidden' id='"+groupjson[i].username +"'/><span class='ui-li-count timeago' title='"+groupjson[i].jointime+"'></span></li>");
	   	                    //添加坐标到地图
	   	                    var addmarker = new AMap.Marker({  
	   					        position:new AMap.LngLat(groupjson[i].ulng,groupjson[i].ulat),
	   					        icon:"css/images/marker/"+groupLength+".png",
	   					        cursor:'move'   //鼠标悬停点标记时的鼠标样式  
	   					    });  
	   	                    addmarker.setMap(mapObj);  
	   	                    toast("欢迎"+groupjson[i].username+"加入！");
	   				  	}
	   					if(flag) {
	   						mapObj.setFitView();
	   		            	$("#partinList").listview('refresh');
	   		            	myScroll.refresh();
	   		            	jQuery("span.timeago").timeago(); 
	   		            	$("#currentGroupLength").text(groupLength);
	   					}
               	}
            );
	    };
	    var locatePerson = function(lng,lat) {
	    	mapObj.setZoomAndCenter(18,new AMap.LngLat(lng,lat));
	    };
	    var partinGroup = function() {
	    	if(groupLength >= (groupNumLimit - 1)) {
	    		toast("参加人数已达上限");
	    		return;
	    	}
	    	var username = "${currentusername}";
	    	var aid = "${activity.id}";
	    	var ulng = localStorage.getItem("lng");
	    	var ulat = localStorage.getItem("lat");
	    	showLoading("加入中...");
	    	jQuery.ajax({
	            type: "POST",
	            cache: false, //无缓存 Jquery会给每个请求加一个随即数
	            url: "<%=basePath%>singleactivity/partin", //提交的url 请求下级地址
	            dataType : "json",
	            data: 
	            {
	            	username : username,
	            	aid : aid,
	            	ulng : ulng,
	            	ulat : ulat
        		},
        		success: function(result) {
        			hideLoading();
        			var status = result.model.status;
        			if(status == '-2') {
		            	toast("您已经加入。。。");
		            	location.reload();
		            } else if(status == '0') {
		            	toast("成功加入，到时参加哦");
		            	$("#buttoncontrolgroup_2").hide();
		            	$("#buttoncontrolgroup_1").show();
		            } else {
		            	toast("加入失败！！！");
		            	location.reload();
		            }
        		},
	            complete: function() {
	            	hideLoading();
	            }
	        });
	    };
	    jQuery(window).resize(function(){
			$("#wrapper").css({"width":$("#iCenter").css('width')}); 
		});
	    var retPrevPage = function() {
	    	showLoading();
	    	if("${ret}" == '1' ) {
	    		location.href='<%=basePath%>activity/view';
	    	} else if("${ret}" == '2') {
	    		location.href='<%=basePath%>activity/person';
	    	}
	    	
	    };
	</script>
	<body>
		<div data-role="page">
			<div data-role="header" style="overflow:hidden;">
				<h1 id="title">
					${atime }-${activity.addr}			
				</h1>
			    <a onclick="retPrevPage();" data-icon="arrow-l" class="ui-btn-left">返回</a>
			    <a onclick="showLoading();location.href='<%=basePath%>index/home'" data-icon="home" class="ui-btn-right">主页</a>
			</div><!-- /header -->
			<div data-role="main" class="ui-content">
				<div id='iCenter'></div>
				<fieldset data-role="controlgroup" data-type="horizontal" data-mini="true">
			        <button data-icon="location" data-iconpos="notext">Search</button>
			        <button id="addrButton" onclick="mapObj.setFitView();" style="background-color: #FF776B;font-weight:bold;">
			        <script>
			        	var addr = "${activity.addr }";
			        	if(addr.length > 15 && $(document).width() < 500) {
			        		addr = "..." + addr.substring(addr.length-15);
			        	}
			        	document.write(addr);
			        </script>
			        </button>
			    </fieldset>
			    <fieldset data-role="controlgroup" data-type="horizontal" data-mini="true">
			    	<button data-icon="clock" data-iconpos="notext">Search</button>
			        <button >${atime }</button>
			        <button data-icon="user" data-iconpos="notext">Search</button>
			        <button onclick="locatePerson('${activity.lng}','${activity.lat}');">${activity.leader }</button>
			    </fieldset>
			    <hr style="height:3px; background-color:#ccc; border:0; margin-top:12px; margin-bottom:12px;">
			    <div id="wrapper">
					<div id="scroller">
					    <ul id="partinList" data-role="listview" data-inset="true">
					    	<li style="cursor:pointer" onclick="locatePerson('${activity.lng }','${activity.lat }');">
								<img src="css/images/marker/mini/marker_leader.png" alt="France" class="ui-li-icon ui-corner-none">
								${activity.leader }
								<input type="hidden" id="${activity.leader }"/>
								<span class='ui-li-count timeago' title="${activity.starttimestr }"></span>
							</li>
					    	<li id="partinDevider"  data-role="list-divider">已入队(<span id="currentGroupLength">${fn:length(group)}</span>/${activity.numlimit-1})：
					    		<span class='ui-li-count' style="background-color:#2a2a2a;border-color: #1d1d1d;color:#fff;text-shadow: 0 1px 0 #111;">${activity.gathertime }集合</span>
					    	</li>
							<c:forEach items="${group}" var="ag" varStatus="status"> 
						    	<li style="cursor:pointer;" onclick="locatePerson('${ag.ulng }','${ag.ulat }');">
									<img src="css/images/marker/mini/${status.index+1}.png" alt="France" class="ui-li-icon ui-corner-none">
									${ag.uname }
									<input type="hidden" id="${ag.uname }"/>
									<span class='ui-li-count timeago' title="${ag.jointimestr}"></span>
								</li>
							</c:forEach>
							<li>
								<span style="font-size:0.5em;color:rgb(142, 146, 146);">入队后可以群聊哦！</span>
							</li>
						</ul>
					</div>
				</div>
		  	</div>
		  	<div data-role="footer" data-position="fixed" style="text-align:right;overflow:hidden;">
		        <div id="buttoncontrolgroup_1" style="overflow:hidden;" data-role="controlgroup" data-type="horizontal">
		        	<button>${currentusername}</button>
			    	<button onclick="showLoading();location.href='<%=basePath%>singleactivitymsg/view?aid=${activity.id }'" class="ui-btn-b" data-icon="arrow-r" data-iconpos="right">群聊
			    		<span id="msgnSpan"></span>
			    	</button>
				</div> 
				<div id="buttoncontrolgroup_2" style="overflow:hidden;" data-role="controlgroup" data-type="horizontal">
		        	<button>${currentusername}</button>
			    	<button id="iwantjoinbutton" class="ui-btn-b" onclick="partinGroup();" style="background-color: #FF776B;font-weight:bold;">加入</button>
				</div>
		    </div>
		</div>
	</body>
</html>
