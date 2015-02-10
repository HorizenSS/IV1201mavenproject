package model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Defines a user account.
 * 
 * @author      Kentaro Hayashida
 * @author      Johny Premanantham
 * @author      Armin Arya
 * @version     1.0
 * @since       2015-01-03
 */
@Entity
public class Accounts implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String account;
    private String password;
    private String email;
    private String firstname;
    private String lastname;


    
    public Accounts(){
        
    }
    
    public Accounts(String account, String password, String email, String firstname, String lastname){
        this.account = account;
        this.password = password;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.id = account;
    }
 
    
    public String getaccount(){
        return account;
    }
    
    public String getpassword(){
        return password;
    }
     public String getemail(){
        return email;
    }
 public String getfirstname(){
        return firstname;
    }
    public String getlastname(){
        return lastname;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Accounts)) {
            return false;
        }
        Accounts other = (Accounts) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bank.model.Accounts[ id=" + id + " ]";
    }
    
}
