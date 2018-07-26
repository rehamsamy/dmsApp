package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.AreaItem;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.User;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseMembers {

    @SerializedName("Message")
    private Message message;

    @SerializedName("List")
    private List<User> list;

    @SerializedName("User")
    private User user;

    public User getUser() {
        return user;
    }

    public Message getMessage() {
        return message;
    }

    public List<User> getList() {
        return list;
    }

    @Override
    public String toString() {
        return
                "ResponseAreas{" +
                        "message = '" + message + '\'' +
                        ",list = '" + user + '\'' +
                        "}";
    }
}