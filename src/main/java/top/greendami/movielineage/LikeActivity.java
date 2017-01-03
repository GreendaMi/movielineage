package top.greendami.movielineage;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.wlf.filedownloader.FileDownloader;

import java.util.ArrayList;
import java.util.List;

import adapter.LikeFilmListAdapter;
import bean.daoBean.likefilmbean;
import butterknife.Bind;
import butterknife.ButterKnife;
import model.DAOManager;
import tool.UI;
import ui.IconFontTextView;

/**
 * Created by GreendaMi on 2016/12/19.
 */

public class LikeActivity extends Activity implements View.OnTouchListener{
    @Bind(R.id.backBt)
    IconFontTextView mBackBt;
    @Bind(R.id.DOWN)
    TextView mDOWN;
    @Bind(R.id.edit)
    TextView mEdit;
    @Bind(R.id.title)
    RelativeLayout mTitle;
    @Bind(R.id.content)
    RecyclerView mContent;
    @Bind(R.id.delete_bt)
    TextView mDeleteBt;
    @Bind(R.id.cancel_bt)
    TextView mCancelBt;
    @Bind(R.id.delete_view)
    LinearLayout mDeleteView;

    List<likefilmbean> likefilmList = new ArrayList<>();
    LikeFilmListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_like);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UI.enter(this);
        initViews();
        initEvent();
    }

    private void initEvent() {
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
                    mAdapter.notifyItemRangeChanged(0, likefilmList.size());
                    FileDownloader.pauseAll();

                } else {
                    //由删除状态转入正常状态
                    mEdit.setTag(0);
                    mEdit.setTextColor(getResources().getColor(R.color.DarkFontColor));
                    mEdit.setTextSize(20);
                    mDOWN.setTextSize(24);
                    mDOWN.setTextColor(getResources().getColor(R.color.FontColor));
                    mAdapter.Type = 0;
                    mAdapter.notifyItemRangeChanged(0, likefilmList.size());
                    mDeleteView.setVisibility(View.GONE);
                }
            }
        });

        mDeleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (likefilmbean lfb : mAdapter.deleteList) {
                    DAOManager.getInstance(LikeActivity.this).deleteLikeFilm(lfb);
                    likefilmList.remove(lfb);
                    mAdapter.notifyDataSetChanged();
                }
                mCancelBt.callOnClick();
            }
        });
        mDeleteBt.setOnTouchListener(this);
        mCancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEdit.setTag(0);
                mEdit.setTextColor(getResources().getColor(R.color.DarkFontColor));
                mEdit.setTextSize(20);
                mDOWN.setTextSize(24);
                mDOWN.setTextColor(getResources().getColor(R.color.FontColor));
                mAdapter.Type = 0;
                mAdapter.notifyItemRangeChanged(0, likefilmList.size());
                mDeleteView.setVisibility(View.GONE);
            }
        });
        mCancelBt.setOnTouchListener(this);
    }

    private void initViews() {
        mDOWN.setText("收藏");
        likefilmList = DAOManager.getInstance(this).queryLikeFilmList();

        mAdapter = new LikeFilmListAdapter(this,likefilmList);
        GridLayoutManager mgr=new GridLayoutManager(this,2);
        mContent.setLayoutManager(mgr);
        mContent.setItemAnimator(new DefaultItemAnimator());
        mContent.setAdapter(mAdapter);
    }

    private void back() {
        finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
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
