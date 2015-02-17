package view;

import controller.Facade;
import java.io.PrintWriter;

//backing bean
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import model.accountInterface;

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

    public String login() {
        try {
            //startConversation();
            //transactionFailure = null;
            result = Facade.login(account, password);
            //online = result;

            if ("admin".equals(result)) {
                adminsuccess = true;
                return jsf22Bugfix();

            }
            if (result != null) {
                success = true;
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
            startConversation();
            transactionFailure = null;
                
            //Validation
              while (account.equals("")) {
                  result = "Enter a username!";
                  return jsf22Bugfix();
         }
               while (2 >= password.length()) {
                  result = "Password must be atlest 2 characters!";
                  return jsf22Bugfix();
         }             
              
            result = Facade.register(account, password, email, firstname, lastname,competence);
            
            if(result == null){
            toapplyconfirmation = true;
            }
            
        } catch (Exception e) {
            handleException(e);
        }
        return jsf22Bugfix();
    }


    public String add() {
        try {
            result = Facade.add(name,timeperiod,dateofregistration,competence);

        } catch (Exception e) {
            handleException(e);

        }
        return jsf22Bugfix();
    }

    public String logout() {
        try {

            result = Facade.logout();
            online = result;
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
            toaccountSV = true;
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

    public String getaccount() { //Must have
        return null;
    }

    public void setpassword(String password) {
        this.password = password;
    }

    public String getpassword() { //Must have
        return null;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public String getemail() { //Must have
        return null;
    }

    public void setfirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getfirstname() { //Must have
        return null;
    }

    public void setlastname(String lastname) {
        this.lastname = lastname;
    }

    public String getlastname() { //Must have
        return null;
    }

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
    
    public void setcompetence(String competence){
    this.competence = competence;
    }
    
    public String getcompetence(){
    return competence;
    }
    public boolean geterror(){
    return error;
    }
}
