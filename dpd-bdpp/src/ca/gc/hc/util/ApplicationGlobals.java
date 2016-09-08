package ca.gc.hc.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.util.LabelValueBean;

import ca.gc.hc.dao.SearchDrugDao;
import ca.gc.hc.model.DrugClass;
import ca.gc.hc.model.ExternalStatus;

/*******************************************************************************
 * An object used to store constants and other relatively static values that
 * are used throughout the application. "relatively static values" are lazy
 * initialized and are provided with a clear() method should an action occur
 * where their values could be outdated. They will require an active Hibernate
 * Session when initializing. Note that this is implemented as a Singleton.
 */
public final class ApplicationGlobals {
	
	private HashMap<String,List<LabelValueBean>> statusMap = new HashMap<String,List<LabelValueBean>>();
	private HashMap<String,List<LabelValueBean>> drugClassMap = new HashMap<String,List<LabelValueBean>>();
	private HashMap<String,List<LabelValueBean>> uniqueFormsMap = new HashMap<String,List<LabelValueBean>>();
	private HashMap<String,List<LabelValueBean>> uniqueRoutesMap = new HashMap<String,List<LabelValueBean>>();
	private HashMap<String,List<LabelValueBean>> uniqueSchedulesMap = new HashMap<String,List<LabelValueBean>>();

	
	public static final String APP_VERSION = "1.0 beta1";
	public static final String LANG_EN = Locale.ENGLISH.getLanguage();
	public static final String LANG_FR = Locale.FRENCH.getLanguage();
	private static final String SELECT_ALL_EN = "Select all";
	private static final String SELECT_ALL_FR = "Aucune restriction";
	private static final String DISCONTINUED_ALL_EN = "Discontinued (ALL)";
	private static final String DISCONTINUED_ALL_FR = "Discontinué (Tout)";
	public static final String ACTIVE_DRUG_STATUS_ID = "2"; //This needs to equal the wqry_status.status_code of 'Marketed (Notified)',
															// or any equivalent description that means "active"
	public final static String APPROVED = "99";
	
	
	//Keys used to store instances in Application, Session, or Request attributes
	public static final String APP_GLOBALS = "appGlobals"; //Application
	public static final String DRUG_CLASS_NAMES = "drugClassNames";
	public static final String ERROR_PSWD_EXCEED_TIME_CHANGE = "error_pswd_exceed_time_change";
	public static final String ERROR_OBSERVATION_COMMENT = "error.observation_comment";
	public static final String ERROR_FILE_NOT_EXIST_ACCOUNT_LOCKED = "error.file_not_exist_account_lock";
	public static final String ERROR_EMPTY_FIELD = "error.empty_file";
	public static final String ERROR_TOTAL_REQUESTED = "error.total_requested";
	public static final String ERROR_RECORD_NO_MATCH = "error.no_results";
	public static final String ERROR_OPENING_PDF_DOCUMENT = "error_opening_pdf_document"; 
	public static final String ERROR_OPENING_IMAGE = "error_opening_image";
	public static final String ERROR_CONNECTING_URL = "error_connecting_url";
	public static final String ERROR_PDF_NOT_FOUND = "error_pdf_not_found";
	public static final String ERROR_IMAGE_NOT_FOUND = "error_image_not_found";
	
	
	/**
	* The bean name for the toggle locale URL bean and the toggle locale display
	* cache bean.  String constants defined for the locales for french and
	* english.
	*/
	static public final String TOGGLE_LOCALE_URL_BEAN = "toggleURL";
	static public final String TOGGLE_LOCALE_DISPLAY_CACHE_BEAN = "displayCache";
	static public final String LOCALE_STRING_FRENCH = "FR_CA";
	static public final String LOCALE_STRING_ENGLISH = "EN_CA";
	static public final String PARAM_SELECTED_LANGUAGE = "selectedLanguage";

	/**
	 * The name of the object in the HttpSession holding the search result object
	 * and search criteria object.
	 */
	static public final String SEARCH_RESULT_KEY = "dpd.search.results";
	static public final String USER_SEARCH_CRITERIA = "dpd.search.criteria"; // as typed by user: used in GUI
	static public final String SELECTED_PRODUCT = "dpd.selected.product";
	static public final String PAGER_FORM = "dpd.pager";
	static public final String SELECTED_STATUS = "dpd.selected.status";

