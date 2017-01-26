package com.lucasurbas.facebooksdktest.model;

/**
 * Created by Lucas on 26/01/2017.
 */

public class FacebookResponse {

    private int likesCount;
    private int commentsCount;

    public FacebookResponse(String rawResponse) {

    }

    public int getLikesCount() {
        return likesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }
}
