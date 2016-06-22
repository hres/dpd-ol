package ca.gc.hc.model;


import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import ca.gc.hc.util.LocaleDependantObject;
import ca.gc.hc.util.StringsUtil;


/** @author Hibernate CodeGenerator */
public class Veterinary extends LocaleDependantObject implements Serializable
{
	private Long id;

	/** identifier field */
	private Long drugCode;

	/** nullable persistent field */
	private String vetSpeciesE;
	private String vetSpeciesF;

	/** nullable persistent field */
//	private String vetSubSpeciesE;
//	private String vetSubSpeciesF;

	/** full constructor */
	public Veterinary(String tcVetSpeciesE, String tcVetSpeciesF, String tcVetSubSpeciesE, String tcVetSubSpeciesF)
	{
		this.vetSpeciesE = tcVetSpeciesE;
		this.vetSpeciesF = tcVetSpeciesF;
//		this.vetSubSpeciesE = tcVetSubSpeciesE;
//		this.vetSubSpeciesF = tcVetSubSpeciesF;
	}

	/** default constructor */
	public Veterinary()
	{
	}

	public String toString()
	{
		return new ToStringBuilder(this)
		.append("drugCode", getDrugCode())
		.append("vetSpeciesE", getVetSpeciesE())
		.append("vetSpeciesF", getVetSpeciesF())
//		.append("vetSubSpeciesE", getVetSubSpeciesE())
//		.append("vetSubSpeciesF", getVetSubSpeciesF())
		.toString();
	}

	public Long getDrugCode() {
		return drugCode;
	}

	public void setDrugCode(Long drugCode) {
		this.drugCode = drugCode;
	}

	public String getVetSpecies() {
		return isLanguageFrench() ? StringsUtil.substituteIfNull(vetSpeciesF, vetSpeciesE) : vetSpeciesE;
	}

//	public String getVetSubSpecies() {
//		return isLanguageFrench() ? StringsUtil.substituteIfNull(vetSubSpeciesF, vetSubSpeciesE) : vetSubSpeciesE;
//	}

	public boolean equals(Object other)
	{
		if (!(other instanceof Veterinary))
			return false;
		Veterinary castOther = (Veterinary) other;
		return new EqualsBuilder()
		.append(this.getDrugCode(), castOther.getDrugCode())
		.append(this.getVetSpeciesE(), castOther.getVetSpecies())
		.append(this.getVetSpeciesF(), castOther.getVetSpecies())
//		.append(this.getVetSubSpeciesE(), castOther.getVetSubSpecies())
//		.append(this.getVetSubSpeciesF(), castOther.getVetSubSpecies())
		.isEquals();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/*
	public int hashCode()
	{
	  return new HashCodeBuilder()
		.append(getDrugCode())
		.append(getAhfsNumber())
		.toHashCode();
	}
	 */

	/**
	 * @author Sylvain Larivière 2009-09-21
	 */
//	public String getLowerCaseSubSpecies(){
//		return this.getVetSubSpecies().toLowerCase();
//	}

	public String getVetSpeciesE() {
		return vetSpeciesE;
	}

	public void setVetSpeciesE(String vetSpeciesE) {
		this.vetSpeciesE = vetSpeciesE;
	}

	public String getVetSpeciesF() {
		return vetSpeciesF;
	}

	public void setVetSpeciesF(String vetSpeciesF) {
		this.vetSpeciesF = vetSpeciesF;
	}

//	public String getVetSubSpeciesE() {
//		return vetSubSpeciesE;
//	}
//
//	public void setVetSubSpeciesE(String vetSubSpeciesE) {
//		this.vetSubSpeciesE = vetSubSpeciesE;
//	}
//
//	public String getVetSubSpeciesF() {
//		return vetSubSpeciesF;
//	}
//
//	public void setVetSubSpeciesF(String vetSubSpeciesF) {
//		this.vetSubSpeciesF = vetSubSpeciesF;
//	}

//	public void setVetSpecies(String vetSpecies) {
//		this.vetSpecies = vetSpecies;
//	}
//
//	public void setVetSubSpecies(String vetSubSpecies) {
//		this.vetSubSpecies = vetSubSpecies;
//	}

}
