package ca.gc.hc.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import ca.gc.hc.util.LocaleDependantObject;
import ca.gc.hc.util.StringsUtil;

/** @author Hibernate CodeGenerator */
public class Form extends LocaleDependantObject implements Serializable
{

	/** identifier field */
	private Long drugCode;

	/** nullable persistent field */
	private String pharmaceuticalFormE;
	private String pharmaceuticalFormF;

	/** full constructor */
	public Form(String pharmaceuticalFormE, String pharmaceuticalFormF)
	{
	  this.pharmaceuticalFormE = pharmaceuticalFormE;
	  this.pharmaceuticalFormF = pharmaceuticalFormF;
	}

	/** default constructor */
	public Form()
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

	public java.lang.String getPharmaceuticalForm() {
		return isLanguageFrench() ? StringsUtil.substituteIfNull(pharmaceuticalFormF, pharmaceuticalFormE) : pharmaceuticalFormE;
	}

	public String toString()
	{
	  return new ToStringBuilder(this)
		.append("drugCode", getDrugCode())
		.append("pharmaceuticalFormE", getPharmaceuticalFormE())
		.append("pharmaceuticalFormF", getPharmaceuticalFormF())
		.toString();
	}

	public boolean equals(Object other)
	{
	  if (!(other instanceof Form))
		return false;
	  Form castOther = (Form) other;
	  return new EqualsBuilder()
		.append(this.getDrugCode(), castOther.getDrugCode())
		.append(this.getPharmaceuticalFormE(), castOther.getPharmaceuticalFormE())
		.append(this.getPharmaceuticalFormF(), castOther.getPharmaceuticalFormF())
		.isEquals();
	}

	public int hashCode()
	{
	  return new HashCodeBuilder()
		.append(getDrugCode())
		.append(getPharmaceuticalFormE())
		.append(getPharmaceuticalFormF())
		.toHashCode();
	}

	public String getPharmaceuticalFormE() {
		return pharmaceuticalFormE;
	}

	public void setPharmaceuticalFormE(String pharmaceuticalFormE) {
		this.pharmaceuticalFormE = pharmaceuticalFormE;
	}

	public String getPharmaceuticalFormF() {
		return pharmaceuticalFormF;
	}

	public void setPharmaceuticalFormF(String pharmaceuticalFormF) {
		this.pharmaceuticalFormF = pharmaceuticalFormF;
	}

	
}
