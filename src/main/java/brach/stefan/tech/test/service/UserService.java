package brach.stefan.tech.test.service;

import javax.ws.rs.core.Response;

import brach.stefan.tech.test.model.User;
import brach.stefan.tech.test.rest.model.send.ConnectToUserDto;

public interface UserService {
    public Response getUserConnections(User principal);

    public Response connectToUser(User principal, ConnectToUserDto connectToUserDto);
}
