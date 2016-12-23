package bean.apiBean;

/**
 * Created by GreendaMi on 2016/12/21.
 */

public class oneLike_E {
    String phone;
    String url;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "{\"phone\":\""+ phone+"\"}";
    }
}
