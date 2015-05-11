package jintli.iwantplay.pojo.response;

/**
 * 音乐
 * Created by IntelliJ IDEA.
 * User: zhangkuan
 * Date: 14-4-1
 * Time: 下午2:13
 */
public class Music {
    // 音乐名称
    private String title;
    // 音乐描述
    private String description;
    // 音乐链接
    private String musicUrl;
    // 高质量音乐链接，WIFI环境优先使用该链接播放音乐
    private String hQMusicUrl;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMusicUrl() {
        return this.musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public String getHQMusicUrl() {
        return this.hQMusicUrl;
    }

    public void setHQMusicUrl(String HQMusicUrl) {
        this.hQMusicUrl = HQMusicUrl;
    }
}
