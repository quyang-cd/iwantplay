package jintli.iwantplay.pojo.response;
import java.util.List;

/**
 * 文本消息
 * Created by IntelliJ IDEA.
 * User: zhangkuan
 * Date: 14-4-1
 * Time: 下午2:15
 */
public class NewsMessageResponse {
    // 图文消息个数，限制为10条以内
     private int articleCount;
    // 多条图文消息信息，默认第一个item为大图
    private List<Article> articles;

    public int getArticleCount() {
        return this.articleCount;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
    }

    public List<Article> getArticles() {
        return this.articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
