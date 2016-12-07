package ui;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;

import top.greendami.movielineage.R;

/**
 * Created by GreendaMi on 2016/12/7.
 */

public class FilmInfoBehavior extends AppBarLayout.ScrollingViewBehavior {
    final AlphaAnimation InAnimation = new AlphaAnimation(0, 1);

    public FilmInfoBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        View back = coordinatorLayout.findViewById(R.id.backBt);
        View top = coordinatorLayout.findViewById(R.id.top);
        final View cover = coordinatorLayout.findViewById(R.id.cover);
        if(target.getTop() + top.getTop() - dyUnconsumed   > target.getTop() / 7 && dyUnconsumed > 0){
            top.offsetTopAndBottom(- dyUnconsumed);
            back.offsetTopAndBottom(dyUnconsumed);

        }else if(dyUnconsumed > 0){
            //change title color
            cover.setVisibility(View.VISIBLE);
            InAnimation.setDuration(300);
            cover.startAnimation(InAnimation);
        }



        if(top.getBottom() - dyUnconsumed - 1 < child.getHeight() && dyUnconsumed < 0){
            top.offsetTopAndBottom(- dyUnconsumed);
            back.offsetTopAndBottom(dyUnconsumed);
            cover.setVisibility(View.INVISIBLE);
        }




    }

}
