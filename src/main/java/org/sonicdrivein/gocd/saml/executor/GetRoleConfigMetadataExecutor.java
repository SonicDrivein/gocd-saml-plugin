package org.sonicdrivein.gocd.saml.executor;

import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

public class GetRoleConfigMetadataExecutor implements RequestExecutor {

    public GoPluginApiResponse execute() throws Exception {
        return new DefaultGoPluginApiResponse(200, "[]");
    }

}
