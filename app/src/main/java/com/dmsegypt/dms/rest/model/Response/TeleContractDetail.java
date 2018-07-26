package com.dmsegypt.dms.rest.model.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeleContractDetail {

@SerializedName("category")
@Expose
private String category;
@SerializedName("AnnualCeiling")
@Expose
private String annualCeiling;
@SerializedName("SubscriberNum")
@Expose
private String subscriberNum;
@SerializedName("MedicalNetwork")
@Expose
private String medicalNetwork;
@SerializedName("Accommodation")
@Expose
private String accommodation;
@SerializedName("InpatientService")
@Expose
private String inpatientService;
@SerializedName("Consulation")
@Expose
private String consulation;
@SerializedName("InVestigations")
@Expose
private String inVestigations;
@SerializedName("Physiotherapy")
@Expose
private String physiotherapy;
@SerializedName("OutpatientMedications")
@Expose
private String outpatientMedications;
@SerializedName("NormalDelivery")
@Expose
private String normalDelivery;
@SerializedName("Section")
@Expose
private String section;
@SerializedName("LegalAbortion")
@Expose
private String legalAbortion;
@SerializedName("Basic")
@Expose
private String basic;
@SerializedName("RootCanal")
@Expose
private String rootCanal;
@SerializedName("Crowns")
@Expose
private String crowns;
@SerializedName("Optical")
@Expose
private String optical;
@SerializedName("PreExisting")
@Expose
private String preExisting;
@SerializedName("CriticalCases")
@Expose
private String criticalCases;

public String getCategory() {
return category;
}

public void setCategory(String category) {
this.category = category;
}

public String getAnnualCeiling() {
return annualCeiling;
}

public void setAnnualCeiling(String annualCeiling) {
this.annualCeiling = annualCeiling;
}

public String getSubscriberNum() {
return subscriberNum;
}

public void setSubscriberNum(String subscriberNum) {
this.subscriberNum = subscriberNum;
}

public String getMedicalNetwork() {
return medicalNetwork;
}

public void setMedicalNetwork(String medicalNetwork) {
this.medicalNetwork = medicalNetwork;
}

public String getAccommodation() {
return accommodation;
}

public void setAccommodation(String accommodation) {
this.accommodation = accommodation;
}

public String getInpatientService() {
return inpatientService;
}

public void setInpatientService(String inpatientService) {
this.inpatientService = inpatientService;
}

public String getConsulation() {
return consulation;
}

public void setConsulation(String consulation) {
this.consulation = consulation;
}

public String getInVestigations() {
return inVestigations;
}

public void setInVestigations(String inVestigations) {
this.inVestigations = inVestigations;
}

public String getPhysiotherapy() {
return physiotherapy;
}

public void setPhysiotherapy(String physiotherapy) {
this.physiotherapy = physiotherapy;
}

public String getOutpatientMedications() {
return outpatientMedications;
}

public void setOutpatientMedications(String outpatientMedications) {
this.outpatientMedications = outpatientMedications;
}

public String getNormalDelivery() {
return normalDelivery;
}

public void setNormalDelivery(String normalDelivery) {
this.normalDelivery = normalDelivery;
}

public String getSection() {
return section;
}

public void setSection(String section) {
this.section = section;
}

public String getLegalAbortion() {
return legalAbortion;
}

public void setLegalAbortion(String legalAbortion) {
this.legalAbortion = legalAbortion;
}

public String getBasic() {
return basic;
}

public void setBasic(String basic) {
this.basic = basic;
}

public String getRootCanal() {
return rootCanal;
}

public void setRootCanal(String rootCanal) {
this.rootCanal = rootCanal;
}

public String getCrowns() {
return crowns;
}

public void setCrowns(String crowns) {
this.crowns = crowns;
}

public String getOptical() {
return optical;
}

public void setOptical(String optical) {
this.optical = optical;
}

public String getPreExisting() {
return preExisting;
}

public void setPreExisting(String preExisting) {
this.preExisting = preExisting;
}

public String getCriticalCases() {
return criticalCases;
}

public void setCriticalCases(String criticalCases) {
this.criticalCases = criticalCases;
}

}