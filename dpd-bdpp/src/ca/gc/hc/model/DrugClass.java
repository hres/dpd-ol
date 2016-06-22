/**
 * 
 */
package ca.gc.hc.model;

import java.io.Serializable;

import ca.gc.hc.util.LocaleDependantObject;
import ca.gc.hc.util.StringsUtil;

/**
 * @author SYLARIVI
 *
 */
public class DrugClass extends LocaleDependantObject implements Serializable {

	private static final long serialVersionUID = 4681187089584143109L;
	private Long drugClassId;
	private String drugClassE;
	private String drugClassF;
	
	public DrugClass() {
	}
	
	public DrugClass(Long ID, String drugClassE, String drugClassF) {
		this.setDrugClassId(ID);
		this.setDrugClassE(drugClassE);
		this.setDrugClassF(drugClassF);
	}

	public void setDrugClassId(Long drugClassId) {
		this.drugClassId = drugClassId;
	}

	public Long getDrugClassId() {
		return drugClassId;
	}

	public void setDrugClassE(String drugClassE) {
		this.drugClassE = drugClassE;
	}

	public String getDrugClassE() {
		return drugClassE;
	}

	public void setDrugClassF(String drugClassF) {
		this.drugClassF = drugClassF;
	}

	public String getDrugClassF() {
		return drugClassF;
	}

	public String getStatus() {
		return isLanguageFrench() ? StringsUtil.substituteIfNull(drugClassF, drugClassE) : drugClassE;
	}
}
