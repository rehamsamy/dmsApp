package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.Agent;
import com.dmsegypt.dms.rest.model.BatchPharmacy;
import com.dmsegypt.dms.rest.model.ContractDetail;
import com.dmsegypt.dms.rest.model.Message;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mahmoud on 5/29/2018.
 */

public class AgentResponse {

    @SerializedName("Message")
    @Expose
    private Message message;


    @SerializedName("List")
    @Expose
    private List<Agent> list;


    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public List<Agent> getList() {
        return list;
    }

    public void setList(List<Agent> list) {
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
