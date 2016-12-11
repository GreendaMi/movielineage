package top.greendami.movielineage;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.Bind;
import butterknife.ButterKnife;
import tool.UI;
import top.greendami.loadingimageview.LoadingImageView;
import ui.IconFontTextView;

/**
 * Created by GreendaMi on 2016/12/10.
 */

public class DownLoadActivity extends Activity {
    @Bind(R.id.backBt)
    IconFontTextView mBackBt;
    @Bind(R.id.img)
    LoadingImageView mImg;
    @Bind(R.id.button)
    Button mButton;
    int p = 1;
    @Bind(R.id.buttonP)
    Button mButtonP;
    @Bind(R.id.textView)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ButterKnife.bind(this);
        Glide.with(this).load("http://cs.xinpianchang.com/uploadfile/article/2016/12/10/584b941aa17ca.jpeg").asBitmap().diskCacheStrategy(DiskCacheStrategy.RESULT).into(mImg);


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p = p + 2;
                mImg.setProgress(p);
                mTextView.setText(p +"");
            }
        });

        mButtonP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p = p - 2;
                mImg.setProgress(p);
                mTextView.setText(p+"");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        UI.enter(this);
    }
}
