package model;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 检索短片
 * Created by GreendaMi on 2017/1/6.
 */

public class searchFilm {

    public List<String> Doget(String keyWord , String page){
        final List<String> result = new ArrayList<>();
        OkHttpClient mOkHttpClient = new OkHttpClient();

        String url = "http://www.xinpianchang.com/search/index/ts-article/kw-" + keyWord + "/t-/page-";

//创建一个Request
        final Request request = new Request.Builder()
                .url( url + page)
                .build();

        try {
            Response response = mOkHttpClient.newCall(request).execute();

            Document doc = Jsoup.parse(new String( response.body().bytes()));

            Elements master = doc.getElementsByClass("exp-title overdot");
            for(Element e : master){
                String s = "http://www.xinpianchang.com" + e.attr("href");
                Log.d("searchFilm", s);
                result.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}
