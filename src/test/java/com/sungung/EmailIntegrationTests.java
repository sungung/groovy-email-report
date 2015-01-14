package com.sungung;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.UnknownHostException;

/**
 * @author PARK Sungung
 * @since 0.0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class EmailIntegrationTests {

    @Autowired
    JavaMailSender javaMailSender;

    /**
     * To pass the test, you need to turn off SMTP server
     * @throws UnknownHostException
     */
    @Test(expected = UnknownHostException.class)
    public void emailTest() throws UnknownHostException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("me@sungung.com");
        message.setTo("you@sungung.com");
        message.setText("Hello world");
        javaMailSender.send(message);
    }

}
