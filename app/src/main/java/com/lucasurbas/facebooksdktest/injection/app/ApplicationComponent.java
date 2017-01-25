package com.lucasurbas.facebooksdktest.injection.app;

import android.app.Application;

import com.lucasurbas.facebooksdktest.injection.database.DbModule;
import com.squareup.sqlbrite.BriteDatabase;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Lucas on 25/01/2017.
 */

@Singleton
@Component(
        modules = {ApplicationModule.class, DbModule.class}
)
public interface ApplicationComponent {

    Application getApplication();

    BriteDatabase getDataBase();

}
