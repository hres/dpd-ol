package ca.gc.hc.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/*******************************************************************************
 * An object responsible for initializing the application, which includes 
 * setting up application-wide constants.
 */
public class ApplicationInitializer implements ServletContextListener {

    /***************************************************************************
     * Notification that the web application is ready to process requests.
     */
    public void contextInitialized(ServletContextEvent sce) {
        ApplicationGlobals.connectToContext(sce.getServletContext());
    }

    /***************************************************************************
     * Notification that the servlet context is about to be shut down.
     */
    public void contextDestroyed(ServletContextEvent sce) {
        //Nothing to do.
    }
}
