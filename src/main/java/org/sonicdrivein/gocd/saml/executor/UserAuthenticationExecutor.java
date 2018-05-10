package org.sonicdrivein.gocd.saml.executor;

import org.sonicdrivein.gocd.saml.model.Configuration;
import org.sonicdrivein.gocd.saml.model.Credentials;
import org.sonicdrivein.gocd.saml.model.User;
import org.sonicdrivein.gocd.saml.utils.SAMLParser;
import org.sonicdrivein.gocd.saml.utils.SAMLValidator;
import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.Response;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.schema.impl.XSAnyImpl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.thoughtworks.go.plugin.api.response.DefaultGoApiResponse.SUCCESS_RESPONSE_CODE;

public class UserAuthenticationExecutor implements RequestExecutor {
    private static final Logger LOG = Logger.getLoggerFor(UserAuthenticationExecutor.class);
    private static final Gson GSON = new Gson();
    private final GoPluginApiRequest request;
    private final SAMLValidator samlValidator;

    public UserAuthenticationExecutor(GoPluginApiRequest request, SAMLValidator samlValidator) {
        this.request = request;
        this.samlValidator = samlValidator;
    }

    @Override
    public GoPluginApiResponse execute() throws Exception {
        Configuration configuration = Configuration.fromJSON(request.requestBody());
        Credentials credentials = Credentials.fromJSON(request.requestBody());
        Response response = SAMLParser.parse(credentials.getSamlResponse());
        LOG.info("Executing validation...");
        samlValidator.validSignature(response, configuration.getSamlMetadataPath());
        if (!samlValidator.isValidDateTime(response)) {
            return new DefaultGoPluginApiResponse(SUCCESS_RESPONSE_CODE, "{}");
       }

        Assertion assertion = response.getAssertions().get(0);
        String userEmail = assertion.getSubject().getNameID().getValue();
        String userName = userEmail.split("@")[0];
        List<String> userRoles = getADGroups(assertion);
        User user = new User(userName, userName, userEmail);

        HashMap<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("roles", userRoles);
        LOG.info(String.format("%s has been authenticated successfully", user));
        return new DefaultGoPluginApiResponse(SUCCESS_RESPONSE_CODE, GSON.toJson(result));
    }

    private List<String> getADGroups(Assertion assertion) {
        for (AttributeStatement attributeStatement : assertion.getAttributeStatements()) {
            for (Attribute attribute : attributeStatement.getAttributes()) {
                if (attribute.getName().equals("Groups")) {
                    return attribute.getAttributeValues().stream()
                            .map(this::getAttributeValue).collect(Collectors.toList());
                }
            }
        }
        return Collections.emptyList();
    }


    private String getAttributeValue(XMLObject attributeValue) {
        return attributeValue == null ?
                null :
                attributeValue instanceof XSString ?
                        getStringAttributeValue((XSString) attributeValue) :
                        attributeValue instanceof XSAnyImpl ?
                                getAnyAttributeValue((XSAnyImpl) attributeValue) :
                                attributeValue.toString();
    }

    private String getStringAttributeValue(XSString attributeValue) {
        return attributeValue.getValue();
    }

    private String getAnyAttributeValue(XSAnyImpl attributeValue) {
        return attributeValue.getTextContent();
    }

}
