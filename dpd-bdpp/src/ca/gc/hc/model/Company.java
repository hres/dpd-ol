package ca.gc.hc.model;

import java.io.Serializable;

import ca.gc.hc.util.LocaleDependantObject;
import ca.gc.hc.util.StringsUtil;


/** @author Hibernate CodeGenerator */
public class Company extends LocaleDependantObject implements Serializable
{

  /** identifier field */
  private Long companyCode;

  /** nullable persistent field */
  private String mfrCode;

  /** nullable persistent field */
  private String companyName;

  /** nullable persistent field */
  private String companyType;

  /** nullable persistent field */
  private String suiteNumner;

  /** nullable persistent field */
  private String streetNameE;
  private String streetNameF;

  /** nullable persistent field */
  private String cityName;

  /** nullable persistent field */
  private String provinceE;
  private String provinceF;

  /** nullable persistent field */
  private String countryE;
  private String countryF;

  /** nullable persistent field */
  private String postalCode;

  /** nullable persistent field */
  private String postOfficeBox;

  /** full constructor */
  public Company(
    java.lang.Long companyCode,
    java.lang.String mfrCode,
    java.lang.String companyName,
    java.lang.String companyType,
    java.lang.String suiteNumner,
    java.lang.String streetNameE,
    java.lang.String streetNameF,
    java.lang.String cityName,
    java.lang.String provinceE,
    java.lang.String provinceF,
    java.lang.String countryE,
    java.lang.String countryF,
    java.lang.String postalCode,
    java.lang.String postOfficeBox)
  {
    this.companyCode = companyCode;
    this.mfrCode = mfrCode;
    this.companyName = companyName;
    this.companyType = companyType;
    this.suiteNumner = suiteNumner;
    this.streetNameE = streetNameE;
    this.streetNameF = streetNameF;
    this.cityName = cityName;
    this.provinceE = provinceE;
    this.provinceF = provinceF;
    this.countryE = countryE;
    this.countryF = countryF;
    this.postalCode = postalCode;
    this.postOfficeBox = postOfficeBox;
  }

  /** default constructor */
  public Company()
  {
  }

  public java.lang.Long getCompanyCode()
  {
    return this.companyCode;
  }

  public void setCompanyCode(java.lang.Long companyCode)
  {
    this.companyCode = companyCode;
  }

  public java.lang.String getMfrCode()
  {
    return this.mfrCode;
  }

  public void setMfrCode(java.lang.String mfrCode)
  {
    this.mfrCode = mfrCode;
  }

  public java.lang.String getCompanyName()
  {
    return this.companyName;
  }

  public void setCompanyName(java.lang.String companyName)
  {
    this.companyName = companyName;
  }

  public java.lang.String getCompanyType()
  {
    return this.companyType;
  }

  public void setCompanyType(java.lang.String companyType)
  {
    this.companyType = companyType;
  }

  public java.lang.String getSuiteNumner()
  {
    return this.suiteNumner;
  }

  public void setSuiteNumner(java.lang.String suiteNumner)
  {
    this.suiteNumner = suiteNumner;
  }

  public java.lang.String getStreetNameE()
  {
    return this.streetNameE;
  }

  public void setStreetNameE(java.lang.String streetName)
  {
    this.streetNameE = streetName;
  }

  public java.lang.String getCityName()
  {
    return this.cityName;
  }

  public void setCityName(java.lang.String cityName)
  {
    this.cityName = cityName;
  }

  public java.lang.String getProvinceE()
  {
    return this.provinceE;
  }

  public void setProvinceE(java.lang.String province)
  {
    this.provinceE = province;
  }

  public java.lang.String getCountryE()
  {
    return this.countryE;
  }

  public void setCountryE(java.lang.String country)
  {
    this.countryE = country;
  }

  public java.lang.String getPostalCode()
  {
    return this.postalCode;
  }

  public void setPostalCode(java.lang.String postalCode)
  {
    this.postalCode = postalCode;
  }

  public java.lang.String getPostOfficeBox()
  {
    return this.postOfficeBox;
  }

  public void setPostOfficeBox(java.lang.String postOfficeBox)
  {
    this.postOfficeBox = postOfficeBox;
  }

public String getStreetNameF() {
	return streetNameF;
}
public String getStreetName() {
	return isLanguageFrench() ? StringsUtil.substituteIfNull(streetNameF, streetNameE) : streetNameE;
}

public void setStreetNameF(String streetNameF) {
	this.streetNameF = streetNameF;
}

public String getProvinceF() {
	return provinceF;
}

public String getProvince() {
	return isLanguageFrench() ? StringsUtil.substituteIfNull(provinceF, provinceE) : provinceE;
}

public void setProvinceF(String provinceF) {
	this.provinceF = provinceF;
}

public String getCountryF() {
	return countryF;
}

public String getCountry() {
	return isLanguageFrench() ? StringsUtil.substituteIfNull(countryF, countryE) : countryE;
}

public void setCountryF(String countryF) {
	this.countryF = countryF;
}


}
