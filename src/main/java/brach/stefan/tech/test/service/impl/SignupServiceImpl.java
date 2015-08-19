package brach.stefan.tech.test.service.impl;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import brach.stefan.tech.test.auth.MyPasswordValidator;
import brach.stefan.tech.test.auth.PasswordHash;
import brach.stefan.tech.test.dao.UserDao;
import brach.stefan.tech.test.model.User;
import brach.stefan.tech.test.msg.ErrMsg;
import brach.stefan.tech.test.msg.ErrMsgWebException;
import brach.stefan.tech.test.rest.model.send.SignupUserDto;
import brach.stefan.tech.test.rest.model.send.validator.SignupUserDtoValidator;
import brach.stefan.tech.test.service.SignupService;

import com.google.inject.Inject;

public class SignupServiceImpl implements SignupService {
    private final static Logger LOG = LoggerFactory.getLogger(SignupServiceImpl.class);
    private final static MyPasswordValidator myPasswordValidator = new MyPasswordValidator();
    @Inject
    private UserDao userDao;

    public Response signup(SignupUserDto signupUserDto) {
        SignupUserDtoValidator.validate(signupUserDto, myPasswordValidator);
        String hash = "";
        try {
            hash = PasswordHash.createHash(signupUserDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Unable to create hash.", e);
            throw new ErrMsgWebException(ErrMsg.SIGNUP_FAIL);
        } catch (InvalidKeySpecException e) {
            LOG.error("Unable to create hash.", e);
            throw new ErrMsgWebException(ErrMsg.SIGNUP_FAIL);
        }
        User user = new User(signupUserDto.getEmail(), hash);
        try {
            userDao.createUser(user);
        } catch (Exception exception) {
            LOG.warn("Unable to create user because user already exists.", exception);
            throw new ErrMsgWebException(ErrMsg.SIGNUP_FAIL_USER_ALREADY_EXISTS);
        }
        return Response.ok().build();
    }
}
