//输入提示  
function autoSearch() {   
    var keywords = document.getElementById("locationkeywords").value;  
    if(keywords == '') {
    	document.getElementById("result1").style.display = "none";
    	return;
    }
    var auto;    
    var autoOptions = {  
        pageIndex:1,  
        pageSize:10,  
        city: curCity //城市，默认全国  
    };  
    auto = new AMap.Autocomplete(autoOptions);  
    //查询成功时返回查询结果  
    AMap.event.addListener(auto, "complete", autocomplete_CallBack);  
    auto.search(keywords);  
}  


//输出输入提示结果的回调函数  
function autocomplete_CallBack(data) {  
    var resultStr = "";  
    var tipArr = [];  
    tipArr = data.tips;  
    if (tipArr.length>0) {                    
        for (var i = 0; i < tipArr.length; i++) {  
            resultStr += "<div id='divid" + (i + 1) + "' onmouseover='openMarkerTipById(" + (i + 1)  
                        + ",this)' onclick='selectResult(" + i + ")' onmouseout='onmouseout_MarkerStyle(" + (i + 1)  
                        + ",this)' style=\"font-size: 13px;cursor:pointer;padding:5px 5px 5px 5px;\">" + tipArr[i].name + "<span style='color:#C1C1C1;'>"+ tipArr[i].district + "</span></div>";  
        }  
    }  
    else  {  
        resultStr = " π__π 亲,人家找不到结果!<br />要不试试：<br />1.请确保所有字词拼写正确<br />2.尝试不同的关键字<br />3.尝试更宽泛的关键字";  
    }  
     
    document.getElementById("result1").innerHTML = resultStr;  
    document.getElementById("result1").style.display = "block";  
} 

//输入提示框鼠标滑过时的样式  
function openMarkerTipById(pointid, thiss) {  //根据id打开搜索结果点tip    
    thiss.style.background = '#CAE1FF';  
}  
  
//输入提示框鼠标移出时的样式  
function onmouseout_MarkerStyle(pointid, thiss) {  //鼠标移开后点样式恢复    
    thiss.style.background = "";  
}  


//从输入提示框中选择关键字并查询  
function selectResult(index) {  
    if (navigator.userAgent.indexOf("MSIE") > 0) {  
        document.getElementById("locationkeywords").onpropertychange = null;  
        document.getElementById("locationkeywords").onfocus = focus_callback;  
    }  
    //截取输入提示的关键字部分  
    var text = document.getElementById("divid" + (index + 1)).innerHTML.replace(/<[^>].*?>.*<\/[^>].*?>/g,"");;  
    document.getElementById("locationkeywords").value = text;  
    document.getElementById("result1").style.display = "none";  
    //根据选择的输入提示关键字查询  
    mapObj.plugin(["AMap.PlaceSearch"], function() {          
        var msearch = new AMap.PlaceSearch();  //构造地点查询类  
        AMap.event.addListener(msearch, "complete", placeSearch_CallBack); //查询成功时的回调函数  
        msearch.search(text);  //关键字查询查询  
    });  
} 

//定位选择输入提示关键字  
function focus_callback() {  
    if (navigator.userAgent.indexOf("MSIE") > 0) {  
        document.getElementById("locationkeywords").onpropertychange = autoSearch;  
   }  
}  

//输出关键字查询结果的回调函数  
function placeSearch_CallBack(data) { 
    var poiArr = data.poiList.pois;  
    var resultCount = poiArr.length; 
    if(resultCount > 0) {
    	showLoading("加载中...");
    	//清空地图上的InfoWindow和Marker  
        windowsArr = [];  
        marker     = [];  
        mapObj.clearMap();  
    	addmarker(0, poiArr[0].location); 
    } else {
    	toast("未找到该地址附近活动！");
    }
    mapObj.setFitView();  
}  

//添加查询结果的marker&infowindow      
function addmarker(i, loc) {  
    var lngX = loc.getLng();  
    var latY = loc.getLat();  
    var markerOption = {  
        map:mapObj,  
        icon:"http://webapi.amap.com/images/marker_sprite.png",  
        position:new AMap.LngLat(lngX, latY)  
    };  
    var mar = new AMap.Marker(markerOption);            
    marker.push(new AMap.LngLat(lngX, latY));  
    startGetActivitiesFromService(lngX,latY);
}  
