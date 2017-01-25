package com.lucasurbas.facebooksdktest.ui.gallery;

import com.lucasurbas.facebooksdktest.model.GalleryItem;
import com.lucasurbas.facebooksdktest.ui.BaseNavigator;
import com.lucasurbas.facebooksdktest.ui.BasePresenter;
import com.lucasurbas.facebooksdktest.ui.BaseView;

import java.util.List;

/**
 * Created by Lucas on 25/01/2017.
 */

public interface GalleryContract {

    interface Navigator extends BaseNavigator {

        void openCamera();

        void openGalleryItemDetails(String itemId);

    }

    interface View extends BaseView {

        void showGalleryItems(List<GalleryItem> galleryItems);

        void showEmptyScreen();

        void showToast(String message);
    }

    interface Presenter extends BasePresenter<View> {

        void takePhoto();

        void loadGalleryItems();

        void startGalleryItemDetails(String itemId);

    }

}
