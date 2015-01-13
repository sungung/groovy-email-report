package com.sungung;

import com.sungung.adapter.GroovyAdapter;
import com.sungung.adapter.GroovyReportException;
import com.sungung.env.AppConfiguration;
import groovy.lang.GroovyClassLoader;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author PARK Sungung
 * @since 0.0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class GroovyRunTests {

    private final static URL scriptPath = GroovyRunTests.class.getClass().getResource("/groovy/BrewerReport.groovy");
    private final static GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
    private static Class clazz = null;

    static {
        try {
            clazz = groovyClassLoader.parseClass(new File(scriptPath.getPath()));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Autowired
    DataSource dataSource;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    AppConfiguration appConfiguration;

    @Test
    public void existGroovySampleTest() {
        assertNotNull(scriptPath);
    }

    @Test(expected = GroovyReportException.class)
    public void groovyReportInfraTests() throws IllegalAccessException, InstantiationException, IOException {

        GroovyAdapter groovyAdapter = (GroovyAdapter) clazz.newInstance();

        Map<String, String> params = new HashMap<String, String>();
        params.put("k0","v0");
        params.put("k1","v1");
        params.put("k2","v2");

        groovyAdapter.execute(params);

    }

    @Test
    public void groovyDatabaseReportTest() throws IOException, IllegalAccessException, InstantiationException {

        GroovyAdapter groovyAdapter = (GroovyAdapter) clazz.newInstance();
        groovyAdapter.setDataSource(dataSource);
        groovyAdapter.execute(new HashMap<String, String>());
        File report = groovyAdapter.getReportFile();
        String contents = IOUtils.toString(new FileInputStream(report));

        assertTrue(contents.contains("Brewer,Contact,Address,Suburb,Postcode,Phone,Email,Website"));

    }

    @Test(expected = GroovyReportException.class)
    public void missingEmailRecipientTest() throws IOException, IllegalAccessException, InstantiationException {

        GroovyAdapter groovyAdapter = (GroovyAdapter) clazz.newInstance();
        groovyAdapter.setAppConfiguration(appConfiguration);
        groovyAdapter.setDataSource(dataSource);
        groovyAdapter.setReportFile(new File("BrewerReport.csv"));
        groovyAdapter.setMailSender(javaMailSender);
        groovyAdapter.execute(new HashMap<String, String>());
        groovyAdapter.fetchEmailReport("Victoria Micro Brewer Contact", new HashMap<String, String>());

    }

    @Test
    public void groovyEmailReportTest() throws IllegalAccessException, InstantiationException {

        GroovyAdapter groovyAdapter = (GroovyAdapter) clazz.newInstance();
        groovyAdapter.setAppConfiguration(appConfiguration);
        groovyAdapter.setDataSource(dataSource);
        groovyAdapter.setReportFile(new File("BrewerReport.csv"));
        groovyAdapter.setMailSender(javaMailSender);
        groovyAdapter.execute(new HashMap<String, String>());

        Map<String, String> params = new HashMap<String, String>();
        params.put(GroovyAdapter.PARAM_REPORT_TITLE, "Victoria Micro Brewer Contact");
        params.put(GroovyAdapter.PARAM_EMAIL_RECIPIENTS, "test@sungung.com");
        groovyAdapter.fetchEmailReport("brewer_list", params);

    }

}
