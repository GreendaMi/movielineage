package top.greendami.movielineage;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import adapter.FilmListAdapter;
import bean.filmBean;
import butterknife.Bind;
import butterknife.ButterKnife;
import model.getFilm;
import model.getPageList;
import tool.NetworkType;
import tool.NetworkTypeInfo;
import tool.UI;
import ui.ChTextView;
import ui.DotsPreloader;
import ui.IconFontTextView;
import ui.RefreshLayout;
import ui.SystemDialog;

/**
 * Created by GreendaMi on 2016/12/23.
 */

public class CategoryActivity extends Activity {

    @Bind(R.id.backBt)
    IconFontTextView mBackBt;
    @Bind(R.id.title_text)
    ChTextView mTitleText;
    @Bind(R.id.rc)
    RecyclerView mRc;
    @Bind(R.id.Refresh_layout)
    RefreshLayout mRefreshLayout;

    List<filmBean> mDatas;
    FilmListAdapter mAdapter;
    int PAGE = 1;
    Handler mHandler;
    LinearLayoutManager layoutManager;
    @Bind(R.id.DotsPreloader)
    DotsPreloader mDotsPreloader;
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        InitView();
        InitEvent();
    }

    private void InitEvent() {
        // 设置下拉刷新监听器
        mRefreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                mRefreshLayout.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        PAGE = 1;
                        mDatas.clear();
                        mAdapter.notifyDataSetChanged();
                        SystemDialog.showLoadingDialog(CategoryActivity.this, false);
                        mRefreshLayout.setRefreshing(false);
                        LoadData(PAGE);
                    }
                }, 0);
            }
        });


        mRc.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == mAdapter.getItemCount()) {
//                    SystemDialog.showLoadingDialog(CategoryActivity.this, false);
                    if(mDotsPreloader.getVisibility() == View.INVISIBLE){
                        mDotsPreloader.setVisibility(View.VISIBLE);
                    }
                    LoadData(++PAGE);
                }
            }
        });

        mBackBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        mBackBt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setScaleX(0.95f);
                    v.setScaleY(0.95f);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setScaleX(1f);
                    v.setScaleY(1f);
                }
                return false;
            }
        });
    }

    private void InitView() {
        mHandler = new Handler();
        mTitleText.setText(UI.getData(0).toString());
        mDatas = new ArrayList<>();
        SystemDialog.showLoadingDialog(CategoryActivity.this, false);
        mAdapter = new FilmListAdapter(this, mDatas);
        layoutManager = new LinearLayoutManager(CategoryActivity.this);
        layoutManager.generateLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        mRc.setLayoutManager(layoutManager);
        mRc.setItemAnimator(new DefaultItemAnimator());
        mRc.setAdapter(mAdapter);
        mRefreshLayout.setClickable(false);
        LoadData(PAGE);
    }


    public void LoadData(final int page) {
        if(isLoading){
            //如果正在加载，那么这次加载不响应
            PAGE--;
            return;
        }
        if(NetworkTypeInfo.getNetworkType(CategoryActivity.this) == NetworkType.NoNetwork){
            UI.Toast("请连接网络！");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (UI.getData(1) instanceof String) {

                } else {
                    return;
                }
                String baseurl = (String) (UI.getData(1));
                isLoading = true;
                List<String> urls = new getPageList().DogetByurl(baseurl, page + "");

                for (final String url : urls) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            filmBean f = new getFilm().Doget(url);
                            if (f.getImg() != null && !f.getImg().isEmpty() && f.getUrl() != null && !f.getUrl().isEmpty()) {
                                mDatas.add(new getFilm().Doget(url));

                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAdapter.notifyDataSetChanged();
                                        SystemDialog.dismissLoadingDialog();
                                        if(mDotsPreloader.getVisibility() == View.VISIBLE){
                                            mDotsPreloader.setVisibility(View.INVISIBLE);
                                        }
                                        isLoading = false;
                                    }
                                });
                            }
                        }
                    }).start();

                }


//                Observable.from(new getPageList().DogetByurl((String)(UI.getData(1)), page + ""))
//                        .map(new Func1<String, filmBean>() {
//
//                            @Override
//                            public filmBean call(String s) {
//                                return new getFilm().Doget(s);
//                            }
//                        })
//                        .filter(new Func1<filmBean, Boolean>() {
//                            @Override
//                            public Boolean call(filmBean filmBean) {
//                                return (null != filmBean.getUrl() && null != filmBean.getImg() && !filmBean.getUrl().isEmpty() && !filmBean.getImg().isEmpty());
//                            }
//                        })
//                        .subscribe(new Observer<filmBean>() {
//                            @Override
//                            public void onCompleted() {
//
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//
//                            }
//
//                            @Override
//                            public void onNext(final filmBean filmBean) {
//
//                                mHandler.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Log.d("CategoryActivity", "System.currentTimeMillis(next):" + System.currentTimeMillis());
//                                        mDatas.add(filmBean);
//                                        mAdapter.notifyItemChanged(mDatas.size() - 1);
//                                        SystemDialog.dismissLoadingDialog();
//                                    }
//                                });
//                            }
//                        });

            }
        }).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
        }
        return true;
    }

    private void back() {
        UI.pop();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UI.enter(this);
    }
}