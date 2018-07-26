package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.DoctorSpicific;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.ProviderType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mahmoud on 9/10/2017.
 */

public class ResponseDoctorSpicific {
    @SerializedName("Message")
    @Expose
    private Message message;

    @SerializedName("List")
    @Expose
    private List<DoctorSpicific> list;
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public List<DoctorSpicific> getList() {
        return list;
    }

    public void setList(List<DoctorSpicific> list) {
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
