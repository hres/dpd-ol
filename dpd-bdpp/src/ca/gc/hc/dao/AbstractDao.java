package ca.gc.hc.dao;

import ca.gc.hc.util.HibernateUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;

/*******************************************************************************
 * Loosely based on org.hibernate.ce.auction.dao.hibernate.GenericHibernateDAO,
 * a class in the auction example at http://caveatemptor.hibernate.org.
 * Converted from Java 1.5 and adjusted to use HibernateUtil directly as a
 * source of Sessions rather than keep them in an instance variable. This allows
 * instances to be thread-safe and therefore re-usable rather than requiring
 * them to be instantiated and subsequently discarded for each request.
 * -Dwight Hubley, 2006-04-12
 * <br><br>
 * Implements the generic CRUD data access operations using Hibernate APIs.
 * <p>
 * To write a DAO, subclass and parameterize this class with your persistent class.
 * Of course, assuming that you have a traditional 1:1 appraoch for Entity:DAO design.
 * <p>
 * You have to inject the <tt>Class</tt> object of the persistent class to
 * construct a DAO.
 *
 * @author christian.bauer@jboss.com
 */
public abstract class AbstractDao {
    public static final String ERROR_DUPLICATE_RECORD = "duplicateRecord";
    public static final String ERROR_GENERIC = "genericError";
    public static final String ERROR_REQUIRED_FIELD = "requiredField";
    
    private Class modelClass;

    /***************************************************************************
     * Constructs a data access object that deals with instances of the passed
     * Class in the model.
     */
    protected AbstractDao(Class modelClass) {
        this.modelClass = modelClass;
    }

