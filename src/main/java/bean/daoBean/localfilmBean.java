package bean.daoBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import bean.filmBean;

/**
 * Created by GreendaMi on 2016/12/12.
 */

@Entity
public class localfilmBean extends filmBean {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String ID;
    String path;
    String isNew;
    String buildDate;
    String name;
    String from;
    String url;
    String introduce;
    String img;
    String date;
    String tag;
    String comment;
    @Generated(hash = 138061500)
    public localfilmBean(Long id, @NotNull String ID, String path, String isNew,
            String buildDate, String name, String from, String url,
            String introduce, String img, String date, String tag, String comment) {
        this.id = id;
        this.ID = ID;
        this.path = path;
        this.isNew = isNew;
        this.buildDate = buildDate;
        this.name = name;
        this.from = from;
        this.url = url;
        this.introduce = introduce;
        this.img = img;
        this.date = date;
        this.tag = tag;
        this.comment = comment;
    }
    @Generated(hash = 1012278125)
    public localfilmBean() {
    }
    public String getID() {
        return this.ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }
    public String getPath() {
        return this.path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getIsNew() {
        return this.isNew;
    }
    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }
    public String getBuildDate() {
        return this.buildDate;
    }
    public void setBuildDate(String buildDate) {
        this.buildDate = buildDate;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getFrom() {
        return this.from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getIntroduce() {
        return this.introduce;
    }
    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }
    public String getImg() {
        return this.img;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTag() {
        return this.tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public String getComment() {
        return this.comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
