package brach.stefan.dae.rest.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import brach.stefan.dae.rest.model.send.LoginUserDto;
import brach.stefan.dae.service.LoginService;

import com.google.inject.Inject;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {
    @Inject
    private LoginService loginService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginUserDto loginUserDto) {
        return loginService.login(loginUserDto);
    }
}
