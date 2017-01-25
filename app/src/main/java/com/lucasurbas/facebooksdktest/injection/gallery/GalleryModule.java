package com.lucasurbas.facebooksdktest.injection.gallery;

import android.content.Context;

import com.lucasurbas.facebooksdktest.injection.ActivityScope;
import com.lucasurbas.facebooksdktest.ui.gallery.GalleryActivity;
import com.lucasurbas.facebooksdktest.ui.gallery.GalleryContract;
import com.lucasurbas.facebooksdktest.ui.gallery.GalleryNavigator;
import com.lucasurbas.facebooksdktest.ui.gallery.GalleryPresenter;

import dagger.Module;
import dagger.Provides;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;

/**
 * Created by Lucas on 25/01/2017.
 */

@Module
public class GalleryModule {

    private final GalleryActivity galleryActivity;

    public GalleryModule(GalleryActivity galleryActivity) {
        this.galleryActivity = galleryActivity;
    }

    @Provides
    @ActivityScope
    GalleryContract.Navigator provideNavigator(GalleryNavigator navigator) {
        return navigator;
    }

    @Provides
    @ActivityScope
    GalleryContract.Presenter providePresenter(GalleryPresenter presenter) {
        return presenter;
    }

    @Provides
    @ActivityScope
    Context provideContext(){
        return galleryActivity;
    }

    @Provides
    @ActivityScope
    GalleryActivity provideGalleryActivity(){
        return galleryActivity;
    }

    @Provides
    @ActivityScope
    ReactiveLocationProvider provideReactiveLocationProvider(){
        return new ReactiveLocationProvider(galleryActivity);
    }
}
