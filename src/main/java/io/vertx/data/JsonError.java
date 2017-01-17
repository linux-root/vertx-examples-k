package io.vertx.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Created by khanhdb@eway.vn on 1/17/17.
 */
public class JsonError {
    @JsonProperty("message")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String message;

    @JsonProperty("root_cause_message")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String rootCauseMessage;

    @JsonProperty("detail")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String detail;

    public JsonError(){
        //constructor
    }

    public JsonError(String message){
        this.message = message;
    }

    /* set message, rootCauseMessage từ dữ liệu của throwable
      (debug == true) => setDetail = stack trace, else : không setDetail
    * */
    public JsonError(Throwable throwable, boolean debug){
        this.message = ExceptionUtils.getMessage(throwable);
        this.rootCauseMessage = ExceptionUtils.getRootCauseMessage(throwable);
        if(debug){
            this.setDetail(ExceptionUtils.getStackTrace(throwable));
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRootCauseMessage(String rootCauseMessage) {
        this.rootCauseMessage = rootCauseMessage;

    }

    public void setDetail(String detail) {
        this.detail = detail;

    }

}
