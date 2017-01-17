package app;

/**
 * Created by Khanhdb@eway.vn on 1/17/17.
 */

import io.vertx.core.Vertx;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.TimeZone;

/**
 * Created by chipn@eway.vn on 1/16/17.
 */
public class Runner {

    static {
        //Nếu app.home không được set thì app.home = user.dir
        System.setProperty("app.home", StringUtils.isBlank(System.getProperty("app.home")) ? System.getProperty("user.dir") : System.getProperty("app.home"));
        System.setProperty("vertx.cwd", System.getProperty("app.home"));

        File logbackFile = new File(System.getProperty("app.home"), "conf/logback.xml");
        if (logbackFile.exists() && StringUtils.isBlank(System.getProperty("logback.configurationFile"))) {
            System.setProperty("logback.configurationFile", logbackFile.getAbsolutePath());
        }

        try {
            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Bangkok")); // NOTE : tránh tìnhh trạng lệch múi giờ giữa các service được deploy ở các server khác nhau
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Runner.class);


    public static void main(String[] args) throws Exception {
        AppConfig appConfig = AppConfig.newInstance(AppConfig.class);
        appConfig.validate();

       WebServerExample server = new WebServerExample(Vertx.vertx(), appConfig);
        server.start(event -> {
            if (event.succeeded()) {
                logger.debug("WebServer started at http://127.0.0.1:" + appConfig.getHttpPort());
            } else {
                throw new RuntimeException("Unable to start WebServer at http://127.0.0.1:" + appConfig.getHttpPort(), event.cause());
            }
        });
    }
}
