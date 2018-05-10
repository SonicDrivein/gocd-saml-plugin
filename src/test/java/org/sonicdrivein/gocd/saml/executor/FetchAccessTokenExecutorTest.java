package org.sonicdrivein.gocd.saml.executor;

import org.sonicdrivein.gocd.saml.GoCDSAMLPlugin;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class FetchAccessTokenExecutorTest {

    @Test
    public void shouldFetchToken() throws Exception {
        GoCDSAMLPlugin plugin = new GoCDSAMLPlugin();
        HashMap<String, String> authRequestParameters = new HashMap<>();
        authRequestParameters.put("SAMLResponse", "saml-response-content");

        GoPluginApiResponse response = plugin.handle(
                new RequestBuilder(RequestFromServer.REQUEST_FETCH_ACCESS_TOKEN)
                        .withRequestParameters(authRequestParameters).build());

        assertEquals(response.responseBody(), "{\"SAMLResponse\":\"saml-response-content\"}");
    }
}
