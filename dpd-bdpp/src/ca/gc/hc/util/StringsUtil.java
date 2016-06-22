/*
 * Created on Apr 16, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ca.gc.hc.util;

import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import ca.gc.hc.controller.DisplayItemAction;


/**
	Extensions to {@link java.lang.String}.

	<ul>
	<li>possible object sharing: {@link #valueOf(String)}, {@link #valueOf(StringBuffer)}, {@link #valueOf(char)}
	<li>sorting in dictionary order: {@link #DICTIONARY_ORDER}, {@link #DICTIONARY_CASE_INSENSITIVE_ORDER},
		{@link #compareDictionary(String, String, boolean)}
	<li>{@link #trim(String, String)}, {@link #trimWhitespace(String)}, {@link #trimPunct(String)},
	<li>translate/convert/format: {@link #casify(String, String, Map)}, {@link #toASCII7(String)}, {@link #fromPigLatin(String)},
		raw {@link #getBytes(String)} without character set encoding
		<!-- @link #javaString2raw(String)}, @link #raw2javaString(String)}, -->
	<li>algorithms: {@link #minEditDistance(String, String)}
	</ul>

	@version $Revision: 1.3.6.1 $ $Date: 2015/08/20 12:36:55 $
 */
public class StringsUtil
{
	public static String PUNCT = ".?!,:;()[]'\"";
	private static Logger log = Logger.getLogger(StringsUtil.class);

	// list of stop words doesn't have to be complete as -- seems there should be a standard list somewhere
	/**
  A list of common English words, useful in several applications.
  => put this in text file.  App-specific.
  @see multivalent.std.adaptor.ManualPage
  public static final String[] STOPWORDS = {
  "a", "an", "the",
  "who", "I", "we", "us", "he", "him", "his", "she", "her", "them", "they", "it", "this", "that", "these", "those",
  "be", "being", "am", "was", "were", "is", "are",
  "did", "do", "done", "doing", "make", "made", "making", "take", "took",
  "what", "why", "well", "since", "still", "together", "toward", "while",
  "of", "from", "to", "for", "in", "on", "into", "at", "by", "about",
  "if", "when", "then", "and", "or", "either", "neither", "but", "with", "without", "that", "which", "because",
  "where", "here", "there", "everywhere", "somewhere", "above", "below", "around",
  "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "hundred", "thousand", "million",
  "some", "also", "many", "few", "most", "each", "every", "another", "again",
  "yes", "no", "maybe", "true", "false",
  };
	 */

	/**
  A list of common computer-related words.
  => put this in text file.  App-specific.
  public static final String[] COMPUTERWORDS = {
  "computer", "user", "Java", "Sun", "Solaris", "Apple", "Macintosh", "Microsoft", "Windows",
  "kilo", "mega", "giga",
  "WWW", "HTML", "HTTP", "homepage",
  };
	 */

	/**
  Canonical array of single-character strings.
  Since we're putting each word into a separate Node, we can save a little by not allocating many copies of "a", "i", "1", ....
  Would like to do this with common words too, but how to be efficient as new String() 3x faster than hash lookup.
  Would like to put this in INode.setName()/Leaf.setName(), but by the time that's called, already created new String, so saves memory (after gc!) but not time.
	 */
	private static String[] SINGLECHARSTRING = new String[0x100];
	static {
		for (int i = 0, imax = SINGLECHARSTRING.length; i < imax; i++)
			SINGLECHARSTRING[i] = String.valueOf((char) i).intern();
	}

	private static final int OFF_ACCENT = 160, OFF_GREEK = 913;
	private static final String[] unicode2ascii_ =
		new String[376 + 1 - OFF_ACCENT]
		           /*,
  	greek2ascii_ = new String[977+1 - OFF_GREEK]/*, ascii2greek_ = new String['Z'+1]*/;
	static {
		String[] str = {
				// 7-bit ASCII
				"160, ,!,c,L,o,Y,|,s,XXX,(C),a,<<,-,XXX,(R),-,o,+-,2,3,XXX,u,P,.,XXX,1,o,>>,1/4,1/2,3/4,?"
				+ ",A,A,A,A,A,A,A,C,E,E,E,E,I,I,I,I,D,N,O,O,O,O,O,x,O,U,U,U,U,Y,th,ss"
				+ ",a,a,a,a,a,a,a,c,e,e,e,e,i,i,i,i,d,n,o,o,o,o,o,/,o,u,u,u,u,y,th,y",
				"338,OE,oe",
				"352,S,s",
				"376,Y",

				// Greek
				/*"913,A,B,G,D,E,Z,E,Th,I,K,L,M,N,Ks,O,P,R,S,T,U,F,Ch,Ps,O",
      "945,a,b,g,d,e,z,e,th,i,k,l,m,n,ks,o,p,r,s,s,t,u,f,ch,ps,o",
      //"977", "thetasym","", "upsih","", "982", "piv","",*/
		};

		for (int i = 0, imax = str.length; i < imax; i++)
		{
			StringTokenizer st = new StringTokenizer(str[i], ",");
			int x = Integer.parseInt(st.nextToken());
			while (st.hasMoreTokens())
			{
				String sub = valueOf(st.nextToken());
				if ("XXX".equals(sub))
				{
				} // skip -- can't have empty token
				else if (x <= 376)
					unicode2ascii_[x - OFF_ACCENT] = sub;
				//else { greek2ascii_[x - OFF_GREEK] = sub; /*ascii2greek_[sub.charAt(0)] = valueOf(x);*/ }
				x++;
			}
		}
	}

