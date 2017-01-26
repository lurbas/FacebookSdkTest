package com.lucasurbas.facebooksdktest.ui.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lucasurbas.facebooksdktest.R;
import com.lucasurbas.facebooksdktest.injection.app.ApplicationComponent;
import com.lucasurbas.facebooksdktest.injection.details.DaggerDetailsComponent;
import com.lucasurbas.facebooksdktest.injection.details.DetailsModule;
import com.lucasurbas.facebooksdktest.model.GalleryItem;
import com.lucasurbas.facebooksdktest.ui.util.BaseActivity;
import com.squareup.picasso.Picasso;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lucas on 26/01/2017.
 */

public class DetailsActivity extends BaseActivity implements DetailsContract.View {

    private static final String KEY_ITEM_ID = "key_item_id";

    @BindView(R.id.activity_details__image) ImageView image;
    @BindView(R.id.activity_details__likes_counter) TextView counterLikes;
    @BindView(R.id.activity_details__comments_counter) TextView counterComments;

    @Inject DetailsContract.Presenter presenter;

    public static Intent getIntent(Context context, String itemId) {
        Intent i = new Intent(context, DetailsActivity.class);
        i.putExtra(KEY_ITEM_ID, itemId);
        return i;
    }

    @Override
    protected void setupActivityComponent(ApplicationComponent applicationComponent) {
        DaggerDetailsComponent.builder()
                .applicationComponent(applicationComponent)
                .detailsModule(new DetailsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setupToolbar();
        ButterKnife.bind(this);

        String itemId = getIntent().getStringExtra(KEY_ITEM_ID);

        presenter.attachView(this);
        presenter.loadGalleryItem(itemId);
    }

    private void setupToolbar() {
        getSupportActionBar().setTitle(R.string.activity_details__title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void showGalleryItem(GalleryItem item) {
        File photoFile = new File(item.path());
        Picasso.with(this).load(photoFile)
                .fit()
                .centerCrop()
                .into(image);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showCounters(int likesCount, int commentsCount) {
        counterLikes.setText(String.valueOf(likesCount));
        counterComments.setText(String.valueOf(commentsCount));
    }
}
