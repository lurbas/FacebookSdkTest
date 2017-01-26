package com.lucasurbas.facebooksdktest.model.facebook;

import com.google.gson.annotations.Expose;

/**
 * Created by Lucas on 26/01/2017.
 */

public class FbPostDetailsResponse {

    @Expose private FbLikes likes;
    @Expose private FbComments comments;

    public int getLikesCount() {
        return likes.summary.totalCount;
    }

    public int getCommentsCount() {
        return comments.summary.totalCount;
    }
}
