package ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import top.greendami.movielineage.R;

/**
 * Created by GreendaMi on 2016/9/17.
 */
public class DotsPreloader extends View{

    public int mColor;
    public Paint mPain = new Paint();
    private int mLayoutSize = 100;
    double time = 0;
    int flag = 0;
    boolean isLoading = true;

    public DotsPreloader(Context context) {
        super(context);
    }

    public DotsPreloader(Context context, AttributeSet attrs) {
        super(context, attrs);
        mColor = context.getResources().getColor(R.color.DarkFontColor);
        init();
    }

    public DotsPreloader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        mPain.setAntiAlias(true);
        mPain.setColor(mColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        mLayoutSize = Math.min(widthSpecSize,heightSpecSize);
        setMeasuredDimension(mLayoutSize, mLayoutSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isLoading){
            double radius = (mLayoutSize/6) * 0.3;
            double gap = (mLayoutSize/6) * 0.4;
            List<Point> mCenterPoints = getCenterPoints(radius,gap);
            for(Point mCenterPoint : mCenterPoints){
                canvas.drawCircle(mCenterPoint.x , mCenterPoint.y , (float) radius , mPain);
            }
//      postInvalidateDelayed(20);
            invalidate();
        }
    }



    private List<Point> getCenterPoints(double r, double g) {
        List<Point> mCenterPoints = new ArrayList<Point>();

        float mCenterPointGap = new Float(2 * r + g);
        double a = (1-(1-time) * (1-time)) * mCenterPointGap;

        if(time > 1){
            time = 0;
            if(flag == 0){
                flag = 1;
            }else{
                flag = 0;
            }
        }else{
            time = time + 0.025;
        }

        int y = mLayoutSize/2;
        Float x2 = new Float((r + g)/2 + mCenterPointGap * 1 - a);
        Float x3 = new Float((r + g)/2 + mCenterPointGap * 2 - a);
        Float x4 = new Float((r + g)/2 + mCenterPointGap * 3 - a);
        Float x5 = new Float((r + g)/2 + mCenterPointGap * 4 - a);
        Float x6 = new Float((r + g)/2 + mCenterPointGap * 5 - a);

        Double x1 = 0.5 * g + r +  (1-(1-time) * (1-time)) * mCenterPointGap * 5;

        Double y1 = Math.sqrt((mLayoutSize - g - 2 * r) * (mLayoutSize - g - 2 * r)/4 - (x1 - mLayoutSize * 0.5) * (x1 - mLayoutSize * 0.5)) + mLayoutSize * 0.5;
        if(flag == 0){
            y1 = mLayoutSize * 0.5 - Math.sqrt((mLayoutSize - g - 2 * r) * (mLayoutSize - g - 2 * r)/4 - (x1 - mLayoutSize * 0.5) * (x1 - mLayoutSize * 0.5));
        }

        mCenterPoints.add(new Point(x1.intValue(),y1.intValue()));
        mCenterPoints.add(new Point(x2.intValue(),y));
        mCenterPoints.add(new Point(x3.intValue(),y));
        mCenterPoints.add(new Point(x4.intValue(),y));
        mCenterPoints.add(new Point(x5.intValue(),y));
        mCenterPoints.add(new Point(x6.intValue(),y));
        return  mCenterPoints;
    }

    public void start() {
        isLoading = true;
        invalidate();
    }

    public void stop() {
        isLoading = false;
    }
}
