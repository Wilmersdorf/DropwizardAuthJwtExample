package brach.stefan.tech.test.service.auth.impl;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import brach.stefan.tech.test.dao.UserDao;
import brach.stefan.tech.test.model.Keys;
import brach.stefan.tech.test.service.auth.JwtReaderService;

import com.google.inject.Inject;

public class JwtReaderServiceImpl implements JwtReaderService {
    private final static Logger LOG = LoggerFactory.getLogger(JwtReaderServiceImpl.class);
    @Inject
    private Keys keys;
    @Inject
    UserDao userDao;

    public String getEmailFromJwt(String token) throws InvalidJwtException {
        if (userDao == null) {
            LOG.info("userDao is null");
        }
        if (keys == null) {
            LOG.info("keys are null");
        }
        JwtConsumer jwtConsumer = new JwtConsumerBuilder().setEvaluationTime(NumericDate.now())
                .setVerificationKey(keys.getSignatureKey()).build();
        JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
        return (String) jwtClaims.getClaimValue("email");
    }
}
