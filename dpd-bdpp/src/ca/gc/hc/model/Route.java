package ca.gc.hc.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import ca.gc.hc.util.LocaleDependantObject;
import ca.gc.hc.util.StringsUtil;

/** @author Hibernate CodeGenerator */
public class Route extends LocaleDependantObject implements Serializable
{

  /** identifier field */
  private Long drugCode;

  /** nullable persistent field */
  private String routeOfAdministrationE;
  private String routeOfAdministrationF;

  /** full constructor */
  public Route(String routeOfAdministrationE, String routeOfAdministrationF) {
	  this.routeOfAdministrationE = routeOfAdministrationE;
	  this.routeOfAdministrationF = routeOfAdministrationF;
  }

  /** default constructor */
  public Route()
  {
  }

  public java.lang.Long getDrugCode()
  {
    return this.drugCode;
  }

  public void setDrugCode(java.lang.Long drugCode)
  {
    this.drugCode = drugCode;
  }

  public String getRouteOfAdministration() {
	return isLanguageFrench() ? StringsUtil.substituteIfNull(routeOfAdministrationF, routeOfAdministrationE) : routeOfAdministrationE;
  }
  
  public String toString()
  {
	return new ToStringBuilder(this)
	  .append("drugCode", getDrugCode())
	  .append("routeOfAdministrationE", getRouteOfAdministrationE())
	  .append("routeOfAdministrationF", getRouteOfAdministrationF())
	  .toString();
  }

  public boolean equals(Object other)
  {
	if (!(other instanceof Route))
	  return false;
	Route castOther = (Route) other;
	return new EqualsBuilder()
	  .append(this.getDrugCode(), castOther.getDrugCode())
	  .append(this.getRouteOfAdministrationE(), castOther.getRouteOfAdministrationE())
	  .append(this.getRouteOfAdministrationF(), castOther.getRouteOfAdministrationF())
	  .isEquals();
  }

  public int hashCode()
  {
	return new HashCodeBuilder()
	  .append(getDrugCode())
	  .append(getRouteOfAdministrationE())
	  .append(getRouteOfAdministrationF())
	  .toHashCode();
  }

public String getRouteOfAdministrationE() {
	return routeOfAdministrationE;
}

public void setRouteOfAdministrationE(String routeOfAdministrationE) {
	this.routeOfAdministrationE = routeOfAdministrationE;
}

public String getRouteOfAdministrationF() {
	return routeOfAdministrationF;
}

public void setRouteOfAdministrationF(String routeOfAdministrationF) {
	this.routeOfAdministrationF = routeOfAdministrationF;
}


}
