package com.lucasurbas.facebooksdktest.injection.database;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;

import com.lucasurbas.facebooksdktest.database.DatabaseOpenerHelper;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Lucas on 19/06/16.
 */
@Module
public class DbModule {

    @Provides
    @Singleton
    SQLiteOpenHelper provideOpenHelper(Application application) {
        return new DatabaseOpenerHelper(application);
    }

    @Provides
    @Singleton
    SqlBrite provideSqlBrite() {
        return SqlBrite.create(new SqlBrite.Logger() {
            @Override
            public void log(String message) {
                Timber.tag("Database").v(message);
            }
        });
    }

    @Provides
    @Singleton
    BriteDatabase provideDatabase(SqlBrite sqlBrite, SQLiteOpenHelper helper) {
        BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
        db.setLoggingEnabled(true);
        return db;
    }
}
