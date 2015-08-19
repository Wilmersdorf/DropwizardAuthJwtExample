package brach.stefan.tech.test.service.auth;

import java.security.NoSuchAlgorithmException;

import brach.stefan.tech.test.TechTestConfiguration;
import brach.stefan.tech.test.model.Keys;

public interface KeyBuilderService {
    public Keys createKeys(TechTestConfiguration configuration) throws NoSuchAlgorithmException;
}
