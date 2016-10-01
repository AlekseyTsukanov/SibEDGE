package com.acukanov.sibedge.ui.list.general;


import android.app.Activity;

import com.acukanov.sibedge.data.database.model.Items;
import com.acukanov.sibedge.ui.base.IView;

import java.util.ArrayList;

public interface IListFragmentView extends IView {
    void showProgress();
    void hideProgress();
    void onItemsLoaded(ArrayList<Items> items);
    void onAddItemSelected(Activity activity);
    void onItemDeleted();
}
