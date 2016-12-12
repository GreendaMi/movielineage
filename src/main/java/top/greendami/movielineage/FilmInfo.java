package top.greendami.movielineage;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bean.filmBean;
import butterknife.Bind;
import butterknife.ButterKnife;
import tool.BlurBitmapUtil;
import tool.DensityUtil;
import tool.UI;
import ui.IconFontTextView;

import static tool.UI.getData;
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
    @Bind(R.id.date)
    TextView mDate;
    @Bind(R.id.comment)
    TextView mComment;
    @Bind(R.id.img2)
    CoordinatorLayout mImg2;
    @Bind(R.id.backBt)
    IconFontTextView mBack;
    @Bind(R.id.play)
    ImageView mPlay;
    @Bind(R.id.lay1)
    ImageView mLay1;
    @Bind(R.id.lay2)
    AppBarLayout mLay2;
    @Bind(R.id.top)
    RelativeLayout mTop;

    int topPo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        setContentView(R.layout.activity_filminfo);
        ButterKnife.bind(this);
        mFilmBean = (filmBean) getData();
        topPo = (int)UI.getData(2);
        InitView();
        InitEvent();
    }

    private void InitEvent() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mTop.getTop() == 0){
                    endAnim();
                }else{
                    moveTop();
                }
            }
        });
        mBack.setOnTouchListener(this);
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UI.push(Player.class, mFilmBean);
            }
        });
        mPlay.setOnTouchListener(this);

    }


    private void InitView() {
        mLay1.setImageBitmap((Bitmap) (getData(1)));
        mImg1.setImageBitmap((Bitmap) (getData(1)));

        mName.setText(mFilmBean.getName());
        mComment.setText(mFilmBean.getComment());
        mTag.setText(mFilmBean.getTag());
        mFrom.setText(mFilmBean.getFrom());
        mDate.setText(mFilmBean.getDate());


        starAnim();
    }

    private void starAnim() {
        mLay2.setVisibility(View.INVISIBLE);
        mPlay.setVisibility(View.INVISIBLE);
        AnimationSet animationSet;
        animationSet = new AnimationSet(true);
        //加上一个55，标题栏的高度
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, (int) (getData(2)) + DensityUtil.dip2px(FilmInfo.this, 55), 0);
        //设置动画执行的时间
        translateAnimation.setDuration(400);
        animationSet.addAnimation(translateAnimation);


        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.75f, 1.0f, 1.75f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(400);//设置动画持续时间
        animationSet.addAnimation(animation);

        animationSet.setFillAfter(true);

        mLay1.startAnimation(animationSet);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Bitmap newBitmap = BlurBitmapUtil.blurBitmap(FilmInfo.this, (Bitmap) (getData(1)), 25);
                mImg2.setBackground(new BitmapDrawable(newBitmap));
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLay2.setVisibility(View.VISIBLE);
                mPlay.setVisibility(View.VISIBLE);
                mLay1.setAlpha(0);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void moveTop(){
        finish();
        overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
    }

    private void endAnim() {



        mLay2.setVisibility(View.INVISIBLE);
        mPlay.setVisibility(View.INVISIBLE);
        mLay1.setAlpha(255);
        AnimationSet animationSet;
        animationSet = new AnimationSet(true);
        //加上一个55，标题栏的高度
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, (int) (topPo) + DensityUtil.dip2px(FilmInfo.this, 55));
        //设置动画执行的时间
        translateAnimation.setDuration(400);
        animationSet.addAnimation(translateAnimation);


        ScaleAnimation animation = new ScaleAnimation(1.75f, 1.0f, 1.75f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(400);//设置动画持续时间
        animationSet.addAnimation(animation);

        animationSet.setFillAfter(true);



        mLay1.startAnimation(animationSet);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {


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
            if(mTop.getTop() == 0){
                endAnim();
            }else{
                moveTop();
            }

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
    }
}
