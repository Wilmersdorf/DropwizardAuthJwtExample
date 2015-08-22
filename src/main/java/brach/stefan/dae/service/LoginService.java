package brach.stefan.dae.service;

import javax.ws.rs.core.Response;

import brach.stefan.dae.rest.model.send.LoginUserDto;

public interface LoginService {
    public Response login(LoginUserDto userDto);
}
