package ca.gc.hc.bean;

import ca.gc.hc.controller.DisplayItemAction;
import ca.gc.hc.util.ApplicationGlobals;
import ca.gc.hc.util.LocaleDependantObject;
import org.apache.log4j.Logger;

/**
 * Provides an ISO language code for use in an xhtml or html lang attribute where the related data is
 * presented in a language other than the page language, as required by WCAG2. Primarily used in a French
 * page where the French data is not found and English data is displayed instead. 
 * @author Sylvain Larivière 2012-09-18
 */
public abstract class BaseBean extends LocaleDependantObject{
	/**
	 * @param englishProperty A String representing an English field in a bilingual context
	 * @param frenchProperty A String representing the corresponding French field
	 * @return The appropriate ISO language code suitable for an xml:lang or html:lang attribute if
	 * either parameter is null, or an empty String if neither parameter is null 
	 */
	private static Logger log = Logger.getLogger(BaseBean.class);
	
	public String getLanguageOfPart(String englishProperty, String frenchProperty) {
		String result = "";
		
		if (englishProperty == null && frenchProperty == null) { 
			//result will be an empty String
			log.debug("Language of part for " + englishProperty + ", " + frenchProperty + " is an empty string");		
			}else if (isLanguageFrench()) {
				if (frenchProperty == null) {
					result = ApplicationGlobals.LANG_EN;
				}
			}else if (englishProperty == null) {
				result = ApplicationGlobals.LANG_FR;
			}
		log.debug("Language of part for " + englishProperty + ", " + frenchProperty + "= " + result);		
		return result;	
	}
}
