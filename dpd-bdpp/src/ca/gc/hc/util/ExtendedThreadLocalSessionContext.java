package ca.gc.hc.util;

import org.hibernate.FlushMode;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.context.ThreadLocalSessionContext;
import org.hibernate.engine.SessionFactoryImplementor;

/**
 * --- NOT CURRENTLY USED ---
 * A class borrowed from the auction example at http://caveatemptor.hibernate.org
 * -Dwight Hubley, 2006-04-11
 * <br><br>
 * Trivial implementation of a session-per-conversation context.
 * <p>
 * This is how you can activate it programmatically:
 * <pre>
 *         HibernateUtil.getConfiguration().setProperty(
 *              Environment.CURRENT_SESSION_CONTEXT_CLASS,
 *              ExtendedThreadLocalSessionContext.class.getName()
 *        );
 *
 * </pre>
 * <p>
 * Or simply set the <tt>hibernate.current_session_context_class</tt>
 * configuration option to this fully qualified class name.
 * <p>
 * You have to bind/unbind your disconnected long-running Session on each requst:
 * <p>
 * <pre>
 *  ExtendedThreadLocalSessionContext.bind(hibernateSession); // Begin request
 *  // ... service executes
 *  hibernateSession = ExtendedThreadLocalSessionContext.unbind(sf); // End request
 *  // ... store hibernateSession until next request
 * </pre>
 * <p>
 * Finally, don't forget to end your long-running conversation by
 * calling <tt>flush()</tt> on your Session, and make sure you close
 * it afterwards.
 *
 * @see ca.gc.hc.nhpd.util.filter.HibernateThreadExtendedFilter
 *
 * @author christian@hibernate.org
 */
public class ExtendedThreadLocalSessionContext extends ThreadLocalSessionContext {

	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ExtendedThreadLocalSessionContext.class);

    public ExtendedThreadLocalSessionContext(SessionFactoryImplementor factory) {
        super(factory);
    }

    // No automatic flushing of the Session at transaction commit, client calls flush()
    protected boolean isAutoFlushEnabled() { return false; }
    protected Session buildOrObtainSession() {
        log.debug("Opening a new Session");
        Session s = super.buildOrObtainSession();

        log.debug("Disabling automatic flushing of the Session");
        s.setFlushMode(FlushMode.NEVER);

        return s;
    }

    // No automatic closing of the Session at transaction commit, client calls close()
    protected boolean isAutoCloseEnabled() { return false; }

    // Don't unbind after transaction completion, we expect the client to do this
    // so it can flush() and close() if needed (or continue the conversation).
    protected CleanupSynch buildCleanupSynch() {
        return new NoCleanupSynch(factory);
    }
    private static class NoCleanupSynch extends ThreadLocalSessionContext.CleanupSynch {
        public NoCleanupSynch(SessionFactory factory) { super(factory); }
        public void beforeCompletion() {}
        public void afterCompletion(int i) {}
    }
}
