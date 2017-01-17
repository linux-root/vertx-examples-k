package io.vertx;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;
import io.vertx.data.JsonRespone;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.*;
import org.slf4j.Logger;

import javax.jws.WebService;

/**
 * Created by Khanhdb@eway.vn on 1/17/17.
 */
public abstract class WebServer <T extends BaseAppConfig>{

    static {
        System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.SLF4LogDelegateFactory");
    }
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(WebServer.class);
    //vert x tao ra tat ca
    protected final T appConfig;
    protected Router router;
    protected Vertx vertx;
    protected HttpServer httpServer;

    public WebServer(Vertx vertx, T appConfig){
        this.appConfig = appConfig;
        this.vertx = vertx;
        this.router = Router.router(vertx);
    }

    public static Logger getLogger() {
        return logger;
    }

    public T getAppConfig() {
        return appConfig;
    }

    public Router getRouter() {
        return router;
    }

    public Vertx getVertx() {
        return vertx;
    }

    public HttpServer getHttpServer() {
        return httpServer;
    }

    public void close(){
        if(httpServer != null){
            httpServer.close();
        }                                //WARNING
        if(vertx != null){
            vertx.close();
        }
    }

    public void start(Handler<AsyncResult<HttpServer>> listentHandler) throws  Exception{
        this.setupJsonMapper();
        this.setupHttpServer();

        //setup router
        this.setupLoggerHandler(router);
        this.setupBodyHandler(router);
        this.setupCookieHandler(router);
        this.setupStaticHandler(router);
        this.setupFaviconHandler(router);
        this.setupFailureHandler(router);
        this.setupNotFoundHandler(router);
        this.setupRouter(router);
    }

    protected void setupJsonMapper(){
        Json.mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        Json.mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Json.mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        Json.prettyMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        Json.prettyMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Json.prettyMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    protected void setupHttpServer(){
        HttpServerOptions httpServerOptions = new HttpServerOptions();
        httpServerOptions.setMaxInitialLineLength(4096*4);
        httpServerOptions.setCompressionSupported(true); //vì dữ liệu truyền có dung lượng nhỏ nên có thể dùng tùy chọn này để gửi nhanh hơn. Không dùng khi server gửi đi file dung lượng cao
        httpServerOptions.setMaxHeaderSize(8192*2); //Default is 8192
        this.httpServer = this.vertx.createHttpServer(httpServerOptions);
    }

    protected void setupLoggerHandler(Router router){
        LoggerHandler loggerHandler = LoggerHandler.create();
        router.route().handler(loggerHandler);
    }

    protected void setupBodyHandler(Router router){
        BodyHandler bodyHandler = BodyHandler.create();
        router.route().handler(bodyHandler);
    }

    protected void setupCookieHandler(Router router){
        CookieHandler cookieHandler = CookieHandler.create(); //create vi khong the dung new
        router.route().handler(cookieHandler);
    }

    protected void setupStaticHandler(Router router){
        StaticHandler staticHandler = StaticHandler.create();
        // disable cache for better testing
        staticHandler.setCachingEnabled(false);
        router.route("/static/*").handler(staticHandler); // tất cả các handler còn lại để trống rout() có nghĩa là handler cho tất cả trường hợp. Am I right? vd : luon hien thi icon, luon xu ly body,... trong moi truong hop
    }

    protected void setupFaviconHandler(Router router){
        FaviconHandler faviconHandler = FaviconHandler.create();
        router.route().handler(faviconHandler);
    }

    protected void setupFailureHandler(Router router){
        //why?
        router.route().failureHandler(this::handleFailure);
    }

    protected void setupNotFoundHandler(Router router){
        router.route().failureHandler(this::handleNotFound);
    }

    protected void handleFailure(RoutingContext routingContext) {
        final Throwable failure = routingContext.failure();
        if(failure == null){
            routingContext.next();
            return;
        }
        logger.error("handle failure: " + routingContext.request().path(), failure);
        JsonRespone<Object> jsonRespone = new JsonRespone<>(routingContext, 500);
        jsonRespone.setError(failure, Boolean.valueOf(routingContext.request().getParam("debug")));
        jsonRespone.write();
    }

    protected void handleNotFound(RoutingContext routingContext){
        JsonRespone<Object> jsonRespone = new JsonRespone<>(routingContext,404);
        jsonRespone.setError("Resoure not found");
        jsonRespone.write();
    }


    protected abstract void setupRouter(Router router) throws Exception;
}
