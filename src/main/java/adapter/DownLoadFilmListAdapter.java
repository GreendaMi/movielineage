package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.listener.OnRetryableFileDownloadStatusListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bean.daoBean.localfilmBean;
import bean.filmBean;
import model.DownLoadManager;
import tool.NetworkType;
import tool.NetworkTypeInfo;
import tool.UI;
import top.greendami.loadingimageview.LoadingImageView;
import top.greendami.movielineage.FilmInfo;
import top.greendami.movielineage.R;
import ui.IconFontTextView;

import static top.greendami.movielineage.R.id.delete_select;
import static top.greendami.movielineage.R.id.text_icon;

/**
 * Created by GreendaMi on 2016/12/12.
 */

public class DownLoadFilmListAdapter extends RecyclerView.Adapter implements OnRetryableFileDownloadStatusListener {

    Context mContext;
    protected List<localfilmBean> mDatas;
    protected LayoutInflater mInflater;

    public int Type = 0;//0:normal,1:delete

    private Map<String, WeakReference<NormalViewHolder>> mConvertViews = new LinkedHashMap<>();
    Handler mHandler;

    public List<localfilmBean> deleteList = new ArrayList<>();


    public DownLoadFilmListAdapter(Context context, List<localfilmBean> datas) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
        mHandler = new Handler();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.download_item, parent, false);
        return new DownLoadFilmListAdapter.NormalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final DownLoadFilmListAdapter.NormalViewHolder mHolder = (DownLoadFilmListAdapter.NormalViewHolder) holder;
        final localfilmBean mData = mDatas.get(position);
        Glide.with(mContext).load(mData.getImg()).asBitmap().diskCacheStrategy(DiskCacheStrategy.RESULT).into(mHolder.bg);
        mHolder.name.setText(mData.getName());

        DownloadFileInfo mDownloadFileInfo = FileDownloader.getDownloadFile(mData.getUrl());
        //下载完成
        final boolean isComplete = mDownloadFileInfo != null && mDownloadFileInfo.getDownloadedSizeLong() == mDownloadFileInfo.getFileSizeLong();
        mHolder.view.setTag(position);
        //控制初始时候的背景
        if (isComplete) {
            mHolder.text.setVisibility(View.GONE);
            mHolder.icon.setVisibility(View.GONE);
            mHolder.bg.setProgress(100);
            mHolder.speed.setText(DownLoadManager.convertFileSize(mDownloadFileInfo.getDownloadedSizeLong()));
        } else {
            //正在下载
            String url = mData.getUrl();
            if (TextUtils.isEmpty(url)) {
                mConvertViews.remove(url);
            }
            if(Type == 0 && NetworkTypeInfo.getNetworkType(mContext) == NetworkType.Wifi){
                DownLoadManager.startDownLoad(mData);
            }else{
                DownLoadManager.pause(mData.getUrl());
            }
            mConvertViews.put(url, new WeakReference<>((NormalViewHolder) holder));
        }

        switch (Type) {
            case 0:
                mHolder.deleteSelect.setVisibility(View.GONE);
                mHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isComplete) {
                            filmBean f = new filmBean();
                            f.setName(mData.getName());
                            f.setFrom(mData.getFrom());
                            f.setUrl(mData.getUrl());
                            f.setIntroduce(mData.getIntroduce());
                            f.setImg(mData.getImg());
                            f.setDate(mData.getDate());
                            f.setTag(mData.getTag());
                            f.setComment(mData.getComment());
                            mHolder.bg.setDrawingCacheEnabled(true);
                            Bitmap bitmap = Bitmap.createBitmap(mHolder.bg.getDrawingCache());
                            mHolder.bg.setDrawingCacheEnabled(false);
                            //如果下载完成，点击播放
                            tool.UI.push(FilmInfo.class, mData, bitmap, mHolder.view.getTop());
                        } else {
                            //如果下载未完成，点击暂停
                            DownLoadManager.toggle(mData);
                        }
                    }
                });
                break;
            case 1:
                mHolder.deleteSelect.setVisibility(View.VISIBLE);
                mHolder.deleteSelect.setText(mContext.getResources().getString(R.string.选择1));
                mHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mHolder.deleteSelect.getText().equals(mContext.getResources().getString(R.string.选择1))){
                            mHolder.deleteSelect.setText(mContext.getResources().getString(R.string.选择2));
                            deleteList.add(mData);
                        }else{
                            mHolder.deleteSelect.setText(mContext.getResources().getString(R.string.选择1));
                            deleteList.remove(mData);
                        }
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    //下载监听器

    @Override
    public void onFileDownloadStatusRetrying(DownloadFileInfo downloadFileInfo, int retryTimes) {
        // 正在重试下载（如果你配置了重试次数，当一旦下载失败时会尝试重试下载），retryTimes是当前第几次重试
        if (downloadFileInfo == null) {
            return;
        }
        WeakReference<NormalViewHolder> weakCacheConvertView = mConvertViews.get(downloadFileInfo.getUrl());
        if (weakCacheConvertView == null || weakCacheConvertView.get() == null) {
            return;
        }
        NormalViewHolder mHolder = weakCacheConvertView.get();
        mHolder.text.setText("正在重试：" + retryTimes);
        mHolder.icon.setText(mContext.getResources().getString(R.string.暂停));
        mHolder.speed.setText(0 + "KB/s");
    }

    @Override
    public void onFileDownloadStatusWaiting(DownloadFileInfo downloadFileInfo) {
        // 等待下载（等待其它任务执行完成，或者FileDownloader在忙别的操作）
        if (downloadFileInfo == null) {
            return;
        }
        WeakReference<NormalViewHolder> weakCacheConvertView = mConvertViews.get(downloadFileInfo.getUrl());
        if (weakCacheConvertView == null || weakCacheConvertView.get() == null) {
            return;
        }
        NormalViewHolder mHolder = weakCacheConvertView.get();
        mHolder.text.setText("排队中");
        mHolder.icon.setText(mContext.getResources().getString(R.string.暂停));
        mHolder.speed.setText(0 + "KB/s");
    }

    @Override
    public void onFileDownloadStatusPreparing(DownloadFileInfo downloadFileInfo) {
        // 准备中（即，正在连接资源）
        if (downloadFileInfo == null) {
            return;
        }
        WeakReference<NormalViewHolder> weakCacheConvertView = mConvertViews.get(downloadFileInfo.getUrl());
        if (weakCacheConvertView == null || weakCacheConvertView.get() == null) {
            return;
        }
        NormalViewHolder mHolder = weakCacheConvertView.get();
        mHolder.text.setText("正在连接");
        mHolder.icon.setText(mContext.getResources().getString(R.string.暂停));
        mHolder.speed.setText(0 + "KB/s");
    }

    @Override
    public void onFileDownloadStatusPrepared(DownloadFileInfo downloadFileInfo) {
        // 已准备好（即，已经连接到了资源）
        if (downloadFileInfo == null) {
            return;
        }
        WeakReference<NormalViewHolder> weakCacheConvertView = mConvertViews.get(downloadFileInfo.getUrl());
        if (weakCacheConvertView == null || weakCacheConvertView.get() == null) {
            return;
        }
        NormalViewHolder mHolder = weakCacheConvertView.get();
        mHolder.text.setText("缓存中");
        mHolder.icon.setText(mContext.getResources().getString(R.string.暂停));
    }

    @Override
    public void onFileDownloadStatusDownloading(DownloadFileInfo downloadFileInfo, float downloadSpeed, long
            remainingTime) {
        // 正在下载，downloadSpeed为当前下载速度，单位KB/s，remainingTime为预估的剩余时间，单位秒
        if (downloadFileInfo == null) {
            return;
        }
        WeakReference<NormalViewHolder> weakCacheConvertView = mConvertViews.get(downloadFileInfo.getUrl());
        if (weakCacheConvertView == null || weakCacheConvertView.get() == null) {
            return;
        }
        NormalViewHolder cacheConvertView = weakCacheConvertView.get();
        cacheConvertView.bg.setProgress((int) (downloadFileInfo.getDownloadedSizeLong() * 100 / downloadFileInfo.getFileSizeLong()));
        cacheConvertView.speed.setText(downloadSpeed + "KB/s");

    }

    @Override
    public void onFileDownloadStatusPaused(DownloadFileInfo downloadFileInfo) {
        // 下载已被暂停
        if (downloadFileInfo == null) {
            return;
        }
        WeakReference<NormalViewHolder> weakCacheConvertView = mConvertViews.get(downloadFileInfo.getUrl());
        if (weakCacheConvertView == null || weakCacheConvertView.get() == null) {
            return;
        }
        NormalViewHolder mHolder = weakCacheConvertView.get();
        mHolder.text.setText("暂停");
        mHolder.icon.setText(mContext.getResources().getString(R.string.播放));
        mHolder.speed.setText("");
    }

    @Override
    public void onFileDownloadStatusCompleted(DownloadFileInfo downloadFileInfo) {
        // 下载完成（整个文件已经全部下载完成）
        if (downloadFileInfo == null) {
            return;
        }
        WeakReference<NormalViewHolder> weakCacheConvertView = mConvertViews.get(downloadFileInfo.getUrl());
        if (weakCacheConvertView == null || weakCacheConvertView.get() == null) {
            return;
        }
        NormalViewHolder mHolder = weakCacheConvertView.get();
        mHolder.text.setVisibility(View.GONE);
        mHolder.icon.setVisibility(View.GONE);
        mHolder.bg.setProgress(100);
        mHolder.speed.setText("");

        this.notifyItemChanged((int)(mHolder.view.getTag()));
    }

    @Override
    public void onFileDownloadStatusFailed(String url, DownloadFileInfo downloadFileInfo, FileDownloadStatusFailReason failReason) {
        // 下载失败了，详细查看失败原因failReason，有些失败原因你可能必须关心

        String failType = failReason.getType();
        String failUrl = failReason.getUrl();// 或：failUrl = url，url和failReason.getType()会是一样的

        if (FileDownloadStatusFailReason.TYPE_URL_ILLEGAL.equals(failType)) {
            // 下载failUrl时出现url错误
        } else if (FileDownloadStatusFailReason.TYPE_STORAGE_SPACE_IS_FULL.equals(failType)) {
            // 下载failUrl时出现本地存储空间不足
            Toast.makeText(UI.TopActivity,"存储空间不足",Toast.LENGTH_SHORT).show();
        } else if (FileDownloadStatusFailReason.TYPE_NETWORK_DENIED.equals(failType)) {
            // 下载failUrl时出现无法访问网络
            Toast.makeText(UI.TopActivity,"无法访问网络",Toast.LENGTH_SHORT).show();
        } else if (FileDownloadStatusFailReason.TYPE_NETWORK_TIMEOUT.equals(failType)) {
            // 下载failUrl时出现连接超时
        } else {
            // 更多错误....
        }

        // 查看详细异常信息
        Throwable failCause = failReason.getCause();// 或：failReason.getOriginalCause()

        // 查看异常描述信息
        String failMsg = failReason.getMessage();// 或：failReason.getOriginalCause().getMessage()
    }

    public class NormalViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public IconFontTextView icon;
        public TextView text;
        public TextView speed;
        public IconFontTextView deleteSelect;
        public LoadingImageView bg;
        public View view;

        public NormalViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            name = (TextView) itemView.findViewById(R.id.name);
            text = (TextView) itemView.findViewById(R.id.text_text);
            speed = (TextView) itemView.findViewById(R.id.speed);
            icon = (IconFontTextView) itemView.findViewById(text_icon);
            deleteSelect = (IconFontTextView) itemView.findViewById(delete_select);

            bg = (LoadingImageView) itemView.findViewById(R.id.img);
        }

    }

}
