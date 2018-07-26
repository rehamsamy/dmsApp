
package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceMetadata {
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("manufacturer")
    @Expose
    private String manufacturer;
    @SerializedName("deviceClass")
    @Expose
    private String deviceClass;
    @SerializedName("screenWidthPx")
    @Expose
    private Integer screenWidthPx;
    @SerializedName("screenHeightPx")
    @Expose
    private Integer screenHeightPx;
    @SerializedName("nativePlatform")
    @Expose
    private String nativePlatform;
    @SerializedName("screenDensityDpi")
    @Expose
    private Integer screenDensityDpi;
    @SerializedName("glEsVersion")
    @Expose
    private Integer glEsVersion;
    @SerializedName("cpuModel")
    @Expose
    private String cpuModel;
    @SerializedName("cpuMake")
    @Expose
    private String cpuMake;
    @SerializedName("ramMb")
    @Expose
    private Integer ramMb;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getDeviceClass() {
        return deviceClass;
    }

    public void setDeviceClass(String deviceClass) {
        this.deviceClass = deviceClass;
    }

    public Integer getScreenWidthPx() {
        return screenWidthPx;
    }

    public void setScreenWidthPx(Integer screenWidthPx) {
        this.screenWidthPx = screenWidthPx;
    }

    public Integer getScreenHeightPx() {
        return screenHeightPx;
    }

    public void setScreenHeightPx(Integer screenHeightPx) {
        this.screenHeightPx = screenHeightPx;
    }

    public String getNativePlatform() {
        return nativePlatform;
    }

    public void setNativePlatform(String nativePlatform) {
        this.nativePlatform = nativePlatform;
    }

    public Integer getScreenDensityDpi() {
        return screenDensityDpi;
    }

    public void setScreenDensityDpi(Integer screenDensityDpi) {
        this.screenDensityDpi = screenDensityDpi;
    }

    public Integer getGlEsVersion() {
        return glEsVersion;
    }

    public void setGlEsVersion(Integer glEsVersion) {
        this.glEsVersion = glEsVersion;
    }

    public String getCpuModel() {
        return cpuModel;
    }

    public void setCpuModel(String cpuModel) {
        this.cpuModel = cpuModel;
    }

    public String getCpuMake() {
        return cpuMake;
    }

    public void setCpuMake(String cpuMake) {
        this.cpuMake = cpuMake;
    }

    public Integer getRamMb() {
        return ramMb;
    }

    public void setRamMb(Integer ramMb) {
        this.ramMb = ramMb;
    }

}
