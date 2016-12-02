package tool;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;


/**
 * Created by zhaopy on 2015/11/11.
 */
public class UI {
    public static Activity TopActivity;

    public static Object[] passedData;
    private static Object[] returnData;

    public static final String PREFERENCES_NAME = "GreendaMi";


    public static void enter(Activity activity){
        TopActivity = activity;
    }

    private static void pushActivity(Class<?> activityClass, Object[] data){
        Intent intent = new Intent(TopActivity, activityClass);
        passedData = data;
        returnData = null;
        TopActivity.startActivity(intent);
//        TopActivity.overridePendingTransition(R.anim.in, R.anim.out);

    }

    public static void push(Class<?> activityClass){
        pushActivity(activityClass, null);
    }
    public static void push(Class<?> activityClass, Object... data) {
        pushActivity(activityClass, data);
    }

    public static void popAcivity(Object[] data) {
        returnData = data;
        TopActivity.finish();
    }

    public static void pop(Object... data) {
        popAcivity(data);
    }
    public static void pop() {
        popAcivity(null);
    }

    public static Object getReturnData(int index) {
        Object data = returnData != null && returnData.length > index ? returnData[index]
                : null;
        returnData = null;
        return data;
    }
    public static Object getReturnData() {
        return getReturnData(0);
    }
    public static void setReturnData(Object[] data) {
        returnData = data;
    }

    public static Object getData(int index) {
        return passedData != null && passedData.length > index ? passedData[index]
                : null;
    }

    public static Object getData() {
        return getData(0);
    }

    public static void Toast(final String Message){
        TopActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TopActivity, Message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 本地化数据
     * @param k
     * @param v
     */
    public static void Save(String k,String v){
        SharedPreferences mSharedPreferences = TopActivity.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
        mSharedPreferences.edit().putString(k,v).commit();
    }

    public static String get(String k){
        SharedPreferences mSharedPreferences = TopActivity.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
        if(mSharedPreferences.contains(k)){
            return mSharedPreferences.getString(k,"");
        }
        return "";
    }

    public static boolean IsContain(String k){
        SharedPreferences mSharedPreferences = TopActivity.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
        return mSharedPreferences.contains(k);
    }
}
