package com.chico.library;

/**
 * Created on 2016/9/29.
 * Author Chico Chen
 * 事件的监听回调
 */
public interface VersionDownloadListener {
    /**
     * 下载请求开始回调
     */
    void onStarted();

    /**
     * 进度更新回调
     *
     * @param progress
     * @param downloadUrl
     */
    void onProgressChanged(int progress, String downloadUrl);

    /**
     * 下载完成回调
     *
     * @param completeSize
     * @param downloadUrl
     */
    void onFinished(int completeSize, String downloadUrl);

    /**
     * 下载失败回调
     */
    void onFailure();
}
