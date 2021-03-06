/*
 * Created on November, 2006
 * Created by: Diane Beauvais
 * Action class providing the search for Drug function.
 */
package ca.gc.hc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import ca.gc.hc.bean.SearchCriteriaBean;
import ca.gc.hc.controller.util.ActionUtil;
import ca.gc.hc.dao.SearchDrugDao;
import ca.gc.hc.util.ApplicationGlobals;
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
			SearchForm thisForm = (SearchForm) form;

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
					SearchCriteriaBean safeCrit = (SearchCriteriaBean) session
							.getAttribute(ApplicationGlobals.QUERY_SEARCH_CRITERIA);
					ActionUtil.processAjaxRequest(ajaxBean, request, safeCrit,
							response, messages);
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
					 * Determine type of search and process accordingly
					 */
					if ((StringsUtil.hasData(thisForm.getAtc()) || (StringsUtil
							.hasData(thisForm.getDin())))) {
						list = processSearchByAtcOrDin(request, criteria);
						/*
						 * SL/2012-09-04: If searching by DIN or ATC: remove
						 * default status value since this product may be
						 * inactive, in which case ShortcutSearchAction, if run
						 * for an AIG search, will be using an incorrect status
						 * as part of its criteria
						 */
						//thisForm.setStatus(null);
					} else {
						list = processSearchByNames(request, criteria);
					}
					
					log.debug("Total match found: [" + list.size() + "].");

					if (list.size() == 0) {
						request.getSession().setAttribute(
								ApplicationGlobals.RESULT_COUNT_KEY, 0);
						// A "No match found" message will display in the
						// results page
						return mapping.findForward("multiplematch");

					} else if (list.size() == 1) {
						DrugBean bean = (DrugBean) list.get(0);
						session.setAttribute(
								ApplicationGlobals.SELECTED_PRODUCT,
								ActionUtil.postProcessDrugBean(bean, request));

					} else if (list.size() > 1) {
						session.setAttribute(
								ApplicationGlobals.SEARCH_RESULT_KEY, list);
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
			} else if (list.size() > 1) { 
				request.getSession().setAttribute(
						ApplicationGlobals.SEARCH_RESULT_KEY, list);				
					forward = (mapping.findForward("multiplematch"));
			} else if (list.size() == 1) {
					forward = (mapping.findForward("onematch"));
			} else {
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"error.failure.system"));
				saveMessages(request, messages);
				forward = (mapping.getInputForward());
			}

		} else {
			// inactive session
			forward = mapping.findForward("sessionTimeout");
		}

		return forward;
	}
	
	private List processSearchByAtcOrDin(HttpServletRequest request,
			SearchCriteriaBean criteria) throws Exception {
		List results = new ArrayList();

		results = new SearchDrugDao().SearchByCriteria(criteria, request);
		request.getSession().setAttribute(ApplicationGlobals.RESULT_COUNT_KEY,
				results.size());
		request.getSession().setAttribute(
				ApplicationGlobals.USER_SEARCH_CRITERIA, criteria);
		request.getSession().setAttribute(
				ApplicationGlobals.QUERY_SEARCH_CRITERIA, criteria);
		// Save the status ID for later, since when
		// searching by ATC or DIN, there is no default
		// status. Search_criteria.jsp requires one to
		// display in the Product Info page
		//ActionUtil.assignSelectedStatusAttribute(results, request);

		return results;
	}					

	List processSearchByNames(HttpServletRequest request,
			SearchCriteriaBean criteria) throws Exception {
		SearchCriteriaBean safeCrit = null; // SQL-safer clone
		List resultsList = new ArrayList();
		
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
		request.getSession().setAttribute(
				ApplicationGlobals.USER_SEARCH_CRITERIA, criteria);
		request.getSession().setAttribute(
				ApplicationGlobals.QUERY_SEARCH_CRITERIA, safeCrit);
		log.debug("Search Criteria:" + safeCrit.toString());

		SearchDrugDao dao = new SearchDrugDao();
		
		int resultsSize = dao.getQueryResultsCount(safeCrit);
		request.getSession().setAttribute(
				ApplicationGlobals.RESULT_COUNT_KEY, resultsSize);

		// Paginate if the result size is greater than the value set
		// for DataTable server processing
		int serverSideThreshold = ApplicationGlobals.instance()
				.getDataTableServerProcessingThreshold();
		log.debug("serverSideThreshold = " + serverSideThreshold);
		
		if (resultsSize > 0
				&& resultsSize >= serverSideThreshold) {
			ActionUtil.setupForServerProcessing(request, dao);
			// Only get the first page of results
			resultsList = dao.getNextResults(safeCrit, request);
			
		} else if(resultsSize > 0
				&& resultsSize < serverSideThreshold) {
			// re-query for actual results
			resultsList = new SearchDrugDao().SearchByCriteria(safeCrit, request);
			request.getSession().setAttribute(
			ApplicationGlobals.RESULT_COUNT_KEY, resultsList.size());
		}
		return resultsList;
	}
	
	private SearchCriteriaBean doubleEmbeddedQuotesIn(SearchCriteriaBean crit) {
		// Process any criteria item that can include an apostrophe
		crit.setBrandName(StringsUtil.doubleEmbeddedQuotes(crit.getBrandName()));
		crit.setCompanyName(StringsUtil.doubleEmbeddedQuotes(crit
				.getCompanyName()));

		return crit;
	}
}// Close SearchAction
