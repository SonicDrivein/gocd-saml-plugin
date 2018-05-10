package org.sonicdrivein.gocd.saml.utils;

import org.sonicdrivein.gocd.saml.executor.UserAuthenticationExecutor;
import com.thoughtworks.go.plugin.api.logging.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.opensaml.Configuration;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.metadata.IDPSSODescriptor;
import org.opensaml.saml2.metadata.provider.FilesystemMetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.security.MetadataCredentialResolver;
import org.opensaml.security.MetadataCriteria;
import org.opensaml.security.SAMLSignatureProfileValidator;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.security.CriteriaSet;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.credential.UsageType;
import org.opensaml.xml.security.criteria.EntityIDCriteria;
import org.opensaml.xml.security.criteria.UsageCriteria;
import org.opensaml.xml.security.keyinfo.KeyInfoCredentialResolver;
import org.opensaml.xml.signature.impl.ExplicitKeySignatureTrustEngine;
import org.opensaml.xml.validation.ValidationException;

import java.io.File;

public class SAMLValidator {

    private ExplicitKeySignatureTrustEngine trustEngine;
    private SAMLSignatureProfileValidator profileValidator;
    private static final Logger LOG = Logger.getLoggerFor(UserAuthenticationExecutor.class);

    public void validSignature(Response response, String samlMetadataPath) throws SecurityException, MetadataProviderException, ValidationException, BadSAMLSignatureException {
        initialize(samlMetadataPath);
        profileValidator.validate(response.getSignature());
        if (!trustEngine.validate(response.getSignature(), getCriteria(response))) {
            throw new BadSAMLSignatureException("Signature was either invalid or signing key could not be established as trusted");
        }
    }

    public boolean isValidDateTime(Response response) {
        Conditions conditions = response.getAssertions().get(0).getConditions();
        DateTime lowerBound = conditions.getNotBefore();
        DateTime upperBound = conditions.getNotOnOrAfter();
        DateTime currentTime = new DateTime().toDateTime(DateTimeZone.UTC);
        return currentTime.isAfter(lowerBound) && currentTime.isBefore(upperBound);
    }

    private void initialize(String samlMetadataPath) throws MetadataProviderException {
        if (!(trustEngine == null) && !(profileValidator == null)) {
            return;
        }
        trustEngine = initTrustEngine(samlMetadataPath);
        profileValidator = getSignatureProfileValidator();
    }

    private CriteriaSet getCriteria(Response response) {
        CriteriaSet criteriaSet = new CriteriaSet();
        criteriaSet.add(new EntityIDCriteria(response.getIssuer().getValue()));
        criteriaSet.add(new MetadataCriteria(IDPSSODescriptor.DEFAULT_ELEMENT_NAME, SAMLConstants.SAML20P_NS));
        criteriaSet.add(new UsageCriteria(UsageType.SIGNING));
        return criteriaSet;
    }

    private ExplicitKeySignatureTrustEngine initTrustEngine(String samlMetadataPath) throws MetadataProviderException {
        MetadataProvider mdProvider = getMetadataProvider(samlMetadataPath);
        MetadataCredentialResolver mdCredResolver = new MetadataCredentialResolver(mdProvider);
        KeyInfoCredentialResolver keyInfoCredResolver =
                Configuration.getGlobalSecurityConfiguration().getDefaultKeyInfoCredentialResolver();
        return new ExplicitKeySignatureTrustEngine(mdCredResolver, keyInfoCredResolver);
    }

    private MetadataProvider getMetadataProvider(String samlMetadataPath) throws MetadataProviderException {
        FilesystemMetadataProvider metadataProvider = new FilesystemMetadataProvider(new File(samlMetadataPath));
        metadataProvider.setParserPool(new BasicParserPool());
        metadataProvider.initialize();
        return metadataProvider;
    }

    private SAMLSignatureProfileValidator getSignatureProfileValidator() {
        return new SAMLSignatureProfileValidator();
    }
}