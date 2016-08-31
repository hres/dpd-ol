package ca.gc.hc.bean;

import java.io.Serializable;

public class JsonSummaryBean extends BaseBean implements Serializable {
	private static final long serialVersionUID = -8908533481917521984L;
	//properties match the column ID's of the DataTable
	private String status;
	private String din;
	private String company;
	private String brand;
	private String drugClass;
	private String pm;
	private String schedule;
	private String aiNum; //number of active ingredients
	private String majorAI;	//first ingredient when sorted by id
	private String AIStrength;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDin() {
		return din;
	}
	public void setDin(String din) {
		this.din = din;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getPm() {
		return pm;
	}
	public void setPm(String pm) {
		this.pm = pm;
	}
	public String getSchedule() {
		return schedule;
	}
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	public String getMajorAI() {
		return majorAI;
	}
	public void setMajorAI(String majorAI) {
		this.majorAI = majorAI;
	}
	public String getAIStrength() {
		return AIStrength;
	}
	public void setAIStrength(String aIStrength) {
		AIStrength = aIStrength;
	}
	public String getAiNum() {
		return aiNum;
	}
	public void setAiNum(String aiNum) {
		this.aiNum = aiNum;
	}
	public String getDrugClass() {
		return drugClass;
	}
	public void setDrugClass(String drugClass) {
		this.drugClass = drugClass;
	}
	
}
