package com.lucasurbas.facebooksdktest.app;

import android.app.Application;
import android.content.Context;

import com.lucasurbas.facebooksdktest.BuildConfig;
import com.lucasurbas.facebooksdktest.injection.app.ApplicationComponent;
import com.lucasurbas.facebooksdktest.injection.app.ApplicationModule;
import com.lucasurbas.facebooksdktest.injection.app.DaggerApplicationComponent;
import com.lucasurbas.facebooksdktest.injection.database.DbModule;

import timber.log.Timber;

/**
 * Created by Lucas on 25/01/2017.
 */

public class AwesomeApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .dbModule(new DbModule())
                .build();
    }

    public static ApplicationComponent getAppComponent(Context context) {
        return ((AwesomeApplication) context.getApplicationContext()).applicationComponent;
    }
}
