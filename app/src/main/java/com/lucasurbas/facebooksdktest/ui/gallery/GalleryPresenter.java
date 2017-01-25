package com.lucasurbas.facebooksdktest.ui.gallery;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.lucasurbas.facebooksdktest.model.GalleryItem;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Lucas on 25/01/2017.
 */

public class GalleryPresenter implements GalleryContract.Presenter {

    private static final String KEY_PHOTO_PATH = "key_photo_path";

    private GalleryContract.View view;
    private GalleryContract.Navigator navigator;
    private BriteDatabase database;
    private Subscription subscription;
    private String photoPath;

    @Inject
    public GalleryPresenter(GalleryContract.Navigator navigator, BriteDatabase database) {
        this.navigator = navigator;
        this.database = database;
    }

    @Override
    public void attachView(GalleryContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    public Bundle saveState() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_PHOTO_PATH, photoPath);
        return bundle;
    }

    @Override
    public void restoreState(Bundle bundle) {
        photoPath = bundle.getString(KEY_PHOTO_PATH);
    }

    @Override
    public void takePhoto() {
        photoPath = navigator.openCamera();
        if (photoPath == null && view != null) {
            view.showToast("Error while opening camera");
        }
    }

    @Override
    public void loadGalleryItems() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        QueryObservable queryObservable = database.createQuery(GalleryItem.TABLE_NAME, GalleryItem.SELECT_ALL);
        if (subscription == null || subscription.isUnsubscribed()) {
            subscription = queryObservable.mapToList(new Func1<Cursor, GalleryItem>() {
                @Override
                public GalleryItem call(Cursor cursor) {
                    return GalleryItem.SELECT_ALL_MAPPER.map(cursor);
                }
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<GalleryItem>>() {
                        @Override
                        public void call(List<GalleryItem> galleryItems) {
                            if (view != null) {
                                if (galleryItems.isEmpty()) {
                                    view.showEmptyScreen();
                                } else {
                                    view.showGalleryItems(galleryItems);
                                }
                            }
                        }

                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            if (view != null) {
                                view.showToast("error");
                            }
                        }
                    });
        }
    }

    @Override
    public void galleryItemClick(GalleryItem item) {
        navigator.openGalleryItemDetails(item._id());
        if (view != null) {
            view.showToast("item clicked");
        }
    }

    @Override
    public void savePictureAsGalleryItem() {
        database.insert(GalleryItem.TABLE_NAME, GalleryItem.FACTORY.marshal()
                        ._id(UUID.randomUUID().toString())
                        .is_shared(GalleryItem.NOT_SHARED)
                        .path(photoPath)
                        .asContentValues(),
                SQLiteDatabase.CONFLICT_REPLACE);
    }
}
