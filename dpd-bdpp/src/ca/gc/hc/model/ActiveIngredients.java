package ca.gc.hc.model;

import java.io.Serializable;
import ca.gc.hc.util.LocaleDependantObject;
import ca.gc.hc.util.StringsUtil;

/** @author Hibernate CodeGenerator */
public class ActiveIngredients extends LocaleDependantObject implements Serializable
{

  /** identifier field */
  private Long id;

  /** persistent field */
  private Long drugCode;

  /** persistent field */
  private String ingredientE;
  private String ingredientF;

  /** nullable persistent field */
  private String strength;

  /** nullable persistent field */
  private String strengthUnitE;
  public String getStrengthUnitE() {
	return strengthUnitE;
}

public void setStrengthUnitE(String strengthUnitE) {
	this.strengthUnitE = strengthUnitE;
}

private String strengthUnitF;

  public String getStrengthUnitF() {
	return strengthUnitF;
}

public void setStrengthUnitF(String strengthUnitF) {
	this.strengthUnitF = strengthUnitF;
}

/** nullable persistent field */
  private String dosageValue;

  /** nullable persistent field */
  private String dosageUnitE;
  private String dosageUnitF;
  

  public String getDosageUnitF() {
	return dosageUnitF;
}

  public void setDosageUnitF(String dosageUnitF) {
	this.dosageUnitF = dosageUnitF;
}

/** full constructor */
  public ActiveIngredients(Long drugCode, String ingredientE, String ingredientF, 
		  String strength, String strengthUnitE, String strengthUnitF, String dosageValue, String dosageUnitE, String dosageUnitF)
  {
    this.drugCode = drugCode;
    this.ingredientE = ingredientE;
    this.ingredientF = ingredientF;
    this.strength = strength;
    this.strengthUnitE = strengthUnitE;
    this.dosageValue = dosageValue;
    this.dosageUnitE = dosageUnitE;
    this.dosageUnitF = dosageUnitF;
  }

  /** default constructor */
  public ActiveIngredients()
  {
  }

  /** minimal constructor */
  public ActiveIngredients(String ingredientE, String ingredientF)
  {
    this.ingredientE = ingredientE;
    this.ingredientF = ingredientF;
  }

  public Long getId()
  {
    return id;
  }

  public void setId(Long long1)
  {
    id = long1;
  }

  public java.lang.Long getDrugCode()
  {
    return this.drugCode;
  }

  public void setDrugCode(java.lang.Long drugCode)
  {
    this.drugCode = drugCode;
  }

  public java.lang.String getIngredient() {
	  return isLanguageFrench() ? StringsUtil.substituteIfNull(ingredientF, ingredientE) : ingredientE;
  }

  public void setIngredientE(java.lang.String ingredientE)
  {
    this.ingredientE = ingredientE;
  }

  public java.lang.String getStrength()
  {
    return this.strength;
  }

  public void setStrength(java.lang.String strength)
  {
    this.strength = strength;
  }

  public java.lang.String getStrengthUnit()
  {
	  return isLanguageFrench() ? StringsUtil.substituteIfNull(strengthUnitF, strengthUnitE) : strengthUnitE;
  }

  public java.lang.String getDosageValue()
  {
    return this.dosageValue;
  }

  public void setDosageValue(java.lang.String dosageValue)
  {
    this.dosageValue = dosageValue;
  }

  public java.lang.String getDosageUnitE()
  {
    return this.dosageUnitE;
  }
  
  public String getDosageUnit() {
	  return isLanguageFrench() ? StringsUtil.substituteIfNull(dosageUnitF, dosageUnitE) : dosageUnitE;
  }
  
  public void setDosageUnitE(java.lang.String dosageUnitE)
  {
    this.dosageUnitE = dosageUnitE;
  }

  public String getFirstAIName() {
    return isLanguageFrench() ? StringsUtil.substituteIfNull(ingredientF, ingredientE) : ingredientE;
	}

	/**
	 * Sylvain Larivière 2009-12-07
	 * @return active ingredient strength in the form &lt;strength&gt; &lt;unit&gt;
	 *  for instance " 100 MG", or ".2 %".
	 *  Dosage (eg "per tablet" or "per ml") is delegated to getDosageText()
	 *  @see getDosageText().
	 */
	public String getAiStrengthAndDosageText(){
		String result = "";
		
		result = getAiStrengthText(); 
		if (!isDosageUnitAPercentage()) {
			result += getAiDosageText();
		}	
		return  result;
	}
	
	public String getAiStrengthText(){
		return this.strength + " " + this.getStrengthUnit();
	}

	public String getIngredientF() {
		return ingredientF;
	}

	public void setIngredientF(String ingredientF) {
		this.ingredientF = ingredientF;
	}

	public String getIngredientE() {
		return ingredientE;
	}
	
	/**
	 * Sylvain Larivière 2009-12-07
	 * @return Dosage in the form " / &lt;value&gt; &lt;unit&gt; "
	 *  for instance " / 5 ML" (per 5 ml) or " / ML" (per ml)
	 * @see getAIStrengthAndDosageText()
	 */
	public String getAiDosageText() {
		String result = "";
		
		if (StringsUtil.hasData(this.dosageValue)) {
			if (StringsUtil.hasData(this.getDosageUnit())) {
				result = " / " + this.dosageValue + " " + this.getDosageUnit();
			}
		}else {
			if (!isDosageUnitAPercentage()) 
				if (StringsUtil.hasData(getDosageUnit())){
			result = " / " + this.getDosageUnit();
			}
		}
		return result;
	}
	
	public boolean isDosageUnitAPercentage() {
		return StringsUtil.hasData(this.getDosageUnit()) && dosageUnitE.equals("%");
	}
  
}
