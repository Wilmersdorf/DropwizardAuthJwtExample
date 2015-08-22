package brach.stefan.dae.auth;

import io.dropwizard.auth.Authorizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import brach.stefan.dae.model.Role;
import brach.stefan.dae.model.User;

public class ExampleAuthorizer implements Authorizer<User> {
    private final static Logger LOG = LoggerFactory.getLogger(ExampleAuthorizer.class);

    @Override
    public boolean authorize(User user, String role) {
        if (user == null) {
            LOG.error("Unable to authorize since user is null.");
            return false;
        } else if (user.getRole() == null) {
            LOG.error("Unable to authorize since user role is null.");
            return false;
        } else if (role == null) {
            LOG.error("Unable to authorize since role is null.");
            return false;
        }
        Role roleToEnum = null;
        try {
            roleToEnum = Role.valueOf(role);
        } catch (IllegalArgumentException iae) {
            LOG.error("Unable to authorize since role " + role + " is not a valid role.");
            return false;
        }
        return user.getRole() == roleToEnum;
    }
}
