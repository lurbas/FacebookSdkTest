package com.lucasurbas.facebooksdktest.model.facebook;

import com.google.gson.annotations.Expose;

/**
 * Created by Lucas on 26/01/2017.
 */

public class FbComment {

    @Expose public String id;
    @Expose public String message;
    @Expose public FbUser from;
}
