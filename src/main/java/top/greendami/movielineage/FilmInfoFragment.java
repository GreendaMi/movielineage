package top.greendami.movielineage;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import bean.filmBean;
import butterknife.Bind;
import butterknife.ButterKnife;
import tool.BlurBitmapUtil;
import tool.UI;
import ui.EnTextView;
import ui.IconFontTextView;

import static top.greendami.movielineage.R.id.t;

/**
 * Created by GreendaMi on 2016/12/2.
 */

public class FilmInfoFragment extends Fragment implements View.OnTouchListener {

    @Bind(R.id.back)
    IconFontTextView mBack;
    @Bind(R.id.img1)
    ImageView mImg1;
    @Bind(R.id.play)
    IconFontTextView mPlay;
    @Bind(R.id.topR)
    RelativeLayout mTopR;
    @Bind(R.id.name)
    TextView mName;
    @Bind(R.id.v)
    View mV;
    @Bind(R.id.f)
    EnTextView mF;
    @Bind(R.id.from)
    TextView mFrom;
    @Bind(t)
    EnTextView mT;
    @Bind(R.id.tag)
    TextView mTag;
    @Bind(R.id.d)
    EnTextView mD;
    @Bind(R.id.date)
    TextView mDate;
    @Bind(R.id.comment)
    TextView mComment;
    @Bind(R.id.img2)
    ScrollView mImg2;

    Bitmap mBitmap;

    public void setFilmBean(filmBean filmBean,Bitmap bitmap) {
        mFilmBean = filmBean;
        if(mBitmap != null){
            mBitmap.recycle();
        }
        mBitmap = bitmap;
    }

    public filmBean mFilmBean;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filminfo_layout, container, false);
        ButterKnife.bind(this, view);
        InitView();
        InitEvent();
        return view;
    }

    private void InitEvent() {
//        mBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("FilmInfoFragment", "mBack");
//                ((MainActivity)getActivity()).CloseFilmInfo();
//            }
//        });
        mBack.setOnTouchListener(this);
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UI.push(Player.class, mFilmBean);
            }
        });
        mPlay.setOnTouchListener(this);

    }


    //当显示的时候，刷新数据
    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){
            reFreshView();
        }
    }

    private void reFreshView() {

        if(mFilmBean != null){
            mName.setText(mFilmBean.getName());
            mComment.setText(mFilmBean.getComment());
            mTag.setText(mFilmBean.getTag());
            mFrom.setText(mFilmBean.getFrom());
            mDate.setText(mFilmBean.getDate());
        }
        if(mBitmap != null){
            mImg1.setImageBitmap(mBitmap);
            Bitmap newBitmap = BlurBitmapUtil.blurBitmap(getActivity(),mBitmap,25);
            mImg2.setBackground(new BitmapDrawable(newBitmap));
        }
    }



    private void InitView() {
        if(mFilmBean != null){
            Picasso.with(getActivity()).load(mFilmBean.getImg()).into(mImg1);
            Picasso.with(getActivity())
                    .load(mFilmBean.getImg())
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            Bitmap newBitmap = BlurBitmapUtil.blurBitmap(getActivity(),bitmap,25);
                            mImg2.setBackground(new BitmapDrawable(newBitmap));
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
            mName.setText(mFilmBean.getName());
            mComment.setText(mFilmBean.getComment());
            mTag.setText(mFilmBean.getTag());
            mFrom.setText(mFilmBean.getFrom());
            mDate.setText(mFilmBean.getDate());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
