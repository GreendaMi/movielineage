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
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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

    final int DATE = 1 ,FILM = 2;
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
        switch (viewType) {
            case DATE:
                View v1 = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.date_item, parent, false);
                return new DateViewHolder(v1);
            case FILM:
                View v2 = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.film_item, parent, false);
                return new FilmViewHolder(v2);
        }
        return null;

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
        switch (mData.getType()){
            case DATE:
                DateViewHolder mDateViewHolder = (DateViewHolder)holder;
                mDateViewHolder.date.setText(mData.getName());
                break;
            case FILM:
                final FilmViewHolder mFilmViewHolder = (FilmViewHolder)holder;
                Glide.with(mContext).load(mData.getImg()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(mFilmViewHolder.bg);
                mFilmViewHolder.from.setText(mData.getFrom());
                mFilmViewHolder.name.setText(mData.getName());
                mFilmViewHolder.tag.setText(mData.getTag());

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
                        if(tool.UI.getData(1) != null){
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


                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    public class DateViewHolder extends RecyclerView.ViewHolder {
        public TextView date;

        public DateViewHolder(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.date);
        }
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