	//DataTables
	static public final String QUERY_SEARCH_CRITERIA = "dpd.query.search.criteria"; // with quotes doubled: used for queries
	static public final String PAGE_LENGTH_KEY = "page_length";
	static public final int INITIAL_PAGE_LENGTH_VALUE = 25;
	static public final String RESULT_COUNT_KEY = "result_count";
	static public final String DATA_TABLES_PROCESSING_TOGGLE_KEY = "DT.server.processing.results.toggle";
	static public final String DATA_TABLES_PROCESSING_TOGGLE_VALUE = "dataTables.toggle.value";
	static public final String AJAX_BEAN = "ajax.bean";
	static public final String AJAX_STATUS = "ajax.status"; 
	private int dataTablesProcessingToggleLevel = 0;
	/* index of the Brand name column in the DataTable */
	public static final String DATA_TABLE_BRAND_NAME_COLUMN = "3";
	
	
	public final static String ACTIVE_CODE = "A";
	public final static String DISCONTINUE_CODE = "D";
	public final static Long APPROVED_STATUS_CODE = 1L;

	
	/**
	* String constants defined for the resource bundle for the redirect page.
	*/
	public static final String REDIRECT_FILE = "Redirect";
	public static final String REDIRECT_ROOT = "root";
	public static final String SQL_QUERY = "";

