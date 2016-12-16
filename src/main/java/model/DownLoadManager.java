package model;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.wlf.filedownloader.FileDownloadConfiguration;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.listener.OnDetectBigUrlFileListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bean.daoBean.localfilmBean;
import bean.filmBean;
import tool.MD5;

/**
 * 视频下载
 * Created by GreendaMi on 2016/12/12.
 */

public class DownLoadManager {
    static final List<String> isDownLoading = new ArrayList<>();

    static String DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
            "NinePoint";

    static Context mContext;

    public static void initDownLoadManager(Context context){
        mContext = context;

        // 1、创建Builder
        FileDownloadConfiguration.Builder builder = new FileDownloadConfiguration.Builder(context);

        // 2.配置Builder
        // 配置下载文件保存的文件夹
        builder.configFileDownloadDir(DIR);
        // 配置同时下载任务数量，如果不配置默认为2
        builder.configDownloadTaskSize(10);
        // 配置失败时尝试重试的次数，如果不配置默认为0不尝试
        builder.configRetryDownloadTimes(5);
        // 开启调试模式，方便查看日志等调试相关，如果不配置默认不开启
        builder.configDebugMode(true);
        // 配置连接网络超时时间，如果不配置默认为15秒
        builder.configConnectTimeout(25000);// 25秒

        // 3、使用配置文件初始化FileDownloader
        FileDownloadConfiguration configuration = builder.build();
        FileDownloader.init(configuration);
    }

    public static void startDownLoad(filmBean f){
        final localfilmBean lf = new localfilmBean();

        lf.setComment(f.getComment());
        lf.setFrom(f.getFrom());
        lf.setImg(f.getImg());
        lf.setName(f.getName());
        lf.setUrl(f.getUrl());
        lf.setDate(f.getDate());
        lf.setTag(f.getTag());
        lf.setIntroduce(f.getIntroduce());

        //转换成本低保存模型
        lf.setID(MD5.compute(f.getUrl()));
        lf.setBuildDate(new Date().toString());
        lf.setIsNew("1");
        lf.setPath(DIR + "/" + lf.getID());


        //开始缓存封面
        FileDownloader.detect(f.getImg(), new OnDetectBigUrlFileListener() {
            @Override
            public void onDetectNewDownloadFile(String url, String fileName, String saveDir, long fileSize) {
                // 如果有必要，可以改变文件名称fileName和下载保存的目录saveDir
                FileDownloader.createAndStart(url, DIR + File.separatorChar + "IMG" , lf.getID());
                lf.setImg(DIR + File.separatorChar + "IMG" + File.separatorChar + lf.getID());
                Log.d("DownLoadManager", (DIR + File.separatorChar + "IMG" + File.separatorChar + lf.getID()));
            }
            @Override
            public void onDetectUrlFileExist(String url) {
                // 继续下载，自动会断点续传（如果服务器无法支持断点续传将从头开始下载）
                FileDownloader.start(url);
            }
            @Override
            public void onDetectUrlFileFailed(String url, DetectBigUrlFileFailReason failReason) {
                // 探测一个网络文件失败了，具体查看failReason
            }
        });

        //开始缓存视频
        FileDownloader.detect(f.getUrl(), new OnDetectBigUrlFileListener() {
            @Override
            public void onDetectNewDownloadFile(String url, String fileName, String saveDir, long fileSize) {
                // 如果有必要，可以改变文件名称fileName和下载保存的目录saveDir
                FileDownloader.createAndStart(url, DIR , lf.getID().toString());
                isDownLoading.add(url);
            }
            @Override
            public void onDetectUrlFileExist(String url) {
                // 继续下载，自动会断点续传（如果服务器无法支持断点续传将从头开始下载）
                FileDownloader.start(url);
                isDownLoading.add(url);
            }
            @Override
            public void onDetectUrlFileFailed(String url, DetectBigUrlFileFailReason failReason) {
                // 探测一个网络文件失败了，具体查看failReason
            }
        });

        if(DAOManager.getInstance(mContext).queryFilmList(lf.getID()).size() == 0){
            DAOManager.getInstance(mContext).insertFilm(lf);
        }

    }


    //下载状态转换
    public static void toggle(filmBean f){
        if(isDownLoading.contains(f.getUrl())){
            FileDownloader.pause(f.getUrl());
            isDownLoading.remove(f.getUrl());
        }else{
            FileDownloader.start(f.getUrl());
            isDownLoading.add(f.getUrl());
        }
    }

    public static void pause(String s){
        FileDownloader.pause(s);
        isDownLoading.remove(s);
    }

    //文件大小显示方式转换
    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }

}
