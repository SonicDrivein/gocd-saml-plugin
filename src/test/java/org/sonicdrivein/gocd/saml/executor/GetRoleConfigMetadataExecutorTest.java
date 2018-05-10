package org.sonicdrivein.gocd.saml.executor;

import org.sonicdrivein.gocd.saml.GoCDSAMLPlugin;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GetRoleConfigMetadataExecutorTest {

    @Test
    public void getRoleConfigMetadataIsEmptyArray() throws Exception {
        // since we are very simplistic dealing with role mapping (AD group maps to role by name)
        // we dont need to have any config metadata
        GoCDSAMLPlugin goCDSAMLPlugin = new GoCDSAMLPlugin();

        GoPluginApiResponse response = goCDSAMLPlugin.handle(new RequestBuilder(RequestFromServer.REQUEST_GET_ROLE_CONFIG_METADATA).build());

        assertEquals("[]", response.responseBody());
    }
}