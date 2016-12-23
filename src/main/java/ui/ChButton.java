package ui;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by GreendaMi on 2016/12/20.
 */

public class ChButton extends Button {
    Context context;
    public ChButton(Context context) {
        super(context);
    }

    public ChButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "iconfont/Chinese.ttf");
        setTypeface(typeface);
    }
}
