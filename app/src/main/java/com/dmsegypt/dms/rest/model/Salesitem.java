package com.dmsegypt.dms.rest.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mahmoud on 3/28/2018.
 */

public class Salesitem implements Parcelable {

    boolean loading;

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public static Creator<Salesitem> getCREATOR() {
        return CREATOR;
    }

    @SerializedName("Active")
    @Expose
    private String active;
    @SerializedName("AgentAname")
    @Expose
    private String agentAname;
    @SerializedName("AgentEname")
    @Expose
    private String agentEname;
    @SerializedName("AreaCode")
    @Expose
    private String areaCode;
    @SerializedName("CallCount")
    @Expose
    private String callCount;
    @SerializedName("CallDate")
    @Expose
    private String callDate;
    @SerializedName("CallTime")
    @Expose
    private String callTime;
    @SerializedName("ContactMail")
    @Expose
    private String contactMail;
    @SerializedName("ContactName")
    @Expose
    private String contactName;
    @SerializedName("ContacatTele")
    @Expose
    private String contacatTele;
    @SerializedName("CreatedBy")
    @Expose
    private String createdBy;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("CustomerType")
    @Expose
    private String customerType;
    @SerializedName("Device")
    @Expose
    private String device;
    @SerializedName("FaxNo")
    @Expose
    private String faxNo;
    @SerializedName("Finish")
    @Expose
    private String finish;
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("Mail")
    @Expose
    private String mail;
    @SerializedName("MeetingDate")
    @Expose
    private String meetingDate;
    @SerializedName("MeetingTime")
    @Expose
    private String meetingTime;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("NextCallDate")
    @Expose
    private String nextCallDate;
    @SerializedName("NextDateTime")
    @Expose
    private String nextDateTime;
    @SerializedName("NotInterestReply")
    @Expose
    private String notInterestReply;
    @SerializedName("NotInterestReplyNotes")
    @Expose
    private String notInterestReplyNotes;
    @SerializedName("ParentId")
    @Expose
    private String parentId;
    @SerializedName("ReplyNotes")
    @Expose
    private String replyNotes;
    @SerializedName("ReplyType")
    @Expose
    private String replyType;
    @SerializedName("TeleNo")
    @Expose
    private String teleNo;
    @SerializedName("CompName")
    @Expose
    private String compName;
    @SerializedName("EndDateContract")
    @Expose
    private String endDateContract;
    @SerializedName("ContractDetail")
    @Expose
    private List<TeleContractDetail> contractDetail = null;

    public Salesitem(Parcel in) {
        active = in.readString();
        agentAname = in.readString();
        agentEname = in.readString();
        areaCode = in.readString();
        callCount = in.readString();
        callDate = in.readString();
        callTime = in.readString();
        contactMail = in.readString();
        contactName = in.readString();
        contacatTele = in.readString();
        createdBy = in.readString();
        createdDate = in.readString();
        customerType = in.readString();
        device = in.readString();
        faxNo = in.readString();
        finish = in.readString();
        id = in.readString();
        mail = in.readString();
        meetingDate = in.readString();
        meetingTime = in.readString();
        mobile = in.readString();
        nextCallDate = in.readString();
        nextDateTime = in.readString();
        notInterestReply = in.readString();
        notInterestReplyNotes = in.readString();
        parentId = in.readString();
        replyNotes = in.readString();
        replyType = in.readString();
        teleNo = in.readString();
        compName = in.readString();
        endDateContract = in.readString();
        contractDetail = in.createTypedArrayList(TeleContractDetail.CREATOR);
    }

    public static final Creator<Salesitem> CREATOR = new Creator<Salesitem>() {
        @Override
        public Salesitem createFromParcel(Parcel in) {
            return new Salesitem(in);
        }

        @Override
        public Salesitem[] newArray(int size) {
            return new Salesitem[size];
        }
    };

    public Salesitem() {

    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getAgentAname() {
        return agentAname;
    }

    public void setAgentAname(String agentAname) {
        this.agentAname = agentAname;
    }

    public String getAgentEname() {
        return agentEname;
    }

    public void setAgentEname(String agentEname) {
        this.agentEname = agentEname;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getCallCount() {
        return callCount;
    }

    public void setCallCount(String callCount) {
        this.callCount = callCount;
    }

    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public String getContactMail() {
        return contactMail;
    }

    public void setContactMail(String contactMail) {
        this.contactMail = contactMail;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContacatTele() {
        return contacatTele;
    }

    public void setContacatTele(String contacatTele) {
        this.contacatTele = contacatTele;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getFaxNo() {
        return faxNo;
    }

    public void setFaxNo(String faxNo) {
        this.faxNo = faxNo;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNextCallDate() {
        return nextCallDate;
    }

    public void setNextCallDate(String nextCallDate) {
        this.nextCallDate = nextCallDate;
    }

    public String getNextDateTime() {
        return nextDateTime;
    }

    public void setNextDateTime(String nextDateTime) {
        this.nextDateTime = nextDateTime;
    }

    public String getNotInterestReply() {
        return notInterestReply;
    }

    public void setNotInterestReply(String notInterestReply) {
        this.notInterestReply = notInterestReply;
    }

    public String getNotInterestReplyNotes() {
        return notInterestReplyNotes;
    }

    public void setNotInterestReplyNotes(String notInterestReplyNotes) {
        this.notInterestReplyNotes = notInterestReplyNotes;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getReplyNotes() {
        return replyNotes;
    }

    public void setReplyNotes(String replyNotes) {
        this.replyNotes = replyNotes;
    }

    public String getReplyType() {
        return replyType;
    }

    public void setReplyType(String replyType) {
        this.replyType = replyType;
    }

    public String getTeleNo() {
        return teleNo;
    }

    public void setTeleNo(String teleNo) {
        this.teleNo = teleNo;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getEndDateContract() {
        return endDateContract;
    }

    public void setEndDateContract(String endDateContract) {
        this.endDateContract = endDateContract;
    }

    public List<TeleContractDetail> getContractDetail() {
        return contractDetail;
    }

    public void setContractDetail(List<TeleContractDetail> contractDetail) {
        this.contractDetail = contractDetail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(active);
        dest.writeString(agentAname);
        dest.writeString(agentEname);
        dest.writeString(areaCode);
        dest.writeString(callCount);
        dest.writeString(callDate);
        dest.writeString(callTime);
        dest.writeString(contactMail);
        dest.writeString(contactName);
        dest.writeString(contacatTele);
        dest.writeString(createdBy);
        dest.writeString(createdDate);
        dest.writeString(customerType);
        dest.writeString(device);
        dest.writeString(faxNo);
        dest.writeString(finish);
        dest.writeString(id);
        dest.writeString(mail);
        dest.writeString(meetingDate);
        dest.writeString(meetingTime);
        dest.writeString(mobile);
        dest.writeString(nextCallDate);
        dest.writeString(nextDateTime);
        dest.writeString(notInterestReply);
        dest.writeString(notInterestReplyNotes);
        dest.writeString(parentId);
        dest.writeString(replyNotes);
        dest.writeString(replyType);
        dest.writeString(teleNo);
        dest.writeString(compName);
        dest.writeString(endDateContract);
        dest.writeTypedList(contractDetail);
    }
}
