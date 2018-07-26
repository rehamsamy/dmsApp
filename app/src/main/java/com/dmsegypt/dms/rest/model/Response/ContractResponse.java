package com.dmsegypt.dms.rest.model.Response;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.dmsegypt.dms.rest.model.Message;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContractResponse
{

    @SerializedName("List")
    @Expose
    private ArrayList<String> list = null;
    @SerializedName("Message")
    @Expose
    private Message message;


    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }


}
