package brach.stefan.tech.test.service.auth;

import org.jose4j.jwt.consumer.InvalidJwtException;

public interface JwtReaderService {
    public String getEmailFromJwt(String token) throws InvalidJwtException;
}
