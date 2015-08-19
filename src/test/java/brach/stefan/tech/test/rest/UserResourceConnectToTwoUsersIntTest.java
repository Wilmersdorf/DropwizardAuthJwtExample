package brach.stefan.tech.test.rest;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

import org.junit.ClassRule;
import org.junit.Test;

import brach.stefan.tech.test.TechTestApplication;
import brach.stefan.tech.test.TechTestConfiguration;
import brach.stefan.tech.test.rest.helper.IntTestConnectHelper;
import brach.stefan.tech.test.rest.helper.IntTestLoginHelper;
import brach.stefan.tech.test.rest.helper.IntTestSignupHelper;
import brach.stefan.tech.test.test.validator.UserConnectionValidator;

public class UserResourceConnectToTwoUsersIntTest {
    private static final String url = "http://localhost:%d/user";
    @ClassRule
    public static final DropwizardAppRule<TechTestConfiguration> RULE = new DropwizardAppRule<TechTestConfiguration>(
            TechTestApplication.class, ResourceHelpers.resourceFilePath("tech-test-stefan-integration_mariadb.yml"));

    @Test
    public void testConnectToTwoUsers() {
        IntTestSignupHelper.signupUser("one@test.io", "testOne", RULE);
        String authToken = IntTestLoginHelper.getAuthToken("one@test.io", "testOne", RULE);
        UserConnectionValidator.validate(authToken, "[]", RULE);
        IntTestSignupHelper.signupUser("two@test.io", "testTwo", RULE);
        UserConnectionValidator.validate(authToken, "[{\"email\":\"two@test.io\",\"connected\":false}]", RULE);
        IntTestSignupHelper.signupUser("three@test.io", "testThree", RULE);
        // false false
        UserConnectionValidator.validate(authToken,
                "[{\"email\":\"two@test.io\",\"connected\":false},{\"email\":\"three@test.io\",\"connected\":false}]", RULE);
        // true false
        IntTestConnectHelper.connect(authToken, "two@test.io", true, RULE);
        UserConnectionValidator.validate(authToken,
                "[{\"email\":\"two@test.io\",\"connected\":true},{\"email\":\"three@test.io\",\"connected\":false}]", RULE);
        // true true
        IntTestConnectHelper.connect(authToken, "three@test.io", true, RULE);
        UserConnectionValidator.validate(authToken,
                "[{\"email\":\"two@test.io\",\"connected\":true},{\"email\":\"three@test.io\",\"connected\":true}]", RULE);
        // true true to check if posting same connection status works
        IntTestConnectHelper.connect(authToken, "two@test.io", true, RULE);
        IntTestConnectHelper.connect(authToken, "three@test.io", true, RULE);
        UserConnectionValidator.validate(authToken,
                "[{\"email\":\"two@test.io\",\"connected\":true},{\"email\":\"three@test.io\",\"connected\":true}]", RULE);
        // false true
        IntTestConnectHelper.connect(authToken, "two@test.io", false, RULE);
        UserConnectionValidator.validate(authToken,
                "[{\"email\":\"two@test.io\",\"connected\":false},{\"email\":\"three@test.io\",\"connected\":true}]", RULE);
        // false false
        IntTestConnectHelper.connect(authToken, "three@test.io", false, RULE);
        UserConnectionValidator.validate(authToken,
                "[{\"email\":\"two@test.io\",\"connected\":false},{\"email\":\"three@test.io\",\"connected\":false}]", RULE);
        // false false to check if posting same connection status works
        IntTestConnectHelper.connect(authToken, "two@test.io", false, RULE);
        IntTestConnectHelper.connect(authToken, "three@test.io", false, RULE);
        UserConnectionValidator.validate(authToken,
                "[{\"email\":\"two@test.io\",\"connected\":false},{\"email\":\"three@test.io\",\"connected\":false}]", RULE); //
    }
}
