package cn.andrewlu.community.entity;

/**
 * Created by lenovo on 2015/9/12.
 */
public class Image {
    private String url;
    public Image(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}