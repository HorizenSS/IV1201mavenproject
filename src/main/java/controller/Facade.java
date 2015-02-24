package controller;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import model.Accounts;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.Applies;
import model.Jobs;
import java.io.FileOutputStream;

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
    public PrintWriter pwlogin;
    public PrintWriter pwregistertime;
    /**
     * Variable applied will contain the name of the applied users
     */
    private String[] applied = new String[20];
    private ArrayList<String> approved = new ArrayList<String>();
    private Document document;
    private PdfWriter pdfWriter;
    private Paragraph paragraph;
    private PageSize pagesize;
    private Rectangle rectangle;

    public static String applicantList = "";

    public Facade() throws FileNotFoundException, DocumentException {
        //LOGGIN
        this.pw = new PrintWriter("registeredlog.txt");
        this.pwlogin = new PrintWriter("loginlog.txt");
        this.pwregistertime = new PrintWriter("registertime.txt");

    }

    /**
     * This method is used for the login purpose, if an admin logins the admin
     * will get directed to an admin page. If a applicant logins they will be
     * directed to the applicant page which displays the current applies
     *
     * @param account the account name of the user who wants to login
     * @param password the password of the user who wants to login
     * @return the user name or null if something went wrong
     */
    public String login(String account, String password) {
        Accounts acc = em.find(Accounts.class, account);

        if (acc != null) {
            if (account.equals(acc.getaccount()) && password.equals(acc.getpassword())) {
                login = true;
                logout = false;
                adminlogin = false;
                currentAccount = acc;
                if ("admin".equals(acc.getaccount())) {
                    adminlogin = true;
                }
                pwlogin.println(acc.getaccount() + " has logged in.");
                pwlogin.flush();
                return acc.getaccount();

            }

        }
        return null;
    }

    public String register(String account, String password, String email, String firstname, String lastname, String competence, long startTime) throws IOException {

        Accounts acc = em.find(Accounts.class, account);

        if (acc != null) {
            return ("Account exists!!");
        }

        pw.println(account + " is registered.");
        pw.flush();
        em.persist(new Accounts(account, password, email, firstname, lastname, competence));
        em.persist(new Applies(firstname, lastname, email, password, competence));

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        String totalTimeString = String.valueOf(totalTime);
        pwregistertime.println("Time to register account: " + account + " = " + totalTimeString + "ms");
        pwregistertime.flush();

        return null;

    }

    /**
     * Must call listApplicants because listApplicants is the method that
     * initiates the variable applicantList - which will contain the applicants.
     *
     *
     */
    public void pdf() throws FileNotFoundException, DocumentException {
        listApplicants();

        this.document = new Document();
        this.rectangle = new Rectangle(pagesize.LETTER);
        document.setPageSize(rectangle);
        this.pdfWriter
                = PdfWriter.getInstance(document, new FileOutputStream("applicants.pdf"));
        this.paragraph = new Paragraph();

        document.open();
        document.add(new Chunk(""));
        paragraph.add(applicantList);
        document.add(paragraph);
        document.close();
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
            em.persist(new Jobs(item, timeperiod, dateofregistration, competence));
            return "Job is added";

        }
        return "Must be logged in as admin";
    }

    public String listApplicants() throws DocumentException {

        List<Applies> applies = em.createQuery("from Applies m", Applies.class).getResultList();

        applicantList = "";
        int c = 1;
        for (Applies app : applies) {
            applicantList = applicantList + "Apply number " + c + " = " + "Account name: " + app.getname() + ", Firstname: " + app.getfirstname()+ ", Last name: " + app.getlastname() + ", Email: "
                    + app.getemail()+ ", Kompetens: " + app.getcompetence() + "    || \n ";
            applied[c] = app.getname();
            c++;
        }

        return applicantList;

    }

    public String approve(int applicantnr) {

        if (adminlogin == false) {
            return "ONLY ADMINS CAN APPROVE!";
        }

        String approvedApplicant = applied[applicantnr];
        Applies apply = em.find(Applies.class, approvedApplicant);
      
        if (approvedApplicant != null) {
            approved.add(approvedApplicant);                                                         //AT THE MOMENT THE FUNCTION RETURNS A APROPRIATE MESSAGE AND ADDS THE APPROVED ACCOUNT NAME TO AN ARRAYLIST
            em.remove(apply);
            return "Applicant number: " + applicantnr + ", Name: " + approvedApplicant + ", is approved!";
        }
        return "Applicant number does not exist!";
    }

    public String fillDB() {

        em.persist(new Jobs("test job", "test", "test", "test"));
        em.persist(new Accounts("admin", "admin", "admin@admin.se", "sven", "svensson", "bla"));

        return "";
    }

    //AUTHORIZATION
    public String checkAuthorization() {

        if (login == false) {
            return "NOT-AUTHORIZED";
        }
        return "AUTHORIZED";
    }

    public static String checkAuthorizationAdmin() {

        if (adminlogin == false) {
            return "NOT-AUTHORIZED";
        }
        return "AUTHORIZED";
    }

    public static String test(String a) {
        return "b";

    }
}
