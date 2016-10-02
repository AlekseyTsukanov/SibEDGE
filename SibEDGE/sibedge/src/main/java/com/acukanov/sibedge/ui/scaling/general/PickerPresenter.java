package com.acukanov.sibedge.ui.scaling.general;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.acukanov.sibedge.ui.base.IPresenter;
import com.acukanov.sibedge.utils.LogUtils;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

public class PickerPresenter implements IPresenter<IPickerView> {
    private static final String LOG_TAG = LogUtils.makeLogTag(PickerPresenter.class);
    private IPickerView mScalingView;

    @Inject
    PickerPresenter() {}

    @Override
    public void attachView(IPickerView IView) {
        mScalingView = IView;
    }

    @Override
    public void detachView() {
        mScalingView = null;
    }

    public String takePhoto(Activity activity, Fragment fragment, int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = null;
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            try {
                file = File.createTempFile(
                        System.currentTimeMillis() + "_",
                        ".jpg",
                        activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                );
                Uri photoUri = FileProvider.getUriForFile(
                        activity,
                        activity.getApplicationContext().getPackageName() + ".fileprovider",
                        file
                );
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            } catch (IOException e) {
                Toast.makeText(activity, "The error has been occured while photo creating", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            if (fragment != null) {
                fragment.startActivityForResult(takePictureIntent, requestCode);
            } else {
                activity.startActivityForResult(takePictureIntent, requestCode);
            }
        }
        return "file:" + file.getAbsolutePath();
    }

    public void chooseImageFromGallery(Activity activity, Fragment fragment, int requestCode) {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        if (fragment != null) {
            fragment.startActivityForResult(galleryIntent, requestCode);
        } else {
            activity.startActivityForResult(galleryIntent, requestCode);
        }
    }
}
