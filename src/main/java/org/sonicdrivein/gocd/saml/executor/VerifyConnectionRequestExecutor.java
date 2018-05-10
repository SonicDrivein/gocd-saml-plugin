package org.sonicdrivein.gocd.saml.executor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;

public class VerifyConnectionRequestExecutor implements RequestExecutor {
    private static final Gson GSON = new Gson();
    private static final Logger LOG = Logger.getLoggerFor(UserAuthenticationExecutor.class);
    private final GoPluginApiRequest request;

    public VerifyConnectionRequestExecutor(GoPluginApiRequest request) {
        this.request = request;
    }

    @Override
    public GoPluginApiResponse execute() throws IOException {
        JsonObject jsonObject = GSON.fromJson(this.request.requestBody(), JsonObject.class);
        String authorizationUrl = jsonObject.get("AuthorizationUrlField").getAsString();
        HashMap<String, String> result = new HashMap<>();
        if (validationConfiguration(authorizationUrl)) {
            result.put("status", "success");
            result.put("message", "Check connection passed");
        } else {
            result.put("status", "failure");
            result.put("message", String.format("Check connection failed, unable to reach %s", authorizationUrl));
        }
        return DefaultGoPluginApiResponse.success(GSON.toJson(result));
    }

    public Boolean validationConfiguration(String authUrl) throws IOException {
        URL url = new URL(authUrl);
        boolean available = false;
        int port = url.getPort() == -1 ? url.getDefaultPort() : url.getPort();
        Socket s = null;

        try {
            s = new Socket(url.getHost(), port);
            if (s.isConnected()) {
                available = true;
            }
        } catch (Exception e) { //connection refused
            LOG.error(e.getClass() + ": " + e.getMessage(), e);
        }
        finally {
            if (s != null) {
                s.close();
            }
        }
        return available;
    }
}
