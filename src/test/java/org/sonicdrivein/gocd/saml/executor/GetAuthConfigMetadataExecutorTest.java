package org.sonicdrivein.gocd.saml.executor;

import org.sonicdrivein.gocd.saml.GoCDSAMLPlugin;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GetAuthConfigMetadataExecutorTest {

    @Test
    public void getAuthConfigMetadata() throws Exception {
        GoCDSAMLPlugin goCDSAMLPlugin = new GoCDSAMLPlugin();

        GoPluginApiResponse response = goCDSAMLPlugin.handle(new RequestBuilder(RequestFromServer.REQUEST_GET_AUTH_CONFIG_METADATA).build());

        assertEquals("[" +
                "{\"key\":\"AuthorizationUrlField\",\"metadata\":{\"required\":true,\"secure\":false}}," +
                "{\"key\":\"SamlMetadataPath\",\"metadata\":{\"required\":true,\"secure\":false}}" +
                "]", response.responseBody());

    }
}