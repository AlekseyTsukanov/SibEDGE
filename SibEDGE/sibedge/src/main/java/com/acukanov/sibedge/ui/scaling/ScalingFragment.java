package com.acukanov.sibedge.ui.scaling;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acukanov.sibedge.R;
import com.acukanov.sibedge.ui.base.BaseActivity;
import com.acukanov.sibedge.ui.base.BaseFragment;
import com.acukanov.sibedge.utils.LogUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class ScalingFragment extends BaseFragment implements IScalingView {
    private static final String LOG_TAG = LogUtils.makeLogTag(ScalingFragment.class);
    private Activity mActivity;
    @Inject ScalingPresenter mScalingPresenter;

    public ScalingFragment() {}

    public static ScalingFragment newInstance() {
        return new ScalingFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_scaling, container, false);
        ButterKnife.inject(this, rootView);
        mScalingPresenter.attachView(this);

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mScalingPresenter.detachView();
    }
}
