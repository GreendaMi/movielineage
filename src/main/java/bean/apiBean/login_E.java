package bean.apiBean;

/**
 * Created by GreendaMi on 2016/12/20.
 */

public class login_E {
    String LastLoginTime;

    public String getLastLoginTime() {
        return LastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        LastLoginTime = lastLoginTime;
    }

    public login_E(String lastLoginTime) {
        LastLoginTime = lastLoginTime;
    }
}
