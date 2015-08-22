package brach.stefan.dae.test;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

import org.junit.ClassRule;

import brach.stefan.dae.DaeApplication;
import brach.stefan.dae.DaeConfiguration;

public class IntegrationTest {
    @ClassRule
    public static final DropwizardAppRule<DaeConfiguration> RULE = new DropwizardAppRule<DaeConfiguration>(
            DaeApplication.class, ResourceHelpers.resourceFilePath("dae-test-integration.yml"));
}
