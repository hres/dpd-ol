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
import org.apache.struts.util.LabelValueBean;

import ca.gc.hc.bean.DrugBean;
import ca.gc.hc.bean.DrugSummaryBean;
import ca.gc.hc.bean.SearchCriteriaBean;
import ca.gc.hc.controller.util.ActionUtil;
import ca.gc.hc.dao.SearchDrugDao;
import ca.gc.hc.util.ApplicationGlobals;
import ca.gc.hc.util.StringsUtil;
import ca.gc.hc.view.PagerForm;
import ca.gc.hc.view.SearchForm;

/**
 * Description : This action classes is responsible for the logic behind the
 * search Drug product.
 */
public final class SearchAction extends Action {
	private String strLanguage;
	// Get an instance for log4j
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SearchAction.class);


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
			if (request.getAttribute(CANCEL_PROPERTY_NAME) != null) {
				request.removeAttribute("SearchForm");
				return (mapping.findForward("SearchPage"));
			}

			SearchForm thisForm = (SearchForm) form;

			/* SL/2012-09-04:
			 * Searching by DIN or ATC: remove default status value since this product 
			 * may be inactive, in which case ShortcutSearchAction, if run for an AIG search,
			 * will be using an incorrect status as part of its criteria  */
			if ((StringsUtil.hasData(thisForm.getAtc()) || (StringsUtil.hasData(thisForm.getDin())))) {
				thisForm.setStatus(null);
			}
			
			SearchCriteriaBean criteria = new SearchCriteriaBean();

			List list = new ArrayList();
			session.removeAttribute(ApplicationGlobals.SEARCH_RESULT_KEY);
			session.removeAttribute(ApplicationGlobals.SELECTED_PRODUCT);
			session.removeAttribute(ApplicationGlobals.SEARCH_CRITERIA);
			session.removeAttribute(ApplicationGlobals.PAGER_FORM);
			// session.removeAttribute("product_monograph");
			session.removeAttribute("product_label");
			session.removeAttribute("approvedLabel");
			session.removeAttribute("marketedLabel");
					
			try {
				Long lngDrugCode = null;
				BeanUtils.copyProperties(criteria, thisForm);

				session.setAttribute(ApplicationGlobals.SEARCH_CRITERIA,
						criteria);
				
				session.setAttribute(ApplicationGlobals.SELECTED_STATUS,
					criteria.getStatusCode());
				
				//Map the selected drug class codes to their descriptions, save these
				//descriptions in a single comma-separated String, and store in
				//a session attribute for displaying back to the user in the gui
				mapDrugClassNames(criteria.getDrugClass(), session);

			
				/*Single quotes in SQL will raise an Oracle error. Double the single quotes in
				 * criteria items that can include one. Use a clone so that when we return to the jsp,
				 * the criteria displayed will be as entered and not as modified below 
				*/
				SearchCriteriaBean crit = new SearchCriteriaBean();
				BeanUtils.copyProperties(crit, criteria);
				crit = doubleEmbeddedQuotesIn(crit);
				
				list = new SearchDrugDao().SearchByCriteria(crit, request);

				log.debug("Search Criteria:" + criteria.toString());

				if (list.size() > 0) 
				{
					log.debug("Total match found: [" + list.size() + "].");	
					
					//if searching by ATC
					if (StringsUtil.hasData(criteria.getAtc())) {
						// Save the status ID for later, since when searching by ATC, there is no default
						// status. Search_criteria.jsp requires one to display in the Product Info page
						ActionUtil.assignSelectedStatusAttribute(list, request);
					}
					
					//if searching by drug code or by DIN
					if ((criteria.getDrugCode() != null && criteria.getDrugCode().longValue() > 0) 
							|| (criteria.getDin() != null && criteria.getDin().length() > 0)) 
					{
						if (criteria.getDrugCode() != null && criteria.getDrugCode().longValue() > 0) 
						{
							lngDrugCode = criteria.getDrugCode();
						} 
						else if (criteria.getDin() != null && criteria.getDin().length() > 0) 
						{
							if (list.size() == 1) {
								DrugBean bean = (DrugBean) list.get(0);
								lngDrugCode = bean.getDrugProduct().getDrugCode();
							}else{
								/* If the drug product happens to have more than one route, for instance, 
								 * the list size will be > 1 for the same DIN. And the list will contain 
								 * DrugSummaryBean's and not DrugBean's - SL/2010-03-05 */ 
								String className = list.get(0).getClass().getSimpleName();
								if (className.equals("DrugSummaryBean")) {
									DrugSummaryBean bean = (DrugSummaryBean) list.get(0);
									lngDrugCode = bean.getDrugCode();
								}else if(className.equals("DrugBean")){
									DrugBean bean = (DrugBean) list.get(0);
									lngDrugCode = bean.getDrugProduct().getDrugCode();
								}
							}
							// Save the status ID for later, since when searching by DIN, there is no default
							// status. Search_criteria.jsp requires one to display in the Product Info page
							ActionUtil.assignSelectedStatusAttribute(list, request);
						}
						
						//Get product labels
//						ProductLabelDao labelDao = new ProductLabelDao();
//						List productLabels = new ArrayList();
//
//						productLabels = labelDao.searchLabelByDrugCode(lngDrugCode);
//
//						Boolean approvedLabel = false;
//						Boolean marketedLabel = false;
//
//						for(Iterator i = productLabels.iterator(); i.hasNext(); )
//						{
//							ProductLabel productLabel = (ProductLabel) i.next();
//
//							if(!productLabel.getApprvdLabelFname().equals(""))
//							{
//								approvedLabel = true;
//							}
//
//							if(!productLabel.getMarketedLebelFname().equals(""))
//							{
//								marketedLabel = true;		
//							}					 
//						}
//
//						if(approvedLabel || marketedLabel)
//						{
//							session.setAttribute("product_label", productLabels);
//						}
//
//						if(approvedLabel)
//						{
//							session.setAttribute("approvedLabel", approvedLabel);
//						}
//
//						if(marketedLabel)
//						{
//							session.setAttribute("marketedLabel", marketedLabel);
//						}			
						//end of product labels


						// //get the product monograph information for the
						// specific drug code
						// ProductMonographDao dao = new ProductMonographDao();
						// ProductMonograph pm = new ProductMonograph();
						// pm = dao.searchPMByDrugCode(lngDrugCode);
						// if(pm != null)
						// {
						// session.setAttribute("product_monograph",
						// pm.getPmName());
						// }
						// else
						// {
						// session.setAttribute("product_monograph", "");
						// }
						// PRODUCT LABEL - TO PUT BACK LATER
						// //get the product label information for the specific
						// drug code
						// ProductLabelDao labelDao = new ProductLabelDao();
						// List productLabels = new ArrayList();
						// productLabels =
						// labelDao.searchLabelByDrugCode(lngDrugCode);
						// if(productLabels != null)
						// {
						// session.setAttribute("product_label", productLabels);
						// }
						// else
						// {
						// session.setAttribute("product_label", "");
						// }

						// }
						// else
						// {
						// session.setAttribute("product_monograph", "");
						// // PRODUCT LABEL - TO PUT BACK LATER
						// session.setAttribute("product_label", "");
					}
				}

				if (list.size() == 1) 
				{
					DrugBean bean = (DrugBean) list.get(0);
					session.setAttribute(ApplicationGlobals.SELECTED_PRODUCT, ActionUtil.postProcessDrugBean(bean, request));

					// lngDrugCode = bean.getDrugProduct().getDrugCode();
					// //get the product monograph information for the specific
					// drug code
					// ProductMonographDao dao = new ProductMonographDao();
					// ProductMonograph pm = new ProductMonograph();
					// pm = dao.searchPMByDrugCode(lngDrugCode);
					// if(pm != null)
					// {
					// session.setAttribute("product_monograph",
					// pm.getPmName());
					// }
					// else
					// {
					// session.setAttribute("product_monograph", "");
					// }

					// PRODUCT LABEL - TO PUT BACK LATER
					// //get the product label information for the specific drug
					// code
					// ProductLabelDao labelDao = new ProductLabelDao();
					// List productLabels = new ArrayList();
					// productLabels =
					// labelDao.searchLabelByDrugCode(lngDrugCode);
					// if(productLabels != null)
					// {
					// session.setAttribute("product_label", productLabels);
					// }
					// else
					// {
					// session.setAttribute("product_label", "");
					// }

				} else {
					session.setAttribute(ApplicationGlobals.SEARCH_RESULT_KEY, list);
				}
			} catch (Exception e) {
				log.debug(e.getMessage());
			}

			// Report any errors we have discovered back to the original form
			if (!messages.isEmpty()) {
				saveMessages(request, messages);
				forward = (mapping.getInputForward());
			} else {
				if (session.getAttribute(ApplicationGlobals.SEARCH_RESULT_KEY) != null) {
					PagerForm myForm = new PagerForm();
					myForm.setPage(1);
					session.setAttribute(ApplicationGlobals.SEARCH_RESULT_PAGE_NUMBER, 1);

					//ActionUtil.setPager(mapping, request, myForm, ApplicationGlobals.INITIAL_ACTION);

					forward = (mapping.findForward("multiplematch"));
				} else if (session.getAttribute(ApplicationGlobals.SELECTED_PRODUCT) != null) {
					forward = (mapping.findForward("onematch"));
				} else {
					messages.add(ActionMessages.GLOBAL_MESSAGE,	new ActionMessage("error.failure.system"));
					saveMessages(request, messages);
					forward = (mapping.getInputForward());
				}
			}
		} else {
			forward = mapping.findForward("sessionTimeout");
		}
		return forward;
	}// Close ActionForward


	/**
	 * @param drugClasseCodes A String array containing the user-selected codes 
	 * @param session The current HttpSession
	 * <p>Creates a comma-separated list of localized drug class descriptions as a 
	 * String and saves it on the current session. Used to display the user search criterion
	 * in the search summary results page</p>
	 */
	private void mapDrugClassNames(String[] drugClasseCodes, HttpSession session) {
		if (drugClasseCodes.length > 0) {
			String classNames = "";
			String name = "";
			try {
				for (String classCode : drugClasseCodes) {
				//getUniqueDrugClasses() is localized
					for (LabelValueBean drugClass : ApplicationGlobals.instance().getUniqueDrugClasses()) {
						name = drugClass.getLabel();
						if (drugClass.getValue().equals(classCode)) {
							classNames=classNames.concat(name + ", ");
							break;
						}
					}
				}
				session.setAttribute(ApplicationGlobals.DRUG_CLASS_NAMES, classNames.substring(0, classNames.lastIndexOf(",")));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//session.setAttribute(ApplicationGlobals.DRUG_CLASS_NAMES, );
		}else {
			session.removeAttribute(ApplicationGlobals.DRUG_CLASS_NAMES);
		}
		 
	}


	private SearchCriteriaBean doubleEmbeddedQuotesIn(SearchCriteriaBean crit) {
		//Process any criteria item that can include an apostrophe
		crit.setBrandName(StringsUtil.doubleEmbeddedQuotes(crit.getBrandName()));
		crit.setCompanyName(StringsUtil.doubleEmbeddedQuotes(crit.getCompanyName()));
		
		return crit;
	}
}// Close SearchAction
