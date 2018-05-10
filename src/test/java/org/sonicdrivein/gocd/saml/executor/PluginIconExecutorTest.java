package org.sonicdrivein.gocd.saml.executor;

import org.sonicdrivein.gocd.saml.GoCDSAMLPlugin;
import org.sonicdrivein.gocd.saml.utils.Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PluginIconExecutorTest {
    @Test
    public void shouldGetPlugin() throws Exception {
        Gson GSON = new Gson();
        JsonObject expectedJsonObject = new JsonObject();
        GoCDSAMLPlugin plugin = new GoCDSAMLPlugin();
        GoPluginApiResponse response = plugin.handle(new RequestBuilder(RequestFromServer.REQUEST_GET_PLUGIN_ICON).build());
        expectedJsonObject.addProperty("content_type", "image/png");
        expectedJsonObject.addProperty("data", Base64.encodeBase64String(Util.readResourceBytes("/saml_button.png")));
        assertEquals(GSON.toJson(expectedJsonObject), response.responseBody());
    }
}
