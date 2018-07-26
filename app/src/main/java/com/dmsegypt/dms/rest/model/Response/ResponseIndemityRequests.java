package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.IndemnityRequest;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.NotificationItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mahmoud on 11/12/17.
 */

public class ResponseIndemityRequests {
    @SerializedName("Message")
    private Message message;

    @SerializedName("List")
    private List<IndemnityRequest> list;

    public ResponseIndemityRequests(Message message, List<IndemnityRequest> list) {
        this.message = message;
        this.list = list;
    }

    public ResponseIndemityRequests() {
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }


    @Override
    public String toString(){
        return
                "ResponseIndemityRequests{" +
                        "message = '" + message + '\'' +
                        ",list = '" + list + '\'' +
                        "}";
    }

    public List<IndemnityRequest> getList() {
        return list;
    }

    public void setList(List<IndemnityRequest> list) {
        this.list = list;
    }
}
