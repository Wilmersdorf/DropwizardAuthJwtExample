package brach.stefan.dae.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.ws.rs.core.Response;

import brach.stefan.dae.dao.UserConnectionDao;
import brach.stefan.dae.dao.UserDao;
import brach.stefan.dae.model.User;
import brach.stefan.dae.model.UserConnection;
import brach.stefan.dae.rest.model.back.AdminUserConnectionDto;
import brach.stefan.dae.service.AdminUserService;

import com.google.inject.Inject;

public class AdminUserServiceImpl implements AdminUserService {
    private final UserDao userDao;
    private final UserConnectionDao userConnectionDao;

    @Inject
    public AdminUserServiceImpl(UserDao userDao, UserConnectionDao userConnectionDao) {
        this.userDao = userDao;
        this.userConnectionDao = userConnectionDao;
    }

    @Override
    public Response getUsers(User principal) {
        List<User> users = userDao.getAllNormalUsers();
        Map<Long, String> emailHm = users.stream().collect(Collectors.toMap(User::getId, User::getEmail));
        List<UserConnection> serverConnections = userConnectionDao.getAllUserConnections();
        HashMap<Long, AdminUserConnectionDto> connectionDtoHm = new HashMap<Long, AdminUserConnectionDto>();
        users.forEach(user -> {
            AdminUserConnectionDto connectionDto = new AdminUserConnectionDto(user.getEmail());
            connectionDtoHm.put(user.getId(), connectionDto);
        });
        serverConnections.forEach(i -> addConnectedEmails(connectionDtoHm.get(i.getLowerId()), emailHm.get(i.getHigherId())));
        serverConnections.forEach(i -> addConnectedEmails(connectionDtoHm.get(i.getHigherId()), emailHm.get(i.getLowerId())));
        List<AdminUserConnectionDto> toReturn = new ArrayList<AdminUserConnectionDto>(connectionDtoHm.values());
        return Response.ok(toReturn).build();
    }

    private void addConnectedEmails(@Nullable AdminUserConnectionDto connectionDto, @Nullable String email) {
        if (connectionDto != null) {
            connectionDto.addConnectedToEmail(email);
        }
    }
}
