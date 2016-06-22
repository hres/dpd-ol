package ca.gc.hc.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import ca.gc.hc.model.ProductLabel;

import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Struts File DisplayItemAction Form.
 * 
 */
public class DisplayLabelAction extends Action {

	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DisplayLabelAction.class);

//	/***************************************************************************
//	* Maps the resources used for the button text to the related action
//	* methods.
//	*/
	protected Map getKeyMethodMap() {
		Map buttonActions = new HashMap();

		// PRODUCT LABEL 
//		buttonActions.put("button.viewLabel", "viewLabel");
		buttonActions.put("button.viewPM", "viewPM");
		return buttonActions;
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		HttpSession session = request.getSession(false);
		ActionErrors errors= new ActionErrors();
		ActionForward forward= new ActionForward(); // return value
		Locale locale = this.getLocale(request);
		MessageResources resources = getResources(request);
		String strFile="";
		String path = "";
		String fileExtension = "";
		
		String labelType = (String)request.getParameter("label");
		try {
			//get the location from the property file (approved or marketed)
			String location = "";
			
			if(labelType != null && labelType.equals("marketed"))
			{
				location = resources.getMessage(locale, "marketedLocation");
			} else {
				location = resources.getMessage(locale, "labelLocation");
			}
//			location = "file:///C:/Development/2009-2010/DPD%20Online%203/Dpd_Test_Labels/";
			List productLabels = (List)session.getAttribute("product_label");

			// step 1: creation of a document-object
			Document document = new Document();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// step 2:
			// we create a writer that listens to the document
			// and directs a PDF-stream to a file

			PdfWriter writer = PdfWriter.getInstance(document, baos);

			//step 3: we open the document
			document.open();
			
			if (productLabels.size() > 1)
				// if more than one image, assume both are JPEG images for now (March 2010 v3.0 release)
			{
				java.util.Iterator productItr = productLabels.iterator();

				productItr = productLabels.iterator();

				while (productItr.hasNext()) {

					ProductLabel productLabel = new ProductLabel();
					productLabel = (ProductLabel) productItr.next();	
					if(labelType != null && labelType.equals("marketed"))
					{
						strFile = productLabel.getMarketedLebelFname();	
					} else {
						strFile = productLabel.getApprvdLabelFname();	
					}
					
					fileExtension = strFile.substring(strFile.indexOf(".")+1).toUpperCase();
					path = location + strFile;	
					log.debug("Display image: [" + path + "]");
					//step 4: add image(s) to document
					Image jpg;
					
					try
					{
						jpg = Image.getInstance(path);
					} catch(FileNotFoundException f){
						jpg = Image.getInstance(path.toLowerCase());
					}
					
					jpg.scaleToFit(500, 500);

					document.add(jpg);
					document.newPage();
				}
				//step 5: we close the document
				document.close();
			} else { // If only one image, assume it is either JPEG or PDF only for now

				ProductLabel productLabel = new ProductLabel();
				productLabel = (ProductLabel) productLabels.get(0);
				
				if(labelType != null && labelType.equals("marketed"))
				{
					strFile = productLabel.getMarketedLebelFname();	
				} else {
					strFile = productLabel.getApprvdLabelFname();	
				}
				path = location + strFile;				
				fileExtension = strFile.substring(strFile.indexOf(".")+1).toUpperCase();
				
				log.debug("Display " + fileExtension + ": [" + path + "]");
				
				if (fileExtension.equals("JPG")){
					//step 4: add image to document
					Image jpg;
					try {
						jpg = Image.getInstance(path);
					} catch (FileNotFoundException f){
						jpg = Image.getInstance(path.toLowerCase());
					}
					jpg.scaleToFit(500, 500);
					document.add(jpg);
					//step 5: we close the document
					document.close();
				}				
			}
			
			
			if (fileExtension.equals("JPG")) {
//				Get the response object output stream
				ServletOutputStream out = response.getOutputStream();
				if(labelType != null && labelType.equals("marketed"))
				{
					response.setHeader("Content-Disposition", "attachment; filename="+resources.getMessage(locale, "label.marketedLabel").replace(" ", "")+".pdf");
				} else {
					response.setHeader("Content-Disposition", "attachment; filename="+resources.getMessage(locale, "label.approvedLabel").replace(" ", "")+".pdf");					
				}

				//set the content type - it is a PDF
				response.setContentType("application/pdf");
				response.setContentLength(baos.size());
				//write the output
				baos.writeTo(out);
				//flush it out
				out.flush();
				out.close();

			}else if (fileExtension.equals("PDF")){
				URL url = new URL(path);
				// get the url info and put it in the buffered input stream
				InputStream in = new BufferedInputStream(url.openStream());
				// Get the response object output stream
				ServletOutputStream outs = response.getOutputStream();
				response.setHeader("Content-Disposition", "attachment; filename=" + strFile);
				response.setContentType("application/pdf");

				int bit = 256;
				int i = 0;
				while ((bit) >= 0) {
					bit = in.read();
					outs.write(bit);
				}
				outs.flush();
				outs.close();

			}
			// return nothing since it is opening pdf document
			forward = null;
			
		} catch (ConnectException ce) {
			log.error(ce.getMessage() + fileExtension + " : " + path);

//			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(ApplicationGlobals.ERROR_CONNECTING_URL));

			//if there is an error go back to
			forward = mapping.findForward("error");
		} catch( MalformedURLException mfue ) {
			log.error(mfue.getMessage() + fileExtension + " : " + path);
//			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(ApplicationGlobals.ERROR_CONNECTING_URL));
		} catch( UnknownHostException ukhe ) {
			log.error(ukhe.getMessage() + fileExtension + " : " + path);
//			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(ApplicationGlobals.ERROR_CONNECTING_URL));
			//if there is an error go back to
			forward = mapping.findForward("error");
		}catch(FileNotFoundException f){
			log.debug(f.getMessage());
//			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(ApplicationGlobals.ERROR_IMAGE_NOT_FOUND));
			//if there is an error go back to
			forward = mapping.findForward("error");
		}catch(IOException io){
			log.debug(io.getMessage());
//			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(ApplicationGlobals.ERROR_OPENING_IMAGE));
			//return nothing since it is opening pdf document
			forward = null;
		}catch (Exception e) {
			log.error(e.getMessage() + fileExtension + " : " + path);
			//Report the error using the appropriate name and ID.
//			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(ApplicationGlobals.ERROR_OPENING_IMAGE));

			//if there is an error go back to
			forward = mapping.findForward("error");
		}

		//If a message is required, save the specified key(s)
		// into the request.
		if (!errors.isEmpty()) {
			saveMessages(request, errors);
		}
		return forward;
	}

}