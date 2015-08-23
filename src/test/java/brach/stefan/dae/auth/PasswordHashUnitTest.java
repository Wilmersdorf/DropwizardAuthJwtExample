package brach.stefan.dae.auth;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PasswordHashUnitTest {
    private final static Logger LOG = LoggerFactory.getLogger(PasswordHashUnitTest.class);

    /*
     * adapted from the tests http://crackstation.net/hashing-security.htm
     */
    @Test
    public void passwordHashUnitTest() {
        try {
            for (int i = 0; i < 100; i++) {
                String password = "" + i;
                String hash = PasswordHash.createHash(password);
                String secondHash = PasswordHash.createHash(password);
                if (hash.equals(secondHash)) {
                    Assert.fail("FAILURE: TWO HASHES ARE EQUAL!");
                }
                String wrongPassword = "" + (i + 1);
                // do not accept wrong password
                Assert.assertFalse(PasswordHash.validatePassword(wrongPassword, hash));
                // do accept correct password
                Assert.assertTrue(PasswordHash.validatePassword(password, hash));
            }
        } catch (Exception ex) {
            LOG.error("fail", ex);
            Assert.fail();
        }
    }
}