	private StringsUtil()
	{
	}

	/**
  Returns byte array of low byte of each character.
  Like {@link java.lang.String#getBytes()} but no encoding,
  and {@link java.lang.String#getBytes(int, int, byte[], int)} but not deprecated.
	 */
	public static byte[] getBytes(String s)
	{
		if (s == null)
			s = "";
		int len = s.length();
		byte[] b = new byte[len];
		for (int i = 0; i < len; i++)
			b[i] = (byte) s.charAt(i);
		return b;
	}

	/*public static String parseString(String val, String defaultval) { => behaviors use getAttr
  return (val!=null? val: defaultval);
  }*/

	/**
  Canonicalizes single character strings for which 0<=charAt(0)<=255.
	 */
	public static String valueOf(String str)
	{
		String s;
		if (str == null)
			s = null;
		else if (str.length() == 0)
			s = "";
		else if (str.length() == 1 && str.charAt(0) < SINGLECHARSTRING.length)
			s = SINGLECHARSTRING[str.charAt(0)];
		else
			s = str;
		return s;
	}

	/**
  Return possibly shared String.
  If String is 1-character long and char<256, then guaranteed shared.
	 */
	public static String valueOf(StringBuffer sb)
	{
		String s;
		if (sb == null)
			s = null;
		else if (sb.length() == 0)
			s = "";
		else if (sb.length() == 1 && sb.charAt(0) < SINGLECHARSTRING.length)
			s = SINGLECHARSTRING[sb.charAt(0)];
		else
			s = sb.substring(0); // vs .toString() -- space or speed?
		return s;
	}

	public static String valueOf(char ch)
	{
		return ch < SINGLECHARSTRING.length
		? SINGLECHARSTRING[ch]
		                   : String.valueOf(ch);
	}
	//public static String valueOf(int ch) { return ch < SINGLECHARSTRING.length? SINGLECHARSTRING[ch]: String.valueOf((char)ch); }
	//public static String valueOf(String val, Map canon) {}

	/** Trim letters in passed chars from ends of word. */
	public static String trim(String txt, String chars)
	{
		return trim(txt, chars, 0, txt.length() - 1);
	}
	public static String trim(String txt, String chars, int start, int end)
	{
		//if (txt==null) return null; -- go boom
		while (start < end && chars.indexOf(txt.charAt(start)) != -1)
			start++;
		while (end >= start && chars.indexOf(txt.charAt(end)) != -1)
			end--;
		return (start <= end ? txt.substring(start, end + 1) : "");
	}

	/**
  Return 7-bit ASCII transcription of Unicode by removing accents (e.g., "&Aacute;"=>"A") and making other character substitutions (e.g., "&copy;" => "(C)").
  If no changes to remove, return <var>txt</var>.
	 */
	public static String toASCII7(String txt)
	{ // like toUpperCase()
		for (int i = 0, imax = txt.length(); i < imax; i++)
		{
			char ch = txt.charAt(i);
			if (ch >= 128)
			{
				String[] u2a = unicode2ascii_;
				int end = OFF_ACCENT + u2a.length;
				StringBuffer sb = new StringBuffer(txt.length());
				for (int j = 0, jmax = txt.length(); j < jmax; j++)
				{
					ch = txt.charAt(j);
					if (ch < 128)
						sb.append(ch);
					else if (
							OFF_ACCENT <= ch && ch < end && u2a[ch - OFF_ACCENT] != null)
						sb.append(u2a[ch - OFF_ACCENT]);
					//else skip char
				}
				txt = sb.toString();
				break;
			}
		}
		return txt;
	}

	/**
  Returns the minimum number of operations to transform one string into the other.
  An operation is insert character, delete character, substitute character.
	 */
	public static int minEditDistance(String a, String b)
	{
		if (a == b)
			return 0;
		else if (a == null || a.equals(""))
			return b.length();
		else if (b == null || b.equals(""))
			return a.length();
		else if (a.equals(b))
			return 0;

		// dynamic programming
		int alen = a.length() + 1, blen = b.length() + 1;
		int[][] c = new int[alen][blen];

		for (int i = 0; i < alen; i++)
			c[i][0] = i;
		for (int j = 0; j < blen; j++)
			c[0][j] = j;

		for (int i = 1; i < alen; i++)
		{
			for (int j = 1; j < blen; j++)
			{
				int scost =
					c[i - 1][j - 1] + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1);
				int dcost = 1 + c[i - 1][j];
				int icost = 1 + c[i][j - 1];

				c[i][j] = Math.min(scost, Math.min(dcost, icost));
			}
		}

