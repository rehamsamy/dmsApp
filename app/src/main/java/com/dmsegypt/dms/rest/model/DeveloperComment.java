package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mahmoud on 4/4/2018.
 */

public class DeveloperComment {
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("lastModified")
    @Expose
    private LastModified lastModified;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LastModified getLastModified() {
        return lastModified;
    }

    public void setLastModified(LastModified lastModified) {
        this.lastModified = lastModified;
    }
}
