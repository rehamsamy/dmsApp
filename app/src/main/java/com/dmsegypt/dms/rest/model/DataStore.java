package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahmoud on 10/01/17.
 */

public class DataStore {

    @SerializedName("AppName")
    @Expose
    private String appname;

    @SerializedName("Auther")
    @Expose
    private String auther;

    @SerializedName("Last_version_description")
    @Expose
    private String lsat_version_description;

    @SerializedName("Locale")
    @Expose
    private String locale;

    @SerializedName("Package_name")
    @Expose
    private String package_name;


    @SerializedName("Publish_date")
    @Expose
    private String publish_date;

    @SerializedName("Publish_date_formated")
    @Expose
    private String publish_date_formated;

    @SerializedName("Status")
    @Expose
    private String status;

    @SerializedName("Version")
    @Expose
    private String version;

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getAuther() {
        return auther;
    }

    public void setAuther(String auther) {
        this.auther = auther;
    }

    public String getLsat_version_description() {
        return lsat_version_description;
    }

    public void setLsat_version_description(String lsat_version_description) {
        this.lsat_version_description = lsat_version_description;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(String publish_date) {
        this.publish_date = publish_date;
    }

    public String getPublish_date_formated() {
        return publish_date_formated;
    }

    public void setPublish_date_formated(String publish_date_formated) {
        this.publish_date_formated = publish_date_formated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "DataStore{" +
                "appname='" + appname + '\'' +
                ", auther='" + auther + '\'' +
                ", lsat_version_description='" + lsat_version_description + '\'' +
                ", locale='" + locale + '\'' +
                ", package_name='" + package_name + '\'' +
                ", publish_date='" + publish_date + '\'' +
                ", publish_date_formated='" + publish_date_formated + '\'' +
                ", status='" + status + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
