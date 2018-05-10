package org.sonicdrivein.gocd.saml.executor;


import org.sonicdrivein.gocd.saml.GoCDSAMLPlugin;
import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class AuthConfigValidateRequestExecutorTest {

    @Test
    public void shouldValidateInvalidAuthConfigRequest() throws Exception {
        GoCDSAMLPlugin plugin = new GoCDSAMLPlugin();
        Gson GSON = new Gson();
        GoPluginApiResponse response = plugin.handle(new RequestBuilder(RequestFromServer.REQUEST_VALIDATE_AUTH_CONFIG)
                .withRequestBody("{}")
                .build());

        List<Map<String, String>> expectedResult = new ArrayList<>();

        HashMap<String, String> authorizationUrlMetadata = new HashMap<>();
        authorizationUrlMetadata.put("message", "AuthorizationUrlField must not be blank.");
        authorizationUrlMetadata.put("key", "AuthorizationUrlField");
        HashMap<String, String> samlMetdataPathMetadata = new HashMap<>();
        samlMetdataPathMetadata.put("message", "SamlMetadataPath must not be blank.");
        samlMetdataPathMetadata.put("key", "SamlMetadataPath");
        expectedResult.add(authorizationUrlMetadata);
        expectedResult.add(samlMetdataPathMetadata);

        assertEquals(GSON.toJson(expectedResult), response.responseBody());
    }

    @Test
    public void shouldValidateValidAuthConfigRequest() throws Exception {
        GoCDSAMLPlugin plugin = new GoCDSAMLPlugin();
        GoPluginApiResponse response = plugin.handle(new RequestBuilder(RequestFromServer.REQUEST_VALIDATE_AUTH_CONFIG)
                .withRequestBody("{" +
                        "\"AuthorizationUrlField\":\"https://www.google.com\"," +
                        "\"SamlMetadataPath\":\"/a/b/c\"}")
                .build());

        assertEquals("[]", response.responseBody());
    }
}
