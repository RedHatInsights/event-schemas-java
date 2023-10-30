package com.redhat.cloud.event.core.v1;

import com.fasterxml.jackson.annotation.*;

/**
 * Notification recipients. Should be in a top-level field named "notification_recipients"
 */
public class Recipients {
    private String[] emails;
    private Boolean ignoreUserPreferences;
    private Boolean onlyAdmins;
    private String[] users;

    /**
     * List of emails to direct the notification to. This won’t override notification's
     * administrators settings. Emails list will be merged with other settings.
     */
    @JsonProperty("emails")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String[] getEmails() { return emails; }
    @JsonProperty("emails")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setEmails(String[] value) { this.emails = value; }

    /**
     * Setting to true ignores all the user preferences on this Recipient setting (It doesn’t
     * affect other configuration that an Administrator sets on their Notification settings).
     * Setting to false honors the user preferences.
     */
    @JsonProperty("ignore_user_preferences")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Boolean getIgnoreUserPreferences() { return ignoreUserPreferences; }
    @JsonProperty("ignore_user_preferences")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setIgnoreUserPreferences(Boolean value) { this.ignoreUserPreferences = value; }

    /**
     * Setting to true sends an email to the administrators of the account. Setting to false
     * sends an email to all users of the account.
     */
    @JsonProperty("only_admins")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Boolean getOnlyAdmins() { return onlyAdmins; }
    @JsonProperty("only_admins")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setOnlyAdmins(Boolean value) { this.onlyAdmins = value; }

    /**
     * List of users to direct the notification to. This won’t override notification's
     * administrators settings. Users list will be merged with other settings.
     */
    @JsonProperty("users")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String[] getUsers() { return users; }
    @JsonProperty("users")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setUsers(String[] value) { this.users = value; }
}
