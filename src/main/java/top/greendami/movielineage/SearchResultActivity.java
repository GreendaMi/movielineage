package top.greendami.movielineage;

import android.view.View;

import java.util.List;

import bean.filmBean;
import model.getFilm;
import model.searchFilm;
import tool.NetworkType;
import tool.NetworkTypeInfo;
import tool.UI;
import ui.SystemDialog;

/**
 * 关键字检索结果展示
 * Created by GreendaMi on 2017/1/6.
 */

public class SearchResultActivity extends CategoryActivity {


    String keyWord = "";
    @Override
    public void InitView() {
        keyWord = UI.getData().toString();
        super.InitView();
        mTitleText.setText("9点片场");
    }

    @Override
    public void LoadData(final int page) {
        if(isLoading){
            //如果正在加载，那么这次加载不响应
            PAGE--;
            return;
        }
        if(NetworkTypeInfo.getNetworkType(SearchResultActivity.this) == NetworkType.NoNetwork){
            UI.Toast("请连接网络！");
            SystemDialog.dismissLoadingDialog();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                isLoading = true;
                List<String> urls = new searchFilm().Doget(keyWord,page + "");

                if(urls.size() == 0){
                    UI.Toast("没有了^_^");
                    SystemDialog.dismissLoadingDialog();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(mDotsPreloader.getVisibility() == View.VISIBLE){
                                mDotsPreloader.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                    isLoading = false;
                    return;
                }
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
            }
        }).start();
    }
}
