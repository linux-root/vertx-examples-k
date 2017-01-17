package app.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by kurro on 1/17/17.
 */
public class SmartPhone {

    private String databaseId;
    private String title;
    private String description;
    private Integer number;
    private Date createdAt;
    private Date updatedAt;

    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }



    @JsonProperty("created_at_text")
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "YYYY-MM-dd\'T\'HH:mm:ssZZ",  // gắn với method get bên dưới
            timezone = "GMT+7"
    )
    public Date getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("updated_at_text")
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "YYYY-MM-dd\'T\'HH:mm:ssZZ",
            timezone = "GMT+7"
    )
    public Date getUpdatedAt() {
        return updatedAt;
    }




}
