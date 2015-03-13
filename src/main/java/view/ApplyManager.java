package view;

import com.itextpdf.text.DocumentException;
import controller.Facade;
import java.io.FileNotFoundException;
import java.io.IOException;

//backing bean
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import model.Person;

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
    private boolean approvedsuccess;
    private String item;
    private int applicantnr;
    private String additem;
    private String test;
    private static String status = null;
    private String result = null;
    private String resulta = null;
    private String resultb = null;
    private String resultc = null;
    private List<Person> c;
    
    
    @Inject
    private Conversation conversation;

    private void startConversation() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
    }

    private void stopConversation() throws Exception {
         Facade.savetxt();  //If the server shutdowns for whatever reason
        if (!conversation.isTransient()) {
            conversation.end();
        }
    }

    private void handleException(Exception e) throws Exception {
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
     * @return 
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

    public String tohomepage() throws Exception {
        try {

            tohomepage = true;
        } catch (Exception e) {
            handleException(e);

        }
        return jsf22Bugfix();
    }

    public String checkAuthorization() throws Exception {
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

    public String listApplicants() throws Exception {
        try {            
            
            resultb = Facade.listApplicants();
            if(error == true){resultb = null;}
        } catch (Exception e) {
            handleException(e);
        }
        return jsf22Bugfix();
    }

            
  public String approve() throws Exception {
        try {
            resulta = Facade.approve(test);
           if(resulta != null){
           approvedsuccess = true;
           }
        } catch (Exception e) {
            handleException(e);

        }
        return jsf22Bugfix();
    }
  
    public String approvearray() throws Exception {
        try {
            
            c = Facade.approvearray();
           
        } catch (Exception e) {
            handleException(e);

        }
        return jsf22Bugfix();
    }
  
      public String logout() throws Exception {
        try {

            result = Facade.logout();
            tohomepage = true;
        } catch (Exception e) {
            handleException(e);

        }
        return jsf22Bugfix();
    }
      public String PDF() throws IOException, Exception {
        try {

             Facade.pdf();
        } catch (FileNotFoundException | DocumentException e) {
            handleException(e);

        }
        return jsf22Bugfix();
    }      
      
      public Person[] getCom() throws Exception {
         Person[] a = null;

        try {
        c = Facade.approvearray();    
        a = new Person[c.size()];//create array size of how many competence that exists in the List c.
           int i = 0;
           for(Person com : c){//put all competence in the array
               
               a[i] = com;
               i++;
               
             }
        } catch (Exception e) {
            handleException(e);

        }
        return a;
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
        boolean b = adminsuccess;
        adminsuccess = false;
        return b;
    }

    public boolean gettohomepage() {
        boolean b = tohomepage;
        tohomepage = false;
        return b;
    }

    public boolean geterror() {
        boolean b = error;
        error = false;
        return b;
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

    public boolean isApprovedsuccess() {

        return approvedsuccess;
    }

    public String getResultc() {
        return resultc;
    }

    public void setResultc(String resultc) {
        this.resultc = resultc;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }



}
