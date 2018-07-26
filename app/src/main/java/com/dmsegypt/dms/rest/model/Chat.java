package com.dmsegypt.dms.rest.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by amr on 18/12/2017.
 */

public class Chat implements Parcelable {
    private String id;
    private String title="";
    private int isGroup;
    private HashMap<String,MemberChat> members;
    private boolean isPrivate=false;
   private MessageChat last_message;

    public MessageChat getLast_message() {
        return last_message;
    }

    public void setLast_message(MessageChat last_message) {
        this.last_message = last_message;
    }

    public boolean isPrivate() {
        return isPrivate;
    }




    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIsGroup() {
        return isGroup;
    }
    public void setIsGroup(int isGroup) {
        this.isGroup = isGroup;
    }

    public HashMap<String, MemberChat> getMembers() {
        return members;
    }

    public void setMembers(HashMap<String, MemberChat> members) {
        this.members = members;
    }

    public Chat() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeInt(this.isGroup);
        dest.writeSerializable(this.members);
        dest.writeByte(this.isPrivate ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.last_message, flags);
    }

    protected Chat(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.isGroup = in.readInt();
        this.members = (HashMap<String, MemberChat>) in.readSerializable();
        this.isPrivate = in.readByte() != 0;
        this.last_message = in.readParcelable(MessageChat.class.getClassLoader());
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel source) {
            return new Chat(source);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };
}
