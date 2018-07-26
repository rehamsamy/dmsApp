package com.dmsegypt.dms.rest.model.Response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mohamed Abdallah on 25/04/2017.
 **/

public class ResponseUpdateProfileImage {

    @SerializedName("Details")
    private String details;

    @SerializedName("Code")
    private int code;

    @SerializedName("ImageURL")
    private String imageURL;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setDetails(String details){
        this.details = details;
    }

    public String getDetails(){
        return details;
    }

    @Override
    public String toString(){
        return
                "Message{" +
                        "details = '" + details + '\'' +
                        ",code = '" + code + '\'' +
                        "}";
    }
}
