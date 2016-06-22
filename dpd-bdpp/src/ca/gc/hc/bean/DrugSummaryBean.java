/*
 * Created on Apr 18, 2004
 *
 */
package ca.gc.hc.bean;

import java.io.Serializable;

import net.quarksys.common.framework.util.CryptoUtil;

import org.apache.commons.lang.builder.ToStringBuilder;

import ca.gc.hc.model.ActiveIngredients;
import ca.gc.hc.model.DrugProduct;
import ca.gc.hc.model.DrugStatus;
import ca.gc.hc.model.Form;
import ca.gc.hc.model.Route;
import ca.gc.hc.model.Schedule;


/**
 * Java Bean class to hold the Drug Product summary information.
 * Updated SL/2009-10-01 to include first AI, route, dosage, and schedule
 */
public class DrugSummaryBean extends BaseBean implements Serializable
{
 
	/*
	 * The String Cryptor instance
	 */
	private static CryptoUtil sc = new CryptoUtil();
	/*
	 * The encryptyed drug code
	 */
	private String id;

	private DrugProduct drug;
//	private Long drugCode;
//	private String din;
//	private String brandName;
//	private Long numberOfAis;
//	private String drugClass;
	private String companyName;
	private DrugStatus status;
	private String pm;
	private String pmE;
	private String pmF;
	private ActiveIngredients firstAI;	//SL/2009-10-01: the first active ingredient (alphabetically) used
	private String AiStrengthAndDosageText;
//	private String firstAIName;
	private Route route;
	private Form  dosage;
	private Schedule schedule;

  public DrugSummaryBean(DrugProduct vo, String company, DrugStatus status, 
		  String pmE, String pmF)
  {
	//this.id = sc.encrypt(vo.getDrugCode().toString());
	this.id = vo.getDrugCode().toString();
    this.drug = vo;
    this.companyName = company;
    this.status = status;
    this.pmE = pmE;
    this.pmF = pmF;    
  }
 
  /** default constructor */
  public DrugSummaryBean()
  {
  }
  
  /*
   * Partial constructor excluding route and dosage form. Drug products are queried without these two fields
   * in order to avoid duplicates in the results summary. 
   */
  public DrugSummaryBean(DrugProduct vo, String company, DrugStatus status, String pmE, String pmF, 
			Schedule schedule, ActiveIngredients firstAI)
	{
		//this.id = sc.encrypt(vo.getDrugCode().toString());
		this.id = vo.getDrugCode().toString();
		this.drug= vo;
		this.companyName = company;
		this.status = status;
		this.pmE = pmE;
		this.pmF = pmF;
		this.schedule = schedule;  
		this.firstAI= firstAI;
//		this.firstAIName= firstAI.getfirstAIName();
//		this.AiStrengthAndDosageText= firstAI.getAiStrengthAndDosageText();
	}

	public String getBrandName() {
		return drug.getBrandName();
	}
	
	public String getBrandNameLangOfPart() {
		return super.getLanguageOfPart(drug.getBrandNameE(), drug.getBrandNameF());
	}
	
	public String getDrugClass() {
		 return drug.getDrugClass();
	}
	
	public String getClassLangOfPart() {
		return super.getLanguageOfPart(drug.getDrugClassE(), drug.getDrugClassF());
	}

	public Long getNumberOfAis()
	{
		return this.drug.getNumberOfAis();
	}

	/**
	 * @param string
	 */
	public void setBrandNameE(String string)
	{
		this.drug.setBrandNameE(string);
	}
	public void setBrandNameF(String string)
	{
		this.drug.setBrandNameF(string);
	}

	/**
	 * @param string
	 */
	public void setCompanyName(String string)
	{
		companyName = string;
	}

	/**
	 * @param string
	 */
	public void setDin(String string)
	{
		this.drug.setDrugIdentificationNumber(string);
	}

	/**
	 * @param string
	 */
	public void setDrugClassE(String string)
	{
		this.drug.setDrugClassE(string);
	}
	
	public void setDrugClassF(String string)
	{
		this.drug.setDrugClassF(string);
	}

	/**
	 * @param long1
	 */
	public void setNumberOfAis(Long long1)
	{
		this.drug.setNumberOfAis(long1);
	}

	/**
	 * @return
	 */
	public Long getDrugCode()
	{
		return this.drug.getDrugCode();
	}

	/**
	 * @param string
	 */
//	public void setDrugCode(Long string)
//	{
//		//this.id = sc.encrypt(string.toString());
//		this.id = string.toString();
//		drugCode = string;
//	}

	/**
	 * @return
	 */
	public String getId()
	{
		return id;
	}
	
	public Long getStatusID() {
		return status.getStatusID();
	}
	

