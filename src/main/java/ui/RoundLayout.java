package ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import tool.DensityUtil;
import top.greendami.movielineage.R;

/**
 * Created by GreendaMi on 2016/12/22.
 */

public class RoundLayout extends RelativeLayout {
    float xmlboder = 100;
    float boder = 100;
    int step = 1;
    public RoundLayout(Context context) {
        this(context, null);
    }

    public RoundLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundLayout);
        xmlboder = typedArray.getDimension(R.styleable.RoundLayout_boder,  boder);
        boder = xmlboder;
        typedArray.recycle();
        setWillNotDraw(false);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }
//    public void startAnim(){
//        //加大步数，加速动画
//        step = step + 25;
//        //减小边框距离
//        boder = boder - step;
//        if(boder > 0){
//
//            invalidate();
//        }else{
//            boder = 0;
//            invalidate();
//        }
//    }
//
//    public void resetAnim() {
//        boder = xmlboder;
//        step = 1;
//    }


    @Override
    public void draw(Canvas canvas) {

        Path mPath = new Path();
        mPath.moveTo(0,0);
        mPath.lineTo(getWidth() - boder, 0);
        mPath.lineTo(getWidth() - boder, DensityUtil.dip2px(getContext(),170));
        //绘制贝塞尔曲线
        mPath.quadTo(getWidth(), (getHeight() + DensityUtil.dip2px(getContext(),170))/2 , getWidth() - boder, getHeight());
        mPath.lineTo(0, getHeight());
        mPath.setFillType(Path.FillType.WINDING);
        mPath.close();

        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG|Paint.ANTI_ALIAS_FLAG));
        canvas.clipPath(mPath);

        super.draw(canvas);
//        if(boder > 0 && boder < xmlboder){
//            startAnim();
//        }
    }
}