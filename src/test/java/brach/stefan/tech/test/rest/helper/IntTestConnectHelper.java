package brach.stefan.tech.test.rest.helper;

import io.dropwizard.testing.junit.DropwizardAppRule;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.Assert;

import brach.stefan.tech.test.TechTestConfiguration;
import brach.stefan.tech.test.rest.model.send.ConnectToUserDto;

public class IntTestConnectHelper {
    public static void connect(String authToken, String email, boolean connected, DropwizardAppRule<TechTestConfiguration> RULE) {
        ConnectToUserDto connectToUserDto = new ConnectToUserDto();
        connectToUserDto.setConnected(connected);
        connectToUserDto.setEmail(email);
        Client client = new JerseyClientBuilder().build();
        Response response = client.target(String.format("http://localhost:%d/user/connection", RULE.getLocalPort())).request()
                .header("authToken", authToken).post(Entity.json(connectToUserDto));
        Assert.assertEquals(200, response.getStatus());
    }
}
