package com.acukanov.sibedge.ui.scaling.browse;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ZoomControls;

import com.acukanov.sibedge.R;
import com.acukanov.sibedge.ui.base.BaseActivity;
import com.acukanov.sibedge.ui.base.BaseFragment;
import com.acukanov.sibedge.utils.LogUtils;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ScalingFragment extends BaseFragment implements IScalingView, View.OnTouchListener {
    private static final String LOG_TAG = LogUtils.makeLogTag(ScalingFragment.class);
    private static final String BUNDLE_ARG_IMAGE_URI = "bundle_arg_image_uri";
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private Activity mActivity;
    private Uri mImageUri;
    @Inject ScalingPresenter mScalingPresenter;
    @InjectView(R.id.img_image_view)
    ImageView mImgView;
    @InjectView(R.id.zoom_control) ZoomControls mZoomControls;

    private Matrix mMatrix = new Matrix();
    private Matrix mSavedMatrix = new Matrix();
    private PointF mStart = new PointF();
    private PointF mMid = new PointF();
    private int mode = NONE;
    private float mOldDist = 1f;

    public ScalingFragment() {}

    public static ScalingFragment newInstance(String imageUri) {
        ScalingFragment instance = new ScalingFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_ARG_IMAGE_URI, imageUri);
        instance.setArguments(args);
        return instance;
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
        mImageUri = Uri.parse(getArguments().getString(BUNDLE_ARG_IMAGE_URI));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scaling, container, false);
        ButterKnife.inject(this, rootView);
        mScalingPresenter.attachView(this);
        mScalingPresenter.loadImageIntoView(mImageUri);
        mZoomControls.setIsZoomInEnabled(true);
        mZoomControls.setIsZoomOutEnabled(true);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.menu_drawer_scaling);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mScalingPresenter.detachView();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mSavedMatrix.set(mMatrix);
                mStart.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mOldDist = spacing(event);
                if (mOldDist > 10f) {
                    mSavedMatrix.set(mMatrix);
                    midPoint(mMid, event);
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    mMatrix.set(mSavedMatrix);
                    mMatrix.postTranslate(event.getX() - mStart.x, event.getY() - mStart.y);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        mMatrix.set(mSavedMatrix);
                        float scale = newDist / mOldDist;
                        mMatrix.postScale(scale, scale, mMid.x, mMid.y);
                    }
                }
                break;
        }
        view.setImageMatrix(mMatrix);
        return true;
    }

    @Override
    public void onLoadImageIntoView(Uri imageUri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), imageUri);
        mImgView.setImageBitmap(bitmap);
        mImgView.setOnTouchListener(this);

        mZoomControls.setOnZoomInClickListener(v -> {
            float x = mImgView.getScaleX();
            float y = mImgView.getScaleY();
            mImgView.setScaleX((float) (x + 1));
            mImgView.setScaleY((float) (y + 1));
        });

        mZoomControls.setOnZoomOutClickListener(v-> {
            float x = mImgView.getScaleX();
            float y = mImgView.getScaleY();
            mImgView.setScaleX((float) (x - 1));
            mImgView.setScaleY((float) (y - 1));
        });
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
}
