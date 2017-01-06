package top.greendami.movielineage;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import org.wlf.filedownloader.FileDownloader;

import bean.daoBean.likefilmbean;
import bean.filmBean;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import model.DAOManager;
import model.DownLoadManager;
import tool.ACache;
import tool.NetworkType;
import tool.NetworkTypeInfo;
import tool.ScreenInfo;
import tool.UI;
import tool.formatTime;
import ui.DotsPreloader;
import ui.IconFontTextView;

/**
 * Created by GreendaMi on 2016/11/29.
 */

public class Player extends Activity implements View.OnTouchListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener, SurfaceHolder.Callback {
    private static final String TAG = "Player";
    @butterknife.Bind(R.id.dotView)
    DotsPreloader mDotView;
    @butterknife.Bind(R.id.back)
    IconFontTextView mBack;
    @butterknife.Bind(R.id.p)
    IconFontTextView mP;
    @butterknife.Bind(R.id.share)
    IconFontTextView mShare;
    @butterknife.Bind(R.id.download)
    IconFontTextView mDownload;
    @butterknife.Bind(R.id.like)
    IconFontTextView mLike;
    @butterknife.Bind(R.id.name)
    TextView mName;
    @butterknife.Bind(R.id.current)
    TextView mCurrent;
    @butterknife.Bind(R.id.length)
    TextView mLength;
    @butterknife.Bind(R.id.timeline)
    SeekBar mTimeline;
    @butterknife.Bind(R.id.playcontrol)
    RelativeLayout mPlaycontrol;

    private int mVideoWidth;
    private int mVideoHeight;
    private MediaPlayer mMediaPlayer;
    private SurfaceView mPreview;
    private SurfaceHolder holder;
    private String path;
    private Bundle extras;
    private static final String MEDIA = "media";
    private static final int LOCAL_AUDIO = 1;
    private static final int STREAM_AUDIO = 2;
    private static final int RESOURCES_AUDIO = 3;
    private static final int LOCAL_VIDEO = 4;
    private static final int STREAM_VIDEO = 5;
    private boolean mIsVideoSizeKnown = false;
    private boolean mIsVideoReadyToBePlayed = false;

    /**
     * 动画
     **/
    Animation mAnimationIn = null, mAnimationOut = null;
    /**
     * 控制界面是否显示
     **/
    boolean isControlling = false;
    /**
     * 控制界面显示时长,10s,超过就隐藏
     **/
    public static final int CONTROLPANEL_KEEPTIME = 10;

    filmBean mFilmBean;
    Handler mHandler;

    PowerManager.WakeLock mWakeLock;

