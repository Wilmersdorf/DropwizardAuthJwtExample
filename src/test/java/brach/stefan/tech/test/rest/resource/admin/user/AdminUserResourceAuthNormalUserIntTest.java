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
import brach.stefan.tech.test.rest.helper.IntTestSignupHelper;

public class AdminUserResourceAuthNormalUserIntTest {
    private static final String url = "http://localhost:%d/admin/user";
    @ClassRule
    public static final DropwizardAppRule<TechTestConfiguration> RULE = new DropwizardAppRule<TechTestConfiguration>(
            TechTestApplication.class, ResourceHelpers.resourceFilePath("tech-test-stefan-integration.yml"));

    @Test
    public void normalUserHasNoAccess() {
        IntTestSignupHelper.signupUser("one@test.io", "testOne", RULE);
        String authToken = IntTestLoginHelper.getAuthToken("one@test.io", "testOne", RULE);
        Client client = new JerseyClientBuilder().build();
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().header("authToken", authToken).get();
        Assert.assertEquals(403, response.getStatus());
    }
}
