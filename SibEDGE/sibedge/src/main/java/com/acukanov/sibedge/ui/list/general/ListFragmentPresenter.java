package com.acukanov.sibedge.ui.list.general;


import android.app.Activity;

import com.acukanov.sibedge.data.database.DatabaseHelper;
import com.acukanov.sibedge.data.database.model.Items;
import com.acukanov.sibedge.ui.base.IPresenter;
import com.acukanov.sibedge.utils.LogUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscription;
import rx.schedulers.Schedulers;

public class ListFragmentPresenter implements IPresenter<IListFragmentView> {
    private static final String LOG_TAG = LogUtils.makeLogTag(ListFragmentPresenter.class);
    private IListFragmentView mListFragmentView;
    private DatabaseHelper mDatabaseHelper;
    private Subscription mSubscription;

    @Inject ListFragmentPresenter(DatabaseHelper databaseHelper) {
        mDatabaseHelper = databaseHelper;
    }

    @Override
    public void attachView(IListFragmentView IView) {
        mListFragmentView = IView;
    }

    @Override
    public void detachView() {
        mListFragmentView = null;
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    public void getItems() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mListFragmentView.showProgress();
        ArrayList<Items> items = new ArrayList<>();
        mSubscription = mDatabaseHelper.findAllItems()
                .subscribe(items1 -> {
                    LogUtils.error(LOG_TAG, "onNext getItems");
                    if (items1 != null) {
                        items.add(items1);
                    }
                }, e -> {
                    LogUtils.error(LOG_TAG, "onError getItems");
                    mListFragmentView.hideProgress();
                }, () -> {
                    LogUtils.error(LOG_TAG, "onComplete getItems");
                    mListFragmentView.hideProgress();
                    mListFragmentView.onItemsLoaded(items);
                });
    }

    public void updateItem(Items item) {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mSubscription = mDatabaseHelper.updateItem(item)
                .subscribeOn(Schedulers.io())
                .subscribe(item1 -> {
                    LogUtils.error(LOG_TAG, "onNext updateItem");
                }, e -> {
                    LogUtils.error(LOG_TAG, "onError updateItem");
                }, () -> {
                    LogUtils.error(LOG_TAG, "onComplete updateItem");
                });
    }

    public void addItem(Activity activity) {
        mListFragmentView.onAddItemSelected(activity);
    }

    public void deleteItem(Items item) {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mSubscription = mDatabaseHelper.deleteItem(item)
                .subscribe(action -> {
                    LogUtils.error(LOG_TAG, "onNext deleteItem");
                }, e -> {
                    LogUtils.error(LOG_TAG, "onError deleteItem");
                }, () -> {
                    LogUtils.error(LOG_TAG, "onComplete deleteItem");
                    mListFragmentView.onItemDeleted();
                });
    }
}
