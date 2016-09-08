/*
 * Created on Apr 16, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ca.gc.hc.controller;

// Import Java IO classes.
import goc.webtemplate.component.jsp.MasterApplicationBean;
import goc.webtemplate.jsp.beans.ApplicationBaseSettingsBean;

import java.io.IOException;
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.PropertyMessageResources;

import ca.gc.hc.util.ApplicationGlobals;
/**
 * Action class that will pick the LOCALE paased from called application.
 */
public class StartupAction extends Action
{
	private String strLanguage = null;
 
  /**
   * Process the specified HTTP request, and create the corresponding HTTP
   * response (or forward to another web component that will create it).
   * Return an <code>ActionForward</code> instance describing where and how
   * control should be forwarded, or <code>null</code> if the response has
   * already been completed.
   * <p>
   * @param mapping  The ActionMapping used to select this instance.
   * @param actionForm  The optional ActionForm bean for this request (if any).
   * @param request  The HTTP request we are processing.
   * @param response  The HTTP response we are creating.
   *
   * @exception IOException  If an input/output error occurs.
   * @exception ServletException  If a servlet exception occurs.
   */
  public ActionForward execute(
    ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
  {
		ActionMessages messages = new ActionMessages();
		ActionForward forward = new ActionForward();
		//creates a session if one does not exist	
		HttpSession session = request.getSession(true);
		session.setAttribute("sessionActive", "Yes");
		String strLang = request.getParameter("lang");
		
		MasterApplicationBean applicationscopebean = new MasterApplicationBean();
		applicationscopebean.setRequest(request);
		ApplicationBaseSettingsBean goctemplateclientbean = new ApplicationBaseSettingsBean();
		goctemplateclientbean.setRequest(request);
		
		if (strLang.equals("en")) {
			

			session.setAttribute(Globals.LOCALE_KEY, new Locale("en", "CA"));
			ApplicationGlobals.setUserLocale(new Locale("en", "CA"));
			session.setAttribute("GoCTemplateCulture", ApplicationGlobals.instance().getUserLocale());
			session.setAttribute("lang", "en");
		}
		else if(strLang.equals("fr"))
		{
			session.setAttribute(Globals.LOCALE_KEY,  new Locale("fr", "CA"));
			ApplicationGlobals.setUserLocale(new Locale("fr", "CA"));
			session.setAttribute("GoCTemplateCulture", ApplicationGlobals.instance().getUserLocale());
			session.setAttribute("lang", "fr");
		}
		
		String strStaticFallBackFilePath = goctemplateclientbean.getStaticFallbackFilePath();
		String strRefTopStaticFile = applicationscopebean.getStaticFile(strStaticFallBackFilePath, "gcweb", "refTop.html");
		session.setAttribute("refTopStaticfile", strRefTopStaticFile);
		String strDefTopStaticFile = applicationscopebean.getStaticFile(strStaticFallBackFilePath, "gcweb", "top-" + goctemplateclientbean.getTwoLetterCultureLanguage() + ".html");
		session.setAttribute("defTopStaticFile", strDefTopStaticFile);
		String strDefFootStaticFile = applicationscopebean.getStaticFile(strStaticFallBackFilePath,  "gcweb", "footer-" + goctemplateclientbean.getTwoLetterCultureLanguage() + ".html");
		session.setAttribute("defFootStaticFile", strDefFootStaticFile);
		String strDefPreFooter = applicationscopebean.getStaticFile(strStaticFallBackFilePath,  "gcweb", "prefooter-" + goctemplateclientbean.getTwoLetterCultureLanguage() + ".html");
		session.setAttribute("defPreFooter", strDefPreFooter);
		session.setAttribute("languageLinkUrl", goctemplateclientbean.getLanguageLinkUrl());
		session.setAttribute("applicationscopebean", applicationscopebean);
		session.setAttribute("goctemplateclientbean", goctemplateclientbean);
				   

		/*
		 * DataTables: For optimal performance, set a maximum search results
		 * limit after which the Search Results DataTable will operate in
		 * server-processing mode after initial draw: - only the first 25
		 * pages will be loaded and displayed in the initial draw - every
		 * DataTable event (re-sort, results-per-page change, filter) will
		 * be delegated to the server - the server will respond to events
		 * with a JSON object containing DataTables-expected parameters and
		 * appropriately updated search results, based on the special
		 * parameters sent by the DataTable See
		 * http://legacy.datatables.net/usage/server-side (until version 10
		 * or later of DataTables starts being used)
		 */
		if (ApplicationGlobals.instance()
				.getDataTableServerProcessingThreshold() == 0) {
			PropertyMessageResources res = (PropertyMessageResources) request
					.getAttribute(Globals.MESSAGES_KEY);

			try {
				int toggleValue = Integer
						.parseInt(res
								.getMessage(ApplicationGlobals.DATA_TABLES_PROCESSING_TOGGLE_KEY));
				ApplicationGlobals.instance()
						.setdataTablesProcessingToggleLevel(toggleValue);
				request.getSession()
						.setAttribute(
								ApplicationGlobals.DATA_TABLES_PROCESSING_TOGGLE_VALUE,
								toggleValue);
				request.getSession().setAttribute(
						ApplicationGlobals.PAGE_LENGTH_KEY,
						ApplicationGlobals.INITIAL_PAGE_LENGTH_VALUE); 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(session.isNew()) {
			   try {
				ApplicationGlobals.instance().refreshAllSearchPageLists();
				} catch (Exception e) {
					// Report the error using the appropriate name and ID.
					   messages.add("name", new ActionMessage("id"));
				}
		   }
				
	
	   //If a message is required, save the specified key(s)
	   // into the request for use by the <struts:errors> tag.
	
	   if (!messages.isEmpty()) {
		   saveMessages(request, messages);
	
		   // Forward control to the appropriate 'failure' URI (change name as desired)
		   forward = mapping.findForward("failure");
	
		} else {
		   //load the search page
			forward = mapping.findForward("SearchPage");
		}
	
	
	   // return the appropriate page
		   return (forward);
	  }

}