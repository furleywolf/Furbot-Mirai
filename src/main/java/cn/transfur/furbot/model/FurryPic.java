package cn.transfur.furbot.model;

/**
 * Date: 2022/1/28
 * Author: Jmeow
 */
public class FurryPic {

    private int id;
    private String name;
    private String url;
    private String thumb;
    //图片字节流
    private byte[] imageBytes;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getThumb() {
        return thumb;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    @Override
    public String toString() {
        return "FurryPic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", thumb='" + thumb + '\'' +
                '}';
    }
}