package com.lucasurbas.facebooksdktest.app;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.lucasurbas.facebooksdktest.injection.app.ApplicationComponent;
import com.lucasurbas.facebooksdktest.injection.app.ApplicationModule;
import com.lucasurbas.facebooksdktest.injection.app.DaggerApplicationComponent;
import com.lucasurbas.facebooksdktest.injection.database.DbModule;

/**
 * Created by Lucas on 25/01/2017.
 */

public class AwesomeApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());

        // Dependency graph is build using Dagger2
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .dbModule(new DbModule())
                .build();
    }

    public static ApplicationComponent getAppComponent(Context context) {
        return ((AwesomeApplication) context.getApplicationContext()).applicationComponent;
    }
}
