package tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.filmBean;
import model.getFilm;
import model.getPageList;

/**
 * Created by GreendaMi on 2017/1/10.
 */

//保存信息
public class DataController {

    static Map<String,List<filmBean>> newPages = new HashMap<>();
    static Map<String,List<filmBean>> hotPages = new HashMap<>();


    //对外接口，异步调用
    public static List<filmBean> getDatasByPage(int type,int page){
        LoadData(type,page + 1);
        if(type == 0){
            return newPages.containsKey(page + "") ? newPages.get(page + "") : null;
        }else{
            return hotPages.containsKey(page + "") ? hotPages.get(page + "") : null;
        }
    }


    //对外接口，后台主动加载
    public static void LoadData(final int type,final int page){
        final List<filmBean> mDatas = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<String> urls = new getPageList().Doget(type , page+"");
                if(urls.size() == 0){
                    UI.Toast("没有了^_^");
                    return;
                }
                for (final String url :urls) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            filmBean f = new getFilm().Doget(url);
                            if(f.getImg()!=null && !f.getImg().isEmpty() && f.getUrl()!=null && !f.getUrl().isEmpty()){
                                mDatas.add(new getFilm().Doget(url));

                                if(mDatas.size() == urls.size()){
                                    if(type == 0){
                                        newPages.put(page+"",mDatas);
                                    }else{
                                        hotPages.put(page+"",mDatas);
                                    }
                                }

                            }

                        }
                    }).start();

                }

            }
        }).start();
    }


    private static List<filmBean> getDate(int type, int page) {
        final List<filmBean> mDatas = new ArrayList<>();
        final List<String> urls = new getPageList().Doget(type , page+"");
        if(urls.size() == 0){
            UI.Toast("没有了^_^");
            return null;
        }
        for (final String url :urls) {
            filmBean f = new getFilm().Doget(url);
            if(f.getImg()!=null && !f.getImg().isEmpty() && f.getUrl()!=null && !f.getUrl().isEmpty()){
                mDatas.add(f);

                if(mDatas.size() == urls.size()){
                    if(type == 0){
                        newPages.put(page+"",mDatas);
                    }else{
                        hotPages.put(page+"",mDatas);
                    }
                }

            }
        }
        return mDatas;
    }
}
