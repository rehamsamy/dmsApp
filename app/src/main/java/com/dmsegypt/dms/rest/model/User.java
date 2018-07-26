package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("CardId")
    @Expose
    private String cardId;
    @SerializedName("CompanyId")
    @Expose
    private String companyId;
    @SerializedName("Degree")
    @Expose
    private String degree;

    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("ImageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("LastName")
    @Expose
    private String lastName;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("NationalId")
    @Expose
    private String nationalId;
    @SerializedName("SecondName")
    @Expose
    private String secondName;


    @SerializedName("DepartmentId")
    @Expose
    private String DepartmentId;



    @SerializedName("UserType")
    @Expose
    private String userType;

    @SerializedName("Hr_request_type")
    @Expose
    private String hr_request_type;


    @SerializedName("Ins_end_date")
    @Expose
    private String ins_end_date;

    @SerializedName("Ins_start_date")
    @Expose
    private String ins_start_date;

    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("PrvId")
    @Expose
    private String PrvId;
    @SerializedName("PrvType")
    @Expose
    private String PrvType;





    private boolean isSelected;
    private String switch_user_type;

    public String getSwitch_user_type() {
        return switch_user_type;
    }

    public void setSwitch_user_type(String switch_user_type) {
        this.switch_user_type = switch_user_type;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDepartmentId() {
        return DepartmentId;
    }



    public void setDepartmentId(String departmentId) {
        DepartmentId = departmentId;
    }

    public String getIns_end_date() {
        return ins_end_date;
    }

    public void setIns_end_date(String ins_end_date) {
        this.ins_end_date = ins_end_date;
    }

    public String getIns_start_date() {
        return ins_start_date;
    }

    public void setIns_start_date(String ins_start_date) {
        this.ins_start_date = ins_start_date;
    }

    public String getHr_request_type() {
        return hr_request_type;
    }

    public void setHr_request_type(String hr_request_type) {
        this.hr_request_type = hr_request_type;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPrvId() {
        return PrvId;
    }

    public void setPrvId(String prvId) {
        PrvId = prvId;
    }

    public String getPrvType() {
        return PrvType;
    }

    public void setPrvType(String prvType) {
        PrvType = prvType;
    }

    @Override
    public String toString() {
        return
                "User{" +
                        "email = '" + email + '\'' +
                        ",degree = '" + degree + '\'' +
                        ",firstName = '" + firstName + '\'' +
                        ",nationalId = '" + nationalId + '\'' +
                        ",imageUrl = '" + imageUrl + '\'' +
                        ",cardId = '" + cardId + '\'' +
                        ",lastName = '" + lastName + '\'' +
                        ",mobile = '" + mobile + '\'' +
                        ",secondName = '" + secondName + '\'' +
                        "}";
    }
}