package model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by GreendaMi on 2016/12/16.
 */

public class getFilmViewsNumber {

    public String Doget(String url){
        String Result = "";
        url = url.replace("http://www.xinpianchang.com/a","");

        String baseUrl = "http://www.xinpianchang.com/index.php?app=article&ac=filmplay&ts=plat_views&id=" + url.split("[?]")[0];
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url(baseUrl)
                .build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            JSONObject jsonObject = new JSONObject(new String( response.body().bytes()));
            Result = jsonObject.optString("views");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
        return Result;
    }
}
