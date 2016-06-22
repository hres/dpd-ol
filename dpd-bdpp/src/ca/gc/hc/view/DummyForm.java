package ca.gc.hc.view;

import org.apache.struts.action.ActionForm;

/*******************************************************************************
 * An Action Form used when one isn't really required. All Action Mappings seem
 * to require an ActionForm - even those that shouldn't require one (like ones
 * that are simply forwarding to a Global Forward). When none is really required,
 * use this so the intent is obvious.
 */
public class DummyForm extends ActionForm {
    
    public DummyForm() {
        //System.err.println("Creating DummyForm...");
    }

}
