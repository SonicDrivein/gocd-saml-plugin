package org.sonicdrivein.gocd.saml.executor;


import org.sonicdrivein.gocd.saml.GoCDSAMLPlugin;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CapabilitiesExecutorTest {

    @Test
    public void shouldGetCapabilities() throws Exception{
        GoCDSAMLPlugin plugin = new GoCDSAMLPlugin();
        GoPluginApiResponse response = plugin.handle(new RequestBuilder(RequestFromServer.REQUEST_GET_CAPABILITIES).build());
        assertEquals("{\"supported_auth_type\":\"web\",\"can_search\":false,\"can_authorize\":true}", response.responseBody());
    }
}


