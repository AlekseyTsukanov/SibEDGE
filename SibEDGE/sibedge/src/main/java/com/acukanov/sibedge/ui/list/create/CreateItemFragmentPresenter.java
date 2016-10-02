package com.acukanov.sibedge.ui.list.create;


import android.app.Activity;

import com.acukanov.sibedge.data.database.DatabaseHelper;
import com.acukanov.sibedge.data.database.model.Items;
import com.acukanov.sibedge.ui.base.IPresenter;
import com.acukanov.sibedge.utils.LogUtils;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreateItemFragmentPresenter implements IPresenter<ICreateFragmentView> {
    private static final String LOG_TAG = LogUtils.makeLogTag(CreateItemFragmentPresenter.class);
    private ICreateFragmentView mCreateView;
    private DatabaseHelper mDatabaseHelper;
    private Subscription mSubscription;

    @Inject
    CreateItemFragmentPresenter(DatabaseHelper databaseHelper) {
        mDatabaseHelper = databaseHelper;
    }

    @Override
    public void attachView(ICreateFragmentView IView) {
        mCreateView = IView;
    }

    @Override
    public void detachView() {
        mCreateView = null;
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    public void saveNewItem(Activity activity, String text) {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        Items item = new Items();
        item.setText(text);
        mCreateView.showProgress();
        mSubscription = mDatabaseHelper.createItem(item)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(action -> {
                    LogUtils.debug(LOG_TAG, "onNext item saved");
                    mCreateView.onNewItemSaved(activity);
                    mCreateView.hideProgress();
                }, e -> {
                    LogUtils.error(LOG_TAG, "onError save");
                    mCreateView.onErrorSavingItem(activity);
                }, () -> {
                    LogUtils.debug(LOG_TAG, "onComplete saving");
                    mCreateView.hideProgress();
                });
    }

    public void savingError(Activity activity) {
        mCreateView.onErrorSavingItem(activity);
    }
}
