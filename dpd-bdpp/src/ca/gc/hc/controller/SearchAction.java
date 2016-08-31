/*
 * Created on November, 2006
 * Created by: Diane Beauvais
 * Action class providing the search for Drug function.
 */
package ca.gc.hc.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.gc.hc.bean.AjaxBean;
import ca.gc.hc.bean.AjaxBean.AjaxRequestStatus;
import ca.gc.hc.bean.DrugBean;
import ca.gc.hc.bean.DrugSummaryBean;
import ca.gc.hc.bean.SearchCriteriaBean;
import ca.gc.hc.controller.util.ActionUtil;
import ca.gc.hc.dao.SearchDrugDao;
import ca.gc.hc.util.ApplicationGlobals;
import ca.gc.hc.util.DataTableColumn;
import ca.gc.hc.util.JsonBuilder;
import ca.gc.hc.util.StringsUtil;
import ca.gc.hc.view.SearchForm;

/**
 * Description : This action classes is responsible for the logic behind the
 * search Drug product.
 */
public final class SearchAction extends Action {
	private String strLanguage;
	// Get an instance for log4j
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(SearchAction.class);

	private final static String CANCEL_PROPERTY_NAME = "org.apache.struts.action.CANCEL";

	/**
	 * Process the specified HTTP request, and create the corresponding non-HTTP
	 * response (or forward to another web component that will create it).
	 * Return an <code>ActionForward</code> instance describing where and how
	 * control should be forwarded, or <code>null</code> if the response has
	 * already been completed.
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param actionForm
	 *            The optional ActionForm bean for this request (if any)
	 * @param request
	 *            The non-HTTP request we are processing
	 * @param response
	 *            The non-HTTP response we are creating
	 * 
	 * @exception IOException
	 *                if an input/output error occurs
	 * @exception ServletException
	 *                if a servlet exception occurs
	 * 
	 */

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.io.IOException, javax.servlet.ServletException {

		HttpSession session = request.getSession();
		ActionForward forward = new ActionForward();
		ActionMessages messages = new ActionMessages();

		if (session.getAttribute("sessionActive") != null) {
			strLanguage = request.getParameter("lang");
			if ("en".equals(strLanguage)) {
				session.setAttribute("org.apache.struts.action.LOCALE",
						new java.util.Locale("en", "CA"));
				request.getSession().setAttribute(Globals.LOCALE_KEY,
						new Locale("en", "CA"));
			} else if ("fr".equals(strLanguage)) {
				session.setAttribute("org.apache.struts.action.LOCALE",
						new java.util.Locale("fr", "CA"));
				request.getSession().setAttribute(Globals.LOCALE_KEY,
						Locale.CANADA_FRENCH);
			}
			if (request.getAttribute(CANCEL_PROPERTY_NAME) != null) {
				request.removeAttribute("SearchForm");
				return (mapping.findForward("SearchPage"));
			}
			/*
			 * !! IMPORTANT !! : On the Tomcat server in the HRE environment,
			 * when a search returns too many results, it takes too long for the
			 * DataTable to load all results and while this is happening, (many
			 * seconds) users cannot interact with it.
			 * 
			 * When the number of results exceeds the limit set in
			 * ApplicationResources, only the first 25 results will be returned
			 * initially and the DataTable will request more results as
			 * required, somewhat similar to the pre-WET4 implementation (no
			 * DataTable) where the data was always paged to 25 results per
			 * page.
			 * 
			 * The AjaxBean is used to capture the DataTable's request
			 * parameters. See also method processAjaxRequest() herein.
			 * 
			 * See http://legacy.datatables.net/usage/server-side (until version
			 * 10 or later of DataTables starts being used)
			 */
			AjaxBean ajaxBean = null;
			SearchCriteriaBean criteria = null;
			SearchCriteriaBean safeCrit = null; // SQL-safer clone
			SearchForm thisForm = (SearchForm) form;

			/*
			 * SL/2012-09-04: If searching by DIN or ATC: remove default status
			 * value since this product may be inactive, in which case
			 * ShortcutSearchAction, if run for an AIG search, will be using an
			 * incorrect status as part of its criteria
			 */
			if ((StringsUtil.hasData(thisForm.getAtc()) || (StringsUtil
					.hasData(thisForm.getDin())))) {
				thisForm.setStatus(null);
			}

			List list = new ArrayList();

			/*
			 * See if we are in server-processing mode. Parameter sEcho is
			 * always generated by the DataTable when it is in server-processing
			 * mode, and its value is incremented with each request.
			 * 
			 * See http://legacy.datatables.net/usage/server-side (until version
			 * 10 or later of DataTables starts being used)
			 */
			String echo = request.getParameter("sEcho");
			boolean isDataTableServerSideProcessing = echo != null
					&& !echo.equals("0");

			try {
				if (isDataTableServerSideProcessing) {
					ajaxBean = (AjaxBean) session
							.getAttribute(ApplicationGlobals.AJAX_BEAN);
					safeCrit = (SearchCriteriaBean) session
							.getAttribute(ApplicationGlobals.QUERY_SEARCH_CRITERIA);
					processAjaxRequest(ajaxBean, request, safeCrit, response,
							messages);
					session.setAttribute(ApplicationGlobals.AJAX_BEAN, ajaxBean);

				} else {
					// Not an Ajax request: process for normal JSP
					criteria = new SearchCriteriaBean();

					BeanUtils.copyProperties(criteria, thisForm);
					session.setAttribute(ApplicationGlobals.SELECTED_STATUS,
							criteria.getStatusCode());
					/*
					 * Map the selected drug class codes to their descriptions,
					 * save these descriptions in a single comma-separated
					 * String, and store in a session attribute for displaying
					 * back to the user in the gui.
					 */
					ActionUtil.mapDrugClassNames(criteria.getDrugClass(),
							session);

					/*
					 * Single quotes in SQL will raise an Oracle error. Double
					 * the quotes in criteria items that can include one, for
					 * searching. !! IMPORTANT !! ENSURE THAT ALL SUCH ITEMS ARE
					 * COVERED IN doubleEmbeddedQuotesIn(). Use a clone so that
					 * when we return to the jsp, the criteria displayed will be
					 * as entered and not as modified below.
					 */
					safeCrit = new SearchCriteriaBean();
					BeanUtils.copyProperties(safeCrit, criteria);
					safeCrit = doubleEmbeddedQuotesIn(safeCrit);
					session.setAttribute(
							ApplicationGlobals.USER_SEARCH_CRITERIA, criteria);
					session.setAttribute(
							ApplicationGlobals.QUERY_SEARCH_CRITERIA, safeCrit);

					SearchDrugDao dao = new SearchDrugDao();
//					list = dao.SearchByCriteria(safeCrit, request);
//					int resultsSize = list.size();
					int resultsSize = dao.getQueryResultsCount(safeCrit);
					request.getSession().setAttribute(
							ApplicationGlobals.RESULT_COUNT_KEY, resultsSize);

					// Paginate if the result size is greater than the value set
					// for DataTable server processing
					if (resultsSize > 0
							&& resultsSize >= ApplicationGlobals.instance()
									.getDataTableServerProcessingThreshold()) {
						setupForServerProcessing(request, session, dao);
						// Only get the first page of results
						list = dao.getNextResults(safeCrit, request);
						
					} else if(resultsSize > 0
							&& resultsSize < ApplicationGlobals.instance()
							.getDataTableServerProcessingThreshold()){
						// re-query for actual results
						list = dao.SearchByCriteria(safeCrit, request);
						request.getSession().setAttribute(
						ApplicationGlobals.RESULT_COUNT_KEY, list.size());
					
					} else if (resultsSize > 0) {
						// resultSize is less than server-processing threshold:
						// return all results
						list = new SearchDrugDao().SearchByCriteria(safeCrit,
								request);
					} else {
						// results = 0
						request.getSession().setAttribute(
								ApplicationGlobals.RESULT_COUNT_KEY, 0);
						return mapping.findForward("multiplematch");
					}

					session.setAttribute(
							ApplicationGlobals.SEARCH_RESULT_KEY, list);

					log.debug("Search Criteria:" + safeCrit.toString());

					if (list.size() > 0) {
						log.debug("Total match found: [" + list.size() + "].");

						// if searching by ATC or DIN
						if ((StringsUtil.hasData(criteria.getAtc()))
								|| (criteria.getDin() != null && criteria
										.getDin().length() > 0)) {
							// Save the status ID for later, since when
							// searching by ATC or DIN, there is no default
							// status. Search_criteria.jsp requires one to
							// display in the Product Info page
							ActionUtil.assignSelectedStatusAttribute(list,
									request);
						}

					} else if (list.size() == 1) {
						DrugBean bean = (DrugBean) list.get(0);
						session.setAttribute(
								ApplicationGlobals.SELECTED_PRODUCT,
								ActionUtil.postProcessDrugBean(bean, request));
					}

				}
			} catch (Exception e) {
				log.error(e.getMessage());
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"error.failure.system"));
				forward = (mapping.getInputForward());

			}

