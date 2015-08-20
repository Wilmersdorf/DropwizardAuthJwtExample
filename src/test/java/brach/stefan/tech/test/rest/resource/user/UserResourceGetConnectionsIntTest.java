package brach.stefan.tech.test.rest.resource.user;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import brach.stefan.tech.test.TechTestApplication;
import brach.stefan.tech.test.TechTestConfiguration;
import brach.stefan.tech.test.rest.helper.IntTestConnectHelper;
import brach.stefan.tech.test.rest.helper.IntTestLoginHelper;
import brach.stefan.tech.test.rest.helper.IntTestSignupHelper;

public class UserResourceGetConnectionsIntTest {
    private static final String url = "http://localhost:%d/user";
    @ClassRule
    public static final DropwizardAppRule<TechTestConfiguration> RULE = new DropwizardAppRule<TechTestConfiguration>(
            TechTestApplication.class, ResourceHelpers.resourceFilePath("tech-test-stefan-integration.yml"));

    @BeforeClass
    public static void oneTimeSetUp() {
        IntTestSignupHelper.signupUser("one@test.io", "testOne", RULE);
    }

    @Test
    public void adminCannotAccessGetUserConnections() {
        String authToken = IntTestLoginHelper.getAuthToken("admin@test.io", "adminpw", RULE);
        Client client = new JerseyClientBuilder().build();
        Response getUserConnectionsResponse = client.target(String.format(url, RULE.getLocalPort())).request()
                .header("authToken", authToken).get();
        Assert.assertEquals(403, getUserConnectionsResponse.getStatus());
    }

    @Test
    public void needAuthToken() {
        Client client = new JerseyClientBuilder().build();
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().get();
        Assert.assertEquals(401, response.getStatus());
    }

    @Test
    public void getConnectionsSuccess() {
        String authToken = IntTestLoginHelper.getAuthToken("one@test.io", "testOne", RULE);
        Client client = new JerseyClientBuilder().build();
        // no connections yet
        Response response = client.target(String.format("http://localhost:%d/user", RULE.getLocalPort())).request()
                .header("authToken", authToken).get();
        Assert.assertEquals(200, response.getStatus());
        Assert.assertEquals("[]", response.readEntity(String.class));
        // sign up second user and check not connected
        IntTestSignupHelper.signupUser("two@test.io", "testOne", RULE);
        Response responseNotConnected = client.target(String.format("http://localhost:%d/user", RULE.getLocalPort())).request()
                .header("authToken", authToken).get();
        Assert.assertEquals(200, responseNotConnected.getStatus());
        Assert.assertEquals("[{\"email\":\"two@test.io\",\"connected\":false}]", responseNotConnected.readEntity(String.class));
        // connect to second user and check connected
        IntTestConnectHelper.connect(authToken, "two@test.io", true, RULE);
        Response responseConnected = client.target(String.format("http://localhost:%d/user", RULE.getLocalPort())).request()
                .header("authToken", authToken).get();
        Assert.assertEquals(200, responseConnected.getStatus());
        Assert.assertEquals("[{\"email\":\"two@test.io\",\"connected\":true}]", responseConnected.readEntity(String.class));
    }
}
