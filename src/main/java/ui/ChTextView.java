package ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by GreendaMi on 2016/12/20.
 */

public class ChTextView extends TextView {
    Context context;

    public ChTextView(Context context) {
        super(context);
    }

    public ChTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "iconfont/Chinese.ttf");
        setTypeface(typeface);
    }
}