package com.chico.library;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created on 2016/9/29.
 * Author Chico Chen
 */
public class VersionManage {
    private static VersionManage manager;
    private ThreadPoolExecutor threadPoolExecutor;
    private VersionDownloadRequest request;

    static {
        manager = new VersionManage();
    }

    public static VersionManage getInstance() {
        return manager;
    }


    private VersionManage() {
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    }

    public void startDownloads(String downloadUrl, String localPath, String apkName, VersionDownloadListener listener) {
        if (request != null) {
            return;
        }
        String apkPath = checkLocalFilePath(localPath, apkName);
        //开始文件的下载任务
        request = new VersionDownloadRequest(downloadUrl, apkPath, listener);
        Future<?> future = threadPoolExecutor.submit(request);
    }

    /**
     * 用来检查文件路径是否已经存在
     *
     * @param localPath
     */
    private String checkLocalFilePath(String localPath, String apkName) {
        File dir = new File(localPath);
        if(!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, apkName);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file.getAbsolutePath();
    }
}
