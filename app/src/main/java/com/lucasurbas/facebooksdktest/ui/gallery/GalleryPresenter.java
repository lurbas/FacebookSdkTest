package com.lucasurbas.facebooksdktest.ui.gallery;

import android.database.Cursor;

import com.lucasurbas.facebooksdktest.model.GalleryItem;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;

import java.util.List;

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

    private GalleryContract.View view;
    private GalleryContract.Navigator navigator;
    private BriteDatabase database;
    private Subscription subscription;

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
    public void takePhoto() {
        navigator.openCamera();
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
    public void startGalleryItemDetails(String itemId) {
        navigator.openGalleryItemDetails(itemId);
    }
}
