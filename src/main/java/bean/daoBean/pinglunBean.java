package bean.daoBean;

import java.util.List;

/**
 * Created by GreendaMi on 2017/1/11.
 */

public class pinglunBean {

    private List<ResultsBean> results;

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * Content : HAO
         * ID : http://video.xinpianchang.com/57c000e4b4f68.mp4
         * createdAt : 2017-01-11 15:36:29
         * isVisiable : 1
         * objectId : s4SVJJJM
         * updatedAt : 2017-01-11 15:56:02
         * userID : 12333
         */

        private String Content;
        private String ID;
        private String createdAt;
        private String isVisiable;
        private String objectId;
        private String updatedAt;
        private String userID;

        public String getContent() {
            return Content;
        }

        public void setContent(String Content) {
            this.Content = Content;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getIsVisiable() {
            return isVisiable;
        }

        public void setIsVisiable(String isVisiable) {
            this.isVisiable = isVisiable;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }
    }
}
