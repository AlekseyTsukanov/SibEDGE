package com.acukanov.sibedge.ui.base;


public interface IPresenter<V extends IView> {
    void attachView(V IView);
    void detachView();
}