package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.BatchDetail;
import com.dmsegypt.dms.rest.model.Message;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by amr on 28/11/2017.
 */

public class ResponseBatchDetail {
    @SerializedName("Sumsystemamt")
    private String sumsystemamt;
    @SerializedName("Sumclaimamt")
    private String Sumclaimamt;
    @SerializedName("Sumdifferenceamt")
    private String sumdifferenceamt;
    @SerializedName("Countdiscmedcode")
    private String countdiscmedcode;
    @SerializedName("Countdiscclaimno")
    private String countdiscclaimno;
    @SerializedName("Countbatchno")
    private String countbatchno;
    @SerializedName("Message")
    private Message message;

    public String getSumsystemamt() {
        return sumsystemamt;
    }

    public void setSumsystemamt(String sumsystemamt) {
        this.sumsystemamt = sumsystemamt;
    }

    public String getSumclaimamt() {
        return Sumclaimamt;
    }

    public void setSumclaimamt(String sumclaimamt) {
        Sumclaimamt = sumclaimamt;
    }

    public String getSumdifferenceamt() {
        return sumdifferenceamt;
    }

    public void setSumdifferenceamt(String sumdifferenceamt) {
        this.sumdifferenceamt = sumdifferenceamt;
    }

    public String getCountdiscmedcode() {
        return countdiscmedcode;
    }

    public void setCountdiscmedcode(String countdiscmedcode) {
        this.countdiscmedcode = countdiscmedcode;
    }

    public String getCountdiscclaimno() {
        return countdiscclaimno;
    }

    public void setCountdiscclaimno(String countdiscclaimno) {
        this.countdiscclaimno = countdiscclaimno;
    }

    public String getCountbatchno() {
        return countbatchno;
    }

    public void setCountbatchno(String countbatchno) {
        this.countbatchno = countbatchno;
    }

    @SerializedName("List")
    private List<BatchDetail> list;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public List<BatchDetail> getList() {
        return list;
    }

    public void setList(List<BatchDetail> list) {
        this.list = list;
    }
}