	/**
	* Hide the DIN for radiopharmaceutical products
	* ADR0183 October 2014
	*/
	public final static Long RADIOPHARMACEUTICAL_CLASS_CODE = 9L;
	public final static String NOT_APPLICABLE_E = "Not Applicable";
	public final static String NOT_APPLICABLE_F = "Sans objet";
	
	
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ApplicationGlobals.class);
	
	
	private static ApplicationGlobals instance;
	private static ThreadLocal userLanguage = new ThreadLocal();
	private static ThreadLocal userLocale = new ThreadLocal();

	List status;
	
	static {
			instance = new ApplicationGlobals();
		
	}
	/***************************************************************************
	 * Generic singleton constructor.
	 */
	private ApplicationGlobals() {
	}
	/***************************************************************************
	 * Gets the single instance of this class.
	 */
	public static ApplicationGlobals instance() {


		return instance;
	}

	/***************************************************************************
	 * Adds the singleton of this class to the attributes of the passed
	 * ServletContext (application).
	 */
	public static void connectToContext(ServletContext context) {
		log.info("Hooking up ApplicationGlobals");
		context.setAttribute(APP_GLOBALS, instance());
	}

	/***************************************************************************
	 * Gets the language of the user based on a previous call to
	 * initLocalization(). If it has not been set, this defaults it to LANG_EN.
	 * @return the language previously set by a call to initLocalization().
	 * @see #initLocalization()
	 */
	public String getUserLanguage() {
		String language = (String)userLanguage.get();

		if (language == null) {
			log.error("User Language requested without Localization being properly initialized.");
			language = LANG_EN; //Default value
			userLanguage.set(language);
		}

		return language;
	}

	/***************************************************************************
	 * Gets the locale of the user based on a previous call to initLocalization().
	 * If it has not been set, this defaults it to Canadian English.
	 * @return the locale previously set by a call to initLocalization().
	 * @see #initLocalization()
	 */
	public Locale getUserLocale() {
		Locale locale = (Locale)userLocale.get();

		if (locale == null) {
			log.error("User Locale requested without Localization being properly initialized.");
			locale = new Locale("en", "CA"); //Default value
			userLocale.set(locale);
		}

		return locale;
	}

	/***************************************************************************
	 * A method borrowed from org.apache.struts.util.RequestUtils (not available
	 * until version 1.2).
	 * -Dwight Hubley, 2006-05-08
	 * <p>Look up and return current user locale, based on the specified
	 * parameters.</p>
	 *
	 * @param request The request used to lookup the Locale
	 * @param localKey  Name of the session attribute for our user's Locale. If
	 *        this is <code>null</code>, the default locale key is used for the
	 *        lookup.
	 * @return current user locale
	 */
	public Locale getUserLocale(HttpServletRequest request, String localKey) {
		Locale userLocale = null;
		HttpSession session = request.getSession(false);

		if (localKey == null) {
			localKey = Globals.LOCALE_KEY;
		}

		// Only check session if sessions are enabled
		if (session != null) {
			userLocale = (Locale) session.getAttribute(localKey);
		}

		if (userLocale == null) {
			// Returns Locale based on Accept-Language header or the server default
			userLocale = request.getLocale();
		}

		return userLocale;
	}

	/***************************************************************************
	 * This should be called prior to any requests for collections of language-
	 * specific items so that they will be returned in the correct language.
	 * Typically called by Actions or at the top of a JSP, this stores the
	 * language in a ThreadLocal object so that subsequent access to these
	 * collections during the current Request will be in the appropriate
	 * language. It determines the language requested by the user based on their
	 * Locale. All Locales with a language other than French will use English.
	 * @param request the servlet request we are processing.
	 */
	public void initLocalization(HttpServletRequest request) {
		Locale locale = getUserLocale(request, null);

		if (locale == null) {
			locale = new Locale("en", "CA"); //Default value
		}
		userLocale.set(locale);
		if (LANG_FR.equals(locale.getLanguage())) {
			userLanguage.set(LANG_FR);
		} else {
			userLanguage.set(LANG_EN); //Default value
		}

		log.info("Initializing Localization to " + getUserLocale());	 
	}

	public static void setUserLocale(Locale locale) {

		if (locale == null) {
			locale = new Locale("en", "CA"); //Default value
		}
		userLocale.set(locale);
		if (LANG_FR.equals(locale.getLanguage())) {
			userLanguage.set(LANG_FR);
		} else {
			userLanguage.set(LANG_EN); //Default value
		}
	}

	/***************************************************************************
	 * Gets a collection of all of the status that are in the system. These
	 * are Locale dependant, so initLocalization(request) must have already been
	 * called during the processing of this request.
	 * @return a collection of valid status descriptions.
	 * @see #initLocalization(request)
	 * Updated SL/2010-01-25 to no longer query by status descriptions but to use status_code instead
	 */
	public List getStatus() throws Exception {
		if (statusMap.isEmpty()) {
			this.loadUniqueStatuses();
		}
		return (List)statusMap.get(getUserLanguage());
	}


	public List<String> getUniqueRoutes() throws Exception {
		if (uniqueRoutesMap.isEmpty()) {
			loadUniqueRoutes();
		}
		return (List)uniqueRoutesMap.get(getUserLanguage());
	}

	public List getUniqueForms() throws Exception {
		if (this.uniqueFormsMap.isEmpty()) {
			loadUniquePharmaceuticalForms();
		}
		return (List)uniqueFormsMap.get(getUserLanguage());
	}

	public List<LabelValueBean> getUniqueDrugClasses() throws Exception {
		if (this.drugClassMap.isEmpty()) {
			this.loadUniqueDrugClasses();
		}
		return (List)drugClassMap.get(getUserLanguage());
	}

	public List getUniqueSchedules() throws Exception {
		if (uniqueSchedulesMap.isEmpty()) {
			loadUniqueSchedules();
		}
		return (List)uniqueSchedulesMap.get(getUserLanguage());
	}

	/**
	 * Populates distinct routes of administration for use in combo box. 
	 * @author Sylvain Larivière 2009-09-10
	 * Updated 2009-10-26 to allow English and French lists
	 */
	private void loadUniqueRoutes() throws Exception {

		try {
			SearchDrugDao search = new SearchDrugDao();
			HashMap<String,List<String>> routesMap = search.retrieveUniqueRoutes();	

			if (routesMap != null) {
				this.populateDrugSearchCriteriaList(routesMap, uniqueRoutesMap);
			}
		}catch (Exception e) {
			log.error("Stack Trace: ", e);
			StringBuffer message = new StringBuffer("loadUniqueRoutes [");
			message.append("] failed");
			throw new Exception(message.toString());
		}
	}

	/**
	 * Populates distinct pharmaceutical forms for use in combo box. 
	 * @author Sylvain Larivière 2009-09-10
	 * Updated 2009-10-26 to allow English and French lists
	 */
	private void loadUniquePharmaceuticalForms() throws Exception {
		
		try {	
			SearchDrugDao search = new SearchDrugDao();			
			HashMap<String,List<String>> formsMap = search.retrieveUniqueForms();
			if (formsMap != null) {
				this.populateDrugSearchCriteriaList(formsMap, uniqueFormsMap);
			}

		} catch (Exception e) {
			log.error("Stack Trace: ", e);
			StringBuffer message = new StringBuffer("loadUniquePharmaceuticalForms [");
			message.append("] failed");
			throw new Exception(message.toString());
		}
	}
	
	/**
	 * Populates distinct schedules for use in combo box. 
	 * @author Sylvain Larivière 2009-09-10
	 * Updated 2009-10-26 to allow English and French lists
	 */
	private void loadUniqueSchedules() throws Exception {
		
		try {
			SearchDrugDao search = new SearchDrugDao();			
			HashMap<String,List<String>> schedulesMap = search.retrieveUniqueSchedules();
			
			if (schedulesMap != null) {	
				this.populateDrugSearchCriteriaList(schedulesMap, uniqueSchedulesMap);
			}
		}catch (Exception e) {
			log.error("Stack Trace: ", e);
			StringBuffer message = new StringBuffer("loadUniqueSchedules [");
			message.append("] failed");
			throw new Exception(message.toString());
		}
	}
	
	public String getSelectAllLabel() {
		String selectAllLabel = "";
		
		if (LANG_FR.equals(getUserLocale().getLanguage())) {
			selectAllLabel = SELECT_ALL_FR;
		}else {
			selectAllLabel = SELECT_ALL_EN;
		}
		return selectAllLabel;
	}
	
	private String getSelectAllLabel(String language) {
		String selectAllLabel = "";
		
		if (language.equals(LANG_FR)) {
			selectAllLabel = SELECT_ALL_FR;
		}else {
			selectAllLabel = SELECT_ALL_EN;
		}
		return selectAllLabel;
	}
	
	private void populateDrugSearchCriteriaList(HashMap mapList, HashMap uniqueMap) 
		throws Exception {
		
		try {
			uniqueMap.clear();
			if(mapList.containsKey(LANG_EN)) {
				generateUniqueMapforLanguage(LANG_EN, mapList, uniqueMap);	
				
			}
			if(mapList.containsKey(LANG_FR)) {
				generateUniqueMapforLanguage(LANG_FR, mapList, uniqueMap);	
				
			}
		} catch (Exception e) {
			log.error("Stack Trace: ", e);
			StringBuffer message = new StringBuffer("populateDrugSearchCriteriaList [");
			message.append("] failed");
			throw new Exception(message.toString());
		}
	}
	
	private void generateUniqueMapforLanguage(String language, HashMap mapList, HashMap uniqueMap) throws Exception {
		ArrayList<LabelValueBean> uniqueListItems = new ArrayList<LabelValueBean>();
		
//		add the "SelectAll label"
		uniqueListItems.add(new LabelValueBean(getSelectAllLabel(language), "0"));
		//populate the rest of the list
		ArrayList<String> itemsList = (ArrayList<String>)mapList.get(language);
		Iterator it = itemsList.iterator();
		
		try {
			
			for (String item: itemsList) {
				if(item != null) {
					uniqueListItems.add(new LabelValueBean(item, item));
				}
				
			}
//			while (it.hasNext()) {
//				if (it.next() != null) {
//					String item = it.next().toString();					
//					uniqueListItems.add(new LabelValueBean(item, item));
//				}
//			}
			uniqueMap.put(language, uniqueListItems);
		} catch (Exception e) {
			StringBuffer message = new StringBuffer("generateUniqueMapforLanguage [");
			message.append(language);
			message.append("] failed");
			throw new Exception(message.toString());
		}
	}
	
	/**
	 * Populates distinct statuses for use in combo box.
	 * 
	 * @author Sylvain Larivière 2010-01-25 Updated SL/2014-12-24 ADR0183 to
	 *         implement external status codes
	 */
	private void loadUniqueStatuses() throws Exception {
		try {
			SearchDrugDao search = new SearchDrugDao();
			List<ExternalStatus> allStatuses = search.retrieveAllExternalStatuses();

			if (allStatuses != null) {
				this.populateDrugStatusSearchCriteria(allStatuses);
			}
		} catch (Exception e) {
			log.error("Stack Trace: ", e);
			StringBuffer message = new StringBuffer("loadUniqueStatuses [");
			message.append("] failed");
			throw new Exception(message.toString());
		}
	}

	private void populateDrugStatusSearchCriteria(
			List<ExternalStatus> statusList) throws Exception {
		this.statusMap.clear();
		generateUniqueStatusMapForLanguage(ApplicationGlobals.LANG_EN,
				statusList);
		generateUniqueStatusMapForLanguage(ApplicationGlobals.LANG_FR,
				statusList);
	}

	private void generateUniqueStatusMapForLanguage(String language,
			List<ExternalStatus> statusList) {
		ArrayList<LabelValueBean> uniqueListItems = new ArrayList<LabelValueBean>();

		// sort statusList based on language
		Collections.sort(statusList, new ExternalStatusComparator(language));

		for (ExternalStatus s : statusList) {
			String localizedStatus = "";
			if (language.equals(LANG_FR)) {
				localizedStatus = s.getExternalStatusF();
			} else {
				localizedStatus = s.getExternalStatusE();
			}
			uniqueListItems.add(new LabelValueBean(localizedStatus, s
					.getExternalStatusId().toString()));
		}
		// add the "Select ALL" label
		uniqueListItems.add(0, new LabelValueBean(getSelectAllLabel(language),
				"0"));
		statusMap.put(language, uniqueListItems);
	}
	
	private String getDiscontinuedAllLabel(String language) {
		String DiscontinuedAllLabel = "";
		
		if (language.equals(LANG_FR)) {
			DiscontinuedAllLabel = DISCONTINUED_ALL_FR;
		}else {
			DiscontinuedAllLabel = DISCONTINUED_ALL_EN;
		}
		return DiscontinuedAllLabel;
	}
	
	public void refreshAllSearchPageLists() throws Exception{
		statusMap.clear();
		loadUniqueStatuses();
		uniqueRoutesMap.clear();
		loadUniqueRoutes();
		uniqueFormsMap.clear();
		loadUniquePharmaceuticalForms();
		uniqueSchedulesMap.clear();
		loadUniqueSchedules();
		drugClassMap.clear();
		loadUniqueDrugClasses();
	}
	
	/**
	 * Populates distinct drug classes for use in combo box. 
	 * @author Sylvain Larivière 2014-10-15
	 */
	private void loadUniqueDrugClasses() throws Exception {
		try {
			SearchDrugDao search = new SearchDrugDao();
			List<DrugClass> allDrugClasses = search.retrieveUniqueDrugClasses();	

			if (allDrugClasses != null) {
				this.populateDrugClassSearchCriteria(allDrugClasses);
			}
		}catch (Exception e) {
			log.error("Stack Trace: ", e);
			StringBuffer message = new StringBuffer("loadUniqueDrugClasses [");
			message.append("] failed");
			throw new Exception(message.toString());
		}
	}
	
	private void populateDrugClassSearchCriteria(List<DrugClass> classList)  
	throws Exception {
		drugClassMap.clear();
		generateUniqueDrugClassMapForLanguage(ApplicationGlobals.LANG_EN, classList);
		generateUniqueDrugClassMapForLanguage(ApplicationGlobals.LANG_FR, classList);
	}
	
	/**
	 * @param language A String corresponding to either the LANG_EN or LANG_FR global constants
	 * @param drugClassList A List of all distinct DrugClass instances in the database
	 * <p>Generates a list of LabelValueBean's containing the localized drug class descriptions and their ID, 
	 * and adds it to a Map keyed on the language constant</p>
	 */
	private void generateUniqueDrugClassMapForLanguage(String language, List<DrugClass> drugClassList) {
		ArrayList<LabelValueBean> uniqueListItems = new ArrayList<LabelValueBean>();
		String localizedDrugClass;
		
//		add the "SelectAll label"
		uniqueListItems.add(new LabelValueBean(getSelectAllLabel(language), "0"));
		
		//populate the rest of the list		
		for (DrugClass c : drugClassList){
			localizedDrugClass = "";
			if (language.equals(LANG_FR)) {
				localizedDrugClass = c.getDrugClassF();
			}else{
				localizedDrugClass = c.getDrugClassE();
			}
			uniqueListItems.add(new LabelValueBean(localizedDrugClass, c.getDrugClassId().toString()));
		}
		drugClassMap.put(language, uniqueListItems);
	}
	public int getDataTableServerProcessingThreshold() {
		return dataTablesProcessingToggleLevel;
	}
	public void setdataTablesProcessingToggleLevel(int dataTablesProcessingToggleLevel) {
		this.dataTablesProcessingToggleLevel = dataTablesProcessingToggleLevel;
	}
	
}
