package model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.lucasurbas.facebooksdktest.model.GalleryEntryModel;
import com.squareup.sqldelight.RowMapper;

/**
 * Created by Lucas on 25/01/2017.
 */

@AutoValue
public abstract class GalleryEntry implements GalleryEntryModel {

    public static GalleryEntry create(String _id, String uri, boolean isShared){
        return new AutoValue_GalleryEntry(_id, uri, isShared ? 1 : 0);
    }

    private static final Factory<GalleryEntry> FACTORY = new Factory<>(new Creator<GalleryEntry>() {
        @Override
        public GalleryEntry create(@NonNull String _id, String uri, long isShared) {
            return new AutoValue_GalleryEntry(_id, uri, isShared);
        }
    });

    public static final RowMapper<GalleryEntry> SELECT_ALL_MAPPER = FACTORY.select_allMapper();

    public static final RowMapper<GalleryEntry> SELECT_BY_ID_MAPPER = FACTORY.select_by_idMapper();

    public boolean isShared(){
        return is_shared() == 1;
    }
}
