package com.acukanov.sibedge.ui.list.create;


import android.app.Activity;

import com.acukanov.sibedge.ui.base.IView;

public interface ICreateFragmentView extends IView {
    void onNewItemSaved(Activity activity);
    void onErrorSavingItem(Activity activity);
    void showProgress();
    void hideProgress();
}
