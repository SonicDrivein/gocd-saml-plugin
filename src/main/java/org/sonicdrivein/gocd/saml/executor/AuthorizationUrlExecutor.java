package org.sonicdrivein.gocd.saml.executor;

import org.sonicdrivein.gocd.saml.model.Configuration;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.sonicdrivein.gocd.saml.utils.Util;

import java.util.HashMap;

public class AuthorizationUrlExecutor implements RequestExecutor {

    private final GoPluginApiRequest request;

    public AuthorizationUrlExecutor(GoPluginApiRequest request) {
        this.request = request;
    }

    @Override
    public GoPluginApiResponse execute() {
        HashMap<String, Object> response = new HashMap<>();
        Configuration configuration = Configuration.fromJSON(request.requestBody());
        response.put("authorization_server_url", configuration.getAuthorizationUrlField());
        return new DefaultGoPluginApiResponse(200, Util.GSON.toJson(response));
    }
}
