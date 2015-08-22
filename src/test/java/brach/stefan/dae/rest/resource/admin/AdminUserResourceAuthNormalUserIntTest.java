package brach.stefan.dae.rest.resource.admin;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.Assert;
import org.junit.Test;

import brach.stefan.dae.test.IntegrationTest;
import brach.stefan.dae.test.helper.IntTestLoginHelper;
import brach.stefan.dae.test.helper.IntTestSignupHelper;

public class AdminUserResourceAuthNormalUserIntTest extends IntegrationTest {
    private static final String url = "http://localhost:%d/admin/user";

    @Test
    public void normalUserHasNoAccess() {
        IntTestSignupHelper.signupUser("one@test.io", "testOne", RULE);
        String authToken = IntTestLoginHelper.getAuthToken("one@test.io", "testOne", RULE);
        Client client = new JerseyClientBuilder().build();
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().header("authToken", authToken).get();
        Assert.assertEquals(403, response.getStatus());
    }
}
