package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.List;

import bean.filmBean;
import top.greendami.movielineage.FilmInfo;
import top.greendami.movielineage.R;

/**
 * Created by GreendaMi on 2016/11/29.
 */

public class FilmListAdapter extends RecyclerView.Adapter {

    Context mContext;
    protected List<filmBean> mDatas;
    protected LayoutInflater mInflater;

    Handler mHandler;

    public FilmListAdapter(Context context,  List<filmBean> datas)
    {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
        mHandler = new Handler();
    }

    /**
     * 加载视图
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v2 = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.film_item, parent, false);
        return new FilmViewHolder(v2);

    }

    /**
     * 绑定数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final filmBean mData = mDatas.get(position);
        RecyclerView.ViewHolder mHolder;
        final FilmViewHolder mFilmViewHolder = (FilmViewHolder)holder;

        //当图片加载完毕，再绑定点击事件
        Glide.with(mContext).load(mData.getImg()).into(new GlideDrawableImageViewTarget(mFilmViewHolder.bg){
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
                mFilmViewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {

                        //隐藏文字
                        mFilmViewHolder.from.setAlpha(0);
                        mFilmViewHolder.name.setAlpha(0);
                        mFilmViewHolder.tag.setAlpha(0);

                        mFilmViewHolder.bg.setDrawingCacheEnabled(true);
                        Bitmap bitmap = Bitmap.createBitmap(mFilmViewHolder.bg.getDrawingCache());
                        mFilmViewHolder.bg.setDrawingCacheEnabled(false);
                        if(tool.UI.getData(1) != null && tool.UI.getData(1) instanceof Bitmap){
                            ((Bitmap)(tool.UI.getData(1))).recycle();
                        }
                        tool.UI.push(FilmInfo.class, mData ,bitmap,view.getTop());

                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mFilmViewHolder.from.setAlpha(1);
                                mFilmViewHolder.name.setAlpha(1);
                                mFilmViewHolder.tag.setAlpha(1);

                            }
                        }, 300);

                    }
                });
            }
        });
        mFilmViewHolder.from.setText(mData.getFrom());
        mFilmViewHolder.name.setText(mData.getName());
        mFilmViewHolder.tag.setText(mData.getTag());

    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    public class FilmViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView tag;
        public TextView from;
        public ImageView bg;
        public View view;

        public View getView() {
            return view;
        }

        public FilmViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.filmItem);
            name = (TextView) itemView.findViewById(R.id.name);
            tag = (TextView) itemView.findViewById(R.id.tag);
            from = (TextView) itemView.findViewById(R.id.from);

            bg = (ImageView) itemView.findViewById(R.id.bg);
        }

    }
}