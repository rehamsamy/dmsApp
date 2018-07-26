package com.dmsegypt.dms.rest.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mahmoud on 3/28/2018.
 */

public class SalesCall implements Parcelable {

    public SalesCall() {
    }

    public Salesitem getSalesitem() {
        return salesitem;
    }

    public void setSalesitem(Salesitem salesitem) {
        this.salesitem = salesitem;
    }

    @SerializedName("SalesCall")
    @Expose()
    private Salesitem salesitem;

    protected SalesCall(Parcel in) {
        salesitem = in.readParcelable(Salesitem.class.getClassLoader());
    }

    public static final Creator<SalesCall> CREATOR = new Creator<SalesCall>() {
        @Override
        public SalesCall createFromParcel(Parcel in) {
            return new SalesCall(in);
        }

        @Override
        public SalesCall[] newArray(int size) {
            return new SalesCall[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(salesitem, flags);
    }
}
