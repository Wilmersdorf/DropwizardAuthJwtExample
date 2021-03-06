package brach.stefan.dae.test.helper;

import io.dropwizard.testing.junit.DropwizardAppRule;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.Assert;

import brach.stefan.dae.DaeConfiguration;
import brach.stefan.dae.rest.model.send.SignupUserDto;

public class IntTestSignupHelper {
    public static void signupUser(String email, String password, DropwizardAppRule<DaeConfiguration> RULE) {
        Client client = new JerseyClientBuilder().build();
        SignupUserDto signupUserDto = new SignupUserDto();
        signupUserDto.setEmail(email);
        signupUserDto.setPassword(password);
        Response response = client.target(String.format("http://localhost:%d/signup", RULE.getLocalPort())).request()
                .post(Entity.json(signupUserDto));
        Assert.assertEquals(200, response.getStatus());
    }
}
