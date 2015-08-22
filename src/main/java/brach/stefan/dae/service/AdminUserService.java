package brach.stefan.dae.service;

import javax.ws.rs.core.Response;

import brach.stefan.dae.model.User;

public interface AdminUserService {
    public Response getUsers(User principal);
}
