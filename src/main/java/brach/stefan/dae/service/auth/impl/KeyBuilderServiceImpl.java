package brach.stefan.dae.service.auth.impl;

import java.security.NoSuchAlgorithmException;

import org.jose4j.keys.HmacKey;

import brach.stefan.dae.DaeConfiguration;
import brach.stefan.dae.model.Keys;
import brach.stefan.dae.service.auth.KeyBuilderService;

public class KeyBuilderServiceImpl implements KeyBuilderService {
    public Keys createKeys(DaeConfiguration configuration) throws NoSuchAlgorithmException {
        Keys keys = new Keys();
        HmacKey signatureKey = createSignatureKey(configuration.getJwtSignatureSecret());
        keys.setSignatureKey(signatureKey);
        return keys;
    }

    private HmacKey createSignatureKey(String signatureSecret) {
        return new HmacKey(signatureSecret.getBytes());
    }
}
