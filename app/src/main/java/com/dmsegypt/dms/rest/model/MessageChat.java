package com.dmsegypt.dms.rest.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by amr on 18/12/2017.
 */

public class MessageChat implements Parcelable {
    private String sent_by="";
    private String message="";
    private String date="";
    private String id="";
    private int is_seen;
    private String img_url=null;
    private Uri uri;
    private int imageState=0;

    public int getImageState() {
        return imageState;
    }

    public void setImageState(int imageState) {
        this.imageState = imageState;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public int getIs_seen() {
        return is_seen;
    }

    public void setIs_seen(int is_seen) {
        this.is_seen = is_seen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSent_by() {
        return sent_by;
    }

    public void setSent_by(String sent_by) {
        this.sent_by = sent_by;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public MessageChat() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sent_by);
        dest.writeString(this.message);
        dest.writeString(this.date);
        dest.writeString(this.id);
        dest.writeInt(this.is_seen);
        dest.writeString(this.img_url);
        dest.writeParcelable(this.uri, flags);
        dest.writeInt(this.imageState);
    }

    protected MessageChat(Parcel in) {
        this.sent_by = in.readString();
        this.message = in.readString();
        this.date = in.readString();
        this.id = in.readString();
        this.is_seen = in.readInt();
        this.img_url = in.readString();
        this.uri = in.readParcelable(Uri.class.getClassLoader());
        this.imageState = in.readInt();
    }

    public static final Creator<MessageChat> CREATOR = new Creator<MessageChat>() {
        @Override
        public MessageChat createFromParcel(Parcel source) {
            return new MessageChat(source);
        }

        @Override
        public MessageChat[] newArray(int size) {
            return new MessageChat[size];
        }
    };
}
