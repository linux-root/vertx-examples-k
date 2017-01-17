import app.AppConfig;
import app.WebServerExample;
import io.restassured.RestAssured;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.ServerSocket;

import static io.restassured.RestAssured.get;

/**
 * Created by Khanhdb@eway.vn on 1/17/17.
 */
@RunWith(VertxUnitRunner.class)

public class WebServerExampleTest extends TestCase {

    private Vertx vertx;
    @Before
    public void setUp(TestContext testContext) throws Exception {
        vertx = Vertx.vertx();
        ServerSocket serverSocket = new ServerSocket(0);
        RestAssured.port = serverSocket.getLocalPort(); // get random port

        serverSocket.close();

        AppConfig appConfig = new AppConfig();
        appConfig.setHttpPort(RestAssured.port);
        appConfig.validate();

        WebServerExample webServerExample = new WebServerExample(vertx, appConfig);
        Async async = testContext.async();
        webServerExample.start(event -> {
            if (event.succeeded()) {
                async.complete();
            } else {
                testContext.fail();
            }
        });
    }

        @After
        public void tearDown(TestContext testContext){
            vertx.close(testContext.asyncAssertSuccess());
        }

        @Test
    public void testStaticIndex(){
            get("/static").then().statusCode(200);
    }
}
