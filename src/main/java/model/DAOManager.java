package model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import bean.apiBean.base_E;
import bean.apiBean.likebean_E;
import bean.apiBean.whetherRejist_E;
import bean.daoBean.DaoMaster;
import bean.daoBean.DaoSession;
import bean.daoBean.likefilmbean;
import bean.daoBean.likefilmbeanDao;
import bean.daoBean.localfilmBean;
import bean.daoBean.localfilmBeanDao;
import bean.likebean;
import model.Api.Api;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import tool.UI;

/**
 * 数据库操作的对外接口
 * Created by GreendaMi on 2016/12/14.
 */

public class DAOManager {

    private final static String dbName = "ninepoint_db";
    private static DAOManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    public DAOManager(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }

    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static DAOManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DAOManager.class) {
                if (mInstance == null) {
                    mInstance = new DAOManager(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }

    /**
     * 插入一条记录
     *
     * @param localfilmBean
     */
    public void insertDownLoadFilm(localfilmBean localfilmBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        localfilmBeanDao localfilmBeanDao = daoSession.getLocalfilmBeanDao();
        localfilmBeanDao.insert(localfilmBean);
    }

    /**
     * 插入一条记录
     *
     * @param likefilmbean
     */
    public void insertLikeFilm(final likefilmbean likefilmbean) {
        likebean_E likebean_e = new likebean_E();
        likebean_e.setPhone(UI.get("Me"));
        likebean_e.setUrl(likefilmbean.getUrl());
        likebean_e.setName(likefilmbean.getName());
        likebean_e.setTag(likefilmbean.getTag());
        likebean_e.setComment(likefilmbean.getComment());
        likebean_e.setImg(likefilmbean.getImg());
        likebean_e.setFrom(likefilmbean.getFrom());
        likebean_e.setDate(likefilmbean.getDate());
        likebean_e.setIntroduce(likefilmbean.getIntroduce());
        Observable<base_E> Observable = Api.apiService.insertLike(likebean_e);
        Observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                })
                .subscribe(new Subscriber<base_E>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(base_E jsonObjects) {
                        Log.d("DAOManager", jsonObjects.getObjectId());
                        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
                        DaoSession daoSession = daoMaster.newSession();
                        likefilmbeanDao likefilmbeanDao = daoSession.getLikefilmbeanDao();
                        likefilmbean.setBmobID(jsonObjects.getObjectId());
                        likefilmbeanDao.insert(likefilmbean);
                    }
                });

    }

    /**
     * 插入集合
     *
     * @param localfilmBeans
     */
    public void insertDownLoadFilmList(List<localfilmBean> localfilmBeans) {
        if (localfilmBeans == null || localfilmBeans.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        localfilmBeanDao localfilmBeanDao = daoSession.getLocalfilmBeanDao();
        localfilmBeanDao.insertInTx(localfilmBeans);
    }

    /**
     * 删除一条记录
     *
     * @param localfilmBean
     */
    public void deleteDownLoadFilm(localfilmBean localfilmBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        localfilmBeanDao localfilmBeanDao = daoSession.getLocalfilmBeanDao();
        localfilmBeanDao.delete(localfilmBean);

    }

    /**
     * 删除一条记录
     *
     * @param likefilmbean
     */
    public void deleteLikeFilm(final likefilmbean likefilmbean) {

        Observable<base_E> Observable = Api.apiService.deleteLike(likefilmbean.getBmobID());

        Observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                })
                .subscribe(new Subscriber<base_E>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(base_E jsonObjects) {
                        Log.d("DAOManager", jsonObjects.getObjectId());
                        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
                        DaoSession daoSession = daoMaster.newSession();
                        likefilmbeanDao likefilmbeanDao = daoSession.getLikefilmbeanDao();
                        likefilmbeanDao.delete(likefilmbean);
                    }
                });
    }

    /**
     * 更新一条记录
     *
     * @param localfilmBean
     */
    public void updateDownLoadFilm(localfilmBean localfilmBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        localfilmBeanDao localfilmBeanDao = daoSession.getLocalfilmBeanDao();
        localfilmBeanDao.update(localfilmBean);
    }

    /**
     * 查询所有下载列表
     */
    public List<localfilmBean> queryFilmList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        localfilmBeanDao localfilmBeanDao = daoSession.getLocalfilmBeanDao();
        QueryBuilder<localfilmBean> qb = localfilmBeanDao.queryBuilder();
        List<localfilmBean> list = qb.list();
        return list;
    }

    /**
     * 查询下载
     */
    public List<localfilmBean> queryDownLoadFilmList(String id) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        localfilmBeanDao mLocalfilmBeanDao = daoSession.getLocalfilmBeanDao();
        QueryBuilder<localfilmBean> qb = mLocalfilmBeanDao.queryBuilder();
        qb.where(localfilmBeanDao.Properties.ID.eq(id));
        List<localfilmBean> list = qb.list();
        return list;
    }

    /**
     * 查询收藏
     */
    public List<likefilmbean> queryLikeFilmList(String url) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        likefilmbeanDao mlikefilmbeanDao = daoSession.getLikefilmbeanDao();
        QueryBuilder<likefilmbean> qb = mlikefilmbeanDao.queryBuilder();
        qb.where(likefilmbeanDao.Properties.Url.eq(url));
        List<likefilmbean> list = qb.list();
        return list;
    }

    /**
     * 查询收藏列表
     */
    public List<likefilmbean> queryLikeFilmList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        likefilmbeanDao likefilmbeanDao = daoSession.getLikefilmbeanDao();
        QueryBuilder<likefilmbean> qb = likefilmbeanDao.queryBuilder();
        List<likefilmbean> list = qb.list();
        return list;
    }

    /**
     * 更新本地收藏库（登录后调用）
     */
    public void RefreshLikeFilmList(String me) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        final likefilmbeanDao likefilmbeanDao = daoSession.getLikefilmbeanDao();
        likefilmbeanDao.deleteAll();


        Observable<likebean> Observable = Api.apiService.getAllLike(new whetherRejist_E(me).toString());

        Observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                })
                .subscribe(new Subscriber<likebean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(likebean mLikebean) {
                        for (likebean.ResultsBean mResultsBean:mLikebean.getResults()) {
                            likefilmbean mlikefilmbean = new likefilmbean();
                            mlikefilmbean.setBmobID(mResultsBean.getObjectId());
                            mlikefilmbean.setComment(mResultsBean.getComment());
                            mlikefilmbean.setIntroduce(mResultsBean.getIntroduce());
                            mlikefilmbean.setImg(mResultsBean.getImg());
                            mlikefilmbean.setUrl(mResultsBean.getUrl());
                            mlikefilmbean.setName(mResultsBean.getName());
                            mlikefilmbean.setTag(mResultsBean.getTag());
                            mlikefilmbean.setFrom(mResultsBean.getFrom());
                            mlikefilmbean.setDate(mResultsBean.getDate());
                            likefilmbeanDao.insert(mlikefilmbean);
                        }
                    }
                });







    }
}
