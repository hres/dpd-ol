/*
 * Created on Apr 18, 2004
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

import net.quarksys.common.framework.util.CryptoUtil;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import ca.gc.hc.bean.DrugBean;
import ca.gc.hc.bean.DrugSummaryBean;
import ca.gc.hc.bean.SearchCriteriaBean;
import ca.gc.hc.controller.util.ActionUtil;
import ca.gc.hc.dao.SearchDrugDao;
import ca.gc.hc.util.ApplicationGlobals;
import ca.gc.hc.view.PagerForm;

/**
 * Description : This action classes is responsible for the logic behind the
 * search Drug product.
 */
public final class ShortcutSearchAction extends Action {
	private String strLanguage;
	/*
	 * The String Cryptor instance
	 */
	private static CryptoUtil sc = new CryptoUtil();

	// Get an instance for log4j
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ShortcutSearchAction.class);

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
		
		DynaActionForm thisForm = (DynaActionForm) form;

		SearchCriteriaBean criteria = new SearchCriteriaBean();

		ActionMessages messages = new ActionMessages();

		HttpSession session = request.getSession();
		ActionForward forward = new ActionForward();
		if (session.getAttribute("sessionActive") != null) {
			strLanguage = request.getParameter("lang");	
			if ("en".equals(strLanguage)) 
			{
				session.setAttribute("org.apache.struts.action.LOCALE",new java.util.Locale("en","CA"));
				request.getSession().setAttribute(Globals.LOCALE_KEY, new Locale("en", "CA"));
			}
			else if ("fr".equals(strLanguage)) 
			{
				session.setAttribute("org.apache.struts.action.LOCALE",new java.util.Locale("fr","CA"));
				request.getSession().setAttribute(Globals.LOCALE_KEY, Locale.CANADA_FRENCH);
			}

			session.removeAttribute(ApplicationGlobals.USER_SEARCH_CRITERIA);
			session.removeAttribute(ApplicationGlobals.QUERY_SEARCH_CRITERIA);
			session.removeAttribute(ApplicationGlobals.SEARCH_RESULT_KEY);
			try {
				if (thisForm.get("no") != null
						&& ((String) thisForm.get("no")).length() > 0) {
					criteria.setAigNumber((String) thisForm.get("no"));
				}

				if (thisForm.get("code") != null
						&& ((String) thisForm.get("code")).length() > 0) {

					//criteria.setCompanyCode(new Long(sc.decrypt((String) thisForm.get("code"))));
					criteria.setCompanyCode(new Long((String) thisForm.get("code")));
					criteria.setAigNumber(null);
					
				}
				// new
				if ((criteria.getAigNumber() != null)
						|| (criteria.getCompanyCode() != null)) {
//					Long lngStatus = (Long) session
//							.getAttribute(ApplicationGlobals.SELECTED_STATUS);
//					if (lngStatus.longValue() > 0) {
//						criteria.setStatusCode(Long
//								.valueOf(ApplicationGlobals.ACTIVE));
//					} else {
//						criteria.setStatusCode(Long
//								.valueOf(ApplicationGlobals.DISCONTINUE));
//					}
							
				}
				/*SL/2012-09-07: If the main search was for a DIN, criteria.getStatusCode() will be null*/
					if(session.getAttribute(ApplicationGlobals.SELECTED_STATUS)
							.equals(ApplicationGlobals.ACTIVE_DRUG_STATUS_ID)) {
						criteria.setStatusCode(ApplicationGlobals.SELECTED_STATUS);
					}else {
					}
				//}
					
					


				SearchDrugDao search = new SearchDrugDao();
				List list = new ArrayList();
				
				list = search.SearchByCriteria(criteria, request);

				// SL/2009-09-18: this raises a null pointer exception
//				log.debug("Search Criteria:" + criteria.toString());

				if (list.size() > 0) {
					// log.debug("Total match found: [" + list.size() + "].");
					if (list.size() == 1)  {
						/* SL/2012-08-10 Bug fix: the code was not anticipating that a single product
						 * could be returned when searching for all products by a company
						 */
						if (criteria.getAigNumber() != null) {
							// if only one record is returned by an AIG search it is because it's the
							// original product: the request attribute will trigger a message to that effect
							// found!"
							request.setAttribute("similarAIG", "Y");
						}else { 
							//it is the company that has no other product
							request.setAttribute("similarProduct", "Y");
						}
					} else { // more than one result returned
						session.setAttribute(
								ApplicationGlobals.SEARCH_RESULT_KEY, list);
	
						if (criteria.getCompanyCode() != null) {
							criteria.setCompanyName(extractCompanyName(list, messages));
						}
					}

					session.setAttribute(ApplicationGlobals.USER_SEARCH_CRITERIA,
								criteria);
				}else { // no other product from this company
					messages.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("message.no.similar_company_product"));
				}
			} catch (Exception e) {
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"error.failure.system"));
				// //PRODUCT LABEL - TO PUT BACK LATER
				session.setAttribute("product_label", "");
				session.setAttribute("approvedLabel", "");
				session.setAttribute("marketedLabel", "");
				// session.setAttribute("product_monograph", "");
			}

			// Report any errors we have discovered back to the original form
			if (!messages.isEmpty()) {
				saveMessages(request, messages);
				forward = (mapping.getInputForward());
			}

			messages = null;
			if (session.getAttribute(ApplicationGlobals.SEARCH_RESULT_KEY) != null) {
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

	/**
	 * @param list List of search results as either DrugBeanSummaries or DrugBeans
	 * @param messages Error messages common to this Action
	 * @return A String containing the company name for the results returned
	 * @author Sylvain Larivière 2012-08-10 
	 */
	private String extractCompanyName(List list, ActionMessages messages) {
		String className = "";
		String companyName = "";
		
		className = list.get(0).getClass().getSimpleName();
		if (className.equals("DrugSummaryBean")) {
			DrugSummaryBean summary = new DrugSummaryBean();
			summary = (DrugSummaryBean) list.get(0);
			companyName = summary.getCompanyName();
		}else if (className.equals("DrugBean")){
			DrugBean drug = new DrugBean();
			drug = (DrugBean) list.get(0);
			companyName = drug.getCompanyVO().getCompanyName();
		}else{
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
			"error.failure.system"));
		}	
		return companyName;
	}
}
