package com.acukanov.sibedge.ui.scaling.general;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.acukanov.sibedge.R;
import com.acukanov.sibedge.ui.base.BaseActivity;
import com.acukanov.sibedge.ui.base.BaseFragment;
import com.acukanov.sibedge.ui.scaling.browse.ScalingActivity;
import com.acukanov.sibedge.utils.DialogFactory;
import com.acukanov.sibedge.utils.LogUtils;
import com.acukanov.sibedge.utils.PermissionsUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PickerFragment extends BaseFragment implements IPickerView, View.OnClickListener {
    private static final String LOG_TAG = LogUtils.makeLogTag(PickerFragment.class);
    private static final int REQUEST_PERMISSION_CAMERA = 0;
    private static final int REQUEST_GALLERY = 0;
    private static final int REQUEST_CAMERA = 1;
    private Activity mActivity;
    @Inject PickerPresenter mPickerPresenter;
    @InjectView(R.id.btn_open_gallery) ImageButton btnOpenGallery;
    @InjectView(R.id.btn_open_camera) ImageButton btnOpenCamera;
    private String mPhotoPath;

    public PickerFragment() {}

    public static PickerFragment newInstance() {
        return new PickerFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) mActivity).activityComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_picker, container, false);
        ButterKnife.inject(this, rootView);
        mPickerPresenter.attachView(this);

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPickerPresenter.detachView();
    }

    @Override @OnClick({R.id.btn_open_gallery, R.id.btn_open_camera})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open_gallery:
                mPickerPresenter.chooseImageFromGallery(mActivity, this, REQUEST_GALLERY);
                break;
            case R.id.btn_open_camera:
                if (PermissionsUtils.hasPermission(mActivity, Manifest.permission.CAMERA)) {
                    mPhotoPath = mPickerPresenter.takePhoto(mActivity, this, REQUEST_CAMERA);
                } else {
                    PermissionsUtils.requestPermissionsSafely(
                            mActivity,
                            this,
                            new String[] {Manifest.permission.CAMERA},
                            REQUEST_PERMISSION_CAMERA
                    );
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_GALLERY:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Uri uri = data.getData();
                    ScalingActivity.startActivity(mActivity, this, uri);
                }
                break;
            case REQUEST_CAMERA:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    ScalingActivity.startActivity(mActivity, this, Uri.parse(mPhotoPath));
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPhotoPath = mPickerPresenter.takePhoto(mActivity, this, REQUEST_CAMERA);
                } else {
                    DialogFactory.createSimpleOkErrorDialog(mActivity,
                            R.string.dialog_title_camera_permission_error,
                            R.string.dialog_message_camera_permission_forbidden).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
