package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mahmoud on 11/12/17.
 */

public class IndemnityRequest {

    @SerializedName("Id")
    String id;

    @SerializedName("Description")
    String descreption;

    @SerializedName("Type")
    String type;

    @SerializedName("ImageBath")
    String imageBath;

    @SerializedName("Images")
    List<String> images;

    @SerializedName("Card_id")
    String cardId;

    @SerializedName("Request_date")
    String request_date;

    @SerializedName("Title")
    String title;

    @SerializedName("Req_type")
    String req_type;

    @SerializedName("Emp_ename")
    String emp_ename;


    public IndemnityRequest(String id, String descreption, String type, String imageBath, List<String> images, String cardId, String request_date, String title, String req_type, String emp_ename) {
        this.id = id;
        this.descreption = descreption;
        this.type = type;
        this.imageBath = imageBath;
        this.images = images;
        this.cardId = cardId;
        this.request_date = request_date;
        this.title = title;
        this.req_type = req_type;
        this.emp_ename = emp_ename;
    }

    public IndemnityRequest() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescreption() {
        return descreption;
    }

    public void setDescreption(String descreption) {
        this.descreption = descreption;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageBath() {
        return imageBath;
    }

    public void setImageBath(String imageBath) {
        this.imageBath = imageBath;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getRequest_date() {
        return request_date;
    }

    public void setRequest_date(String request_date) {
        this.request_date = request_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReq_type() {
        return req_type;
    }

    public void setReq_type(String req_type) {
        this.req_type = req_type;
    }

    public String getEmp_ename() {
        return emp_ename;
    }

    public void setEmp_ename(String emp_ename) {
        this.emp_ename = emp_ename;
    }
}
