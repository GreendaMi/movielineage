package top.greendami.movielineage;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import ui.RefreshLayout;
import ui.SystemDialog;

/**
 * Created by GreendaMi on 2016/12/1.
 */

public class FilmFragment extends Fragment {
    @Bind(R.id.rc)
    RecyclerView mRc;
    @Bind(R.id.Refresh_layout)
    RefreshLayout mRefreshLayout;

    List<filmBean> mDatas;
    FilmListAdapter mAdapter;
    int PAGE = 1;
    Handler mHandler;
    LinearLayoutManager layoutManager;

    boolean isHidden = true;
    boolean isLoading = false;

    int type = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_fragment, container, false);
        ButterKnife.bind(this, view);
        InitView();
        InitEvent();
        mHandler = new Handler();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden = hidden;
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
                        SystemDialog.showLoadingDialog(getActivity(),false);
                        LoadData(PAGE);
                        mRefreshLayout.setRefreshing(false);
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
//                    SystemDialog.showLoadingDialog(getActivity(),false);
                    ((MainActivity)getActivity()).showLoadingBar();
                    LoadData(++PAGE);
                }
            }
        });
    }

    private void InitView() {
        initData();
        mAdapter = new FilmListAdapter(getActivity(),mDatas);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.generateLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        mRc.setLayoutManager(layoutManager);
        mRc.setItemAnimator(new DefaultItemAnimator());
        mRc.setAdapter(mAdapter);
        mRefreshLayout.setClickable(false);
    }

    public void LoadData(final int page){
        if(isLoading){
            //如果正在加载，那么这次加载不响应
            PAGE--;
            return;
        }
        if(NetworkTypeInfo.getNetworkType(getActivity()) == NetworkType.NoNetwork){
            UI.Toast("请连接网络！");
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                isLoading = true;
                List<String> urls = new getPageList().Doget(type , page+"");

                for (final String url :urls) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            filmBean f = new getFilm().Doget(url);
                            if(f.getImg()!=null && !f.getImg().isEmpty() && f.getUrl()!=null && !f.getUrl().isEmpty()){
                                mDatas.add(new getFilm().Doget(url));

                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAdapter.notifyDataSetChanged();
                                        SystemDialog.dismissLoadingDialog();
                                        ((MainActivity)getActivity()).dismissLoadingBar();
                                        isLoading = false;
                                    }
                                });
                            }

                        }
                    }).start();

                }


//                Observable.from(new getPageList().Doget(type , page+""))
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
//                            public void onNext(filmBean filmBean) {
//                                filmBean.setType(2);
//                                mDatas.add(filmBean);
//
//                                mHandler.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        mAdapter.notifyDataSetChanged();
//                                        SystemDialog.dismissLoadingDialog();
//                                    }
//                                });
//                            }
//                        });

            }
        }).start();
    }

    private void initData() {
        mDatas = new ArrayList<>();
        SystemDialog.showLoadingDialog(getActivity(),false);
        LoadData(PAGE);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}

