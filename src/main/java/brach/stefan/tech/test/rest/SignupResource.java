package brach.stefan.tech.test.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import brach.stefan.tech.test.rest.model.send.SignupUserDto;
import brach.stefan.tech.test.service.SignupService;

import com.google.inject.Inject;

@Path("/signup")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SignupResource {
    @Inject
    SignupService signupService;

    @POST
    public Response signup(SignupUserDto signupUserDto) {
        return signupService.signup(signupUserDto);
    }
}
