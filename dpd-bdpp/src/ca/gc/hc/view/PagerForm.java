/*
 * Created on Apr 21, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ca.gc.hc.view;

import org.apache.struts.action.ActionForm;
import org.hibernate.Query;

/**
 * @author tpdwebsp
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PagerForm extends ActionForm
{
	private String totalCount = null;
	private String totalPages = null;
	private int page = 1;
	private String pageSize = null;
	private String startIndex = null;
	private String endIndex = null;
	private String offset = null;
	private String passedPages = null;
	private String pagesLeft =  null;
	/*Added SL/2012-07-20 to prevent the DisplayNextPageAction from adding duplicate results to
	 * the the last results page when the user has clicked the 'Previous Results' link before
	 * clicking 'Next Results' again. Incremented by setPage as needed.
	 */
	private int maxPageVisited = 0;	
	
	
  /**
   * @return
   */
  public String getEndIndex()
  {
    return endIndex;
  }

  /**
   * @return
   */
  public String getOffset()
  {
    return offset;
  }

  /**
   * @return
   */
  public int getPage()
  {
    return page;
  }

  /**
   * @return
   */
  public String getPageSize()
  {
    return pageSize;
  }

  /**
   * @return
   */
  public String getPagesLeft()
  {
    return pagesLeft;
  }

  /**
   * @return
   */
  public String getPassedPages()
  {
    return passedPages;
  }

  /**
   * @return
   */
  public String getStartIndex()
  {
    return startIndex;
  }

  /**
   * @return
   */
  public String getTotalCount()
  {
    return totalCount;
  }

  /**
   * @param string
   */
  public void setEndIndex(String string)
  {
    endIndex = string;
  }

  /**
   * @param string
   */
  public void setOffset(String string)
  {
    offset = string;
  }

  /**
   * Updated SL/2012-07-20 to increment maxPageVisited as required to maintain the value of the highest-numbered page visited.
   * @param int intPage The next results page to visit.
   */
  public void setPage(int intPage)
  {
    page = intPage;
    if (intPage > maxPageVisited) { maxPageVisited = intPage; }
  }

  public int getMaxPageVisited() {
	return maxPageVisited;
}

/**
   * @param string
   */
  public void setPageSize(String string)
  {
    pageSize = string;
  }

  /**
   * @param string
   */
  public void setPagesLeft(String string)
  {
    pagesLeft = string;
  }

  /**
   * @param string
   */
  public void setPassedPages(String string)
  {
    passedPages = string;
  }

  /**
   * @param string
   */
  public void setStartIndex(String string)
  {
    startIndex = string;
  }

  /**
   * @param string
   */
  public void setTotalCount(String string)
  {
    totalCount = string;
  }

  /**
   * @return
   */
  public String getTotalPages()
  {
    return totalPages;
  }

  /**
   * @param string
   */
  public void setTotalPages(String string)
  {
    totalPages = string;
  }
}