    /***************************************************************************
     * Although this is primarily intended to be used by methods that support
     * autoCommit, it may be called externally. This commits the open database
     * transaction and starts a new one. If a problem occurs when the transaction
     * is being committed, it is rolled back and a new one is started.
     * This is used to force any problems to occur when the action is done so
     * a meaningful error message can be displayed to the user. Otherwise, the
     * Filter will close the Transaction when the Request is finishing and any
     * problems encountered will cause the error page to be displayed.
     * @throws HibernateException if a problem is encountered.
     */
    public void commit() throws HibernateException {
        try {
            getSession().getTransaction().commit();
            getSession().beginTransaction();

        } catch (HibernateException ex) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                getSession().beginTransaction();
            }
            throw ex;
        }
    }

    /***************************************************************************
     * Deletes the passed object from the persistent store. Note that this does
     * not remove the model equivalent Object from memory, it simply makes it
     * transient (not have an equivalent in the persistent store).
     * @param entity the entity to be deleted from the persistent store.
     * @throws HibernateException if a problem is encountered.
     */
    public void delete(Object entity) throws HibernateException {
        getSession().delete(entity);
    }

    /***************************************************************************
     * Gets a collection of all the instances of the model's Class from
     * persistent store. Returns an empty List if none are found.
     * @param order a Collection of Orders that determine the sort of the 
     *        returned instances. If empty or null, they will not be sorted.
     * @return all instances of the model's Class that are in the persistent
     *         store.
     * @throws HibernateException if a problem is encountered.
     */
    public List findAll(Collection order) throws HibernateException {
        return findByCriteria(null, order);
    }

    /***************************************************************************
     * Gets a collection of instances of the model's Class from the persistent
     * store using query by example. Returns an empty List if none are found
     * that match.
     * @return instances of the model's Class that are in the persistent store
     *         that match the query criteria.
     * @throws HibernateException if a problem is encountered.
     */
    public List findByExample(Object exampleInstance, String[] excludeProperty) {
        Criteria crit = getSession().createCriteria(getModelClass());
        Example example =  Example.create(exampleInstance);
        
        if (excludeProperty != null && excludeProperty.length > 0) {
            for (int i=0; i< excludeProperty.length; i++) {
                example.excludeProperty(excludeProperty[i]);
            }
        }
        System.err.println("example: " + example);
        crit.add(example);
        return crit.list();
    }

    /***************************************************************************
     * Gets an appropriate error key by parsing through the passed exception to
     * try and determine the problem. The calling method would typically create
     * an ActionError with the returned String. These keys must be in the
     * ApplicationResources.properties file for this to work properly.
     * @param HibernateException the problem that was encountered.
     * @return one of the error constants.
     */
    public String getErrorKey(HibernateException e) {
        String errorMessages = "";
        
        for (int i=0; i<e.getMessages().length; i++) {
            errorMessages += e.getMessages()[i];
        }
        //System.err.println("Error Messages: " + errorMessages);
        if (errorMessages.indexOf("NULL") != -1) {
            return ERROR_REQUIRED_FIELD;
        } else if (errorMessages.indexOf("insert") != -1
                   && errorMessages.indexOf("unique constraint") != -1) {
            return ERROR_DUPLICATE_RECORD;
        }
        
        return ERROR_GENERIC;
    }

    /***************************************************************************
     * Ensures that the passed model object is in a state that allows it to be
     * worked with (initialized, deleted, or modified). This should be used for
     * any objects that were created during a previous request. Note that the
     * passed object should be replaced with the one returned from this method.
     * 
     * This is required since Hibernate cannot lazy initialize or otherwise work
     * with detached objects (those created by a Session other than the current
     * one). Since Sessions are maintained over the life of a request, objects
     * spanning requests become detached. Note that if this object is actually
     * detached, this causes a single select statement to be sent to the
     * database to reload the object (although any changes that had been made to
     * it should be preserved).
     * @param entity the model object to be attached to the current Session.
     * @return an equivalent object that is attached to the current Session.
     * @throws HibernateException if a problem is encountered.
     */
    public Object prepForUse(Object entity) throws HibernateException {
        if (getSession().contains(entity)) {
            return entity;
        }
        return getSession().merge(entity);
    }

    /***************************************************************************
     * Ensures that the passed model objects are in a state that allows them to
     * be worked with (initialized, deleted, or modified). This should be used
     * for any objects that were created during a previous request. Note that
     * this will modify the passed list "in place", replacing all detached
     * objects with equivalent attached ones.
     * 
     * This is required since Hibernate cannot lazy initialize or otherwise work
     * with detatched objects (those created by a Session other than the current
     * one). Since Sessions are maintained over the life of a request, objects
     * spanning requests become detached. Note that for each object that is
     * actually detached, this will cause a single select statement to be sent
     * to the database to reload the object (although any changes that had been
     * made to it should be preserved).
     * @param entities a List of model objects to be attached to the current
     *        Session.
     * @throws HibernateException if a problem is encountered.
     */
    public void prepForUse(List entities) throws HibernateException {
        for (int i=0; i < entities.size(); i++) {
            if (!getSession().contains(entities.get(i))) {
                entities.set(i, getSession().merge(entities.get(i)));
            }
        }
    }

    /***************************************************************************
     * Adds or updates the passed object in the persistent store. To add a new
     * object, simply create a new instance of the model Object (which makes a
     * transient one), update its values, then pass it to this method. It will
     * be inserted into the persistent store with a newly assigned unique ID.
     * Note that if you pre-populate the unique ID and another object already
     * exists with that ID, this will update the existing object rather than
     * sensing a unique constraint failure. In that case, use save() instead.
     * This can also be used to update detached objects (those retrieved by a
     * previous Session).
     * @param entity the entity to be updated or added to the persistent store.
     * @param autoCommit true if this should commit the transaction (it also
     *        subsequently starts a new one for future actions). Done so that
     *        problems are immediately apparent.
     * @return the entity to be updated or added to the persistent store.
     * @throws HibernateException if a problem is encountered.
     */
    public void save(Object entity, boolean autoCommit)
                throws HibernateException {
        getSession().save(entity);
        if (autoCommit) {
            commit();
        }
    }

    /***************************************************************************
     * Adds or updates the passed object in the persistent store. To add a new
     * object, simply create a new instance of the model Object (which makes a
     * transient one), update its values, then pass it to this method. It will
     * be inserted into the persistent store with a newly assigned unique ID.
     * Note that if you pre-populate the unique ID and another object already
     * exists with that ID, this will update the existing object rather than
     * sensing a unique constraint failure. In that case, use save() instead.
     * This can also be used to update detached objects (those retrieved by a
     * previous Session).
     * @param entity the entity to be updated or added to the persistent store.
     * @param autoCommit true if this should commit the transaction (it also
     *        subsequently starts a new one for future actions). Done so that
     *        problems are immediately apparent.
     * @return the entity to be updated or added to the persistent store.
     * @throws HibernateException if a problem is encountered.
     */
    public void saveOrUpdate(Object entity, boolean autoCommit)
                throws HibernateException {
        getSession().saveOrUpdate(entity);
        if (autoCommit) {
            commit();
        }
    }

    //+++++ PROTECTED ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    /***************************************************************************
     * A convenience method used by subclasses to return the instances of the
     * model's Class that match the passed critera.
     * @param criterion a Collection of Criterion that determine the instances
     *        to be returned. If empty or null, this will return all instances.
     * @param order a Collection of Orders that determine the sort of the 
     *        returned instances. If empty or null, they will not be sorted.
     * @return a collection of the model objects that match the criteria in
     *         the persistent store, if any. Returns an empty PagedList if none
     *         are found.
     * @throws HibernateException if a problem is encountered.
     */
    protected List findByCriteria(Collection criterion, Collection order)
                   throws HibernateException {
        Criteria crit = getSession().createCriteria(getModelClass());
        
        if (criterion != null) {
            Iterator it = criterion.iterator();
            while (it.hasNext()) {
                crit.add((Criterion)it.next());
            }
        }
        if (order != null) {
            Iterator it = order.iterator();
            while (it.hasNext()) {
                crit.addOrder((Order)it.next());
            }
        }
        return crit.list();
    }

    /***************************************************************************
     * A convenience method used to return a specific instance of the model's
     * Class. It can only return pure objects, so typically subclasses will
     * implement their own findById() calling this and casting the result to the
     * appropriate model Class.
     * @param id the unique key of the desired object in the persistent store.
     * @param lock true if the corresponding record should automatically be
     *        locked from updates in the persistent store.
     * @return the model object, if any, with the passed unique ID in the
     *         persistent store. Returns null if none is found.
     * @throws HibernateException if a problem is encountered.
     */
    protected Object findByIdBase(Serializable id, boolean lock)
                     throws HibernateException {
        Object persistentObj;
        
        if (lock) {
            persistentObj = getSession().load(getModelClass(), id, LockMode.UPGRADE);
        } else {
            persistentObj = getSession().load(getModelClass(), id);
        }
        try {
            //The next line "touches" the object so that it will lazy initialize
            //and possibly throw the not found exception:
            persistentObj.toString();
            return persistentObj;
        } catch (ObjectNotFoundException e) {
            return null;
        }
    }

    /***************************************************************************
     * Gets the model's Class that this deals with in the persistence layer.
     * @return the model's Class that this deals with in the persistence layer.
     */
    protected Class getModelClass() {
        return modelClass;
    }

    /***************************************************************************
     * Gets a Hibernate Session. Currently implemented to retrieve the current
     * one from HibernateUtil.
     * @return the current Hibernate Session.
     */
    protected Session getSession() {
        return HibernateUtil.getSessionFactory().getCurrentSession();
    }
}
