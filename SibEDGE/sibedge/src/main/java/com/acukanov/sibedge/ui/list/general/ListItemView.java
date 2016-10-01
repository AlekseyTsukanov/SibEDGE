package com.acukanov.sibedge.ui.list.general;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acukanov.sibedge.R;

public class ListItemView extends LinearLayout {
    private Activity mActivity;
    private View mRootView;
    private LayoutInflater mLayoutInflater;
    private ImageView mImageItem;
    private TextView mTextItem;
    private CheckBox mCheckItem;


    public ListItemView(Context context) {
        super(context);
        mActivity = (Activity) context;
        mLayoutInflater = LayoutInflater.from(mActivity);
        mRootView = mLayoutInflater.inflate(R.layout.partial_fragment_list_layout, this, true);
    }

    public ListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mActivity = (Activity) context;
        mLayoutInflater = LayoutInflater.from(mActivity);
        mRootView = mLayoutInflater.inflate(R.layout.partial_fragment_list_layout, this, true);
    }

    public ListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mActivity = (Activity) context;
        mLayoutInflater = LayoutInflater.from(mActivity);
        mRootView = mLayoutInflater.inflate(R.layout.partial_fragment_list_layout, this, true);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ListItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mActivity = (Activity) context;
        mLayoutInflater = LayoutInflater.from(mActivity);
        mRootView = mLayoutInflater.inflate(R.layout.partial_fragment_list_layout, this, true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mImageItem = (ImageView) findViewById(R.id.image_item);
        mTextItem = (TextView) findViewById(R.id.text_item);
        mCheckItem = (CheckBox) findViewById(R.id.check_item);
    }
}
