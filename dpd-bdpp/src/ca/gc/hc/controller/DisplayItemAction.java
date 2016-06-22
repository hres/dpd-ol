package ca.gc.hc.controller;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import ca.gc.hc.util.ApplicationGlobals;

/**
 * Struts File DisplayItemAction Form.
 * 
 */
public class DisplayItemAction extends Action {

	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DisplayItemAction.class);

//	/***************************************************************************
//	 * Maps the resources used for the button text to the related action
//	 * methods.
//	 */
	protected Map getKeyMethodMap() {
		Map buttonActions = new HashMap();

		// PRODUCT LABEL - TO PUT BACK LATER
//		buttonActions.put("button.viewLabel", "viewLabel");
		buttonActions.put("button.viewPM", "viewPM");
		return buttonActions;
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HttpSession session = request.getSession(false);

		ActionMessages messages = new ActionMessages();
		ActionForward forward = new ActionForward(); // return value

		if (session.getAttribute("sessionActive") != null) {
			Locale locale = this.getLocale(request);
			MessageResources resources = getResources(request);
			String strFile = "";
			try {

				String strFileName = (String)request.getParameter("pm-mp");
				
				strFileName = strFileName + ".PDF";
				
				//get the server name and pm location
				String strLocation = resources.getMessage(locale, "pmLocation"); 
				strFile =  strLocation + strFileName;
				URL url = new URL(strFile);
				// get the url info and put it in the buffered input stream
				InputStream in = new BufferedInputStream(url.openStream());
				// Get the response object output stream
				ServletOutputStream outs = response.getOutputStream();
				response.setHeader("Content-Disposition", "attachment; filename=PM" + strFileName);
				response.setContentType("application/pdf");

				int bit = 256;
				int i = 0;
				while ((bit) >= 0) {
					bit = in.read();
					outs.write(bit);
				}
				outs.flush();
				outs.close();

				// return nothing since it is opening pdf document
				forward = null;
			} catch (ConnectException ce) {
				log.error(ce.getMessage() + "PDF : " + strFile);
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						ApplicationGlobals.ERROR_CONNECTING_URL));

				// if there is an error go back to
				forward = mapping.findForward("error");
			} catch (FileNotFoundException f) {
				log.debug(f.getMessage());
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						ApplicationGlobals.ERROR_PDF_NOT_FOUND));
				// if there is an error go back to
				forward = mapping.findForward("error");
			} catch (MalformedURLException mfue) {
				log.error(mfue.getMessage() + "PDF : " + strFile);
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						ApplicationGlobals.ERROR_CONNECTING_URL));
			} catch (UnknownHostException ukhe) {
				log.error(ukhe.getMessage() + "PDF : " + strFile);
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						ApplicationGlobals.ERROR_CONNECTING_URL));
				// if there is an error go back to
				forward = mapping.findForward("error");

			} catch (IOException io) {
				log.debug(io.getMessage());
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						ApplicationGlobals.ERROR_OPENING_PDF_DOCUMENT));
				// return nothing since it is opening pdf document

				forward = null;
			} catch (Exception e) {
				log.error(e.getMessage() + "PDF : " + strFile);
				// Report the error using the appropriate name and ID.
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
						ApplicationGlobals.ERROR_OPENING_PDF_DOCUMENT));

				// if there is an error go back to
				forward = mapping.findForward("error");
			}
			// If a message is required, save the specified key(s)
			// into the request.
			if (!messages.isEmpty()) {
				saveMessages(request, messages);
			}
		} else {
			forward = mapping.findForward("sessionTimeout");
		}
		return forward;

	}
}