package org.sonicdrivein.gocd.saml.executor;

import org.sonicdrivein.gocd.saml.GoCDSAMLPlugin;
import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.request.DefaultGoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AuthorizationUrlExecutorTest {

    @Test
    public void shouldGetAuthorizationUrl() throws Exception {
        Gson GSON = new Gson();
        GoCDSAMLPlugin plugin = new GoCDSAMLPlugin();
        DefaultGoPluginApiRequest request = new RequestBuilder(RequestFromServer.REQUEST_AUTHORIZATION_URL)
                .withRequestBody("{\"auth_configs\":[{\"configuration\":{\"AuthorizationUrlField\":\"https://www.google.com\"}}]}")
                .build();
        GoPluginApiResponse response = plugin.handle(request);
        assertEquals("{\"authorization_server_url\":\"https://www.google.com\"}", response.responseBody());


    }
}
