package brach.stefan.tech.test.rest;

import io.dropwizard.auth.Auth;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import brach.stefan.tech.test.model.User;
import brach.stefan.tech.test.service.AdminUserService;

import com.google.inject.Inject;

@RolesAllowed("ADMIN")
@Path("admin/user")
public class AdminUserResource {
    @Inject
    AdminUserService adminUserService;

    @GET
    public Response getUsers(@Auth User principal) {
        return adminUserService.getUsers(principal);
    }
}
