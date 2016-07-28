/*
 * Created on Apr 9, 2004
 *
 */
package ca.gc.hc.bean;
import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import ca.gc.hc.util.ApplicationGlobals;
 

/**
 * 
 * DPD Online Query Seach Croteria Bean.
 *
 */
public class SearchCriteriaBean implements Serializable
{
	private Long drugCode;
	private Long companyCode;
	private String din;
	private String atc;
	private String companyName;
	private String statusCode;
	private String[] status;
	private String brandName;
	private String activeIngredient;
	private String aigNumber;
	private String[] route;
	private String[] dosage;	//dosage form (pharmaceutical form)
	private String[] schedule;
	private String vetSpecies;
	private String vetSubSpecies;   
	private String[] drugClass = null;

	/**
	 * @return Active Ingredient Group Number
	 */
	public String getAigNumber()
	{
		return this.aigNumber;
	}

	/**
	 * @return ATC Number
	 */
	public String getAtc()
	{
		return this.atc;
	}

	/**
	 * @return Active Ingredient Name
	 */
	public String getActiveIngredient()
	{
		return this.activeIngredient;
	}

	/**
	 * @return Product Brand Name
	 */
	public String getBrandName()
	{
		return this.brandName;
	}

	/**
	 * @return Company Name
	 */
	public String getCompanyName()
	{

		return this.companyName;
	}

	/**
	 * @return Drug Identification Number
	 */
	public String getDin()
	{
		return this.din;
	}

	/**
	 * @return
	 */
	public Long getDrugCode()
	{
		return drugCode;
	}

	/**
	 * @return Drug Status
	 */
	public String[] getStatus()
	{
		return this.status;
	}

	/**
	 * Set AIG Number
	 * @param string
	 */
	public void setAigNumber(String string)
	{
		if (string != null && string.length() > 0)
		{
			this.aigNumber = string.toUpperCase();
		}
		else
		{
			this.aigNumber = null;
		}
	}

	/**
	 * Set ATC Number
	 * @param string
	 */
	public void setAtc(String string)
	{
		if (string != null && string.length() > 0)
		{
			this.atc = string.toUpperCase();
		}
		else
		{
			this.atc = null;
		}
	}

	/**
	 * Set Active Ingredient Name
	 * @param string
	 */
	public void setActiveIngredient(String string)
	{
		if (string != null && string.length() > 0)
		{
			/*
				SL/2013-02-14: should display the way it was entered
				particularly now that search is case- and accent-insensitive
			*/
//			this.activeIngredient = string.toUpperCase();
			this.activeIngredient = string;
		}
		else
		{
			this.activeIngredient = null;
		}
	}

	/**
	 * Set Product Brand Name
	 * @param string
	 */
	public void setBrandName(String string)
	{
		if (string != null && string.length() > 0)
		{
			/*
			SL/2013-02-14: should display the way it was entered
			particularly now that search is case- and accent-insensitive
		*/
//			this.brandName = string.toUpperCase();
			this.brandName = string;
		}
		else
		{
			this.brandName = null;
		}
	}

	/**
	 * Set Company Name
	 * @param string
	 */
	public void setCompanyName(String string)
	{
		if (string != null && string.length() > 0)
		{
			/*
			SL/2013-02-14: should display the way it was entered
			particularly now that search is case- and accent-insensitive
		*/
//			this.companyName = string.toUpperCase();
			this.companyName = string;
		}
		else
		{
			this.companyName = null;
		}
	}

	/**
	 * Set Drug Identification Number
	 * @param string
	 */
	public void setDin(String string)
	{
		if (string != null && string.length() > 0)
		{
			this.din = string;
		}
		else
		{
			this.din = null;
		}
	}

	/**
	 * @param string
	 */
	public void setDrugCode(Long val)
	{
		drugCode = val;
	}

	/**
	 * Set Drug Marketing status
	 * @param string
	 */
	public void setStatus(String[] val)
	{
		this.status = val;
	}

	/**
	 * @return
	 */
	public Long getCompanyCode()
	{
		return companyCode;
	}

	/**
	 * @param long1
	 */
	public void setCompanyCode(Long long1)
	{
		companyCode = long1;
	}

