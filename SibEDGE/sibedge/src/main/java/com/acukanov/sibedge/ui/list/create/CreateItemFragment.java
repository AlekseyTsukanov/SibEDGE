package com.acukanov.sibedge.ui.list.create;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.acukanov.sibedge.R;
import com.acukanov.sibedge.ui.base.BaseActivity;
import com.acukanov.sibedge.ui.base.BaseFragment;
import com.acukanov.sibedge.utils.DialogFactory;
import com.acukanov.sibedge.utils.LogUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CreateItemFragment extends BaseFragment implements ICreateFragmentView, View.OnClickListener {
    private static final String LOG_TAG = LogUtils.makeLogTag(CreateItemFragment.class);
    private Activity mActivity;
    @Inject CreateItemFragmentPresenter mCreateFragmentPresenter;
    @InjectView(R.id.btn_done) Button mBtnDone;
    @InjectView(R.id.btn_revert) Button mBtnRevert;
    @InjectView(R.id.text_new_item) EditText mTextNewItem;
    @InjectView(R.id.progress_bar) ProgressBar mProgress;

    public CreateItemFragment() {}

    public static CreateItemFragment newInstance() {
        return new CreateItemFragment();
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
        setTitle(R.string.fragment_title_create);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_item, container, false);
        ButterKnife.inject(this, rootView);
        mCreateFragmentPresenter.attachView(this);

        mBtnDone.setOnClickListener(this);
        mBtnRevert.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCreateFragmentPresenter.detachView();
    }

    @Override @OnClick({R.id.btn_done, R.id.btn_revert})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_done:
                if (!mTextNewItem.getText().toString().equals("")) {
                    mCreateFragmentPresenter.saveNewItem(mActivity, mTextNewItem.getText().toString());
                } else {
                    mCreateFragmentPresenter.savingError(mActivity);
                }
                break;
            case R.id.btn_revert:
                mActivity.setResult(Activity.RESULT_CANCELED);
                mActivity.finish();
                break;
        }
    }

    @Override
    public void onNewItemSaved(Activity activity) {
        activity.setResult(Activity.RESULT_OK);
        activity.finish();
    }

    @Override
    public void onErrorSavingItem(Activity activity) {
        DialogFactory.createSimpleOkErrorDialog(activity, R.string.dialog_error_title, R.string.dialog_message_text_epmty).show();
    }

    @Override
    public void showProgress() {
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgress.setVisibility(View.GONE);
    }
}
