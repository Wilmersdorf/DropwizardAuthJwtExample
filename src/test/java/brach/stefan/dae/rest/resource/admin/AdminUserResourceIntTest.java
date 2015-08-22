package brach.stefan.dae.rest.resource.admin;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.Assert;
import org.junit.Test;

import brach.stefan.dae.test.IntegrationTest;
import brach.stefan.dae.test.helper.IntTestConnectHelper;
import brach.stefan.dae.test.helper.IntTestLoginHelper;
import brach.stefan.dae.test.helper.IntTestSignupHelper;

public class AdminUserResourceIntTest extends IntegrationTest {
    private static final String url = "http://localhost:%d/admin/user";

    @Test
    public void getConnectionsOfThreeUsers() {
        // there are no connections yet
        getConnectionsAndValidate("[]");
        // sign up first user and validate user has no connections
        IntTestSignupHelper.signupUser("one@test.io", "testOne", RULE);
        getConnectionsAndValidate("[{\"email\":\"one@test.io\",\"connectedToEmails\":[]}]");
        // sign up second user and validate no user has any connections
        IntTestSignupHelper.signupUser("two@test.io", "testTwo", RULE);
        getConnectionsAndValidate("[{\"email\":\"one@test.io\",\"connectedToEmails\":[]},"
                + "{\"email\":\"two@test.io\",\"connectedToEmails\":[]}]");
        // connect first user to second and validate connection
        String authToken1 = IntTestLoginHelper.getAuthToken("one@test.io", "testOne", RULE);
        IntTestConnectHelper.connect(authToken1, "two@test.io", true, RULE);
        getConnectionsAndValidate("[{\"email\":\"one@test.io\",\"connectedToEmails\":[\"two@test.io\"]},"
                + "{\"email\":\"two@test.io\",\"connectedToEmails\":[\"one@test.io\"]}]");
        // connect second user to first and validate connection unchanged
        String authToken2 = IntTestLoginHelper.getAuthToken("two@test.io", "testTwo", RULE);
        IntTestConnectHelper.connect(authToken2, "one@test.io", true, RULE);
        getConnectionsAndValidate("[{\"email\":\"one@test.io\",\"connectedToEmails\":[\"two@test.io\"]},"
                + "{\"email\":\"two@test.io\",\"connectedToEmails\":[\"one@test.io\"]}]");
        // unconnect second user from first user and validate they're not
        // connected
        IntTestConnectHelper.connect(authToken2, "one@test.io", false, RULE);
        getConnectionsAndValidate("[{\"email\":\"one@test.io\",\"connectedToEmails\":[]},"
                + "{\"email\":\"two@test.io\",\"connectedToEmails\":[]}]");
        // unconnect first user from second user and validate they're not
        // connected
        IntTestConnectHelper.connect(authToken1, "two@test.io", false, RULE);
        getConnectionsAndValidate("[{\"email\":\"one@test.io\",\"connectedToEmails\":[]},"
                + "{\"email\":\"two@test.io\",\"connectedToEmails\":[]}]");
        // signup third user and validate there are no connections
        IntTestSignupHelper.signupUser("three@test.io", "testThree", RULE);
        getConnectionsAndValidate("[{\"email\":\"one@test.io\",\"connectedToEmails\":[]},"
                + "{\"email\":\"two@test.io\",\"connectedToEmails\":[]},"
                + "{\"email\":\"three@test.io\",\"connectedToEmails\":[]}]");
        // connect third to first user
        String authToken3 = IntTestLoginHelper.getAuthToken("three@test.io", "testThree", RULE);
        IntTestConnectHelper.connect(authToken3, "one@test.io", true, RULE);
        getConnectionsAndValidate("[{\"email\":\"one@test.io\",\"connectedToEmails\":[\"three@test.io\"]},"
                + "{\"email\":\"two@test.io\",\"connectedToEmails\":[]},"
                + "{\"email\":\"three@test.io\",\"connectedToEmails\":[\"one@test.io\"]}]");
        // connect third to second user
        IntTestConnectHelper.connect(authToken3, "two@test.io", true, RULE);
        getConnectionsAndValidate("[{\"email\":\"one@test.io\",\"connectedToEmails\":[\"three@test.io\"]},"
                + "{\"email\":\"two@test.io\",\"connectedToEmails\":[\"three@test.io\"]},"
                + "{\"email\":\"three@test.io\",\"connectedToEmails\":[\"one@test.io\",\"two@test.io\"]}]");
        // connect second to first user
        IntTestConnectHelper.connect(authToken2, "one@test.io", true, RULE);
        getConnectionsAndValidate("[{\"email\":\"one@test.io\",\"connectedToEmails\":[\"three@test.io\",\"two@test.io\"]},"
                + "{\"email\":\"two@test.io\",\"connectedToEmails\":[\"three@test.io\",\"one@test.io\"]},"
                + "{\"email\":\"three@test.io\",\"connectedToEmails\":[\"one@test.io\",\"two@test.io\"]}]");
    }

    private void getConnectionsAndValidate(String expected) {
        String authToken = IntTestLoginHelper.getAuthToken("admin@test.io", "adminpw", RULE);
        Client client = new JerseyClientBuilder().build();
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().header("authToken", authToken).get();
        Assert.assertEquals(200, response.getStatus());
        Assert.assertEquals(expected, response.readEntity(String.class));
    }
}
