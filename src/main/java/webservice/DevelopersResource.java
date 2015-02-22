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

/**
 *
 * @author johny_000
 */
@Path("developers")
public class DevelopersResource {
    
    @GET
    public String developers(){
    return "web service";
}
    @GET
    @Path("{first}-{last}")
    public String developer(@PathParam("first")String first, @PathParam("last") String last){
    return first + "_" + last;
    }
}
