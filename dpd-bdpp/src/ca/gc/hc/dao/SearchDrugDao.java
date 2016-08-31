/**
 * Name:  SearchDrugDao
 * Purpose:  Data Access Object to create, read, update object for table USER
 * Date: June 2006
 * Author: Diane Beauvais
 * 
 */
package ca.gc.hc.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.IntegerType;

import ca.gc.hc.bean.AjaxBean;
import ca.gc.hc.bean.AjaxBean.AjaxRequestStatus;
import ca.gc.hc.bean.DrugBean;
import ca.gc.hc.bean.DrugSummaryBean;
import ca.gc.hc.bean.SearchCriteriaBean;
import ca.gc.hc.model.AHFS;
import ca.gc.hc.model.ATC;
import ca.gc.hc.model.ActiveIngredients;
import ca.gc.hc.model.Company;
import ca.gc.hc.model.DrugClass;
import ca.gc.hc.model.DrugProduct;
import ca.gc.hc.model.DrugStatus;
import ca.gc.hc.model.ExternalStatus;
import ca.gc.hc.model.Form;
import ca.gc.hc.model.Packaging;
import ca.gc.hc.model.ProductMonograph;
import ca.gc.hc.model.Route;
import ca.gc.hc.model.Schedule;
import ca.gc.hc.model.Veterinary;
import ca.gc.hc.util.ApplicationGlobals;
import ca.gc.hc.util.DataTableColumn;
import ca.gc.hc.util.StringsUtil;

public class SearchDrugDao extends AbstractDao {
	/***************************************************************************
	 * Generic Constructor.
	 */
	public SearchDrugDao() {
		super(SearchCriteriaBean.class);
	}

	private final static String DUPLICATE_ROW_EXCEPTION_MSG = "than one row with the given identifier";
	private final static int ROUTE_CRITERIA = 1;
	private final static int DOSAGE_FORM_CRITERIA = 2;
	private final static int SCHEDULE_CRITERIA = 3;
	private final static int DRUG_CLASS_CRITERIA = 4;
	private final static int FOURTH_LEVEL_ATC_LENGTH = 5;
	private final static int FIFTH_LEVEL_ATC_LENGTH = 7;
	
	// SL/2009-10-05: positional constants used to load drug product instances
	// cf populateDrugProductSummaries() and basicDrugSummarySelect()
	private final static int DRUG_CODE = 0;
	private final static int DRUG_BRAND_E = 1;
	private final static int DRUG_BRAND_F = 2;
	private final static int DIN = 3;
	private final static int COMPANY_CODE = 4;
	private final static int DRUG_CLASS_E = 5;
	private final static int DRUG_CLASS_F = 6;
	private final static int NUMBER_OF_AIS = 7;
	private final static int AI_GROUP_NUMBER = 8;
	private final static int COMPANY_NAME = 9;
	private final static int EXT_STATUS_E = 10;
	private final static int EXT_STATUS_F = 11;
	private final static int STATUS_ID = 12;
	private final static int SCHEDULE_E = 13;
	private final static int SCHEDULE_F = 14;
	private final static int CLASS_CODE = 15;
	private final static int INGREDIENT_E = 16;
	private final static int INGREDIENT_F = 17;
	private final static int STRENGTH = 18;
	private final static int STRENGTH_UNIT_E = 19;
	private final static int STRENGTH_UNIT_F = 20;
	private final static int DOSAGE_VALUE = 21;
	private final static int DOSAGE_UNIT_E = 22;
	private final static int DOSAGE_UNIT_F = 23;
	private final static int PM_NAME_E = 24;
	private final static int PM_NAME_F = 25;
	
	// used for localized searches
	private final static String INGREDIENT_COLUMN = "ingredient";	
	private final static String ROUTE_COLUMN = "route_of_administration";
	private final static String FORM_COLUMN = "pharmaceutical_form";
	private final static String SCHEDULE_COLUMN = "schedule";
	private final static String BRAND_NAME_COLUMN = "brand_name";
	
	private final static String DRUG_CLASS_COLUMN = "class_code";
	
	private boolean isAjaxRequest = false;
	private AjaxBean ajaxBean = null;

	/**
	 * The local instance of the LOG4J Logger.
	 */

	private static org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(SearchDrugDao.class);

	public List SearchByCriteria(SearchCriteriaBean criteria,
			HttpServletRequest request) throws Exception {

		if (criteria.getDrugCode() != null
				&& criteria.getDrugCode().longValue() > 0) {
			return searchDrugByDrugCode(criteria.getDrugCode(), request);
		} else if (criteria.getDin() != null && criteria.getDin().length() > 0) {
			return searchDrugByDIN(criteria, request);
		} else if (criteria.getCompanyCode() != null
				&& criteria.getCompanyCode().longValue() > 0) {
			return searchDrugByCompanyCode(criteria, request);
		} else if (criteria.getAtc() != null && criteria.getAtc().length() > 0) {
			return searchDrugByATC(criteria, request);
		} else {
			return searchDrugByNames(criteria, request);
		}

	}

	private List searchDrugByDrugCode(Long drugCode, HttpServletRequest request)
			throws Exception {
		List val = new ArrayList();
		try {
			String query = "";

			query = "select drug.* from WQRY_DRUG_PRODUCT drug"
					+ " WHERE drug.DRUG_CODE = " + drugCode.longValue();

			List drugProducts = getSession().createSQLQuery(query).addEntity(
					"u", DrugProduct.class).list();

			log.debug("Search By drugCode Query is: " + query);

			if (drugProducts.size() > 0) {
				for (Iterator it = drugProducts.iterator(); it.hasNext();) {
					DrugBean drugInfo = new DrugBean();
					DrugProduct drug = (DrugProduct) it.next();
					// Compose the DrugBean Object
					drugInfo.setDrugProduct(drug);
					drugInfo.setActiveIngredientList(retrieveAIS(drug
							.getDrugCode()));
					drugInfo.setAhfsList(retrieveAHFS(drug.getDrugCode()));
					try {
						ATC current = retrieveAtcVO(drug.getDrugCode());

						/*
						 * SL/2012-03-23 ADR3291 and ADR1183: if the ATC is at
						 * the 5th level, display the fourth level ATC number
						 * and description instead.
						 */
						if (current != null
								&& current.getAtcNumber().length() == FIFTH_LEVEL_ATC_LENGTH) {
							try {
								String fourthLevelATCNumber = current
										.getAtcNumber().substring(0,
												FOURTH_LEVEL_ATC_LENGTH);
								ATC fourthLevelATC = retrieveAtcFourthLevelATC(fourthLevelATCNumber);
								if (fourthLevelATC != null
										&& StringsUtil.hasData(fourthLevelATC
												.getAtcE())) {
									current.setAtcE(fourthLevelATC.getAtcE());
									current.setAtcF(fourthLevelATC.getAtcF());
									current.setAtcNumber(fourthLevelATC
											.getAtcNumber());
								}
							} catch (Exception e) {
								/*
								 * If a 4th-level ATC cannot be obtained, the
								 * 5th-level ATC (current) will be displayed
								 * instead: no substitution
								 */
							}
						}
						drugInfo.setAtcVO(current);
					} catch (NullPointerException e) {
						// e.printStackTrace();
					}
					drugInfo.setFormList(retrieveForms(drug.getDrugCode()));
					drugInfo.setPackagingList(retrievePackagings(drug
							.getDrugCode()));
					drugInfo.setRouteList(retrieveRoutes(drug.getDrugCode()));
					drugInfo.setStatusVO(retrieveStatusVO(drug.getDrugCode()));
					drugInfo.setScheduleList(retrieveSchedules(drug
							.getDrugCode()));
					drugInfo.setCompanyVO(retrieveCompanyVO(drug
							.getCompanyCode()));
					drugInfo.setPmVO(retrievePM(drug.getDrugCode()));
					drugInfo.setVetSpecies(retrieveVetSpecies(drug
							.getDrugCode()));

					val.add(drugInfo);
				}
			}

		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
				return val;
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search Drug Product By drugCode [");
				message.append(drugCode);
				message.append("] failed");
				throw new Exception(message.toString());
			}
		}
		return val;
	}

