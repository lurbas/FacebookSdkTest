package com.lucasurbas.facebooksdktest.ui.utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lucasurbas.facebooksdktest.app.AwesomeApplication;
import com.lucasurbas.facebooksdktest.injection.app.ApplicationComponent;

/**
 * Created by Lucas on 25/01/2017.
 */

public abstract class BaseActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityComponent(AwesomeApplication.getAppComponent(this));
    }

    protected abstract void setupActivityComponent(ApplicationComponent applicationComponent);
}
