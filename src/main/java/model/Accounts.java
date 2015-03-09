package model;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

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
   
    @OneToOne(cascade={CascadeType.ALL})
    @JoinColumn(name = "COMPETENCE_ID")
    private Competence competence;
    
    @OneToOne(cascade={CascadeType.ALL})
    @JoinColumn(name = "PERSON_ID")    
    private Person account;
    
    private String firstname;
    private String password;
    private String email;
    private String lastname;
    private boolean approved;

    
    public Accounts(){
        
    }
    
    public Accounts(Person account, String password, String email, String firstname, String lastname, Competence competence, boolean approved){
        this.account = account;
        this.password = password;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.competence = competence;
        this.approved = approved;
        this.id = firstname;
    }
 
    
    public Person getAccount(){
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
    public void setAccount(Person person_id){
    this.account = person_id;
    }     
    public String getlastname(){
        return lastname;
    }
    
    
    public void setCompetence(Competence competence_id){
    this.competence = competence_id;
    }
    
    public Competence getCompetence(){
    return competence;
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

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
    
}
