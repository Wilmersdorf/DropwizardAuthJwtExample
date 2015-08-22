package brach.stefan.dae.service;

import javax.ws.rs.core.Response;

import brach.stefan.dae.rest.model.send.SignupUserDto;

public interface SignupService {
    public Response signup(SignupUserDto userDto);
}
