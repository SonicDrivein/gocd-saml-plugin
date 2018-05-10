package org.sonicdrivein.gocd.saml.executor;

import org.sonicdrivein.gocd.saml.GoCDSAMLPlugin;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RoleConfigValidateRequestExecutorTest {
    @Test
    public void rolesAreAlwaysValid() throws Exception {
        GoCDSAMLPlugin goCDSAMLPlugin = new GoCDSAMLPlugin();

        GoPluginApiResponse response = goCDSAMLPlugin.handle(new RequestBuilder(RequestFromServer.REQUEST_VALIDATE_ROLE_CONFIG).build());

        assertEquals("[]", response.responseBody()); // empty array = empty list of errors


    }
}