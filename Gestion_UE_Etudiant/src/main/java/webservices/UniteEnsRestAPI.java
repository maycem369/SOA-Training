package webservices;

import entities.UniteEnseignement;
import metiers.UniteEnseignementBusiness;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
@Path("/ue")
public class UniteEnsRestAPI {
    static UniteEnseignementBusiness helper= new UniteEnseignementBusiness();
    //web service to get the list of UE
    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //getListUEs
    public Response getListUe(){
        return Response.status(200)
                .entity(helper.getListeUE())
                .build();
    }
    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON) //INPUT
    @Produces(MediaType.TEXT_PLAIN)
    public Response addUniteEnseignement (UniteEnseignement ue){
        if(helper.addUniteEnseignement(ue)){
            return Response.status(201).entity("Succes").build();
        }
        else{
            //400bad request
            return Response.status(400).entity("Erreur").build();
        }
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{code}")
    public Response getUEByCode(@PathParam("code") int code){
        return Response.status(200)
                .entity(helper.getUEByCode(code))
                .build();
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Response getUEBySemestre(@QueryParam("semestre") int semestre){
        return Response.status(200)
                .entity(helper.getUEBySemestre(semestre))
                .build();
    }
    @Path("/update")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateUniteEnseignement(UniteEnseignement ue){
        if(helper.updateUniteEnseignement(ue.getCode(), ue)){
            return Response.status(200).entity("Succes").build();
        }
        else{
            return Response.status(404).entity("Erreur").build();
        }
    }
    @Path("/delete/{code}")
    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteUEByCode(@PathParam("code") int code){
        if(helper.deleteUniteEnseignement(code)){
            return Response.status(200).entity("Supprimée").build();
        }
        else{
            return Response.status(404).entity("Erreur").build();
        }
    }
}
