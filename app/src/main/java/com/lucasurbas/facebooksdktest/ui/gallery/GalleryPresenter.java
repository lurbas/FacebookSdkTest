package com.lucasurbas.facebooksdktest.ui.gallery;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.google.android.gms.location.LocationRequest;
import com.lucasurbas.facebooksdktest.constants.Constants;
import com.lucasurbas.facebooksdktest.model.GalleryItem;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.R.attr.country;
import static com.facebook.AccessToken.getCurrentAccessToken;

/**
 * Created by Lucas on 25/01/2017.
 */

public class GalleryPresenter implements GalleryContract.Presenter {

    private static final String KEY_PHOTO_PATH = "key_photo_path";
    private static final int LOCATION_TIMEOUT_IN_SECONDS = 6;

    private GalleryContract.View view;
    private GalleryContract.Navigator navigator;
    private BriteDatabase database;
    private ReactiveLocationProvider locationProvider;
    private Subscription subscription;
    private String photoPath;

    @Inject
    public GalleryPresenter(GalleryContract.Navigator navigator, BriteDatabase database, ReactiveLocationProvider locationProvider) {
        this.navigator = navigator;
        this.database = database;
        this.locationProvider = locationProvider;
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
        if (item.post_id() != null) {
            navigator.openGalleryItemDetails(item._id());
        } else if (view != null) {
            view.showPublishDialog(item);
        }
    }

    @Override
    public void savePictureAsGalleryItem() {
        Profile facebookProfile = Profile.getCurrentProfile();
        if (view != null) {
            view.showToast(String.format(Locale.ROOT, "You look awesome, %s!", facebookProfile.getName()));
        }
        database.insert(GalleryItem.TABLE_NAME, GalleryItem.FACTORY.marshal()
                        ._id(UUID.randomUUID().toString())
                        .path(photoPath)
                        .asContentValues(),
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void tryPublish(final GalleryItem item) {
        Set<String> permission = AccessToken.getCurrentAccessToken().getPermissions();
        if (view != null) {
            if (permission.contains(Constants.PUBLISH_PERMISSIONS)) {
                view.askForLocationPermission(item);
            } else {
                view.askForPublishPermission(item);
            }
        }
    }

    @RequiresPermission(
            anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}
    )
    @Override
    public void publish(final GalleryItem item) {

        LocationRequest req = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setNumUpdates(1)
                .setExpirationDuration(TimeUnit.SECONDS.toMillis(LOCATION_TIMEOUT_IN_SECONDS))
                .setFastestInterval(TimeUnit.SECONDS.toMillis(1))
                .setInterval(100);

        locationProvider.getUpdatedLocation(req)
                .timeout(LOCATION_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .first()
                .flatMap(new Func1<Location, Observable<List<Address>>>() {
                    @Override
                    public Observable<List<Address>> call(Location location) {
                        return locationProvider
                                .getReverseGeocodeObservable(location.getLatitude(), location.getLongitude(), 1);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Address>>() {
                    @Override
                    public void call(List<Address> addressList) {
                        if (!addressList.isEmpty()) {
                            Address address = addressList.get(0);
                            if (view != null) {
                                view.showToast("Location: " + address.getLocality() + ", " + address.getCountryName());
                            }
                            publishOnFacebook(item, address.getLocality(), address.getLocality());
                        } else if (view != null) {
                            view.showToast("Address empty");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (throwable instanceof TimeoutException) {
                            if (view != null) {
                                view.showToast("Timeout getting location");
                            }
                        } else {
                            if (view != null) {
                                view.showToast("Error getting location");
                            }
                        }
                    }
                }, new Action0() {
                    @Override
                    public void call() {
//                        if (view != null) {
//                            view.showToast("Completed");
//                        }
                    }
                });
    }

    private void publishOnFacebook(final GalleryItem item, String city, String country) {
        final Bitmap image = getBitmap(item.path());
        if (image == null) {
            if (view != null) {
                view.showToast("Error decoding bitmap");
            }
            return;
        }

        if (view != null) {
            view.showToast("Start sharing!");
        }

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setUserGenerated(true)
                .setCaption(String.format(Locale.ROOT, "See how cool I am! Made in %s, %s", city, country))
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        ShareApi.share(content, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                if (view != null) {
                    view.showToast("Posted on Facebook!");
                }
                database.insert(GalleryItem.TABLE_NAME, GalleryItem.FACTORY.marshal()
                                ._id(item._id())
                                .post_id(result.getPostId())
                                .path(item.path())
                                .asContentValues(),
                        SQLiteDatabase.CONFLICT_REPLACE);
            }

            @Override
            public void onCancel() {
                if (view != null) {
                    view.showToast("Cancel posting on Facebook");
                }
            }

            @Override
            public void onError(FacebookException error) {
                if (view != null) {
                    view.showToast("Error posting on Facebook: " + error.getMessage());
                }
            }
        });
    }

    private Bitmap getBitmap(String path) {
        // Get the dimensions of the View
        int targetW = 800;
        int targetH = 800;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        return BitmapFactory.decodeFile(path, bmOptions);
    }

    @Override
    public boolean checkFacebookAccess() {
        boolean hasAccessToken = getCurrentAccessToken() != null;
        if (!hasAccessToken) {
            navigator.openLoginScreen();
            navigator.finish();
        }
        return hasAccessToken;
    }

    @Override
    public void logout() {
        LoginManager.getInstance().logOut();
        navigator.openLoginScreen();
        navigator.finish();
    }
}
