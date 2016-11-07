/**
 * Name:  	ProductLabel
 * Purpose: VO to map to the table WQRY_PRODUCT_LABEL
 * Date: 	October 2006
 * Author: 	Diane Beauvais
 */
package ca.gc.hc.model;

import java.io.Serializable;
import java.sql.Date;


public class ProductLabel implements Serializable{
	
	/** identifier field */
	private Long drugCode;
	private Date dateScanned;
	private String marketedLebelFname;
	private Integer marketedLabelNo;
	private Date apprvdLabelDate;
	private String apprvdLabelFname;
	
	public Long getDrugCode() {
		return drugCode;
	}
	public void setDrugCode(Long drugCode) {
		this.drugCode = drugCode;
	}
	public Date getDateScanned() {
		return dateScanned;
	}
	public void setDateScanned(Date dateScanned) {
		this.dateScanned = dateScanned;
	}
	public String getMarketedLebelFname() {
		if(marketedLebelFname == null)
		{
			return "";
		} else {
			return marketedLebelFname;	
		}
	}
	public void setMarketedLebelFname(String marketedLebelFname) {
		this.marketedLebelFname = marketedLebelFname;
	}
	
	public Date getApprvdLabelDate() {
		return apprvdLabelDate;
	}
	public void setApprvdLabelDate(Date apprvdLabelDate) {
		this.apprvdLabelDate = apprvdLabelDate;
	}
	public String getApprvdLabelFname() {
		if(apprvdLabelFname == null)
		{
			return "";
		} else {
			return apprvdLabelFname;	
		}
	}
	public void setApprvdLabelFname(String apprvdLabelFname) {
		this.apprvdLabelFname = apprvdLabelFname;
	}
	public Integer getMarketedLabelNo() {
		return marketedLabelNo;
	}
	public void setMarketedLabelNo(Integer marketedLabelNo) {
		this.marketedLabelNo = marketedLabelNo;
	}
}