	/**
	 * @return
	 */
	public String getStatus()
	{
		return status.getStatus();
	}

	/**
	 * @param string
	 */
	public void setStatus(DrugStatus status)
	{
		this.status = status;
	}

	/**
	 * @return Well formated object content
	 */
	public String toString()
	{
		return new ToStringBuilder(this)
		.append("drugCode", getDrugCode())
		.append("din", getDin())
		.append("companyName", getCompanyName())
		.append("brandName", getBrandName())
		.append("numberOfAis", getNumberOfAis())
		.append("drugClass", getDrugClass())
		.append("status", getStatus())
		.append("route", getRoute())
		.append("form",getDosage())
		.append("schedule", getSchedule())    
		.toString();
	}




	public String getDin() {
		return this.drug.getDrugIdentificationNumber();
	}

	/***************************************************************************
	 * Gets the name of the product monograph in the language appropriate for the Locale.
	 * @return the locale-specific name of the product monograph.
	 */
	public String getPm() {
		if (this.drug.isLanguageFrench()) {
			return this.pmF;
		}else{
			return this.pmE;
		}
	}

	/**
	 * @return
	 */
	public String getPmE() {
		return pmE;
	}

	/**
	 * @return
	 */
	public String getPmF() {
		return pmF;
	}

	/**
	 * @param string
	 */
	public void setPmE(String string) {
		pmE = string;
	}

	/**
	 * @param string
	 */
	public void setPmF(String string) {
		pmF = string;
	}
	public String getDosage() {
		return dosage.getPharmaceuticalForm();
	}
	public void setDosage(Form dosage) {
		this.dosage = dosage;
	}
	
	public ActiveIngredients getfirstAI() {
		return this.firstAI;
	}
	
	public void setfirstAI(ActiveIngredients firstAI) {
		this.firstAI = firstAI;
	}
	public String getRoute() {
		return route.getRouteOfAdministration();
	}
	public void setRoute(Route route) {
		this.route = route;
	}
	public String getSchedule() {
		return schedule.getSchedule();
	}
	
	public String getScheduleLangOfPart() {
		return super.getLanguageOfPart(schedule.getScheduleE(), schedule.getScheduleF());
	}
	
	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public DrugProduct getDrug() {
		return drug;
	}

	public void setDrug(DrugProduct drug) {
		this.drug = drug;
	}

	public String getCompanyName() {
		return companyName;
	}
	
	public String getFirstAIName() {
		return this.firstAI.getFirstAIName();
	}
	
	public String getFirstAILangOfPart() {
		return super.getLanguageOfPart(firstAI.getIngredientE(), firstAI.getIngredientF());
	}

	public String getAiStrengthAndDosageText(){		
		return this.firstAI.getAiStrengthAndDosageText();
	}
	
	/**
	 * @param index An int corresponding to an index in the current List of ActiveIngredients 
	 * @return Either an empty string if the related bilingual properties actually exist in both official
	 * languages, or the ISO language code to use in a lang attribute where the property in the requested
	 * language is missing, and its equivalent in the other official language is being returned instead. <br/>
	 * <strong>Note:</strong> Normally, both languages are expected to be present. If either is missing, 
	 * the caller is responsible for getting the individual lang attribute(s) if required using
	 * getAiStrengthLangOfPart and getAiDosageLangOfPart.
	 * @see getAiStrengthLangOfPart
	 * @see getAiDosageLangOfPart
	 * @author Sylvain Larivière 2012-09-19
	 */
	public String getAiStrengthAndDosageLangOfPart() {
		String strengthUnitLangOfPart = super.getLanguageOfPart(firstAI.getStrengthUnitE(), firstAI.getStrengthUnitF());
		String dosageUnitLangOfPart = super.getLanguageOfPart(firstAI.getDosageUnitE(), firstAI.getDosageUnitF());
		
		return strengthUnitLangOfPart + dosageUnitLangOfPart;
	}
	
	public String getAiStrengthLangOfPart() {
		return super.getLanguageOfPart(firstAI.getStrengthUnitE(), firstAI.getStrengthUnitF());
	}
	
	public String getAiDosageLangOfPart() {
		return super.getLanguageOfPart(firstAI.getDosageUnitE(), firstAI.getDosageUnitF());
	}
	
	public String getAiDosageText() {
		return firstAI.getAiDosageText();
	}
	
	public String getStrengthUnit() {
		return firstAI.getStrengthUnit();
	}
	
	public String getDosageUnit() {
		return firstAI.getDosageUnit();
	}
	
	public String getDosageValue() {
		return firstAI.getDosageValue();
	}
	
	public String getAiStrengthText() {
		return firstAI.getAiStrengthText();
	}
	
	public String getStrength() {
		return firstAI.getStrength();
	}
	
}
