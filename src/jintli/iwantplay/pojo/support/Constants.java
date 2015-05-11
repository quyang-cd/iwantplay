package jintli.iwantplay.pojo.support;

public class Constants {
	
	private Constants() {}

    //换行
    public static final String CRLF = "\n";

    //自动上报位置后cache的key的后缀：open_id+后缀
    public static final String CACHE_COOR_AUTO_SUFFIX = "_auto";

    //手动上报位置后cache的key的后缀：open_id+后缀
    public static final String CACHE_COOR_MANUAL_SUFFIX = "_manual";
    //public static final String BATH_PATH = "http://iwantplay.ngrok.com/iwantplay/";
    public static final String BATH_PATH = "http://iwantplay.sinaapp.com/";

    //将第三方图片转为本地url

    //欢迎语
    public static final String JD_O2O_WECHAT_WELCOME_MSG = "欢迎光临！\n\ue10f 点击下面+号发送位置，查找周边的活动！\n\ue10f 输入关键字（如‘乒乓球’）搜索相关活动！";
	public static final String ACTIVITY_KEY = "msg_activity_";
	public static final String ACTIVITY_GROUP = "group_activity_";//加入通知
	public static final String ACTIVITY_EXIT_GROUP = "group_activity_exit_";//退出通知
	public static final String ACTIVITY_GROUP_NUM = "group_activity_num_";//退出通知
	public static final String SUFFIX_FOR_WECHAT_LNGLAT = "_latestlnglat";//存储地理位置
	
	public static final String STATIC_MAP_URL = "http://st.map.qq.com/api?size=320*200&center={X},{Y}&format=gif&markers={X},{Y},blue,0";
}
