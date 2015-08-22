package brach.stefan.dae.rest.resource.signup;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.Assert;
import org.junit.Test;

import brach.stefan.dae.rest.model.error.ErrMsg;
import brach.stefan.dae.rest.model.send.SignupUserDto;
import brach.stefan.dae.test.IntegrationTest;
import brach.stefan.dae.test.validator.ResponseValidator;

public class SignupResourceIntTest extends IntegrationTest {
    private static final String url = "http://localhost:%d/signup";

    @Test
    public void noArgument() {
        Client client = new JerseyClientBuilder().build();
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().post(null);
        ResponseValidator.validate(response, 400, ErrMsg.SIGNUP_FAIL_NO_CREDENTIALS.toString());
    }

    @Test
    public void noEmail() {
        Client client = new JerseyClientBuilder().build();
        SignupUserDto signupUserDto = new SignupUserDto();
        signupUserDto.setPassword("test123");
        Response response = client.target(String.format(url, RULE.getLocalPort())).request()
                .post(Entity.json(new SignupUserDto()));
        ResponseValidator.validate(response, 400, ErrMsg.SIGNUP_FAIL_NO_EMAIL.toString());
    }

    @Test
    public void invalidEmail() {
        Client client = new JerseyClientBuilder().build();
        SignupUserDto signupUserDto = new SignupUserDto();
        signupUserDto.setEmail("invalidEmail");
        signupUserDto.setPassword("test123");
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().post(Entity.json(signupUserDto));
        ResponseValidator.validate(response, 400, ErrMsg.SIGNUP_FAIL_INVALID_EMAIL.toString());
    }

    @Test
    public void noPassword() {
        Client client = new JerseyClientBuilder().build();
        SignupUserDto signupUserDto = new SignupUserDto();
        signupUserDto.setEmail("test@gmail.com");
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().post(Entity.json(signupUserDto));
        ResponseValidator.validate(response, 400, ErrMsg.SIGNUP_FAIL_NO_PASSWORD.toString());
    }

    @Test
    public void invalidPasswordToShort() {
        Client client = new JerseyClientBuilder().build();
        SignupUserDto signupUserDto = new SignupUserDto();
        signupUserDto.setEmail("test@gmail.com");
        signupUserDto.setPassword("a");
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().post(Entity.json(signupUserDto));
        ResponseValidator.validate(response, 400, "Password must be at least 2 characters in length.");
    }

    @Test
    public void invalidPasswordContainsWhiteSpace() {
        Client client = new JerseyClientBuilder().build();
        SignupUserDto signupUserDto = new SignupUserDto();
        signupUserDto.setEmail("test@gmail.com");
        signupUserDto.setPassword("abcd abcd");
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().post(Entity.json(signupUserDto));
        ResponseValidator.validate(response, 400, "Password cannot contain whitespace characters.");
    }

    @Test
    public void invalidPasswordNoAlphabeticalCharacters() {
        Client client = new JerseyClientBuilder().build();
        SignupUserDto signupUserDto = new SignupUserDto();
        signupUserDto.setEmail("test@gmail.com");
        signupUserDto.setPassword("123");
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().post(Entity.json(signupUserDto));
        ResponseValidator.validate(response, 400, "Password must contain at least 1 alphabetical characters.");
    }

    @Test
    public void signupSuccess() {
        Client client = new JerseyClientBuilder().build();
        SignupUserDto signupUserDto = new SignupUserDto();
        signupUserDto.setEmail("success@gmail.com");
        signupUserDto.setPassword("test123");
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().post(Entity.json(signupUserDto));
        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    public void signupUserAlreadyExist() {
        Client client = new JerseyClientBuilder().build();
        SignupUserDto signupUserDto = new SignupUserDto();
        signupUserDto.setEmail("duplicate@gmail.com");
        signupUserDto.setPassword("test123");
        client.target(String.format(url, RULE.getLocalPort())).request().post(Entity.json(signupUserDto));
        Response response = client.target(String.format(url, RULE.getLocalPort())).request().post(Entity.json(signupUserDto));
        ResponseValidator.validate(response, 409, ErrMsg.SIGNUP_FAIL_USER_ALREADY_EXISTS.toString());
    }
}
