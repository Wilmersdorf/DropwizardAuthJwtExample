package brach.stefan.dae.service.impl;

import java.util.HashSet;
import java.util.List;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import brach.stefan.dae.dao.UserConnectionDao;
import brach.stefan.dae.dao.UserDao;
import brach.stefan.dae.exception.IdConflictException;
import brach.stefan.dae.exception.NoSuchIdException;
import brach.stefan.dae.model.Role;
import brach.stefan.dae.model.User;
import brach.stefan.dae.model.UserConnection;
import brach.stefan.dae.model.extractor.UserConnectionListExtractor;
import brach.stefan.dae.rest.model.back.UserConnectionDto;
import brach.stefan.dae.rest.model.back.creator.UserConnectionDtoCreator;
import brach.stefan.dae.rest.model.error.ErrMsg;
import brach.stefan.dae.rest.model.error.ErrMsgWebException;
import brach.stefan.dae.rest.model.send.ConnectToUserDto;
import brach.stefan.dae.rest.model.send.validator.ConnectToUserDtoValidator;
import brach.stefan.dae.service.UserService;

import com.google.inject.Inject;

public class UserServiceImpl implements UserService {
    private final static Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    @Inject
    private UserDao userDao;
    @Inject
    private UserConnectionDao userConnectionDao;

    @Override
    public Response getUserConnections(User principal) {
        List<User> users = userDao.getAllNormalUsersExcept(principal.getId());
        List<UserConnection> serverConnections = userConnectionDao.getAllUserConnectionsByUser(principal.getId());
        HashSet<Long> connectedIds = null;
        try {
            connectedIds = UserConnectionListExtractor.extractOtherUserIds(serverConnections, principal.getId());
        } catch (NoSuchIdException e) {
            LOG.error("Unable to extract connectedIds.", e);
            throw new ErrMsgWebException(ErrMsg.GET_USER_CONNECTIONS_FAIL);
        }
        List<UserConnectionDto> toReturn = UserConnectionDtoCreator.createUserConnectionDtoList(users, connectedIds);
        return Response.ok(toReturn).build();
    }

    @Override
    public Response connectToUser(User principal, ConnectToUserDto connectToUserDto) {
        ConnectToUserDtoValidator.validate(connectToUserDto);
        String email = connectToUserDto.getEmail();
        User userToConnect = userDao.findUserByEmail(email);
        if (userToConnect == null) {
            throw new ErrMsgWebException(ErrMsg.CONNECT_TO_USER_FAIL_USER_NOT_FOUND);
        } else if (userToConnect.getRole() == Role.ADMIN) {
            throw new ErrMsgWebException(ErrMsg.CONNECT_TO_USER_FAIL_USER_NOT_FOUND);
        }
        boolean isConnected = connectToUserDto.isConnected();
        UserConnection userConnection;
        try {
            userConnection = new UserConnection(principal.getId(), userToConnect.getId());
        } catch (IdConflictException e) {
            throw new ErrMsgWebException(ErrMsg.CONNECT_TO_USER_FAIL_CANNOT_CONNECT_TO_ONESELF);
        }
        if (isConnected == true) {
            userConnectionDao.insertConnection(userConnection);
        } else {
            userConnectionDao.deleteConnection(userConnection);
        }
        return Response.ok().build();
    }
}
