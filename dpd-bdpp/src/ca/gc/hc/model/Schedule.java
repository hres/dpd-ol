package ca.gc.hc.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import ca.gc.hc.util.LocaleDependantObject;
import ca.gc.hc.util.StringsUtil;

/** @author Hibernate CodeGenerator */
public class Schedule extends LocaleDependantObject implements Serializable
{

	/** identifier field */
	private Long drugCode;

	/** nullable persistent field */
	private String scheduleE;
	private String scheduleF;

	/** default constructor */
	public Schedule()
	{
	}

	/** full constructor */
	public Schedule(String scheduleE, String scheduleF)
	{
		this.scheduleE = scheduleE;
		this.scheduleF = scheduleF;
	}

	public String getScheduleE() {
		return scheduleE;
	}

	public void setScheduleE(String scheduleE) {
		this.scheduleE = scheduleE;
	}

	public String getScheduleF() {
		return scheduleF;
	}

	public void setScheduleF(String scheduleF) {
		this.scheduleF = scheduleF;
	}

	public java.lang.Long getDrugCode()
	{
		return this.drugCode;
	}

	public void setDrugCode(java.lang.Long drugCode)
	{
		this.drugCode = drugCode;
	}

	public String getSchedule() {
		return isLanguageFrench() ? StringsUtil.substituteIfNull(scheduleF, scheduleE) : scheduleE;
	}
	
	public String toString()
	{
		return new ToStringBuilder(this)
		.append("drugCode", getDrugCode())
		.append("scheduleE", getScheduleE())
		.append("scheduleF", getScheduleF())
		.toString();
	}

	public boolean equals(Object other)
	{
		if (!(other instanceof Schedule))
			return false;
		Schedule castOther = (Schedule) other;
		return new EqualsBuilder()
		.append(this.getDrugCode(), castOther.getDrugCode())
		.append(this.getScheduleE(), castOther.getScheduleE())
		.append(this.getScheduleF(), castOther.getScheduleF())
		.isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder()
		.append(getDrugCode())
		.append(getScheduleE())
		.append(getScheduleF())
		.toHashCode();
	}


}
