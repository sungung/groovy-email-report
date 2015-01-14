package com.sungung.controller;

import com.sungung.adapter.*;
import com.sungung.env.AppConfiguration;
import com.sungung.service.ReportSupportService;
import groovy.lang.GroovyClassLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * REST API for email reporting.
 * @author PARK Sungung
 * @Since 0.0.1
 */
@RestController
public class ReportService {

    private static final Logger log = Logger.getLogger(ReportService.class.getName());

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    DataSource dataSource;

    @Autowired
    AppConfiguration appConfiguration;

    @Autowired
    ReportSupportService reportSupportService;

    /**
     * Client will wait util process completed. Endpoint looks like below
     * curl http://localhost:9000/report/BrewerReport?RECIPIENTS=user@helloworld.com
     * @param groovy        the name groovy script
     * @param queryParams   report filters
     */
    @RequestMapping(value = "/report/{groovy}")
    @ResponseStatus(HttpStatus.OK)
    public void runGroovyReport(@PathVariable String groovy, @RequestParam Map<String, String> queryParams) {
        GroovyAdapter groovyAdapter = newGroovyInstance(groovy);
        groovyAdapter.execute(queryParams);
        groovyAdapter.fetchEmailReport(
                queryParams.get(GroovyAdapter.PARAM_REPORT_TITLE)==null ? groovy : queryParams.get(GroovyAdapter.PARAM_REPORT_TITLE),
                queryParams
        );
    }

    /**
     * Overload default method, adding report recipient into path variable
     * curl http://localhost:9000/report/BrewerReport/user@helloworld.com
     * @param groovy        the name groovy script
     * @param recipients    the email address of report recipients
     * @param queryParams   report filters
     */
    @RequestMapping(value = "/report/{groovy}/{recipients}")
    @ResponseStatus(HttpStatus.OK)
    public void runGroovyReport(@PathVariable String groovy, @PathVariable String recipients, @RequestParam Map<String, String> queryParams) {
        queryParams.put("RECIPIENTS",recipients);
        runGroovyReport(groovy, queryParams);
    }


    /**
     * Invoke new thread then quit immediately
     * @param groovy        the name groovy script
     * @param queryParams   report filters
     */
    @RequestMapping(value = "/job/{groovy}")
    @ResponseStatus(HttpStatus.OK)
    public void runGroovyReportJob(@PathVariable String groovy, @RequestParam Map<String, String> queryParams) {
        GroovyAdapter groovyAdapter = newGroovyInstance(groovy);
        try {
            reportSupportService.executeGroovy(groovyAdapter, queryParams);
        } catch (InterruptedException e) {
            log.log(Level.SEVERE, String.format("%s is failed.", queryParams.get(GroovyAdapter.PARAM_REPORT_TITLE)), e);
        }
    }

    @RequestMapping(value = "job/{groovy}/{recipients}")
    @ResponseStatus(HttpStatus.OK)
    public void runGroovyReportJob(@PathVariable String groovy, @PathVariable String recipients, @RequestParam Map<String, String> queryParams) {
        queryParams.put("RECIPIENTS",recipients);
        runGroovyReportJob(groovy, queryParams);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Object handleGroovyNotFoundException(GroovyNotFoundException e) {
        StatusMessage message = new StatusMessage();
        message.setCode("ERROR");
        message.setMessage(e.getMessage());
        return message;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Object handleGroovyLoadException(GroovyLoadException e) {
        StatusMessage message = new StatusMessage();
        message.setCode("ERROR");
        message.setMessage(e.getMessage());
        return message;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Object handleGroovyReportException(GroovyReportException e) {
        StatusMessage message = new StatusMessage();
        message.setCode("ERROR");
        message.setMessage(e.getMessage());
        return message;
    }

    private GroovyAdapter newGroovyInstance(String script) {

        String groovyScript = script.concat(".groovy");
        URL scriptPath = this.getClass().getResource("/groovy/".concat(groovyScript));

        GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
        Class clazz;
        GroovyAdapter groovyAdapter;

        try {
            clazz = groovyClassLoader.parseClass(new File(scriptPath.getPath()));
        } catch (IOException e) {
            log.log(Level.SEVERE,String.format("Groovy script %s is not found.", groovyScript), e);
            throw new GroovyLoadException(String.format("Groovy script %s is not found.", groovyScript), e);
        }

        try {
            groovyAdapter = (GroovyAdapter) clazz.newInstance();
        } catch (InstantiationException e) {
            log.log(Level.SEVERE,String.format("Failed to create instance of %s", groovyScript), e);
            throw new GroovyLoadException(String.format("Failed to create instance of %s", groovyScript), e);
        } catch (IllegalAccessException e) {
            log.log(Level.SEVERE,String.format("Failed to create instance of %s", groovyScript), e);
            throw new GroovyLoadException(String.format("Failed to create instance of %s", groovyScript), e);
        }

        try {
            groovyAdapter.setReportFile(File.createTempFile("REPORT",".csv"));
        } catch (IOException e) {
            log.log(Level.SEVERE,String.format("Failed to add temporary file of %s", groovyScript), e);
            throw new GroovyLoadException(String.format("Failed to add temporary file of %s", groovyScript), e);
        }

        // Inject email report infrastructure
        groovyAdapter.setAppConfiguration(appConfiguration);
        groovyAdapter.setDataSource(dataSource);
        groovyAdapter.setMailSender(mailSender);

        return groovyAdapter;
    }
}
