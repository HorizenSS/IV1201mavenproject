/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import controller.Facade;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 *
 * @author johny_000
 */
@Path("applications")
public class ApplicationResource {

    @GET
    public String applications(){
    String a = Facade.applicantList;
    return a;
}
    @GET
    @Path("{accountname}-{password}-{email}-{firstname}-{lastname}-{competence}")
    public String developer(@PathParam("accountname")String accountname, @PathParam("password") String password, @PathParam("email") String email
    , @PathParam("firstname") String firstname, @PathParam("lastname") String lastname, @PathParam("competence") String competence){    
        
        return accountname + password;
}
}