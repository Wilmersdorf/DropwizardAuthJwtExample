package brach.stefan.tech.test.service.auth.impl;

import java.security.NoSuchAlgorithmException;

import org.jose4j.keys.HmacKey;

import brach.stefan.tech.test.TechTestConfiguration;
import brach.stefan.tech.test.model.Keys;
import brach.stefan.tech.test.service.auth.KeyBuilderService;

public class KeyBuilderServiceImpl implements KeyBuilderService {

    public Keys createKeys(TechTestConfiguration configuration) throws NoSuchAlgorithmException {
        Keys keys = new Keys();
        HmacKey signatureKey = createSignatureKey(configuration.getJwtSignatureSecret());
        keys.setSignatureKey(signatureKey);
        return keys;
    }

    private HmacKey createSignatureKey(String signatureSecret) {
        return new HmacKey(signatureSecret.getBytes());
    }

}
