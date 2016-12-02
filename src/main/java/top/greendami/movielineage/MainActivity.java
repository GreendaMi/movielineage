package top.greendami.movielineage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import adapter.MyFragmentPagerAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import tool.UI;
import ui.DynamicWave;
import ui.EnTextView;


public class MainActivity extends FragmentActivity {

    FragmentManager fManager;
    FragmentTransaction mTransaction;
    @Bind(R.id.v1)
    View mV1;
    @Bind(R.id.v2)
    View mV2;
    @Bind(R.id.vp_view)
    ViewPager mVpView;
    @Bind(R.id.content_frame)
    RelativeLayout mContentFrame;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.Dw)
    DynamicWave mDw;
    private Fragment NEW, HOT, COM;

    EnTextView New, Hot, Com;

    ArrayList<Fragment> fragmentsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initEvent();
        UI.enter(this);
        New.callOnClick();
    }

    private void initView() {

        New = (EnTextView) findViewById(R.id.New);
        Hot = (EnTextView) findViewById(R.id.hot);
        Com = (EnTextView) findViewById(R.id.comment);

        NEW = new NewFragment();
        HOT = new HotFragment();
        COM = new CommentFragment();

        fragmentsList.add(NEW);
        fragmentsList.add(HOT);
        fragmentsList.add(COM);


        mVpView.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList));
        select(0);

    }

    /**
     * Fragment切换
     *
     * @param i
     */
    private void select(int i) {
        hideFragment();
        switch (i) {
            case 0:
                mVpView.setCurrentItem(0);
                New.setScaleX(1.2f);
                New.setScaleY(1.2f);
                New.setTextColor(getResources().getColor(R.color.FontColor));
                break;
            case 1:
                mVpView.setCurrentItem(1);
                Hot.setScaleX(1.2f);
                Hot.setScaleY(1.2f);
                Hot.setTextColor(getResources().getColor(R.color.FontColor));
                break;

            case 2:
                mVpView.setCurrentItem(2);
                Com.setScaleX(1.2f);
                Com.setScaleY(1.2f);
                Com.setTextColor(getResources().getColor(R.color.FontColor));
                break;

        }
    }

    private void hideFragment() {

        Hot.setTextColor(getResources().getColor(R.color.DarkFontColor));
        New.setTextColor(getResources().getColor(R.color.DarkFontColor));
        Com.setTextColor(getResources().getColor(R.color.DarkFontColor));

        New.setScaleX(1.0f);
        New.setScaleY(1.0f);
        Hot.setScaleX(1.0f);
        Hot.setScaleY(1.0f);
        Com.setScaleX(1.0f);
        Com.setScaleY(1.0f);

        mV1.setAlpha(0.2f);
        mV2.setAlpha(0.2f);

    }

//    public void FilmSelect(filmBean f , ImageView v){
//
//        fManager = getSupportFragmentManager();
//        mTransaction = fManager.beginTransaction();
//        hideFragment(mTransaction);
//        if(mFilmInfo == null){
//            mFilmInfo = new FilmInfoFragment();
//            mTransaction.add(R.id.content, mFilmInfo);
//        }
//
//        v.setDrawingCacheEnabled(true);
//        Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
//        v.setDrawingCacheEnabled(false);
//
//        mFilmInfo.setFilmBean(f , bitmap);
//        mTransaction.show(mFilmInfo);
//        mTransaction.commit();
//
//    }
//
//    public void CloseFilmInfo(){
//        Log.d("MainActivity", "CloseFilmInfo");
//        select(select);
//    }

    private void initEvent() {

        New.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select(0);
                mV1.setAlpha(1f);
            }
        });

        Hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select(1);
                mV1.setAlpha(1f);
                mV2.setAlpha(1f);
            }
        });
        Com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mV2.setAlpha(1f);
                select(2);
            }
        });

        mVpView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                select(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
//                mDrawerLayout.setFocusable(true);
//                mDrawerLayout.requestFocus();
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {
                //抽屉打开的过程中，停止海浪的动画，否则卡顿
                if(newState == DrawerLayout.STATE_DRAGGING ){
                    mDw.Stop();
                }else{
                    mDw.Start();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        UI.enter(this);
    }


}
