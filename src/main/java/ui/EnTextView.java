package ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by GreendaMi on 2016/11/30.
 */

public class EnTextView extends TextView {
    Context context;

    public EnTextView(Context context) {
        super(context);
    }

    public EnTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "iconfont/En.ttf");
        setTypeface(typeface);
    }
}