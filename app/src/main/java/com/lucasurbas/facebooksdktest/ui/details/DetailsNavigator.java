package com.lucasurbas.facebooksdktest.ui.details;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

/**
 * Created by Lucas on 25/01/2017.
 */

public class DetailsNavigator implements DetailsContract.Navigator {

    private WeakReference<DetailsActivity> detailsActivity;

    @Inject
    public DetailsNavigator(DetailsActivity detailsActivity) {
        this.detailsActivity = new WeakReference<>(detailsActivity);
    }

    @Override
    public void finish() {
        detailsActivity.get().finish();
    }

}
