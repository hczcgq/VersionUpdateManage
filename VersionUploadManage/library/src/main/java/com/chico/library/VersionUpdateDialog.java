package com.chico.library;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created on 2016/9/29.
 * Author Chico Chen
 */
public class VersionUpdateDialog extends DialogFragment implements View.OnClickListener {

    private String version_code;
    private String version_describe;
    private String version_url;
    private String version_local;
    private String apk_name;

    private Dialog dialog;

    public static VersionUpdateDialog getInstance(Bundle bundle) {
        VersionUpdateDialog fragment = new VersionUpdateDialog();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(VersionConstant.VERSION_CODE)) {
            version_code = getArguments().getString(VersionConstant.VERSION_CODE);
        }

        if (getArguments().containsKey(VersionConstant.VERSION_DESCRIBE)) {
            version_describe = getArguments().getString(VersionConstant.VERSION_DESCRIBE);
        }

        if (getArguments().containsKey(VersionConstant.VERSION_URL)) {
            version_url = getArguments().getString(VersionConstant.VERSION_URL);
        }

        if (getArguments().containsKey(VersionConstant.VERSION_LOCAL)) {
            version_local = getArguments().getString(VersionConstant.VERSION_LOCAL);
        }

        if (getArguments().containsKey(VersionConstant.APK_NAME)) {
            apk_name = getArguments().getString(VersionConstant.APK_NAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.dialog_version, container);
        TextView titleText = (TextView) rootView.findViewById(R.id.tv_version);
        TextView describeText = (TextView) rootView.findViewById(R.id.tv_describe);
        Button cancelBtn = (Button) rootView.findViewById(R.id.btn_cancel);
        Button confirmBtn = (Button) rootView.findViewById(R.id.btn_confirm);

        titleText.setText(String.format(getString(R.string.versioninfo), version_code));
        describeText.setText(Html.fromHtml(version_describe));

        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startDownload();
                }
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_cancel) {
            dialog.dismiss();

        } else if (i == R.id.btn_confirm) {
            checkPermission();
            dialog.dismiss();
        }
    }

    private void checkPermission() {
        boolean hasPermission = (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            startDownload();
        }
    }

    private void startDownload() {
        Intent it = new Intent(getActivity(), VersionService.class);
        it.putExtra(VersionConstant.APK_NAME, apk_name);
        it.putExtra(VersionConstant.VERSION_LOCAL, version_local);
        it.putExtra(VersionConstant.VERSION_URL, version_url);
        getActivity().startService(it);
    }
}
