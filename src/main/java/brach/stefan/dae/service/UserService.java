package brach.stefan.dae.service;

import javax.ws.rs.core.Response;

import brach.stefan.dae.model.User;
import brach.stefan.dae.rest.model.send.ConnectToUserDto;

public interface UserService {
    public Response getUserConnections(User principal);

    public Response connectToUser(User principal, ConnectToUserDto connectToUserDto);
}
