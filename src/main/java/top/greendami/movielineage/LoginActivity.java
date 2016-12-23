package top.greendami.movielineage;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import bean.apiBean.base_E;
import bean.apiBean.login_E;
import bean.apiBean.whetherRejist_E;
import bean.userbean;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import model.Api.Api;
import model.DAOManager;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import tool.UI;
import ui.ChButton;
import ui.IconFontTextView;

/**
 * Created by GreendaMi on 2016/12/9.
 */

public class LoginActivity extends Activity implements View.OnTouchListener {
    @Bind(R.id.backBt)
    IconFontTextView mBackBt;
    @Bind(R.id.phone)
    EditText mPhone;
    @Bind(R.id.yzm)
    EditText mYzm;
    @Bind(R.id.yzm_button)
    ChButton mYzmButton;
    @Bind(R.id.ok)
    ChButton mOk;

    int YZMTIME = 60;
    private TimerTask timerTask;
    private Timer timer;
    private Handler handler;

    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initEvent();
    }

    private void initEvent() {
        handler = new Handler();
        mBackBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        mYzmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMobile(mPhone.getText().toString().trim())) {
                    SMSSDK.getVerificationCode("86", mPhone.getText().toString().trim());
                    startCount(handler);
                } else {
                    UI.Toast("请输入正确手机号");
                }
            }
        });
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0) {
                    UI.Toast("请输入手机号，获取验证码");
                    return;
                }
                if (TextUtils.isEmpty(mYzm.getText().toString().trim())) {
                    UI.Toast("请输入验证码");
                    return;
                }
                if (!isMobile(mPhone.getText().toString().trim())) {
                    UI.Toast("请输入正确手机号");
                    return;
                }
                SMSSDK.submitVerificationCode("86", mPhone.getText().toString().trim(), mYzm.getText().toString().trim());

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        UI.enter(this);
        EventHandler eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成

                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        UI.Save("Me", mPhone.getText().toString().trim());
                        final String loingTime = new Date().toString();
                        UI.Save("LOGINTIME", loingTime);
                        new DAOManager(LoginActivity.this).RefreshLikeFilmList(mPhone.getText().toString().trim());
                        //向服务器提交
                        Observable<userbean> Observable = Api.apiService.whetherRejist(new whetherRejist_E(mPhone.getText().toString().trim()).toString());
                        Observable.subscribeOn(Schedulers.io())
                                .unsubscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .doOnError(new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        throwable.printStackTrace();
                                    }
                                })
                                .flatMap(new Func1<userbean, rx.Observable<?>>() {
                                    @Override
                                    public Observable<base_E> call(userbean userbean) {

                                        if (userbean.getResults().size() > 0) {
                                            return Api.apiService.Login(userbean.getResults().get(0).getObjectId(), new login_E(loingTime));
                                        } else {
                                            return Api.apiService.Rejist(new whetherRejist_E(mPhone.getText().toString().trim()));
                                        }

                                    }
                                })
                                .subscribe(new Subscriber<Object>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void onNext(Object jsonObjects) {
                                        if( ((base_E) jsonObjects).getCode() == 0){
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    UI.Toast("登录成功！");
                                                    UI.pop();
                                                }
                                            });
                                        }

                                    }
                                });
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        UI.Toast("请注意查收短信息");
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(((Throwable)data).getMessage());
                        UI.Toast(jsonObject.optString("detail"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    @Override
    protected void onPause() {
        super.onPause();
        SMSSDK.unregisterAllEventHandler();
    }

    private void back() {
        finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
        }
        return true;
    }

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

    /**
     * 验证手机格式
     */
    public boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][35678]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }

    /**
     * 获取验证码按钮的计时器
     */
    public void startCount(final Handler handler) {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (YZMTIME > 0) {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            mYzmButton.setText("" + YZMTIME);
                            mYzmButton.setClickable(false);
                            //进入不可编辑状态
                            mPhone.setFocusable(false);
                            mPhone.setFocusableInTouchMode(false);
                            flag = 1;
                        }
                    });
                } else {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            mYzmButton.setText("重新获取");
                            YZMTIME = 30 + 1;
                            mYzmButton.setClickable(true);
                            //进入不可编辑状态
                            mPhone.setFocusable(true);
                            mPhone.setFocusableInTouchMode(true);
                            timer.cancel();
                            flag = 0;
                        }
                    });
                }
                YZMTIME--;
            }
        };
        timer.schedule(timerTask, 0, 1000);

    }
}
