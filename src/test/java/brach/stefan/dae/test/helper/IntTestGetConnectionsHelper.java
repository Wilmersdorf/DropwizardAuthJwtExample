package brach.stefan.dae.test.helper;

import io.dropwizard.testing.junit.DropwizardAppRule;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.Assert;

import brach.stefan.dae.TechTestConfiguration;

public class IntTestGetConnectionsHelper {
    public static void validate(String authToken, String expected, DropwizardAppRule<TechTestConfiguration> RULE) {
        Client client = new JerseyClientBuilder().build();
        Response response = client.target(String.format("http://localhost:%d/user", RULE.getLocalPort())).request()
                .header("authToken", authToken).get();
        Assert.assertEquals(200, response.getStatus());
        Assert.assertEquals(expected, response.readEntity(String.class));
    }
}
