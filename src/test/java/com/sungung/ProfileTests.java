package com.sungung;

import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.StandardEnvironment;

import static org.junit.Assert.assertEquals;


/**
 * @author PARK Sungung
 * @since 0.0.1
 */
public class ProfileTests {

    private final StandardEnvironment environment = new StandardEnvironment();

    private final ApplicationEnvironmentPreparedEvent event = new ApplicationEnvironmentPreparedEvent(
            new SpringApplication(), new String[0], this.environment);

    private final ConfigFileApplicationListener initializer = new ConfigFileApplicationListener();

    @Test
    public void testDefaultProfile() throws Exception {
        SpringApplication application = new SpringApplication(Application.class);
        application.setWebEnvironment(false);
        ConfigurableApplicationContext context = application.run();
        String property = context.getEnvironment().getProperty("ger.app.env");
        assertEquals("default", property);
    }

    @Test
    public void testDevelopmentProfile() throws Exception {
        SpringApplication application = new SpringApplication(Application.class);
        application.setAdditionalProfiles("development");
        application.setWebEnvironment(false);
        ConfigurableApplicationContext context = application.run();
        String property = context.getEnvironment().getProperty("ger.app.env");
        assertEquals("development", property);
    }

    @Test
    public void testProductionProfile() throws Exception {
        SpringApplication application = new SpringApplication(Application.class);
        application.setAdditionalProfiles("production");
        application.setWebEnvironment(false);
        ConfigurableApplicationContext context = application.run();
        String property = context.getEnvironment().getProperty("ger.app.env");
        assertEquals("production", property);
    }

}
