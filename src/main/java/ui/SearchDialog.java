package ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import tool.DensityUtil;
import tool.NetworkType;
import tool.NetworkTypeInfo;
import tool.ScreenInfo;
import tool.UI;
import top.greendami.movielineage.MainActivity;
import top.greendami.movielineage.R;
import top.greendami.movielineage.SearchResultActivity;

/**
 * 打开搜索对话框
 * Created by GreendaMi on 2017/1/4.
 */

public class SearchDialog {


    public static void Show(final Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_search, null);
        final IconFontTextView searchButton = (IconFontTextView)view.findViewById(R.id.search_button);
        final EditText edit = (EditText)view.findViewById(R.id.edit);

        final AlertDialog mDialog = new AlertDialog.Builder(context,R.style.Dialog_Fullscreen).setView(view).create();
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                InputMethodManager imm = ( InputMethodManager ) edit.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
                if ( imm.isActive( ) ) {
                    imm.hideSoftInputFromWindow( edit.getApplicationWindowToken( ) , 0 );

                }
            }
        });
        mDialog.show();
        setDialogSize(mDialog,context);
        edit.requestFocus();
        //打开键盘
        InputMethodManager imm = ( InputMethodManager ) edit.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
        imm.showSoftInputFromInputMethod(edit.getWindowToken(), 0);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkTypeInfo.getNetworkType(context) == NetworkType.NoNetwork) {
                    UI.Toast("请连接网络！");
                    return;
                }
                if(!edit.getText().toString().trim().isEmpty()){
                    mDialog.dismiss();
                    UI.push(SearchResultActivity.class, edit.getText().toString().trim());
                    UI.TopActivity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                }
            }
        });
        searchButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    view.setScaleX(0.95f);
                    view.setScaleY(0.95f);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.setScaleX(1f);
                    view.setScaleY(1f);
                }
                return false;
            }
        });

        edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //处理事件
                    searchButton.callOnClick();
                }
                return false;
            }
        });

    }

    private static void setDialogSize(Dialog dg,Context context) {

        WindowManager windowManager = ((MainActivity)context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dg.getWindow().getAttributes();
        lp.width = (int)(display.getWidth()); //设置宽度



        Window dialogWindow = dg.getWindow();


//        dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
//显示的坐标
        lp.x = 0;
        lp.y = 0;
        int width = ScreenInfo.get().widthPixel;
        int height = DensityUtil.dip2px(context,55);
//dialog的大小
        lp.width = width;
        lp.height = height;


        dg.getWindow().setAttributes(lp);
    }
}
