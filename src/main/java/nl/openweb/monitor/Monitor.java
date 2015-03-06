package nl.openweb.monitor;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.text.MessageFormat;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.ws.rs.core.Response;

import nl.openweb.monitor.Config.Page;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tdclighthouse.commons.mail.util.Mail;
import com.tdclighthouse.commons.mail.util.MailClient;

public class Monitor implements Runnable {
    private static final Logger LOG = LogManager.getLogger(Monitor.class);
    private final WebClient webClient;
    private final String expectedResponseType;
    private final MailClient mailClient;
    private final Config config;
    private final Page page;
    private boolean crashReported = false;

    public Monitor(Config config, Page page, MailClient mailClient) {
        this.webClient = createWebClient(config, page);
        if (StringUtils.isNotBlank(page.getRequestContentType())) {
            webClient.accept(page.getRequestContentType());
        }
        this.expectedResponseType = page.getResponseContentType();
        this.mailClient = mailClient;
        this.config = config;
        this.page = page;
    }

    private WebClient createWebClient(Config config, Page page) {
        WebClient client = WebClient.create(page.getUrl());
        ClientConfiguration conf = WebClient.getConfig(client);
        HTTPConduit conduit = (HTTPConduit) conf.getConduit();
        conduit.getClient().setReceiveTimeout(config.getReadTimeout());
        conduit.getClient().setConnectionTimeout(config.getConnectTimeout());
        return client;
    }

    @Override
    public void run() {
        try {
            Response response = call();
            if (response == null || !checkResponseStatus(response) || !checkResponseContentType(response)
                    || !checkContent(response)) {
                sendCrashedEmail(response);
            } else {
                if (crashReported) {
                    sendRecoveryEmail();
                }
            }
        } catch (MessagingException e) {
            LOG.debug(e.getMessage(), e);
        }
    }

    private Response call() {
        Response response = null;
        try {
            LOG.debug("Trying to send a request to {}", page.getUrl());
            response = webClient.get();
            logResponse(response);
        } catch (Exception e) {
            if (e.getCause() instanceof SocketTimeoutException) {
                LOG.debug("Request to {} timed out.", page.getUrl());
            } else {
                LOG.error(e.getMessage(), e);
            }
        }
        return response;
    }

    private void logResponse(Response response) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("get request to {} resulted in a {}", page.getUrl(), response.getStatus());
            LOG.debug("get request to {} response type check result: {}", page.getUrl(),
                    checkResponseContentType(response));
            LOG.debug("get request to {} response body check result: {}", page.getUrl(), checkContent(response));
        }
    }

    private void sendRecoveryEmail() throws MessagingException, AddressException {
        Mail mail = new Mail(config.getRecoveryEmailsubject(), config.getRecoveryEmailsubject());
        mailClient.sendMail(config.getFromAddress(), config.getToAddress(), mail);
        crashReported = false;
    }

    private void sendCrashedEmail(Response response) throws MessagingException, AddressException {
        String messageBody;
        if (response != null) {
            messageBody = MessageFormat.format(config.getEmailBody(), false, response.getStatus(),
                    checkResponseContentType(response), checkContent(response));
        } else {
            messageBody = MessageFormat.format(config.getEmailBody(), true, null, null, null);
        }
        Mail mail = new Mail(config.getSubject(), messageBody);
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
