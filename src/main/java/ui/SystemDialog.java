package ui;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import tool.ViewExtensions;
import top.greendami.movielineage.R;


/**
 * 系统弹出框
 *
 * @author Administrator
 */
public class SystemDialog extends LinearLayout implements OnClickListener {
    private TextView systemdialog_title, systemdialog_cancel, systemdialog_ok;

    private static TextView systemInfoDialog_ok, systemInfoDialog_title;

    private static AlertDialog dialog;

    private static AlertDialog infoDialog;

    public static AlertDialog getLoadingdialog() {
        return loadingdialog;
    }

    public static AlertDialog getDialog() {
        return dialog;
    }

    private static AlertDialog loadingdialog;
    private static OnKeyListenerForBack onKeyListenerForBack;
    private static NetworkView view;
    private static Object data;
    private OnSystemDialogClickListener listener;

    public interface OnSystemDialogClickListener {
        public void leftClick(Object object);

        public void rightClick(Object object);
    }

    protected SystemDialog(Context context, OnSystemDialogClickListener listener) {
        super(context);
        ViewExtensions.loadLayout(this, R.layout.dialog_system_ok_no);
        this.listener = listener;
        init();
    }

    protected SystemDialog(Context context) {
        super(context);
        ViewExtensions.loadLayout(this, R.layout.dialog_system_ok);
    }

    protected void settingTitle(String title) {
        systemdialog_title.setText(title);
    }

    protected void settingLeft(String left) {
        systemdialog_cancel.setText(left);
    }

    protected void settingRight(String right) {
        systemdialog_ok.setText(right);
    }

    private void init() {
        systemdialog_title = (TextView) findViewById(R.id.systemdialog_title);
        systemdialog_cancel = (TextView) findViewById(R.id.systemdialog_cancel);
        systemdialog_ok = (TextView) findViewById(R.id.systemdialog_ok);
        systemdialog_cancel.setOnClickListener(this);
        systemdialog_ok.setOnClickListener(this);
    }

    public static AlertDialog showDialog(Context context, String title,
                                         Object object, OnSystemDialogClickListener listener) {
        return showDialog(context, title, null, null, object, true, listener);
    }

    public static AlertDialog showDialog(Context context, String title,
                                         String left, String right, OnSystemDialogClickListener listener) {
        return showDialog(context, title, left, right, null, true, listener);
    }

    public static AlertDialog showDialog(Context context, String title,
                                         String left, String right, boolean cancelable,
                                         OnSystemDialogClickListener listener) {
        return showDialog(context, title, left, right, null, cancelable,
                listener);
    }

    public static AlertDialog showDialog(Context context, String title,
                                         String left, String right, Object object, boolean cancelable,
                                         OnSystemDialogClickListener listener) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        SystemDialog view = new SystemDialog(context, listener);
        if (title != null) {
            view.settingTitle(title);
        }
        if (left != null) {
            view.settingLeft(left);
        }
        if (right != null) {
            view.settingRight(right);
        }
        data = object;
        dialog = new AlertDialog.Builder(context).setCancelable(cancelable)
                .create();
        dialog.setCanceledOnTouchOutside(cancelable);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.show();
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        dialog.addContentView(view, params);

        return dialog;
    }

    public interface OnKeyListenerForBack {
        public void backKeydown();
    }

    public static void showLoadingDialog(Context context, boolean cancelable) {
        showLoadingDialog(context, cancelable, null);
    }

    public static void showLoadingDialog(Context context, boolean cancelable,
                                         OnKeyListenerForBack listener) {
        onKeyListenerForBack = listener;
        dismissLoadingDialog();
        view = new NetworkView(context);
        loadingdialog = new AlertDialog.Builder(context).setCancelable(
                cancelable).create();
        loadingdialog.setCanceledOnTouchOutside(cancelable);
        Window window = loadingdialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable());
        loadingdialog.show();
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        view.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
                if (arg2.getAction() == KeyEvent.KEYCODE_BACK) {
                    if (onKeyListenerForBack != null)
                        onKeyListenerForBack.backKeydown();
                }
                return false;
            }
        });
        loadingdialog.addContentView(view, params);
        view.start();
    }

    public static void showInfoDialog(Context context, boolean cancelable, String title,
                                      OnClickListener clickListener) {
        SystemDialog view = new SystemDialog(context);
        if (infoDialog != null && infoDialog.isShowing()) {
            infoDialog.dismiss();
        }
        systemInfoDialog_ok = (TextView) view.findViewById(R.id.system_info_dialog_ok);
        systemInfoDialog_title = (TextView) view.findViewById(R.id.system_info_dialog_title);
        systemInfoDialog_ok.setOnClickListener(clickListener);
        systemInfoDialog_title.setText(title);
        infoDialog = new AlertDialog.Builder(context).setCancelable(
                cancelable).create();

        infoDialog.setCanceledOnTouchOutside(cancelable);
        Window window = infoDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        infoDialog.show();
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        infoDialog.addContentView(view, params);

    }

    public static void showInfoDialog(Context context, boolean cancelable, String title) {

        showInfoDialog(context, cancelable, title, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (infoDialog != null && infoDialog.isShowing()) {
                    infoDialog.dismiss();
                }
            }
        });
    }

    public static void dismissLoadingDialog() {
        if (loadingdialog != null && loadingdialog.isShowing()) {
            if (view != null) {
                view.stop();
            }
            loadingdialog.dismiss();
        }
    }

    public void onClick(View v) {
        if(v.getId() == R.id.system_info_dialog_ok && listener != null ){
            listener.rightClick(data);
        }
        if(v.getId() == R.id.systemdialog_cancel && listener != null ){
            listener.leftClick(data);
        }

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
