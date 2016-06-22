package ca.gc.hc.util;

import ca.gc.hc.util.ApplicationGlobals;
import java.util.Locale;

/*******************************************************************************
 * An object used as a Superclass to domain objects (Entities) that have Locale-
 * dependant values. It provides some simple functionality around establishing
 * what language these should be displayed in.
 */
public abstract class LocaleDependantObject {
    private Locale userLocale;
    
    /***************************************************************************
     * Gets whether the language of the user's Locale is French.
     * @return true if the language of the user's Locale is French.
     */
    public boolean isLanguageFrench() {
    	
		
	    return ApplicationGlobals.instance().LANG_FR.equals(getUserLocale().getLanguage());
    }

    /***************************************************************************
     * Gets the Locale that this object should use when returning Locale-dependant
     * values. If set to a specific value (e.g. it is being maintained in the
     * Page or Request scope), this will return that value. Otherwise (e.g. it
     * is being maintained in the Session or Application scope), it will
     * get the user's current Locale setting from ApplicationGlobals.
     * @return the Locale (language) that text values should be returned in.
     * @see #setUserLocale(Locale)
     */
    public Locale getUserLocale() {
        if (userLocale != null) {
            return userLocale;
        }
        return ApplicationGlobals.instance().getUserLocale();
    }
    
    /***************************************************************************
     * Sets the Locale that this object should use when returning Locale-dependant
     * values. Only set when this object should always return values based on
     * the same Locale (e.g. it is being maintained in the Page or Request scope).
     * @param aLocale the Locale (language) that text values should be returned
     *        in.
     * @see #getUserLocale()
     */
    public void setUserLocale(Locale aLocale) {
        userLocale = aLocale;
    }

    /**
     * @param english
     * @param french
     * @return Either english or french depending on the user locale
     * @author Sylvain Lariviere 2014-01-07
     */
    public String localisedProperty(String english, String french) {
    	if (isLanguageFrench()) {
    		return french;
    	}else{
    		return english;
    	}
    	
    }
}
