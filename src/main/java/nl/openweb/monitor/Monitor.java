package nl.openweb.monitor;

import java.io.IOException;
import java.io.InputStream;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.ws.rs.core.Response;

import nl.openweb.monitor.Config.Page;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tdclighthouse.commons.mail.util.Mail;
import com.tdclighthouse.commons.mail.util.MailClient;

public class Monitor implements Runnable {
    private static Logger LOG = LoggerFactory.getLogger(Monitor.class);

    private final WebClient webClient;
    private final String expectedResponseType;
    private final MailClient mailClient;
    private final Config config;
    private boolean crashReported = false;

    public Monitor(Config config, Page page, MailClient mailClient) {
        this.webClient = WebClient.create(page.getUrl());
        if (StringUtils.isNotBlank(page.getRequestContentType())) {
            webClient.accept(page.getRequestContentType());
        }
        this.expectedResponseType = page.getResponseContentType();
        this.mailClient = mailClient;
        this.config = config;
    }

    @Override
    public void run() {
        try {
            Response response = webClient.get();
            if (!checkResponseStatus(response) || !checkResponseContentType(response) || !checkContent(response)) {
                sendCrashedEmail();
            } else {
                if (crashReported) {
                    sendRecoveryEmail();
                }
            }
        } catch (MessagingException e) {
            LOG.debug(e.getMessage(), e);
        }
    }

    private void sendRecoveryEmail() throws MessagingException, AddressException {
        Mail mail = new Mail(config.getRecoveryEmailsubject(), config.getRecoveryEmailsubject());
        mailClient.sendMail(config.getFromAddress(), config.getToAddress(), mail);
        crashReported = false;
    }

    private void sendCrashedEmail() throws MessagingException, AddressException {
        Mail mail = new Mail(config.getSubject(), config.getEmailBody());
        if (!crashReported) {
            mailClient.sendMail(config.getFromAddress(), config.getToAddress(), mail);
            crashReported = true;
        }
    }

    private boolean checkContent(Response response) {
        boolean result;
        try {
            String body = IOUtils.toString((InputStream) response.getEntity());
            result = body.length() > 0;
        } catch (IOException e) {
            result = false;
        }
        return result;
    }

    private boolean checkResponseStatus(Response response) {
        return Response.Status.OK.getStatusCode() == response.getStatus();
    }

    private boolean checkResponseContentType(Response response) {
        return StringUtils.isBlank(expectedResponseType) || expectedResponseType.equals(getContentType(response));
    }

    private String getContentType(Response response) {
        String contentType = null;
        String contentTypeHeader = response.getHeaderString("Content-Type");
        if (StringUtils.isNotBlank(contentTypeHeader)) {
            contentType = contentTypeHeader.split("\\s*;")[0];

        }
        return contentType;
    }

}
