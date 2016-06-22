/*
 * Created on Apr 9, 2004
 */
package ca.gc.hc.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import ca.gc.hc.model.AHFS;
import ca.gc.hc.model.ATC;
import ca.gc.hc.model.ActiveIngredients;
import ca.gc.hc.model.Company;
import ca.gc.hc.model.DrugProduct;
import ca.gc.hc.model.DrugStatus;
import ca.gc.hc.model.Form;
import ca.gc.hc.model.ProductMonograph;
import ca.gc.hc.model.Route;
import ca.gc.hc.model.Schedule;
import ca.gc.hc.model.Veterinary;
import ca.gc.hc.util.ApplicationGlobals;

/**
 * Drug Product Detail Info Bean
 */
public class DrugBean extends BaseBean implements Serializable
{
	/*
	 * The String Cryptor instance
	 */
	//private static CryptoUtil sc = new CryptoUtil();

	private String companyId;

	private DrugProduct drugProduct = new DrugProduct();

	private List activeIngredientList = new ArrayList();

	private List ahfsList = new ArrayList();

	private ATC atcVO = new ATC();

	private Company companyVO = new Company();

	private ProductMonograph pmVO = new ProductMonograph();

	private List formList = new ArrayList();
	
	private List packagingList = new ArrayList();

	private List<Route> routeList = new ArrayList<Route>();

	private List scheduleList = new ArrayList();

	private DrugStatus statusVO = new DrugStatus();

	private String statusAbbreviation;

	private List vetSpecies;
	
	private String distinctVetSpecies;

	/**
	 * @return
	 */
	public List getActiveIngredientList()
	{
		return activeIngredientList;
	}
	
