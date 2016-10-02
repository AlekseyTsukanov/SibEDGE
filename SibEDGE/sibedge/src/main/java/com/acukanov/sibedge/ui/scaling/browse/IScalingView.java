package com.acukanov.sibedge.ui.scaling.browse;


import android.net.Uri;

import com.acukanov.sibedge.ui.base.IView;

import java.io.IOException;

public interface IScalingView extends IView {
    void onLoadImageIntoView(Uri imageUri) throws IOException;
}
