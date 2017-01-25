package com.lucasurbas.facebooksdktest.ui.gallery;

import android.os.Bundle;
import android.widget.Toast;

import com.lucasurbas.facebooksdktest.R;
import com.lucasurbas.facebooksdktest.injection.app.ApplicationComponent;
import com.lucasurbas.facebooksdktest.injection.gallery.DaggerGalleryComponent;
import com.lucasurbas.facebooksdktest.model.GalleryItem;
import com.lucasurbas.facebooksdktest.ui.BaseActivity;

import java.util.List;

public class GalleryActivity extends BaseActivity implements GalleryContract.View {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
    }

    @Override
    protected void setupActivityComponent(ApplicationComponent applicationComponent) {
        DaggerGalleryComponent.builder()
                .applicationComponent(applicationComponent)
                .build()
                .inject(this);

    }

    @Override
    public void showGalleryItems(List<GalleryItem> galleryItems) {

    }

    @Override
    public void showEmptyScreen() {

    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
