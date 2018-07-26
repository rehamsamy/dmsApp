package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amr on 28/11/2017.
 */

public class BatchDetail {
    @SerializedName("Prv_branch_name")
    String prv_branch_name;
    @SerializedName("Sum_differenceamt_reason")
    String sum_differenceamt_reason;
    @SerializedName("Batch_no")
    String batch_no;
    @SerializedName("Prv_no")
    String prv_no;
    @SerializedName("Prv_name")
    String prv_name;
    @SerializedName("Prv_branch_num")
    String prv_branch_num;
    @SerializedName("Card_id")
    String card_id;
    @SerializedName("Claim_no")
    String claim_no;
    @SerializedName("Med_code")
    String med_code;
    @SerializedName("Med_name")
    String med_name;
    @SerializedName("System_amt")
    String system_amt;
    @SerializedName("Claim_amt")
    String claim_amt;
    @SerializedName("Difference_amt")
    String difference_amt;
    @SerializedName("Disc_code")
    String disc_code;
    @SerializedName("Group_name")
    String group_name;
    @SerializedName("Unit")
    String unit;

    public String getPrv_branch_name() {
        return prv_branch_name;
    }

    public void setPrv_branch_name(String prv_branch_name) {
        this.prv_branch_name = prv_branch_name;
    }

    public String getSum_differenceamt_reason() {
        return sum_differenceamt_reason;
    }

    public void setSum_differenceamt_reason(String sum_differenceamt_reason) {
        this.sum_differenceamt_reason = sum_differenceamt_reason;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public String getPrv_no() {
        return prv_no;
    }

    public void setPrv_no(String prv_no) {
        this.prv_no = prv_no;
    }

    public String getPrv_name() {
        return prv_name;
    }

    public void setPrv_name(String prv_name) {
        this.prv_name = prv_name;
    }

    public String getPrv_branch_num() {
        return prv_branch_num;
    }

    public void setPrv_branch_num(String prv_branch_num) {
        this.prv_branch_num = prv_branch_num;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getClaim_no() {
        return claim_no;
    }

    public void setClaim_no(String claim_no) {
        this.claim_no = claim_no;
    }

    public String getMed_code() {
        return med_code;
    }

    public void setMed_code(String med_code) {
        this.med_code = med_code;
    }

    public String getMed_name() {
        return med_name;
    }

    public void setMed_name(String med_name) {
        this.med_name = med_name;
    }

    public String getSystem_amt() {
        return system_amt;
    }

    public void setSystem_amt(String system_amt) {
        this.system_amt = system_amt;
    }

    public String getClaim_amt() {
        return claim_amt;
    }

    public void setClaim_amt(String claim_amt) {
        this.claim_amt = claim_amt;
    }

    public String getDifference_amt() {
        return difference_amt;
    }

    public void setDifference_amt(String difference_amt) {
        this.difference_amt = difference_amt;
    }

    public String getDisc_code() {
        return disc_code;
    }

    public void setDisc_code(String disc_code) {
        this.disc_code = disc_code;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object obj) {
        BatchDetail detail=(BatchDetail)obj;
        return this.group_name.equals(detail.group_name);
    }
}