	/***************************************************************************
	 * Gets the a list of Drugs by a DIN number Returns an empty List if none
	 * are found that match.
	 * 
	 * @param criteria  The current SearchCriteriaBean instance, to adapt the sorting
	 * clause where DataTable server processing is required
	 * @return the List of Drugs by a DIN number Updated and refactored
	 *         SL/2009-10-02 for additional fields returned
	 */
	private List searchDrugByDIN(SearchCriteriaBean criteria, HttpServletRequest request)
			throws Exception {
		List val = new ArrayList();
		try {
			String query = this.basicDrugSummarySelect(criteria);
			query = query + "and drug.DRUG_IDENTIFICATION_NUMBER = '" + criteria.getDin()
					+ "' " + localizedSummaryOrderByClause(criteria);

			List drugInfos = getSession().createSQLQuery(query).list();

			log.debug("Search By DIN Query is: " + query);

			val = this.populateBeans(drugInfos, request);

		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
				return val;
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search Drug Product By DIN [");
				message.append(criteria.getDin());
				message.append("] failed");
				throw new Exception(message.toString());
			}
		}
		return val;
	}

	/**
	 * @param drugInfos
	 *            Search results as a List of objects arrays
	 * @param request
	 *            The current request
	 * @return An ArrayList of either DrugBeans or DrugSummaryBeans, sorted on
	 *         brand names. If there is only one result, it will be searched
	 *         again but this time by drug code. This will return a
	 *         <strong>DrugBean</strong> suitable for the Product Information
	 *         page, whereas for multiple results, we need to return a list of
	 *         <strong>DrugSummaryBeans</strong>, suitable for the search
	 *         results page.
	 * @author SL/2012-07-19
	 */
	@SuppressWarnings("unchecked")
	private List populateBeans(List drugInfos, HttpServletRequest request) {
		List beans = new ArrayList();

		if (drugInfos.size() == 1) {
			Object[] drug = (Object[]) drugInfos.get(0);
			Long drugCode = Long.valueOf(drug[DRUG_CODE].toString());
			try {
				beans = searchDrugByDrugCode(drugCode, request);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (drugInfos.size() > 1) {
			beans = this.populateDrugProductSummaries(drugInfos, request);
		}
		return beans;
	}

	/**
	 * @author Sylvain LariviËre 2009-10-01
	 * @param drugCode
	 * @return the first active ingredient name (alphabetically) from the active
	 *         ingredients for the passed drug code, or the single AI name if
	 *         there is only one
	 */
	private ActiveIngredients retrieveFirstAI(Long drugCode) throws Exception {
		String query = "";
		ActiveIngredients result = new ActiveIngredients();

		try {
			query = "SELECT * FROM "
					+ "(select * from wqry_active_ingredients t where t.drug_code= "
					+ drugCode.toString() + " ORDER BY "
					+ localizedSearchColumnFor(INGREDIENT_COLUMN) + ")"
					+ " WHERE ROWNUM = 1"; // first row, in sorted order

			log.debug("Retrieving first active ingredient: " + query);

			result = (ActiveIngredients) getSession().createSQLQuery(query)
					.addEntity(ActiveIngredients.class).uniqueResult();

		} catch (HibernateException e) {
			log.error("Stack Trace: ", e);
			StringBuffer message = new StringBuffer("RetrieveFirstAI [");
			message.append(drugCode);
			message.append("] failed");
			throw new Exception(message.toString());
		}

		return result;
	}

	/***************************************************************************
	 * Gets the a list of Drugs by a Company Code Returns an empty List if none
	 * are found that match.
	 * 
	 * @param criteria  The current SearchCriteriaBean instance, to adapt the sorting
	 * clause where DataTable server processing is required
	 * @return the List of Drugs by a Company Code Updated SL/2009-10-14 for
	 *         additional criteria
	 */
	@SuppressWarnings("unchecked")
	private List searchDrugByCompanyCode(SearchCriteriaBean criteria,
			HttpServletRequest request) throws Exception {
		List val = new ArrayList();
		try {
			String queryString = buildSearchByCompanyCodeSql(criteria);

			List drugInfos = getSession().createSQLQuery(queryString).list();

			// Save the query for possible DataTable server processing
			request.getSession().setAttribute(ApplicationGlobals.SQL_QUERY,
					queryString);

			log.debug("Search By Company Code Query is: " + queryString);
			
			if (drugInfos.size() > 0) {
				val = this.populateBeans(drugInfos, request);
			}
		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
				return val;
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search Drug Product By Company [");
				message.append(criteria.getCompanyCode().toString());
				message.append("] Status [");
				message.append(criteria.getStatusCode() != null ? criteria
						.getStatusCode() : null);
				message.append("] failed");
				throw new Exception(message.toString());
			}
		}
		return val;
	}

	private String buildSearchByCompanyCodeSql(SearchCriteriaBean criteria) {
		String queryString;
		queryString = this.basicDrugSummarySelect(criteria)
				+ " and drug.COMPANY_CODE = "
				+ criteria.getCompanyCode().longValue();

		if (criteria.getStatusCode() != null) {
			queryString = queryString + " and st.status_code = "
						+ criteria.getStatusCode();
		}
		queryString = queryString + localizedSummaryOrderByClause(criteria);
		return queryString;
	}

	/***************************************************************************
	 * Gets the a list of Drugs by ATC Returns an empty List if none are found
	 * that match.
	 * 
	 * @param criteria  The current SearchCriteriaBean instance, to adapt the sorting
	 * clause where DataTable server processing is required
	 * @return the List of Drugs by a ATC
	 */
	private List searchDrugByATC(SearchCriteriaBean criteria, HttpServletRequest request)
			throws Exception {
		List val = new ArrayList();
		try {
			String sql = this.basicDrugSummarySelect(criteria);
			sql = sql + " and drug.DRUG_CODE in";
			sql = sql
					+ " (select distinct d.DRUG_CODE from WQRY_DRUG_PRODUCT d, WQRY_ATC atc";
			sql = sql + " where d.DRUG_CODE = atc.DRUG_CODE";
			sql = sql + " and atc.TC_ATC_NUMBER LIKE '" + criteria.getAtc().toUpperCase()
					+ "%'";
			sql = sql + ")" + localizedSummaryOrderByClause(criteria);

			Query query = getSession().createSQLQuery(sql);

			// Save the query for possible DataTable server processing
			request.getSession()
					.setAttribute(ApplicationGlobals.SQL_QUERY, sql);			

			List queryResults = query.list();			
			
			log.debug("Search by ATC Query is: " + sql);

			val = this.populateBeans(queryResults, request);

		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
				return val;
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search Drug Product By ATC [");
				message.append(criteria);
				message.append("] failed");
				throw new Exception(message.toString());
			}
		}
		return val;
	}

	/***************************************************************************
	 * Gets the a list of Drugs by a Compnay name Returns an empty List if none
	 * are found that match.
	 * 
	 * @param criteria
	 * @return the List of Drugs by a company name
	 */
	private List searchDrugByNames(SearchCriteriaBean criteria,
			HttpServletRequest request) throws Exception {
		List values = new ArrayList();
		try {
			String sql = buildSearchByNamesSql(criteria);
			Query query = getSession().createSQLQuery(sql);

			// Save the query for possible DataTable server processing
			request.getSession()
					.setAttribute(ApplicationGlobals.SQL_QUERY, sql);

			List queryResults = query.list();

			if (queryResults.size() > 0) {
				values = this.populateBeans(queryResults, request);
			}

		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
				return values;
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search Drug Product By ATC [");
				message.append(criteria.toString());
				message.append("] failed");
				throw new Exception(message.toString());
			}
		}

		return values;
	}

	/***************************************************************************
	 * Gets the a list of AIS Returns an empty List if none are found that
	 * match.
	 * 
	 * @param drugCode
	 * @return the List of AIS
	 */
	private List retrieveAIS(Long drugCode) throws Exception {
		List activeIngredients = null;
		try {

			String query = "select ais.* from WQRY_ACTIVE_INGREDIENTS ais ";
			query = query + "where ais.DRUG_CODE = " + drugCode.longValue();
			query = query + " ORDER BY "
					+ localizedSearchColumnFor(INGREDIENT_COLUMN)
					+ ", STRENGTH, STRENGTH_UNIT";
			activeIngredients = getSession().createSQLQuery(query).addEntity(
					"u", ActiveIngredients.class).list();

		} catch (HibernateException he) {
			log.error("Stack Trace: ", he);
			StringBuffer message = new StringBuffer("Search By drugCode [");
			message.append(drugCode);
			message.append("] failed");
			throw new Exception(message.toString());
		}
		return activeIngredients;
	}

	/***************************************************************************
	 * Gets the veterinary species from a drug code Returns null if none are
	 * found that match.
	 * 
	 * @param drugCode
	 * @return a String of the vet species
	 */
	private List retrieveVetSpecies(Long drugCode) throws Exception {
		List species = null;
		try {
			String query = "SELECT veterinary.* FROM WQRY_DRUG_VETERINARY_SPECIES veterinary WHERE veterinary.drug_code = "
					+ drugCode.longValue();
			species = getSession().createSQLQuery(query).addEntity(
					Veterinary.class).list();

		} catch (HibernateException he) {
			log.error("Stack Trace: ", he);
			StringBuffer message = new StringBuffer(
					"Retrieve Vet Species By drugCode [");
			message.append(drugCode);
			message.append("] failed");
			throw new Exception(message.toString());
		}
		return species;
	}

	/***************************************************************************
	 * Gets the a list of AHFS by drug code Returns an empty List if none are
	 * found that match.
	 * 
	 * @param drugCode
	 * @return the List of AHFS by drug code
	 */
	private List retrieveAHFS(Long drugCode) throws Exception {
		List ahfsInfos = null;
		try {
			String query = "select ahfs.* from WQRY_AHFS ahfs "
					+ "where ahfs.DRUG_CODE = " + drugCode.longValue();

			ahfsInfos = getSession().createSQLQuery(query).addEntity("u",
					AHFS.class).list();
			// addEntity("u", AHFS.class).setLockMode("u",
			// LockMode.READ).list();

		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
				return ahfsInfos;
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search AHFS By drugCode [");
				message.append(drugCode);
				message.append("] failed");
				throw new Exception(message.toString());
			}
		}
		return ahfsInfos;
	}

	/***************************************************************************
	 * Gets the a list of ATC by drug code Returns an empty List if none are
	 * found that match.
	 * 
	 * @param drugCode
	 * @return the List of ATC by drug code
	 */
	private ATC retrieveAtcVO(Long drugCode) throws Exception {
		ATC atc = new ATC();
		try {

			String query = "select atc.* from WQRY_ATC atc "
					+ "where atc.DRUG_CODE = " + drugCode.longValue();

			atc = (ATC) getSession().createSQLQuery(query).addEntity("u",
					ATC.class).uniqueResult();

		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
				return atc;
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search ATC By drugCode [");
				message.append(drugCode);
				message.append("] failed");
				throw new Exception(message.toString());
			}
		}
		return atc;
	}

	private ATC retrieveAtcFourthLevelATC(String atcFourthLevelNumber)
			throws Exception {
		ATC atc = new ATC();

		/*
		 * SL/2012-03-23 ADR1183: A new code table was added to contain all ATC
		 * codes, as opposed to only the codes currently used by products (in
		 * WQRY_ATC). This allows us to display the corresponding 4th-level code
		 * description for any 5th-level code, even when that 4th-level code is
		 * not currently used by any product in WQRY_ATC
		 */
		try {
			String query = "SELECT c.tc_atc_number, c.tc_atc_desc, c.tc_atc_desc_f "
					+ "FROM dpd_online_owner.WQRY_TC_FOR_ATC c "
					+ "where c.tc_atc_number = '" + atcFourthLevelNumber + "'";
			Object[] results = (Object[]) getSession().createSQLQuery(query)
					.uniqueResult();

			if (results.length > 0) {
				atc.setAtcNumber((String) results[0]);
				atc.setAtcE((String) results[1]);
				atc.setAtcF((String) results[2]);
			}
		} catch (Exception e) {
			throw new Exception(e.toString());
		}

		return atc;
	}

	/***************************************************************************
	 * Gets the a list of Company by drug code Returns an empty List if none are
	 * found that match.
	 * 
	 * @param companyCode
	 * @return the List of Company by drug code
	 */
	private Company retrieveCompanyVO(Long companyCode) throws Exception {
		Company company = null;
		try {
			String query = "select co.* from WQRY_COMPANIES  co "
					+ "where co.COMPANY_CODE = " + companyCode.longValue();

			company = (Company) getSession().createSQLQuery(query).addEntity(
					"u", Company.class).uniqueResult();

		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
				return company;
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search Company By companyCode [");
				message.append(companyCode);
				message.append("] failed");
				throw new Exception(message.toString());
			}
		}
		return company;
	}

	/***************************************************************************
	 * Gets the a list of Forms by drug code Returns an empty List if none are
	 * found that match.
	 * 
	 * @param drugCode
	 * @return the List of Forms by drug code
	 */
	private List retrieveForms(Long drugCode) throws Exception {
		List formInfos = new ArrayList();
		try {
			String query = "select forms.* from WQRY_FORM forms "
					+ "where forms.DRUG_CODE = " + drugCode.longValue();

			formInfos = getSession().createSQLQuery(query)
					.addEntity(Form.class).list();

		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
				return formInfos;
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search Dosage Form By drugCode [");
				message.append(drugCode);
				message.append("] failed");
				throw new Exception(message.toString());
			}
		}
		return formInfos;
	}

	/***************************************************************************
	 * Gets the a list of Packaging by drug code Returns an empty List if none
	 * are found that match.
	 * 
	 * @param drugCode
	 * @return the List of Packaging by drug code
	 */
	private List retrievePackagings(Long drugCode) throws Exception {
		List packageInfos = new ArrayList();
		try {
			String query = "select distinct pkgs.* from WQRY_PACKAGING pkgs ";
			query = query + "where pkgs.DRUG_CODE = " + drugCode.longValue();
			query = query + " order by pkgs.PACKAGE_SIZE asc";

			packageInfos = getSession().createSQLQuery(query).addEntity("u",
					Packaging.class).list();

		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
				return packageInfos;
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search Packaging By drugCode [");
				message.append(drugCode);
				message.append("] failed");
				throw new Exception(message.toString());
			}
		}
		return packageInfos;
	}

	/***************************************************************************
	 * Gets the a list of Routes by drug code Returns an empty List if none are
	 * found that match.
	 * 
	 * @param drugCode
	 * @return the List of Routes by drug code
	 */
	@SuppressWarnings("unchecked")
	private List<Route> retrieveRoutes(Long drugCode) throws Exception {
		List<Route> routeInfos = new ArrayList<Route>();
		try {
			String query = "select r.* " + "from WQRY_ROUTE r "
					+ "where r.DRUG_CODE = " + drugCode.longValue();

			routeInfos = getSession().createSQLQuery(query).addEntity(
					Route.class).list();
		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
				return routeInfos;
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search Route By drugCode [");
				message.append(drugCode);
				message.append("] failed");
				throw new Exception(message.toString());
			}
		}
		return routeInfos;
	}

	/**
	 * Retrieves a map of unique routes of administration
	 * 
	 * @return HashMap of distinct routes, per language
	 * @throws Exception
	 * @author Sylvain LariviËre, 2009-09-10 Updated 2009-10-26 to account for
	 *         English and French lists Updated 2012-06-07 to uncomment French
	 *         lists implementation now that content is translated
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, List<String>> retrieveUniqueRoutes()
			throws Exception {
		List<String> englishRoutes = new ArrayList<String>();
		List<String> frenchRoutes = new ArrayList<String>();
		HashMap<String, List<String>> allRoutes = new HashMap<String, List<String>>();

		try {
			String query = "select distinct(r.route_of_administration) "
					+ "from wqry_route r " + "where r.inactive_date is null "
					+ "order by r.route_of_administration";
			englishRoutes = (List) getSession().createSQLQuery(query).list();
			allRoutes.put(ApplicationGlobals.LANG_EN, englishRoutes);

			query = "select distinct(r.route_of_administration_f) "
					+ "from wqry_route r " + "where r.inactive_date is null "
					+ "order by r.route_of_administration_f";
			frenchRoutes = getSession().createSQLQuery(query).list();
			allRoutes.put(ApplicationGlobals.LANG_FR, frenchRoutes);
		} catch (HibernateException he) {
			log.error("Stack Trace: ", he);
			StringBuffer message = new StringBuffer("retrieveUniqueRoutes [");
			message.append("] failed");
			throw new Exception(message.toString());
		}

		return allRoutes;
	}

	/***************************************************************************
	 * Gets the a list of Schedules by drug code Returns an empty List if none
	 * are found that match.
	 * 
	 * @param drugCode
	 * @return the List of Schedules by drug code
	 */
	private List retrieveSchedules(Long drugCode) throws Exception {
		List values = new ArrayList();
		try {
			String query = "select schedules.* from WQRY_SCHEDULE schedules "
					+ "where schedules.DRUG_CODE = " + drugCode.longValue();
			values = getSession().createSQLQuery(query).addEntity(
					Schedule.class).list();

		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
				return values;
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search Schedule By drugCode [");
				message.append(drugCode);
				message.append("] failed");
				throw new Exception(message.toString());
			}
		}
		return values;
	}

	/***************************************************************************
	 * Gets a Drug Status record Returns an empty record.
	 * 
	 * @param drugCode
	 * @return the Drug Status Record
	 */
	private DrugStatus retrieveStatusVO(Long drugCode) throws Exception {
		DrugStatus values = null;
		try {
			String query = "select status.* from WQRY_STATUS  status "
					+ "where status.DRUG_CODE = " + drugCode.longValue();

			values = (DrugStatus) getSession().createSQLQuery(query).addEntity(
					"u", DrugStatus.class).uniqueResult();

		} catch (HibernateException he) {
			if (he.getMessage().indexOf(
					"More than one row with the given identifier was found") > 0) {
				log.warn("Data Problem:", he);
				return values;
			} else {
				if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
					log.warn("Data Problem: " + he.getMessage());
					return values;
				} else {
					log.error("Stack Trace: ", he);
					StringBuffer message = new StringBuffer(
							"Search Status By drugCode [");
					message.append(drugCode);
					message.append("] failed");
					throw new Exception(message.toString());
				}
			}
		}
		return values;
	}

	/***************************************************************************
	 * Gets the a productMonograph by drug code Returns an empty List if none
	 * are found that match.
	 * 
	 * @param drugCode
	 * @return the product monograph by drug code
	 * SL/2015-06-22: No longer restricted to "Marketed" status
	 */
	public ProductMonograph retrievePM(Long drugCode) throws Exception {
		ProductMonograph productMonograph = new ProductMonograph();
		try {

			String query = "select pm.* from WQRY_PM_DRUG pm, WQRY_STATUS c"
					+ " where pm.DRUG_CODE = "
					+ drugCode.longValue()
					+ " and pm.PM_DATE = (select max(a.PM_DATE) from WQRY_PM_DRUG a "
					+ " where a.DRUG_CODE = "
					+ drugCode.longValue()
					+ " and a.PM_VER_NUMBER = (select max(b.PM_VER_NUMBER)"
					+ " from WQRY_PM_DRUG b "
					+ " where b.DRUG_CODE = "
					+ drugCode.longValue()
					+ " and b.pm_date = (select max(a.PM_DATE) from WQRY_PM_DRUG a "
					+ " where a.DRUG_CODE = " + drugCode.longValue() + ")))"
					+ " and c.DRUG_CODE = " + drugCode.longValue();

			productMonograph = (ProductMonograph) getSession().createSQLQuery(
					query).addEntity("u", ProductMonograph.class)
					.uniqueResult();

			log.debug("Search for Product Monograph By drugCode Query is: "
					+ query);

		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
				return productMonograph;
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search Product Monograph By drugCode [");
				message.append(drugCode);
				message.append("] failed");
				throw new Exception(message.toString());
			}
		}
		return productMonograph;
	}

	// SL/2013-02-13: Added Translate statements to sql to allow
	// accent-insensitive free-text searches
	private String buildSearchByNamesSql(SearchCriteriaBean criteria)
			throws HibernateException {

		StringBuffer query = new StringBuffer(basicDrugSummarySelect(criteria));
		query.append(basicDrugSummaryFromClause(criteria));
		query.append(basicDrugSummaryWhereClause(criteria));
		query.append(CriteriaDrugSummaryWhereClause(criteria));
		query.append(localizedSummaryOrderByClause(criteria));

		log.debug("Search by Names Query is: " + query);
		
		return query.toString();
	}

	private String CriteriaDrugSummaryWhereClause(SearchCriteriaBean criteria) {
		String wherePortion = ""; // " WHERE drug.drug_code = status.drug_code";

		if (criteria.getStatusCode() != null) {
			if (!criteria.getStatusCode().equals("0")) { // not Select ALL
				wherePortion += " and ste.EXTERNAL_STATUS_CODE = "
						+ criteria.getStatusCode();
			}
		}

		if (criteria.getCompanyName() != null
				&& criteria.getCompanyName().length() > 0) {
			// replace space with wildcard to be able to search for multiple
			// words
			String strCompanyName = criteria.getCompanyName().replace(" ", "%");
			// wherePortion += (" AND UPPER(co.COMPANY_NAME) LIKE '%" +
			// strCompanyName.trim().toUpperCase() + "%'");
			wherePortion += " and drug.DRUG_CODE in (select drug_code from wqry_drug_product where company_code "
						+ "in (select company_code from WQRY_COMPANIES co "
							+ " where translate(upper(co.COMPANY_NAME),'¿¬ƒ«»…À ÃŒœ“‘÷Ÿ⁄€‹','AAACEEEEIIIOOOUUUU') like '%"
					+ StringsUtil.AsUnAccentedUppercase(strCompanyName.trim())
					+ "%'))";

		}

		if (criteria.getAigNumber() != null
				&& criteria.getAigNumber().length() > 0) {
			wherePortion += " AND drug.AI_GROUP_NO = '"
					+ criteria.getAigNumber() + "'";
		}

		if (criteria.getBrandName() != null
				&& criteria.getBrandName().length() > 0) {
			// replace space with wildcard to be able to search for multiple
			// words
			String strBrandName = criteria.getBrandName().replace(" ", "%");
			// SL/2013-02-05 ADR0106 - search on both English and French brands,
			// accent- and case-insensitive
			// wherePortion += " AND ((Upper(BRAND_NAME) like '%" +
			// strBrandName.trim().toUpperCase() + "%')"
			// + " or (upper(BRAND_NAME_F) like '%" +
			// strBrandName.trim().toUpperCase() + "%'))";
			wherePortion += " and ((translate(upper(BRAND_NAME),'¿¬ƒ«»…À ÃŒœ“‘÷Ÿ⁄€‹','AAACEEEEIIIOOOUUUU') like '%"
					+ StringsUtil.AsUnAccentedUppercase(strBrandName.trim())
					+ "%')"
					+ " or (translate(upper(BRAND_NAME_F),'¿¬ƒ«»…À ÃŒœ“‘÷Ÿ⁄€‹','AAACEEEEIIIOOOUUUU') like '%"
					+ StringsUtil.AsUnAccentedUppercase(strBrandName.trim())
					+ "%'))";
		}

		if (criteria.getActiveIngredient() != null
				&& criteria.getActiveIngredient().length() > 0) {
			String strActiveIngredient = criteria.getActiveIngredient().trim()
					.toUpperCase();

			int andPosition = -1;
			int orPosition = -1;

			String ingredient = "";

			// break up on the AND first
			// SL/2013-02-18 Bug fix: was treating the beginning of 'original'
			// as an operator
			andPosition = strActiveIngredient.indexOf(" AND ");
			orPosition = strActiveIngredient.indexOf(" OR ");

			wherePortion += " AND drug.drug_code IN (select DISTINCT drug_code from wqry_active_ingredients "
					// + " WHERE upper(" +
					// localizedSearchColumnFor(INGREDIENT_COLUMN) +
					// ") LIKE '%";
					+ " WHERE translate(upper("
					+ localizedSearchColumnFor(INGREDIENT_COLUMN)
					+ "),'¿¬ƒ«»…À ÃŒœ“‘÷Ÿ⁄€‹','AAACEEEEIIIOOOUUUU') like '%";

			if (andPosition != -1) {
				// there is an "AND" found in the string
				int pos = -1;

				Boolean andFound = false;

				while (andPosition > 0) {
					// Is the next operator an "AND" or an "OR"?

					if (orPosition == -1) {
						pos = andPosition;
						andFound = true;
					} else if (andPosition < orPosition && andPosition != -1) {
						pos = andPosition;
						andFound = true;
					} else {
						pos = orPosition;
						andFound = false;
					}

					ingredient = strActiveIngredient.substring(0, pos);

					// wherePortion += ingredient + "%'";
					wherePortion += StringsUtil
							.AsUnAccentedUppercase(ingredient)
							+ "%'";

					if (andFound) {
						wherePortion += ")";
						wherePortion += " AND drug.drug_code IN (select DISTINCT drug_code from wqry_active_ingredients WHERE "
								// + "upper(" +
								// localizedSearchColumnFor(INGREDIENT_COLUMN) +
								// ") LIKE '%";
								+ "translate(upper("
								+ localizedSearchColumnFor(INGREDIENT_COLUMN)
								+ "),'¿¬ƒ«»…À ÃŒœ“‘÷Ÿ⁄€‹','AAACEEEEIIIOOOUUUU') like '%";

						strActiveIngredient = strActiveIngredient
								.substring(pos + 5);
					} else {
						// wherePortion += " OR upper(" +
						// localizedSearchColumnFor(INGREDIENT_COLUMN) +
						// ") LIKE '%";
						wherePortion += " OR translate(upper("
								+ localizedSearchColumnFor(INGREDIENT_COLUMN)
								+ "),'¿¬ƒ«»…À ÃŒœ“‘÷Ÿ⁄€‹','AAACEEEEIIIOOOUUUU') like '%";

						strActiveIngredient = strActiveIngredient
								.substring(pos + 4);
					}
					//					
					orPosition = strActiveIngredient.indexOf(" OR ");
					andPosition = strActiveIngredient.indexOf(" AND ");
				}
			}

			// no "AND" found - just extract the "OR"
			String orPortion = "";

			orPosition = strActiveIngredient.indexOf(" OR ");

			if (orPosition != -1) {
				while (orPosition > 0) {
					ingredient = strActiveIngredient.substring(0, orPosition);

					// orPortion += ingredient + "%'";
					orPortion += StringsUtil.AsUnAccentedUppercase(ingredient)
							+ "%'";

					strActiveIngredient = strActiveIngredient
							.substring(orPosition + 4);
					orPosition = strActiveIngredient.indexOf(" OR ");

					// orPortion += " OR upper(" +
					// localizedSearchColumnFor(INGREDIENT_COLUMN) +
					// ") LIKE '%";
					orPortion += " OR translate(upper("
							+ localizedSearchColumnFor(INGREDIENT_COLUMN)
							+ "),'¿¬ƒ«»…À ÃŒœ“‘÷Ÿ⁄€‹','AAACEEEEIIIOOOUUUU') like '%";
				}
			}

			// orPortion += strActiveIngredient + "%'";
			orPortion += StringsUtil.AsUnAccentedUppercase(strActiveIngredient)
					+ "%'";

			wherePortion += orPortion + ")";
		}

		if (criteria.getRoute() != null && criteria.getRoute().length > 0) {
			// Updated to include multiple user-selected routes and refactored
			// SL/2009-09-09
			// Updated to include drug class SL/2014-10-17
			wherePortion = includeSelectedCriteriaItems(criteria.getRoute(),
					wherePortion, ROUTE_CRITERIA);
		}

		if (criteria.getDosage() != null && criteria.getDosage().length > 0) {
			wherePortion = includeSelectedCriteriaItems(criteria.getDosage(),
					wherePortion, DOSAGE_FORM_CRITERIA);
		}

		if (criteria.getSchedule() != null && criteria.getSchedule().length > 0) {
			wherePortion = includeSelectedCriteriaItems(criteria.getSchedule(),
					wherePortion, SCHEDULE_CRITERIA);
		}

		if (criteria.getDrugClass() != null
				&& criteria.getDrugClass().length > 0) {
			wherePortion = includeSelectedCriteriaItems(
					criteria.getDrugClass(), wherePortion, DRUG_CLASS_CRITERIA);

		}
		return wherePortion;
	}

	private String includeSelectedCriteriaItems(String[] items,
			String whereClause, int criteriaType) {
		String formsQuery = "";
		String tableName = "";
		String fieldname = "";
		boolean isVarcharColumn;

		if (items[0].equals("0") && items.length == 1) { //"Select all" could be included with other items)
			log.debug("select all selected");
		} else {
			switch (criteriaType) {
			case ROUTE_CRITERIA:
				tableName = "WQRY_ROUTE";
				fieldname = localizedSearchColumnFor(ROUTE_COLUMN);
				break;
			case DOSAGE_FORM_CRITERIA:
				tableName = "WQRY_FORM";
				fieldname = localizedSearchColumnFor(FORM_COLUMN);
				break;
			case SCHEDULE_CRITERIA:
				tableName = "WQRY_SCHEDULE";
				fieldname = localizedSearchColumnFor(SCHEDULE_COLUMN);
				break;
			case DRUG_CLASS_CRITERIA:
				tableName = "WQRY_DRUG_PRODUCT";
				fieldname = DRUG_CLASS_COLUMN;
				break;
			}
			isVarcharColumn = criteriaType != DRUG_CLASS_CRITERIA;

			// collect all items in the criteria, comma-separated
			formsQuery = " and drug.drug_code in (Select t.drug_code From "
					+ tableName + " t " + "Where t." + fieldname + " in ("
					+ stringArrayToString(items, isVarcharColumn) + "))";
		}
		whereClause += formsQuery;

		return whereClause;
	}

	/**
	 * Retrieves a map of unique pharmaceutical forms
	 * 
	 * @return HashMap of distinct pharmaceutical forms, per language
	 * @throws Exception
	 * @author Sylvain LariviËre, 2009-09-10 <br/>
	 *         Updated 2009-10-26 to account for English and French lists.
	 *         Updated 2012-06-07 to uncomment French lists implementation now
	 *         that content is translated
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, List<String>> retrieveUniqueForms() throws Exception {
		List<String> englishForms = new ArrayList<String>();
		List<String> frenchForms = new ArrayList<String>();
		HashMap<String, List<String>> allForms = new HashMap<String, List<String>>();

		try {
			String query = "select distinct(f.pharmaceutical_form) "
					+ "from wqry_form f " + "where f.inactive_date is null "
					+ "order by f.pharmaceutical_form";
			englishForms = (List) getSession().createSQLQuery(query).list();
			allForms.put(ApplicationGlobals.LANG_EN, englishForms);

			query = "select distinct(f.pharmaceutical_form_f) "
					+ "from wqry_form f " + "where f.inactive_date is null "
					+ "order by f.pharmaceutical_form_f";
			frenchForms = (List) getSession().createSQLQuery(query).list();
			allForms.put(ApplicationGlobals.LANG_FR, frenchForms);

		} catch (HibernateException he) {
			log.error("Stack Trace: ", he);
			StringBuffer message = new StringBuffer(
					"Search Unique Pharmaceutical Forms [");
			message.append("] failed");
			throw new Exception(message.toString());
		}
		return allForms;
	}

	/**
	 * Retrieves a map of unique schedules
	 * 
	 * @return HashMap of distinct schedules
	 * @throws Exception
	 * @author Sylvain LariviËre, 2009-09-10 Updated 2009-10-26 to account for
	 *         English and French lists Updated 2012-06-07 to uncomment French
	 *         lists implementation now that content is translated
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, List<String>> retrieveUniqueSchedules()
			throws Exception {
		List<String> englishSchedules = new ArrayList<String>();
		List<String> frenchSchedules = new ArrayList<String>();
		HashMap<String, List<String>> allSchedules = new HashMap<String, List<String>>();

		try {
			String query = "select distinct(sc.schedule) "
					+ "from wqry_schedule sc "
					+ "where sc.inactive_date is null "
					+ "order by sc.schedule";
			englishSchedules = (List) getSession().createSQLQuery(query).list();
			allSchedules.put(ApplicationGlobals.LANG_EN, englishSchedules);

			query = "select distinct(sc.schedule_f) "
					+ "from wqry_schedule sc "
					+ "where sc.inactive_date is null "
					+ "order by sc.schedule_f";
			frenchSchedules = getSession().createSQLQuery(query).list();
			allSchedules.put(ApplicationGlobals.LANG_FR, frenchSchedules);

		} catch (HibernateException he) {
			log.error("Stack Trace: ", he);
			StringBuffer message = new StringBuffer(
					"Search Unique Pharmaceutical Forms [");
			message.append("] failed");
			throw new Exception(message.toString());
		}
		return allSchedules;
	}

	/**
	 * Retrieves a List of unique DrugClass objects
	 * 
	 * @throws Exception
	 * @author Sylvain LariviËre, 2010-10-15
	 */
	@SuppressWarnings("unchecked")
	public List<DrugClass> retrieveUniqueDrugClasses() throws Exception {
		List<DrugClass> allClasses = new ArrayList<DrugClass>();
		List classList = new ArrayList();

		try {
			String query = "select distinct class_code, CLASS, CLASS_F "
				+ "from WQRY_DRUG_PRODUCT where class_code is not null " + "order by "
					+ localizedSearchColumnFor("class");

			log.debug("Retrieving unique drug classes :" + query);

			classList = (List) getSession().createSQLQuery(query).list();

			Iterator it = classList.iterator();
			while (it.hasNext()) {
				Object[] row = (Object[]) it.next();
				DrugClass drugClass = new DrugClass();
				drugClass.setDrugClassId(Long.valueOf(row[0].toString()));
				drugClass.setDrugClassE(row[1].toString());
				drugClass.setDrugClassF(row[2].toString());
				allClasses.add(drugClass);
			}
		} catch (HibernateException he) {
			log.error("Stack Trace: ", he);
			StringBuffer message = new StringBuffer(
					"retrieveUniqueDrugClasses [");
			message.append("] failed");
			throw new Exception(message.toString());
		}
		return allClasses;
	}

	private String stringArrayToString(String[] anAra, boolean isVarcharColumn) {
		String string = "";

		for (int i = 0; i < anAra.length; i++) {
			if(!"0".equals(anAra[i])) { //disregard 0 as it corresponds to "Select All" and was multi-selected in
				String apostrophe = isVarcharColumn ? "'" : "";
				// collect all items in the criteria array, comma-separated
				string += apostrophe + anAra[i].concat(apostrophe).concat(", ");
			}
		}
		string = string.substring(0, string.length() - 2); // remove last comma
		return string;
	}

	/**
	 * @see basicDrugSummarySelect()
	 * @return A list of DrugSummaryBean's used to populate DrugProduct search
	 *         result summaries. Updated SL/2012-06-07 for French lists
	 *         implementation now that content is translated
	 * @author Sylvain LariviËre 2009-10-05
	 */
	/*
	 * NOTE that the list constants below match the order of fields of the
	 * SELECT clause in basicDrugSummarySelect()
	 */
	private List populateDrugProductSummaries(List drugData,
			HttpServletRequest request) throws NumberFormatException {
		List<DrugSummaryBean> val = new ArrayList<DrugSummaryBean>();
		String pmE = "";
		String pmF = "";
		Long drugCode = new Long(0);

		try {
			for (Iterator it = drugData.iterator(); it.hasNext();) {
				Object[] list = (Object[]) it.next();
				DrugProduct drug = new DrugProduct();

				drugCode = Long.valueOf(list[DRUG_CODE].toString());
				drug.setDrugCode(drugCode);
				drug.setBrandNameE((String) list[DRUG_BRAND_E]);
				drug.setBrandNameF((String) list[DRUG_BRAND_F]);
				drug.setDrugIdentificationNumber((String) list[DIN]);
				drug.setCompanyCode(Long.valueOf(list[COMPANY_CODE].toString()));
				drug.setDrugClassE((String) list[DRUG_CLASS_E]);
				drug.setDrugClassF((String) list[DRUG_CLASS_F]);
				drug.setNumberOfAis(Long
						.valueOf(list[NUMBER_OF_AIS].toString()));
				drug.setAiGroupNo((String) list[AI_GROUP_NUMBER]);
				drug.setClassCode(Long
						.valueOf(list[CLASS_CODE].toString()));
				
				pmE=StringsUtil.emptyForNull((String) list[PM_NAME_E]);
				pmF=StringsUtil.emptyForNull((String) list[PM_NAME_F]);

				String company = (String) list[COMPANY_NAME];
				String statusE = (String) list[EXT_STATUS_E];
				String statusF = (String) list[EXT_STATUS_F];
				Long statusID = Long.valueOf(list[STATUS_ID].toString());
				ExternalStatus extStatus = new ExternalStatus();
				extStatus.setExternalStatusE(statusE);
				extStatus.setExternalStatusF(statusF);
				DrugStatus status = new DrugStatus();
				status.setStatusID(statusID);
				status.setExternalStatus(extStatus);
				String scheduleE = (String) list[SCHEDULE_E];
				String scheduleF = (String) list[SCHEDULE_F];
				Schedule schedule = new Schedule(scheduleE, scheduleF);
				
				ActiveIngredients firstAI = new ActiveIngredients();
				firstAI.setDosageUnitE((String) list[DOSAGE_UNIT_E]);
				firstAI.setDosageUnitF((String) list[DOSAGE_UNIT_F]);
				firstAI.setDosageValue((String) list[DOSAGE_VALUE]);
				firstAI.setDrugCode(drugCode);
				firstAI.setIngredientE((String) list[INGREDIENT_E]);
				firstAI.setIngredientF((String) list[INGREDIENT_F]);
				firstAI.setStrength((String) list[STRENGTH]);
				firstAI.setStrengthUnitE((String) list[STRENGTH_UNIT_E]);
				firstAI.setStrengthUnitF((String) list[STRENGTH_UNIT_F]);

				DrugSummaryBean bean = new DrugSummaryBean(drug, company,
						status, pmE, pmF, schedule, firstAI);

				val.add(bean);
			}

		} catch (NumberFormatException n) {
			log.error("Stack Trace: ", n);
			StringBuffer message = new StringBuffer(
					"Populate Drug Products failed");
			throw new NumberFormatException(message.toString());
		} catch (Exception e) {
			log.error("Stack Trace: ", e);
			StringBuffer message = new StringBuffer("Populate Drug Products [");
			message.append("] failed");
		}

		return val;
	}
	
	/**
	 * @author Sylvain LariviËre 2009-10-05, 2016-08-22
	 * @param criteria
	 *            The current SearchCriteriaBean instance.
	 * @return The basic Select statement common to all returned DrugProduct
	 *         summaries
	 *         <p>
	 *         Updated 2012-06-07 to uncomment French lists implementation now
	 *         that content is translated, and 2016-08-22 to implement DataTable
	 *         Server-side processing.
	 *         </p>
	 * @see populateDrugProductSummaries
	 */
	/*
	 * NOTE that the order of fields in the SELECT clause below, except for the
	 * localizedSummaryBrandNameSortingClause, MUST match the class-level
	 * positional constants in the class declarations
	 */
	private String basicDrugSummarySelect(SearchCriteriaBean criteria) {
		StringBuffer query = new StringBuffer();
		query.append("select distinct drug.drug_code, drug.brand_name, drug.brand_name_F, ");
		query.append("drug.drug_identification_number, drug.company_code, ");
		query.append("drug.class, drug.class_f, drug.number_of_ais, drug.ai_group_no, ");
		query.append("co.COMPANY_NAME, ste.EXTERNAL_STATUS_ENGLISH, ste.EXTERNAL_STATUS_FRENCH, ste.EXTERNAL_STATUS_CODE, ");
		query.append("s.schedule, s.schedule_f, drug.class_code, ");
		query.append("i.ingredient, i.ingredient_f, i.strength, i.strength_unit, i.strength_unit_f, ");
		query.append("i.DOSAGE_VALUE, i.DOSAGE_UNIT, i.DOSAGE_UNIT_F, ");
		query.append("pm.PM_ENGLISH_FNAME, pm.PM_FRENCH_FNAME ");
		query.append(localizedSummaryBrandNameSortingClause(criteria));

		return query.toString();
	}
	
	private String basicDrugSummarySelectForCountOnly(SearchCriteriaBean criteria) {
		StringBuffer query = new StringBuffer("select distinct count(*) ");

		return query.toString();
	}

	private String basicDrugSummaryWhereClause(SearchCriteriaBean criteria) {
		StringBuffer buf = new StringBuffer();
		buf.append("where drug.DRUG_CODE = st.DRUG_CODE ");
		buf.append("and st.EXTERNAL_STATUS_CODE = ste.EXTERNAL_STATUS_CODE ");
		buf.append("and drug.COMPANY_CODE = co.COMPANY_CODE ");
		buf.append("and drug.drug_code = s.drug_code ");
		buf.append("and drug.drug_code = r.drug_code ");
		buf.append("and drug.drug_code = f.drug_code ");
		buf.append("and drug.drug_code = i.drug_code(+) ");
		buf.append("and drug.drug_code = pm.DRUG_CODE(+) ");
		buf.append("and i.id = ");
		buf.append("(select min(id) from wqry_active_ingredients i where drug.drug_code = i.drug_code) ");
		
		return buf.toString();
	}

	private String basicDrugSummaryFromClause(SearchCriteriaBean criteria) {
		StringBuffer buf = new StringBuffer();
		
		buf.append("from WQRY_DRUG_PRODUCT drug, WQRY_COMPANIES co, WQRY_STATUS st, ");
		buf.append("WQRY_STATUS_EXTERNAL ste, wqry_route r, wqry_form f, wqry_schedule s, ");
		buf.append("wqry_active_ingredients i, WQRY_PM_DRUG pm ");
		
		return buf.toString();
	}

	/**
	 * @param columnName
	 *            The English sorting column
	 * @return A String representing the appropriate version of the sorting
	 *         column based on the user language. Simply adds "_f" to the passed
	 *         column name if the user language is French. Based on database
	 *         naming convention.
	 */
	private String localizedSearchColumnFor(String columnName) {
		String result = "";
		Boolean isFrench = ApplicationGlobals.LANG_FR.equals(ApplicationGlobals
				.instance().getUserLocale().getLanguage());

		if (isFrench) {
			result = columnName + "_f";
		} else {
			result = columnName;
		}
		return result;
	}

	/**
	 * @return A String containing an SQL case statement to use in the standard
	 *         drug summary search in order to sort brand names by either the
	 *         value in the user language if it exists, or else by the
	 *         corresponding value in the other official language.
	 *         <p>
	 *         Updated to handle DataTable server-side processing, including
	 *         sorting on other columns. In this case, the user language cannot
	 *         change between requests, and so no language checking is required.
	 *         </p>
	 * @author Sylvain LariviËre 2013-02-07, 2016-08-11
	 * @param criteria
	 *            The current SearchCriteriaBean instance.
	 */
	private String localizedSummaryBrandNameSortingClause(
			SearchCriteriaBean criteria) {
		String result = "";

		if (isAjaxRequest
				&& !ajaxBean.getColumnOrderMap().containsKey(new Integer(
						ApplicationGlobals.DATA_TABLE_BRAND_NAME_COLUMN))) {
			/*
			 * User changed the sorting order or the sorting column. Unless this
			 * is an Ajax request that includes the brand name column as a sort
			 * column, do nothing: there should be no SORT_COLUMN pseudo-column.
			 * 
			 * But if the brand name column IS included, either by itself or
			 * with other columns, then do include the SORT_COLUMN. It will be
			 * used in the ORDER BY clause.
			 */
		} else {
			/*
			 * Always sort by brand name, ascending, and DIN: therefore, this
			 * CASE clause needs to be added
			 */
			Boolean isFrench = ApplicationGlobals.LANG_FR
					.equals(ApplicationGlobals.instance().getUserLocale()
							.getLanguage());
			if (isFrench) {
				result = ", CASE WHEN DRUG.BRAND_NAME_F IS NOT NULL THEN UPPER(DRUG.BRAND_NAME_F)"
						+ " WHEN DRUG.BRAND_NAME IS NOT NULL THEN upper(DRUG.BRAND_NAME)"
						+ " ELSE NULL END AS SORT_COLUMN ";
			} else {
				result = ", CASE WHEN DRUG.BRAND_NAME IS NOT NULL THEN UPPER(DRUG.BRAND_NAME)"
						+ " WHEN DRUG.BRAND_NAME_F IS NOT NULL THEN upper(DRUG.BRAND_NAME_F)"
						+ " ELSE NULL END AS SORT_COLUMN ";
			}
		}
		return result;
	}

	/**
	 * @return A String containing an SQL order by clause that is adapted to the
	 *         user language for the Search Results Summary Page, where all data
	 *         is sorted on the brand name column of the table. English and
	 *         French brand names are sorted together, in a case- and
	 *         accent-insensitive order.
	 * @see localizedSummaryBrandNameSortingClause
	 * @author Sylvain LariviËre 2013-01-08 Updated SL/2013-02-14 to sort case-
	 *         and accent-insensitively, SL/2016-08-19 use the sort column
	 *         indexes and related sort direction to support DataTable server
	 *         processing (multi-sorting is possible)
	 * @param criteria
	 *            The current SearchCriteriaBean instance.
	 */
	private String localizedSummaryOrderByClause(SearchCriteriaBean criteria) {
		StringBuffer result = new StringBuffer(" ORDER BY ");
		String translateClause = "translate(SORT_COLUMN,'¿¬ƒ«»…À ÃŒœ“‘÷Ÿ⁄€‹','AAACEEEEIIIOOOUUUU')";

		if (isAjaxRequest) {
			List<DataTableColumn> sortCols = ajaxBean.getSortColumns();
			Map<Integer, String> colOrdering = ajaxBean.getColumnOrderMap();
			
			for (DataTableColumn col : sortCols) {
				int index = col.getColumnIndex();
				/*
				 * If the entry key matches the brand name column index, apply the case- and
				 * accent- insensitivity; the entry value is the sort direction.
				 */
				if (Integer.parseInt(ApplicationGlobals.DATA_TABLE_BRAND_NAME_COLUMN) == index) {
					result.append(translateClause);
				}else{
					//sort by the field name matching the DataTable column we are sorting on
					result.append(col.getFieldName() + " ");
				}
				result.append(colOrdering.get(Integer.valueOf(index)) + ",");
			}
			// remove the last comma
			result.deleteCharAt(result.length()-1);
		} else {
			// initial jsp page sorting
			result.append(translateClause);
			result.append(", DRUG.DRUG_IDENTIFICATION_NUMBER");
		}
		return result.toString();
	}
	
	@SuppressWarnings("unchecked")
	public List<ExternalStatus> retrieveAllExternalStatuses() throws Exception {
		List<ExternalStatus> statusList = new ArrayList<ExternalStatus>();
		//PFIX ADR0144-1516: only return external statuses that are actually used by a product
		statusList =getSession().createQuery("select distinct ext from DrugStatus s inner join s.externalStatus ext").list();
		
		return statusList;
	}

	/**
	 * @param criteria
	 *            The current SearchCriteriaBean
	 * @param request
	 *            The current request
	 * @return A List of DrugSummaryBean's that is suitable for the current
	 *         DataTable Ajax request
	 * @throws Exception
	 * @author SL/2016-08-22
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getNextResults(SearchCriteriaBean criteria,
			HttpServletRequest request) throws Exception {
		String sql = "";
		List values = new ArrayList();

		if ((ajaxBean.getAjaxStatus().equals(AjaxRequestStatus.INACTIVE) || !ajaxBean
				.isUpdatingPageNumber()) || ajaxBean.isUpdatingSorting()) {
			/*
			 * No usable query is available since only a count was obtained so
			 * far. Generate the query sql.
			 * 
			 * Currently, only searching by names or company code could possibly
			 * generate a number of search results that is above the DataTable
			 * server-processing threshold. Hence excluding other types of
			 * searches.
			 */
			if (criteria.getCompanyCode() != null
					&& criteria.getCompanyCode().longValue() > 0) {
				sql = buildSearchByCompanyCodeSql(criteria);
			} else {
				sql = buildSearchByNamesSql(criteria);
			}

		} else if (ajaxBean.isUpdatingPageNumber()) {
			// stored sql may be used: search unchanged
			sql = (String) request.getSession().getAttribute(
					ApplicationGlobals.SQL_QUERY);
		} else if (ajaxBean.isUpdatingSorting()) {
			// re-query with new sort order
		}

