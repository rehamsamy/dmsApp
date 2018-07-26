
package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserComment {


    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("lastModified")
    @Expose
    private LastModified lastModified;
    @SerializedName("starRating")
    @Expose
    private Integer starRating;
    @SerializedName("reviewerLanguage")
    @Expose
    private String reviewerLanguage;
    @SerializedName("device")
    @Expose
    private String device;
    @SerializedName("androidOsVersion")
    @Expose
    private Integer androidOsVersion;
    @SerializedName("thumbsUpCount")
    @Expose
    private Integer thumbsUpCount;
    @SerializedName("thumbsDownCount")
    @Expose
    private Integer thumbsDownCount;
    @SerializedName("deviceMetadata")
    @Expose
    private DeviceMetadata deviceMetadata;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LastModified getLastModified() {
        return lastModified;
    }

    public void setLastModified(LastModified lastModified) {
        this.lastModified = lastModified;
    }

    public Integer getStarRating() {
        return starRating;
    }

    public void setStarRating(Integer starRating) {
        this.starRating = starRating;
    }

    public String getReviewerLanguage() {
        return reviewerLanguage;
    }

    public void setReviewerLanguage(String reviewerLanguage) {
        this.reviewerLanguage = reviewerLanguage;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Integer getAndroidOsVersion() {
        return androidOsVersion;
    }

    public void setAndroidOsVersion(Integer androidOsVersion) {
        this.androidOsVersion = androidOsVersion;
    }

    public Integer getThumbsUpCount() {
        return thumbsUpCount;
    }

    public void setThumbsUpCount(Integer thumbsUpCount) {
        this.thumbsUpCount = thumbsUpCount;
    }

    public Integer getThumbsDownCount() {
        return thumbsDownCount;
    }

    public void setThumbsDownCount(Integer thumbsDownCount) {
        this.thumbsDownCount = thumbsDownCount;
    }

    public DeviceMetadata getDeviceMetadata() {
        return deviceMetadata;
    }

    public void setDeviceMetadata(DeviceMetadata deviceMetadata) {
        this.deviceMetadata = deviceMetadata;
    }

}
