package brach.stefan.tech.test.rest;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

import brach.stefan.tech.test.TechTestApplication;
import brach.stefan.tech.test.TechTestConfiguration;
import brach.stefan.tech.test.rest.model.send.LoginUserDto;

public class UserResourceAdminIntTest {
    private static final String url = "http://localhost:%d/user";
    @ClassRule
    public static final DropwizardAppRule<TechTestConfiguration> RULE = new DropwizardAppRule<TechTestConfiguration>(
            TechTestApplication.class, ResourceHelpers.resourceFilePath("tech-test-stefan-integration.yml"));

    @Test
    public void adminCannotAccessGetUserConnections() {
        String authToken = getAdminAuthToken();
        Client client = new JerseyClientBuilder().build();
        Response getUserConnectionsResponse = client.target(String.format(url, RULE.getLocalPort())).request()
                .header("authToken", authToken).get();
        Assert.assertEquals(403, getUserConnectionsResponse.getStatus());
    }

    private String getAdminAuthToken() {
        Client client = new JerseyClientBuilder().build();
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("admin@test.io");
        loginUserDto.setPassword("adminpw");
        Response loginResponse = client.target(String.format("http://localhost:%d/login", RULE.getLocalPort())).request()
                .post(Entity.json(loginUserDto));
        JSONObject loginResponseJo = new JSONObject(loginResponse.readEntity(String.class));
        String authToken = loginResponseJo.optString("authToken");
        Assert.assertTrue(!StringUtils.isBlank(authToken));
        return authToken;
    }
}
