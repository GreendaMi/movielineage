package ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import tool.DensityUtil;
import tool.ScreenInfo;
import top.greendami.movielineage.MainActivity;
import top.greendami.movielineage.R;

/**
 * 打开搜索对话框
 * Created by GreendaMi on 2017/1/4.
 */

public class SearchDialog {


    public static void Show(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_search, null);
        IconFontTextView searchButton = (IconFontTextView)view.findViewById(R.id.search_button);
        final EditText edit = (EditText)view.findViewById(R.id.edit);


        AlertDialog mDialog = new AlertDialog.Builder(context,R.style.Dialog_Fullscreen).setView(view).create();
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
