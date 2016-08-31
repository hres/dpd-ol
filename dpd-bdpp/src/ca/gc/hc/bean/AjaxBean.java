package ca.gc.hc.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.gc.hc.util.DataTableColumn;

/**
 * @author Sylvain Larivière 2016-08-22
 *         <p>
 *         Bean used to capture DataTable parameters when they are operating in
 *         server-side processing mode. In this mode the DataTable submits an
 *         Ajax request for any event raised in the DataTable, for the servlet
 *         to handle.
 *         </p>
 */
public class AjaxBean {
	public enum AjaxRequestStatus {
		ACTIVE, INACTIVE
	};

	private int displayLength = 0;
	private int displayStart = 0;
	private int totalCountBeforeFiltering = 0;
	private int totalCountAfterFiltering = 0;
	private String echo = "";
	/* Map of all column names keyed on their column index */
	private Map<Integer, DataTableColumn> allColumns = new HashMap<Integer, DataTableColumn>();
	/*
	 * List of columns that are used for sorting (each DataTableColumn instance
	 * knows its sort direction)
	 */
	private List<DataTableColumn> sortColumns = new ArrayList<DataTableColumn>();
	/*
	 * Map of sort column indexes and the corresponding sort direction ("asc" or
	 * "desc"), from DataTable parameters, used to build the sortColumns
	 * collection. DataTables rows can be ordered by multiple columns by
	 * clicking on the sort arrow with the Shift key depressed.
	 */
	private Map<Integer, String> columnOrderMap = new HashMap<Integer, String>();

	private AjaxRequestStatus ajaxStatus = AjaxRequestStatus.INACTIVE;

	private boolean isUpdatingSorting = false;
	private boolean isUpdatingPageLength = false;
	private boolean isFiltering = false;
	private boolean isUpdatingPageNumber = false;

	public int getDisplayLength() {
		return displayLength;
	}

	public void setDisplayLength(int displayLength) {
		// assert if state was changed from its initial value 
		if (this.displayLength > 0 && this.displayLength != displayLength) {
			isUpdatingPageLength = true;
		} 
		this.displayLength = displayLength;
	}

	public int getDisplayStart() {
		return displayStart;
	}

	public void setDisplayStart(int displayStart) {
		// assert if state has changed from its initial value
		if (this.displayStart != displayStart) {
			isUpdatingPageNumber  = true;
		} 
		this.displayStart = displayStart;
	}

	public String getEcho() {
		return echo;
	}

	public void setEcho(String echo) {
		this.echo = echo;
	}

	public AjaxRequestStatus getAjaxStatus() {
		return ajaxStatus;
	}

	public void setAjaxStatus(AjaxRequestStatus ajaxStatus) {
		this.ajaxStatus = ajaxStatus;
	}

	public int getTotalCountBeforeFiltering() {
		return totalCountBeforeFiltering;
	}

	public void setTotalCountBeforeFiltering(int totalResultsCount) {
		this.totalCountBeforeFiltering = totalResultsCount;
	}

	public boolean hasFilteredCount() {
		return (totalCountAfterFiltering > 0);
	}

	public int getTotalCountAfterFiltering() {
		return totalCountAfterFiltering;
	}

	public void setTotalCountAfterFiltering(int totalCountAfterFiltering) {
		this.totalCountAfterFiltering = totalCountAfterFiltering;
	}

	public Map<Integer, DataTableColumn> getAllColumns() {
		return allColumns;
	}

	public void setAllColumns(Map<Integer, DataTableColumn> allColumns) {
		this.allColumns = allColumns;
	}

	public int getColumnCount() {
		return allColumns.size();
	}

	public List<DataTableColumn> getSortColumns() {
		return sortColumns;
	}

	public void setSortColumns(List<DataTableColumn> sortColumns) {
		// assert if state has changed from its initial value
		if (!this.sortColumns.isEmpty()
				&& !this.sortColumns.equals(sortColumns)) {
			isUpdatingSorting = true;
		}
		this.sortColumns = sortColumns;
	}

	public Map<Integer, String> getColumnOrderMap() {
		return columnOrderMap;
	}

	public void setColumnOrderMap(Map<Integer, String> columnOrderMap) {
		this.columnOrderMap = columnOrderMap;
	}

	public boolean isUpdatingSorting() {
		return isUpdatingSorting;
	}

	public boolean isUpdatingPageLength() {
		return isUpdatingPageLength;
	}

	public boolean isFiltering() {
		return isFiltering;
	}

	public boolean isUpdatingPageNumber() {
		return isUpdatingPageNumber;
	}

	public void resetProcessingFlags() {
		isUpdatingSorting = false;
		isUpdatingPageLength = false;
		isFiltering = false;
		isUpdatingPageNumber = false;
	}
}
