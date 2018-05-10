package org.sonicdrivein.gocd.saml.executor;

import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.HashMap;

import static org.sonicdrivein.gocd.saml.utils.Util.GSON;

public class FetchAccessTokenExecutor implements RequestExecutor {
    private final GoPluginApiRequest request;

    public FetchAccessTokenExecutor(GoPluginApiRequest request) {
        this.request = request;
    }

    @Override
    public GoPluginApiResponse execute() throws Exception {
        HashMap<String, String> result = new HashMap<>();
        result.put("SAMLResponse", request.requestParameters().get("SAMLResponse"));
        return new DefaultGoPluginApiResponse(200, GSON.toJson(result));
    }
}
