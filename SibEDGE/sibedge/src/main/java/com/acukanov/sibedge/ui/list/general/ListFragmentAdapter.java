package com.acukanov.sibedge.ui.list.general;


import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.acukanov.sibedge.R;
import com.acukanov.sibedge.data.database.DatabaseHelper;
import com.acukanov.sibedge.data.database.model.Items;
import com.acukanov.sibedge.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ListFragmentAdapter extends RecyclerView.Adapter<ListFragmentAdapter.ViewHolder> {
    private static final String LOG_TAG = LogUtils.makeLogTag(ListFragmentAdapter.class);
    private static final int MENU_GROUP_ID = 0;
    public static final int CONTEXT_MENU_EDIT_ITEM = 0;
    public static final int CONTEXT_MENU_DELETE_ITEM = 1;
    private Activity mActivity;
    private DatabaseHelper mDatabaseHelper;
    private ListFragmentPresenter mPresenter;
    private List<Items> mItems;
    private int mPosition = -1;
    private Items mItem;

    @Inject
    ListFragmentAdapter(Activity activity, DatabaseHelper databaseHelper, ListFragmentPresenter presenter) {
        mActivity = activity;
        mItems = new ArrayList<>();
        mDatabaseHelper = databaseHelper;
        mPresenter = presenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.list_item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            if (mItems != null && !mItems.isEmpty()) {
                Items item = mItems.get(position);
                viewHolder.textItem.setText(item.getText());
                if (item.getCheck() == 1) {
                    viewHolder.imageItem.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_camera_black_24dp));
                    viewHolder.checkItem.setChecked(true);
                }
                viewHolder.checkItem.setOnClickListener(l -> {
                    if (viewHolder.checkItem.isChecked()) {
                        item.setCheck(1);
                        viewHolder.imageItem.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_camera_black_24dp));
                    } else {
                        item.setCheck(0);
                        viewHolder.imageItem.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_map_black_24dp));
                    }
                    mPresenter.updateItem(item);
                });
                viewHolder.itemView.setOnClickListener(l -> {
                    createEditDialog(position);
                });
                viewHolder.itemView.setOnLongClickListener(l -> {
                    setPosition(position);
                    setItem(item);
                    return false;
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    // region getters and setters

    public List<Items> getItems() {
        return mItems;
    }

    public void setItems(ArrayList<Items> items) {
        this.mItems = items;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public Items getItem() {
        return mItem;
    }

    public void setItem(Items mItem) {
        this.mItem = mItem;
    }
    // endregion

    public void createEditDialog(int position) {
        Items item = mItems.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_change_item, null);
        builder.setView(dialogView);
        EditText textChangeItem = (EditText) dialogView.findViewById(R.id.text_change_item);
        textChangeItem.setText(item.getText());
        builder.setTitle(R.string.dialog_title_change_item);
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            item.setText(textChangeItem.getText().toString());
            mPresenter.updateItem(item);
            notifyItemChanged(position);
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }

    public void clearAdapter() {
        if (mItems != null && !mItems.isEmpty()) {
            for (int i = 0; i < mItems.size(); i++) {
                mItems.remove(0);
            }
            notifyItemRangeRemoved(0, mItems.size());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.image_item) ImageView imageItem;
        @InjectView(R.id.text_item) TextView textItem;
        @InjectView(R.id.check_item) CheckBox checkItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            itemView.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
                menu.add(MENU_GROUP_ID, CONTEXT_MENU_EDIT_ITEM, 0, R.string.context_menu_edit);
                menu.add(MENU_GROUP_ID, CONTEXT_MENU_DELETE_ITEM, 0, R.string.context_menu_delete);
            });
        }
    }
}
