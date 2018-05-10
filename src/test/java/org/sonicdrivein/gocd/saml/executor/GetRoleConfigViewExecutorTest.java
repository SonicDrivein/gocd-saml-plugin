package org.sonicdrivein.gocd.saml.executor;

import org.sonicdrivein.gocd.saml.GoCDSAMLPlugin;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GetRoleConfigViewExecutorTest {

    @Test
    public void getRoleConfigViewIsEmpty() throws Exception {
        // since our role mappings are so simple so our role config view is just empty
        GoCDSAMLPlugin goCDSAMLPlugin = new GoCDSAMLPlugin();

        GoPluginApiResponse response = goCDSAMLPlugin.handle(new RequestBuilder(RequestFromServer.REQUEST_ROLE_CONFIG_VIEW).build());

        assertEquals("{\"template\":\"\\u003cbr/\\u003e\"}", response.responseBody());
    }
}