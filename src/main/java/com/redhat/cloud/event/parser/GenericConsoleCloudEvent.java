package com.redhat.cloud.event.parser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericConsoleCloudEvent<T> {

    @JsonProperty("$schema")
    private final String jsonSchema = "https://console.redhat.com/api/schemas/events/v1/events.json";

    @JsonInclude(JsonInclude.Include.NON_NULL)
    UUID id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String source;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("specversion")
    String specVersion;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String subject;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    LocalDateTime time;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("dataschema")
    String dataSchema;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("redhatorgid")
    String orgId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("redhataccount")
    String accountId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSpecVersion() {
        return specVersion;
    }

    public void setSpecVersion(String specVersion) {
        this.specVersion = specVersion;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDataSchema() {
        return dataSchema;
    }

    public void setDataSchema(String dataSchema) {
        this.dataSchema = dataSchema;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
