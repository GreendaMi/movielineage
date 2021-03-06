package top.greendami.movielineage;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import model.getCategoryCover;
import tool.UI;

/**
 * Created by GreendaMi on 2016/12/1.
 */

public class CategoryFragment extends Fragment implements View.OnClickListener {
    @Bind(R.id.A1)
    RelativeLayout mA1;
    @Bind(R.id.A2)
    RelativeLayout mA2;
    @Bind(R.id.A3)
    RelativeLayout mA3;
    @Bind(R.id.B1)
    RelativeLayout mB1;
    @Bind(R.id.B2)
    RelativeLayout mB2;
    @Bind(R.id.B3)
    RelativeLayout mB3;
    @Bind(R.id.C1)
    RelativeLayout mC1;
    @Bind(R.id.C2)
    RelativeLayout mC2;
    @Bind(R.id.C3)
    RelativeLayout mC3;
    @Bind(R.id.D1)
    RelativeLayout mD1;
    @Bind(R.id.D2)
    RelativeLayout mD2;
    @Bind(R.id.D3)
    RelativeLayout mD3;

    List<RelativeLayout> ViewsList = new ArrayList<>();
    Map<String,ImageView> UrlImageList = new HashMap<>();
    Handler mHandler;
    @Bind(R.id.image_A1)
    ImageView imageA1;
    @Bind(R.id.image_A2)
    ImageView imageA2;
    @Bind(R.id.image_A3)
    ImageView imageA3;
    @Bind(R.id.image_B1)
    ImageView imageB1;
    @Bind(R.id.image_B2)
    ImageView imageB2;
    @Bind(R.id.image_B3)
    ImageView imageB3;
    @Bind(R.id.image_C1)
    ImageView imageC1;
    @Bind(R.id.image_C2)
    ImageView imageC2;
    @Bind(R.id.image_C3)
    ImageView imageC3;
    @Bind(R.id.image_D1)
    ImageView imageD1;
    @Bind(R.id.image_D2)
    ImageView imageD2;
    @Bind(R.id.image_D3)
    ImageView imageD3;

    boolean firsrFlg = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tag, container, false);
        ButterKnife.bind(this, view);
        initViews();
        InitEvent();
        mHandler = new Handler();
        return view;
    }

    private void initViews() {
        ViewsList.add(mA1);
        ViewsList.add(mA2);
        ViewsList.add(mA3);
        ViewsList.add(mB1);
        ViewsList.add(mB2);
        ViewsList.add(mB3);
        ViewsList.add(mC1);
        ViewsList.add(mC2);
        ViewsList.add(mC3);
        ViewsList.add(mD1);
        ViewsList.add(mD2);
        ViewsList.add(mD3);

        UrlImageList.put("http://www.xinpianchang.com/channel/index/id-1/sort-like/page-",imageA1);
        UrlImageList.put("http://www.xinpianchang.com/channel/index/id-121/sort-like/page-",imageA2);
        UrlImageList.put("http://www.xinpianchang.com/channel/index/id-2/sort-like/page-",imageA3);
        UrlImageList.put("http://www.xinpianchang.com/channel/index/id-13/sort-like/page-",imageB1);
        UrlImageList.put("http://www.xinpianchang.com/channel/index/id-11/sort-like/page-",imageB2);
        UrlImageList.put("http://www.xinpianchang.com/channel/index/id-3/sort-like/page-",imageB3);
        UrlImageList.put("http://www.xinpianchang.com/channel/index/id-10/sort-like/page-",imageC1);
        UrlImageList.put("http://www.xinpianchang.com/channel/index/id-12/sort-like/page-",imageC2);
        UrlImageList.put("http://www.xinpianchang.com/channel/index/id-7/sort-like/page-",imageC3);
        UrlImageList.put("http://www.xinpianchang.com/channel/index/id-5/sort-like/page-",imageD1);
        UrlImageList.put("http://www.xinpianchang.com/channel/index/id-9/sort-like/page-",imageD2);
        UrlImageList.put("http://www.xinpianchang.com/channel/index/type-newera/sort-like/page-",imageD3);

    }

    //设置背景
    public void SetBagrounds() {
        //只加载一次
        if(!firsrFlg){
            return;
        }
        for(final String k: UrlImageList.keySet()){
            firsrFlg = false;
            new Thread(){
                @Override
                public void run() {
                    final ImageView img = UrlImageList.get(k);
                    final String url = new getCategoryCover().Doget(k);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(getActivity()).load(url).asBitmap().diskCacheStrategy(DiskCacheStrategy.RESULT).into(img);
                        }
                    });

                }
            }.start();
        }

        
    }

    private void InitEvent() {
        for (RelativeLayout rl : ViewsList) {
            rl.setOnClickListener(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.A1:
                UI.push(CategoryActivity.class, "剧情短片", "http://www.xinpianchang.com/channel/index/id-1/sort-like/page-");
                break;
            case R.id.A2:
                UI.push(CategoryActivity.class, "网络电影", "http://www.xinpianchang.com/channel/index/id-121/sort-like/page-");
                break;
            case R.id.A3:
                UI.push(CategoryActivity.class, "广告/宣传片", "http://www.xinpianchang.com/channel/index/id-2/sort-like/page-");
                break;
            case R.id.B1:
                UI.push(CategoryActivity.class, "创意实验", "http://www.xinpianchang.com/channel/index/id-13/sort-like/page-");
                break;
            case R.id.B2:
                UI.push(CategoryActivity.class, "纪录片", "http://www.xinpianchang.com/channel/index/id-11/sort-like/page-");
                break;
            case R.id.B3:
                UI.push(CategoryActivity.class, "MV", "http://www.xinpianchang.com/channel/index/id-3/sort-like/page-");
                break;
            case R.id.C1:
                UI.push(CategoryActivity.class, "混剪/配音", "http://www.xinpianchang.com/channel/index/id-10/sort-like/page-");
                break;
            case R.id.C2:
                UI.push(CategoryActivity.class, "特殊摄影", "http://www.xinpianchang.com/channel/index/id-12/sort-like/page-");
                break;
            case R.id.C3:
                UI.push(CategoryActivity.class, "栏目/网剧", "http://www.xinpianchang.com/channel/index/id-7/sort-like/page-");
                break;
            case R.id.D1:
                UI.push(CategoryActivity.class, "动画", "http://www.xinpianchang.com/channel/index/id-5/sort-like/page-");
                break;
            case R.id.D2:
                UI.push(CategoryActivity.class, "预告片/花絮", "http://www.xinpianchang.com/channel/index/id-9/sort-like/page-");
                break;
            case R.id.D3:
                UI.push(CategoryActivity.class, "OTHERS", "http://www.xinpianchang.com/channel/index/type-newera/sort-like/page-");
                break;

        }
        setAnim(v);
        UI.TopActivity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    public void setAnim(final View v) {
        v.setAlpha(0.5f);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                v.setAlpha(1);
            }
        }, 300);
    }
}
