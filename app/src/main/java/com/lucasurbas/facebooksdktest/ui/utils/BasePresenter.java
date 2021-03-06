package com.lucasurbas.facebooksdktest.ui.utils;

/**
 * Created by Lucas on 25/01/2017.
 */
public interface BasePresenter<V extends BaseView> {

    void attachView(V view);

    void detachView();
}
