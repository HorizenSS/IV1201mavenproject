package view;

import controller.CashierFacade;

//backing bean
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import model.accountInterface;

/**
 * ApplyManager manages many requests concerning user accounts as well as
 * handling exceptions.
 *
 * @author Kentaro Hayashida
 * @author Johny Premanantham
 * @version 1.0
 * @since 2015-01-03
 */
@Named("applyManager")
@ConversationScoped
public class ApplyManager implements Serializable {

    private static final long serialVersionUID = 16247164405L;
    @EJB

    //globals
    private CashierFacade cashierFacade;
    private Exception transactionFailure;
    private boolean success = false;
    private boolean adminsuccess = false;
    private boolean tohomepage = false;

    private String item;
    private String additem;
    private String resultcart;

    private static String online = null;
    private static String status = null;
    private String result = null;
    @Inject
    private Conversation conversation;

    private void startConversation() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
    }

    private void stopConversation() {
        if (!conversation.isTransient()) {
            conversation.end();
        }
    }

    private void handleException(Exception e) {
        stopConversation();
        e.printStackTrace(System.err);
        transactionFailure = e;
    }

    /**
     * @return <code>true</code> if the latest transaction succeeded, otherwise
     * <code>false</code>.
     */
    public boolean getTransactionFailure() {
        return transactionFailure == null;
    }

    /**
     * Returns the latest thrown exception.
     */
    public Exception getException() {
        return transactionFailure;
    }

    /**
     * This return value is needed because of a JSF 2.2 bug. Note 3 on page 7-10
     * of the JSF 2.2 specification states that action handling methods may be
     * void. In JSF 2.2, however, a void action handling method plus an
     * if-element that evaluates to true in the faces-config navigation case
     * causes an exception.
     *
     * @return an empty string.
     */
    private String jsf22Bugfix() {
        return "";
    }

    public String apply() {
        try {

            result = cashierFacade.apply();

        } catch (Exception e) {
            handleException(e);

        }
        return jsf22Bugfix();
    }

    public String addToApplies() {
        try {

            result = cashierFacade.addToApplies(item);
        } catch (Exception e) {
            handleException(e);

        }
        return jsf22Bugfix();
    }

    public String tohomepage() {
        try {

            tohomepage = true;
        } catch (Exception e) {
            handleException(e);

        }
        return jsf22Bugfix();
    }

    public void setstatus(String status) {
        this.status = status;
    }

    public String getstatus() {
        return status;
    }

    public String getResult() {
        return result;
    }

    public void nullResult() {
        result = null;
    }

    public boolean getsuccess() {
        return success;
    }

    public boolean getadminsuccess() {
        return adminsuccess;
    }

    public boolean gettohomepage() {
        return tohomepage;
    }

    public String getitem() {
        return item;
    }

    public void setitem(String item) {
        this.item = item;
    }

    public String getadditem() {
        return additem;
    }

    public void setadditem(String additem) {
        this.additem = additem;
    }

}
