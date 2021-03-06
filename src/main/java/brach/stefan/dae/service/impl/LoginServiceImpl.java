package brach.stefan.dae.service.impl;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.ws.rs.core.Response;

import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import brach.stefan.dae.auth.PasswordHash;
import brach.stefan.dae.dao.UserDao;
import brach.stefan.dae.model.User;
import brach.stefan.dae.rest.model.back.AuthTokenRoleDto;
import brach.stefan.dae.rest.model.error.ErrMsg;
import brach.stefan.dae.rest.model.error.ErrMsgWebException;
import brach.stefan.dae.rest.model.send.LoginUserDto;
import brach.stefan.dae.rest.model.send.validator.LoginUserDtoValidator;
import brach.stefan.dae.service.LoginService;
import brach.stefan.dae.service.auth.JwtBuilderService;

import com.google.inject.Inject;

public class LoginServiceImpl implements LoginService {
    private final static Logger LOG = LoggerFactory.getLogger(LoginServiceImpl.class);
    private final UserDao userDao;
    private final JwtBuilderService jwtBuilderService;

    @Inject
    public LoginServiceImpl(UserDao userDao, JwtBuilderService jwtBuilderService) {
        this.userDao = userDao;
        this.jwtBuilderService = jwtBuilderService;
    }

    @Override
    public Response login(LoginUserDto loginUserDto) {
        LoginUserDtoValidator.validate(loginUserDto);
        User user = userDao.findUserByEmail(loginUserDto.getEmail());
        if (user == null) {
            LOG.warn("User not found.");
            throw new ErrMsgWebException(ErrMsg.LOGIN_FAIL_USER_NOT_FOUND_OR_WRONG_PASSWORD);
        }
        boolean isAllowed = false;
        try {
            isAllowed = PasswordHash.validatePassword(loginUserDto.getPassword(), user.getHash());
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Unable to compute hash.", e);
            throw new ErrMsgWebException(ErrMsg.LOGIN_FAIL_USER_NOT_FOUND_OR_WRONG_PASSWORD);
        } catch (InvalidKeySpecException e) {
            LOG.warn("Unable to compute hash.", e);
            throw new ErrMsgWebException(ErrMsg.LOGIN_FAIL_USER_NOT_FOUND_OR_WRONG_PASSWORD);
        }
        if (isAllowed) {
            String authToken;
            try {
                authToken = jwtBuilderService.createJwt(loginUserDto.getEmail());
            } catch (JoseException e) {
                LOG.error("Unable to create authToken.", e);
                throw new ErrMsgWebException(ErrMsg.LOGIN_FAIL_USER_NOT_FOUND_OR_WRONG_PASSWORD);
            }
            AuthTokenRoleDto authTokenRoleDto = new AuthTokenRoleDto(authToken, user.getRole());
            return Response.ok().entity(authTokenRoleDto).build();
        } else {
            throw new ErrMsgWebException(ErrMsg.LOGIN_FAIL_USER_NOT_FOUND_OR_WRONG_PASSWORD);
        }
    }
}
