package io.vertx;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.vertx.core.logging.LoggerFactory;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by kurro on 1/17/17.
 */
public class BaseAppConfig {

    private static final io.vertx.core.logging.Logger logger = LoggerFactory.getLogger(BaseAppConfig.class);

    @JsonProperty("http.port")
    private Integer httpPort;

    public void validate() {
        Validate.notNull(httpPort, "http.port must be not null");
    }

    public static io.vertx.core.logging.Logger getLogger() {
        return logger;
    }

    public Integer getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(Integer httpPort) {
        this.httpPort = httpPort;
    }
    public static <T extends BaseAppConfig> T newInstance(Class<T> appConfigClass) throws IOException {
        return newInstance("conf/config.yml", appConfigClass);
    }

    public static <T extends BaseAppConfig> T newInstance(String appConfigPath, Class<T> appConfigClass) throws IOException {
        if (new File(appConfigPath).isAbsolute()) {
            return newInstance(new File(appConfigPath), appConfigClass);
        }
        return newInstance(new File(System.getProperty("app.home"), appConfigPath), appConfigClass);
    }

    public static <T extends BaseAppConfig> T newInstance(File appConfigFile, Class<T> appConfigClass) throws IOException {
        Validate.isTrue(appConfigFile.exists(), "AppConfigFile not exists: " + appConfigFile.getAbsolutePath());
        String extension = FilenameUtils.getExtension(appConfigFile.getPath());
        ObjectMapper objectMapper = extension.equalsIgnoreCase("yaml") || extension.equalsIgnoreCase("yml") ? new ObjectMapper(new YAMLFactory()) : new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        T appConfig = objectMapper.readValue(appConfigFile, appConfigClass);
        String httpPort = System.getProperty("http.port", Integer.toString(appConfig.getHttpPort())); //get từ system, nếu không có thì lấy default  từ app.config.getHttpPort
        appConfig.setHttpPort(Integer.valueOf(httpPort)); //có rồi còn set làm cc gì? tl: port được ưu tiên lấy ở system property trước, nếu không có mới lấy trong config file
        return appConfig;
    }
}
