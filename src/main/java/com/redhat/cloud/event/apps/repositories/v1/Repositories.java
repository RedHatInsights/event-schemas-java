package com.redhat.cloud.event.apps.repositories.v1;

import com.fasterxml.jackson.annotation.*;
import java.time.OffsetDateTime;

public class Repositories {
    private String distributionArch;
    private String[] distributionVersions;
    private Long failedIntrospectionsCount;
    private String gpgKey;
    private String lastIntrospectionError;
    private OffsetDateTime lastIntrospectionTime;
    private OffsetDateTime lastSuccessIntrospectionTime;
    private OffsetDateTime lastUpdateIntrospectionTime;
    private Boolean metadataVerification;
    private String name;
    private Long packageCount;
    private String status;
    private String url;
    private String uuid;

    @JsonProperty("distribution_arch")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getDistributionArch() { return distributionArch; }
    @JsonProperty("distribution_arch")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setDistributionArch(String value) { this.distributionArch = value; }

    @JsonProperty("distribution_versions")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String[] getDistributionVersions() { return distributionVersions; }
    @JsonProperty("distribution_versions")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setDistributionVersions(String[] value) { this.distributionVersions = value; }

    @JsonProperty("failed_introspections_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Long getFailedIntrospectionsCount() { return failedIntrospectionsCount; }
    @JsonProperty("failed_introspections_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setFailedIntrospectionsCount(Long value) { this.failedIntrospectionsCount = value; }

    @JsonProperty("gpg_key")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getGPGKey() { return gpgKey; }
    @JsonProperty("gpg_key")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setGPGKey(String value) { this.gpgKey = value; }

    @JsonProperty("last_introspection_error")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getLastIntrospectionError() { return lastIntrospectionError; }
    @JsonProperty("last_introspection_error")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setLastIntrospectionError(String value) { this.lastIntrospectionError = value; }

    @JsonProperty("last_introspection_time")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public OffsetDateTime getLastIntrospectionTime() { return lastIntrospectionTime; }
    @JsonProperty("last_introspection_time")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setLastIntrospectionTime(OffsetDateTime value) { this.lastIntrospectionTime = value; }

    @JsonProperty("last_success_introspection_time")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public OffsetDateTime getLastSuccessIntrospectionTime() { return lastSuccessIntrospectionTime; }
    @JsonProperty("last_success_introspection_time")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setLastSuccessIntrospectionTime(OffsetDateTime value) { this.lastSuccessIntrospectionTime = value; }

    @JsonProperty("last_update_introspection_time")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public OffsetDateTime getLastUpdateIntrospectionTime() { return lastUpdateIntrospectionTime; }
    @JsonProperty("last_update_introspection_time")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setLastUpdateIntrospectionTime(OffsetDateTime value) { this.lastUpdateIntrospectionTime = value; }

    @JsonProperty("metadata_verification")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Boolean getMetadataVerification() { return metadataVerification; }
    @JsonProperty("metadata_verification")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setMetadataVerification(Boolean value) { this.metadataVerification = value; }

    @JsonProperty("name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getName() { return name; }
    @JsonProperty("name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setName(String value) { this.name = value; }

    @JsonProperty("package_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Long getPackageCount() { return packageCount; }
    @JsonProperty("package_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setPackageCount(Long value) { this.packageCount = value; }

    @JsonProperty("status")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getStatus() { return status; }
    @JsonProperty("status")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setStatus(String value) { this.status = value; }

    @JsonProperty("url")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getURL() { return url; }
    @JsonProperty("url")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setURL(String value) { this.url = value; }

    @JsonProperty("uuid")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getUUID() { return uuid; }
    @JsonProperty("uuid")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setUUID(String value) { this.uuid = value; }
}
