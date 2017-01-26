package com.lucasurbas.facebooksdktest.injection.details;

import com.lucasurbas.facebooksdktest.injection.ActivityScope;
import com.lucasurbas.facebooksdktest.injection.app.ApplicationComponent;
import com.lucasurbas.facebooksdktest.ui.details.DetailsActivity;

import dagger.Component;

/**
 * Created by Lucas on 26/01/2017.
 */

@ActivityScope
@Component(
        dependencies = {ApplicationComponent.class},
        modules = {DetailsModule.class}
)
public interface DetailsComponent {

    void inject(DetailsActivity activity);
}
