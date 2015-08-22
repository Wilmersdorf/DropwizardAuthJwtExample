package brach.stefan.dae;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TechTestConfiguration extends Configuration {
    @Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database = new DataSourceFactory();

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @NotEmpty
    private String jwtSignatureSecret;

    @JsonProperty
    public String getJwtSignatureSecret() {
        return jwtSignatureSecret;
    }

    @JsonProperty
    public void setJwtSignatureSecret(String jwtSignatureSecret) {
        this.jwtSignatureSecret = jwtSignatureSecret;
    }
}
