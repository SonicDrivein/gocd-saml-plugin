package org.sonicdrivein.gocd.saml.executor;

import org.sonicdrivein.gocd.saml.model.Capabilities;
import org.sonicdrivein.gocd.saml.model.SupportedAuthType;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import static com.thoughtworks.go.plugin.api.response.DefaultGoApiResponse.SUCCESS_RESPONSE_CODE;

public class GetCapabilitiesExecutor {

    public GoPluginApiResponse execute() {
        Capabilities capabilities = getCapabilities();
        return new DefaultGoPluginApiResponse(SUCCESS_RESPONSE_CODE, capabilities.toJSON());
    }

    Capabilities getCapabilities() {
        return new Capabilities(SupportedAuthType.Web, false, true);
    }
}
