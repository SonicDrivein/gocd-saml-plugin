package org.sonicdrivein.gocd.saml.executor;

import com.thoughtworks.go.plugin.api.request.DefaultGoPluginApiRequest;

import java.util.HashMap;

public class RequestBuilder {
    private final DefaultGoPluginApiRequest request;

    public RequestBuilder(RequestFromServer requestType) {
        this.request = new DefaultGoPluginApiRequest("", "", requestType.requestName());
    }
    public RequestBuilder withRequestBody(String requestBody) {
        this.request.setRequestBody(requestBody);
        return this;
    }

    public RequestBuilder withRequestParameters(HashMap<String, String> requestParameters) {
        this.request.setRequestParams(requestParameters);
        return this;
    }

    public DefaultGoPluginApiRequest build() {
        return request;
    }
}
