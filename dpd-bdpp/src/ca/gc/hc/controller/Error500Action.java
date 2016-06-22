/*
 * Created on Apr 06, 2016
 *
 */
package ca.gc.hc.controller;
import goc.webtemplate.component.jsp.MasterApplicationBean;
import goc.webtemplate.jsp.beans.ApplicationBaseSettingsBean;



// Import Java IO classes.
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

import ca.gc.hc.util.ApplicationGlobals;

public class Error500Action extends Action {
		
	/**
	 * Process the specified HTTP request, and create the corresponding HTTP
	 * response (or forward to another web component that will create it).
	 * Return an <code>ActionForward</code> instance describing where and how
	 * control should be forwarded, or <code>null</code> if the response has
	 * already been completed.
	 * <p>
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance.
	 * @param actionForm
	 *            The optional ActionForm bean for this request (if any).
	 * @param request
	 *            The HTTP request we are processing.
	 * @param response
	 *            The HTTP response we are creating.
	 * 
	 * @exception IOException
	 *                If an input/output error occurs.
	 * @exception ServletException
	 *                If a servlet exception occurs.
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
	
		// creates a session if one does not exist
		HttpSession session = request.getSession(true);
		session.setAttribute("sessionActive", "Yes");

		MasterApplicationBean applicationscopebean = new MasterApplicationBean();
		applicationscopebean.setRequest(request);
		ApplicationBaseSettingsBean goctemplateclientbean = new ApplicationBaseSettingsBean();
		goctemplateclientbean.setRequest(request);
		
		session.setAttribute("lang", goctemplateclientbean.getTwoLetterCultureLanguage());
		

		request.setAttribute("applicationscopebean", applicationscopebean);
		request.setAttribute("goctemplateclientbean", goctemplateclientbean);
		
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

		
		return mapping.findForward("error500");
		
	}
}