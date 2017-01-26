package com.lucasurbas.facebooksdktest.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.RowMapper;

/**
 * Created by Lucas on 25/01/2017.
 */

@AutoValue
public abstract class GalleryItem implements GalleryItemModel {

    public static GalleryItem create(String _id, String uri, String postId){
        return new AutoValue_GalleryItem(_id, uri, postId);
    }

    public static final Factory<GalleryItem> FACTORY = new Factory<>(new Creator<GalleryItem>() {
        @Override
        public GalleryItem create(@NonNull String _id, String uri, String postId) {
            return new AutoValue_GalleryItem(_id, uri, postId);
        }
    });

    public static final RowMapper<GalleryItem> SELECT_ALL_MAPPER = FACTORY.select_allMapper();

    public static final RowMapper<GalleryItem> SELECT_BY_ID_MAPPER = FACTORY.select_by_idMapper();

}
