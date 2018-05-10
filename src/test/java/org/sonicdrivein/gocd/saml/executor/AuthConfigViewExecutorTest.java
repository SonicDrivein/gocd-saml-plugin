package org.sonicdrivein.gocd.saml.executor;


import org.sonicdrivein.gocd.saml.GoCDSAMLPlugin;
import org.sonicdrivein.gocd.saml.utils.Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AuthConfigViewExecutorTest {

    @Test
    public void shouldGetAuthConfigView() throws Exception {
        Gson GSON = new Gson();
        GoCDSAMLPlugin plugin = new GoCDSAMLPlugin();
        JsonObject expectedJsonObject = new JsonObject();
        expectedJsonObject.addProperty("template", Util.readResource("/auth-config.template.html"));
        GoPluginApiResponse response = plugin.handle(new RequestBuilder(RequestFromServer.REQUEST_AUTH_CONFIG_VIEW).build());
        assertEquals(GSON.toJson(expectedJsonObject), response.responseBody());

    }
}
