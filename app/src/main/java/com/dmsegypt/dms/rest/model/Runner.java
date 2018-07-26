package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mahmoud on 28/12/17.
 */

public class Runner implements Serializable {


    @SerializedName("Runner_address")
    @Expose
    private String runner_address;

    @SerializedName("Runner_aname")
    @Expose
    private String runner_aname;

    @SerializedName("Runner_birthdate")
    @Expose
    private String runner_birthdate;


    @SerializedName("Runner_createdby")
    @Expose
    private String runner_createdby;


    @SerializedName("Runner_createddate")
    @Expose
    private String runner_createddate;


    @SerializedName("Runner_ename")
    @Expose
    private String runner_ename;


    @SerializedName("Runner_id")
    @Expose
    private String runner_id;

    @SerializedName("Runner_lic")
    @Expose
    private String runner_lic;


    @SerializedName("Runner_national")
    @Expose
    private String runner_national;


    @SerializedName("Runner_own")
    @Expose
    private String runner_own;


    @SerializedName("Runner_pwd")
    @Expose
    private String runner_pwd;


    @SerializedName("Runner_rate")
    @Expose
    private String runner_rate;


    @SerializedName("Runner_status")
    @Expose
    private String runner_status;


    @SerializedName("Runner_updatedby")
    @Expose
    private String runner_updatedby;

    @SerializedName("Runner_updateddate")
    @Expose
    private String runner_updateddate;


    @SerializedName("Runner_username")
    @Expose
    private String runner_username;

    @SerializedName("Runner_work")
    @Expose
    private String runner_work;

    public String getRunner_address() {
        return runner_address;
    }

    public void setRunner_address(String runner_address) {
        this.runner_address = runner_address;
    }

    public String getRunner_aname() {
        return runner_aname;
    }

    public void setRunner_aname(String runner_aname) {
        this.runner_aname = runner_aname;
    }

    public String getRunner_birthdate() {
        return runner_birthdate;
    }

    public void setRunner_birthdate(String runner_birthdate) {
        this.runner_birthdate = runner_birthdate;
    }

    public String getRunner_createdby() {
        return runner_createdby;
    }

    public void setRunner_createdby(String runner_createdby) {
        this.runner_createdby = runner_createdby;
    }

    public String getRunner_createddate() {
        return runner_createddate;
    }

    public void setRunner_createddate(String runner_createddate) {
        this.runner_createddate = runner_createddate;
    }

    public String getRunner_ename() {
        return runner_ename;
    }

    public void setRunner_ename(String runner_ename) {
        this.runner_ename = runner_ename;
    }

    public String getRunner_id() {
        return runner_id;
    }

    public void setRunner_id(String runner_id) {
        this.runner_id = runner_id;
    }

    public String getRunner_lic() {
        return runner_lic;
    }

    public void setRunner_lic(String runner_lic) {
        this.runner_lic = runner_lic;
    }

    public String getRunner_national() {
        return runner_national;
    }

    public void setRunner_national(String runner_national) {
        this.runner_national = runner_national;
    }

    public String getRunner_own() {
        return runner_own;
    }

    public void setRunner_own(String runner_own) {
        this.runner_own = runner_own;
    }

    public String getRunner_pwd() {
        return runner_pwd;
    }

    public void setRunner_pwd(String runner_pwd) {
        this.runner_pwd = runner_pwd;
    }

    public String getRunner_rate() {
        return runner_rate;
    }

    public void setRunner_rate(String runner_rate) {
        this.runner_rate = runner_rate;
    }

    public String getRunner_status() {
        return runner_status;
    }

    public void setRunner_status(String runner_status) {
        this.runner_status = runner_status;
    }

    public String getRunner_updatedby() {
        return runner_updatedby;
    }

    public void setRunner_updatedby(String runner_updatedby) {
        this.runner_updatedby = runner_updatedby;
    }

    public String getRunner_updateddate() {
        return runner_updateddate;
    }

    public void setRunner_updateddate(String runner_updateddate) {
        this.runner_updateddate = runner_updateddate;
    }

    public String getRunner_username() {
        return runner_username;
    }

    public void setRunner_username(String runner_username) {
        this.runner_username = runner_username;
    }

    public String getRunner_work() {
        return runner_work;
    }

    public void setRunner_work(String runner_work) {
        this.runner_work = runner_work;
    }

    public Runner(String runner_address, String runner_aname, String runner_birthdate, String runner_createdby, String runner_createddate, String runner_ename, String runner_id, String runner_lic, String runner_national, String runner_own, String runner_pwd, String runner_rate, String runner_status, String runner_updatedby, String runner_updateddate, String runner_username, String runner_work) {
        this.runner_address = runner_address;
        this.runner_aname = runner_aname;
        this.runner_birthdate = runner_birthdate;
        this.runner_createdby = runner_createdby;
        this.runner_createddate = runner_createddate;
        this.runner_ename = runner_ename;
        this.runner_id = runner_id;
        this.runner_lic = runner_lic;
        this.runner_national = runner_national;
        this.runner_own = runner_own;
        this.runner_pwd = runner_pwd;
        this.runner_rate = runner_rate;
        this.runner_status = runner_status;
        this.runner_updatedby = runner_updatedby;
        this.runner_updateddate = runner_updateddate;
        this.runner_username = runner_username;
        this.runner_work = runner_work;
    }

}
