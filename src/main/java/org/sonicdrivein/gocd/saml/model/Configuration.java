package org.sonicdrivein.gocd.saml.model;

import org.sonicdrivein.gocd.saml.annotation.MetadataHelper;
import org.sonicdrivein.gocd.saml.annotation.ProfileField;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.sonicdrivein.gocd.saml.utils.Util;

import java.util.List;
import java.util.Map;

public class Configuration {

    @Expose
    @SerializedName("AuthorizationUrlField")
    @ProfileField(key = "AuthorizationUrlField", required = true, secure = false)
    private String authorizationUrlField;


    @Expose
    @SerializedName("SamlMetadataPath")
    @ProfileField(key = "SamlMetadataPath", required = true, secure = false)
    private String samlMetadataPath;


    public Configuration() {
    }

    public Configuration(String authorizationUrlField, String samlMetadataPath) {
        this.authorizationUrlField = authorizationUrlField;
        this.samlMetadataPath = samlMetadataPath;
    }

    public static Configuration fromJSON(String json) {
        JsonObject jsonObject = Util.GSON.fromJson(json, JsonObject.class);
        return Util.GSON.fromJson(jsonObject.getAsJsonArray("auth_configs").get(0).getAsJsonObject().get("configuration"), Configuration.class);
    }

    public static List<Map<String, String>> validate(Map<String, String> properties) {
        return MetadataHelper.validate(Configuration.class, properties);
    }

    public String getAuthorizationUrlField() {
        return authorizationUrlField;
    }

    public String getSamlMetadataPath() {
        return samlMetadataPath;
    }
}
