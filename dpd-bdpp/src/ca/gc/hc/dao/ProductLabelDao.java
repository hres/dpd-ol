/**
 * Name:  ProductLabelDao
 * Purpose:  Data Access Object to create, read, update object for table WQRY_PRODUCT_LABEL
 * Date: October 2006
 * Author: Diane Beauvais
 * 
 */
package ca.gc.hc.dao;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;

import ca.gc.hc.model.ProductLabel;


/*******************************************************************************
 * An object used to retrieve instances of ApplicationUser from persistent store.
 * @see ca.gc.hc.nhpd.model.ApplicationUser
 */
public class ProductLabelDao extends AbstractDao {
	/***************************************************************************
	 * Generic Constructor.
	 */
	private final static String DUPLICATE_ROW_EXCEPTION_MSG = "than one row with the given identifier";

	public ProductLabelDao() {
		super(ProductLabel.class);
	}
	
	/**
	* The local instance of the LOG4J Logger.
	*/
	
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ProductLabelDao.class);
	
	/***************************************************************************
	 * Gets the a productLabel by drug code
	 * Returns an empty object if none are found that match.
	 * @param drugCode
	 * @return the product label by drug code
	 */
	  public List searchLabelByDrugCode(Long drugCode) throws Exception
	  {
		List productLabels = new ArrayList();
		//ProductLabel label = new ProductLabel();
		try
		{
	  
			String query = "select label.* from DPD_ONLINE_OWNER.WQRY_LABEL label"
			  + " where label.DRUG_CODE = " + drugCode.longValue()
			  + " order by label.MARKETED_LABEL_NO";
		
			productLabels = getSession().createSQLQuery(query).
			addEntity("u",ProductLabel.class).list();
							
			log.debug("Search for Product Label By drugCode Query is: " + query);

	
		} catch (HibernateException he)
		{
		  if (he.getMessage().indexOf(DUPLICATE_ROW_EXCEPTION_MSG) > 0)
		  {
			log.warn("Data Problem: " + he.getMessage());
			return productLabels;
		  } else
		  {
			log.error("Stack Trace: ", he);
			StringBuffer message = new StringBuffer("Search Product Label By drugCode [");
			message.append(drugCode);
			message.append("] failed");
			throw new Exception(message.toString());
		  }
		}
		return productLabels;
	  }
}
