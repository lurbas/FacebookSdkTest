package com.lucasurbas.facebooksdktest.ui.gallery;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.lucasurbas.facebooksdktest.R;
import com.lucasurbas.facebooksdktest.injection.app.ApplicationComponent;
import com.lucasurbas.facebooksdktest.injection.gallery.DaggerGalleryComponent;
import com.lucasurbas.facebooksdktest.model.GalleryItem;
import com.lucasurbas.facebooksdktest.ui.util.BaseActivity;
import com.lucasurbas.facebooksdktest.ui.util.ViewUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class GalleryActivity extends BaseActivity implements GalleryContract.View {

    private static final int ITEM_WIDTH_DP = 160;

    @BindView(R.id.activity_gallery__recycler_view) RecyclerView recyclerView;
    @BindView(R.id.activity_gallery__button_photo) View buttonPhoto;

    @Inject GalleryContract.Presenter presenter;

    private GalleryItemsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        setupRecyclerView();

        presenter.attachView(this);
        presenter.loadGalleryItems();
    }

    private void setupRecyclerView() {
        recyclerView.setHasFixedSize(true);

        final GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        ViewUtils.onLaidOut(recyclerView, new Runnable() {
            @Override
            public void run() {
                int viewWidth = recyclerView.getMeasuredWidth();
                float itemViewWidth = ViewUtils.convertDpToPixel(ITEM_WIDTH_DP, GalleryActivity.this);
                int newSpanCount = (int) Math.floor(viewWidth / itemViewWidth);
                layoutManager.setSpanCount(newSpanCount);
                layoutManager.requestLayout();
            }
        });
        adapter = new GalleryItemsAdapter();
        adapter.getItemClickObservable()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<GalleryItem>() {
                    @Override
                    public void call(GalleryItem item) {
                        presenter.galleryItemClick(item);
                    }
                });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
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
        adapter.setGalleryItems(galleryItems);
    }

    @Override
    public void showEmptyScreen() {

    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
