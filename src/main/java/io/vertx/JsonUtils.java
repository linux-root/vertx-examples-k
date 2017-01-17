package io.vertx;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.data.JsonRequest;
import io.vertx.data.JsonRespone;
import io.vertx.ext.web.RoutingContext;

import java.io.IOException;

/**
 * Created by khanhdb@eway.vn on 1/17/17.
 */
public class JsonUtils {
    private final static ObjectMapper objectMapper = new ObjectMapper(){
        {
            configure(JsonParser.Feature.ALLOW_COMMENTS, true);
            setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        }
    };

        public static <T> JsonRequest<T> decode(RoutingContext routingContext, Class<T> genericClass) {
            JsonRequest<T> jsonRequest;
            try {
                if (HttpMethod.GET.equals(routingContext.request().method()) || HttpMethod.PUT.equals(routingContext.request().method())) {
                    JavaType jsonRequestType = objectMapper.getTypeFactory().constructParametricType(JsonRequest.class, genericClass); //tạo ra kiểu JsonRequest<T>
                    jsonRequest = objectMapper.readValue(routingContext.getBody().getBytes(), jsonRequestType);
                } else {
                    jsonRequest = new JsonRequest<T>();
                }
                jsonRequest.setContext(routingContext);
                return jsonRequest;

            }catch (Exception e){
                throw new RuntimeException("fail to decode", e);
            }
        }



    public static Buffer encode(Object object){
        if(object == null) return  null;

        try {
            return Buffer.buffer(objectMapper.writeValueAsBytes(object));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("fail to encode as JSON", e);
        }
    }
}
