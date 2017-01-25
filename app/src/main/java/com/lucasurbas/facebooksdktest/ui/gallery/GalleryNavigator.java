package com.lucasurbas.facebooksdktest.ui.gallery;

import javax.inject.Inject;

/**
 * Created by Lucas on 25/01/2017.
 */

public class GalleryNavigator implements GalleryContract.Navigator {

    private GalleryActivity galleryActivity;

    @Inject
    public GalleryNavigator(GalleryActivity galleryActivity){
        this.galleryActivity = galleryActivity;
    }

    @Override
    public void openCamera() {

    }

    @Override
    public void openGalleryItemDetails(String itemId) {

    }
}
