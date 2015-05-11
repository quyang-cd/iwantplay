$(document).bind("mobileinit", function() {
	//不支持3D转屏的设备禁止转屏效果
	$.mobile.transitionFallbacks.slide = "none";
	//禁止hover延迟
	$.mobile.buttonMarkup.hoverDelay = "false";
	$.mobile.defaultPageTransition = 'none'; 
 	$.mobile.defaultDialogTransition = 'none'; 
});
/**
 * 微信页面相关
 */
function onBridgeReady(){
   WeixinJSBridge.call('hideToolbar');
}

if (typeof WeixinJSBridge == "undefined"){
    if( document.addEventListener ){
        document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
    }else if (document.attachEvent){
        document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
        document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
    }
}else{
    onBridgeReady();
}
/*
 * 显示加载框
 */
var showLoading = function(msg) {
	var theme = 'a',
    msgText = msg,
    textVisible = true,
    textonly = false;
	$.mobile.loading( "show", {
	        text: msgText,
	        textVisible: textVisible,
	        theme: theme,
	        textonly: textonly
	});
};
/*
 * 隐藏加载框
 */
var hideLoading = function() {
	$.mobile.loading( "hide" );
};

function goTo(page) {
	showLoading();
	$.mobile.changePage(page, {
		transition : "slide"
	});
}
function goBack() {
	$.mobile.back();
}

//自动消失信息提示框接口 String msg
var toast = function(msg, time) {//msg：提示内容，time:吐司延迟消失时间第(可以不写，不写默认1500毫秒延迟)
 	$("<div class='ui-loader ui-overlay-shadow ui-body-e ui-corner-all'><h3>"
     + msg + "</h3></div>").css( {
		  display : "block",
		  opacity : 0.90,
		  position : "fixed",
		  padding : "7px",
		  "text-align" : "center",
		  background:'#555',
		  color:'#fff',
		  width : "270px",
		  left : ($(window).width() - 284) / 2,
		  top : $(window).height() / 2
 	}).appendTo($.mobile.pageContainer).delay(time ? time : 1500).fadeOut(400,
	   function() {
	    $(this).remove();
	   });
};
var getNameByType = function(atype) {
	var type;
	switch(atype.toString())
    {
        case '0':
        	type = "篮球";
          break;
        case '1':
        	type = "足球";
          break;
        case '2':
        	type = "羽毛球";
          break;
        case '3':
        	type = "乒乓球";
          break;
        case '4':
        	type = "游泳";
          break;
        case '5':
        	type = "健身";
          break;
        case '6':
        	type = "瑜伽";
          break;
        case '7':
        	type = "网球";
          break;
        case 'a':
        	type = "看电视";
          break;
        case 'b':
        	type = "围棋";
          break;
        case 'c':
        	type = "麻将";
          break;
        case 'd':
        	type = "炸金花";
          break;
        case 'e':
        	type = "龙门阵";
          break;
        case 'f':
        	type = "杀人游戏";
          break;
        case 'g':
        	type = "三国杀";
          break;
        case 'h':
        	type = "台球";
          break;
        case 'i':
        	type = "K歌";
          break;
        default:
    }
	return type;
};
var getNameByTime = function(atype) {
	var time;
	switch(atype)
    {
        case '0':
        	time = "今天下午";
          break;
        case '1':
        	time = "明天上午";
          break;
        case '2':
        	time = "明天下午";
          break;
        case '3':
        	time = "后天上午";
          break;
        case '4':
        	time = "今天上午";
          break;
        case '5':
        	time = "后天下午";
          break;
        default:
    }
	return time;
};

var getABCFromDigit = function(digit) {
	var ABC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	return ABC.charAt(digit);
};

var genAtimeByH = function(currentH) {
	var atimeoptions = "<select id='atime' data-mini='true' data-native-menu='false'>";
	if(currentH > 0 && currentH < 8) {
		atimeoptions += "<option value='4'>今天上午</option>";
		atimeoptions += "<option value='0'>今天下午</option>";
		atimeoptions += "<option value='1'>明天上午</option>";
		atimeoptions += "<option value='2'>明天下午</option>";
	} else if(currentH > 8 && currentH < 12) {
		atimeoptions += "<option value='0'>今天下午</option>";
		atimeoptions += "<option value='1'>明天上午</option>";
		atimeoptions += "<option value='2'>明天下午</option>";
		atimeoptions += "<option value='3'>后天上午</option>";
	} else {
		atimeoptions += "<option value='1'>明天上午</option>";
		atimeoptions += "<option value='2'>明天下午</option>";
		atimeoptions += "<option value='3'>后天上午</option>";
		atimeoptions += "<option value='5'>后天下午</option>";
	}
	atimeoptions += "</select>";
	return atimeoptions;
};

var UrNavigator = {
	Chrome: function() {
        return navigator.userAgent.match(/Chrome/i) ? true : false;
    },
    Android: function() {
        return navigator.userAgent.match(/Android/i) ? true : false;
    },
    BlackBerry: function() {
        return navigator.userAgent.match(/BlackBerry/i) ? true : false;
    },
    iOS: function() {
        return navigator.userAgent.match(/iPhone|iPad|iPod/i) ? true : false;
    },
    Windows: function() {
        return navigator.userAgent.match(/IEMobile/i) ? true : false;
    },
    any: function() {
        return !(isMobile.Android() || isMobile.BlackBerry() || isMobile.iOS() || isMobile.Windows());
    }
};

function setCookie(c_name, value){
	var exdate=new Date();
	exdate.setDate(exdate.getDate() + 60*60*24*7);
	document.cookie=c_name+ "=" + escape(value) + ";path=/;expires="+exdate.toGMTString();
}
function getCookie(name)//取cookies函数        
{
    var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
     if(arr != null) return unescape(arr[2]); return null;
}
function delCookie(name)//删除cookie
{
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval=getCookie(name);
    if(cval!=null) document.cookie= name + "="+cval+";expires="+exp.toGMTString();
}