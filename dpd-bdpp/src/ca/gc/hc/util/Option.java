package ca.gc.hc.util;

/*******************************************************************************
 * A class that encapsulates an option to be displayed to the user (typically in
 * a pick list).
 */
public class Option implements Comparable {
    private String value;
    private String label;
    
    /***************************************************************************
     * Constructs an Option with the passed parameters.
     */
    public Option(String aValue, String aLabel) {
        this.value = aValue;
        this.label = aLabel;
    }

    /***************************************************************************
     * Gets the label used to describe this option to a user.
     * @return the label used to describe this option to a user.
     */
    public String getLabel() {
        return label;
    }

    /***************************************************************************
     * Gets the value of this option. This is the unique identifier.
     * @return the unique identifier of this option.
     */
    public String getValue() {
        return value;
    }

    /***************************************************************************
     * Compares this object with the specified object for order. Returns a
     * negative integer, zero, or a positive integer as this object is less than,
     * equal to, or greater than the specified object.
     * Used to sort these for display.
     * @param o the object to compare this to.
     */
    public int compareTo(Object o) throws ClassCastException {
        Option option = (Option)o;

        return getLabel().compareTo(option.getLabel());
    }
    
    /***************************************************************************
     * Over-ridden to provide useful debugging information.
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append("Option [value: ");
        buffer.append(getValue());
        buffer.append(", label: ");
        buffer.append(getLabel());
        buffer.append("]");
        
        return buffer.toString();
    }
}
