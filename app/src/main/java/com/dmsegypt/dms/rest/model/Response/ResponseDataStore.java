package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.DataStore;
import com.dmsegypt.dms.rest.model.Message;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mahmoud on 10/01/17.
 */

public class ResponseDataStore {

    @SerializedName("Message")
    private Message message;

    @SerializedName("List")
    private List<DataStore> list;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public List<DataStore> getList() {
        return list;
    }

    public void setList(List<DataStore> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ResponseDataStore{" +
                "message=" + message +
                ", list=" + list +
                '}';
    }
}
