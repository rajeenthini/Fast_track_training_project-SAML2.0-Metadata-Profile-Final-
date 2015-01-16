/*
 * Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.sso.saml.metadata;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.metadata.AssertionConsumerService;
import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml2.metadata.provider.FilesystemMetadataProvider;
import org.opensaml.saml2.metadata.provider.HTTPMetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.parse.BasicParserPool;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.core.model.SAMLSSOServiceProviderDO;
import org.wso2.carbon.identity.core.persistence.IdentityPersistenceManager;
import org.wso2.carbon.identity.sso.saml.dto.SAMLSSOServiceProviderDTO;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.session.UserRegistry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Timer;

import static org.wso2.carbon.identity.sso.saml.util.SAMLSSOUtil.doBootstrap;


public class SAMLSSOMetadataConfigManager {

    private static final Log log = LogFactory.getLog(SAMLSSOMetadataConfigManager.class);
    private static UserRegistry registry = null;


    public SAMLSSOMetadataConfigManager(Registry userRegistry) {
        doBootstrap();
        registry = (UserRegistry) userRegistry;


    }


    /**
     * read SAML object by uploading SAML metadata file
     *
     * @param fileContent content of a file
     * @return SAMLSSOServiceProviderDTO object
     */

    public SAMLSSOServiceProviderDTO readServiceProvidersFromFile(String fileContent) {


        String assertionConsumerServiceURL = null;
        SAMLSSOServiceProviderDTO serviceProviderDTO = new SAMLSSOServiceProviderDTO();
        File metadataFile = new File("metadataFile");
        FileOutputStream fos;
        byte[] fileContentByteArray = fileContent.getBytes();


        try {
            fos = new FileOutputStream(metadataFile);
            fos.write(fileContentByteArray);
            FilesystemMetadataProvider filesystemMetadataProvider;


            filesystemMetadataProvider = new FilesystemMetadataProvider(metadataFile);
            filesystemMetadataProvider.setRequireValidMetadata(true);
            filesystemMetadataProvider.setParserPool(new BasicParserPool());
            filesystemMetadataProvider.initialize();

            EntityDescriptor entityDescriptor = (EntityDescriptor) filesystemMetadataProvider.getMetadata();

            String issuer = entityDescriptor.getEntityID();

            //Now IS is not supporting multiple Assertion consumer urls
            for (AssertionConsumerService acs : entityDescriptor.getSPSSODescriptor(SAMLConstants.SAML20P_NS)
                    .getAssertionConsumerServices()) {
                assertionConsumerServiceURL = acs.getLocation();

            }


            serviceProviderDTO.setIssuer(issuer);
            serviceProviderDTO.setAssertionConsumerUrl(assertionConsumerServiceURL);
            serviceProviderDTO.setUseFullyQualifiedUsername(true);


        } catch (IOException e) {
            log.debug("IO exception" + e.getMessage());

        } catch (MetadataProviderException e) {
            log.debug("metadata provider exception" + e.getMessage());

        }


        return serviceProviderDTO;

    }

    /**
     * read SAML object by uploading SAML metadata file
     *
     * @param metadataUrl url of a metadata file
     * @return SAMLSSOServiceProviderDTO object
     */

    public SAMLSSOServiceProviderDTO readServiceProvidersFromUrl(String metadataUrl) {


        SAMLSSOServiceProviderDTO serviceProviderDTO = new SAMLSSOServiceProviderDTO();
        String assertionConsumerServiceURL = null;

        URI metadataURI;


        try {
            HTTPMetadataProvider httpMetadataProvider;
            Timer timer = new Timer();
            HttpClient client = new HttpClient();

            metadataURI = new URI(metadataUrl);
            httpMetadataProvider = new HTTPMetadataProvider(timer, client, metadataURI.toString());
            httpMetadataProvider.setRequireValidMetadata(true);
            httpMetadataProvider.setParserPool(new BasicParserPool());
            httpMetadataProvider.initialize();

            EntityDescriptor entityDescriptor = (EntityDescriptor) httpMetadataProvider.getMetadata();
            String issuer = entityDescriptor.getEntityID();


            for (AssertionConsumerService acs : entityDescriptor.getSPSSODescriptor(SAMLConstants.SAML20P_NS)
                    .getAssertionConsumerServices()) {
                assertionConsumerServiceURL = acs.getLocation();

            }


            serviceProviderDTO.setIssuer(issuer);
            serviceProviderDTO.setAssertionConsumerUrl(assertionConsumerServiceURL);
            serviceProviderDTO.setUseFullyQualifiedUsername(true);


        } catch (MetadataProviderException e) {
            log.debug("metadata Provider Exception" + e.getMessage());
        } catch (URISyntaxException e) {
            log.debug("URI syntax error exception" + e.getMessage());
        }


        return serviceProviderDTO;

    }

    /**
     * add SAML object to registry
     *
     * @param samlssoServiceProviderDO SAML service provider object
     * @return boolean value
     * @throws IdentityException
     */
    public boolean addMetadataSAMLSSOObject(SAMLSSOServiceProviderDO samlssoServiceProviderDO) throws IdentityException {
        IdentityPersistenceManager persistenceManager;
        try {
            persistenceManager = IdentityPersistenceManager.getPersistanceManager();
        } catch (IdentityException e) {
            log.error("Adding error service provider object to registry" + e.getMessage());
            throw new IdentityException("Adding error service provider object to registry", e);
        }
        return persistenceManager != null && persistenceManager.addServiceProvider(registry, samlssoServiceProviderDO);
    }

    /**
     * add SAML metadata file to registry
     *
     * @param fileContent content of a file
     * @param issuer      name of the issuer
     * @return boolean value
     * @throws IdentityException
     */
    public boolean addMetadataSAMLSSOFileResource(String fileContent, String issuer) throws IdentityException {
        IdentityPersistenceManager persistenceManager;
        try {

            persistenceManager = IdentityPersistenceManager.getPersistanceManager();
        } catch (IdentityException e) {
            log.error(e + "Adding error service provider metadata file  to registry");
            throw new IdentityException("Adding error service provider object to registry", e);
        }
        return persistenceManager != null && persistenceManager.addMetadataServiceProvider(registry, fileContent, issuer);
    }


}