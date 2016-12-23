package top.greendami.movielineage;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.listener.OnDownloadFileChangeListener;

import java.util.ArrayList;
import java.util.List;

import adapter.DownLoadFilmListAdapter;
import bean.daoBean.localfilmBean;
import butterknife.Bind;
import butterknife.ButterKnife;
import model.DAOManager;
import tool.UI;
import ui.IconFontTextView;

/**
 * Created by GreendaMi on 2016/12/10.
 */

public class DownLoadActivity extends Activity implements View.OnTouchListener {
    @Bind(R.id.backBt)
    IconFontTextView mBackBt;

    List<localfilmBean> localfilmList = new ArrayList<>();
    DownLoadFilmListAdapter mAdapter;
    @Bind(R.id.content)
    RecyclerView mContent;
    LinearLayoutManager layoutManager;
    @Bind(R.id.edit)
    TextView mEdit;
    @Bind(R.id.DOWN)
    TextView mDOWN;
    @Bind(R.id.delete_bt)
    TextView mDeleteBt;
    @Bind(R.id.cancel_bt)
    TextView mCancelBt;
    @Bind(R.id.delete_view)
    LinearLayout mDeleteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_like);
        ButterKnife.bind(this);
        initViews();
        initEvent();
    }

    private void initViews() {
        mDOWN.setText("下载");
        localfilmList = DAOManager.getInstance(this).queryFilmList();

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
        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEdit.getTag() == null || Integer.parseInt(mEdit.getTag().toString()) == 0) {
                    //由正常状态转入删除状态

                    mDeleteView.setVisibility(View.VISIBLE);

                    mEdit.setTag(1);
                    mEdit.setTextColor(getResources().getColor(R.color.FontColor));
                    mEdit.setTextSize(24);
                    mDOWN.setTextSize(20);
                    mDOWN.setTextColor(getResources().getColor(R.color.DarkFontColor));
                    mAdapter.Type = 1;
                    mAdapter.notifyItemRangeChanged(0, localfilmList.size());
                    FileDownloader.pauseAll();

                } else {
                    //由删除状态转入正常状态
                    mEdit.setTag(0);
                    mEdit.setTextColor(getResources().getColor(R.color.DarkFontColor));
                    mEdit.setTextSize(20);
                    mDOWN.setTextSize(24);
                    mDOWN.setTextColor(getResources().getColor(R.color.FontColor));
                    mAdapter.Type = 0;
                    mAdapter.notifyItemRangeChanged(0, localfilmList.size());
                    mDeleteView.setVisibility(View.GONE);
                }
            }
        });

        mCancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEdit.setTag(0);
                mEdit.setTextColor(getResources().getColor(R.color.DarkFontColor));
                mEdit.setTextSize(20);
                mDOWN.setTextSize(24);
                mDOWN.setTextColor(getResources().getColor(R.color.FontColor));
                mAdapter.Type = 0;
                mAdapter.notifyItemRangeChanged(0, localfilmList.size());
                mDeleteView.setVisibility(View.GONE);
            }
        });
        mCancelBt.setOnTouchListener(this);
        mBackBt.setOnTouchListener(this);
        mDeleteBt.setOnTouchListener(this);

        mDeleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (localfilmBean lfb : mAdapter.deleteList) {
                    FileDownloader.delete(lfb.getUrl(), true, null);
                    FileDownloader.delete(lfb.getImg(), true, null);
                    localfilmList.remove(lfb);
                    DAOManager.getInstance(DownLoadActivity.this).deleteDownLoadFilm(lfb);
                    mAdapter.notifyDataSetChanged();
                }
                mCancelBt.callOnClick();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            mEdit.callOnClick();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        UI.enter(this);
    }

    private void back() {
        finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            view.setScaleX(0.95f);
            view.setScaleY(0.95f);
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            view.setScaleX(1f);
            view.setScaleY(1f);
        }
        return false;
    }
}
