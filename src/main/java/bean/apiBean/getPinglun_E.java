package bean.apiBean;

/**
 * Created by GreendaMi on 2017/1/11.
 */

public class getPinglun_E {
    String url;

    public getPinglun_E(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "{\"ID\":\""+ url+"\"}";
    }
}
