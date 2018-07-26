package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mahmoud on 25/12/17.
 */

public class Order {
    @SerializedName("Comp_address")
    private String comp_address;

    @SerializedName("Comp_area")
    private String comp_area;

    @SerializedName("Comp_cc")
    private String comp_cc;

    @SerializedName("Comp_gover")
    private String comp_gover;

    @SerializedName("Comp_id")
    private String comp_id;

    @SerializedName("Comp_name")
    private String comp_name;

    @SerializedName("Comp_person")
    private String comp_person;

    @SerializedName("Comp_phone1")
    private String comp_phone1;

    @SerializedName("Comp_phone2")
    private String comp_phone2;

    @SerializedName("Confirm_notes")
    private String confirm_notes;

    @SerializedName("Created_by")
    private String created_by;

    @SerializedName("Created_date")
    private String created_date;

    @SerializedName("Hold_time")
    private String hold_time;

    @SerializedName("Order_date")
    private String order_date;

    @SerializedName("Order_no")
    private String order_no;

    @SerializedName("Order_notes")
    private String order_notes;


    @SerializedName("Order_state")
    private String order_state;

    private boolean bar_visible;

    @SerializedName("Order_time")
    private String order_time;


    @SerializedName("Order_type")
    private String order_type;

    @SerializedName("Run_id")
    private String run_id;

    @SerializedName("Reasons")
    private List<OrderReason> reasons;

    @SerializedName("Status")
    private String status;

    @SerializedName("Updated_by")
    private String updated_by;

    @SerializedName("Updated_date")
    private String updated_date;
    @SerializedName("Vip")
    private String vip;


    public Order(String comp_address, String comp_area, String comp_cc, String comp_gover, String comp_id, String comp_name, String comp_person, String comp_phone1, String comp_phone2, String confirm_notes, String created_by, String created_date, String hold_time, String order_date, String order_no, String order_notes, String order_state, String order_time, String order_type, String run_id, List<OrderReason> reasons, String status, String updated_by, String updated_date,String vip) {
        this.comp_address = comp_address;
        this.comp_area = comp_area;
        this.comp_cc = comp_cc;
        this.comp_gover = comp_gover;
        this.comp_id = comp_id;
        this.comp_name = comp_name;
        this.comp_person = comp_person;
        this.comp_phone1 = comp_phone1;
        this.comp_phone2 = comp_phone2;
        this.confirm_notes = confirm_notes;
        this.created_by = created_by;
        this.created_date = created_date;
        this.hold_time = hold_time;
        this.order_date = order_date;
        this.order_no = order_no;
        this.order_notes = order_notes;
        this.order_state = order_state;
        this.order_time = order_time;
        this.order_type = order_type;
        this.run_id = run_id;
        this.reasons = reasons;
        this.status = status;
        this.updated_by = updated_by;
        this.updated_date = updated_date;
        this.vip=vip;

    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public boolean isBar_visible() {
        return bar_visible;
    }

    public void setBar_visible(boolean bar_visible) {
        this.bar_visible = bar_visible;
    }

    public String getOrder_notes() {
        return order_notes;
    }

    public void setOrder_notes(String order_notes) {
        this.order_notes = order_notes;
    }

    public String getComp_address() {
        return comp_address;
    }

    public void setComp_address(String comp_address) {
        this.comp_address = comp_address;
    }

    public String getComp_area() {
        return comp_area;
    }

    public void setComp_area(String comp_area) {
        this.comp_area = comp_area;
    }

    public String getComp_cc() {
        return comp_cc;
    }

    public void setComp_cc(String comp_cc) {
        this.comp_cc = comp_cc;
    }

    public String getComp_gover() {
        return comp_gover;
    }

    public void setComp_gover(String comp_gover) {
        this.comp_gover = comp_gover;
    }

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public String getComp_name() {
        return comp_name;
    }

    public void setComp_name(String comp_name) {
        this.comp_name = comp_name;
    }

    public String getComp_person() {
        return comp_person;
    }

    public void setComp_person(String comp_person) {
        this.comp_person = comp_person;
    }

    public String getComp_phone1() {
        return comp_phone1;
    }

    public void setComp_phone1(String comp_phone1) {
        this.comp_phone1 = comp_phone1;
    }

    public String getComp_phone2() {
        return comp_phone2;
    }

    public void setComp_phone2(String comp_phone2) {
        this.comp_phone2 = comp_phone2;
    }

    public String getConfirm_notes() {
        return confirm_notes;
    }

    public void setConfirm_notes(String confirm_notes) {
        this.confirm_notes = confirm_notes;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getHold_time() {
        return hold_time;
    }

    public void setHold_time(String hold_time) {
        this.hold_time = hold_time;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getOrder_state() {
        return order_state;
    }

    public void setOrder_state(String order_state) {
        this.order_state = order_state;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getRun_id() {
        return run_id;
    }

    public void setRun_id(String run_id) {
        this.run_id = run_id;
    }

    public List<OrderReason> getReasons() {
        return reasons;
    }

    public void setReasons(List<OrderReason> reasons) {
        this.reasons = reasons;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }
}
