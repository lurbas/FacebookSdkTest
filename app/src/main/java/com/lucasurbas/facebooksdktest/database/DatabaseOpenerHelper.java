package com.lucasurbas.facebooksdktest.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lucasurbas.facebooksdktest.model.GalleryItem;


/**
 * Created by Lucas on 25/01/2017.
 */
public class DatabaseOpenerHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "awesome_db";

    public DatabaseOpenerHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GalleryItem.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                // migrate db to newer version
            case 2:
                break;
        }
    }
}
