package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.Review;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Sampa on 03/04/2018.
 */

public class GooglePlayReviewsResponse {

    @SerializedName("reviews")
    @Expose
    private List<Review> reviews = null;

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
