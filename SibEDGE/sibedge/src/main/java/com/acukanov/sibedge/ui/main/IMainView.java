package com.acukanov.sibedge.ui.main;


import android.support.v4.app.Fragment;

import com.acukanov.sibedge.ui.base.IView;

public interface IMainView extends IView {
    void onNavigationItemSelected(Fragment fragment);
    void onSetLocale();
}
