package adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import bean.daoBean.likefilmbean;
import bean.filmBean;
import tool.ACache;
import tool.UI;
import top.greendami.movielineage.Player;
import top.greendami.movielineage.R;
import ui.IconFontTextView;

/**
 * Created by GreendaMi on 2016/12/19.
 */

public class LikeFilmListAdapter extends RecyclerView.Adapter {
    Context mContext;
    protected List<likefilmbean> mDatas;
    protected LayoutInflater mInflater;

    public int Type = 0;//0:normal,1:delete
    public List<likefilmbean> deleteList = new ArrayList<>();
    Handler mHandler;
    public LikeFilmListAdapter(Context context, List<likefilmbean> datas) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
        mHandler = new Handler();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.like_item, parent, false);
        return new LikeFilmListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final LikeFilmListAdapter.ViewHolder mHolder = (LikeFilmListAdapter.ViewHolder) holder;
        final likefilmbean mData = mDatas.get(position);
        Glide.with(mContext).load(mData.getImg()).asBitmap().diskCacheStrategy(DiskCacheStrategy.RESULT).into(mHolder.bg);
        mHolder.name.setText(mData.getName());
        mHolder.from.setText(mData.getFrom());

        switch (Type) {
            case 0:
                mHolder.bf.setVisibility(View.VISIBLE);
                mHolder.deleteSelect.setVisibility(View.INVISIBLE);
                mHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mHolder.bf.setAlpha(0);
                        filmBean f = new filmBean();
                        f.setName(mData.getName());
                        f.setFrom(mData.getFrom());
                        f.setUrl(mData.getUrl());
                        f.setIntroduce(mData.getIntroduce());
                        f.setImg(mData.getImg());
                        f.setDate(mData.getDate());
                        f.setTag(mData.getTag());
                        f.setComment(mData.getComment());
                        //如果下载完成，点击播放
                        ACache mCache = ACache.get(mContext);
                        mCache.put("PlayFilm", f);
                        UI.push(Player.class);

                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mHolder.bf.setAlpha(1);

                            }
                        }, 300);
                    }
                });
                break;
            case 1:
                mHolder.bf.setVisibility(View.INVISIBLE);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView from;
        public IconFontTextView deleteSelect;
        public IconFontTextView bf;
        public ImageView bg;
        public View view;
        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            name = (TextView) itemView.findViewById(R.id.name);
            from = (TextView) itemView.findViewById(R.id.from);
            deleteSelect = (IconFontTextView) itemView.findViewById(R.id.delete_select);
            bf = (IconFontTextView) itemView.findViewById(R.id.play);

            bg = (ImageView) itemView.findViewById(R.id.img);
        }

    }
}
