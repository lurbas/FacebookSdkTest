package com.lucasurbas.facebooksdktest.injection.details;

import android.content.Context;

import com.lucasurbas.facebooksdktest.injection.ActivityScope;
import com.lucasurbas.facebooksdktest.ui.details.DetailsActivity;
import com.lucasurbas.facebooksdktest.ui.details.DetailsContract;
import com.lucasurbas.facebooksdktest.ui.details.DetailsNavigator;
import com.lucasurbas.facebooksdktest.ui.details.DetailsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Lucas on 26/01/2017.
 */

@Module
public class DetailsModule {

    private final DetailsActivity detailsActivity;

    public DetailsModule(DetailsActivity detailsActivity) {
        this.detailsActivity = detailsActivity;
    }

    @Provides
    @ActivityScope
    DetailsContract.Presenter providePresenter(DetailsPresenter presenter) {
        return presenter;
    }

    @Provides
    @ActivityScope
    DetailsContract.Navigator provideNavigator(DetailsNavigator navigator) {
        return navigator;
    }

    @Provides
    @ActivityScope
    Context provideContext(){
        return detailsActivity;
    }

    @Provides
    @ActivityScope
    DetailsActivity provideDetailsActivity(){
        return detailsActivity;
    }

}
