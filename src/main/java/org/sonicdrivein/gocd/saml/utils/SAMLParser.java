/*!
 * Copyright 2012 Sakai Foundation (SF) Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 *     http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.sonicdrivein.gocd.saml.utils;

import org.opensaml.Configuration;
import org.opensaml.saml2.core.Response;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLConfigurator;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.util.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

public class SAMLParser {
    private static String[] xmlToolingConfigs = {
            "/default-config.xml",
            "/schema-config.xml",
            "/signature-config.xml", "/signature-validation-config.xml",
            "/encryption-config.xml",
            "/encryption-validation-config.xml",
            "/soap11-config.xml",
            "/wsfed11-protocol-config.xml",
            "/saml1-assertion-config.xml",
            "/saml1-protocol-config.xml",
            "/saml1-core-validation-config.xml",
            "/saml2-assertion-config.xml",
            "/saml2-protocol-config.xml",
            "/saml2-core-validation-config.xml",
            "/saml1-metadata-config.xml",
            "/saml2-metadata-config.xml",
            "/saml2-metadata-validation-config.xml",
            "/saml2-metadata-attr-config.xml",
            "/saml2-metadata-idp-discovery-config.xml",
            "/saml2-metadata-ui-config.xml",
            "/saml2-protocol-thirdparty-config.xml",
            "/saml2-metadata-query-config.xml",
            "/saml2-assertion-delegation-restriction-config.xml",
            "/saml2-ecp-config.xml",
            "/xacml10-saml2-profile-config.xml",
            "/xacml11-saml2-profile-config.xml",
            "/xacml20-context-config.xml",
            "/xacml20-policy-config.xml",
            "/xacml2-saml2-profile-config.xml",
            "/xacml3-saml2-profile-config.xml",
            "/wsaddressing-config.xml",
            "/wssecurity-config.xml",
    };

    public static void initialize() throws ConfigurationException {
        Class clazz = Configuration.class;
        XMLConfigurator configurator = new XMLConfigurator();

        for (String config : xmlToolingConfigs) {
            configurator.load(clazz.getResourceAsStream(config));
        }
    }

    public static Response parse(String samlResponse) throws Exception {
        byte[] base64DecodedResponse = Base64.decode(samlResponse);
        ByteArrayInputStream is = new ByteArrayInputStream(base64DecodedResponse);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = docBuilder.parse(is);
        Element element = document.getDocumentElement();
        UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();

        return (Response) unmarshallerFactory.getUnmarshaller(element).unmarshall(element);
    }

}