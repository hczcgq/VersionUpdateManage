package com.chico.library;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import java.io.File;

/**
 * Created on 2016/9/29.
 * Author Chico Chen
 */
public class VersionService extends Service {

    private String version_url;
    private String version_local;
    private String apk_name;

    private NotificationManager notificationManager;
    private Notification notification;
    private int notification_id = 19172439;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            stopSelf();
        }

        version_url = intent.getStringExtra(VersionConstant.VERSION_URL);
        version_local = intent.getStringExtra(VersionConstant.VERSION_LOCAL);
        apk_name = intent.getStringExtra(VersionConstant.APK_NAME);

        initNotification();
        startDownload();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startDownload() {
        VersionManage.getInstance().startDownloads(version_url, version_local, apk_name, new VersionDownloadListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgressChanged(int progress, String downloadUrl) {
                notification.contentView.setProgressBar(R.id.down_pb, 100,
                        progress, false);
                showNotification();
            }

            @Override
            public void onFinished(int completeSize, String downloadUrl) {
                notificationManager.cancel(notification_id);
                installApk();
                stopSelf();
            }

            @Override
            public void onFailure() {
                notificationManager.cancel(notification_id);
                stopSelf();
            }
        });
    }


    private void initNotification() {
        notification = new Notification(R.drawable.ic_launcher, "   "
                + getString(R.string.apk_down),
                System.currentTimeMillis());
        notification.contentView = new RemoteViews(getPackageName(),
                R.layout.view_setting_version_down);
        notification.contentView.setProgressBar(R.id.down_pb, 100, 0, false);
        Intent notificationIntent = new Intent(this, VersionService.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        notification.contentIntent = contentIntent;
        showNotification();
    }

    public void showNotification() {
        notificationManager.notify(notification_id, notification);
    }


    public void installApk() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(version_local, apk_name)), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
