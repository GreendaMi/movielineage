package top.greendami.movielineage;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import tool.NetworkType;
import tool.NetworkTypeInfo;
import tool.UI;

/**
 * Created by GreendaMi on 2016/12/19.
 */

public class WelcomeActivity extends Activity {
    Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        mHandler = new Handler();

    }

    @Override
    protected void onResume() {
        super.onResume();
        UI.enter(this);
        initData();
    }

    private void initData() {
        if(NetworkTypeInfo.getNetworkType(WelcomeActivity.this) == NetworkType.NoNetwork){
            UI.push(DownLoadActivity.class);
            WelcomeActivity.this.finish();
        }else{
//            new Thread(){
//                @Override
//                public void run() {
//                    DataController.getDatasByPage(0,1);
//                }
//            }.start();
//            new Thread(){
//                @Override
//                public void run() {
//                    DataController.getDatasByPage(1,1);
//                }
//            }.start();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    UI.push(MainActivity.class);
                    WelcomeActivity.this.finish();
                }
            },0);

        }

    }
}
