package ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by GreendaMi on 2016/12/1.
 */

public class IconFontButton extends Button {
    Context context;
    public IconFontButton(Context context) {
        super(context);
    }

    public IconFontButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
        setTypeface(typeface);
    }
}
