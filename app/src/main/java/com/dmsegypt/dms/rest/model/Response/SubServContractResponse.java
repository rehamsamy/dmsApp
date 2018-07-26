
package com.dmsegypt.dms.rest.model.Response;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.SubServContract;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SubServContractResponse
{

    @SerializedName("List")
    @Expose
    private ArrayList<SubServContract> list = null;
    @SerializedName("Message")
    @Expose
    private Message message;

    public ArrayList<SubServContract> getList() {
        return list;
    }

    public void setList(ArrayList<SubServContract> list) {
        this.list = list;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }


}
