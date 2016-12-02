package tool;

/**
 * Created by zhaopy on 2015/11/18.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.util.ArrayList;

public class ViewExtensions {


    /**
     *
     * 在LinearLayout等Layout中加载布局文件
     * ViewExtensions.loadLayout(this, R.layout.dialog_system_ok_no);
     * @param container
     * @param layoutResourceId
     */
    public static void loadLayout(ViewGroup container, int layoutResourceId) {
        LayoutInflater inflator = (LayoutInflater) container.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflator.inflate(layoutResourceId, container);
    }

    public static void replaceLayout(ViewGroup container, int layoutResourceId) {

        LayoutInflater inflator = (LayoutInflater) container.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //需要加载布局文件的id，需要附加到resource资源文件的根控件，是否将root附加到布局文件的根视图上
        ViewGroup xmlRoot = (ViewGroup)inflator.inflate(layoutResourceId, container, false);

        ArrayList<View> views = new ArrayList<View>();

        for (int i = 0; i <= xmlRoot.getChildCount() - 1; i++) {
            views.add(xmlRoot.getChildAt(i));
        }

        xmlRoot.removeAllViews();
        for (View view : views) {
            container.addView(view);
        }
    }

    public static View createView(Class<?> viewType, Context context) {

        View view = null;

        try {
            view = (View) viewType.getConstructor(Context.class).newInstance(
                    new Object[] { context });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    public static void animateHeightTo(final View view, final int height,
                                       int duration) {

        final int originalHeight = view.getHeight();

        Animation anim = new Animation() {

            @Override
            protected void applyTransformation(float interpolatedTime,
                                               Transformation t) {

                setViewHeight(view,
                        (int) (originalHeight + (height - originalHeight)
                                * interpolatedTime));
            }
        };

        anim.setDuration(duration);
        view.startAnimation(anim);
    }

    private static void setViewHeight(View view, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();

        params.height = height;
        view.setLayoutParams(params);
    }

    public static View findAncestorView(View view, Class type) {
        ViewParent current = view.getParent();

        while (current instanceof View) {
            if (type.isAssignableFrom(current.getClass())) {
                return (View) current;
            }

            current = current.getParent();
        }
        return null;
    }

    public static View findRootView(View view) {

        View current = view;

        while (current.getParent() instanceof View) {
            current = (View)current.getParent();
        }

        return current;
    }

    public static View findViewByPosition(View view, int x, int y, Class type) {
        ArrayList views = new ArrayList();
        findViewByPosition(view, x, y, views, type);
        return views.size() == 0 ? null : (View) views.get(views.size() - 1);
    }

    private static void findViewByPosition(View view, int x, int y,
                                           ArrayList foundViews, Class targetViewType) {

        if (x >= view.getLeft() && x <= view.getLeft() + view.getWidth()
                && y >= view.getTop() && y <= view.getTop() + view.getHeight()) {
            if (targetViewType.equals(view.getClass())) {
                foundViews.add(view);
            }
        } else {
            return;
        }

        if (ViewGroup.class.isAssignableFrom(view.getClass())) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i <= viewGroup.getChildCount() - 1; i++) {
                findViewByPosition(viewGroup.getChildAt(i), x - view.getLeft(),
                        y - view.getTop(), foundViews, targetViewType);
            }
        }
    }
}
