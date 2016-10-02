package com.acukanov.sibedge.ui.list.general;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.acukanov.sibedge.R;
import com.acukanov.sibedge.data.database.model.Items;
import com.acukanov.sibedge.ui.base.BaseActivity;
import com.acukanov.sibedge.ui.base.BaseFragment;
import com.acukanov.sibedge.ui.list.create.CreateItemActivity;
import com.acukanov.sibedge.utils.LogUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ListFragment extends BaseFragment implements IListFragmentView {
    private static final String LOG_TAG = LogUtils.makeLogTag(ListFragment.class);
    private static final int REQUEST_CODE_CREATE_ITEM = 0;
    private Activity mActivity;
    @Inject ListFragmentPresenter mListFragmentPresenter;
    @Inject ListFragmentAdapter mListAdapter;
    @InjectView(R.id.list_custom_items) RecyclerView mListItems;
    @InjectView(R.id.progress_bar) ProgressBar mProgressBar;
    private LinearLayoutManager mLayoutManager;

    public ListFragment() {}

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_add:
                mListFragmentPresenter.addItem(mActivity);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) mActivity).activityComponent().inject(this);
        mLayoutManager = new LinearLayoutManager(mActivity);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.inject(this, rootView);
        mListFragmentPresenter.attachView(this);

        mListItems.setLayoutManager(mLayoutManager);
        mListItems.setHasFixedSize(true);
        mListItems.setAdapter(mListAdapter);

        mListFragmentPresenter.getItems();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.menu_drawer_list);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListFragmentPresenter.detachView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_CREATE_ITEM:
                mListFragmentPresenter.getItems();
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        switch (item.getItemId()) {
            case ListFragmentAdapter.CONTEXT_MENU_EDIT_ITEM:
                position = mListAdapter.getPosition();
                mListAdapter.createEditDialog(position);
                break;
            case ListFragmentAdapter.CONTEXT_MENU_DELETE_ITEM:
                mListFragmentPresenter.deleteItem(mListAdapter.getItem());
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onItemsLoaded(ArrayList<Items> items) {
        mListAdapter.clearAdapter();
        mListAdapter.setItems(items);
        mListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAddItemSelected(Activity activity) {
        CreateItemActivity.startActivityForResult(activity, this, REQUEST_CODE_CREATE_ITEM);
    }

    @Override
    public void onItemDeleted() {
        mListFragmentPresenter.getItems();
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }
}
