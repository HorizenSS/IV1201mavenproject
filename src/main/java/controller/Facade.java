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
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import model.Accounts;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.Applies;
import java.util.Date;
import javax.faces.context.FacesContext;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import model.Competence;
import model.Person;

/**
 * A controller. All calls to the model that are executed because of an action
 * taken by the facade pass through here. EJB Used for data transaction
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
    private long start;
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

    /**
     * Constructor will create text files when a object of this class is
     * created, these text files are used for logging.
     *
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    public Facade() throws FileNotFoundException, DocumentException {
        //LOGGIN
        this.pw = new PrintWriter("registeredlog.txt");
        this.pwlogin = new PrintWriter("loginlog.txt");
        this.pwregistertime = new PrintWriter("registertime.txt");

    }

    /**
     * This method is used for the login purpose, if an admin logins the admin
     * will get directed to an admin page. If a applicant logins they will be
     * directed to the applicant page which displays the current applies. When
     * the admin is online he/she will be able to approve applies and generate a
     * pdf containing the applicants
     *
     * @param account the account name of the user who wants to login
     * @param password the password of the user who wants to login
     * @return the user name or null if something went wrong
     */
    public String login(String account, String password) {
        Accounts acc = em.find(Accounts.class, account);
        Person person = em.find(Person.class, account);
        String name = person.getName();
        if (acc != null) {
            if (account.equals(name) && password.equals(acc.getpassword())) {
                login = true;
                logout = false;
                adminlogin = false;
                currentAccount = acc;
                if ("admin".equals(name)) {
                    adminlogin = true;
                }
                pwlogin.println(name + " has logged in.");
                pwlogin.flush();
                return name;

            }

        }
        throw new EntityNotFoundException("Login error");
    }

    /**
     * This method is used to register applies, the user can enter personal
     * information that will later be handled by a recruiter.
     *
     * @param account
     * @param password
     * @param email
     * @param firstname
     * @param lastname
     * @param competence
     * @param startTime
     * @return
     * @throws IOException
     */
    public String register(String account, String password, String email, String firstname, String lastname, String competence, long startTime) throws IOException, Exception {
           
             this.start = startTime;
           
            Accounts acc = em.find(Accounts.class, account);
            if(acc == null){
            Competence comp = em.find(Competence.class, competence);
            
            pw.println(account + " is registered.");
            pw.flush();
            em.persist(new Accounts(new Person(account), password, email,firstname, lastname, comp,false));
            em.persist(new Applies(account, lastname, firstname, email, competence));

            return null;            
            }else{
            throw new Exception("Register error, account exists. Kontot existerar."); 
            }
  }


    /**
     * This method generates a PDF document containing the applicants. We are
     * using the itext api to create pdf documents Must call listApplicants
     * because listApplicants is the method that initiates the variable
     * applicantList - which will contain the applicants.
     *
     * @throws java.io.FileNotFoundException
     * @throws com.itextpdf.text.DocumentException
     */
    public void pdf() throws FileNotFoundException, DocumentException, IOException {
        listApplicants();
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();

        Document a = new Document();
        try {
            response.setContentType("application/pdf");
            PdfWriter.getInstance(a, response.getOutputStream());
            a.open();
            a.addTitle("Applicant PDF");
            a.add(new Chunk(""));
            a.add(new Paragraph(applicantList));
            a.add(new Paragraph(new Date().toString()));
        } catch (IOException | DocumentException e) {
            System.err.println(e);
        }
        fc.responseComplete();
        a.close();

    }

    /**
     * Save logged information if server shutdowns
     *
     * @return
     * @throws java.lang.Exception
     */
    public String savetxt() throws Exception {

        try {
            
            pw.close();
            pwlogin.close();
            pwregistertime.close();

            return "Files saved";
            
        } catch (Exception e) {
            throw new Exception("Problem with saving text files");
        }

    }

    /**
     * A method that will reset the login and logout variables
     *
     * @return
     */
    public String logout() {
        logout = true;
        login = false;
        adminlogin = false;
        return "";
    }

    /**
     * This method fetches data from the Applies table and stores all data in a
     * string so that we can show who has applied.
     *
     * @return
     * @throws DocumentException
     */
    public String listApplicants() throws DocumentException {

        List<Applies> applies = em.createQuery("from Applies m", Applies.class).getResultList();

        applicantList = "";
        int c = 1;
        for (Applies app : applies) {
            applicantList = applicantList + "Apply number " + c + " = " + "Account name: " + app.getname() + ", Firstname: " + app.getfirstname() + ", Last name: " + app.getlastname() + ", Email: "
                    + app.getemail() + ", Kompetens: " + app.getcompetence() + "    || \n ";
            applied[c] = app.getname();
            c++;
        }

        return applicantList;

    }

    /**
     * Method for approving applies, for the moment this method only stores the
     * applicants name in a arraylist
     *
     * @param applicant
     * @return
     * @throws java.lang.Exception
     */
    public String approve(String applicant) throws Exception {

        Accounts acc = em.find(Accounts.class, applicant);
        if(acc != null){
        acc.setApproved(true);
        return acc.getfirstname()+" ";
        }
        throw new Exception("Approved failed");
     }
    
    public List<Person> approvearray(){
        List<Person> person = em.createQuery("from Person m", Person.class).getResultList();

        return person;
    }

    public String stoptime() {

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - start;

        String totalTimeString = String.valueOf(totalTime);
        pwregistertime.println("Time to register account" + " = " + totalTimeString + "ms");
        pwregistertime.flush();
        return "";
    }

    /**
     * First method that gets called when the application initiates.
     * The method persist some data in the tables.
     *
     * @return
     */
    public String fillDB() {
        if (em.find(Accounts.class, "admin") == null) {
            Competence c = new Competence("java");
            em.persist(new Accounts(new Person("admin"), "admin", "admin@admin.se","admin", "svensson", c,true));
            em.persist(new Competence("c++"));
            em.persist(new Competence("erlang"));
            em.persist(new Competence("python"));
            
        }
        return "";
    }
/**
 * Second method that gets called. 
 * Will retrieve a all competence that exist in the table and store them in a List.
 * @return 
 */
    public List<Competence> init() {

        List<Competence> competence = em.createQuery("from Competence m", Competence.class).getResultList();

        return competence;
    }

    /**
     * Method for checking authorization on the applicant page
     *
     * @return
     */
    public String checkAuthorization() {

        if (login == false) {
            return "NOT-AUTHORIZED";
        }
        return "AUTHORIZED";
    }

    /**
     * Method for checking authorization on the recruiter page
     *
     * @return
     */
    public static String checkAuthorizationAdmin() {

        if (adminlogin == false) {
            return "NOT-AUTHORIZED";
        }
        return "AUTHORIZED";
    }

    /**
     * test
     *
     * @param a
     * @return
     */
    public static String test(String a) {
        return "b";

    }
}
