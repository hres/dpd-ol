/*
 * Created on November, 2006
 * Created by: Diane Beauvais
 * Action class providing the new search for Drug function.
 */
package ca.gc.hc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import ca.gc.hc.dao.SearchDrugDao;
import ca.gc.hc.util.ApplicationGlobals;

/**
 * Description : This action classes is responsible for the logic behind the
 * search Drug product.
 */
public final class NewSearchAction extends Action {
	private String strLanguage;
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
		session.removeAttribute(ApplicationGlobals.SEARCH_RESULT_PAGE_NUMBER);
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
			forward = mapping.findForward("success");

		} else {
			forward = mapping.findForward("sessionTimeout");
		}		
		return forward;
	}// Close ActionForward
	
	
}// Close SearchAction
