package com.dmsegypt.dms.rest.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mahmoud on 4/29/2018.
 */

public class MedicineOrder implements Parcelable{

    @SerializedName("OrderDate")
    @Expose
    private String orderDate;


    @SerializedName("OrderName")
    @Expose
    private String orderName;


    @SerializedName("PharmCode")
    @Expose
    private String pharmCode;


    @SerializedName("BranchCode")
    @Expose
    private String branchCode;


    @SerializedName("OrderState")
    @Expose
    private String orderState;


    @SerializedName("PresImgUrl")
    @Expose
    private String presImgUrl;


    @SerializedName("Notes")
    @Expose
    private String notes;

    @SerializedName("Reply")
    @Expose
    private String reply;


    @SerializedName("CardId")
    @Expose
    private String cardId;


    @SerializedName("PharmName")
    @Expose
    private String pharmName;

    @SerializedName("BranchName")
    @Expose
    private String branchName;

    boolean isLoading=false;


    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getPharmCode() {
        return pharmCode;
    }

    public void setPharmCode(String pharmCode) {
        this.pharmCode = pharmCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getPresImgUrl() {
        return presImgUrl;
    }

    public void setPresImgUrl(String presImgUrl) {
        this.presImgUrl = presImgUrl;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getPharmName() {
        return pharmName;
    }

    public void setPharmName(String pharmName) {
        this.pharmName = pharmName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderDate);
        dest.writeString(this.orderName);
        dest.writeString(this.pharmCode);
        dest.writeString(this.branchCode);
        dest.writeString(this.orderState);
        dest.writeString(this.presImgUrl);
        dest.writeString(this.notes);
        dest.writeString(this.reply);
        dest.writeString(this.cardId);
        dest.writeString(this.pharmName);
        dest.writeString(this.branchName);
        dest.writeByte(this.isLoading ? (byte) 1 : (byte) 0);
    }

    public MedicineOrder() {
    }

    protected MedicineOrder(Parcel in) {
        this.orderDate = in.readString();
        this.orderName = in.readString();
        this.pharmCode = in.readString();
        this.branchCode = in.readString();
        this.orderState = in.readString();
        this.presImgUrl = in.readString();
        this.notes = in.readString();
        this.reply = in.readString();
        this.cardId = in.readString();
        this.pharmName = in.readString();
        this.branchName = in.readString();
        this.isLoading = in.readByte() != 0;
    }

    public static final Creator<MedicineOrder> CREATOR = new Creator<MedicineOrder>() {
        @Override
        public MedicineOrder createFromParcel(Parcel source) {
            return new MedicineOrder(source);
        }

        @Override
        public MedicineOrder[] newArray(int size) {
            return new MedicineOrder[size];
        }
    };
}
