package com.lucasurbas.facebooksdktest.ui.gallery;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lucasurbas.facebooksdktest.R;
import com.lucasurbas.facebooksdktest.model.GalleryItem;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by Lucas on 25/01/2017.
 */

public class GalleryItemsAdapter extends RecyclerView.Adapter<GalleryItemsAdapter.ViewHolder> {

    private List<GalleryItem> galleryItems;
    private final PublishSubject<GalleryItem> onClickSubject;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_view_gallery_item__image) ImageView image;
        @BindView(R.id.item_view_gallery_item__shared) ImageView shared;

        private Picasso picasso;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            picasso = Picasso.with(view.getContext());
        }

        public void setItem(GalleryItem item) {
            // Show facebook icon next to image if already published
            shared.setVisibility(item.post_id() != null ? View.VISIBLE : View.GONE);
            // Load image from file
            File photoFile = new File(item.path());
            picasso.load(photoFile)
                    .fit()
                    .centerCrop()
                    .into(image);
        }
    }

    public GalleryItemsAdapter() {
        this.galleryItems = new ArrayList<>();
        this.onClickSubject = PublishSubject.create();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_gallery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GalleryItem item = galleryItems.get(position);
        holder.setItem(item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSubject.onNext(item);
            }
        });
    }

    public void setGalleryItems(List<GalleryItem> peopleList) {
        this.galleryItems.clear();
        this.galleryItems.addAll(peopleList);
        notifyDataSetChanged();
    }

    public Observable<GalleryItem> getItemClickObservable() {
        return onClickSubject.asObservable();
    }

    @Override
    public int getItemCount() {
        return galleryItems.size();
    }
}
