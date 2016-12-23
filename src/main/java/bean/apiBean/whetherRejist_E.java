package bean.apiBean;

/**
 * Created by GreendaMi on 2016/12/20.
 */

public class whetherRejist_E {
    String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public whetherRejist_E(String phone) {
        this.phone = phone;
    }

    public whetherRejist_E() {
    }

    @Override
    public String toString() {
        return "{\"phone\":\""+ phone+"\"}";
    }
}
