package ca.gc.hc.controller;

import goc.webtemplate.component.jsp.MasterApplicationBean;
import goc.webtemplate.jsp.beans.ApplicationBaseSettingsBean;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.gc.hc.bean.SearchCriteriaBean;
import ca.gc.hc.controller.util.ActionUtil;
import ca.gc.hc.util.ApplicationGlobals;

/*******************************************************************************
 * An Action used to switch between english and french depending on the language
 * the user chooses from the top nav button which is in the header.jsp file on
 * all pages
 */
public class SwitchLanguageAction extends Action {

	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SwitchLanguageAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward = new ActionForward();
		HttpSession session = request.getSession(false);
		String strLang = request.getParameter("lang");
		String formname = request.getParameter("url");
		MasterApplicationBean applicationscopebean = new MasterApplicationBean();
        applicationscopebean.setRequest(request);
        ApplicationBaseSettingsBean goctemplateclientbean = new ApplicationBaseSettingsBean();
        goctemplateclientbean.setRequest(request);
		
        if (strLang.equals("fr")) {

            session.setAttribute(Globals.LOCALE_KEY, new Locale("fr", "CA"));
            ApplicationGlobals.setUserLocale(new Locale("fr", "CA"));
            request.setAttribute("GoCTemplateCulture", ApplicationGlobals
                         .instance().getUserLocale());
            session.setAttribute("GoCTemplateCulture", ApplicationGlobals
                         .instance().getUserLocale());
            session.setAttribute("lang", "fr");
        } else if (strLang.equals("en")) {
            session.setAttribute(Globals.LOCALE_KEY, new Locale("en", "CA"));
            ApplicationGlobals.setUserLocale(new Locale("en", "CA"));
            request.setAttribute("GoCTemplateCulture", ApplicationGlobals
                         .instance().getUserLocale());
            session.setAttribute("GoCTemplateCulture", ApplicationGlobals
                         .instance().getUserLocale());
            session.setAttribute("lang", "en");
        }
        
		/*
		 * Re-map the selected drug class codes to their descriptions, save
		 * these descriptions in a single comma-separated String, and
		 * store in a session attribute for displaying back to the user
		 * in the gui.
		 */
        SearchCriteriaBean crit = (SearchCriteriaBean) session.getAttribute(ApplicationGlobals.USER_SEARCH_CRITERIA);
		if (crit != null) {
			if (crit.getDrugClass() != null) {
				ActionUtil.mapDrugClassNames(crit.getDrugClass(), session);
			}
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
	     
	     session.setAttribute("applicationscopebean", applicationscopebean);
	     session.setAttribute("goctemplateclientbean", goctemplateclientbean);

		if (formname != null) {
			forward = new ActionForward(formname);
		} else {
			// go to the main page
			forward = new ActionForward("t.search.recherche");
		}
		return forward;
	}
}