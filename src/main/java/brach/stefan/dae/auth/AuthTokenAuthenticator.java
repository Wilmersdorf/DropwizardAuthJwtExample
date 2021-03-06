package brach.stefan.dae.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

import org.apache.commons.lang3.StringUtils;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import brach.stefan.dae.dao.UserDao;
import brach.stefan.dae.model.User;
import brach.stefan.dae.service.auth.JwtReaderService;

import com.google.common.base.Optional;
import com.google.inject.Inject;

public class AuthTokenAuthenticator implements Authenticator<String, User> {
    private final static Logger LOG = LoggerFactory.getLogger(AuthTokenAuthenticator.class);
    @Inject
    UserDao userDao;
    @Inject
    JwtReaderService jwtReaderService;

    @Override
    public Optional<User> authenticate(String authToken) throws AuthenticationException {
        String email = "";
        try {
            email = jwtReaderService.getEmailFromJwt(authToken);
        } catch (InvalidJwtException e) {
            throw new AuthenticationException(e);
        }
        if (StringUtils.isBlank(email)) {
            LOG.error("Email is blank.");
            return Optional.absent();
        } else {
            User user = userDao.findUserByEmail(email);
            return Optional.fromNullable(user);
        }
    }
}
