package com.lucasurbas.facebooksdktest.ui.gallery;

import android.os.Bundle;

import com.lucasurbas.facebooksdktest.model.GalleryItem;
import com.lucasurbas.facebooksdktest.ui.util.BaseNavigator;
import com.lucasurbas.facebooksdktest.ui.util.BasePresenter;
import com.lucasurbas.facebooksdktest.ui.util.BaseView;

import java.util.List;

/**
 * Created by Lucas on 25/01/2017.
 */

public interface GalleryContract {

    interface Navigator extends BaseNavigator {

        String openCamera();

        void openGalleryItemDetails(String itemId);

        void openLoginScreen();

        void finish();
    }

    interface View extends BaseView {

        void showGalleryItems(List<GalleryItem> galleryItems);

        void showEmptyScreen();

        void showToast(String message);

        void showPublishDialog(GalleryItem item);
    }

    interface Presenter extends BasePresenter<View> {

        Bundle saveState();

        void restoreState(Bundle bundle);

        void takePhoto();

        void loadGalleryItems();

        void galleryItemClick(GalleryItem item);

        void savePictureAsGalleryItem();

        void publishOnFacebook(GalleryItem item);

        boolean checkFacebookAccess();

        void logout();
    }

}
