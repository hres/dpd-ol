/*
 * Created on November 2008
 */
package ca.gc.hc.controller;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

import ca.gc.hc.view.SearchForm;

/**
 * Description  : This action classes is responsible for the logic 
 *                behind the search Drug product.
 */
public final class DispatchSearchAction extends LookupDispatchAction
{

  //Get an instance for log4j
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DispatchSearchAction.class);

  private final static String CANCEL_PROPERTY_NAME = "org.apache.struts.action.CANCEL";
  /**
   * Process the specified HTTP request, and create the corresponding
   * non-HTTP response (or forward to another web component that will create
   * it).  Return an <code>ActionForward</code> instance describing where
   * and how control should be forwarded, or <code>null</code> if the
   * response has already been completed.
   *
   * @param mapping The ActionMapping used to select this instance
   * @param actionForm The optional ActionForm bean for this request (if any)
   * @param request The non-HTTP request we are processing
   * @param response The non-HTTP response we are creating
   *
   * @exception IOException if an input/output error occurs
   * @exception ServletException if a servlet exception occurs    
   *            
   */
  
  /***************************************************************************
	 * Maps the resources used for the button text to the related action methods.
	 */
	protected Map getKeyMethodMap() {
		Map<String, String> buttonActions = new HashMap<String, String>();
	
		buttonActions.put("button.search", "searchPage");
		buttonActions.put("button.reset", "reset");
		return buttonActions;
	}
	//this method is created in case the user click enter
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
		HttpSession session = request.getSession();
		if (session.getAttribute("sessionActive") != null) {

            // Extract information from the request.
            String method = request.getParameter("method");            
  
            // If no method supplied, then default to searchLicence.
            if (method == null) {
                return searchPage(mapping, form, request, response);
            }
    	} else {
			return mapping.findForward("sessionTimeout");
		}
            return super.execute(mapping, form, request, response);
        }


	//go to the action search-recherche
 	public ActionForward searchPage(
    	ActionMapping mapping,
    	ActionForm form,
    	HttpServletRequest request,
   	 	HttpServletResponse response)
    	throws java.io.IOException, javax.servlet.ServletException
  	{
		ActionForward forward = new ActionForward(); 
		HttpSession session = request.getSession();
		if (session.getAttribute("sessionActive") != null) {
			forward = mapping.findForward("searchPage");
		} else {
			forward = mapping.findForward("sessionTimeout");
		}
		return forward;
	}


 	
	public ActionForward reset(ActionMapping mapping,
	    	ActionForm form,
	    	HttpServletRequest request,
	   	 	HttpServletResponse response)
	    	throws java.io.IOException, javax.servlet.ServletException
	  {
		ActionForward forward = new ActionForward(); 
		SearchForm theForm = (SearchForm)form;
		HttpSession session = request.getSession();
		if (session.getAttribute("sessionActive") != null) {
			theForm.reset(mapping, request);
			forward = mapping.getInputForward();
		} else {
			forward = mapping.findForward("sessionTimeout");
		}
		
		return forward;
	}
}