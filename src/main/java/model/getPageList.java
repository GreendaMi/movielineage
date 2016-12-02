package model;


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
 * Created by GreendaMi on 2016/11/29.
 */

public class getPageList {

    String url;

    public List<String> Doget(int type , String page){
        final List<String> result = new ArrayList<>();
        OkHttpClient mOkHttpClient = new OkHttpClient();

        switch (type){
            case 0: url = "http://www.xinpianchang.com/channel/index/type-0/sort-addtime/page-";break;//最新
            case 1: url = "http://www.xinpianchang.com/channel/index/type-0/sort-like/page-";break;//热门
            case 2: url = "http://www.xinpianchang.com/channel/index/type-0/sort-new_comment/page-";break;//热议
        }

//创建一个Request
        final Request request = new Request.Builder()
                .url( url + page)
                .build();

        try {
            Response response = mOkHttpClient.newCall(request).execute();

            Document doc = Jsoup.parse(new String( response.body().bytes()));

            Elements master = doc.select("div.master-type-intro");
            for(Element e : master){
//                Log.d("getPageList", e.attr("onclick").replace("window.open('","").replace("','_blank')",""));
                String s = e.attr("onclick").replace("window.open('","").replace("','_blank')","");
                result.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
