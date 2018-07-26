package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mahmoud on 19-Sep-17.
 */

public class Image {
    @SerializedName("CardId")
    private String cardid;

    public Image(String cardid, String base64image) {
        this.cardid = cardid;
        this.base64image = base64image;
    }

    public Image() {
    }

    @SerializedName("Base64Image")
    private String base64image;

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public void setBase64image(String base64image) {
        this.base64image = base64image;
    }

    public String getBase64image() {
        return base64image;
    }

}
