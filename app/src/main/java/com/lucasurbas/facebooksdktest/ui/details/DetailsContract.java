package com.lucasurbas.facebooksdktest.ui.details;

import com.lucasurbas.facebooksdktest.model.GalleryItem;
import com.lucasurbas.facebooksdktest.ui.util.BasePresenter;
import com.lucasurbas.facebooksdktest.ui.util.BaseView;

/**
 * Created by Lucas on 26/01/2017.
 */

public interface DetailsContract {

    interface Navigator {
        void finish();
    }

    interface View extends BaseView {

        void showGalleryItem(GalleryItem galleryItem);

        void showToast(String message);

        void showCounters(int likesCount, int commentsCount);
    }

    interface Presenter extends BasePresenter<DetailsContract.View> {

        void loadGalleryItem(String itemId);

    }
}