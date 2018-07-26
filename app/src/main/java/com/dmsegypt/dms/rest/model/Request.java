package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mahmoud on 20-Sep-17.
 */

public class Request implements Serializable {


    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Image")
    @Expose
    private String image;
    @SerializedName("Card_id")
    @Expose
    private String cardID;
    @SerializedName("Request_date")
    @Expose
    private String req_date;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Emp_ename")
    @Expose
    private String emp_ename;

    @SerializedName("Id")
    @Expose
    private String id;


    @SerializedName("Req_type")
    @Expose
    private String req_type;

    @SerializedName("Reply_id")
    @Expose
    private String reply_id;


    @SerializedName("Reply_note")
    @Expose
    private String reply_note;

    @SerializedName("Reply_flag")
    @Expose
    private String reply_flag;
    @SerializedName("Reply_Img")
    @Expose
    private String reply_image;


    public String getReply_image() {
        return reply_image;
    }

    public void setReply_image(String reply_image) {
        this.reply_image = reply_image;
    }

    public String getReply_id() {
        return reply_id;
    }

    public void setReply_id(String reply_id) {
        this.reply_id = reply_id;
    }

    public String getReply_note() {
        return reply_note;
    }

    public void setReply_note(String reply_note) {
        this.reply_note = reply_note;
    }

    public String getReply_flag() {
        return reply_flag;
    }

    public void setReply_flag(String reply_flag) {
        this.reply_flag = reply_flag;
    }

    public String getReq_type() {
        return req_type;
    }

    public void setReq_type(String req_type) {
        this.req_type = req_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public String getReq_date() {
        return req_date;
    }

    public void setReq_date(String req_date) {
        this.req_date = req_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmp_ename() {
        return emp_ename;
    }

    public void setEmp_ename(String emp_ename) {
        this.emp_ename = emp_ename;
    }

}
