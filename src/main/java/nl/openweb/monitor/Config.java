package nl.openweb.monitor;

import java.util.List;

public class Config {

    private int frequency;

    private String recoveryEmailsubject;
    private String recoveryEmailBody;
    private String emailBody;
    private String subject;
    private String fromAddress;
    private int connectTimeout;
    private int readTimeout;
    private String[] toAddress;
    private List<Page> pages;

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String[] getToAddress() {
        return toAddress;
    }

    public void setToAddress(String[] toAddress) {
        this.toAddress = toAddress;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRecoveryEmailsubject() {
        return recoveryEmailsubject;
    }

    public void setRecoveryEmailsubject(String recoveryEmailsubject) {
        this.recoveryEmailsubject = recoveryEmailsubject;
    }

    public String getRecoveryEmailBody() {
        return recoveryEmailBody;
    }

    public void setRecoveryEmailBody(String recoveryEmailBody) {
        this.recoveryEmailBody = recoveryEmailBody;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public static class Page {

        private String url;
        private String requestContentType;
        private String responseContentType;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getRequestContentType() {
            return requestContentType;
        }

        public void setRequestContentType(String requestContentType) {
            this.requestContentType = requestContentType;
        }

        public String getResponseContentType() {
            return responseContentType;
        }

        public void setResponseContentType(String responseContentType) {
            this.responseContentType = responseContentType;
        }

    }

}
