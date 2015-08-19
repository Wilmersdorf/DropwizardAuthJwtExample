package brach.stefan.tech.test.service;

import javax.ws.rs.core.Response;

import brach.stefan.tech.test.model.User;

public interface AdminUserService {
    public Response getUsers(User principal);
}
