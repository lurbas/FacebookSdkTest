package com.lucasurbas.facebooksdktest.model.facebook;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by Lucas on 26/01/2017.
 */

public class FbComments {

    @Expose public List<FbComment> data;
    @Expose public FbCounterSummary summary;
}
