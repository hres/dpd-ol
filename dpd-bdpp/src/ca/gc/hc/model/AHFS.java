package ca.gc.hc.model;


import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import ca.gc.hc.util.LocaleDependantObject;
import ca.gc.hc.util.StringsUtil;


/** @author Hibernate CodeGenerator */
public class AHFS extends LocaleDependantObject implements Serializable
{

	/** identifier field */
	private Long drugCode;

	/** nullable persistent field */
	private String ahfsNumber;

	/** nullable persistent field */
	private String ahfsE;
	private String ahfsF;

	/** full constructor */
	public AHFS(java.lang.String tcAhfsNumber, java.lang.String tcAhfsE, java.lang.String tcAhfsF)
	{
		this.ahfsNumber = tcAhfsNumber;
		this.ahfsE = tcAhfsE;
		this.ahfsF = tcAhfsF;
	}

	/** default constructor */
	public AHFS()
	{
	}

	public String getAhfsE() {
		return ahfsE;
	}

	public void setAhfsE(String ahfsE) {
		this.ahfsE = ahfsE;
	}

	public String getAhfsF() {
		return ahfsF;
	}

	public void setAhfsF(String ahfsF) {
		this.ahfsF = ahfsF;
	}

	public java.lang.Long getDrugCode()
	{
		return this.drugCode;
	}

	public void setDrugCode(java.lang.Long drugCode)
	{
		this.drugCode = drugCode;
	}

	public java.lang.String getAhfsNumber()
	{
		return this.ahfsNumber;
	}

	public void setAhfsNumber(java.lang.String tcAhfsNumber)
	{
		this.ahfsNumber = tcAhfsNumber;
	}

	public java.lang.String getAhfs()
	{
		return isLanguageFrench() ? StringsUtil.substituteIfNull(ahfsF, ahfsE) : ahfsE;
	}

	public String toString()
	{
		return new ToStringBuilder(this)
		.append("drugCode", getDrugCode())
		.append("ahfsNumber", getAhfsNumber())
		.append("ahfsE", getAhfsE())
		.append("ahfsF", getAhfsF())
		.toString();
	}

	public boolean equals(Object other)
	{
		if (!(other instanceof AHFS))
			return false;
		AHFS castOther = (AHFS) other;
		return new EqualsBuilder()
		.append(this.getDrugCode(), castOther.getDrugCode())
		.append(this.getAhfsNumber(), castOther.getAhfsNumber())
		.append(this.getAhfsE(), castOther.getAhfsE())
		.append(this.getAhfsF(), castOther.getAhfsF())
		.isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder()
		.append(getDrugCode())
		.append(getAhfsNumber())
		.toHashCode();
	}
}
