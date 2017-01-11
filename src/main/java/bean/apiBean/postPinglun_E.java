package bean.apiBean;

/**
 * Created by GreendaMi on 2017/1/11.
 */

public class postPinglun_E {
    private String Content;
    private String ID;
    private String userID;

    public postPinglun_E(String content, String ID, String userID) {
        Content = content;
        this.ID = ID;
        this.userID = userID;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
