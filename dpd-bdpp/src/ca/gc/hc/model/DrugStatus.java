package ca.gc.hc.model;

import java.io.Serializable;

import ca.gc.hc.util.LocaleDependantObject;
import ca.gc.hc.util.StringsUtil;


/** @author Hibernate CodeGenerator 
 * Updated SL/2010-01-26 to include statusID and French status
 * Updated SL/2015-02 ADR0183 to implement external status
 *         codes and descriptions. The external status will be used from now on
 *         instead of the previous status codes and descriptions. Previous
 *         status codes are still used internally, but mapped to external status
 *         codes that are for external, public users of DPD Online. The internal
 *         codes and descriptions are still exported to the DPD Online database,
 *         but no longer used in the application. Therefore, they were removed
 *         from this class and the status accessors now return external status
 *         data. In addition, under this same ADR, the first marketed date is no
 *         longer used and was replaced by the original market date. The history
 *         date is maintained and will be the date of the latest status change.
 *         As an example, since the company marketing a drug can change, the
 *         history date would reflect when the new company started marketing the
 *         drug, and the original market date would reflect when the original
 *         company initially started marketing it.
 * */
public class DrugStatus extends LocaleDependantObject  implements Serializable
{
	private static final long serialVersionUID = -6139442932387857207L;
	
	/** persistent field */
	private Long drugCode;
	private Long statusID;
	private ExternalStatus externalStatus; // ADR0183		

	/** nullable persistent field */
	private java.util.Date historyDate;

	/** Original marketing date (regardless of later company changes)*/
	private java.util.Date originalMarketDate; // ADR0183

	/** full constructor */
	public DrugStatus(java.lang.Long drugCode, java.lang.Long statusID,
			java.util.Date historyDate, java.util.Date originalMarketDate,
			ExternalStatus externalStatus) {
		this.drugCode = drugCode;
		this.statusID = statusID;
		this.historyDate = historyDate;
		this.originalMarketDate = originalMarketDate;
		this.setExternalStatus(externalStatus);
	}

	/** default constructor */
	public DrugStatus()
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

	public java.lang.String getStatus() {
		return isLanguageFrench() ? StringsUtil.substituteIfNull(externalStatus
				.getExternalStatusF(), externalStatus.getExternalStatusE())
				: externalStatus.getExternalStatusE();
	}

	public java.util.Date getHistoryDate()
	{
		return this.historyDate;
	}

	public void setHistoryDate(java.util.Date historyDate)
	{
		this.historyDate = historyDate;
	}



	public String getStatusE() {
		return externalStatus.getExternalStatusE();
	}

	public String getStatusF() {
		return externalStatus.getExternalStatusF();
	}

	public Long getStatusID() {
		return statusID;
	}

	public void setStatusID(Long statusID) {
		this.statusID = statusID;
	}

	public void setOriginalMarketDate(java.util.Date originalMarketDate) {
		this.originalMarketDate = originalMarketDate;
	}

	public java.util.Date getOriginalMarketDate() {
		return originalMarketDate;
	}

	public void setExternalStatus(ExternalStatus externalStatus) {
		this.externalStatus = externalStatus;
	}

	public ExternalStatus getExternalStatus() {
		return externalStatus;
	}

}
