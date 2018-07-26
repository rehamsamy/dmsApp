package com.dmsegypt.dms.rest.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Mahmoud on 2/12/2018.
 */

public class Account  implements Serializable, Parcelable {


    private boolean checked;

    public Account() {
    }

    @SerializedName("Active")
    @Expose
    private String active;

    @SerializedName("CardId")
    @Expose
    private String cardId;



    @SerializedName("CompId")
    @Expose
    private String compId;


    @SerializedName("DepartmentId")
    @Expose
    private String departmentId;


    @SerializedName("DeviceType")
    @Expose
    private String deviceType;

    @SerializedName("Email")
    @Expose
    private String email;


    @SerializedName("EmpId")
    @Expose
    private String empId;


    @SerializedName("FirstName")
    @Expose
    private String firstName;

    @SerializedName("HrRequest")
    @Expose
    private String hrRequest;


    @SerializedName("ImageUrl")
    @Expose
    private String imageUrl;


    @SerializedName("LastLogin")
    @Expose
    private String lastLogin;





    @SerializedName("Mobile")
    @Expose
    private String mobile;



    @SerializedName("Name")
    @Expose
    private String name;



    @SerializedName("NotificationToken")
    @Expose
    private String notificationToken;


    @SerializedName("Os")
    @Expose
    private String os;



    @SerializedName("ParentCardId")
    @Expose
    private String parentCardId;



    @SerializedName("Password")
    @Expose
    private String password;



    @SerializedName("RegisterDate")
    @Expose
    private String registerDate;



    @SerializedName("SecondName")
    @Expose
    private String secondName;



    @SerializedName("SmsCode")
    @Expose
    private String smsCode;

    @SerializedName("ThirdName")
    @Expose
    private String thirdName;

    @SerializedName("Type")
    @Expose
    private String type;

    @SerializedName("Birth")
    @Expose
    private String birthdate;
    @SerializedName("PrvCode")
    @Expose
    private String prvCode;
    @SerializedName("PrvType")
    @Expose
    private String PrvType;

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getHrRequest() {
        return hrRequest;
    }

    public void setHrRequest(String hrRequest) {
        this.hrRequest = hrRequest;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotificationToken() {
        return notificationToken;
    }

    public void setNotificationToken(String notificationToken) {
        this.notificationToken = notificationToken;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getParentCardId() {
        return parentCardId;
    }

    public void setParentCardId(String parentCardId) {
        this.parentCardId = parentCardId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrvCode() {
        return prvCode;
    }

    public void setPrvCode(String prvCode) {
        this.prvCode = prvCode;
    }

    public String getPrvType() {
        return PrvType;
    }

    public void setPrvType(String prvType) {
        PrvType = prvType;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.checked ? (byte) 1 : (byte) 0);
        dest.writeString(this.active);
        dest.writeString(this.cardId);
        dest.writeString(this.compId);
        dest.writeString(this.departmentId);
        dest.writeString(this.deviceType);
        dest.writeString(this.email);
        dest.writeString(this.empId);
        dest.writeString(this.firstName);
        dest.writeString(this.hrRequest);
        dest.writeString(this.imageUrl);
        dest.writeString(this.lastLogin);
        dest.writeString(this.mobile);
        dest.writeString(this.name);
        dest.writeString(this.notificationToken);
        dest.writeString(this.os);
        dest.writeString(this.parentCardId);
        dest.writeString(this.password);
        dest.writeString(this.registerDate);
        dest.writeString(this.secondName);
        dest.writeString(this.smsCode);
        dest.writeString(this.thirdName);
        dest.writeString(this.type);
        dest.writeString(this.birthdate);
        dest.writeString(this.prvCode);
        dest.writeString(this.PrvType);
    }

    protected Account(Parcel in) {
        this.checked = in.readByte() != 0;
        this.active = in.readString();
        this.cardId = in.readString();
        this.compId = in.readString();
        this.departmentId = in.readString();
        this.deviceType = in.readString();
        this.email = in.readString();
        this.empId = in.readString();
        this.firstName = in.readString();
        this.hrRequest = in.readString();
        this.imageUrl = in.readString();
        this.lastLogin = in.readString();
        this.mobile = in.readString();
        this.name = in.readString();
        this.notificationToken = in.readString();
        this.os = in.readString();
        this.parentCardId = in.readString();
        this.password = in.readString();
        this.registerDate = in.readString();
        this.secondName = in.readString();
        this.smsCode = in.readString();
        this.thirdName = in.readString();
        this.type = in.readString();
        this.birthdate = in.readString();
        this.prvCode = in.readString();
        this.PrvType = in.readString();
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel source) {
            return new Account(source);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
}
