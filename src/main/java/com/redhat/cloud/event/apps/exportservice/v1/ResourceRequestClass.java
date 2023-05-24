package com.redhat.cloud.event.apps.exportservice.v1;

import com.fasterxml.jackson.annotation.*;
import java.util.Map;
import java.util.UUID;

/**
 * A request for data to be exported
 */
public class ResourceRequestClass {
    private String application;
    private UUID exportRequestUUID;
    private Map<String, Object> filters;
    private Format format;
    private String resource;
    private UUID uuid;
    private String xRhIdentity;

    /**
     * The application being requested
     */
    @JsonProperty("application")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getApplication() { return application; }
    @JsonProperty("application")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setApplication(String value) { this.application = value; }

    /**
     * The unique identifier of the export request that triggered the resource request
     */
    @JsonProperty("export_request_uuid")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public UUID getExportRequestUUID() { return exportRequestUUID; }
    @JsonProperty("export_request_uuid")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setExportRequestUUID(UUID value) { this.exportRequestUUID = value; }

    /**
     * The filters to be applied to the data
     */
    @JsonProperty("filters")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Map<String, Object> getFilters() { return filters; }
    @JsonProperty("filters")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setFilters(Map<String, Object> value) { this.filters = value; }

    /**
     * The format of the data to be exported
     */
    @JsonProperty("format")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Format getFormat() { return format; }
    @JsonProperty("format")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setFormat(Format value) { this.format = value; }

    /**
     * The resource to be exported
     */
    @JsonProperty("resource")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getResource() { return resource; }
    @JsonProperty("resource")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setResource(String value) { this.resource = value; }

    /**
     * A unique identifier for the resource request
     */
    @JsonProperty("uuid")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public UUID getUUID() { return uuid; }
    @JsonProperty("uuid")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setUUID(UUID value) { this.uuid = value; }

    /**
     * The Base64-encoded JSON identity header of the user making the request
     */
    @JsonProperty("x-rh-identity")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getXRhIdentity() { return xRhIdentity; }
    @JsonProperty("x-rh-identity")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setXRhIdentity(String value) { this.xRhIdentity = value; }
}
