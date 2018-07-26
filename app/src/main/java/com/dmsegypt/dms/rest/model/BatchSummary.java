package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amr on 28/11/2017.
 */

public class BatchSummary {
    @SerializedName("Batch_no")
   String batch_no ;
    @SerializedName("Id_group")
    String id_group ;
    @SerializedName("Prv_no")
    String prv_no ;
    @SerializedName("Prv_name")
    String prv_name ;
    @SerializedName("System_amt")
    String system_amt ;
    @SerializedName("Check_amt")
    String check_amt ;
    @SerializedName("Check_status")
    String check_status ;
    @SerializedName("System_no")
    String system_no ;
    @SerializedName("Manual_no")
    String manual_no ;
    @SerializedName("M_vd")
    String m_vd ;
    @SerializedName("Fin_clm")
    String fin_clm ;
    @SerializedName("Mony_statment")
    String mony_statment ;
    @SerializedName("Date_statment")
    String date_statment ;
    @SerializedName("Serv_date_statment")
    String serv_date_statment ;
    @SerializedName("Notes")
    String notes;

 public String getBatch_no() {
  return batch_no;
 }

 public void setBatch_no(String batch_no) {
  this.batch_no = batch_no;
 }

 public String getId_group() {
  return id_group;
 }

 public void setId_group(String id_group) {
  this.id_group = id_group;
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

 public String getSystem_amt() {
  return system_amt;
 }

 public void setSystem_amt(String system_amt) {
  this.system_amt = system_amt;
 }

 public String getCheck_amt() {
  return check_amt;
 }

 public void setCheck_amt(String check_amt) {
  this.check_amt = check_amt;
 }

 public String getCheck_status() {
  return check_status;
 }

 public void setCheck_status(String check_status) {
  this.check_status = check_status;
 }

 public String getSystem_no() {
  return system_no;
 }

 public void setSystem_no(String system_no) {
  this.system_no = system_no;
 }

 public String getManual_no() {
  return manual_no;
 }

 public void setManual_no(String manual_no) {
  this.manual_no = manual_no;
 }

 public String getM_vd() {
  return m_vd;
 }

 public void setM_vd(String m_vd) {
  this.m_vd = m_vd;
 }

 public String getFin_clm() {
  return fin_clm;
 }

 public void setFin_clm(String fin_clm) {
  this.fin_clm = fin_clm;
 }

 public String getMony_statment() {
  return mony_statment;
 }

 public void setMony_statment(String mony_statment) {
  this.mony_statment = mony_statment;
 }

 public String getDate_statment() {
  return date_statment;
 }

 public void setDate_statment(String date_statment) {
  this.date_statment = date_statment;
 }

 public String getServ_date_statment() {
  return serv_date_statment;
 }

 public void setServ_date_statment(String serv_date_statment) {
  this.serv_date_statment = serv_date_statment;
 }

 public String getNotes() {
  return notes;
 }

 public void setNotes(String notes) {
  this.notes = notes;
 }
}
