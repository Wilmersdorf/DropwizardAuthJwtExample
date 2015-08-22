package brach.stefan.dae.rest.resource.user;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import brach.stefan.dae.rest.model.error.ErrMsg;
import brach.stefan.dae.rest.model.send.ConnectToUserDto;
import brach.stefan.dae.test.IntegrationTest;
import brach.stefan.dae.test.helper.IntTestGetConnectionsHelper;
import brach.stefan.dae.test.helper.IntTestLoginHelper;
import brach.stefan.dae.test.helper.IntTestSignupHelper;
import brach.stefan.dae.test.validator.ResponseValidator;

public class UserResourceConnectIntTest extends IntegrationTest {
    private static final String url = "http://localhost:%d/user/connection";

    @BeforeClass
    public static void oneTimeSetUp() {
        IntTestSignupHelper.signupUser("one@test.io", "testOne", RULE);
    }

    @Test
    public void adminCannotAccessEndpoint() {
        ConnectToUserDto connectToUserDto = new ConnectToUserDto();
        connectToUserDto.setEmail("one@test.io");
        connectToUserDto.setConnected(true);
        String authToken = IntTestLoginHelper.getAuthToken("admin@test.io", "adminpw", RULE);
        Client client = new JerseyClientBuilder().build();
        Response connectToUserResponse = client.target(String.format(url, RULE.getLocalPort())).request()
                .header("authToken", authToken).post(Entity.json(connectToUserDto));
        Assert.assertEquals(403, connectToUserResponse.getStatus());
    }

    @Test
    public void needAuthToken() {
        ConnectToUserDto connectToUserDto = new ConnectToUserDto();
        connectToUserDto.setEmail("two@test.io");
        Client client = new JerseyClientBuilder().build();
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().post(Entity.json(connectToUserDto));
        Assert.assertEquals(401, response.getStatus());
    }

    @Test
    public void needConnectToUserDto() {
        String authToken = IntTestLoginHelper.getAuthToken("one@test.io", "testOne", RULE);
        Client client = new JerseyClientBuilder().build();
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().header("authToken", authToken)
                .post(null);
        ResponseValidator.validate(response, 400, ErrMsg.CONNECT_TO_USER_FAIL_INVALID_JSON.toString());
    }

    @Test
    public void needEmailInConnectToUserDto() {
        String authToken = IntTestLoginHelper.getAuthToken("one@test.io", "testOne", RULE);
        ConnectToUserDto connectToUserDto = new ConnectToUserDto();
        connectToUserDto.setEmail("  ");
        Client client = new JerseyClientBuilder().build();
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().header("authToken", authToken)
                .post(Entity.json(connectToUserDto));
        ResponseValidator.validate(response, 400, ErrMsg.CONNECT_TO_USER_FAIL_INVALID_JSON.toString());
    }

    @Test
    public void needToConnectToExistingUser() {
        String authToken = IntTestLoginHelper.getAuthToken("one@test.io", "testOne", RULE);
        ConnectToUserDto connectToUserDto = new ConnectToUserDto();
        connectToUserDto.setEmail("nonexisting@test.org");
        connectToUserDto.setConnected(true);
        Client client = new JerseyClientBuilder().build();
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().header("authToken", authToken)
                .post(Entity.json(connectToUserDto));
        ResponseValidator.validate(response, 404, ErrMsg.CONNECT_TO_USER_FAIL_USER_NOT_FOUND.toString());
    }

    @Test
    public void cannotConnectToAdmin() {
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

    @Test
    public void cannotConnectToOnself() {
        String authToken = IntTestLoginHelper.getAuthToken("one@test.io", "testOne", RULE);
        ConnectToUserDto connectToUserDto = new ConnectToUserDto();
        connectToUserDto.setEmail("one@test.io");
        connectToUserDto.setConnected(true);
        Client client = new JerseyClientBuilder().build();
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().header("authToken", authToken)
                .post(Entity.json(connectToUserDto));
        ResponseValidator.validate(response, 400, ErrMsg.CONNECT_TO_USER_FAIL_CANNOT_CONNECT_TO_ONESELF.toString());
    }

    @Test
    public void connectSuccessTest() {
        String authToken = IntTestLoginHelper.getAuthToken("one@test.io", "testOne", RULE);
        IntTestGetConnectionsHelper.validate(authToken, "[]", RULE);
        IntTestSignupHelper.signupUser("two@test.io", "testOne", RULE);
        connectSuccess(false, "two@test.io");
        IntTestGetConnectionsHelper.validate(authToken, "[{\"email\":\"two@test.io\",\"connected\":false}]", RULE);
        connectSuccess(true, "two@test.io");
        IntTestGetConnectionsHelper.validate(authToken, "[{\"email\":\"two@test.io\",\"connected\":true}]", RULE);
        connectSuccess(true, "two@test.io");
        IntTestGetConnectionsHelper.validate(authToken, "[{\"email\":\"two@test.io\",\"connected\":true}]", RULE);
        connectSuccess(false, "two@test.io");
        IntTestGetConnectionsHelper.validate(authToken, "[{\"email\":\"two@test.io\",\"connected\":false}]", RULE);
    }

    private void connectSuccess(boolean connected, String email) {
        String authToken = IntTestLoginHelper.getAuthToken("one@test.io", "testOne", RULE);
        ConnectToUserDto connectToUserDto = new ConnectToUserDto();
        connectToUserDto.setEmail(email);
        connectToUserDto.setConnected(connected);
        Client client = new JerseyClientBuilder().build();
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().header("authToken", authToken)
                .post(Entity.json(connectToUserDto));
        Assert.assertEquals(200, response.getStatus());
    }
}
