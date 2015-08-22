package brach.stefan.dae.service.auth;

import java.security.NoSuchAlgorithmException;

import brach.stefan.dae.DaeConfiguration;
import brach.stefan.dae.model.Keys;

public interface KeyBuilderService {
    public Keys createKeys(DaeConfiguration configuration) throws NoSuchAlgorithmException;
}
