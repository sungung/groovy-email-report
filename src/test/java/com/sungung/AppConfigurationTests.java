package com.sungung;

import com.sungung.env.AppConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author PARK Sungung
 * @since 0.0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("development")
@SpringApplicationConfiguration(classes = Application.class)
public class AppConfigurationTests {

    @Autowired
    AppConfiguration appConfiguration;

    @Test
    public void envEqualToProfile() {
        assertEquals("development", appConfiguration.getApp().getEnv());
    }

    @Test
    public void emailSenderIdNotNull() {
        assertNotNull(appConfiguration.getMail().getSender());
    }

}
