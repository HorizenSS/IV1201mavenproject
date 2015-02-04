package controller;

import model.Accounts;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.Banned;
import model.Cart;
import model.Stock;

/**
 * A controller. All calls to the model that are executed because of an action
 * taken by the cashier pass through here. EJB Used for data transaction
 *
 * @author Kentaro Hayashida
 * @author Johny Premanantham
 * @version 1.0
 * @since 2015-01-03
 */
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class CashierFacade {

    @PersistenceContext(unitName = "com.mycompany_IV1201mavenproject_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    private static boolean login;
    private static boolean logout = false;
    private static boolean adminlogin;
    private static Accounts currentAccount;

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
                    adminlogin = true;
                }
                return acc.getaccount(); //Start thread that starts webshop

            }

        }
        return null;
    }

    public String register(String account, String password, String email, String firstname, String lastname) {

        if (em.find(Accounts.class, account) != null) {
            return (" Account exists!!");

        }

        em.persist(new Accounts(account, password, email, firstname, lastname));
        return ("Account created, login to apply!");

    }

    public String addToCart(String item) {

        if (item.equals("Tall")) {

            if (login == true) {
                Cart cart = em.find(Cart.class, "Tall Gnome");
                int cartamount = cart.getamount();
                cart.setamount(cartamount + 1);

                return "Tall gnome added to shopping cart";
            }

        }
        if (item.equals("Little")) {

            if (login == true) {
                Cart cart = em.find(Cart.class, "Little Gnome");
                int cartamount = cart.getamount();
                cart.setamount(cartamount + 1);

                return "Little gnome added to shopping cart";
            }

        }
        if (item.equals("Large")) {

            if (login == true) {
                Cart cart = em.find(Cart.class, "Large Gnome");
                int cartamount = cart.getamount();
                cart.setamount(cartamount + 1);

                return "Large gnome added to shopping cart";
            }

        }

        return "Failed";
    }

    public String ban(String banned) {
        if (adminlogin) {
            em.persist(new Banned(banned));

            return banned + " is banned";
        }
        return "Failed";
    }

    public String buy() {
        // Acquire cart.
        Cart cartTall = em.find(Cart.class, "Tall Gnome");
        Cart cartLarge = em.find(Cart.class, "Large Gnome");
        Cart cartLittle = em.find(Cart.class, "Little Gnome");

        // Get current cart amount.
        int tallAmount = cartTall.getamount();
        int largeAmount = cartLarge.getamount();
        int littleAmount = cartLittle.getamount();
        int totalAmount = tallAmount + largeAmount + littleAmount;

        // If user is logged in and account balance is satisfactory.
        if (login == true) {

            // Remove amount from total balance.
           /* int current = currentAccount.getbalance();
             int bought = current - totalAmount*100;
             currentAccount.setbalance(bought);
             */
            // Acquire stock.
            Stock stockTall = em.find(Stock.class, "Tall Gnome");
            Stock stockLarge = em.find(Stock.class, "Large Gnome");
            Stock stockLittle = em.find(Stock.class, "Little Gnome");

            // Get current stock.
            int currentTall = stockTall.getamount();
            int currentLarge = stockLarge.getamount();
            int currentLittle = stockLittle.getamount();

            // Remove gnomes from stock.
            stockTall.setamount(currentTall - tallAmount);
            stockLarge.setamount(currentLarge - largeAmount);
            stockLittle.setamount(currentLittle - littleAmount);

            // Confirm purchase and print balance.
            clearCart();
            return "Gnome(s) bought. Current balance: ";
        }
        return "Failed";
    }

    public String logout() {
        logout = true;
        return "Logout";
    }

    //----------------------------------------------------------------------------------------
    public String add(String item) { //Only adds to 1 type of gnome

        if (item.equals("Tall")) {
            if (adminlogin == true) {
                Stock stock1 = em.find(Stock.class, "Tall Gnome");
                int current = stock1.getamount();
                stock1.setamount(current + 1);

                return "Tall Gnomes: : " + stock1.getamount();

            }

        }
        if (item.equals("Little")) {
            if (adminlogin == true) {
                Stock stock1 = em.find(Stock.class, "Little Gnome");
                int current = stock1.getamount();
                stock1.setamount(current + 1);

                return "Little Gnomes: : " + stock1.getamount();

            }

        }
        if (item.equals("Large")) {
            if (adminlogin == true) {
                Stock stock1 = em.find(Stock.class, "Large Gnome");
                int current = stock1.getamount();
                stock1.setamount(current + 1);

                return "Large Gnomes: : " + stock1.getamount();

            }

        }

        return "Must be logged in as admin";
    }

    public String checkStatus() {

        Stock stock1 = em.find(Stock.class, "Tall Gnome");
        Stock stock2 = em.find(Stock.class, "Large Gnome");
        Stock stock3 = em.find(Stock.class, "Little Gnome");

        return "Tall Gnomes: : " + stock1.getamount() + " || Large Gnomes: " + stock2.getamount() + " || Little Gnomes: " + stock3.getamount();

    }

    public String cart() {

        Cart cart1 = em.find(Cart.class, "Tall Gnome");
        Cart cart2 = em.find(Cart.class, "Large Gnome");
        Cart cart3 = em.find(Cart.class, "Little Gnome");

        return "Tall Gnomes: : " + cart1.getamount() + " || Large Gnomes: " + cart2.getamount() + " || Little Gnomes: " + cart3.getamount();

    }

    public void clearCart() {
        Cart cartTall = em.find(Cart.class, "Tall Gnome");
        Cart cartLarge = em.find(Cart.class, "Large Gnome");
        Cart cartLittle = em.find(Cart.class, "Little Gnome");

        cartTall.setamount(0);
        cartLarge.setamount(0);
        cartLittle.setamount(0);
    }

    public String fillDB() {

        em.persist(new Stock("Tall Gnome", 10));
        em.persist(new Stock("Large Gnome", 10));
        em.persist(new Stock("Little Gnome", 10));

        em.persist(new Cart("Tall Gnome", 0)); //ändra sen
        em.persist(new Cart("Large Gnome", 0)); //ändra sen
        em.persist(new Cart("Little Gnome", 0)); //ändra sen

        em.persist(new Accounts("admin", "admin", "admin@admin.se", "sven", "svensson"));

        return "";
    }
}
