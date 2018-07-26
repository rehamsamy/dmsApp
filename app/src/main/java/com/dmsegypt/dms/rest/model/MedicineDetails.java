package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahmoud on 10/06/17.
 */

public class MedicineDetails {
    @SerializedName("Med_name")
    @Expose
    private String med_name;

    @SerializedName("Dosage_form")
    @Expose
    private String dosage_form;

    @SerializedName("Dos_dur")
    @Expose
    private String dos_dur;

    @SerializedName("Active")
    @Expose
    private String active;

    @SerializedName("Med_code")
    @Expose
    private String med_code;

    @SerializedName("Med_duration")
    @Expose
    private String med_duration;

    @SerializedName("Excess")
    @Expose
    private String excess;

    @SerializedName("Num_of_unit")
    @Expose
    private String num_of_unit;

    @SerializedName("Unit_num")
    @Expose
    private String unit_num;


    @SerializedName("Pack_size")
    @Expose
    private String pack_size;


    public MedicineDetails() {
    }

    public MedicineDetails(String med_name, String dosage_form, String dos_dur, String active, String med_code, String med_duration, String excess, String num_of_unit, String unit_num, String pack_size) {
        this.med_name = med_name;
        this.dosage_form = dosage_form;
        this.dos_dur = dos_dur;
        this.active = active;
        this.med_code = med_code;
        this.med_duration = med_duration;
        this.excess = excess;
        this.num_of_unit = num_of_unit;
        this.unit_num = unit_num;
        this.pack_size = pack_size;
    }

    public String getMed_name() {
        return med_name;
    }

    public void setMed_name(String med_name) {
        this.med_name = med_name;
    }

    public String getDosage_form() {
        return dosage_form;
    }

    public void setDosage_form(String dosage_form) {
        this.dosage_form = dosage_form;
    }

    public String getDos_dur() {
        return dos_dur;
    }

    public void setDos_dur(String dos_dur) {
        this.dos_dur = dos_dur;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getMed_code() {
        return med_code;
    }

    public void setMed_code(String med_code) {
        this.med_code = med_code;
    }

    public String getMed_duration() {
        return med_duration;
    }

    public void setMed_duration(String med_duration) {
        this.med_duration = med_duration;
    }

    public String getExcess() {
        return excess;
    }

    public void setExcess(String excess) {
        this.excess = excess;
    }

    public String getNum_of_unit() {
        return num_of_unit;
    }

    public void setNum_of_unit(String num_of_unit) {
        this.num_of_unit = num_of_unit;
    }

    public String getUnit_num() {
        return unit_num;
    }

    public void setUnit_num(String unit_num) {
        this.unit_num = unit_num;
    }

    public String getPack_size() {
        return pack_size;
    }

    public void setPack_size(String pack_size) {
        this.pack_size = pack_size;
    }


    @Override
    public String toString() {
        return "MedicineDetails{" +
                "med_name='" + med_name + '\'' +
                ", dosage_form='" + dosage_form + '\'' +
                ", dos_dur='" + dos_dur + '\'' +
                ", active='" + active + '\'' +
                ", med_code='" + med_code + '\'' +
                ", med_duration='" + med_duration + '\'' +
                ", excess='" + excess + '\'' +
                ", num_of_unit='" + num_of_unit + '\'' +
                ", unit_num='" + unit_num + '\'' +
                ", pack_size='" + pack_size + '\'' +
                '}';
    }


}
