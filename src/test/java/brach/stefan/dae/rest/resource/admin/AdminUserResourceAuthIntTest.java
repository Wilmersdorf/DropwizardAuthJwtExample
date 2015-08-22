package brach.stefan.dae.rest.resource.admin;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.Assert;
import org.junit.Test;

import brach.stefan.dae.test.IntegrationTest;
import brach.stefan.dae.test.helper.IntTestLoginHelper;

public class AdminUserResourceAuthIntTest extends IntegrationTest {
    private static final String url = "http://localhost:%d/admin/user";

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
