package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.BatchSummary;
import com.dmsegypt.dms.rest.model.Message;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by amr on 28/11/2017.
 */

public class ResponseBatchSummary {
    @SerializedName("Message")
    public Message message;

    @SerializedName("List")
    public List<BatchSummary> list;


    public Message getMessage() {
        return message;
    }

    public ResponseBatchSummary(Message message, List<BatchSummary> list) {
        this.message = message;
        this.list = list;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public List<BatchSummary> getList() {
        return list;
    }

    public void setList(List<BatchSummary> list) {
        this.list = list;
    }
}
