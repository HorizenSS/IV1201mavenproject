/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import controller.Facade;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author johny_000
 */
@Path("applications")
public class ApplicationResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String applications(){
    String a = Facade.applicantList;
    return "<p>"+a+"</p>";
}
    @GET
    @Path("{accountname}-{password}-{email}-{firstname}-{lastname}-{competence}")
    public String developer(@PathParam("accountname")String accountname, @PathParam("password") String password, @PathParam("email") String email
    , @PathParam("firstname") String firstname, @PathParam("lastname") String lastname, @PathParam("competence") String competence){    
        
        return accountname + password;
}
}