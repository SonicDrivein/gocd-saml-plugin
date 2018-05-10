package org.sonicdrivein.gocd.saml.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static org.sonicdrivein.gocd.saml.utils.Util.GSON;

public class Capabilities {
    @Expose
    @SerializedName("supported_auth_type")
    private final SupportedAuthType supportedAuthType;

    @Expose
    @SerializedName("can_search")
    private final boolean canSearch;

    @Expose
    @SerializedName("can_authorize")
    private final boolean canAuthorize;

    public Capabilities(SupportedAuthType supportedAuthType, boolean canSearch, boolean canAuthorize) {
        this.supportedAuthType = supportedAuthType;
        this.canSearch = canSearch;
        this.canAuthorize = canAuthorize;
    }

    public String toJSON() {
        return GSON.toJson(this);
    }
}
