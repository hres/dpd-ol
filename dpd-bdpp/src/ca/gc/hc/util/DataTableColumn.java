package ca.gc.hc.util;

/**
 * @author Sylvain Larivière 2016-08-15 <p>Represents a column in the DataTable
 *         plugin used in the Search Results page as part of the WET4
 *         presentation changes.</p>
 *
 */
public class DataTableColumn {
	private int columnIndex = 0;
	private String name = "";
	private String fieldName = "";
	private boolean isSearchable = false;
	private boolean isSortable = false;
	private String sortDirection = null;

	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSearchable() {
		return isSearchable;
	}

	public void setSearchable(boolean isSearchable) {
		this.isSearchable = isSearchable;
	}

	public boolean isSortable() {
		return isSortable;
	}

	public void setSortable(boolean isSortable) {
		this.isSortable = isSortable;
	}

	public String getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}

}
