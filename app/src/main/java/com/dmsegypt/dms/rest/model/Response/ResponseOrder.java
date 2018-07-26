package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Order;
import com.dmsegypt.dms.rest.model.Provider;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mahmoud on 25/12/17.
 */

public class ResponseOrder {

    @SerializedName("Message")
    @Expose
    private Message message;

    @SerializedName("List")
    @Expose
    private List<Order> list;

    public void setMessage(Message message){
        this.message = message;
    }

    public Message getMessage(){
        return message;
    }

    public void setList(List<Order> list){
        this.list = list;
    }

    public List<Order> getList(){
        return list;
    }

    @Override
    public String toString(){
        return
                "ResponseProviders{" +
                        "message = '" + message + '\'' +
                        ",list = '" + list + '\'' +
                        "}";
    }
}
