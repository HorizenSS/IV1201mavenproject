package model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 
 * @author      Kentaro
 * @author      Johny Premanantham
 * @version     1.0
 * @since       2015-01-03
 */
@Entity
public class Jobs implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String name;
    private String timeperiod;
    private String dateofregistration;
    private String competence;
    
    public Jobs(){
        
    }
    
    public Jobs(String name, String timeperiod, String dateofregistration, String competence){
        this.name = name;
        this.timeperiod = timeperiod;
        this.dateofregistration = dateofregistration;
        this.competence = competence;
        this.id = name;
    }
 
    
    public String getname(){
        return name;
    }
    
    
    public String gettimeperiod(){
    return timeperiod;
    }
    
    public String settimeperiod(String timeperiod){
    this.timeperiod = timeperiod;
    return timeperiod;
    }

    public String getdateofregistration(){
        return dateofregistration;
    }
    
    public String getdateofregistration(String dateofregistration){
        this.dateofregistration = dateofregistration;
        return dateofregistration;
    
    }
    
    public String getcompetence(){
    return competence;
    }
    
    public String setcompetence(String competence){
    this.competence = competence;
    return competence;
    }
    
    
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Jobs)) {
            return false;
        }
        Jobs other = (Jobs) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Jobs[ id=" + id + " ]";
    }
    
}
