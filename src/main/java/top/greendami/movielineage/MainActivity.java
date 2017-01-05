package top.greendami.movielineage;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import adapter.MyFragmentPagerAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import model.DownLoadManager;
import tool.NetworkType;
import tool.NetworkTypeInfo;
import tool.UI;
import ui.ChTextView;
import ui.DotsPreloader;
import ui.DynamicWave;
import ui.IconFontTextView;
import ui.RoundLayout;
import ui.SearchDialog;


public class MainActivity extends FragmentActivity implements View.OnTouchListener {

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
    @Bind(R.id.like)
    TextView mLike;
    @Bind(R.id.dl)
    TextView mDl;
    @Bind(R.id.sh)
    TextView mSh;
    @Bind(R.id.left_drawer)
    RoundLayout mLeftDrawer;
    @Bind(R.id.id)
    ChTextView mId;
    @Bind(R.id.DotsPreloader)
    DotsPreloader mDotsPreloader;
    @Bind(R.id.search_button)
    IconFontTextView searchButton;
    @Bind(R.id.circle_bg)
    View circleBg;
    private Fragment NEW, HOT, COM;

    TextView New, Hot, Com;

    ArrayList<Fragment> fragmentsList = new ArrayList<>();
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initEvent();
        UI.enter(this);
        Hot.callOnClick();
        mHandler = new Handler();

        if (NetworkTypeInfo.getNetworkType(this) == NetworkType.Wifi) {
            DownLoadManager.startAll(this);
        }
    }

    private void initView() {

        New = (TextView) findViewById(R.id.New);
        Hot = (TextView) findViewById(R.id.hot);
        Com = (TextView) findViewById(R.id.comment);

        NEW = new NewFragment();
        HOT = new HotFragment();
        COM = new CategoryFragment();

        fragmentsList.add(NEW);
        fragmentsList.add(HOT);
        fragmentsList.add(COM);

        mVpView.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList));

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
                mV1.setAlpha(1f);
                break;
            case 1:
                mVpView.setCurrentItem(1);
                Hot.setScaleX(1.2f);
                Hot.setScaleY(1.2f);
                Hot.setTextColor(getResources().getColor(R.color.FontColor));
                mV1.setAlpha(1f);
                mV2.setAlpha(1f);
                break;

            case 2:
                mVpView.setCurrentItem(2);
                Com.setScaleX(1.2f);
                Com.setScaleY(1.2f);
                Com.setTextColor(getResources().getColor(R.color.FontColor));
                mV2.setAlpha(1f);
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

    private void initEvent() {

        New.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select(0);
            }
        });

        Hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select(1);
            }
        });
        Com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                if (newState == DrawerLayout.STATE_DRAGGING) {
                    mDw.Stop();
                } else {
                    mDw.Start();
                }
            }
        });
        mDl.setOnTouchListener(this);
        mLike.setOnTouchListener(this);
        mSh.setOnTouchListener(this);
        mDl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawers();

                //加载一个延迟，当侧边栏关闭后，再跳转
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        UI.push(DownLoadActivity.class);
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                    }
                }, 150);
            }
        });

        mLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawers();

                //加载一个延迟，当侧边栏关闭后，再跳转
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        UI.push(LikeActivity.class);
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                    }
                }, 150);
            }
        });

        mSh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSh.getText().equals("注销")) {
                    UI.Save("Me", "");
                    mId.setText("");
                    mSh.setText("登录");
                } else {
                    UI.push(LoginActivity.class);
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ScaleAnimation animation =new ScaleAnimation(0.0f, 16f, 0.0f, 16f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(500);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        circleBg.setVisibility(View.VISIBLE);
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                SearchDialog.Show(MainActivity.this);
                            }
                        },400);

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        circleBg.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                circleBg.startAnimation(animation);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        UI.enter(this);

        if (UI.get("Me") != null && !UI.get("Me").toString().isEmpty()) {
            mSh.setText("注销");
            mId.setText(UI.get("Me").toString().substring(0, 3) + "****" + UI.get("Me").toString().substring(7));


        } else {
            mSh.setText("登录");
            mId.setText("");
        }


    }

    public void showLoadingBar() {
        if (mDotsPreloader.getVisibility() == View.INVISIBLE) {
            mDotsPreloader.setVisibility(View.VISIBLE);
            searchButton.setVisibility(View.INVISIBLE);
        }
    }

    public void dismissLoadingBar() {
        if (mDotsPreloader.getVisibility() == View.VISIBLE) {
            mDotsPreloader.setVisibility(View.INVISIBLE);
            searchButton.setVisibility(View.VISIBLE);
        }
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
