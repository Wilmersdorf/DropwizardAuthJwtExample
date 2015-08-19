package brach.stefan.tech.test.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import brach.stefan.tech.test.dao.UserConnectionDao;
import brach.stefan.tech.test.dao.UserDao;
import brach.stefan.tech.test.model.User;
import brach.stefan.tech.test.model.UserConnection;
import brach.stefan.tech.test.rest.model.back.AdminUserConnectionDto;
import brach.stefan.tech.test.service.AdminUserService;

import com.google.inject.Inject;

public class AdminUserServiceImpl implements AdminUserService {
    private final static Logger LOG = LoggerFactory.getLogger(AdminUserServiceImpl.class);
    @Inject
    private UserDao userDao;
    @Inject
    private UserConnectionDao userConnectionDao;

    @Override
    public Response getUsers(User principal) {
        List<User> users = userDao.getAllNormalUsersExcept(principal.getId());
        HashMap<Long, String> userIdEmailHm = new HashMap<Long, String>();
        for (User user : users) {
            userIdEmailHm.put(user.getId(), user.getEmail());
        }
        List<UserConnection> serverConnections = userConnectionDao.getAllUserConnections(principal.getId());
        HashMap<Long, Long> userIduserId = new HashMap<Long, Long>();
        HashMap<Long, AdminUserConnectionDto> userIdClientConnection = new HashMap<Long, AdminUserConnectionDto>();
        HashSet<Long> connectedIds = new HashSet<Long>();
        for (UserConnection serverConnection : serverConnections) {
            long otherId = 0;
            try {
                otherId = serverConnection.getNonPrincipalId(principal.getId());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            connectedIds.add(otherId);
            userIduserId.put(serverConnection.getLowerId(), serverConnection.getHigherId());
            userIduserId.put(serverConnection.getHigherId(), serverConnection.getLowerId());
        }
        for (User user : users) {
            AdminUserConnectionDto adminUserConnectionDto = new AdminUserConnectionDto();
            adminUserConnectionDto.setEmail(user.getEmail());
            if (connectedIds.contains(user.getId())) {
                adminUserConnectionDto.setConnected(true);
            }
            userIdClientConnection.put(user.getId(), adminUserConnectionDto);
        }
        userIduserId.forEach((firstId, secondId) -> {
            AdminUserConnectionDto adminUserConnectionDto = userIdClientConnection.get(firstId);
            if (adminUserConnectionDto != null) {
                String email = userIdEmailHm.get(secondId);
                if (!StringUtils.isEmpty(email)) {
                    adminUserConnectionDto.addConnectedTo(email);
                }
            }
        });
        List<AdminUserConnectionDto> toReturn = new ArrayList<AdminUserConnectionDto>(userIdClientConnection.values());
        return Response.ok(toReturn).build();
    }
}
