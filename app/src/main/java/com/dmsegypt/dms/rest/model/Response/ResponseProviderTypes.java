package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.ProviderType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseProviderTypes {

    @SerializedName("Message")
    @Expose
    private Message message;

    @SerializedName("List")
    @Expose
    private List<ProviderType> list;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public List<ProviderType> getList() {
        return list;
    }

    public void setList(List<ProviderType> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return
                "ResponseProviderTypes{" +
                        "message = '" + message + '\'' +
                        ",list = '" + list + '\'' +
                        "}";
    }
}