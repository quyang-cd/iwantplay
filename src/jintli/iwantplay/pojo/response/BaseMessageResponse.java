package jintli.iwantplay.pojo.response;

/**
 * 消息基类（公众帐号 -> 普通用户）
 * Created by IntelliJ IDEA.
 * User: zhangkuan
 * Date: 14-4-1
 * Time: 下午2:02
 */
public class BaseMessageResponse {

    // 接收方帐号（收到的OpenID）
    private String toUserName;
    // 开发者微信号
    private String fromUserName;
    // 消息创建时间 （整型）
    private long createTime;
    // 消息类型（text/music/news）
    private String msgType;
    // 位0x0001被标志时，星标刚收到的消息
    private int funcFlag;

    public String getToUserName() {
        return this.toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return this.fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return this.msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public int getFuncFlag() {
        return this.funcFlag;
    }

    public void setFuncFlag(int funcFlag) {
        this.funcFlag = funcFlag;
    }
}
