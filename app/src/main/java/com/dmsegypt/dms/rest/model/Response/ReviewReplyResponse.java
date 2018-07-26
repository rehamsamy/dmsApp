package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.ReviewreplyResult;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mahmoud on 4/4/2018.
 */

public class ReviewReplyResponse {

    @SerializedName("result")
    @Expose
    private ReviewreplyResult result;

    public ReviewreplyResult getResult() {
        return result;
    }

    public void setResult(ReviewreplyResult result) {
        this.result = result;
    }
}
