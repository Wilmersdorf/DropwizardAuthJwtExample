package brach.stefan.dae.rest.resource;

import io.dropwizard.auth.Auth;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import brach.stefan.dae.model.User;
import brach.stefan.dae.service.AdminUserService;

import com.google.inject.Inject;

@RolesAllowed("ADMIN")
@Path("admin/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminUserResource {
    @Inject
    AdminUserService adminUserService;

    @GET
    public Response getUsers(@Auth User principal) {
        return adminUserService.getUsers(principal);
    }
}
