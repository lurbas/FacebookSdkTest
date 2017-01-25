package com.lucasurbas.facebooksdktest.ui.gallery;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lucasurbas.facebooksdktest.R;
import com.lucasurbas.facebooksdktest.constants.Constants;
import com.lucasurbas.facebooksdktest.injection.app.ApplicationComponent;
import com.lucasurbas.facebooksdktest.injection.gallery.DaggerGalleryComponent;
import com.lucasurbas.facebooksdktest.injection.gallery.GalleryModule;
import com.lucasurbas.facebooksdktest.model.GalleryItem;
import com.lucasurbas.facebooksdktest.ui.util.BaseActivity;
import com.lucasurbas.facebooksdktest.ui.util.ViewUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class GalleryActivity extends BaseActivity implements GalleryContract.View {

    private static final String KEY_PRESENTER_STATE = "key_presenter_state";
    private static final int ITEM_WIDTH_DP = 160;

    @BindView(R.id.activity_gallery__recycler_view) RecyclerView recyclerView;
    @BindView(R.id.activity_gallery__button_photo) View buttonPhoto;
    @BindView(R.id.activity_gallery__empty_state) View emptyState;

    @Inject GalleryContract.Presenter presenter;

    private GalleryItemsAdapter adapter;
    private Subscription subscription;

    @Override
    protected void setupActivityComponent(ApplicationComponent applicationComponent) {
        DaggerGalleryComponent.builder()
                .applicationComponent(applicationComponent)
                .galleryModule(new GalleryModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        setupRecyclerView();

        presenter.attachView(this);
        if (savedInstanceState != null) {
            presenter.restoreState(savedInstanceState.getBundle(KEY_PRESENTER_STATE));
        }
        presenter.loadGalleryItems();
    }

    private void setupRecyclerView() {
        recyclerView.setHasFixedSize(true);

        final GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
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
        subscription = adapter.getItemClickObservable()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<GalleryItem>() {
                    @Override
                    public void call(GalleryItem item) {
                        presenter.galleryItemClick(item);
                    }
                });
        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.activity_gallery__button_photo)
    void onPhotoButtonClick() {
        presenter.takePhoto();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBundle(KEY_PRESENTER_STATE, presenter.saveState());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constants.REQUEST_TAKE_PHOTO) {
            presenter.savePictureAsGalleryItem();
        }
    }

    @Override
    public void showGalleryItems(List<GalleryItem> galleryItems) {
        emptyState.setVisibility(View.GONE);
        adapter.setGalleryItems(galleryItems);
    }

    @Override
    public void showEmptyScreen() {
        emptyState.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPublishDialog(final GalleryItem item) {
        new MaterialDialog.Builder(this)
                .title(R.string.activity_gallery__publish_title)
                .content(R.string.activity_gallery__publish_content)
                .positiveText(R.string.dialog_agree)
                .negativeText(R.string.dialog_disagree)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        RxPermissions rxPermissions = new RxPermissions(GalleryActivity.this);
                        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                                .subscribe(new Action1<Boolean>() {
                                    @Override
                                    public void call(Boolean granted) {
                                        if (granted) {
                                            presenter.publishOnFacebook(item);
                                        }else{
                                            showToast("no permissions");
                                        }
                                    }
                                });
                    }
                })
                .show();
    }
}
