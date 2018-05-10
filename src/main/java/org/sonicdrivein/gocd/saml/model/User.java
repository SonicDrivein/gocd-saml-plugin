package org.sonicdrivein.gocd.saml.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @Expose
    @SerializedName("username")
    private String username;

    @Expose
    @SerializedName("display_name")
    private String displayName;

    @Expose
    @SerializedName("email")
    private String emailId;

    public User(String username, String displayName, String emailId) {
        this.username = username;
        this.displayName = displayName;
        this.emailId = emailId == null ? null : emailId.toLowerCase().trim();
    }

    @Override
    public String toString() {
        return "User@{username=" + username +
                ", displayName=" + displayName +
                ", emailId=" + emailId +
                "}";
    }
}