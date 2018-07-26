package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.MedicineDetails;
import com.dmsegypt.dms.rest.model.Message;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mahmoud on 10/06/17.
 */

public class ResponseMedicineDetails {


    @SerializedName("Message")
    private Message message;

    @SerializedName("List")
    private List<MedicineDetails> list;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public List<MedicineDetails> getList() {
        return list;
    }

    public void setList(List<MedicineDetails> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ResponseMedicineDetails{" +
                "message=" + message +
                ", list=" + list +
                '}';
    }
}
