package view;

import com.itextpdf.text.DocumentException;
import controller.Facade;
import java.io.FileNotFoundException;
import java.io.IOException;

//backing bean
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import model.accountInterface;

/**
 * RecruitManager manages many requests concerning user accounts as well as
 * handling exceptions.
 *
 * @author Kentaro Hayashida
 * @author Johny Premanantham
 * @author Armin Arya
 * @version 1.0
 * @since 2015-01-03
 */
@Named("recruitManager")
@ConversationScoped
public class RecruitManager implements Serializable {

    private static final long serialVersionUID = 16247164405L;
    @EJB

    //globals
    private Facade Facade;
    private Exception transactionFailure;
    private boolean success = false;
    private boolean tohomepage = false;
    private boolean error = false;
    private boolean adminlogin = false;
    
    //register and login

    private String timeperiod;
    private String name;
    private String dateofregistration;
    private String competence;

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
   //     Facade.savetxt();  //If the server shutdowns for whatever reason save textfile
        if (!conversation.isTransient()) {
            conversation.end();
        }
    }

    private void handleException(Exception e) {
        stopConversation();
        error = true;
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

        public String listApplicants() {
        try {
            startConversation();
            transactionFailure = null;  
            success = true;
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

    public String tohomepage() {
        try {

            tohomepage = true;
        } catch (Exception e) {
            handleException(e);

        }
        return jsf22Bugfix();
    }
    
     public String pdf() throws IOException {
        try {

            Facade.pdf();
        } catch (FileNotFoundException | DocumentException e) {
            handleException(e);

        }
        return jsf22Bugfix();
    }   
   
      public String checkAuthorizationAdmin() {
        try {
            startConversation();
            transactionFailure = null;
            result = Facade.checkAuthorizationAdmin();

            if ("NOT-AUTHORIZED".equals(result)) {
                error = true;
                tohomepage = true;
            }else{adminlogin = true;}

        } catch (Exception e) {
            handleException(e);
        }
        return jsf22Bugfix();
    }
       
    public String savetxt() {
        try {
            startConversation();
            transactionFailure = null;
            result = Facade.savetxt();


        } catch (Exception e) {
            handleException(e);
        }
        return jsf22Bugfix();
    }     
    public String reset() {
        try {
            startConversation();
            transactionFailure = null;
            adminlogin = false;


        } catch (Exception e) {
            handleException(e);
        }
        return jsf22Bugfix();
    }  
   //SETTERS AND GETTERS        
    public void setstatus(String status) {
        this.status = status;
    }

    public String getstatus() {
        return status;
    }

    public void setonline(String online) {
        this.online = online;
    }

    public String getonline() {
        return online;
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

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public void settimeperiod(String timeperiod) {
        this.timeperiod = timeperiod;
    }

    public String gettimeperiod() {
        return timeperiod;
    }

    public void setdateofregistration(String dateofregistration) {
        this.dateofregistration = dateofregistration;
    }

    public String getdateofregistration() {
        return dateofregistration;
    }

    public void setcompetence(String competence) {
        this.competence = competence;
    }

    public String getcompetence() {
        return competence;
    }

    public boolean gettohomepage() {
        return tohomepage;
    }
    public boolean geterror(){
    return error;
    }
   
    public boolean getadminlogin(){
        return adminlogin;
    }    
}
