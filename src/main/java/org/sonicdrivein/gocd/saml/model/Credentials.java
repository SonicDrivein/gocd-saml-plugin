package org.sonicdrivein.gocd.saml.model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.sonicdrivein.gocd.saml.utils.Util;

public class Credentials {
    @Expose
    @SerializedName("SAMLResponse")
    private String samlResponse;

    public Credentials() {}

    public Credentials(String samlResponse) {
        this.samlResponse = samlResponse;
    }

    public static Credentials fromJSON(String requestBody) {
        JsonObject jsonObject = Util.GSON.fromJson(requestBody, JsonObject.class);
        return Util.GSON.fromJson(jsonObject.get("credentials").toString(), Credentials.class);
    }

    public String getSamlResponse() {
        return samlResponse;
    }


}