			// Report any errors we have discovered back to the original form
			if (!messages.isEmpty()) {
				saveMessages(request, messages);
				forward = (mapping.getInputForward());
			}

			if (ajaxBean != null
					&& AjaxRequestStatus.ACTIVE == ajaxBean.getAjaxStatus()) {
				forward = null;
			} else if (session
					.getAttribute(ApplicationGlobals.SEARCH_RESULT_KEY) != null) {
				forward = (mapping.findForward("multiplematch"));
			} else if (session
					.getAttribute(ApplicationGlobals.SELECTED_PRODUCT) != null) {
				forward = (mapping.findForward("onematch"));
			} else {
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"error.failure.system"));
				saveMessages(request, messages);
				forward = (mapping.getInputForward());
			}

		} else {
			forward = mapping.findForward("sessionTimeout");
		}
		return forward;
	}

	private void setupForServerProcessing(HttpServletRequest request,
			HttpSession session, SearchDrugDao dao) {
		AjaxBean ajaxBean = new AjaxBean();
		ajaxBean.setDisplayLength((int) request.getSession().getAttribute(
				ApplicationGlobals.PAGE_LENGTH_KEY));

		if (request.getSession().getAttribute(ApplicationGlobals.RESULT_COUNT_KEY) != null) {
			ajaxBean.setTotalCountBeforeFiltering((int) request.getSession().getAttribute(ApplicationGlobals.RESULT_COUNT_KEY));
		}
		
		dao.setAjaxRequest(false);
		dao.setAjaxBean(ajaxBean);

		session.setAttribute(ApplicationGlobals.AJAX_BEAN, ajaxBean);
	}

	private void processAjaxRequest(AjaxBean ajaxBean,
			HttpServletRequest request, SearchCriteriaBean crit,
			HttpServletResponse response, ActionMessages messages) {
		try {
			initializeAjaxBean(ajaxBean, request);
			request.getSession().setAttribute(ApplicationGlobals.AJAX_STATUS,
					true);
			SearchDrugDao dao = new SearchDrugDao();
			dao.setAjaxRequest(true);
			dao.setAjaxBean(ajaxBean);
			List ajaxResults = dao.getNextResults(crit, request);
			ajaxBean.resetProcessingFlags();

			@SuppressWarnings("unchecked")
			JsonBuilder builder = new JsonBuilder(
					(List<DrugSummaryBean>) ajaxResults, ajaxBean);

			String ajaxResponse = builder
					.generateDataTableJsonResponse(ajaxResults.size());
			log.debug("ajaxResponse:" + ajaxResponse);

			response.setContentType("application/Json");
			PrintWriter out = response.getWriter();
			out.print(ajaxResponse);
			out.flush();

		} catch (Exception e) {
			log.error(e.getMessage());
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"error.failure.system"));
		}

	}

	/**
	 * @param ajaxBean
	 *            The current AjaxBean instance
	 * @param request
	 *            The current request
	 *            <p>
	 *            Adds DataTable-related parameters from the Ajax request to the
	 *            AjaxBean
	 *            </p>
	 * @author S. Larivière 2016-08-12
	 */
	private void initializeAjaxBean(AjaxBean ajaxBean,
			HttpServletRequest request) throws Exception {
		log.debug("DataTable is requesting server-side processing");

		// Sanitize parameters with a number value that will remain a String
		ajaxBean.setEcho(Integer.valueOf(request.getParameter("sEcho"))
				.toString());
		ajaxBean.setDisplayLength(Integer.parseInt(request
				.getParameter("iDisplayLength")));
		ajaxBean.setDisplayStart(Integer.parseInt(request
				.getParameter("iDisplayStart")));
		ajaxBean.setColumnOrderMap(extractColumnOrderMap(request));
		ajaxBean.setAllColumns(extractAllColumns(request));
		extractSortColumns(request, ajaxBean);

		// general flag to specify that the above properties should now be used
		ajaxBean.setAjaxStatus(AjaxRequestStatus.ACTIVE);
	}

	/**
	 * @param request The current request
	 * @return A Map of valid sort directions based on DataTable request parameters, 
	 * keyed on the corresponding sort column index. Used to generate a collection
	 * of columns that the DataTable was sorted on (multi-select is possible).
	 * @throws Exception if any sort direction has an invalid value
	 */
	private Map<Integer, String> extractColumnOrderMap(
			HttpServletRequest request) throws Exception {
		int sortColumnCount = Integer.valueOf(request
				.getParameter("iSortingCols"));
		Map<Integer, String> orderMap = new HashMap<Integer, String>();

		for (int i = 0; i < sortColumnCount; i++) {
			Integer sortIndex = new Integer(request.getParameter("iSortCol_"
					+ i));
			String sortOrder;
			try {
				sortOrder = safeAssignSortDirection(request.getParameter("sSortDir_" + i));
			} catch (Exception e) {
				// Invalid sort direction value
				log.error(e.getMessage() + " for column index "
						+ sortIndex);
				throw e;
			}
			orderMap.put(sortIndex, sortOrder);
		}

		return orderMap;
	}

	/**
	 * @param direction A String specifying a sort direction
	 * @return That same sort direction if valid (can only be "asc" or "desc").
	 * @throws Exception if direction has an invalid value
	 */
	private String safeAssignSortDirection(String direction) throws Exception {
		if ("asc".equalsIgnoreCase(direction)
				|| "desc".equalsIgnoreCase(direction)) {
			return direction;
		} else {
			throw new Exception("Invalid sort direction value");
		}
	}

	/**
	 * @param request The current request
	 * @param ajaxBean The current AjaxBean 
	 * <p>Assigns to the passed AjaxBean a collection of DataTableColumn instances 
	 * that were used to sort search results (multi-select is permitted).
	 * Each sort column is assigned a sort direction ("asc" or "desc", otherwise null).</p>
	 */
	private void extractSortColumns(
			HttpServletRequest request, AjaxBean ajaxBean) {
		int sortColumnCount = Integer.parseInt(request
				.getParameter("iSortingCols"));
		List<DataTableColumn> sortColumns = new ArrayList<DataTableColumn>();
		for (int i = 0; i < sortColumnCount; i++) {
			Integer colIndex = new Integer(request.getParameter("iSortCol_" + i));
			//get the appropriate column
			DataTableColumn col = ajaxBean.getAllColumns().get(colIndex);
			//assign it with its sort direction
			String dir = ajaxBean.getColumnOrderMap().get(colIndex);
			col.setSortDirection(dir);
			//add the column to the collection
			sortColumns.add(col);
		}
		ajaxBean.setSortColumns(sortColumns);
	}

	private Map<Integer, DataTableColumn> extractAllColumns(
			HttpServletRequest request) throws Exception {
		int allColumnCount = Integer.parseInt(request.getParameter("iColumns"));
		Map<Integer, DataTableColumn> allColumns = new HashMap<Integer, DataTableColumn>();
		for (int i = 0; i < allColumnCount; i++) {
			DataTableColumn col = new DataTableColumn();
			col.setColumnIndex(i);
			col.setName(request.getParameter("mDataProp_" + i));
			col.setFieldName(fieldNameForColumnIndex(i));
			col.setSearchable(stringToBoolean(request
					.getParameter("bSearchable_" + i)));
			col.setSortable(stringToBoolean(request.getParameter("bSortable_"
					+ i)));
			allColumns.put(Integer.valueOf(i), col);
		}
		return allColumns;
	}

	private String fieldNameForColumnIndex(int index) throws Exception {
		String lang = ApplicationGlobals.instance().getUserLanguage();
		String fieldName = "";
		String suffix = "";

		switch (index) {
		case 0:
			suffix = "en".equalsIgnoreCase(lang) ? "ENGLISH" : "FRENCH";
			fieldName = "EXTERNAL_STATUS_";
			break;
		case 1:
			fieldName = "drug_identification_number";
			break;
		case 2:
			fieldName = "COMPANY_NAME";
			break;
		case 3:
			fieldName = "SORT_COLUMN";
			break;
		case 4:
			suffix = "en".equalsIgnoreCase(lang) ? "" : "_f";
			fieldName = "class";
			break;
		case 5:
			suffix = "en".equalsIgnoreCase(lang) ? "ENGLISH_FNAME"
					: "FRENCH_FNAME";
			fieldName = "PM_";
			break;
		case 6:
			suffix = "en".equalsIgnoreCase(lang) ? "" : "_f";
			fieldName = "schedule";
			break;
		case 7:
			suffix = "";
			fieldName = "number_of_ais";
			break;
		case 8:
			suffix = "en".equalsIgnoreCase(lang) ? "" : "_f";
			fieldName = "ingredient";
			break;
		case 9:
			/*
			 * This column corresponds to a mix of field values conditionally
			 * concatenated together. The logic for this is currently in the jsp
			 * page. Hence it is not sortable from a single field. Therefore, it
			 * is set as non-sortable in the gui. 
			 */
			suffix = "";
			fieldName = "";
			break;
		default:
			String message = "Unable to map sort column index to a valid database field name";
			Exception e = new Exception(message);
			throw e;
		}
		return fieldName + suffix;
	}

	private boolean stringToBoolean(String parameter) {
		return "true".equalsIgnoreCase(parameter) ? true : false;
	}

	private SearchCriteriaBean doubleEmbeddedQuotesIn(SearchCriteriaBean crit) {
		// Process any criteria item that can include an apostrophe
		crit.setBrandName(StringsUtil.doubleEmbeddedQuotes(crit.getBrandName()));
		crit.setCompanyName(StringsUtil.doubleEmbeddedQuotes(crit
				.getCompanyName()));

		return crit;
	}
}// Close SearchAction
