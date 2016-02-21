package brach.stefan.dae.service.auth.impl;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import brach.stefan.dae.model.Keys;
import brach.stefan.dae.service.auth.JwtReaderService;

import com.google.inject.Inject;

public class JwtReaderServiceImpl implements JwtReaderService {
    private final static Logger LOG = LoggerFactory.getLogger(JwtReaderServiceImpl.class);
    private final Keys keys;

    @Inject
    public JwtReaderServiceImpl(Keys keys) {
        this.keys = keys;
    }

    public String getEmailFromJwt(String token) throws InvalidJwtException {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder().setEvaluationTime(NumericDate.now())
                .setVerificationKey(keys.getSignatureKey()).build();
        JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
        return (String) jwtClaims.getClaimValue("email");
    }
}
