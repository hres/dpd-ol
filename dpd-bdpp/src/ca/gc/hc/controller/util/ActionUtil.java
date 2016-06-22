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

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;

import ca.gc.hc.bean.DrugBean;
import ca.gc.hc.bean.DrugSummaryBean;

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

  /*
   * Help method to populate the page form that control the behavior of the 
   * page navigation.
   */
  public static void setPager(
    ActionMapping mapping,
    HttpServletRequest request,
    PagerForm myForm,
    int actionType)
  {
    int pageNumber = 0;
    long endIndex;
    long listSize;
    
    listSize = Long.parseLong(request.getSession().getAttribute(ApplicationGlobals.RESULT_COUNT).toString());

    long pageSize = new Long(mapping.getParameter()).longValue();
    long totalPages = listSize / pageSize;
    if ((listSize % pageSize) > 0)
    {
      totalPages++;
    }

    int inputPageNumber = myForm.getPage();

    if (actionType == ApplicationGlobals.INITIAL_ACTION)
    {
      pageNumber = inputPageNumber;
    }
    else if (actionType == ApplicationGlobals.PREVIOUS_PAGE_ACTION)
    {
      if (inputPageNumber > 1)
        pageNumber = inputPageNumber - 1;
    }
    else if (actionType == ApplicationGlobals.NEXT_PAGE_ACTION)
    {
      if (inputPageNumber < totalPages)
        pageNumber = inputPageNumber + 1;
    }

    long offset = pageSize * (pageNumber - 1);
    long startIndex = offset + 1;

    if (pageNumber < totalPages)
    {
      endIndex = startIndex + pageSize - 1;
    }
    else
    {
      endIndex = listSize;
    }

    myForm.setTotalCount(new Long(listSize).toString());
    myForm.setTotalPages(new Long(totalPages).toString());
    myForm.setPage(pageNumber);
    myForm.setOffset(new Long(offset).toString());
    myForm.setPageSize(new Long(pageSize).toString());
    myForm.setStartIndex(new Long(startIndex).toString());
    myForm.setEndIndex(new Long(endIndex).toString());
    myForm.setPagesLeft(new Long(totalPages - pageNumber).toString());
    myForm.setPassedPages(new Long(pageNumber - 1).toString());

	request.getSession().setAttribute(ApplicationGlobals.PAGER_FORM, myForm);
  }

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
		if (statusID.equals(ApplicationGlobals.ACTIVE_DRUG_STATUS_ID)) {
			request.getSession().setAttribute(ApplicationGlobals.SELECTED_STATUS, statusID);
		}else {
			request.getSession().setAttribute(ApplicationGlobals.SELECTED_STATUS
				, ApplicationGlobals.DISCONTINUE);
		}
		
	}

}
