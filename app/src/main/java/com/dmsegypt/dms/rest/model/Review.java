
package com.dmsegypt.dms.rest.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {


    @SerializedName("reviewId")
    @Expose
    private String reviewId;
    @SerializedName("authorName")
    @Expose
    private String authorName;
    @SerializedName("comments")
    @Expose
    private List<Comment> comments = null;

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
