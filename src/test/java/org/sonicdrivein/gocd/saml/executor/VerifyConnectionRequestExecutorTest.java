package org.sonicdrivein.gocd.saml.executor;

import org.sonicdrivein.gocd.saml.GoCDSAMLPlugin;
import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class VerifyConnectionRequestExecutorTest {

    @Test
    public void shouldVerifyConnectionForValidAuthorizationUrl() throws Exception {
        GoCDSAMLPlugin plugin = new GoCDSAMLPlugin();
        Gson GSON = new Gson();
        HashMap<String, String> expectedResult = new HashMap<>();
        expectedResult.put("status", "success");
        expectedResult.put("message", "Check connection passed");

        GoPluginApiResponse response = plugin.handle(new RequestBuilder(RequestFromServer.REQUEST_VERIFY_CONNECTION)
                .withRequestBody("{\"AuthorizationUrlField\":\"https://www.google.com\"}")
                .build());
        assertEquals(GSON.toJson(expectedResult), response.responseBody());
    }

    @Test
    public void shouldVerifyConnectionForInvalidAuthorizationUrl() throws Exception {
        GoCDSAMLPlugin plugin = new GoCDSAMLPlugin();
        Gson GSON = new Gson();
        HashMap<String, String> expectedResult = new HashMap<>();
        expectedResult.put("status", "failure");
        expectedResult.put("message", "Check connection failed, unable to reach https://foo.com");

        GoPluginApiResponse response = plugin.handle(new RequestBuilder(RequestFromServer.REQUEST_VERIFY_CONNECTION)
                .withRequestBody("{\"AuthorizationUrlField\":\"https://foo.com\"}")
                .build());
        assertEquals(GSON.toJson(expectedResult), response.responseBody());
    }

     @Test
    public void shouldVerifyConnectionForValidAuthorizationUrlWith() throws Exception {
        GoCDSAMLPlugin plugin = new GoCDSAMLPlugin();
        Gson GSON = new Gson();
        HashMap<String, String> expectedResult = new HashMap<>();
        expectedResult.put("status", "success");
        expectedResult.put("message", "Check connection passed");

        GoPluginApiResponse response = plugin.handle(new RequestBuilder(RequestFromServer.REQUEST_VERIFY_CONNECTION)
                .withRequestBody("{\"AuthorizationUrlField\":\"https://<CLEANSED>\"}")
                .build());
        assertEquals(GSON.toJson(expectedResult), response.responseBody());
    }

}
