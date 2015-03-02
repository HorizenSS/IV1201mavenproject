package view;

import controller.Facade;

//backing bean
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.*;
import model.Competence;

/**
 * AcctManager manages many requests concerning user accounts as well as
 * handling exceptions.
 *
 * @author Kentaro Hayashida
 * @author Johny Premanantham
 * @author Armin Arya
 * @version 1.0
 * @since 2015-01-03
 */
@Named("acctManager")
@ConversationScoped
public class AcctManager implements Serializable {

    private static final long serialVersionUID = 16247164405L;
    @EJB
    //globals
    private Facade Facade;
    private Exception transactionFailure;
    private boolean success = false;
    private boolean adminsuccess = false;
    //register and login
    private String account;
    private String password;
    private String email;
    private String firstname;
    private String lastname;
    private String timeperiod;
    private String name;
    private String dateofregistration;
    private String competence;
    private boolean toaccountSV = false;
    private boolean toaccount = false;
    private boolean toapplyconfirmation = false;
    private boolean error = false;
    private String result = null;
    private List<Competence> c;
    private Map<String,String> cc = new LinkedHashMap<String,String>();

   
    

 
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
    
    /** 
     *Login function, used by all users. Recruiter will be directed to recruiter.xhtml, applicant will be directed to applicant.xhtml.
     */
    public String login() {
        try {
            startConversation();
            transactionFailure = null;
            result = Facade.login(account, password);

            if ("admin".equals(result)) {
                adminsuccess = true;
                return jsf22Bugfix();

            }
            if (result != null) {
                success = true;
                stopConversation();
                return jsf22Bugfix();
            }
            
            if(result == null){
                result = "WRONG PASSWORD OR ACCOUNT NAME";
                return jsf22Bugfix();
            }

        } catch (Exception e) {
            handleException(e);
        }
        return jsf22Bugfix();
    }

    public String register() {
        try {
            long startTime = System.currentTimeMillis();  //Used to check the non-functional requirement
            startConversation();
            transactionFailure = null;
                     
            result = Facade.register(account, password, email, firstname, lastname,competence,startTime);
                   
            if(result == null){
            toapplyconfirmation = true;
            }
            
        } catch (Exception e) {
            handleException(e);
        }
        return jsf22Bugfix();
    }
    public String stoptime() {
        try {
          
            Facade.stoptime();
            
        } catch (Exception e) {
            handleException(e);
        }
        return jsf22Bugfix();
    }
    
    public String init() {
        try {
          
             c = Facade.init();
             
        } catch (Exception e) {
            handleException(e);
        }
        return jsf22Bugfix();
    }
    
    
    public String fillDB() {
        try {

            Facade.fillDB();

        } catch (Exception e) {
            handleException(e);

        }
        return jsf22Bugfix();
    }
    
        public String toaccountSV() {
        try {
          //  toaccountSV = true;
        } catch (Exception e) {
            handleException(e);

        }
        return jsf22Bugfix();
    }
        
         public String toaccount() {
        try {
            toaccount = true;
        } catch (Exception e) {
            handleException(e);

        }
        return jsf22Bugfix();
    }


   //SETTERS AND GETTERS        
    public void setaccount(String account) {
        this.account = account;
    }
    @NotNull(message = "Account name field cant be empty")
    public String getaccount() { //Must have
        return null;
    }

    public void setpassword(String password) {
        this.password = password;
    }
    
    @NotNull(message = "Password field cant be empty")
    @Size(min = 1, max = 10, message = "Password length must be between 2-10 characters long")
    public String getpassword() { //Must have
        return null;
    }

    public void setemail(String email) {
        this.email = email;
    }
    
    @NotNull(message = "Email field cant be empty")
    public String getemail() { //Must have
        return null;
    }

    public void setfirstname(String firstname) {
        this.firstname = firstname;
    }
    @NotNull(message = "First name field cant be empty")
    public String getfirstname() { //Must have
        return null;
    }
    
    public void setlastname(String lastname) {
        this.lastname = lastname;
    }
    @NotNull(message = "Last name field cant be empty")
    public String getlastname() { //Must have
        return null;
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
    
    public boolean geterror(){
        return error;
    }
    
    public boolean gettoaccount() {
        return toaccount;
    }
    
    public boolean getadminsuccess() {
        return adminsuccess;
    }

    public boolean gettoaccountSV(){
        return toaccountSV;
    }
    
     public boolean gettoapplyconfirmation(){
        return toapplyconfirmation;
    }
    
    public void setCompetence(String competence){
         this.competence = competence;
    }
    
    public String getCompetence(){
          return competence;
    }

    public List<Competence> getC() {
        return c;
    }
    
}
