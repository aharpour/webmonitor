package nl.openweb.monitor;

import java.io.FileInputStream;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class ConfigFactory implements FactoryBean<Config>, InitializingBean {

    private ObjectMapper objectMapper;
    private Config config;

    @Override
    public void afterPropertiesSet() throws Exception {
        String propertyFilePath = System.getProperties().getProperty("monitor.config");
        if (StringUtils.isBlank(propertyFilePath)) {
            throw new IllegalArgumentException("System variable \"monitor.config\" is required.");
        }
        config = objectMapper.readValue(new FileInputStream(propertyFilePath), Config.class);
    }

    @Override
    public Config getObject() {
        return config;
    }

    @Override
    public Class<Config> getObjectType() {
        return Config.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

}
