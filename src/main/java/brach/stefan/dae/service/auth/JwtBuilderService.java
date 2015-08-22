package brach.stefan.dae.service.auth;

import org.jose4j.lang.JoseException;

public interface JwtBuilderService {
    public String createJwt(String email) throws JoseException;
}
