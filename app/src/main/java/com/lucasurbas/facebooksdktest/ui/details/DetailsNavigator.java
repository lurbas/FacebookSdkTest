package com.lucasurbas.facebooksdktest.ui.details;

import javax.inject.Inject;

/**
 * Created by Lucas on 25/01/2017.
 */

public class DetailsNavigator implements DetailsContract.Navigator {

    private DetailsActivity detailsActivity;

    @Inject
    public DetailsNavigator(DetailsActivity detailsActivity) {
        this.detailsActivity = detailsActivity;
    }

    @Override
    public void finish() {
        detailsActivity.finish();
    }

}