	/**
	 * @return Well formated object content
	 */
	public String toString()
	{
		return new ToStringBuilder(this)
		.append("drugCode", getDrugCode())
		.append("din", getDin())
		.append("atc", getAtc())
		.append("companyCode", getCompanyCode())
		.append("companyName", getCompanyName())
		.append("status", status == null ? null : getStatus().toString())
		.append("brandName", getBrandName())
		.append("activeIngredient", getActiveIngredient())
		.append("aigNumber", getAigNumber())
		.append("route", route == null ? null : getRouteEnumeration())
		.append("dosage", dosage == null ? null : getDosageEnumeration())
		.append("schedule",  schedule == null ? null : getScheduleEnumeration())
		.append("vetSpecies", getVetSpecies())
		.append("vetSubSpecies", getVetSubSpecies())
		.append("drugClass",  drugClass == null ? null : getDrugClassEnumeration())
		.toString();
	}
	
	

	//updated for an array of values SL/2009-09-10
	public String[] getDosage() {
		return dosage;
	}

	//updated for an array of values SL/2009-09-10
	public void setDosage(String[] dosage) {
		this.dosage = dosage;
	}

	//updated for an array of values SL/2009-09-10
	public String[] getSchedule() {
		return schedule;
	}

	//updated for an array of values SL/2009-09-10
	public void setSchedule(String[] schedule) {
		this.schedule = schedule;
	}

	public String getVetSpecies() {
		return vetSpecies;
	}

	public void setVetSpecies(String string) {
		if (string != null && string.length() > 0)
		{
			this.vetSpecies = string.toUpperCase();
		}
		else
		{
			this.vetSpecies = null;
		}
	}

	public String getVetSubSpecies() {
		return vetSubSpecies;
	}

	public void setVetSubSpecies(String string) {
		if (string != null && string.length() > 0)
		{
			this.vetSubSpecies = string.toUpperCase();
		}
		else
		{
			this.vetSubSpecies = null;
		}
	}

	public String[] getRoute() {
		return route;
	}

	public void setRoute(String[] route) {
		this.route = route;
	}

	/**
	 * @author Sylvain Lariviere 2009-09-30
	 * @return All the routes of administration as a single String
	 */
	public String getRouteEnumeration() {
		return enumerateThisStringArray(getRoute());	  
	}
	
	/**
	 * @author Sylvain Lariviere 2009-09-30
	 * @param A String array
	 * @return A String containing all the passed array elements, separated by a comma and space, 
	 * or the selectAll label, if no individual item was selected
	 */
	private String enumerateThisStringArray(String[] anAra){
		String result= "";

		if (anAra != null && anAra.length > 0) {
			if (anAra.length == 1 && anAra[0].toString().equals("0")) {
				// no individual item was selected: just return the selectAll label
				result = ApplicationGlobals.instance().getSelectAllLabel();
			}else{
				for (int i=0; i<anAra.length; i++) {
					if(!"0".equals(anAra[i])) { //exclude value 0: it means "Select All" and was included in the selection
						result= result + anAra[i] + ", ";
					}
				}
				result = result.substring(0, result.length() - 2); //remove last comma
			}
		}else{
			result = null;
		}

		return result;
	}
	/**
	 * @author Sylvain Lariviere 2009-09-30
	 * @return All the dosage forms as a single String
	 */
	public String getDosageEnumeration() {
		return enumerateThisStringArray(getDosage());	  
	}
	
	/**
	 * @author Sylvain Lariviere 2009-09-30
	 * @return All the schedules as a single String
	 */
	public String getScheduleEnumeration() {
		return enumerateThisStringArray(getSchedule());	  
	}

	public String getStatusCode() {
		if (this.status != null && this.status.length > 0) {
			return this.status[0];
		}else{
			return this.statusCode;
		}
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = new String(statusCode);
	}

	public void setDrugClass(String[] drugClass) {
		this.drugClass = drugClass;
	}

	public String[] getDrugClass() {
		return drugClass;
	}
	
	/**
	 * @author Sylvain Lariviere 2014-10-20
	 * @return All the drug classes as a single String
	 */
	public String getDrugClassEnumeration() {
		return enumerateThisStringArray(getDrugClass());	  
	}
	
}
