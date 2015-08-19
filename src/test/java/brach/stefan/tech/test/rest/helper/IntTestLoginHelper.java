package brach.stefan.tech.test.rest.helper;

import io.dropwizard.testing.junit.DropwizardAppRule;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.json.JSONObject;
import org.junit.Assert;

import brach.stefan.tech.test.TechTestConfiguration;
import brach.stefan.tech.test.rest.model.send.LoginUserDto;

public class IntTestLoginHelper {
    public static String getAuthToken(String email, String password, DropwizardAppRule<TechTestConfiguration> RULE) {
        Client client = new JerseyClientBuilder().build();
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setEmail(email);
        loginUserDto.setPassword(password);
        Response loginResponse = client.target(String.format("http://localhost:%d/login", RULE.getLocalPort())).request()
                .post(Entity.json(loginUserDto));
        JSONObject loginResponseJo = new JSONObject(loginResponse.readEntity(String.class));
        String authToken = loginResponseJo.optString("authToken");
        Assert.assertTrue(!StringUtils.isBlank(authToken));
        return authToken;
    }
}
