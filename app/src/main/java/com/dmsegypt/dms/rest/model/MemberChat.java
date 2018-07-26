package com.dmsegypt.dms.rest.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by amr on 31/12/2017.
 */

public class MemberChat implements Parcelable ,Serializable{
    private String id;
    private String name;
    private int message_count;
    private String push_token;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMessage_count() {
        return message_count;
    }

    public void setMessage_count(int message_count) {
        this.message_count = message_count;
    }

    public String getPush_token() {
        return push_token;
    }

    public void setPush_token(String push_token) {
        this.push_token = push_token;
    }

    public MemberChat() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.message_count);
        dest.writeString(this.push_token);
    }

    protected MemberChat(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.message_count = in.readInt();
        this.push_token = in.readString();
    }

    public static final Creator<MemberChat> CREATOR = new Creator<MemberChat>() {
        @Override
        public MemberChat createFromParcel(Parcel source) {
            return new MemberChat(source);
        }

        @Override
        public MemberChat[] newArray(int size) {
            return new MemberChat[size];
        }
    };
}
