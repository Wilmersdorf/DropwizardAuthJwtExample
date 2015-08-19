package brach.stefan.tech.test.rest;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

import brach.stefan.tech.test.TechTestApplication;
import brach.stefan.tech.test.TechTestConfiguration;
import brach.stefan.tech.test.msg.ErrMsg;
import brach.stefan.tech.test.rest.helper.IntTestLoginHelper;
import brach.stefan.tech.test.rest.helper.IntTestSignupHelper;
import brach.stefan.tech.test.rest.model.send.ConnectToUserDto;
import brach.stefan.tech.test.test.validator.ResponseValidator;
import brach.stefan.tech.test.test.validator.UserConnectionValidator;

public class UserResourceConnectIntTest {
    private static final String url = "http://localhost:%d/user/connection";
    @ClassRule
    public static final DropwizardAppRule<TechTestConfiguration> RULE = new DropwizardAppRule<TechTestConfiguration>(
            TechTestApplication.class, ResourceHelpers.resourceFilePath("tech-test-stefan-integration.yml"));

    @Test
    public void connectTestCredentials() {
        IntTestSignupHelper.signupUser("one@test.io", "testOne", RULE);
        adminCannotAccessEndpoint();
        needAuthToken();
        needConnectToUserDto();
        needEmailInConnectToUserDto();
        needToConnectToExistingUser();
        cannotConnectToAdmin();
        cannotConnectToOnself();
        String authToken = IntTestLoginHelper.getAuthToken("one@test.io", "testOne", RULE);
        UserConnectionValidator.validate(authToken, "[]", RULE);
        IntTestSignupHelper.signupUser("two@test.io", "testOne", RULE);
        connectSuccess(false);
        connectSuccess(true);
        connectSuccess(true);
        connectSuccess(false);
    }

    private void adminCannotAccessEndpoint() {
        ConnectToUserDto connectToUserDto = new ConnectToUserDto();
        connectToUserDto.setEmail("one@test.io");
        connectToUserDto.setConnected(true);
        String authToken = IntTestLoginHelper.getAuthToken("admin@test.io", "adminpw", RULE);
        Client client = new JerseyClientBuilder().build();
        Response connectToUserResponse = client.target(String.format(url, RULE.getLocalPort())).request()
                .header("authToken", authToken).post(Entity.json(connectToUserDto));
        Assert.assertEquals(403, connectToUserResponse.getStatus());
    }

    private void needAuthToken() {
        ConnectToUserDto connectToUserDto = new ConnectToUserDto();
        connectToUserDto.setEmail("two@test.io");
        Client client = new JerseyClientBuilder().build();
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().post(Entity.json(connectToUserDto));
        Assert.assertEquals(401, response.getStatus());
    }

    private void needConnectToUserDto() {
        String authToken = IntTestLoginHelper.getAuthToken("one@test.io", "testOne", RULE);
        Client client = new JerseyClientBuilder().build();
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().header("authToken", authToken)
                .post(null);
        ResponseValidator.validate(response, 400, ErrMsg.CONNECT_TO_USER_FAIL_INVALID_JSON.toString());
    }

    private void needEmailInConnectToUserDto() {
        String authToken = IntTestLoginHelper.getAuthToken("one@test.io", "testOne", RULE);
        ConnectToUserDto connectToUserDto = new ConnectToUserDto();
        connectToUserDto.setEmail("  ");
        Client client = new JerseyClientBuilder().build();
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().header("authToken", authToken)
                .post(Entity.json(connectToUserDto));
        ResponseValidator.validate(response, 400, ErrMsg.CONNECT_TO_USER_FAIL_INVALID_JSON.toString());
    }

    private void needToConnectToExistingUser() {
        String authToken = IntTestLoginHelper.getAuthToken("one@test.io", "testOne", RULE);
        ConnectToUserDto connectToUserDto = new ConnectToUserDto();
        connectToUserDto.setEmail("nonexisting@test.org");
        connectToUserDto.setConnected(true);
        Client client = new JerseyClientBuilder().build();
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().header("authToken", authToken)
                .post(Entity.json(connectToUserDto));
        ResponseValidator.validate(response, 404, ErrMsg.CONNECT_TO_USER_FAIL_USER_NOT_FOUND.toString());
    }

    private void cannotConnectToAdmin() {
        // test that admin user exists in database
        IntTestLoginHelper.getAuthToken("admin@test.io", "adminpw", RULE);
        String authToken = IntTestLoginHelper.getAuthToken("one@test.io", "testOne", RULE);
        ConnectToUserDto connectToUserDto = new ConnectToUserDto();
        connectToUserDto.setEmail("admin@test.io");
        connectToUserDto.setConnected(true);
        Client client = new JerseyClientBuilder().build();
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().header("authToken", authToken)
                .post(Entity.json(connectToUserDto));
        ResponseValidator.validate(response, 404, ErrMsg.CONNECT_TO_USER_FAIL_USER_NOT_FOUND.toString());
    }

    private void cannotConnectToOnself() {
        String authToken = IntTestLoginHelper.getAuthToken("one@test.io", "testOne", RULE);
        ConnectToUserDto connectToUserDto = new ConnectToUserDto();
        connectToUserDto.setEmail("one@test.io");
        connectToUserDto.setConnected(true);
        Client client = new JerseyClientBuilder().build();
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().header("authToken", authToken)
                .post(Entity.json(connectToUserDto));
        ResponseValidator.validate(response, 400, ErrMsg.CONNECT_TO_USER_FAIL_CANNOT_CONNECT_TO_ONESELF.toString());
    }

    private void connectSuccess(boolean connected) {
        String authToken = IntTestLoginHelper.getAuthToken("one@test.io", "testOne", RULE);
        ConnectToUserDto connectToUserDto = new ConnectToUserDto();
        connectToUserDto.setEmail("two@test.io");
        connectToUserDto.setConnected(connected);
        Client client = new JerseyClientBuilder().build();
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().header("authToken", authToken)
                .post(Entity.json(connectToUserDto));
        Assert.assertEquals(200, response.getStatus());
    }
}