		try {
			Query query = getSession().createSQLQuery(sql);
			request.getSession()
					.setAttribute(ApplicationGlobals.SQL_QUERY, sql);

			query = query.setFirstResult(ajaxBean.getDisplayStart());
			query.setMaxResults(ajaxBean.getDisplayLength());
			// query.setFetchSize(pageLength);

			List queryResults = query.list();

			if (queryResults.size() > 0) {
				values = this.populateDrugProductSummaries(queryResults,
						request);
			}

		} catch (HibernateException he) {
			if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0) {
				log.warn("Data Problem: " + he.getMessage());
				return values;
			} else {
				log.error("Stack Trace: ", he);
				StringBuffer message = new StringBuffer(
						"Search Drug Product By ATC [");
				// message.append(criteria.toString());
				message.append("] failed");
				throw new Exception(message.toString());
			}
		}

		return values;
	}

	public int getQueryResultsCount(SearchCriteriaBean crit) {
		StringBuffer sql = new StringBuffer("select count(*) from (select distinct drug.drug_code, co.COMPANY_NAME, ste.EXTERNAL_STATUS_ENGLISH, s.schedule, i.ingredient, pm.PM_ENGLISH_FNAME ");
		//int resultsCount=0;
		
		sql.append(basicDrugSummaryFromClause(crit));
		sql.append(basicDrugSummaryWhereClause(crit));
		sql.append(CriteriaDrugSummaryWhereClause(crit));
		sql.append(")");
		
		int resultsCount= ((BigDecimal) getSession().createSQLQuery(sql.toString()).uniqueResult()).intValue();
	        //.uniqueResult()).intValue() ;
		
		return resultsCount;
	}

	public boolean isAjaxRequest() {
		return isAjaxRequest;
	}

	public void setAjaxRequest(boolean isAjaxRequest) {
		this.isAjaxRequest = isAjaxRequest;
	}

	public AjaxBean getAjaxBean() {
		return ajaxBean;
	}

	public void setAjaxBean(AjaxBean ajaxBean) {
		this.ajaxBean = ajaxBean;
	}

}
