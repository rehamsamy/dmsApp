package com.dmsegypt.dms.rest.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mahmoud on 5/30/2018.
 */

public class AfterSales  {

    //AfterSales
    @SerializedName("AfterSales")
    @Expose()
    private AfterSalesItem afterSalesItem;


    public AfterSales(Parcel in) {
        afterSalesItem = in.readParcelable(AfterSalesItem.class.getClassLoader());
    }



    public AfterSales() {

    }

    public AfterSalesItem getAfterSalesItem() {
        return afterSalesItem;
    }

    public void setAfterSalesItem(AfterSalesItem afterSalesItem) {
        this.afterSalesItem = afterSalesItem;
    }



}
