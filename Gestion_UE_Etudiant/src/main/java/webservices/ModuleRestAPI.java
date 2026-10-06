package webservices;

import entities.Module;
import metiers.ModuleBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/module")
public class ModuleRestAPI {

    static ModuleBusiness helper = new ModuleBusiness();

    // GET /api/module/list : liste de tous les modules
    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListModules() {
        return Response.status(200)
                .entity(helper.getAllModules())
                .build();
    }

    // POST /api/module/add : ajouter un module
    // Le JSON doit contenir uniteEnseignement.code
    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addModule(Module module) {
        if (module == null || module.getUniteEnseignement() == null) {
            return Response.status(400).entity("UE obligatoire").build();
        }
        if (helper.getModuleByMatricule(module.getMatricule()) != null) {
            return Response.status(400).entity("Matricule déjà existant").build();
        }
        if (helper.addModule(module)) {
            return Response.status(201).entity("Succes").build();
        }
        // UE inexistante
        return Response.status(400).entity("Erreur : UE introuvable").build();
    }

    // GET /api/module/{matricule} : récupérer un module par matricule
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{matricule}")
    public Response getModuleByMatricule(@PathParam("matricule") String matricule) {
        Module m = helper.getModuleByMatricule(matricule);
        if (m == null) {
            return Response.status(404).entity("Module introuvable").build();
        }
        return Response.status(200).entity(m).build();
    }

    // GET /api/module?type=PROFESSIONNEL : récupérer les modules par type
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public Response getModulesByType(@QueryParam("type") String type) {
        if (type == null) {
            return Response.status(200).entity(helper.getAllModules()).build();
        }
        try {
            Module.TypeModule t = Module.TypeModule.valueOf(type.toUpperCase());
            return Response.status(200).entity(helper.getModulesByType(t)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(400)
                    .entity("Type invalide (TRANSVERSAL, PROFESSIONNEL, RECHERCHE)")
                    .build();
        }
    }

    // GET /api/module/ue/{code} : récupérer les modules d'une UE
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/ue/{code}")
    public Response getModulesByUE(@PathParam("code") int code) {
        entities.UniteEnseignement ue = new entities.UniteEnseignement();
        ue.setCode(code);
        return Response.status(200)
                .entity(helper.getModulesByUE(ue))
                .build();
    }

    // PUT /api/module/update : mettre à jour un module
    @Path("/update")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateModule(Module module) {
        if (module == null || module.getMatricule() == null) {
            return Response.status(400).entity("Matricule obligatoire").build();
        }
        if (helper.updateModule(module.getMatricule(), module)) {
            return Response.status(200).entity("Succes").build();
        }
        return Response.status(404).entity("Erreur : module introuvable").build();
    }

    // DELETE /api/module/delete/{matricule} : supprimer un module
    @Path("/delete/{matricule}")
    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteModule(@PathParam("matricule") String matricule) {
        if (helper.deleteModule(matricule)) {
            return Response.status(200).entity("Supprimé").build();
        }
        return Response.status(404).entity("Erreur : module introuvable").build();
    }
}