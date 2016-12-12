package top.greendami.movielineage;

import android.app.Application;

import model.DownLoadManager;

/**
 * Created by GreendaMi on 2016/12/12.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DownLoadManager.initDownLoadManager(this);
    }
}