    int isControllingTime = 0;
    int x, y;
    AudioManager mAudioManager ;//音量控制

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.player_layout);
        butterknife.ButterKnife.bind(this);
        mDotView.setVisibility(View.VISIBLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
        //因为使用了子线程，不共享内存，所以使用文件系统传递数据

        ACache mCache = ACache.get(Player.this);
        mFilmBean = (filmBean) mCache.getAsObject("PlayFilm");

        Log.d(TAG, "mFilmBean:" + mFilmBean.getUrl());
        Vitamio.isInitialized(this);
        LibsChecker.checkVitamioLibs(this);

        mPreview = (SurfaceView) findViewById(R.id.surface);
        holder = mPreview.getHolder();
        holder.addCallback(this);
        holder.setFormat(PixelFormat.RGBA_8888);
        extras = getIntent().getExtras();

        mHandler = new Handler();
        initEvent();
        initView();

        /**加载透明动画**/

        mAnimationIn = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_in);
        mAnimationOut = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_out);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);//音量控制,初始化定义


    }

    private void initView() {
        if (DAOManager.getInstance(Player.this).queryLikeFilmList(mFilmBean.getUrl()).size() != 0) {
            mLike.setText(getResources().getString(R.string.收藏2));
        } else {
            mLike.setText(getResources().getString(R.string.收藏1));
        }

    }

    private void initEvent() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                finish();
                System.exit(0);
            }
        });
        mBack.setOnTouchListener(this);
        mP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mP.setText(getResources().getText(R.string.播放));
                } else {
                    mMediaPlayer.start();
                    mP.setText(getResources().getText(R.string.暂停));
                }
                isControllingTime = 0;
            }
        });
        mP.setOnTouchListener(this);

        mPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlPanel();
            }
        });
        mPreview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        //手指按下的时候：初始化 x,y 值
                        x = (int) event.getX();
                        y = (int) event.getY();
                        break;

                    case MotionEvent.ACTION_UP:
                /*
                 * 手指抬起来触发 ，所以判断在这里进行
                 * 1.获得结束的x,y
                 * 2.进行判断
                 */
                        int upx = (int) event.getX();
                        int upy = (int) event.getY();
                        return drawTouch(upx, upy);

                }

                return false;
            }
        });
        if (FileDownloader.getDownloadFile(mFilmBean.getUrl()) == null) {
            mDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DownLoadManager.startDownLoad(mFilmBean);
                    mDownload.setTextColor(getResources().getColor(R.color.DarkFontColor));
                    UI.Toast("下载中");
                }
            });
        } else {
            mDownload.setTextColor(getResources().getColor(R.color.DarkFontColor));
        }

        mDownload.setOnTouchListener(this);

        mLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkTypeInfo.getNetworkType(Player.this) == NetworkType.NoNetwork){
                    UI.Toast("请网络！");
                    return;
                }
                if (UI.get("Me") != null && !UI.get("Me").toString().isEmpty()) {
                    likefilmbean lfb = new likefilmbean();
                    lfb.setName(mFilmBean.getName());
                    lfb.setFrom(mFilmBean.getFrom());
                    lfb.setUrl(mFilmBean.getUrl());
                    lfb.setIntroduce(mFilmBean.getIntroduce());
                    lfb.setImg(mFilmBean.getImg());
                    lfb.setDate(mFilmBean.getDate());
                    lfb.setTag(mFilmBean.getTag());
                    lfb.setComment(mFilmBean.getComment());

                    if (DAOManager.getInstance(Player.this).queryLikeFilmList(mFilmBean.getUrl()).size() == 0) {

                        DAOManager.getInstance(Player.this).insertLikeFilm(lfb);
                        mLike.setText(getResources().getString(R.string.收藏2));
                    } else {
                        lfb = DAOManager.getInstance(Player.this).queryLikeFilmList(mFilmBean.getUrl()).get(0);
                        DAOManager.getInstance(Player.this).deleteLikeFilm(lfb);
                        mLike.setText(getResources().getString(R.string.收藏1));
                    }
                } else {
                    UI.push(LoginActivity.class);
                }
            }
        });
    }

    public void controlPanel() {
        if (isControlling) {
            Log.d(TAG, "隐藏");
            mPlaycontrol.startAnimation(mAnimationOut);

            mAnimationOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mPlaycontrol.setVisibility(View.INVISIBLE);
                    isControlling = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        } else {
            Log.d(TAG, "显示");

            mPlaycontrol.startAnimation(mAnimationIn);

            mAnimationIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mPlaycontrol.setVisibility(View.VISIBLE);
                    isControlling = true;
                    isControllingTime = 0;
                    //如果正在缓冲，则不显示播放键
                    if (needResume) {
                        mP.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWakeLock.acquire();
        UI.enter(this);
        MobclickAgent.onPageStart("Player");
        MobclickAgent.onResume(this);
    }


    private void playVideo(String url) {
        doCleanUp();
        try {
            // Create a new media player and set the listeners
            mMediaPlayer = new MediaPlayer(this);
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.setDisplay(holder);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnVideoSizeChangedListener(this);
            mMediaPlayer.setOnInfoListener(this);
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setBufferSize(2048);
            mDotView.setVisibility(View.INVISIBLE);

        } catch (Exception e) {
            Log.e(TAG, "error: " + e.getMessage(), e);
        }
    }


    public void onBufferingUpdate(MediaPlayer arg0, int percent) {

    }

    public void onCompletion(MediaPlayer arg0) {
        UI.pop();
    }

    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        Log.v(TAG, "onVideoSizeChanged called");
        if (width == 0 || height == 0) {
            Log.e(TAG, "invalid video width(" + width + ") or height(" + height + ")");
            return;
        }
        mIsVideoSizeKnown = true;
        mVideoWidth = width;
        mVideoHeight = height;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            startVideoPlayback();
        }
    }

    public void onPrepared(MediaPlayer mediaplayer) {
        Log.d(TAG, "onPrepared called");
        mIsVideoReadyToBePlayed = true;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            startVideoPlayback();
        }
        mLength.setText(formatTime.formatTime(mMediaPlayer.getDuration()));
        mTimeline.setMax(new Long(mMediaPlayer.getDuration()).intValue());
        mTimeline.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            //进度条抬起事件
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mP.setVisibility(View.INVISIBLE);
                mMediaPlayer.seekTo(seekBar.getProgress());
                mCurrent.setText(formatTime.formatTime(new Integer(seekBar.getProgress()).longValue()));
                mDotView.setVisibility(View.VISIBLE);
                isControllingTime = 0;
            }
        });

        //当前播放进度跟踪
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mMediaPlayer != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                                mCurrent.setText(formatTime.formatTime(mMediaPlayer.getCurrentPosition()));
                                mTimeline.setProgress(new Long(mMediaPlayer.getCurrentPosition()).intValue());
                            }
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        //监听播放控制界面保持的时间，超过6s没有操作，就隐藏
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mMediaPlayer != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (isControlling && isControllingTime > CONTROLPANEL_KEEPTIME) {
                                controlPanel();
                            }
                        }
                    });

                    try {
                        Thread.sleep(1000);
                        isControllingTime++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
        Log.d(TAG, "surfaceChanged called");

    }

    public void surfaceDestroyed(SurfaceHolder surfaceholder) {
        Log.d(TAG, "surfaceDestroyed called");
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated called");

        //判断视频是否下载
        if (FileDownloader.getDownloadFile(mFilmBean.getUrl()) != null &&
                FileDownloader.getDownloadFile(mFilmBean.getUrl()).getDownloadedSizeLong() == FileDownloader.getDownloadFile(mFilmBean.getUrl()).getFileSizeLong()) {
            //播放本地视频文件
            playVideo(FileDownloader.getDownloadFile(mFilmBean.getUrl()).getFilePath());
        } else {
            playVideo(mFilmBean.getUrl());
        }


        mName.setText(mFilmBean.getName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
        doCleanUp();
        mWakeLock.release();
        MobclickAgent.onPageEnd("Player");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
        doCleanUp();
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void doCleanUp() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        mIsVideoReadyToBePlayed = false;
        mIsVideoSizeKnown = false;
    }

    private void startVideoPlayback() {
        Log.v(TAG, "startVideoPlayback");
        holder.setFixedSize(mVideoWidth, mVideoHeight);
        mMediaPlayer.start();
    }

    /**
     * 是否需要自动恢复播放，用于自动暂停，恢复播放
     */
    private boolean needResume;

    @Override
    public boolean onInfo(MediaPlayer arg0, int arg1, int arg2) {
        switch (arg1) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                //开始缓存，暂停播放
                if (arg0.isPlaying()) {
                    //stopPlayer();
                    arg0.pause();
                    needResume = true;
                    //如果是显示控制界面的时候,把播放键换成等待条
                    if (isControlling) {
                        mP.setVisibility(View.INVISIBLE);
                    }
                    mDotView.setVisibility(View.VISIBLE);

                }
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                //缓存完成，继续播放
                if (needResume) {
                    mDotView.setVisibility(View.INVISIBLE);
                    if (isControlling) {
                        if (mP.getText().equals(getResources().getText(R.string.暂停))) {
                            mP.setVisibility(View.VISIBLE);
                            arg0.start();
                            needResume = false;
                        }
                    } else {
                        arg0.start();
                        needResume = false;
                    }

                }

                break;
            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                //显示 下载速度
                Log.d(TAG, "download rate:" + arg2);
                break;
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

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        mP.setVisibility(View.VISIBLE);
        mDotView.setVisibility(View.INVISIBLE);
        if (mP.getText().equals(getResources().getString(R.string.暂停))) {
            mp.start();
        }
    }

    //手势控制
    private boolean drawTouch(int upx, int upy) {
        boolean flg = false;
        int MoveDistance = ViewConfiguration.get(Player.this).getScaledDoubleTapSlop() / 2;
        //水平滑动
        if (upx - x > MoveDistance) {
            long moveto = mMediaPlayer.getCurrentPosition() + (mMediaPlayer.getDuration() / 100) * (upx - x) / (MoveDistance / 2);
            moveto = moveto > mMediaPlayer.getDuration() ? mMediaPlayer.getDuration() - 5000 : mMediaPlayer.getCurrentPosition() + (mMediaPlayer.getDuration() / 100) * (upx - x) / (MoveDistance / 2);
            UI.Toast(formatTime.formatTime(moveto));
            mMediaPlayer.seekTo(moveto);
            flg = true;
        } else if (x - upx > MoveDistance) {
            long moveto = mMediaPlayer.getCurrentPosition() - (mMediaPlayer.getDuration() / 100) * (x - upx) / (MoveDistance / 2);
            moveto = moveto < 0 ? 0 : mMediaPlayer.getCurrentPosition() - (mMediaPlayer.getDuration() / 100) * (x - upx) / (MoveDistance / 2);
            UI.Toast(formatTime.formatTime(moveto));
            mMediaPlayer.seekTo(moveto);
            flg = true;
        } else if (upy - y > MoveDistance) {
            if (x < ScreenInfo.get().widthPixel / 2) {
                //左边，亮度调节
                int light = 255;
                if (upy < ScreenInfo.get().heightPixel * 0.2) {
                    light = 0;
                } else if (upy >= ScreenInfo.get().heightPixel * 0.2 && upy <= ScreenInfo.get().heightPixel * 0.8) {

                    light = (int) ((upy - ScreenInfo.get().heightPixel * 0.2) / (ScreenInfo.get().heightPixel * 0.6) * 255);
                }
                Log.d(TAG, "light:" + light);
                setLight(light);
                UI.Toast("亮度调节：" + light * 100 / 255 + "%");
            } else {

                int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//最大音量
                int tempVolume = 0;
                if (upy < ScreenInfo.get().heightPixel * 0.2) {
                    tempVolume = 0;
                } else if (upy >= ScreenInfo.get().heightPixel * 0.2 && upy <= ScreenInfo.get().heightPixel * 0.8) {

                    tempVolume = (int) ((upy - ScreenInfo.get().heightPixel * 0.2) / (ScreenInfo.get().heightPixel * 0.6) * maxVolume);
                }
                Log.d(TAG, "tempVolume:" + tempVolume);
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, tempVolume, 0); //tempVolume:音量
                UI.Toast("音量调节：" + tempVolume * 100 / maxVolume + "%");
            }

            flg = true;
        } else if (y - upy > MoveDistance) {
            if (x < ScreenInfo.get().widthPixel / 2) {
                int light = 255;
                if (upy < ScreenInfo.get().heightPixel * 0.2) {
                    light = 0;
                } else if (upy >= ScreenInfo.get().heightPixel * 0.2 && upy <= ScreenInfo.get().heightPixel * 0.8) {

                    light = (int) ((upy - ScreenInfo.get().heightPixel * 0.2) / (ScreenInfo.get().heightPixel * 0.6) * 255);
                }
                Log.d(TAG, "light:" + light);
                setLight(light);
                UI.Toast("亮度调节：" + light * 100 / 255 + "%");
            }else{
                int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//最大音量
                int tempVolume = 0;
                if (upy < ScreenInfo.get().heightPixel * 0.2) {
                    tempVolume = 0;
                } else if (upy >= ScreenInfo.get().heightPixel * 0.2 && upy <= ScreenInfo.get().heightPixel * 0.8) {

                    tempVolume = (int) ((upy - ScreenInfo.get().heightPixel * 0.2) / (ScreenInfo.get().heightPixel * 0.6) * maxVolume);
                }
                Log.d(TAG, "maxVolume:" + tempVolume);
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, tempVolume, 0); //tempVolume:音量
                UI.Toast("音量调节：" + tempVolume * 100 / maxVolume + "%");
            }

            flg = true;
        }
        return flg;
    }

    //屏幕亮度调节
    private void setLight(int brightness) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
        getWindow().setAttributes(lp);

    }

}
