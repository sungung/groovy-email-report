package com.sungung.adapter;

import com.sungung.env.AppConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author PARK Sungung
 * @Since 0.0.1
 */
public abstract class GroovyAdapter implements IGroovyAdapter {

    private static final Logger log = Logger.getLogger(GroovyAdapter.class.getName());

    private static final String TEMP_REPORT_PREFIX = "REPORT";

    public static final String PARAM_EMAIL_RECIPIENTS = "RECIPIENTS";
    public static final String PARAM_REPORT_TITLE = "REPORT";

    protected JavaMailSender mailSender;
    private DataSource dataSource;
    private File reportFile;
    private AppConfiguration appConfiguration;

    public void log(String message) {
        logWarn(message);
    }

    public void logWarn(String message) {
        String thisClassName = "[" + getClass().getName() +"] ";
        log.log(Level.WARNING, thisClassName + message);
    }

    public File getReportFile() throws IOException {
        if (reportFile == null) {
            this.reportFile = File.createTempFile(TEMP_REPORT_PREFIX,"");
        }
        return reportFile;
    }

    public void setReportFile(File reportFile) {
        this.reportFile = reportFile;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public AppConfiguration getAppConfiguration() {
        return appConfiguration;
    }

    public void setAppConfiguration(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    public void fetchEmailReport(String reportName, Map<String, String> params) {

        String recipients = params.get(PARAM_EMAIL_RECIPIENTS);
        if (StringUtils.isEmpty(recipients)) {
            log.warning(String.format("Email recipients for %s are not defined. %s key value in query parameter to be defined.", reportName, PARAM_EMAIL_RECIPIENTS));
            throw new GroovyReportException(String.format("Email recipients for %s are not defined. %s key value in query parameter to be defined.", reportName, PARAM_EMAIL_RECIPIENTS));
        }

        try {
            if (!getReportFile().exists() || getReportFile().length() == 0) {
                log.warning(String.format("%s report is empty.", reportName));
                throw new GroovyReportException(String.format("%s report is empty.", reportName));
            }
        } catch (IOException e) {
            log.warning(String.format("%s report is empty.", reportName));
            throw new GroovyReportException(String.format("%s report is empty.", reportName));
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Thank you for requesting email report. Your report is generated with below filters");
        sb.append('\n');
        sb.append('\n');

        for (Map.Entry entry : params.entrySet()) {
            sb.append(String.format("%15.15s %s %s",entry.getKey(),":",entry.getValue()));
            sb.append('\n');
        }

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(recipients);
            helper.setFrom(appConfiguration.getMail().getSender());

            String title = params.get(PARAM_REPORT_TITLE)==null?"Email Report":params.get(PARAM_REPORT_TITLE);
            helper.setSubject(String.format("Your %s is ready.", title));
            helper.setText(sb.toString());
            helper.addAttachment(reportName.concat(".csv"), getReportFile());
            mailSender.send(message);
        } catch (MessagingException e) {
            log.log(Level.SEVERE, "Failed to create email report.", e);
            throw new GroovyReportException("Failed to create email report.", e);
        } catch (MailSendException e) {
            log.log(Level.SEVERE, "Failed to send email report.", e);
            throw new GroovyReportException("Failed to send email report.", e);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to email report.", e);
            throw new GroovyReportException("Failed to send email report.", e);
        }
    }
}
