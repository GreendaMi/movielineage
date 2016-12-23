package bean.daoBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by GreendaMi on 2016/12/18.
 */
@Entity
public class likefilmbean {
    @Id(autoincrement = true)
    private Long id;
    String name;
    String from;
    @NotNull
    String url;
    String introduce;
    String img;
    String date;
    String tag;
    String comment;
    String bmobID;
    @Generated(hash = 563947787)
    public likefilmbean(Long id, String name, String from, @NotNull String url,
            String introduce, String img, String date, String tag, String comment,
            String bmobID) {
        this.id = id;
        this.name = name;
        this.from = from;
        this.url = url;
        this.introduce = introduce;
        this.img = img;
        this.date = date;
        this.tag = tag;
        this.comment = comment;
        this.bmobID = bmobID;
    }
    @Generated(hash = 847582815)
    public likefilmbean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public String getBmobID() {
        return this.bmobID;
    }
    public void setBmobID(String bmobID) {
        this.bmobID = bmobID;
    }
}
