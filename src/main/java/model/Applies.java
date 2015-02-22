/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Applies implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String accountname;
    private String firstname;
    private String email;
    private String competence;
    private String lastname;
    
    public Applies(){
        
    }
    
    public Applies(String accountname, String lastname, String firstname, String email, String competence){
        this.accountname = accountname;
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        this.competence = competence;
        this.id = accountname;
    }
 
    
    public String getname(){
        return accountname;
    }
        public String getlastname(){
        return lastname;
    }
    
    public String getfirstname(){
    return firstname;
    }
    public String getemail(){
        return email;
    }
    public String getcompetence(){
        return competence;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Applies)) {
            return false;
        }
        Applies other = (Applies) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Applies[ id=" + id + " ]";
    }
    
}
