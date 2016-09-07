/*
 * Created on Apr 21, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ca.gc.hc.controller.util;

import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;

import ca.gc.hc.bean.AjaxBean;
import ca.gc.hc.bean.DrugBean;
import ca.gc.hc.bean.DrugSummaryBean;
import ca.gc.hc.bean.SearchCriteriaBean;
import ca.gc.hc.bean.AjaxBean.AjaxRequestStatus;
import ca.gc.hc.dao.SearchDrugDao;
import ca.gc.hc.model.AHFS;
import ca.gc.hc.util.ApplicationGlobals;
import ca.gc.hc.util.DataTableColumn;
import ca.gc.hc.util.JsonBuilder;

/**
 * Untility class for the front end.
 */
public class ActionUtil
{
  //Get an instance for log4j
  private static Logger log = Logger.getLogger(ActionUtil.class);
  
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
        log.debug("AHFS [" + ahfs.getAhfsNumber() + "]");
        if (ahfs.getAhfsNumber().startsWith("92:01")
          || ahfs.getAhfsNumber().startsWith("92:02"))
        {
          log.debug("Flag is set.");
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

	public static void processAjaxRequest(AjaxBean ajaxBean,
			HttpServletRequest request, SearchCriteriaBean crit,
			HttpServletResponse response, ActionMessages messages) {
		try {
			initializeAjaxBean(ajaxBean, request);
			request.getSession().setAttribute(ApplicationGlobals.AJAX_STATUS,
					true);
			SearchDrugDao dao = new SearchDrugDao();
			dao.setAjaxRequest(true);
			dao.setAjaxBean(ajaxBean);
			List ajaxResults = dao.getNextResults(crit, request);
			ajaxBean.resetProcessingFlags();

			@SuppressWarnings("unchecked")
			JsonBuilder builder = new JsonBuilder(
					(List<DrugSummaryBean>) ajaxResults, ajaxBean);

			String ajaxResponse = builder
					.generateDataTableJsonResponse(ajaxResults.size());
			log.debug("ajaxResponse:" + ajaxResponse);

			response.setContentType("application/Json");
			PrintWriter out = response.getWriter();
			out.print(ajaxResponse);
			out.flush();

		} catch (Exception e) {
			log.error(e.getMessage());
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"error.failure.system"));
		}

	}

	/**
	 * @param ajaxBean
	 *            The current AjaxBean instance
	 * @param request
	 *            The current request
	 *            <p>
	 *            Adds DataTable-related parameters from the Ajax request to the
	 *            AjaxBean
	 *            </p>
	 * @author S. Larivière 2016-08-12
	 */
	private static void initializeAjaxBean(AjaxBean ajaxBean,
			HttpServletRequest request) throws Exception {
		log.debug("DataTable is requesting server-side processing");

		// Sanitize parameters with a number value that will remain a String
		ajaxBean.setEcho(Integer.valueOf(request.getParameter("sEcho"))
				.toString());
		ajaxBean.setDisplayLength(Integer.parseInt(request
				.getParameter("iDisplayLength")));
		ajaxBean.setDisplayStart(Integer.parseInt(request
				.getParameter("iDisplayStart")));
		ajaxBean.setColumnOrderMap(extractColumnOrderMap(request));
		ajaxBean.setAllColumns(extractAllColumns(request));
		extractSortColumns(request, ajaxBean);

		// general flag to specify that the above properties should now be used
		ajaxBean.setAjaxStatus(AjaxRequestStatus.ACTIVE);
	}

	/**
	 * @param request The current request
	 * @return A Map of valid sort directions based on DataTable request parameters, 
	 * keyed on the corresponding sort column index. Used to generate a collection
	 * of columns that the DataTable was sorted on (multi-select is possible).
	 * @throws Exception if any sort direction has an invalid value
	 */
	private static Map<Integer, String> extractColumnOrderMap(
			HttpServletRequest request) throws Exception {
		int sortColumnCount = Integer.valueOf(request
				.getParameter("iSortingCols"));
		Map<Integer, String> orderMap = new HashMap<Integer, String>();

		for (int i = 0; i < sortColumnCount; i++) {
			Integer sortIndex = new Integer(request.getParameter("iSortCol_"
					+ i));
			String sortOrder;
			try {
				sortOrder = safeAssignSortDirection(request.getParameter("sSortDir_" + i));
			} catch (Exception e) {
				// Invalid sort direction value
				log.error(e.getMessage() + " for column index "
						+ sortIndex);
				throw e;
			}
			orderMap.put(sortIndex, sortOrder);
		}

		return orderMap;
	}

	/**
	 * @param direction A String specifying a sort direction
	 * @return That same sort direction if valid (can only be "asc" or "desc").
	 * @throws Exception if direction has an invalid value
	 */
	private static String safeAssignSortDirection(String direction) throws Exception {
		if ("asc".equalsIgnoreCase(direction)
				|| "desc".equalsIgnoreCase(direction)) {
			return direction;
		} else {
			throw new Exception("Invalid sort direction value");
		}
	}

	/**
	 * @param request The current request
	 * @param ajaxBean The current AjaxBean 
	 * <p>Assigns to the passed AjaxBean a collection of DataTableColumn instances 
	 * that were used to sort search results (multi-select is permitted).
	 * Each sort column is assigned a sort direction ("asc" or "desc", otherwise null).</p>
	 */
	private static void extractSortColumns(
			HttpServletRequest request, AjaxBean ajaxBean) {
		int sortColumnCount = Integer.parseInt(request
				.getParameter("iSortingCols"));
		List<DataTableColumn> sortColumns = new ArrayList<DataTableColumn>();
		for (int i = 0; i < sortColumnCount; i++) {
			Integer colIndex = new Integer(request.getParameter("iSortCol_" + i));
			//get the appropriate column
			DataTableColumn col = ajaxBean.getAllColumns().get(colIndex);
			//assign it with its sort direction
			String dir = ajaxBean.getColumnOrderMap().get(colIndex);
			col.setSortDirection(dir);
			//add the column to the collection
			sortColumns.add(col);
		}
		ajaxBean.setSortColumns(sortColumns);
	}

	private static Map<Integer, DataTableColumn> extractAllColumns(
			HttpServletRequest request) throws Exception {
		int allColumnCount = Integer.parseInt(request.getParameter("iColumns"));
		Map<Integer, DataTableColumn> allColumns = new HashMap<Integer, DataTableColumn>();
		for (int i = 0; i < allColumnCount; i++) {
			DataTableColumn col = new DataTableColumn();
			col.setColumnIndex(i);
			col.setName(request.getParameter("mDataProp_" + i));
			col.setFieldName(fieldNameForColumnIndex(i));
			col.setSearchable(stringToBoolean(request
					.getParameter("bSearchable_" + i)));
			col.setSortable(stringToBoolean(request.getParameter("bSortable_"
					+ i)));
			allColumns.put(Integer.valueOf(i), col);
		}
		return allColumns;
	}

	private static String fieldNameForColumnIndex(int index) throws Exception {
		String lang = ApplicationGlobals.instance().getUserLanguage();
		String fieldName = "";
		String suffix = "";

		switch (index) {
		case 0:
			suffix = "en".equalsIgnoreCase(lang) ? "ENGLISH" : "FRENCH";
			fieldName = "EXTERNAL_STATUS_";
			break;
		case 1:
			fieldName = "drug_identification_number";
			break;
		case 2:
			fieldName = "COMPANY_NAME";
			break;
		case 3:
			fieldName = "SORT_COLUMN";
			break;
		case 4:
			suffix = "en".equalsIgnoreCase(lang) ? "" : "_f";
			fieldName = "class";
			break;
		case 5:
			suffix = "en".equalsIgnoreCase(lang) ? "ENGLISH_FNAME"
					: "FRENCH_FNAME";
			fieldName = "PM_";
			break;
		case 6:
			suffix = "en".equalsIgnoreCase(lang) ? "" : "_f";
			fieldName = "schedule";
			break;
		case 7:
			suffix = "";
			fieldName = "number_of_ais";
			break;
		case 8:
			suffix = "en".equalsIgnoreCase(lang) ? "" : "_f";
			fieldName = "ingredient";
			break;
		case 9:
			/*
			 * This column corresponds to a mix of field values conditionally
			 * concatenated together. The logic for this is currently in the jsp
			 * page. Hence it is not sortable from a single field. Therefore, it
			 * is set as non-sortable in the gui. 
			 */
			suffix = "";
			fieldName = "";
			break;
		default:
			String message = "Unable to map sort column index to a valid database field name";
			Exception e = new Exception(message);
			throw e;
		}
		return fieldName + suffix;
	}

	private static boolean stringToBoolean(String parameter) {
		return "true".equalsIgnoreCase(parameter) ? true : false;
	}

	public static void setupForServerProcessing(HttpServletRequest request,
			SearchDrugDao dao) {
		AjaxBean ajaxBean = new AjaxBean();
		int displayLength = (ApplicationGlobals.INITIAL_PAGE_LENGTH_VALUE);
		ajaxBean.setDisplayLength(displayLength);
		request.getSession().setAttribute(ApplicationGlobals.PAGE_LENGTH_KEY, displayLength);

		if (request.getSession().getAttribute(ApplicationGlobals.RESULT_COUNT_KEY) != null) {
			ajaxBean.setTotalCountBeforeFiltering((int) request.getSession().getAttribute(ApplicationGlobals.RESULT_COUNT_KEY));
		}
		
		dao.setAjaxRequest(false);
		dao.setAjaxBean(ajaxBean);

		request.getSession().setAttribute(ApplicationGlobals.AJAX_BEAN, ajaxBean);
	}


}
