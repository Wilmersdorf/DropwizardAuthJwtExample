package brach.stefan.tech.test.rest.resource.admin.user;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

import brach.stefan.tech.test.TechTestApplication;
import brach.stefan.tech.test.TechTestConfiguration;
import brach.stefan.tech.test.rest.helper.IntTestLoginHelper;

public class AdminUserResourceAuthIntTest {
    private static final String url = "http://localhost:%d/admin/user";
    @ClassRule
    public static final DropwizardAppRule<TechTestConfiguration> RULE = new DropwizardAppRule<TechTestConfiguration>(
            TechTestApplication.class, ResourceHelpers.resourceFilePath("tech-test-stefan-integration.yml"));

    @Test
    public void needAuthToken() {
        Client client = new JerseyClientBuilder().build();
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().get();
        Assert.assertEquals(401, response.getStatus());
    }

    @Test
    public void needValidAuthToken() {
        Client client = new JerseyClientBuilder().build();
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().header("authToken", "abc").get();
        Assert.assertEquals(401, response.getStatus());
    }

    @Test
    public void getConnectionsSuccess() {
        String authToken = IntTestLoginHelper.getAuthToken("admin@test.io", "adminpw", RULE);
        Client client = new JerseyClientBuilder().build();
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().header("authToken", authToken).get();
        Assert.assertEquals(200, response.getStatus());
        Assert.assertEquals("[]", response.readEntity(String.class));
    }
}
