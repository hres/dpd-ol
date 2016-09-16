package ca.gc.hc.util;

import java.util.ArrayList;
import java.util.List;

import ca.gc.hc.bean.AjaxBean;
import ca.gc.hc.bean.DrugSummaryBean;
import ca.gc.hc.bean.JsonSummaryBean;

import com.google.gson.Gson;

/**
 * @author S. Larivière 2016-08-11
 *         <p>
 *         Responsible for converting a standard DrugSummaryBean to JSON format
 *         and generating the Ajax response to a DataTable that is operating in
 *         server-side processing mode.
 *         </p>
 * 
 */
public class JsonBuilder extends LocaleDependantObject {
	private AjaxBean ajaxBean;
	private List<JsonSummaryBean> jsonBeansList = new ArrayList<JsonSummaryBean>();

	// Get an instance for log4j
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(JsonBuilder.class);

	public JsonBuilder(List<DrugSummaryBean> jspSsearchResults,
			AjaxBean ajaxBean) throws Exception {
		this.ajaxBean = ajaxBean;
		toJsonSummaryList(jspSsearchResults);
	}

	/**
	 * @param jspBean
	 *            A DrugSumaryBean containing search results
	 * @return A JsonSummaryBean matching the DataTable columns
	 * @throws Exception
	 */
	public JsonSummaryBean toJsonSummary(DrugSummaryBean jspBean)
			throws Exception {
		JsonSummaryBean jsonBean = new JsonSummaryBean();

		if (hasValidColumnCount(jsonBean, ajaxBean.getColumnCount())) {
			jsonBean.setStatus(jspBean.getStatus());
			jsonBean.setDin(generateDinUrl(jspBean));
			jsonBean.setCompany(jspBean.getCompanyName());
			jsonBean.setBrand(jspBean.getBrandName());
			jsonBean.setDrugClass(jspBean.getDrugClass());
			jsonBean.setAIStrength(jspBean.getAiStrengthAndDosageText());
			jsonBean.setMajorAI(jspBean.getFirstAIName());
			jsonBean.setAiNum(jspBean.getNumberOfAis().toString());
			String hasPm = StringsUtil.hasData(jspBean.getPm()) ? localisedProperty(
					"Yes", "Oui") : localisedProperty("No", "Non");
			jsonBean.setPm(hasPm);
			jsonBean.setSchedule(jspBean.getSchedule());
		} else {
			String message = "The source and destination beans have a different number of declared fields. Conversion failed.";
			Exception e = new Exception(message);
			log.error("Message", e);
			throw e;
		}

		return jsonBean;
	}

	private String generateDinUrl(DrugSummaryBean jspBean) {
		String drugCode = Long.toString(jspBean.getDrugCode());
		String lang = ApplicationGlobals.instance().getUserLanguage();

		// ex: <a href="/dpd-bdpp/info.do?code=81548&amp;lang=en">01234567</a>
		StringBuilder sb = new StringBuilder(
				"<a href=\"/dpd-bdpp/info.do?code=");
		sb.append(drugCode);
		sb.append("&amp;lang=");
		sb.append(ApplicationGlobals.instance().getUserLanguage());
		sb.append("\">");
		sb.append(jspBean.getDin());
		sb.append("</a>");

		return sb.toString();
	}

	/**
	 * @param jsonBean
	 * @param validCount
	 * @return True if jsonBean has a number of declared fields equal to
	 *         validCount + 1 (to account for the additional, inherited
	 *         userLocale field)
	 */
	boolean hasValidColumnCount(JsonSummaryBean jsonBean, int validCount) {
		return jsonBean.getClass().getDeclaredFields().length == validCount + 1;
	}

	private void toJsonSummaryList(List<DrugSummaryBean> jspResultsList)
			throws Exception {
		for (DrugSummaryBean jspBean : jspResultsList) {
			this.jsonBeansList.add(toJsonSummary(jspBean));
		}

	}

	public String generateDataTableJsonResponse(int resultsCount) {
		StringBuilder buf = new StringBuilder("{");

		buf.append("\"sEcho\": ");
		buf.append(ajaxBean.getEcho() + ",");
		buf.append("\"iTotalRecords\": ");
		buf.append(ajaxBean.getTotalCountBeforeFiltering() + ",");
		buf.append("\"iTotalDisplayRecords\": ");
		buf.append((ajaxBean.hasFilteredCount() ? ajaxBean
				.getTotalCountAfterFiltering() : ajaxBean
				.getTotalCountBeforeFiltering())
				+ ",");
		buf.append("\"aaData\": ");
		buf.append(jsonResults());
		buf.append("}");

		return buf.toString();
	}

	private String jsonResults() {
		Gson gson = new Gson();

		return gson.toJson(jsonBeansList);
	}

}
