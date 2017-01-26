package com.lucasurbas.facebooksdktest.ui.details;

import android.database.Cursor;

import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.lucasurbas.facebooksdktest.model.FacebookResponse;
import com.lucasurbas.facebooksdktest.model.GalleryItem;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;

import java.util.Locale;

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

    private DetailsContract.Navigator navigator;
    private DetailsContract.View view;
    private BriteDatabase database;
    private Subscription subscription;

    @Inject
    public DetailsPresenter(DetailsContract.Navigator navigator, BriteDatabase database) {
        this.navigator = navigator;
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
                            loadLikesCount(galleryItem);
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

    private void loadLikesCount(final GalleryItem galleryItem) {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                String.format(Locale.ROOT, "/%s?fields=likes.summary(true),comments.summary(true)", galleryItem.post_id()),
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        FacebookRequestError error = response.getError();
                        if (error != null) {
                            if (error.getErrorCode() == 100) {
                                deletePostId(galleryItem);
                                if (view != null) {
                                    view.showToast("Post doesn't exist on Facebook");
                                }
                                navigator.finish();
                            } else if (view != null) {
                                view.showToast(error.getErrorUserMessage());
                            }
                        } else {
                            FacebookResponse facebookResponse = new FacebookResponse(response.getRawResponse());
                            if (view != null) {
                                view.showCounters(facebookResponse.getLikesCount(), facebookResponse.getCommentsCount());
                            }
                        }
                    }
                }
        ).executeAsync();
    }

    private void deletePostId(GalleryItem item) {
        database.update(GalleryItem.TABLE_NAME, GalleryItem.FACTORY.marshal()
                        .post_id(null)
                        .asContentValues(),
                "_id = ?", item._id());
    }
}
