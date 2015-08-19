package brach.stefan.tech.test.model;

import org.jose4j.keys.HmacKey;

public class Keys {
    private HmacKey signatureKey;

    public HmacKey getSignatureKey() {
        return signatureKey;
    }

    public void setSignatureKey(HmacKey signatureKey) {
        this.signatureKey = signatureKey;
    }
}
