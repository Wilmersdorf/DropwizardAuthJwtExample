package brach.stefan.dae.rest.resource;

import io.dropwizard.auth.Auth;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import brach.stefan.dae.model.User;
import brach.stefan.dae.rest.model.send.ConnectToUserDto;
import brach.stefan.dae.service.UserService;

import com.google.inject.Inject;

@RolesAllowed("NORMAL")
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    @Inject
    UserService userService;

    @GET
    public Response getUserConnections(@Auth User principal) {
        return userService.getUserConnections(principal);
    }

    @POST
    @Path("/connection")
    public Response connectToUser(@Auth User principal, ConnectToUserDto connectToUserDto) {
        return userService.connectToUser(principal, connectToUserDto);
    }
}
