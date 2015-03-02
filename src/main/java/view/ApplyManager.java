package view;

import controller.Facade;

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
 * @author Armin Arya
 * @version 1.0
 * @since 2015-02-03
 */
@Named("applyManager")
@ConversationScoped
public class ApplyManager implements Serializable {

    private static final long serialVersionUID = 16247164405L;
    @EJB

    //globals
    private Facade Facade;
    private Exception transactionFailure;
    private boolean success = false;
    private boolean adminsuccess = false;
    private boolean tohomepage = false;
    private boolean error = false;
    
    private String item;
    private int applicantnr;
    private String additem;

    private static String online = null;
    private static String status = null;
    private String result = null;
    private String resulta = null;
    private String resultb = null;
    @Inject
    private Conversation conversation;

    private void startConversation() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
    }

    private void stopConversation() {
         Facade.savetxt();  //If the server shutdowns for whatever reason
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

    public String tohomepage() {
        try {

            tohomepage = true;
        } catch (Exception e) {
            handleException(e);

        }
        return jsf22Bugfix();
    }

    public String checkAuthorization() {
        try {

            startConversation();
            transactionFailure = null;
            result = Facade.checkAuthorization();
            
            if ("NOT-AUTHORIZED".equals(result)) {
                error = true;
            }

        } catch (Exception e) {
            handleException(e);

        }
        return jsf22Bugfix();
    }

    public String listApplicants() {
        try {            
            
            resultb = Facade.listApplicants();
            
        } catch (Exception e) {
            handleException(e);
        }
        return jsf22Bugfix();
    }

            
  public String approve() {
        try {
            resulta = Facade.approve(applicantnr);
           
        } catch (Exception e) {
            handleException(e);

        }
        return jsf22Bugfix();
    }
      public String logout() {
        try {

            result = Facade.logout();
            tohomepage = true;
        } catch (Exception e) {
            handleException(e);

        }
        return jsf22Bugfix();
    }
    public void setstatus(String status) {
        this.status = status;
    }
   
    public void setapplicantnr(int applicantnr){
        this.applicantnr = applicantnr;
    }

    public int getapplicantnr(){
    return 0;
    }
    
    public String getstatus() {
        return status;
    }

    public String getresulta(){
        return resulta;
    }
    
    public String getresult() {
        return result;
    }
    
    public String getresultb(){
        return resultb;
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

    public boolean geterror() {
        return error;
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
