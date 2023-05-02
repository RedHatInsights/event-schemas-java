package com.redhat.cloud.event.apps.exportservice.v1;

import com.fasterxml.jackson.annotation.*;

/**
 * Event data for data export requests.
 */
public class ExportRequest {
    private ExportRequestClass exportRequest;

    /**
     * A request for data to be exported
     */
    @JsonProperty("exportRequest")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ExportRequestClass getExportRequest() { return exportRequest; }
    @JsonProperty("exportRequest")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setExportRequest(ExportRequestClass value) { this.exportRequest = value; }
}
