package brach.stefan.dae.service.auth;

import java.security.NoSuchAlgorithmException;

import brach.stefan.dae.TechTestConfiguration;
import brach.stefan.dae.model.Keys;

public interface KeyBuilderService {
    public Keys createKeys(TechTestConfiguration configuration) throws NoSuchAlgorithmException;
}
