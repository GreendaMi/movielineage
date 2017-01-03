package top.greendami.movielineage;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.wlf.filedownloader.FileDownloader;

import bean.daoBean.likefilmbean;
import bean.filmBean;
import butterknife.Bind;
import butterknife.ButterKnife;
import model.DAOManager;
import model.DownLoadManager;
import tool.ACache;
import tool.BlurBitmapUtil;
import tool.DensityUtil;
import tool.NetworkType;
import tool.NetworkTypeInfo;
import tool.UI;
import ui.IconFontTextView;

import static tool.UI.getData;
import static top.greendami.movielineage.R.id.comment;
import static top.greendami.movielineage.R.id.date;
import static top.greendami.movielineage.R.id.from;

/**
 * Created by GreendaMi on 2016/12/1.
 */

public class FilmInfo extends Activity implements View.OnTouchListener {

    filmBean mFilmBean;
    @Bind(R.id.img1)
    ImageView mImg1;
    @Bind(R.id.name)
    TextView mName;
    @Bind(from)
    TextView mFrom;
    @Bind(R.id.tag)
    TextView mTag;
    @Bind(date)
    TextView mDate;
    @Bind(comment)
    TextView mComment;
    @Bind(R.id.img2)
    FrameLayout mImg2;
    @Bind(R.id.backBt)
    IconFontTextView mBack;
    @Bind(R.id.play)
    IconFontTextView mPlay;
    @Bind(R.id.lay1)
    ImageView mLay1;
//    @Bind(R.id.lay2)
//    AppBarLayout mLay2;
    @Bind(R.id.top)
    RelativeLayout mTop;