		return c[alen - 1][blen - 1];
	}

	/**
	 * Method trimStringLeft.
	 * Purpose: If substring, exists in string, shorten string by removing characters to the left
	 * of the substring.  If removeSubstring = true, remove substring as well.
	 * @param string
	 * @param subString
	 * @param removeSubstring
	 * @return String
	 */
	public static String trimStringLeft(String s, String subString, boolean removeSubstring) {
		String value= new String("");
		int lastIndex= (s.lastIndexOf(subString));
		if (removeSubstring == true) {
			lastIndex += subString.length();
		}
		value= s.substring(lastIndex, s.length());
		return value;
	}

	/**
	 * @param s A String to be checked for single quotes.
	 * @return The String passed in, with any single quote doubled.
	 * Used where a search string contains an apostrophe. 
	 * <p>Single quotes inside SQL statements will raise an Oracle error.</p>
	 * @author Sylvain LariviËre
	 */
	public static String doubleEmbeddedQuotes(String s) {
		String result = "";

		if (s != null) {
			result = s.replace("'", "''");
		}else{
			result = s;
		}
		return result;
	}

	/**
	 * @param original A String to check for null
	 * @param substitute A String to substitute for original if the latter is null
	 * @return The original String, or the substitute if the original was null. An empty string if 
	 * both original and substitute were null.
	 * <p>This version of the method, without the Locale parameter, is mostly used where English data
	 * is mandatory, and it is required to be displayed in a French field should the French equivalent 
	 * not be present. </p>
	 * @author Sylvain LariviËre 2012-08-15
	 */
	public static String substituteIfNull(String original, String substitute) {
		String result = new String("");
		if (original == null && substitute == null) {
			//log.debug("Original= " + original + ", " + "substitute= " + substitute + "; " + "substituteIfNull returns empty string");
		}else{
			result = (original == null ? substitute : original);
			//log.debug("Original= " + original + ", " + "substitute= " + substitute + "; " + "substituteIfNull returns " + result);
		}
		return result;
	}
	
	/**
	 * @param frenchValue A String representing the French version of an attribute.
	 * @param englishValue A String representing the corresponding English equivalent.
	 * @param userLocale The Locale of the current user, determining the user language.
	 * @return Either frenchValue or englishValue, depending on the user language. If the appropriate
	 * value is null in the user language, return the value in the other language. If both are null, 
	 * return an empty String.<br/>
	 * This version of the method (with a Locale) is used where requirements specify that either the 
	 * French or the English attribute can be null.
	 * @author Sylvain LariviËre 2012-09-28
	 */
	public static String substituteIfNull(String frenchValue, String englishValue, Locale userLocale) {
		String original = "";
		String substitute = "";
		String userLanguage = userLocale.getLanguage();
		
		if (userLanguage.equals(ApplicationGlobals.LANG_EN)) {
			original = englishValue;
			substitute = frenchValue;
		}else {
			original = frenchValue;
			substitute = englishValue;
		}
		return substituteIfNull(original, substitute);
	}
	
	/**
	 * @param A String s
	 * @return True if the passed String is null or has a zero length; false otherwise.
	 * @author Sylvain LariviËre 2012-06-27
	 */
	public static boolean isEmpty( String s) {
		return (s == null || s.trim().length() < 1) || s.trim().equals("");
	}

	/**
	 * @param A String s
	 * @return True if the passed String is not null and has a length greater than zero; false otherwise.
	 * @author Sylvain LariviËre 2012-06-27
	 */
	public static boolean hasData( String s) {
		return (s != null && s.trim().length() > 0);
	}
	
	/**
	 * Translates a string to uppercase, 
	 * then strips all accented characters of their accent
	 * Sylvain LariviËre, 15-Nov-07
	 * @param s
	 * @return the uppercase, unaccented string
	 */
	public static String AsUnAccentedUppercase(String s) {
		String result= "";
		result= Translate(s.toUpperCase(), new String("¿¬ƒ«»…À ÃŒœ“‘÷Ÿ⁄€‹"), 
			new String("AAACEEEEIIIOOOUUUU"));
		return result;
		}
	
	/**
	 * Translates characters from a String using a list of characters to translate, 
	 * and a corresponding list of characters to use instead.
	 * Used here to strip accents from strings, for searching purposes.
	 * Sylvain LariviËre, 15-Nov-07
	 * @param s
	 * @param fromList
	 * @param toList
	 * @return the translated string
	 */
	private static String Translate(String s, String fromList, String toList) {
		String character;
		int mapIndex= 0;
		String result= "";
		
		if (s != null) {
			for(int i= 0; i< s.length(); i++) {
				 character = s.substring(i, i+1);
				 mapIndex= fromList.indexOf(character);
				 if (mapIndex > -1) {
				 	result= result.concat(toList.substring(mapIndex,mapIndex+1));
				 } else {
				 	result= result.concat(character);
				 }
			}
		}
		return result;
	}

}
