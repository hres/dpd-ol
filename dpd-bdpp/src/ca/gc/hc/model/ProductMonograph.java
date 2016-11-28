/**
 * Name:  	ProductMonograph
 * Purpose: VO to map to the table WQRY_PM_DRUG
 * Date: 	October 2006
 * Author: 	Diane Beauvais
 */
package ca.gc.hc.model;

import java.sql.Date;
import ca.gc.hc.util.LocaleDependantObject;

public class ProductMonograph  extends LocaleDependantObject {
	
	/** identifier field */
	private Long drugCode;
	private int pmNumber;
	private int pmVersionNumber;
	private String pmControlNumber = "";
	private Date pmDate;
	private String pmEnglishFName = "";
	private String pmFrenchFName = "";


	
	/**
	 * @return
	 */
	public Long getDrugCode() {
		return drugCode;
	}



	/**
	 * @return
	 */
	public String getPmEnglishFName() {
		return pmEnglishFName + ".PDF";
	}

	/**
	 * @return
	 */
	public String getPmFrenchFName() {
		return pmFrenchFName + ".PDF";
	}

	/**
	 * @return
	 */
	public int getPmNumber() {
		return pmNumber;
	}

	/**
	 * @return
	 */
	public int getPmVersionNumber() {
		return pmVersionNumber;
	}

	

	/**
	 * @param long1
	 */
	public void setDrugCode(Long long1) {
		drugCode = long1;
	}

	/**
	 * @param string
	 */
	public void setPmEnglishFName(String string) {
		pmEnglishFName = string;
	}

	/**
	 * @param string
	 */
	public void setPmFrenchFName(String string) {
		pmFrenchFName = string;
	}
	
	/***************************************************************************
	 * Gets the name of the product monograph in the language appropriate for the Locale.
	 * @return the locale-specific name of the product monograph.
	 */
	public String getPmName() {
		if (isLanguageFrench()) {
			return getPmFrenchFName();
		}
			
		return getPmEnglishFName();
	}


	/**
	 * @param i
	 */
	public void setPmNumber(int i) {
		pmNumber = i;
	}

	/**
	 * @param i
	 */
	public void setPmVersionNumber(int i) {
		pmVersionNumber = i;
	}

	/**
	 * @return
	 */
	public String getPmControlNumber() {
		return pmControlNumber;
	}

	/**
	 * @return
	 */
	public Date getPmDate() {
		return pmDate;
	}

	/**
	 * @param string
	 */
	public void setPmControlNumber(String string) {
		pmControlNumber = string;
	}

	/**
	 * @param date
	 */
	public void setPmDate(Date date) {
		pmDate = date;
	}

}


