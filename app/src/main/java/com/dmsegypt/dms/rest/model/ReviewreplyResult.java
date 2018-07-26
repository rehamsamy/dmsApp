package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mahmoud on 4/4/2018.
 */

public class ReviewreplyResult {
    @SerializedName("replyText")
    @Expose
    private String replyText;
    @SerializedName("lastEdited")
    @Expose
    private LastModified lastEdited;

    public String getReplyText() {
        return replyText;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }

    public LastModified getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(LastModified lastEdited) {
        this.lastEdited = lastEdited;
    }
}
