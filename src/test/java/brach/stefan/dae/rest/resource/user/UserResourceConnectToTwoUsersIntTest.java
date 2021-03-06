package brach.stefan.dae.rest.resource.user;

import org.junit.Test;

import brach.stefan.dae.test.IntegrationTest;
import brach.stefan.dae.test.helper.IntTestConnectHelper;
import brach.stefan.dae.test.helper.IntTestGetConnectionsHelper;
import brach.stefan.dae.test.helper.IntTestLoginHelper;
import brach.stefan.dae.test.helper.IntTestSignupHelper;

public class UserResourceConnectToTwoUsersIntTest extends IntegrationTest {
    @Test
    public void testConnectToTwoUsers() {
        IntTestSignupHelper.signupUser("one@test.io", "testOne", RULE);
        String authToken = IntTestLoginHelper.getAuthToken("one@test.io", "testOne", RULE);
        IntTestGetConnectionsHelper.validate(authToken, "[]", RULE);
        IntTestSignupHelper.signupUser("two@test.io", "testTwo", RULE);
        IntTestGetConnectionsHelper.validate(authToken, "[{\"email\":\"two@test.io\",\"connected\":false}]", RULE);
        IntTestSignupHelper.signupUser("three@test.io", "testThree", RULE);
        // false false
        IntTestGetConnectionsHelper.validate(authToken,
                "[{\"email\":\"two@test.io\",\"connected\":false},{\"email\":\"three@test.io\",\"connected\":false}]", RULE);
        // true false
        IntTestConnectHelper.connect(authToken, "two@test.io", true, RULE);
        IntTestGetConnectionsHelper.validate(authToken,
                "[{\"email\":\"two@test.io\",\"connected\":true},{\"email\":\"three@test.io\",\"connected\":false}]", RULE);
        // true true
        IntTestConnectHelper.connect(authToken, "three@test.io", true, RULE);
        IntTestGetConnectionsHelper.validate(authToken,
                "[{\"email\":\"two@test.io\",\"connected\":true},{\"email\":\"three@test.io\",\"connected\":true}]", RULE);
        // true true to check if posting same connection status works
        IntTestConnectHelper.connect(authToken, "two@test.io", true, RULE);
        IntTestConnectHelper.connect(authToken, "three@test.io", true, RULE);
        IntTestGetConnectionsHelper.validate(authToken,
                "[{\"email\":\"two@test.io\",\"connected\":true},{\"email\":\"three@test.io\",\"connected\":true}]", RULE);
        // false true
        IntTestConnectHelper.connect(authToken, "two@test.io", false, RULE);
        IntTestGetConnectionsHelper.validate(authToken,
                "[{\"email\":\"two@test.io\",\"connected\":false},{\"email\":\"three@test.io\",\"connected\":true}]", RULE);
        // false false
        IntTestConnectHelper.connect(authToken, "three@test.io", false, RULE);
        IntTestGetConnectionsHelper.validate(authToken,
                "[{\"email\":\"two@test.io\",\"connected\":false},{\"email\":\"three@test.io\",\"connected\":false}]", RULE);
        // false false to check if posting same connection status works
        IntTestConnectHelper.connect(authToken, "two@test.io", false, RULE);
        IntTestConnectHelper.connect(authToken, "three@test.io", false, RULE);
        IntTestGetConnectionsHelper.validate(authToken,
                "[{\"email\":\"two@test.io\",\"connected\":false},{\"email\":\"three@test.io\",\"connected\":false}]", RULE); //
    }
}
