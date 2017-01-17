package io.vertx.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by kurro on 1/17/17.
 */
public class JsonRequest<T> {

    @JsonIgnore
    protected RoutingContext context;

    @JsonProperty("data")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected  T data;

    public JsonRequest(){
    }

    public RoutingContext getContext() {
        return context;
    }

    public void setContext(RoutingContext context) {
        this.context = context;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
