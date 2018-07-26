package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mahmoud on 2/15/2018.
 */

public class CardIdsList {

    @SerializedName("list")
    @Expose()
    private List<String> cardIdsList;

    public List<String> getCardIdsList() {
        return cardIdsList;
    }

    public void setCardIdsList(List<String> cardIdsList) {
        this.cardIdsList = cardIdsList;
    }

    public CardIdsList() {
    }
}
