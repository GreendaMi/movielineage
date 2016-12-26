package ui;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;

import top.greendami.movielineage.R;

/**
 * A FloatingActionButton with Text
 * Created by GreendaMi on 2016/12/7.
 */

public class TextFloatingActionButton extends FloatingActionButton {
    Typeface mtypeface;

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    String text;
    int textcolor;


    public TextFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs , R.styleable.TextFloatingActionButton);
        text = a.getString(R.styleable.TextFloatingActionButton_textFloatingActionButtonText);
        a.recycle();
        mtypeface = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint=new Paint();
//        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.WHITE);
        paint.setTypeface(mtypeface);
        paint.setTextSize(getWidth() * 0.6f);

        Rect rect = new Rect();

//返回包围整个字符串的最小的一个Rect区域
        paint.getTextBounds(text, 0, 1, rect);
//拿到字符串的宽度
        float stringWidth = rect.width();
        float stringHeight = rect.height();

        float x =(getWidth()* 0.9f-stringWidth)/2;
        canvas.drawText(text, x ,(getHeight()* 0.9f + stringHeight)/2 ,paint);
    }
}
