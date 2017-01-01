package model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import bean.filmBean;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by GreendaMi on 2016/11/29.
 */

public class getFilm {

    public filmBean Doget(String url) {
        final filmBean f = new filmBean();
        OkHttpClient mOkHttpClient = new OkHttpClient();
//创建一个Request
        final Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = mOkHttpClient.newCall(request).execute();

            Document doc = Jsoup.parse(new String(response.body().bytes()));

            String from = null;
            if (doc.getElementsByClass("useer-name") != null && doc.getElementsByClass("useer-name").select("a") != null && doc.getElementsByClass("useer-name").select("a").first() != null) {
                from = doc.getElementsByClass("useer-name").select("a").first().text();
            }

            String tag = null;
            if (doc.getElementsByClass("clearfix film_intro") != null && doc.getElementsByClass("clearfix film_intro").select("a") != null && doc.getElementsByClass("clearfix film_intro").select("a").first() != null) {
                tag = doc.getElementsByClass("clearfix film_intro").select("a").first().text();
            }

            String name = null;
            if (doc.getElementsByClass("active") != null && doc.getElementsByClass("active").text() != null) {
                name = doc.getElementsByClass("active").text();
            }

            String filmurl = doc.getElementsByClass("video-js").attr("abs:src");

            String img = doc.getElementsByClass("video-js").attr("abs:poster");

            String date = null;
            if (doc.getElementsByClass("inline-mid") != null) {
                date = doc.getElementsByClass("inline-mid").text();
            }

            String introduce = null;
            if (doc.getElementsByClass("clearfix film_intro") != null && doc.getElementsByClass("clearfix film_intro").first() != null && doc.getElementsByClass("clearfix film_intro").first().select("a") != null && doc.getElementsByClass("clearfix film_intro").first().select("a").get(1) != null) {
                introduce = doc.getElementsByClass("clearfix film_intro").first().select("a").get(1).text();
            }

            String comment = null;
            if (doc.getElementsByClass("intro_119 clearfix") != null && doc.getElementsByClass("intro_119 clearfix").select("p") != null) {
                comment = doc.getElementsByClass("intro_119 clearfix").select("p").text();
            }

            f.setImg(img.replace("@960w.jpg", ""));
            f.setName(name);
            f.setFrom(from);
            f.setTag(tag);
            f.setUrl(filmurl);
            f.setDate(date);
            f.setIntroduce(introduce);
            f.setComment(comment);
            f.setNum(new getFilmViewsNumber().Doget(url));
//            Log.d("getFilm", name + "," + filmurl+ "," + img.replace("@960w.jpg",""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }
}
