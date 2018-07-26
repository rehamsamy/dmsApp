
package com.dmsegypt.dms.rest.model.Response;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.dmsegypt.dms.rest.model.ContractDetail;
import com.dmsegypt.dms.rest.model.Message;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContractDetailResponse  {

    @SerializedName("Detail")
    @Expose
    private ContractDetail detail;
    @SerializedName("Message")
    @Expose
    private Message message;






    public ContractDetail getDetail() {
        return detail;
    }

    public void setDetail(ContractDetail detail) {
        this.detail = detail;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }







}
