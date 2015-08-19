package brach.stefan.tech.test.auth;

import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;

import java.io.IOException;
import java.security.Principal;

import javax.annotation.Priority;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

@Priority(Priorities.AUTHENTICATION)
public class TokenAuthFilter<P extends Principal> extends AuthFilter<String, P> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthFilter.class);

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
        final String authToken = requestContext.getHeaders().getFirst("authToken");
        try {
            if (!StringUtils.isBlank(authToken)) {
                try {
                    final Optional<P> principal = authenticator.authenticate(authToken);
                    if (principal.isPresent()) {
                        requestContext.setSecurityContext(new SecurityContext() {
                            @Override
                            public Principal getUserPrincipal() {
                                return principal.get();
                            }

                            @Override
                            public boolean isUserInRole(String role) {
                                return authorizer.authorize(principal.get(), role);
                            }

                            @Override
                            public boolean isSecure() {
                                return requestContext.getSecurityContext().isSecure();
                            }

                            @Override
                            public String getAuthenticationScheme() {
                                return "TOKEN";
                            }
                        });
                        return;
                    }
                } catch (AuthenticationException e) {
                    LOGGER.warn("Error authenticating credentials", e);
                    throw new InternalServerErrorException();
                }
            }
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Error decoding credentials", e);
        }
        throw new WebApplicationException(unauthorizedHandler.buildResponse(prefix, realm));
    }

    /**
     * Builder for {@link BasicCredentialAuthFilter}.
     * <p>
     * An {@link Authenticator} must be provided during the building process.
     * </p>
     *
     * @param <P>
     *            the principal
     */
    public static class Builder<P extends Principal> extends AuthFilterBuilder<String, P, TokenAuthFilter<P>> {
        @Override
        protected TokenAuthFilter<P> newInstance() {
            return new TokenAuthFilter<>();
        }
    }
}
