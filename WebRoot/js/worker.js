function messageHandler(evt){
    var url="/iwantplay/worker/singleactivity";
    var source;
    if(evt.data){
        try{
            source=new EventSource(url);
            source.onopen=function(event){
            	postMessage("连接已经建立，状态号："+this.readyState);
            };
            source.onmessage=function(event){
            	postMessage('客户端收到服务器推送的数据是：'+event.data);
            };
            source.onerror=function(event){
            	postMessage("出错，信息状态号是："+this.readyState);
            };
        }catch(err){
            postMessage('出错啦，错误信息：'+err.message);
        }
    }else{
    	postMessage('已经退出！');
        source.close();
        source=null;
    }
}
self.addEventListener('message',messageHandler,false);