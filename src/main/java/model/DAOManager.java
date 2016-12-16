package model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import bean.daoBean.DaoMaster;
import bean.daoBean.DaoSession;
import bean.daoBean.localfilmBean;
import bean.daoBean.localfilmBeanDao;

/**
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
    public void insertFilm(localfilmBean localfilmBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        localfilmBeanDao localfilmBeanDao = daoSession.getLocalfilmBeanDao();
        localfilmBeanDao.insert(localfilmBean);
    }

    /**
     * 插入集合
     *
     * @param localfilmBeans
     */
    public void insertFilmList(List<localfilmBean> localfilmBeans) {
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
    public void deleteFilm(localfilmBean localfilmBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        localfilmBeanDao localfilmBeanDao = daoSession.getLocalfilmBeanDao();
        localfilmBeanDao.delete(localfilmBean);
    }

    /**
     * 更新一条记录
     *
     * @param localfilmBean
     */
    public void updateFilm(localfilmBean localfilmBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        localfilmBeanDao localfilmBeanDao = daoSession.getLocalfilmBeanDao();
        localfilmBeanDao.update(localfilmBean);
    }

    /**
     * 查询用户列表
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
     * 查询用户列表
     */
    public List<localfilmBean> queryFilmList(String id) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        localfilmBeanDao mLocalfilmBeanDao = daoSession.getLocalfilmBeanDao();
        QueryBuilder<localfilmBean> qb = mLocalfilmBeanDao.queryBuilder();
        qb.where(localfilmBeanDao.Properties.ID.eq(id));
        List<localfilmBean> list = qb.list();
        return list;
    }
}
