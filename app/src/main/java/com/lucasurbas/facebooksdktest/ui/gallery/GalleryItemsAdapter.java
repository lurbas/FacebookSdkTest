package com.lucasurbas.facebooksdktest.ui.gallery;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lucasurbas.facebooksdktest.R;
import com.lucasurbas.facebooksdktest.model.GalleryItem;

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
    private final PublishSubject<GalleryItem> onClickSubject = PublishSubject.create();


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_view_gallery_item__image) ImageView image;
        @BindView(R.id.item_view_gallery_item__shared) ImageView shared;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setItem(GalleryItem item) {
            shared.setVisibility(item.isShared() ? View.VISIBLE : View.GONE);
        }
    }

    public GalleryItemsAdapter() {
        this.galleryItems = new ArrayList<GalleryItem>();
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

    public Observable<GalleryItem> getItemClickObservable(){
        return onClickSubject.asObservable();
    }

    @Override
    public int getItemCount() {
        return galleryItems.size();
    }
}
