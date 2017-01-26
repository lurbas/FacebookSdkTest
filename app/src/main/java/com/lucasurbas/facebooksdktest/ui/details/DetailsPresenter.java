package com.lucasurbas.facebooksdktest.ui.details;

import android.database.Cursor;

import com.lucasurbas.facebooksdktest.model.GalleryItem;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Lucas on 26/01/2017.
 */

public class DetailsPresenter implements DetailsContract.Presenter {

    private DetailsContract.View view;
    private BriteDatabase database;
    private Subscription subscription;

    @Inject
    public DetailsPresenter(BriteDatabase database) {
        this.database = database;
    }

    @Override
    public void attachView(DetailsContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void loadGalleryItem(String itemId) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        QueryObservable queryObservable = database.createQuery(GalleryItem.TABLE_NAME, GalleryItem.SELECT_BY_ID, itemId);
        if (subscription == null || subscription.isUnsubscribed()) {
            subscription = queryObservable.mapToOne(new Func1<Cursor, GalleryItem>() {
                @Override
                public GalleryItem call(Cursor cursor) {
                    return GalleryItem.SELECT_ALL_MAPPER.map(cursor);
                }
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<GalleryItem>() {
                        @Override
                        public void call(GalleryItem galleryItem) {
                            if (view != null) {
                                view.showGalleryItem(galleryItem);
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
}
