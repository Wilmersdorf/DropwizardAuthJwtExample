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
import brach.stefan.tech.test.model.Role;
import brach.stefan.tech.test.msg.ErrMsg;
import brach.stefan.tech.test.rest.model.send.LoginUserDto;
import brach.stefan.tech.test.rest.model.send.SignupUserDto;
import brach.stefan.tech.test.test.validator.ResponseValidator;

public class LoginResourceIntTest {
    private static final String url = "http://localhost:%d/login";
    @ClassRule
    public static final DropwizardAppRule<TechTestConfiguration> RULE = new DropwizardAppRule<TechTestConfiguration>(
            TechTestApplication.class, ResourceHelpers.resourceFilePath("tech-test-stefan-integration.yml"));

    @Test
    public void noArgument() {
        Client client = new JerseyClientBuilder().build();
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().post(null);
        ResponseValidator.validate(response, 400, ErrMsg.LOGIN_FAIL_NO_CREDENTIALS.toString());
    }

    @Test
    public void noEmail() {
        Client client = new JerseyClientBuilder().build();
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setPassword("test123");
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().post(Entity.json(loginUserDto));
        ResponseValidator.validate(response, 400, ErrMsg.LOGIN_FAIL_NO_EMAIL.toString());
    }

    @Test
    public void noPassword() {
        Client client = new JerseyClientBuilder().build();
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("test@gmail.com");
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().post(Entity.json(loginUserDto));
        ResponseValidator.validate(response, 400, ErrMsg.LOGIN_FAIL_NO_PASSWORD.toString());
    }

    @Test
    public void userNotFound() {
        Client client = new JerseyClientBuilder().build();
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("user.not.found@gmail.com");
        loginUserDto.setPassword("test123");
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().post(Entity.json(loginUserDto));
        ResponseValidator.validate(response, 403, ErrMsg.LOGIN_FAIL_USER_NOT_FOUND_OR_WRONG_PASSWORD.toString());
    }

    @Test
    public void signupUserLoginUserSuccess() {
        Client client = new JerseyClientBuilder().build();
        // signup user
        SignupUserDto signupUserDto = new SignupUserDto();
        signupUserDto.setEmail("success@gmail.com");
        signupUserDto.setPassword("test123");
        client.target(String.format("http://localhost:%d/signup", RULE.getLocalPort())).request()
                .post(Entity.json(signupUserDto));
        // login user
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("success@gmail.com");
        loginUserDto.setPassword("test123");
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().post(Entity.json(loginUserDto));
        Assert.assertEquals(200, response.getStatus());
        JSONObject responseJo = new JSONObject(response.readEntity(String.class));
        Assert.assertTrue(!StringUtils.isBlank(responseJo.optString("authToken")));
        Assert.assertEquals(Role.NORMAL.toString(), responseJo.optString("role"));
    }

    @Test
    public void loginAdminSuccess() {
        Client client = new JerseyClientBuilder().build();
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("admin@test.io");
        loginUserDto.setPassword("adminpw");
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().post(Entity.json(loginUserDto));
        Assert.assertEquals(200, response.getStatus());
        JSONObject responseJo = new JSONObject(response.readEntity(String.class));
        Assert.assertTrue(!StringUtils.isBlank(responseJo.optString("authToken")));
        Assert.assertEquals(Role.ADMIN.toString(), responseJo.optString("role"));
    }

    @Test
    public void loginFailureWrongPasswordSuccess() {
        Client client = new JerseyClientBuilder().build();
        // signup user
        SignupUserDto signupUserDto = new SignupUserDto();
        signupUserDto.setEmail("wrong.pw@gmail.com");
        signupUserDto.setPassword("test123");
        client.target(String.format("http://localhost:%d/signup", RULE.getLocalPort())).request()
                .post(Entity.json(signupUserDto));
        // login user
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("wrong.pw@gmail.com");
        loginUserDto.setPassword("wrong");
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().post(Entity.json(loginUserDto));
        ResponseValidator.validate(response, 403, ErrMsg.LOGIN_FAIL_USER_NOT_FOUND_OR_WRONG_PASSWORD.toString());
    }
}
