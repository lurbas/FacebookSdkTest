package com.lucasurbas.facebooksdktest.injection.gallery;

import com.lucasurbas.facebooksdktest.injection.ActivityScope;
import com.lucasurbas.facebooksdktest.injection.app.ApplicationComponent;
import com.lucasurbas.facebooksdktest.ui.gallery.GalleryActivity;

import dagger.Component;

/**
 * Created by Lucas on 25/01/2017.
 */

@ActivityScope
@Component(
        dependencies = {ApplicationComponent.class},
        modules = {GalleryModule.class}
)
public interface GalleryComponent {

    void inject(GalleryActivity activity);

}
