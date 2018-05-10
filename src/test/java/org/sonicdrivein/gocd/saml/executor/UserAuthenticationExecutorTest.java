package org.sonicdrivein.gocd.saml.executor;

import org.sonicdrivein.gocd.saml.GoCDSAMLPlugin;
import org.sonicdrivein.gocd.saml.utils.BadSAMLSignatureException;
import org.sonicdrivein.gocd.saml.utils.SAMLParser;
import org.sonicdrivein.gocd.saml.utils.SAMLValidator;
import org.sonicdrivein.gocd.saml.utils.Util;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opensaml.Configuration;
import org.opensaml.saml2.core.Response;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.security.DefaultSecurityConfigurationBootstrap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserAuthenticationExecutorTest {

    private GoCDSAMLPlugin plugin;

    private String generateSamlToken(String xmlFile) {
        return Base64.encodeBase64String(Util.readResourceBytes(xmlFile));
    }

    @BeforeClass
    public static void setupClass() throws ConfigurationException {
        Configuration.setGlobalSecurityConfiguration(DefaultSecurityConfigurationBootstrap.buildDefaultConfig());
        SAMLParser.initialize();
    }

    @Before
    public void setUp() throws Exception {
        plugin = new GoCDSAMLPlugin();
    }

    @Test
    public void shouldAuthenticateIgnoringValidate() throws Exception {
        // given
        SAMLValidator mock = mock(SAMLValidator.class);
        when(mock.isValidDateTime(isA(Response.class))).thenReturn(true);
        plugin.setSamlValidator(mock);

        // when
        GoPluginApiResponse response = plugin.handle(new RequestBuilder(RequestFromServer.REQUEST_AUTHENTICATE_USER)
                .withRequestBody(generateRequestBody()).build());

        // then
        assertEquals("{" +
                "\"roles\":[\"silly-group\",\"very-silly-group\"]," +
                "\"user\":{" +
                "\"username\":\"temp_user\"," +
                "\"display_name\":\"temp_user\"," +
                "\"email\":\"temp_user@sonicdrivein.com\"" +
                "}}", response.responseBody());
    }

    private String generateRequestBody() {
        String samlMetadataPath = this.getClass().getClassLoader().getResource("SAML.xml").getFile();
        return String.format(
                "{\"auth_configs\":[{\"configuration\":{\"SamlMetadataPath\":\"%s\"},\"id\":\"saml-centrify\"}],\"credentials\":{\"SAMLResponse\":\"%s\"},\"role_configs\":[]}",
                samlMetadataPath,
                generateSamlToken("/saml_assertion_example.xml"));
    }

    @Test
    public void shouldReturnBadResponseCodeForCounterfeitSAMLAssertion() throws UnhandledRequestTypeException {
        try {
            plugin.handle(new RequestBuilder(RequestFromServer.REQUEST_AUTHENTICATE_USER)
                    .withRequestBody(generateRequestBody()).build());
            fail();
        } catch(RuntimeException e) {
            assertEquals(e.getCause().getClass(), BadSAMLSignatureException.class);
        }

    }

}