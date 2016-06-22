package ca.gc.hc.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import ca.gc.hc.util.LocaleDependantObject;

public class ExternalStatus extends LocaleDependantObject implements
		Serializable {
	private static final long serialVersionUID = -4554553917511180689L;

	private Long externalStatusId;
	private String externalStatusE;
	private String externalStatusF;
	private Date inactiveDate;

	public ExternalStatus() {

	};

	public ExternalStatus(Long externalStatusId, String externalStatusE,
			String externalStatusF,Date inactiveDate) {
		this.externalStatusId = externalStatusId;
		this.externalStatusE = externalStatusE;
		this.externalStatusF = externalStatusF;
		this.inactiveDate = inactiveDate;
		
	}

	public void setExternalStatusId(Long externalStatusId) {
		this.externalStatusId = externalStatusId;
	}

	public Long getExternalStatusId() {
		return externalStatusId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((externalStatusE == null) ? 0 : externalStatusE.hashCode());
		result = prime * result
				+ ((externalStatusF == null) ? 0 : externalStatusF.hashCode());
		result = prime
				* result
				+ ((externalStatusId == null) ? 0 : externalStatusId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ExternalStatus))
			return false;
		ExternalStatus other = (ExternalStatus) obj;
		if (externalStatusE == null) {
			if (other.externalStatusE != null)
				return false;
		} else if (!externalStatusE.equals(other.externalStatusE))
			return false;
		if (externalStatusF == null) {
			if (other.externalStatusF != null)
				return false;
		} else if (!externalStatusF.equals(other.externalStatusF))
			return false;
		if (externalStatusId == null) {
			if (other.externalStatusId != null)
				return false;
		} else if (!externalStatusId.equals(other.externalStatusId))
			return false;
		return true;
	}

	public String getExternalStatusE() {
		return externalStatusE;
	}

	public void setExternalStatusE(String externalStatusE) {
		this.externalStatusE = externalStatusE;
	}

	public String getExternalStatusF() {
		return externalStatusF;
	}

	public void setExternalStatusF(String externalStatusF) {
		this.externalStatusF = externalStatusF;
	}

	public String toString() {
		return new ToStringBuilder(this).append("StatusE", externalStatusE)
				.append("StatusF", externalStatusF).toString();
	}

	public String getExternalStatus() {
		return localisedProperty(this.externalStatusE, this.externalStatusF);
	}

	public void setInactiveDate(Date inactiveDate) {
		this.inactiveDate = inactiveDate;
	}

	public Date getInactiveDate() {
		return inactiveDate;
	}
}
