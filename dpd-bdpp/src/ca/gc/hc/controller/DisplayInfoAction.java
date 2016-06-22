/*
 * Created on Apr 19, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ca.gc.hc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.quarksys.common.framework.util.CryptoUtil;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.gc.hc.bean.DrugBean;
import ca.gc.hc.bean.SearchCriteriaBean;
import ca.gc.hc.controller.util.ActionUtil;
import ca.gc.hc.dao.SearchDrugDao;
import ca.gc.hc.util.ApplicationGlobals;

/**
 * Action class to retrieve the drug product info by DIN for display
 */
public class DisplayInfoAction extends Action {

	private static CryptoUtil sc = new CryptoUtil();

	// Get an instance for log4j
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DisplayInfoAction.class);

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

		ActionForward forward = new ActionForward();
		HttpSession session = request.getSession();
		if (session.getAttribute("sessionActive") != null) {
			//DynaActionForm thisForm = (DynaActionForm) form;

			SearchCriteriaBean criteria = new SearchCriteriaBean();

			ActionMessages errors = new ActionMessages();
			
			session.removeAttribute(ApplicationGlobals.SELECTED_PRODUCT);
			session.removeAttribute("product_label");
			session.removeAttribute("approvedLabel");
			session.removeAttribute("marketedLabel");

			try {
				//criteria.setDrugCode(new Long(sc.decrypt((String)request.getParameter("code"))));
				criteria.setDrugCode(new Long((String)request.getParameter("code")));
				List list = new ArrayList();
				SearchDrugDao search = new SearchDrugDao();
				list = search.SearchByCriteria(criteria, request);
				
				log.debug("Search by Drug Code: [" + criteria.getDrugCode()	+ "]");

				if (list.size() == 0) {
					log.error("Null Response bean returned.");

					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.failure.search"));

				} else if (list.size() != 1) {
					log.error("More than one product bean returned..");

					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.failure.search"));
				} else {
					log.debug("Total match found: [" + list.size() + "].");

					DrugBean bean = (DrugBean) list.get(0);
					session.setAttribute(ApplicationGlobals.SELECTED_PRODUCT, ActionUtil.postProcessDrugBean(bean, request));

				}
				// if (criteria.getDrugCode() != null &&
				// criteria.getDrugCode().longValue() > 0)
				// {
				// ProductMonographDao dao = new ProductMonographDao();
				// ProductMonograph pm = new ProductMonograph();
				// pm = dao.searchPMByDrugCode(criteria.getDrugCode());
				//			
				// if(pm != null)
				// {
				// session.setAttribute("product_monograph", pm.getPmName());
				// }
				// else
				// {
				// session.setAttribute("product_monograph", "");
				// }

				 // PRODUCT LABEL - TO PUT BACK LATER
				 // get the product label information for the specific drug code
//				 ProductLabelDao labelDao = new ProductLabelDao();
//				 List productLabels = new ArrayList();
//				 
//				 productLabels = labelDao.searchLabelByDrugCode(criteria.getDrugCode());
//				 
//				 Boolean approvedLabel = false;
//				 Boolean marketedLabel = false;
//				 
//				 for(Iterator i = productLabels.iterator(); i.hasNext(); )
//				 {
//					ProductLabel productLabel = (ProductLabel) i.next();
//					
//					if(!productLabel.getApprvdLabelFname().equals(""))
//					{
//						approvedLabel = true;
//					}
//					
//					if(!productLabel.getMarketedLebelFname().equals(""))
//					{
//						marketedLabel = true;		
//					}					 
//				 }
//				 
//				 if(approvedLabel || marketedLabel)
//				 {
//					 session.setAttribute("product_label", productLabels);
//				 }
//				 
//				 if(approvedLabel)
//				 {
//					 session.setAttribute("approvedLabel", approvedLabel);
//				 }
//						 
//				 if(marketedLabel)
//				 {
//					 session.setAttribute("marketedLabel", marketedLabel);
//				 }

			} catch (Exception e) {
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						"error.failure.system"));
			}

			// Report any errors we have discovered back to the original form
			if (!errors.isEmpty()) {
				saveMessages(request, errors);
				forward = mapping.getInputForward();
			}
			else
			{
				forward = mapping.findForward("success");
			}
		} else {
			forward = mapping.findForward("sessionTimeout");
		}
		
		return forward;

	}
}
