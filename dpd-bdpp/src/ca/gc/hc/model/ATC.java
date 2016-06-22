package ca.gc.hc.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import ca.gc.hc.util.LocaleDependantObject;
import ca.gc.hc.util.StringsUtil;

/** @author Hibernate CodeGenerator */
public class ATC extends LocaleDependantObject implements Serializable
{

  /** identifier field */
  private Long drugCode;

  /** nullable persistent field */
  private String atcNumber;

  /** nullable persistent field */
  private String atcE;
  private String atcF;

  /** full constructor */
  public ATC(java.lang.String tcAtcNumber, java.lang.String tcAtcE, java.lang.String tcAtcF)
  {
    this.atcNumber = tcAtcNumber;
    this.atcE = tcAtcE;
    this.atcE = tcAtcF;
  }

  /** default constructor */
  public ATC()
  {
  }

  public java.lang.Long getDrugCode()
  {
    return this.drugCode;
  }

  public String getAtcE() {
	return atcE;
}

public void setAtcE(String atcE) {
	this.atcE = atcE;
}

public String getAtcF() {
	return atcF;
}

public void setAtcF(String atcF) {
	this.atcF = atcF;
}

public void setDrugCode(java.lang.Long drugCode)
  {
    this.drugCode = drugCode;
  }

  public java.lang.String getAtcNumber()
  {
    return this.atcNumber;
  }

  public void setAtcNumber(java.lang.String tcAtcNumber)
  {
    this.atcNumber = tcAtcNumber;
  }

  public java.lang.String getAtc()
  {
	  return isLanguageFrench() ? StringsUtil.substituteIfNull(atcF, atcE) : atcE;
  }

  public String toString()
	{
	  return new ToStringBuilder(this)
		.append("drugCode", getDrugCode())
		.append("atcNumber", getAtcNumber())
		.append("atcE", getAtcE())
		.append("atcF", getAtcF())
		.toString();
	}

	public boolean equals(Object other)
	{
	  if (!(other instanceof ATC))
		return false;
	  ATC castOther = (ATC) other;
	  return new EqualsBuilder()
		.append(this.getDrugCode(), castOther.getDrugCode())
		.append(this.getAtcNumber(), castOther.getAtcNumber())
		.append(this.getAtcE(), castOther.getAtcE())
		.append(this.getAtcF(), castOther.getAtcF())
		.isEquals();
	}

	public int hashCode()
	{
	  return new HashCodeBuilder(11,39)
	  	.append(drugCode)
	  	.append(atcNumber)
		.toHashCode();
	}



}
