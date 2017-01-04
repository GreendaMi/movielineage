package ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by GreendaMi on 2016/12/10.
 */

public class LoadingImageView extends ImageView {

    int progress = 0;
    Paint mPaint;


    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        if(progress < 0){
            this.progress = 0;
        }
        else if(progress <= 100){
            this.progress = progress;
        }

        else{
            this.progress = 100;
        }
        invalidate();
    }


    Bitmap mBitmap;

    public LoadingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);



        //黑白
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(new float[]{
                0.33F, 0.59F, 0.11F, 0, 0,
                0.33F, 0.59F, 0.11F, 0, 0,
                0.33F, 0.59F, 0.11F, 0, 0,
                0, 0, 0, 1, 0,
        });
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(getDrawable() != null){
            Log.d("LoadingImageView", "progress:" + progress);
            BitmapDrawable db = (BitmapDrawable)getDrawable();
            mBitmap = resizeImage(db.getBitmap(),getWidth(),getHeight());
            Rect mRect = new Rect(0, 0, getWidth(), getHeight() * (100 - progress)/100);
            canvas.drawBitmap(mBitmap , mRect,mRect,mPaint);
            mBitmap.recycle();
        }
    }

    public Bitmap resizeImage(Bitmap bitmap,int w, int h)
    {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(Math.max(scaleWidth,scaleHeight), Math.max(scaleWidth,scaleHeight));
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);
        return new BitmapDrawable(resizedBitmap).getBitmap();
    }

}
