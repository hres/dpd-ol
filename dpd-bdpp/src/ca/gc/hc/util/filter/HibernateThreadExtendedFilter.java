package ca.gc.hc.util.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StaleObjectStateException;

import ca.gc.hc.util.ExtendedThreadLocalSessionContext;
import ca.gc.hc.util.HibernateUtil;

/**
 * --- NOT CURRENTLY USED ---
 * A class borrowed from the auction example at http://caveatemptor.hibernate.org
 * -Dwight Hubley, 2006-04-11
 * <br><br>
 * A servlet filter that provides a thread-bound session-per-conversation.
 * <p>
 * This filter should be used if your <tt>hibernate.current_session_context_class</tt>
 * configuration is set to <tt>thread</tt> and you are not using JTA/CMT. You can use
 * this filter for a thread-bound <tt>Session</tt>, either with resource-local transactions
 * (direct JDBC) or JTA transactions. Set your <tt>hibernate.transaction.factory_class</tt>
 * accordingly.
 * <p>
 * Use this filter for the <b>session-per-conversation</b> pattern
 * with an extended <tt>Session</tt>. Don't forget to set conversation boundaries
 * in your code, as described in Hibernate in Action. One way to do this is to
 * put a marker attribute in the request, and let the filter handle flushing and
 * closing of the Session at the end of a conversation.
 * <p>
 * We recommend the JBoss Seam framework with built-in support for conversations in
 * all environments.
 * <p>
 * Note that you should not use this interceptor out-of-the-box with enabled optimistic
 * concurrency control. Apply your own compensation logic for failed conversations, this
 * is totally dependent on your applications design.
 *
 * @see ca.gc.hc.nhpd.util.ExtendedThreadLocalSessionContext
 *
 * @author Christian Bauer <christian@hibernate.org>
 */
public class HibernateThreadExtendedFilter
            implements Filter {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(HibernateThreadExtendedFilter.class);

    private SessionFactory sf;
    
    public static final String HIBERNATE_SESSION_KEY    = "hibernate_session";
    public static final String END_OF_CONVERSATION_FLAG = "end_of_conversation";

    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {


        // Try to get a Hibernate Session from the HttpSession
        HttpSession httpSession =
                ((HttpServletRequest) request).getSession();
        Session hibernateSession =
                (Session) httpSession.getAttribute(HIBERNATE_SESSION_KEY);

        try {

            if (hibernateSession != null) {
                log.debug("< Continuing conversation");
                ExtendedThreadLocalSessionContext.bind(hibernateSession);
            } else {
                log.debug(">>> New conversation");
            }

            log.debug("Starting a database transaction");
            sf.getCurrentSession().beginTransaction();

            // Do the work...
            chain.doFilter(request, response);

            // End or continue the long-running conversation?
            if (request.getAttribute(END_OF_CONVERSATION_FLAG) != null) {

                log.debug("Flushing Session");
                sf.getCurrentSession().flush();

                log.debug("Committing the database transaction");
                sf.getCurrentSession().getTransaction().commit();

                log.debug("Closing and unbinding Session from thread");
                sf.getCurrentSession().close(); // Unbind is automatic here

                log.debug("Removing Session from HttpSession");
                httpSession.setAttribute(HIBERNATE_SESSION_KEY, null);

                log.debug("<<< End of conversation");

            } else {

                log.debug("Committing database transaction");
                sf.getCurrentSession().getTransaction().commit();

                log.debug("Unbinding Session from thread");
                hibernateSession = ExtendedThreadLocalSessionContext.unbind(sf);

                log.debug("Storing Session in the HttpSession");
                httpSession.setAttribute(HIBERNATE_SESSION_KEY, hibernateSession);

                log.debug("> Returning to user in conversation");
            }

        } catch (StaleObjectStateException staleEx) {
            log.error("This interceptor does not implement optimistic concurrency control!");
            log.error("Your application will not work until you add compensation actions!");
            // Rollback, close everything, possibly compensate for any permanent changes
            // during the conversation, and finally restart business conversation. Maybe
            // give the user of the application a chance to merge some of his work with
            // fresh data... what you do here depends on your applications design.
            throw staleEx;
        } catch (Throwable ex) {
            // Rollback only
            try {
                if (sf.getCurrentSession().getTransaction().isActive()) {
                    log.debug("Trying to rollback database transaction after exception");
                    sf.getCurrentSession().getTransaction().rollback();
                }
            } catch (Throwable rbEx) {
                log.error("Could not rollback transaction after exception!", rbEx);
            } finally {
                log.error("Cleanup after exception!");

                // Cleanup
                log.debug("Closing and unbinding Session from thread");
                sf.getCurrentSession().close(); // Unbind is automatic here

                log.debug("Removing Session from HttpSession");
                httpSession.setAttribute(HIBERNATE_SESSION_KEY, null);

            }

            // Let others handle it... maybe another interceptor for exceptions?
            throw new ServletException(ex);
        }

    }

    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("Initializing filter, obtaining Hibernate SessionFactory from HibernateUtil");
        sf = HibernateUtil.getSessionFactory();
    }

    public void destroy() {}

}