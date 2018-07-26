
package com.dmsegypt.dms.rest.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubServContract implements Parcelable
{

    @SerializedName("carr_amt")
    @Expose
    private String carrAmt;
    @SerializedName("ind_list_price")
    @Expose
    private String indListPrice;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("refund_flag")
    @Expose
    private String refundFlag;
    @SerializedName("sub_ceiling_amt")
    @Expose
    private String subCeilingAmt;
    @SerializedName("sub_ceiling_pert")
    @Expose
    private String subCeilingPert;
    @SerializedName("sub_service_id")
    @Expose
    private String subServiceId;
    @SerializedName("sub_service_name")
    @Expose
    private String subServiceName;


    public String getCarrAmt() {
        return carrAmt;
    }

    public void setCarrAmt(String carrAmt) {
        this.carrAmt = carrAmt;
    }

    public String getIndListPrice() {
        return indListPrice;
    }

    public void setIndListPrice(String indListPrice) {
        this.indListPrice = indListPrice;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getRefundFlag() {
        return refundFlag;
    }

    public void setRefundFlag(String refundFlag) {
        this.refundFlag = refundFlag;
    }

    public String getSubCeilingAmt() {
        return subCeilingAmt;
    }

    public void setSubCeilingAmt(String subCeilingAmt) {
        this.subCeilingAmt = subCeilingAmt;
    }

    public String getSubCeilingPert() {
        return subCeilingPert;
    }

    public void setSubCeilingPert(String subCeilingPert) {
        this.subCeilingPert = subCeilingPert;
    }

    public String getSubServiceId() {
        return subServiceId;
    }

    public void setSubServiceId(String subServiceId) {
        this.subServiceId = subServiceId;
    }

    public String getSubServiceName() {
        return subServiceName;
    }

    public void setSubServiceName(String subServiceName) {
        this.subServiceName = subServiceName;
    }

    public SubServContract() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.carrAmt);
        dest.writeString(this.indListPrice);
        dest.writeString(this.notes);
        dest.writeString(this.refundFlag);
        dest.writeString(this.subCeilingAmt);
        dest.writeString(this.subCeilingPert);
        dest.writeString(this.subServiceId);
        dest.writeString(this.subServiceName);
    }

    protected SubServContract(Parcel in) {
        this.carrAmt = in.readString();
        this.indListPrice = in.readString();
        this.notes = in.readString();
        this.refundFlag = in.readString();
        this.subCeilingAmt = in.readString();
        this.subCeilingPert = in.readString();
        this.subServiceId = in.readString();
        this.subServiceName = in.readString();
    }

    public static final Creator<SubServContract> CREATOR = new Creator<SubServContract>() {
        @Override
        public SubServContract createFromParcel(Parcel source) {
            return new SubServContract(source);
        }

        @Override
        public SubServContract[] newArray(int size) {
            return new SubServContract[size];
        }
    };
}