	public String getIngredientLangOfPart(int index) {
		ActiveIngredients ingred = (ActiveIngredients) activeIngredientList.get(index); 
		
		if(ingred != null) {
			return super.getLanguageOfPart(ingred.getIngredientE(), ingred.getIngredientF());
		}else{
			return new String("");
		}
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
	public String getAiStrengthAndDosageLangOfPart(int index) {
		ActiveIngredients ingred = (ActiveIngredients) activeIngredientList.get(index);

		if(ingred != null) {
			String strengthUnitLangOfPart = super.getLanguageOfPart(ingred.getStrengthUnitE(), ingred.getStrengthUnitF());
			String dosageUnitLangOfPart = super.getLanguageOfPart(ingred.getDosageUnitE(), ingred.getDosageUnitF());
			return strengthUnitLangOfPart + dosageUnitLangOfPart;
		}else{
			return new String("");
		}
	}
	
	public String getAiStrengthLangOfPart(int index) {
		ActiveIngredients ingred = (ActiveIngredients) activeIngredientList.get(index);
		
		if(ingred != null) {
			return super.getLanguageOfPart(ingred.getStrengthUnitE(), ingred.getStrengthUnitF());
		}else{
			return new String("");
		}		
	}
	
	public String getAiDosageLangOfPart(int index) {
		ActiveIngredients ingred = (ActiveIngredients) activeIngredientList.get(index);
		
		if(ingred != null) {
			return super.getLanguageOfPart(ingred.getDosageUnitE(), ingred.getDosageUnitF());
		}else{
			return new String("");
		}		
	}

	/**
	 * @return
	 */
	public List getAhfsList()
	{
		return ahfsList;
	}

	/**
	 * @return
	 */
	public ATC getAtcVO()
	{
		return atcVO;
	}

	/**
	 * @return
	 */
	public Company getCompanyVO()
	{
		return companyVO;
	}

	/**
	 * @return
	 */
	public DrugProduct getDrugProduct()
	{
		return drugProduct;
	}

	/**
	 * @return
	 */
	public List getFormList()
	{
		return formList;
	}

	/**
	 * @return
	 */
	public List getPackagingList()
	{
		return packagingList;
	}

	/**
	 * @return
	 */
	public List<Route> getRouteList()
	{
		return routeList;
	}

	/**
	 * @return
	 */
	public List getScheduleList()
	{
		return scheduleList;
	}

	/**
	 * @return
	 */
	public DrugStatus getStatusVO()
	{
		return statusVO;
	}

	/**
	 * @param list
	 */
	public void setActiveIngredientList(List list)
	{
		activeIngredientList = list;
	}

	/**
	 * @param list
	 */
	public void setAhfsList(List list)
	{
		ahfsList = list;
	}

	/**
	 * @param atc
	 */
	public void setAtcVO(ATC atc)
	{
		atcVO = atc;
	}

	/**
	 * @param company
	 */
	public void setCompanyVO(Company company)
	{
		companyVO = company;
	}

	/**
	 * @param product
	 */
	public void setDrugProduct(DrugProduct product)
	{
		//companyId = sc.encrypt(product.getCompanyCode().toString());
		companyId = product.getCompanyCode().toString();
		drugProduct = product;
	}

	/**
	 * @param list
	 */
	public void setFormList(List list)
	{
		formList = list;
	}

	/**
	 * @param list
	 */
	public void setPackagingList(List list)
	{
		packagingList = list;
	}

	/**
	 * @param list
	 */
	public void setRouteList(List<Route> list)
	{
		routeList = list;
	}

	/**
	 * @param list
	 */
	public void setScheduleList(List list)
	{
		scheduleList = list;
	}

	/**
	 * @param status
	 */
	public void setStatusVO(DrugStatus status)
	{
		statusVO = status;
	}

	/**
	 * @return
	 */
	public String getCompanyId()
	{
		return companyId;
	}

	/**
	 * @return Well formated object content
	 */	
	public String toString()
	{
		return new ToStringBuilder(this)
		.append("Drug", getDrugProduct())
		.append("Active Ingredients", getActiveIngredientList().toArray())
		.append("AHFS", getAhfsList().toArray())
		.append("ATC", getAtcVO())
		.append("Company", getCompanyVO())
		.append("Dosage Forms", getFormList().toArray())
		.append("Packaging", getPackagingList().toArray())
		.append("Route of Admin", getRouteList().toArray())
		.append("Schedule", getScheduleList().toArray())
		.append("Status", getStatusVO().toString())	
		.toString();
	}
	/**
	 * @return
	 */
	public String getStatusAbbreviation() {
		return statusAbbreviation;
	}

	/**
	 * @param string
	 */
	public void setStatusAbbreviation(String string) {
		statusAbbreviation = string;
	}

	/**
	 * @return
	 */
	public ProductMonograph getPmVO() {
		return pmVO;
	}

	/**
	 * @param monograph
	 */
	public void setPmVO(ProductMonograph monograph) {
		pmVO = monograph;
	}

	public List<Veterinary> getVetSpecies() {
		return vetSpecies;
	}

	public void setVetSpecies(List vetSpecies) {
		this.vetSpecies = vetSpecies;
	}

	/**
	 * @return A distinct series of veterinarian species separated by a space, as a String.
	 * Was used when sub-species existed in the database but were not displayed, to avoid duplication in the series
	 * Cannot be used if each value needs to be evaluated individually (and possibly tagges) for language of part (WGAC2)
	 * @author Sylvain Larivière 2009-12-01
	 */
	public String getDistinctVetSpecies() {
		String speciesList = "";
		String species = "";
		
		for (Iterator it = vetSpecies.iterator(); it.hasNext();) {
			Veterinary vetSpecies;
			vetSpecies = (Veterinary)it.next();
			species = vetSpecies.getVetSpecies();
			if (speciesList.indexOf(species) == -1) {
				speciesList += ", " + species;
			}
		}
		if (speciesList.length()>0) {
			speciesList= speciesList.substring(2); //remove initial comma
		}
		return speciesList;
	}
	
	public String getDistinctRoutes() {
		String routes = "";
		
		for (Route route : routeList) {
			if (routes.indexOf(route.getRouteOfAdministration()) == -1) { //do not add repeated values
				routes += ", " + route.getRouteOfAdministration();
			}
		}		
		return routes.length() > 0 ? routes.substring(2) : routes;
	}
	
	public String getBrandNameLangOfPart() {
		return super.getLanguageOfPart(drugProduct.getBrandNameE(), drugProduct.getBrandNameF());
	}
	
	public String getDescriptorLangOfPart() {
		return super.getLanguageOfPart(drugProduct.getDescriptorE(), drugProduct.getDescriptorF());
	}
	public String getStatusLangOfPart() {
		return super.getLanguageOfPart(statusVO.getStatusE(), statusVO.getStatusF());
	}
	public String getStreetNameLangOfPart() {
		return super.getLanguageOfPart(companyVO.getStreetNameE(), companyVO.getStreetNameF());
	}

	public String getProvinceLangOfPart() {
		return super.getLanguageOfPart(companyVO.getProvinceE(), companyVO.getProvinceF());
	}

	public String getCountryLangOfPart() {
		return super.getLanguageOfPart(companyVO.getCountryE(), companyVO.getCountryF());
	}
	public String getDrugClassLangOfPart() {
		return super.getLanguageOfPart(drugProduct.getDrugClassE(), drugProduct.getDrugClassF());
	}
	public String getVetSpeciesLangOfPart(int index) {
		Veterinary species = (Veterinary) vetSpecies.get(index);
		
		if(species != null) {
			return super.getLanguageOfPart(species.getVetSpeciesE(), species.getVetSpeciesF());
		}else{
			return new String("");
		}
	}
	
	public int getVetSpeciesCount() {
		return vetSpecies.size();		
	}
	public String getFormLangOfPart(int index) {
		Form form = (Form) formList.get(index);
		
		if (form != null) {
			return super.getLanguageOfPart(form.getPharmaceuticalFormE(), form.getPharmaceuticalFormF());
		}else{
			return new String("");
		}
	}
	public String getRouteLangOfPart(int index) {
		Route route = (Route) routeList.get(index);
		
		if( route != null) {
			return super.getLanguageOfPart(route.getRouteOfAdministrationE(), route.getRouteOfAdministrationF());
		}else{
			return new String("");
		}
	}
	
	public String getAhfsLangOfPart(int index) {
		AHFS ahfs = (AHFS) ahfsList.get(index);
		
		if(ahfs != null){
			return super.getLanguageOfPart(ahfs.getAhfsE(), ahfs.getAhfsF());
		}else{
			return new String("");
		}		
	}
	
	public String getScheduleLangOfPart(int index) {
		Schedule schedule = (Schedule) scheduleList.get(index);
		
		if(schedule != null){
			return super.getLanguageOfPart(schedule.getScheduleE(), schedule.getScheduleF());
		}else{
			return new String("");
		}
	}
	
	public String getAtcLangOfPart() {
		ATC atc = (ATC) atcVO;
		
		if(atc != null) {
			return super.getLanguageOfPart(atc.getAtcE(), atc.getAtcF());
		}else{
			return new String("");
		}
	}
	
	public int getPharmaceuticalFormCount() {
		return formList.size();
	}
	public int getRouteSpeciesCount() {
		return routeList.size();
	}
	public int getScheduleSpeciesCount() {
		return scheduleList.size();
	}
	public int getAhfsSpeciesCount() {
		return ahfsList.size();
	}
	
	public boolean getIsRadioPharmaceutical() {
		return this.drugProduct.getClassCode().equals(ApplicationGlobals.RADIOPHARMACEUTICAL_CLASS_CODE);
	}
	
	public boolean getIsApproved() {
		return this.statusVO.getExternalStatus().getExternalStatusId().equals(ApplicationGlobals.APPROVED_STATUS_CODE);
	}

	public Object getOriginalMarketDate() {
		String notApplicable = 
		isLanguageFrench() ? ApplicationGlobals.NOT_APPLICABLE_F : ApplicationGlobals.NOT_APPLICABLE_E;
		
		return getIsRadioPharmaceutical() ? notApplicable : statusVO.getOriginalMarketDate();
	}
}
