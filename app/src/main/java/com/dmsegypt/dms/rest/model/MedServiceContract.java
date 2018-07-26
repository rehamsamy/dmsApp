
package com.dmsegypt.dms.rest.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MedServiceContract implements Parcelable {

    @SerializedName("carr_amt")
    @Expose
    private String carrAmt;
    @SerializedName("ceiling_amt")
    @Expose
    private String ceilingAmt;
    @SerializedName("ceiling_pert")
    @Expose
    private String ceilingPert;
    @SerializedName("ind_list_price")
    @Expose
    private String indListPrice;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("refund_flag")
    @Expose
    private String refundFlag;
    @SerializedName("service_id")
    @Expose
    private String serviceId;
    @SerializedName("service_name")
    @Expose
    private String serviceName;


    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getCarrAmt() {
        return carrAmt;
    }

    public void setCarrAmt(String carrAmt) {
        this.carrAmt = carrAmt;
    }

    public String getCeilingAmt() {
        return ceilingAmt;
    }

    public void setCeilingAmt(String ceilingAmt) {
        this.ceilingAmt = ceilingAmt;
    }

    public String getCeilingPert() {
        return ceilingPert;
    }

    public void setCeilingPert(String ceilingPert) {
        this.ceilingPert = ceilingPert;
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


    public MedServiceContract() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.carrAmt);
        dest.writeString(this.ceilingAmt);
        dest.writeString(this.ceilingPert);
        dest.writeString(this.indListPrice);
        dest.writeString(this.notes);
        dest.writeString(this.refundFlag);
        dest.writeString(this.serviceId);
        dest.writeString(this.serviceName);
    }

    protected MedServiceContract(Parcel in) {
        this.carrAmt = in.readString();
        this.ceilingAmt = in.readString();
        this.ceilingPert = in.readString();
        this.indListPrice = in.readString();
        this.notes = in.readString();
        this.refundFlag = in.readString();
        this.serviceId = in.readString();
        this.serviceName = in.readString();
    }

    public static final Creator<MedServiceContract> CREATOR = new Creator<MedServiceContract>() {
        @Override
        public MedServiceContract createFromParcel(Parcel source) {
            return new MedServiceContract(source);
        }

        @Override
        public MedServiceContract[] newArray(int size) {
            return new MedServiceContract[size];
        }
    };
}