    int topPo;
    @Bind(R.id.bf)
    IconFontTextView mBf;
    @Bind(R.id.sc)
    IconFontTextView mSc;
    @Bind(R.id.xz)
    IconFontTextView mXz;
    @Bind(R.id.bf_icon)
    IconFontTextView mBfIcon;

    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        setContentView(R.layout.activity_filminfo);
        ButterKnife.bind(this);
        mFilmBean = (filmBean) getData();
        topPo = (int) UI.getData(2);
        mHandler = new Handler();
        InitView();
        InitEvent();
    }

    private void InitEvent() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endAnim();
            }
        });
        mBack.setOnTouchListener(this);
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ACache mCache = ACache.get(FilmInfo.this);
                mCache.put("PlayFilm", mFilmBean);
                UI.push(Player.class);
            }
        });
        mPlay.setOnTouchListener(this);

        mSc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkTypeInfo.getNetworkType(FilmInfo.this) == NetworkType.NoNetwork){
                    UI.Toast("请链接网络！");
                    return;
                }
                if (UI.get("Me") != null && !UI.get("Me").toString().isEmpty()) {
                    likefilmbean lfb = new likefilmbean();
                    lfb.setName(mFilmBean.getName());
                    lfb.setFrom(mFilmBean.getFrom());
                    lfb.setUrl(mFilmBean.getUrl());
                    lfb.setIntroduce(mFilmBean.getIntroduce());
                    lfb.setImg(mFilmBean.getImg());
                    lfb.setDate(mFilmBean.getDate());
                    lfb.setTag(mFilmBean.getTag());
                    lfb.setComment(mFilmBean.getComment());

                    if (DAOManager.getInstance(FilmInfo.this).queryLikeFilmList(mFilmBean.getUrl()).size() == 0) {

                        DAOManager.getInstance(FilmInfo.this).insertLikeFilm(lfb);
                        mSc.setText(getResources().getString(R.string.收藏2));
                        ScaleAnimation mScaleAnimation = new ScaleAnimation(1.8f, 1.0f, 1.8f, 1.0f,
                                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        AnimationSet mAnimatorSet = new AnimationSet(true);
                        mAnimatorSet.addAnimation(mScaleAnimation);
                        mAnimatorSet.setDuration(1500);
                        mAnimatorSet.setInterpolator(new AnticipateOvershootInterpolator());
//                        mSc.bringToFront();
                        mSc.startAnimation(mAnimatorSet);
                    } else {
                        lfb = DAOManager.getInstance(FilmInfo.this).queryLikeFilmList(mFilmBean.getUrl()).get(0);
                        DAOManager.getInstance(FilmInfo.this).deleteLikeFilm(lfb);
                        mSc.setText(getResources().getString(R.string.收藏1));
                    }

                } else {
                    UI.push(LoginActivity.class);
                }
            }
        });

    }


    private void InitView() {

        mLay1.setImageBitmap((Bitmap) (getData(1)));
        mImg1.setImageBitmap((Bitmap) (getData(1)));

        mName.setText(mFilmBean.getName());
        mComment.setText(mFilmBean.getComment());
        mTag.setText(mFilmBean.getTag());
        mFrom.setText(mFilmBean.getFrom());
        mDate.setText(mFilmBean.getDate());
        if (mFilmBean.getNum() != null && !mFilmBean.getNum().equals("0")) {
            mBf.setText(mFilmBean.getNum());
        } else {
            mBf.setText("");
            mBfIcon.setText("");
        }

        starAnim();

    }


    private void starAnim() {
        mTop.setVisibility(View.INVISIBLE);
        mPlay.setVisibility(View.INVISIBLE);
        AnimationSet animationSet;
        animationSet = new AnimationSet(true);
        //加上一个55，标题栏的高度
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, (int) (getData(2)) + DensityUtil.dip2px(FilmInfo.this, 55), 0);
        //设置动画执行的时间
        translateAnimation.setDuration(300);
        animationSet.addAnimation(translateAnimation);


        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.75f, 1.0f, 1.75f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(300);//设置动画持续时间
        animationSet.addAnimation(animation);

        animationSet.setFillAfter(true);

        mLay1.startAnimation(animationSet);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mTop.setVisibility(View.VISIBLE);
                mPlay.setVisibility(View.VISIBLE);
                mLay1.setAlpha(0);
                Bitmap newBitmap = BlurBitmapUtil.blurBitmap(FilmInfo.this, (Bitmap) (getData(1)), 25);
                mImg2.setBackground(new BitmapDrawable(newBitmap));

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void moveTop() {
        finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    private void endAnim() {


        mTop.setVisibility(View.INVISIBLE);
        mPlay.setVisibility(View.INVISIBLE);
        mLay1.setAlpha(255);
        AnimationSet animationSet;
        animationSet = new AnimationSet(true);
        //加上一个55，标题栏的高度
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, (int) (topPo) + DensityUtil.dip2px(FilmInfo.this, 55));
        //设置动画执行的时间
        translateAnimation.setDuration(300);
        animationSet.addAnimation(translateAnimation);


        ScaleAnimation animation = new ScaleAnimation(1.75f, 1.0f, 1.75f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(300);//设置动画持续时间
        animationSet.addAnimation(animation);

        animationSet.setFillAfter(true);


        mLay1.startAnimation(animationSet);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mImg2.setBackgroundColor(getResources().getColor(R.color.transparent));

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                mImg2.setBackground(null);
                UI.pop();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            endAnim();
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

    @Override
    protected void onResume() {
        super.onResume();
        UI.enter(this);
        if (DAOManager.getInstance(FilmInfo.this).queryLikeFilmList(mFilmBean.getUrl()).size() == 0) {
            mSc.setText(getResources().getString(R.string.收藏1));
        } else {
            mSc.setText(getResources().getString(R.string.收藏2));
        }
        if (FileDownloader.getDownloadFile(mFilmBean.getUrl()) == null) {
            mXz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DownLoadManager.startDownLoad(mFilmBean);
                    mXz.setTextColor(getResources().getColor(R.color.DarkFontColor));
                    UI.Toast("下载中");
                }
            });
        } else {
            mXz.setTextColor(getResources().getColor(R.color.DarkFontColor));
        }
    }
}
