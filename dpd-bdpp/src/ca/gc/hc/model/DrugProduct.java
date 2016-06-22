package ca.gc.hc.model;
import java.io.Serializable;

import ca.gc.hc.util.ApplicationGlobals;
import ca.gc.hc.util.LocaleDependantObject;
import ca.gc.hc.util.StringsUtil;

/** @author Hibernate CodeGenerator */
public class DrugProduct extends LocaleDependantObject implements Serializable {

	/** identifier field */
	private Long drugCode;

	/** persistent field */
	private String brandNameE;
	private String brandNameF;

	/** persistent field */
	private String drugIdentificationNumber;

	/** persistent field */
	private Long companyCode;

	/** nullable persistent field */
	private String drugClassE;
	private String drugClassF;
	private Long classCode;

	/** persistent field */
	private Long numberOfAis;

	/** nullable persistent field */
	private String aiGroupNo;

	private String descriptorE;
	private String descriptorF;

	/** full constructor */
	public DrugProduct(
			java.lang.String brandNameE,
			java.lang.String brandNameF,
			java.lang.String drugIdentificationNumber,
			java.lang.Long companyCode,
			java.lang.String drugClassE,
			java.lang.String drugClassF,
			java.lang.Long numberOfAis,
			java.lang.String aiGroupNo, 
			java.lang.String species, 
			java.lang.String subSpecies, 
			String descriptorE, 
			String descriptorF) {
		this.brandNameE = brandNameE;
		this.brandNameF = brandNameF;
		this.drugIdentificationNumber = drugIdentificationNumber;
		this.companyCode = companyCode;
		this.drugClassE = drugClassE;
		this.drugClassF = drugClassF;
		this.numberOfAis = numberOfAis;
		this.aiGroupNo = aiGroupNo;
		this.descriptorE = descriptorE;
		this.descriptorF = descriptorF;
	}

	/** default constructor */
	public DrugProduct() {
	}

	/** minimal constructor */
	public DrugProduct(
			java.lang.String brandNameE,java.lang.String brandNameF,
			java.lang.String drugIdentificationNumber,
			java.lang.Long companyCode,
			java.lang.Long numberOfAis) {
		this.brandNameE = brandNameE;
		this.brandNameF = brandNameF;
		this.drugIdentificationNumber = drugIdentificationNumber;
		this.companyCode = companyCode;
		this.numberOfAis = numberOfAis;
	}

	public java.lang.Long getDrugCode() {
		return this.drugCode;
	}

	public void setDrugCode(java.lang.Long drugCode) {
		this.drugCode = drugCode;
	}

	public java.lang.String getBrandName() {
		return StringsUtil.substituteIfNull(brandNameF, brandNameE, super.getUserLocale());
	}
	public java.lang.String getBrandNameE() {
		return brandNameE;
	}
	public java.lang.String getBrandNameF() {
		return brandNameF;
	}

	public void setBrandNameE(java.lang.String brandName) {
		this.brandNameE = brandName;
	}
	public void setBrandNameF(java.lang.String brandName) {
		this.brandNameF = brandName;
	}

	public java.lang.String getDrugIdentificationNumber() {
		if (classCode.equals(ApplicationGlobals.RADIOPHARMACEUTICAL_CLASS_CODE))  {
			return super.isLanguageFrench() ? ApplicationGlobals.NOT_APPLICABLE_F : ApplicationGlobals.NOT_APPLICABLE_E;
		}else {
			return this.drugIdentificationNumber;
		} 
	}

	public void setDrugIdentificationNumber(
			java.lang.String drugIdentificationNumber) {
		this.drugIdentificationNumber = drugIdentificationNumber;
	}

	public java.lang.Long getCompanyCode() {
		return this.companyCode;
	}

	public void setCompanyCode(java.lang.Long companyCode) {
		this.companyCode = companyCode;
	}

	public java.lang.String getDrugClass() {		
		return isLanguageFrench() ? StringsUtil.substituteIfNull(drugClassF, drugClassE) : drugClassE;
	}

	public java.lang.Long getNumberOfAis() {
		return this.numberOfAis;
	}

	public void setNumberOfAis(java.lang.Long numberOfAis) {
		this.numberOfAis = numberOfAis;
	}

	public java.lang.String getAiGroupNo() {
		return this.aiGroupNo;
	}

	public void setAiGroupNo(java.lang.String aiGroupNo) {
		this.aiGroupNo = aiGroupNo;
	}

	public String getDescriptor() {
		return isLanguageFrench() ? StringsUtil.substituteIfNull(descriptorF, descriptorE) : descriptorE;
	}

	public void setDescriptorE(String descriptor) {
		this.descriptorE = descriptor;
	}
	public void setDescriptorF(String descriptor) {
		this.descriptorF = descriptor;
	}

	public String getDescriptorE() {
		return descriptorE;
	}

	public String getDescriptorF() {
		return descriptorF;
	}

	public String getDrugClassE() {
		return drugClassE;
	}

	public void setDrugClassE(String drugClassE) {
		this.drugClassE = drugClassE;
	}

	public String getDrugClassF() {
		return drugClassF;
	}

	public void setDrugClassF(String drugClassF) {
		this.drugClassF = drugClassF;
	}

	public void setClassCode(Long classCode) {
		this.classCode = classCode;
	}

	public Long getClassCode() {
		return classCode;
	}

}
