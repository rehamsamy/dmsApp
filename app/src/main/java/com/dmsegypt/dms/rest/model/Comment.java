
package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {

    @SerializedName("userComment")
    @Expose
    private UserComment userComment;
    @SerializedName("developerComment")
    @Expose
    private DeveloperComment developerComment;

    public UserComment getUserComment() {
        return userComment;
    }

    public void setUserComment(UserComment userComment) {
        this.userComment = userComment;
    }

    public DeveloperComment getDeveloperComment() {
        return developerComment;
    }

    public void setDeveloperComment(DeveloperComment developerComment) {
        this.developerComment = developerComment;
    }
}
