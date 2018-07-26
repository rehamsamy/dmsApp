package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mahmoud on 31/12/17.
 */

public class Company {

    @SerializedName("Comp_id")
    private String comp_id;

    @SerializedName("Comp_address1")
    private String comp_address1;

    @SerializedName("Comp_address2")
    private String comp_address2;

    @SerializedName("Comp_aname")
    private String comp_aname;

    @SerializedName("Comp_email")
    private String comp_email;

    @SerializedName("Comp_ename")
    private String comp_ename;

    @SerializedName("Comp_fax")
    private String comp_fax;

    @SerializedName("Comp_tel1")
    private String comp_tel1;

    @SerializedName("Comp_tel2")
    private String comp_tel2;

    public Company() {
    }

    public Company(String comp_id, String comp_address1, String comp_address2, String comp_aname, String comp_email, String comp_ename, String comp_fax, String comp_tel1, String comp_tel2) {
        this.comp_id = comp_id;
        this.comp_address1 = comp_address1;
        this.comp_address2 = comp_address2;
        this.comp_aname = comp_aname;
        this.comp_email = comp_email;
        this.comp_ename = comp_ename;
        this.comp_fax = comp_fax;
        this.comp_tel1 = comp_tel1;
        this.comp_tel2 = comp_tel2;
    }

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public String getComp_address1() {
        return comp_address1;
    }

    public void setComp_address1(String comp_address1) {
        this.comp_address1 = comp_address1;
    }

    public String getComp_address2() {
        return comp_address2;
    }

    public void setComp_address2(String comp_address2) {
        this.comp_address2 = comp_address2;
    }

    public String getComp_aname() {
        return comp_aname;
    }

    public void setComp_aname(String comp_aname) {
        this.comp_aname = comp_aname;
    }

    public String getComp_email() {
        return comp_email;
    }

    public void setComp_email(String comp_email) {
        this.comp_email = comp_email;
    }

    public String getComp_ename() {
        return comp_ename;
    }

    public void setComp_ename(String comp_ename) {
        this.comp_ename = comp_ename;
    }

    public String getComp_fax() {
        return comp_fax;
    }

    public void setComp_fax(String comp_fax) {
        this.comp_fax = comp_fax;
    }

    public String getComp_tel1() {
        return comp_tel1;
    }

    public void setComp_tel1(String comp_tel1) {
        this.comp_tel1 = comp_tel1;
    }

    public String getComp_tel2() {
        return comp_tel2;
    }

    public void setComp_tel2(String comp_tel2) {
        this.comp_tel2 = comp_tel2;
    }
}
