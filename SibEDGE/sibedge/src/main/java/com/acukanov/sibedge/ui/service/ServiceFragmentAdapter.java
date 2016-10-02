package com.acukanov.sibedge.ui.service;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acukanov.sibedge.R;
import com.acukanov.sibedge.data.remote.model.ServiceData;
import com.acukanov.sibedge.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ServiceFragmentAdapter extends RecyclerView.Adapter<ServiceFragmentAdapter.ViewHolder> {
    private static final String LOG_TAG = LogUtils.makeLogTag(ServiceFragmentAdapter.class);
    private Activity mActivity;
    private List<ServiceData> mServiceData;

    @Inject
    ServiceFragmentAdapter(Activity activity) {
        mActivity = activity;
        mServiceData = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.list_item_service_fragment, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            if (mServiceData != null && !mServiceData.isEmpty()) {
                ServiceData data = mServiceData.get(position);
                viewHolder.textMessage.setText(data.getText());
                viewHolder.date.setText(data.getDate());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mServiceData.size();
    }

    // region getters and setters

    public List<ServiceData> getServiceData() {
        return mServiceData;
    }

    public void setServiceData(List<ServiceData> mServiceData) {
        this.mServiceData = mServiceData;
    }
    // endregion

    public void clearAdapter() {
        if (mServiceData != null && !mServiceData.isEmpty()) {
            for (int i = 0; i < mServiceData.size(); i++) {
                mServiceData.remove(0);
            }
            notifyItemRangeRemoved(0, mServiceData.size());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.text_message) TextView textMessage;
        @InjectView(R.id.text_date) TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
