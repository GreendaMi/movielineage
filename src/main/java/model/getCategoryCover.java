package model;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by GreendaMi on 2017/1/6.
 */

public class getCategoryCover {

    public  String Doget(String url){
        String result = "";
        OkHttpClient mOkHttpClient = new OkHttpClient();


//创建一个Request
        final Request request = new Request.Builder()
                .url( url + "1")
                .build();

        try {
            Response response = mOkHttpClient.newCall(request).execute();

            Document doc = Jsoup.parse(new String( response.body().bytes()));

            Elements master = doc.getElementsByClass("film-cover");
            for(Element e : master){
                if(e.attr("src") != null){
                    String s = e.attr("src");
                    Log.d("searchFilm", s);
                    result = s;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
