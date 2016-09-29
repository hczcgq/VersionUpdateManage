package com.chico.android;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import com.chico.library.VersionConstant;
import com.chico.library.VersionUpdateDialog;

public class MainActivity extends AppCompatActivity {

    private String descibe = "3.1.0版本更新日志: <br> 1.APP正式更名，以后我就叫“嗨呀”啦~  <br> 2.个人中心增加消息提醒，麻麻再也不用担心我收不到消息了  <br> 3.弹幕可以点赞了哟，弹幕更好玩啦  <br> 4.帖子增加了踩的功能，让那些辣鸡帖子见鬼去吧  <br> 5.修复了若干BUG，用起来更加流畅啦";
    private String local = Environment.getExternalStorageDirectory() + "/apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startDownload();
    }


    private void startDownload() {
        Bundle bundle = new Bundle();
        bundle.putString(VersionConstant.VERSION_CODE, "2.1.1");
        bundle.putString(VersionConstant.VERSION_DESCRIBE, descibe);
        bundle.putString(VersionConstant.VERSION_URL, "http://static.lolfun.cn/HighYa_v3.1.0_trex_program3-release.apk");
        bundle.putString(VersionConstant.VERSION_LOCAL, local);
        bundle.putString(VersionConstant.APK_NAME, "HighYa.apk");
        VersionUpdateDialog dialog = VersionUpdateDialog.getInstance(bundle);
        dialog.show(getSupportFragmentManager(), null);
    }
}
