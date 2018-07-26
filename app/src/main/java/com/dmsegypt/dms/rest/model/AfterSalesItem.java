package com.dmsegypt.dms.rest.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mahmoud on 5/30/2018.
 */

public class AfterSalesItem implements Parcelable {



    public boolean isLoading;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVisitReason() {
        return visitReason;
    }

    public void setVisitReason(String visitReason) {
        this.visitReason = visitReason;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(String deletedFlag) {
        this.deletedFlag = deletedFlag;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getReasonEdit() {
        return reasonEdit;
    }

    public void setReasonEdit(String reasonEdit) {
        this.reasonEdit = reasonEdit;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    @SerializedName("Id")
    @Expose()
    private String id;

    @SerializedName("CompName")
    @Expose()
    private String companyName;


    @SerializedName("BranchName")
    @Expose()
    private String branchName;


    @SerializedName("EmpName")
    @Expose()
    private String empName;


    @SerializedName("ClientName")
    @Expose()
    private String clientName;


    @SerializedName("VisitDate")
    @Expose()
    private String visitDate;

    @SerializedName("Time")
    @Expose()
    private String time;


    @SerializedName("VisitReason")
    @Expose()
    private String visitReason;


    @SerializedName("CreatedBy")
    @Expose()
    private String createdBy;

    @SerializedName("DeletedFlag")
    @Expose()
    private String deletedFlag;

    @SerializedName("UpdatedBy")
    @Expose()
    private String updatedBy;

    @SerializedName("UpdatedDate")
    @Expose()
    private String updatedDate;

    @SerializedName("ReasonEdit")
    @Expose()
    private String reasonEdit;

    @SerializedName("Feedback")
    @Expose()
    private String feedback;


    public AfterSalesItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isLoading ? (byte) 1 : (byte) 0);
        dest.writeString(this.id);
        dest.writeString(this.companyName);
        dest.writeString(this.branchName);
        dest.writeString(this.empName);
        dest.writeString(this.clientName);
        dest.writeString(this.visitDate);
        dest.writeString(this.time);
        dest.writeString(this.visitReason);
        dest.writeString(this.createdBy);
        dest.writeString(this.deletedFlag);
        dest.writeString(this.updatedBy);
        dest.writeString(this.updatedDate);
        dest.writeString(this.reasonEdit);
        dest.writeString(this.feedback);
    }

    protected AfterSalesItem(Parcel in) {
        this.isLoading = in.readByte() != 0;
        this.id = in.readString();
        this.companyName = in.readString();
        this.branchName = in.readString();
        this.empName = in.readString();
        this.clientName = in.readString();
        this.visitDate = in.readString();
        this.time = in.readString();
        this.visitReason = in.readString();
        this.createdBy = in.readString();
        this.deletedFlag = in.readString();
        this.updatedBy = in.readString();
        this.updatedDate = in.readString();
        this.reasonEdit = in.readString();
        this.feedback = in.readString();
    }

    public static final Creator<AfterSalesItem> CREATOR = new Creator<AfterSalesItem>() {
        @Override
        public AfterSalesItem createFromParcel(Parcel source) {
            return new AfterSalesItem(source);
        }

        @Override
        public AfterSalesItem[] newArray(int size) {
            return new AfterSalesItem[size];
        }
    };
}
