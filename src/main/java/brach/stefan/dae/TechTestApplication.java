package brach.stefan.dae;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.security.NoSuchAlgorithmException;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import brach.stefan.dae.auth.ExampleAuthenticator;
import brach.stefan.dae.auth.ExampleAuthorizer;
import brach.stefan.dae.auth.TokenAuthFilter;
import brach.stefan.dae.dao.UserConnectionDao;
import brach.stefan.dae.dao.UserDao;
import brach.stefan.dae.dao.impl.UserConnectionDaoImpl;
import brach.stefan.dae.dao.impl.UserDaoImpl;
import brach.stefan.dae.model.Keys;
import brach.stefan.dae.model.User;
import brach.stefan.dae.rest.resource.AdminUserResource;
import brach.stefan.dae.rest.resource.HelloWorldResource;
import brach.stefan.dae.rest.resource.LoginResource;
import brach.stefan.dae.rest.resource.SignupResource;
import brach.stefan.dae.rest.resource.UserResource;
import brach.stefan.dae.service.AdminUserService;
import brach.stefan.dae.service.LoginService;
import brach.stefan.dae.service.SignupService;
import brach.stefan.dae.service.UserService;
import brach.stefan.dae.service.auth.JwtBuilderService;
import brach.stefan.dae.service.auth.JwtReaderService;
import brach.stefan.dae.service.auth.impl.JwtBuilderServiceImpl;
import brach.stefan.dae.service.auth.impl.JwtReaderServiceImpl;
import brach.stefan.dae.service.auth.impl.KeyBuilderServiceImpl;
import brach.stefan.dae.service.impl.AdminUserServiceImpl;
import brach.stefan.dae.service.impl.LoginServiceImpl;
import brach.stefan.dae.service.impl.SignupServiceImpl;
import brach.stefan.dae.service.impl.UserServiceImpl;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class TechTestApplication extends Application<TechTestConfiguration> {
    private final static Logger LOG = LoggerFactory.getLogger(TechTestApplication.class);

    public static void main(String[] args) throws Exception {
        new TechTestApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<TechTestConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(TechTestConfiguration conf, Environment env) throws NoSuchAlgorithmException {
        // logging
        env.jersey().register(new LoggingFilter(java.util.logging.Logger.getLogger(LoggingFilter.class.getName()), true));
        // guice injector
        Injector injector = createInjector(conf, env);
        // resource registration
        registerResource(env, injector, HelloWorldResource.class);
        registerResource(env, injector, SignupResource.class);
        registerResource(env, injector, LoginResource.class);
        registerResource(env, injector, UserResource.class);
        registerResource(env, injector, AdminUserResource.class);
        // authentication
        registerAuthentication(env, injector);
        LOG.debug("application started");
    }

    private void registerResource(Environment env, Injector injector, Class<?> theClass) {
        env.jersey().register(injector.getInstance(theClass));
    }

    private void registerAuthentication(Environment env, Injector injector) {
        env.jersey().register(RolesAllowedDynamicFeature.class);
        final TokenAuthFilter<User> tokenAuthFilter = new TokenAuthFilter.Builder<User>()
                .setAuthorizer(injector.getInstance(ExampleAuthorizer.class))
                .setAuthenticator(injector.getInstance(ExampleAuthenticator.class)).buildAuthFilter();
        env.jersey().register(new AuthDynamicFeature(tokenAuthFilter));
        env.jersey().register(new AuthValueFactoryProvider.Binder<User>(User.class));
    }

    private Injector createInjector(TechTestConfiguration conf, Environment env) throws NoSuchAlgorithmException {
        Keys keys = new KeyBuilderServiceImpl().createKeys(conf);
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                // keys
                bind(Keys.class).toInstance(keys);
                // database
                final DBIFactory factory = new DBIFactory();
                final DBI jdbi = factory.build(env, conf.getDataSourceFactory(), "mariaDb");
                final UserDaoImpl userDaoImpl = jdbi.onDemand(UserDaoImpl.class);
                final UserConnectionDaoImpl userConnectionDaoImpl = jdbi.onDemand(UserConnectionDaoImpl.class);
                bind(UserDao.class).toInstance(userDaoImpl);
                bind(UserConnectionDao.class).toInstance(userConnectionDaoImpl);
                // services
                bind(JwtReaderService.class).to(JwtReaderServiceImpl.class).asEagerSingleton();
                bind(JwtBuilderService.class).to(JwtBuilderServiceImpl.class).asEagerSingleton();
                bind(LoginService.class).to(LoginServiceImpl.class).asEagerSingleton();
                bind(SignupService.class).to(SignupServiceImpl.class).asEagerSingleton();
                bind(UserService.class).to(UserServiceImpl.class).asEagerSingleton();
                bind(AdminUserService.class).to(AdminUserServiceImpl.class).asEagerSingleton();
            }
        });
        return injector;
    }
}