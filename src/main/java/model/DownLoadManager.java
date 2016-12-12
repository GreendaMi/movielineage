package model;

import android.content.Context;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.wlf.filedownloader.FileDownloadConfiguration;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.listener.OnDetectBigUrlFileListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bean.filmBean;
import bean.localfilmBean;
import tool.ACache;
import tool.MD5;
import tool.UI;

/**
 * 视频下载
 * Created by GreendaMi on 2016/12/12.
 */

public class DownLoadManager {

    static String DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
            "NinePoint";

    public static void initDownLoadManager(Context context){
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
        lf.setId(MD5.compute(f.getUrl()));
        lf.setBuildDate(new Date().toString());
        lf.setIsNew("1");
        lf.setPath(DIR + "/" + lf.getId());

        //保存本地视频信息
        ACache mCache = ACache.get(UI.TopActivity);
        List<localfilmBean> localfilmList;
        if(mCache.getAsString("films") != null){
            localfilmList = new Gson().fromJson(mCache.getAsString("films"), new TypeToken<List<localfilmBean>>(){}.getType());
            for (localfilmBean lfb:localfilmList) {
                if(lfb.getId().equals(lf.getId())){
                    localfilmList.remove(lfb);
                    break;
                }
            }
            localfilmList.add(lf);
            mCache.put("films",new Gson().toJson(localfilmList));
        }else{
            localfilmList = new ArrayList<>();
            localfilmList.add(lf);
            mCache.put("films",new Gson().toJson(localfilmList));
        }

        //开始缓存
        FileDownloader.detect(f.getUrl(), new OnDetectBigUrlFileListener() {
            @Override
            public void onDetectNewDownloadFile(String url, String fileName, String saveDir, long fileSize) {
                // 如果有必要，可以改变文件名称fileName和下载保存的目录saveDir
                FileDownloader.createAndStart(url, DIR , lf.getId());
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
    }

}
