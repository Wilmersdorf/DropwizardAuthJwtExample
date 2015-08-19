package brach.stefan.tech.test.service;

import javax.ws.rs.core.Response;

import brach.stefan.tech.test.rest.model.send.SignupUserDto;

public interface SignupService {
    public Response signup(SignupUserDto userDto);
}
