package brach.stefan.tech.test.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

import org.jose4j.jwt.consumer.InvalidJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import brach.stefan.tech.test.dao.UserDao;
import brach.stefan.tech.test.model.User;
import brach.stefan.tech.test.service.auth.JwtReaderService;

import com.google.common.base.Optional;
import com.google.inject.Inject;

public class ExampleAuthenticator implements Authenticator<String, User> {
    private final static Logger LOG = LoggerFactory.getLogger(ExampleAuthenticator.class);
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
        User user = userDao.findUserByEmail(email);
        return Optional.fromNullable(user);
    }
}
