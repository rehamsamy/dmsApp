
package com.dmsegypt.dms.rest.model;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContractDetail implements Parcelable
{

    @SerializedName("address_company")
    @Expose
    private String addressCompany;
    @SerializedName("arabic_comapny_name")
    @Expose
    private String arabicComapnyName;
    @SerializedName("class_code_list")
    @Expose
    private ArrayList<ClassCodeContract> classCodeList = null;
    @SerializedName("comp_id")
    @Expose
    private String compId;
    @SerializedName("contract_no")
    @Expose
    private String contractNo;
    @SerializedName("end_date_contract")
    @Expose
    private String endDateContract;
    @SerializedName("english_company_name")
    @Expose
    private String englishCompanyName;
    @SerializedName("start_date_contract")
    @Expose
    private String startDateContract;


    public String getAddressCompany() {
        return addressCompany;
    }

    public void setAddressCompany(String addressCompany) {
        this.addressCompany = addressCompany;
    }

    public String getArabicComapnyName() {
        return arabicComapnyName;
    }

    public void setArabicComapnyName(String arabicComapnyName) {
        this.arabicComapnyName = arabicComapnyName;
    }

    public ArrayList<ClassCodeContract> getClassCodeList() {
        return classCodeList;
    }

    public void setClassCodeList(ArrayList<ClassCodeContract> classCodeList) {
        this.classCodeList = classCodeList;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getEndDateContract() {
        return endDateContract;
    }

    public void setEndDateContract(String endDateContract) {
        this.endDateContract = endDateContract;
    }

    public String getEnglishCompanyName() {
        return englishCompanyName;
    }

    public void setEnglishCompanyName(String englishCompanyName) {
        this.englishCompanyName = englishCompanyName;
    }

    public String getStartDateContract() {
        return startDateContract;
    }

    public void setStartDateContract(String startDateContract) {
        this.startDateContract = startDateContract;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.addressCompany);
        dest.writeString(this.arabicComapnyName);
        dest.writeTypedList(this.classCodeList);
        dest.writeString(this.compId);
        dest.writeString(this.contractNo);
        dest.writeString(this.endDateContract);
        dest.writeString(this.englishCompanyName);
        dest.writeString(this.startDateContract);
    }

    public ContractDetail() {
    }

    protected ContractDetail(Parcel in) {
        this.addressCompany = in.readString();
        this.arabicComapnyName = in.readString();
        this.classCodeList = in.createTypedArrayList(ClassCodeContract.CREATOR);
        this.compId = in.readString();
        this.contractNo = in.readString();
        this.endDateContract = in.readString();
        this.englishCompanyName = in.readString();
        this.startDateContract = in.readString();
    }

    public static final Creator<ContractDetail> CREATOR = new Creator<ContractDetail>() {
        @Override
        public ContractDetail createFromParcel(Parcel source) {
            return new ContractDetail(source);
        }

        @Override
        public ContractDetail[] newArray(int size) {
            return new ContractDetail[size];
        }
    };
}
