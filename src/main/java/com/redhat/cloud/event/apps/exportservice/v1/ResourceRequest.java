package com.redhat.cloud.event.apps.exportservice.v1;

import com.fasterxml.jackson.annotation.*;

/**
 * Event data for data export requests
 */
public class ResourceRequest {
    private ResourceRequestClass resourceRequest;

    /**
     * A request for data to be exported
     */
    @JsonProperty("resource_request")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ResourceRequestClass getResourceRequest() { return resourceRequest; }
    @JsonProperty("resource_request")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setResourceRequest(ResourceRequestClass value) { this.resourceRequest = value; }
}
