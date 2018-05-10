package org.sonicdrivein.gocd.saml;

import org.sonicdrivein.gocd.saml.utils.SAMLParser;
import org.sonicdrivein.gocd.saml.utils.SAMLValidator;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.apache.xml.security.Init;
import org.opensaml.Configuration;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.parse.StaticBasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.security.DefaultSecurityConfigurationBootstrap;
import org.sonicdrivein.gocd.saml.executor.*;

import static org.sonicdrivein.gocd.saml.Constants.PLUGIN_IDENTIFIER;

@Extension
public class GoCDSAMLPlugin implements GoPlugin {
    public static final Logger LOG = Logger.getLoggerFor(GoCDSAMLPlugin.class);

    private GoApplicationAccessor accessor;
    private SAMLValidator samlValidator;

    @Override
    public void initializeGoApplicationAccessor(GoApplicationAccessor accessor) {
        try {
            // ideally we would run `org.opensaml.DefaultBootstrap.bootstrap();` but that fails with some velocity error
            StaticBasicParserPool pp = new StaticBasicParserPool();
            pp.setMaxPoolSize(50);
            pp.initialize();
            Configuration.setParserPool(pp);

            Init.init();

            Configuration.setGlobalSecurityConfiguration(DefaultSecurityConfigurationBootstrap.buildDefaultConfig());

            SAMLParser.initialize();
        } catch (ConfigurationException | XMLParserException e) {
            LOG.error(e.getClass() + ": " + e.getMessage(), e);
        }
        this.accessor = accessor;
    }

    @Override
    public GoPluginApiResponse handle(GoPluginApiRequest request) throws UnhandledRequestTypeException {
        try {
            switch (RequestFromServer.fromString(request.requestName())) {
                case REQUEST_GET_PLUGIN_ICON:
                    return new GetPluginIconExecutor().execute();
                case REQUEST_GET_CAPABILITIES:
                    return new GetCapabilitiesExecutor().execute();
                case REQUEST_GET_AUTH_CONFIG_METADATA:
                    return new GetAuthConfigMetadataExecutor().execute();
                case REQUEST_AUTH_CONFIG_VIEW:
                    return new GetAuthConfigViewExecutor().execute();
                case REQUEST_VALIDATE_AUTH_CONFIG:
                    return new AuthConfigValidateRequestExecutor(request).execute();
                case REQUEST_VERIFY_CONNECTION:
                    return new VerifyConnectionRequestExecutor(request).execute();
                case REQUEST_GET_ROLE_CONFIG_METADATA:
                    return new GetRoleConfigMetadataExecutor().execute();
                case REQUEST_ROLE_CONFIG_VIEW:
                    return new GetRoleConfigViewExecutor().execute();
                case REQUEST_VALIDATE_ROLE_CONFIG:
                    return new RoleConfigValidateRequestExecutor(request).execute();
                case REQUEST_AUTHENTICATE_USER:
                    return new UserAuthenticationExecutor(request, getSamlValidator()).execute();
                case REQUEST_AUTHORIZATION_URL:
                    return new AuthorizationUrlExecutor(request).execute();
                case REQUEST_FETCH_ACCESS_TOKEN:
                    return new FetchAccessTokenExecutor(request).execute();
                default:
                    throw new UnhandledRequestTypeException(request.requestName());
            }
        } catch (NoSuchRequestHandler e) {
            LOG.warn(e.getMessage());
            return null;
        } catch (Exception e) {
            LOG.error("Error while executing request " + request.requestName(), e);
            throw new RuntimeException(e);
        }
    }

    public SAMLValidator getSamlValidator() throws MetadataProviderException {
        if (this.samlValidator == null) {
            this.samlValidator = new SAMLValidator();
        }
        return this.samlValidator;
    }

    public void setSamlValidator(SAMLValidator samlValidator) {
        this.samlValidator = samlValidator;
    }

    @Override
    public GoPluginIdentifier pluginIdentifier() {
        return PLUGIN_IDENTIFIER;
    }
}
