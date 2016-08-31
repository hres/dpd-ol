/*
 * Created on Apr 21, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ca.gc.hc.controller.util;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import ca.gc.hc.bean.DrugBean;
import ca.gc.hc.bean.DrugSummaryBean;
import ca.gc.hc.bean.SearchCriteriaBean;
import ca.gc.hc.util.ApplicationGlobals;
import ca.gc.hc.model.AHFS;
import ca.gc.hc.view.PagerForm;

/**
 * Untility class for the front end.
 */
public class ActionUtil
{
  //Get an instance for log4j
  private static Logger logger = Logger.getLogger(ActionUtil.class);
  
  public static DrugBean postProcessDrugBean(
    DrugBean bean,
    HttpServletRequest request)
    throws RemoteException
  {
    request.getSession().removeAttribute("ahfs.92");

    if (bean.getActiveIngredientList().size() == 0)
      bean.setActiveIngredientList(null);

    if (bean.getAhfsList().size() == 0)
    {
		bean.setAhfsList(null);
    }
    else
    {
      Iterator i = bean.getAhfsList().iterator();

      while (i.hasNext())
      {
        AHFS ahfs = (AHFS) i.next();
        logger.debug("AHFS [" + ahfs.getAhfsNumber() + "]");
        if (ahfs.getAhfsNumber().startsWith("92:01")
          || ahfs.getAhfsNumber().startsWith("92:02"))
        {
          logger.debug("Flag is set.");
          request.getSession().setAttribute("ahfs.92", "true");
        }
      }
    }

    if (bean.getFormList().size() == 0)
	bean.setFormList(null);

    if (bean.getPackagingList().size() == 0)
		bean.setPackagingList(null);

    if (bean.getRouteList().size() == 0)
    	bean.setRouteList(null);

    if (bean.getScheduleList().size() == 0)
		bean.setScheduleList(null);

    if (bean
      .getStatusVO()
      .getStatusID().equals(Long.valueOf(ApplicationGlobals.ACTIVE_DRUG_STATUS_ID)))
		//bean.getStatusVO().setStatus(DPDQConstants.ACTIVE_CODE);
		bean.setStatusAbbreviation(ApplicationGlobals.ACTIVE_CODE);
    else
		//bean.getStatusVO().setStatus(DPDQConstants.DISCONTINUE_CODE);
		bean.setStatusAbbreviation(ApplicationGlobals.DISCONTINUE_CODE);
    return bean;
  }
  

	/**
	 * Saves the status ID on the session for later use, in the case of products searched by DIN or ATC, 
	 * since when searching by these, there is no default status criterion used. <br/>
	 * Search_criteria.jsp requires this to display the status criterion in the Search Results Summary page.
	 * @param list A List normally containing a single DrugBean's or a number of DrugSummaryBean's, 
	 * which themselves contain search results.
	 * @param request The current servlet request.
	 * @author Sylvain Larivière 2012-09-07
	 */
	@SuppressWarnings("unchecked")
	public static void assignSelectedStatusAttribute(List list, HttpServletRequest request) {
		String statusID = "";
		
		String className = list.get(0).getClass().getSimpleName();
		if (className.equals("DrugBean")){
			DrugBean bean = (DrugBean) list.get(0);
			statusID = bean.getStatusVO().getStatusID().toString();
		}else if (className.equals("DrugSummaryBean")) {
			DrugSummaryBean bean = (DrugSummaryBean) list.get(0);
			statusID = bean.getStatusID().toString();
		}
		request.getSession().setAttribute(ApplicationGlobals.SELECTED_STATUS, statusID);
		
	}

	/**
	 * @param drugClasseCodes
	 *            A String array containing the user-selected codes
	 * @param session
	 *            The current HttpSession
	 *            <p>
	 *            Creates a comma-separated list of localized drug class
	 *            descriptions as a String and saves it on the current session.
	 *            Used to display the user search criterion in the search
	 *            summary results page
	 *            </p>
	 * @throws Exception
	 */
	public static void mapDrugClassNames(String[] drugClasseCodes, HttpSession session)
			throws Exception {
		if (drugClasseCodes.length > 0) {
			String classNames = "";
			String name = "";
			for (String classCode : drugClasseCodes) {
				// getUniqueDrugClasses() is localized
				List<LabelValueBean> classes = ApplicationGlobals.instance()
						.getUniqueDrugClasses();
				for (LabelValueBean drugClass : classes) {
					name = drugClass.getLabel();
					if (drugClass.getValue().equals(classCode)) {
						classNames = classNames.concat(name + ", ");
						break;
					}
				}
			}
			session.setAttribute(ApplicationGlobals.DRUG_CLASS_NAMES,
					classNames.substring(0, classNames.lastIndexOf(",")));
		} else {
			session.removeAttribute(ApplicationGlobals.DRUG_CLASS_NAMES);
		}

	}

}
