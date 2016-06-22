/*
 * Created on Apr 16, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ca.gc.hc.util;

import java.util.Comparator;

/**
 * 
 */
public class BilingualComparator implements Comparator
{
  public int compare(Object o1, Object o2)
  {
    String clean1 = StringsUtil.toASCII7(o1.toString());
    String clean2 = StringsUtil.toASCII7(o2.toString());
    int value = clean1.compareTo(clean2);
    return value;
  }
}