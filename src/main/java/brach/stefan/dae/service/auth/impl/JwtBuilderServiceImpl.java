package brach.stefan.dae.service.auth.impl;

import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;

import brach.stefan.dae.model.Keys;
import brach.stefan.dae.service.auth.JwtBuilderService;

import com.google.inject.Inject;

public class JwtBuilderServiceImpl implements JwtBuilderService {
    private final Keys keys;

    @Inject
    public JwtBuilderServiceImpl(Keys keys) {
        this.keys = keys;
    }

    public String createJwt(String email) throws JoseException {
        JwtClaims claims = new JwtClaims();
        claims.setExpirationTimeMinutesInTheFuture(60 * 24);
        claims.setIssuedAtToNow();
        claims.setClaim("email", email);
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(keys.getSignatureKey());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA512);
        String jwt = jws.getCompactSerialization();
        return jwt;
    }
}
