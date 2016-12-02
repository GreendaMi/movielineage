package top.greendami.movielineage;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import bean.filmBean;
import butterknife.Bind;
import butterknife.ButterKnife;
import tool.BlurBitmapUtil;
import tool.UI;
import ui.EnTextView;
import ui.IconFontTextView;

import static top.greendami.movielineage.R.id.from;

/**
 * Created by GreendaMi on 2016/12/1.
 */

public class FilmInfo extends Activity implements View.OnTouchListener {

    filmBean mFilmBean;
    @Bind(R.id.New)
    EnTextView mNew;
    @Bind(R.id.titlebar)
    RelativeLayout mTitlebar;
    @Bind(R.id.img1)
    ImageView mImg1;
    @Bind(R.id.topR)
    RelativeLayout mTopR;
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
    ScrollView mImg2;
    @Bind(R.id.back)
    IconFontTextView mBack;
    @Bind(R.id.play)
    IconFontTextView mPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.filminfo_layout);
        ButterKnife.bind(this);
        mFilmBean = (filmBean) UI.getData();
        InitView();
        InitEvent();
    }

    private void InitEvent() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UI.pop();
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
//        Picasso.with(this).load(mFilmBean.getImg()).into(mImg1);
//        Picasso.with(this)
//                .load(mFilmBean.getImg())
//                .into(new Target() {
//                    @Override
//                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                        mImg1.setImageBitmap(bitmap);
//                        Bitmap newBitmap = BlurBitmapUtil.blurBitmap(FilmInfo.this,bitmap,25);
//                        mImg2.setBackground(new BitmapDrawable(newBitmap));
//                    }
//
//                    @Override
//                    public void onBitmapFailed(Drawable errorDrawable) {
//
//                    }
//
//                    @Override
//                    public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                    }
//                });

        mImg1.setImageBitmap((Bitmap)( UI.getData(1)));
        Bitmap newBitmap = BlurBitmapUtil.blurBitmap(FilmInfo.this,(Bitmap)( UI.getData(1)),25);
        mImg2.setBackground(new BitmapDrawable(newBitmap));
        mName.setText(mFilmBean.getName());
        mComment.setText(mFilmBean.getComment());
        mTag.setText(mFilmBean.getTag());
        mFrom.setText(mFilmBean.getFrom());
        mDate.setText(mFilmBean.getDate());
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
