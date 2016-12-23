package bean.apiBean;

/**
 * Created by GreendaMi on 2016/12/20.
 */

public class base_E {


    /**
     * code : 111
     * error : invalid type for key 'phone', expected 'String', but got 'Number'.
     * createdAt : 2016-12-20 14:36:19
     * objectId : f7093017e6
     */

    private int code = 0;
    private String error = "";
    private String createdAt = "";
    private String objectId = "";
    private String updatedAt = "";

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
