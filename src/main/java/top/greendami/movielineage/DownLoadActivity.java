package top.greendami.movielineage;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.listener.OnDownloadFileChangeListener;

import java.util.List;

import adapter.DownLoadFilmListAdapter;
import bean.localfilmBean;
import butterknife.Bind;
import butterknife.ButterKnife;
import tool.ACache;
import tool.UI;
import ui.IconFontTextView;

/**
 * Created by GreendaMi on 2016/12/10.
 */

public class DownLoadActivity extends Activity {
    @Bind(R.id.backBt)
    IconFontTextView mBackBt;

    List<localfilmBean> localfilmList;
    DownLoadFilmListAdapter mAdapter;
    @Bind(R.id.content)
    RecyclerView mContent;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ButterKnife.bind(this);
//        Glide.with(this).load("http://cs.xinpianchang.com/uploadfile/article/2016/12/10/584b941aa17ca.jpeg").asBitmap().diskCacheStrategy(DiskCacheStrategy.RESULT).into(mImg);


        initViews();
        initEvent();
    }

    private void initViews() {
        ACache mCache = ACache.get(UI.TopActivity);
        if (mCache.getAsString("films") != null) {
            localfilmList = new Gson().fromJson(mCache.getAsString("films"), new TypeToken<List<localfilmBean>>() {
            }.getType());
        }
        mAdapter = new DownLoadFilmListAdapter(this, localfilmList);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.generateLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        mContent.setLayoutManager(layoutManager);
        mContent.setItemAnimator(new DefaultItemAnimator());
        mContent.setAdapter(mAdapter);
    }

    private void initEvent() {


        //注册文件数据变化监听器，监听比如文件不存在了，被删除了，状态变化了等任何与文件数据变化相关都会收到通知
        OnDownloadFileChangeListener mOnDownloadFileChangeListener = new OnDownloadFileChangeListener() {
            @Override
            public void onDownloadFileCreated(DownloadFileInfo downloadFileInfo) {
                // 一个新下载文件被创建，也许你需要同步你自己的数据存储，比如在你的业务数据库中增加一条记录
            }

            @Override
            public void onDownloadFileUpdated(DownloadFileInfo downloadFileInfo, Type type) {
                // 一个下载文件被更新，也许你需要同步你自己的数据存储，比如在你的业务数据库中更新一条记录
            }

            @Override
            public void onDownloadFileDeleted(DownloadFileInfo downloadFileInfo) {
                // 一个下载文件被删除，也许你需要同步你自己的数据存储，比如在你的业务数据库中删除一条记录
            }
        };
        FileDownloader.registerDownloadFileChangeListener(mOnDownloadFileChangeListener);

        FileDownloader.registerDownloadStatusListener(mAdapter);

        mBackBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
        }
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        UI.enter(this);
    }

    private void back(){
        finish();
        overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
    }
}
