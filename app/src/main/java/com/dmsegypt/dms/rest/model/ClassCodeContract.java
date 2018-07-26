
package com.dmsegypt.dms.rest.model;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClassCodeContract implements Parcelable
{

    @SerializedName("accommodation_degree")
    @Expose
    private String accDegree;
    @SerializedName("class_id")
    @Expose
    private String classId;
    @SerializedName("class_name")
    @Expose
    private String className;
    @SerializedName("hospital_degree")
    @Expose
    private String hospitalDegree;
    @SerializedName("max_amount")
    @Expose
    private String maxAmount;
    @SerializedName("med_service_list")
    @Expose
    private ArrayList<MedServiceContract> medServiceList = null;

    public String getAccDegree() {
        return accDegree;
    }

    public void setAmbulance(String accDegree) {
        this.accDegree = accDegree;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getHospitalDegree() {
        return hospitalDegree;
    }

    public void setHospitalDegree(String hospitalDegree) {
        this.hospitalDegree = hospitalDegree;
    }

    public String getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(String maxAmount) {
        this.maxAmount = maxAmount;
    }

    public ArrayList<MedServiceContract> getMedServiceList() {
        return medServiceList;
    }

    public void setMedServiceList(ArrayList<MedServiceContract> medServiceList) {
        this.medServiceList = medServiceList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accDegree);
        dest.writeString(this.classId);
        dest.writeString(this.className);
        dest.writeString(this.hospitalDegree);
        dest.writeString(this.maxAmount);
        dest.writeTypedList(this.medServiceList);
    }

    public ClassCodeContract() {
    }

    protected ClassCodeContract(Parcel in) {
        this.accDegree = in.readString();
        this.classId = in.readString();
        this.className = in.readString();
        this.hospitalDegree = in.readString();
        this.maxAmount = in.readString();
        this.medServiceList = in.createTypedArrayList(MedServiceContract.CREATOR);
    }

    public static final Creator<ClassCodeContract> CREATOR = new Creator<ClassCodeContract>() {
        @Override
        public ClassCodeContract createFromParcel(Parcel source) {
            return new ClassCodeContract(source);
        }

        @Override
        public ClassCodeContract[] newArray(int size) {
            return new ClassCodeContract[size];
        }
    };
}
