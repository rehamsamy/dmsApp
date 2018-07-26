package com.dmsegypt.dms.rest.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mahmoud on 4/4/2018.
 */

public class ReviewReply implements Parcelable {

    public ReviewReply() {
    }

    @SerializedName("replyText")
    @Expose()
    private String replyText = "";

    protected ReviewReply(Parcel in) {
        replyText = in.readString();
    }

    public static final Creator<ReviewReply> CREATOR = new Creator<ReviewReply>() {
        @Override
        public ReviewReply createFromParcel(Parcel in) {
            return new ReviewReply(in);
        }

        @Override
        public ReviewReply[] newArray(int size) {
            return new ReviewReply[size];
        }
    };

    public String getReplyText() {
        return replyText;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(replyText);
    }
}
