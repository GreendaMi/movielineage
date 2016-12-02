package ui;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import tool.ViewExtensions;
import top.greendami.movielineage.R;


/**
 * 动态加载等待的小点点界面
 * Created by zhaopy on 2015/11/18.
 */
public class NetworkView extends FrameLayout {

    public NetworkView(Context context) {
        super(context);

        initialize();
    }

    private DotsPreloader mDotsPreloader;

    private void initialize() {

        ViewExtensions.loadLayout(this, R.layout.network_view);

        mDotsPreloader = (DotsPreloader) findViewById(R.id.dotView);

    }

    public void start() {
        mDotsPreloader.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public void stop() {
        mDotsPreloader.stop();
    }
}
