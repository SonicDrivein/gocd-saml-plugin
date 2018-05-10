package org.sonicdrivein.gocd.saml.utils;


import org.sonicdrivein.gocd.saml.GoCDSAMLPlugin;
import org.sonicdrivein.gocd.saml.executor.RequestBuilder;
import org.sonicdrivein.gocd.saml.executor.RequestFromServer;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class GoCDSAMLPluginTest {

    @Test
    public void handlesNoSuchRequest() throws Exception {
        GoCDSAMLPlugin goCDSAMLPlugin = new GoCDSAMLPlugin();

        try {
            // we dont support user search
            goCDSAMLPlugin.handle(new RequestBuilder(RequestFromServer.REQUEST_SEARCH_USERS).build());
            fail();
        } catch (Exception e) {
            assertEquals(e.getCause().getClass(), UnhandledRequestTypeException.class);
        }
    }

}
