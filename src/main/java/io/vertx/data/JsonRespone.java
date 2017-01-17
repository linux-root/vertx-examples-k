package io.vertx.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.JsonUtils;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by khanhdb@eway.vn on 1/17/17.
 */
public class JsonRespone<T> {

    @JsonProperty("context")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private RoutingContext context;

    @JsonProperty("status_code")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer statusCode;

    @JsonProperty("meta")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private JsonMeta meta;

    @JsonProperty("error")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private JsonError error;

    @JsonProperty("data")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public JsonRespone(RoutingContext context, Integer statusCode){
        this.context = context;
        this.statusCode = statusCode;
    }

    public RoutingContext getContext() {
        return context;
    }

    public void setContext(RoutingContext context) {
        this.context = context;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public JsonMeta getMeta() {
        return meta;
    }

    public void setMeta(JsonMeta meta) {
        this.meta = meta;
    }

    public JsonError getError() {
        return error;
    }

    public void setError(JsonError error) {
        this.error = error;
    }

    public void setError(Throwable error, Boolean debug) {
        this.error = new JsonError(error, debug);
    }


    public void setError(String message) {
        this.error = new JsonError(message);
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void write() {
        context.response().setStatusCode(statusCode)
                .putHeader("Content-Type", "application/json;charset=UTF-8")
                .end(JsonUtils.encode(this));
    }

}
