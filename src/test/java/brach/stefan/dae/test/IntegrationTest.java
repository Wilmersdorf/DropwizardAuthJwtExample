package brach.stefan.dae.test;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

import org.junit.ClassRule;

import brach.stefan.dae.TechTestApplication;
import brach.stefan.dae.TechTestConfiguration;

public class IntegrationTest {
    @ClassRule
    public static final DropwizardAppRule<TechTestConfiguration> RULE = new DropwizardAppRule<TechTestConfiguration>(
            TechTestApplication.class, ResourceHelpers.resourceFilePath("dae-test-integration.yml"));
}
