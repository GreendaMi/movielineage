package bean;

import java.util.List;

/**
 * Created by GreendaMi on 2016/12/21.
 */

public class likebean {
    private List<ResultsBean> results;

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * comment : 11
         * createdAt : 2016-12-21 11:26:57
         * date : 11
         * from : 11
         * img : 11
         * introduce : 11
         * name : 11
         * objectId : yDJ8ZZZc
         * tag : 11
         * updatedAt : 2016-12-21 11:27:14
         * url : 11
         * phone : 1111
         */

        private String comment;
        private String createdAt;
        private String date;
        private String from;
        private String img;
        private String introduce;
        private String name;
        private String objectId;
        private String tag;
        private String updatedAt;
        private String url;
        private String phone;

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUserphone() {
            return phone;
        }

        public void setUserphone(String phone) {
            this.phone = phone;
        }
    }
}
