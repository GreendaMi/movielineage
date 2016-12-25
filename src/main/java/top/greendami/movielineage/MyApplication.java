package top.greendami.movielineage;

import android.app.Application;

import cn.smssdk.SMSSDK;
import model.DAOManager;
import model.DownLoadManager;

/**
 * Created by GreendaMi on 2016/12/12.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        LeakCanary.install(this);
        DownLoadManager.initDownLoadManager(this);
        SMSSDK.initSDK(this, "1a0dd72bac150", "ab46d9ecc832e63195ea777215136989");
        DAOManager.getInstance(this);

    }
}
