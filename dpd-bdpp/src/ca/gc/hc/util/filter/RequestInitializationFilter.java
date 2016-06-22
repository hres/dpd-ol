package ca.gc.hc.util.filter;

import ca.gc.hc.util.ApplicationGlobals;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/*******************************************************************************
 * An object used to do the required initialization when a new Request is about
 * to be processed. This is implemented here because the
 * javax.servlet.ServletRequestListener interface is not available until
 * Servlet 2.4
 */
public class RequestInitializationFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
                throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            
//            //System.err.println("updating localization");
            ApplicationGlobals.instance().initLocalization((HttpServletRequest)request);
        }
        // Call the next filter (continue request processing)
        chain.doFilter(request, response);
    }

    public void init(FilterConfig filterConfig) throws ServletException {}

    public void destroy() {}
}
