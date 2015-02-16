package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import model.Accounts;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.Banned;
import model.Applies;
import model.Jobs;

/**
 * A controller. All calls to the model that are executed because of an action
 * taken by the cashier pass through here. EJB Used for data transaction
 *
 * @author Kentaro Hayashida
 * @author Johny Premanantham
 * @author Armin Arya
 * @version 1.0
 * @since 2015-01-03
 */
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class Facade {

    @PersistenceContext(unitName = "com.mycompany_IV1201mavenproject_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    private static boolean login;
    private static boolean logout = false;
    private static boolean adminlogin;
    private static Accounts currentAccount;
    public PrintWriter pw;

    public Facade() throws FileNotFoundException {
        this.pw = new PrintWriter("registered.txt");
    }

    // List<Gnomes> gnomes = em.createQuery("from Gnomes m", Gnomes.class).getResultList();
    
    public String login(String account, String password) {
        Accounts acc = em.find(Accounts.class, account); //gets an entry
        Banned ban = em.find(Banned.class, account);

        if (ban != null) {
            if (account.equals(ban.gettype())) {
                return "Banned";
            }

        }

        if (acc != null) {
            if (account.equals(acc.getaccount()) && password.equals(acc.getpassword())) { //check if entry contains account and password
                login = true;
                logout = false;
                adminlogin = false;
                currentAccount = acc;
                if ("admin".equals(acc.getaccount())) {
                    pw.close();                                     //CLOSE PRINTWRITER
                    adminlogin = true;
                }
                return acc.getaccount(); //Start thread that starts webshop

            }

        }
        return null;
    }

    public String register(String account, String password, String email, String firstname, String lastname) throws IOException {
        Accounts acc = em.find(Accounts.class, account);
        
        if (acc != null) {
            return (" Account exists!!");
        }      
        
        pw.println(account + " is registered.");
        em.persist(new Accounts(account, password, email, firstname, lastname));    
        return ("Account; "+ account + " created!");

    }

    public String addToApplies(String item) {

       
        return "Unimplemented";
    }


    public String apply() {
    
        return "Unimplemented";
    }

    public String logout() {
        logout = true;
        login = false;
        adminlogin = false;
        return "Logout";
    }

    //----------------------------------------------------------------------------------------
    public String add(String item, String timeperiod, String dateofregistration, String competence) { //Only adds to 1 type of gnome
       
            if (adminlogin == true) {
                em.persist(new Jobs(item,timeperiod,dateofregistration,competence));
                return "Job is added";

            }
        return "Must be logged in as admin";
    }

    public String checkStatus() {

      

        return "Unimplmeneted";

    }

    public String applyList() {

      return "Unimplemented";

    }

    public void clearApplies() {
     //not implemented
    }

    public String fillDB() throws FileNotFoundException, IOException {

        em.persist(new Jobs("test job", "test","test","test"));
        em.persist(new Applies("", 0)); 
        em.persist(new Accounts("admin", "admin", "admin@admin.se", "sven", "svensson"));
        

        
        return "";
    }
    
    public String checkAuthorization(){
        
        if(login == false){
        return "NOT-AUTHORIZED";
        }
        return "AUTHORIZED";
    }
    
        public String checkAuthorizationAdmin(){
    
        if(adminlogin == false){
        return "NOT-AUTHORIZED";
        }
        return "AUTHORIZED";
    } 
}
