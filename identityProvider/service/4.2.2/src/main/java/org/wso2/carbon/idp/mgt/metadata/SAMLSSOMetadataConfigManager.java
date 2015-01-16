/*
 *Copyright (c) 2005-2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *WSO2 Inc. licenses this file to you under the Apache License,
 *Version 2.0 (the "License"); you may not use this file except
 *in compliance with the License.
 *You may obtain a copy of the License at
 *
 *http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing,
 *software distributed under the License is distributed on an
 *"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *KIND, either express or implied.  See the License for the
 *specific language governing permissions and limitations
 *under the License.
 */
package org.wso2.carbon.idp.mgt.metadata;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.metadata.ArtifactResolutionService;
import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml2.metadata.provider.FilesystemMetadataProvider;
import org.opensaml.saml2.metadata.provider.HTTPMetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.parse.BasicParserPool;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.core.persistence.IdentityPersistenceManager;
import org.wso2.carbon.idp.mgt.dto.SAMLSSOIdentityProviderDTO;
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
    private final UserRegistry registry;



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

    public SAMLSSOIdentityProviderDTO readIdentityProvidersFromFile(String fileContent) throws IdentityException {

        String assertionConsumerServiceURL = null;
        SAMLSSOIdentityProviderDTO identityProviderDTO = new SAMLSSOIdentityProviderDTO();
        File metadataFile = new File("metadataFile");
        FileOutputStream fos = null;
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

            String idpIssuerId = entityDescriptor.getEntityID();

            String spIssuerId = entityDescriptor.getIDPSSODescriptor(SAMLConstants.SAML20P_NS).getID();


            for (ArtifactResolutionService acs : entityDescriptor.getIDPSSODescriptor(SAMLConstants.SAML20P_NS)
                    .getArtifactResolutionServices()) {
                assertionConsumerServiceURL = acs.getLocation();

            }


            identityProviderDTO.setIdpEntityId(idpIssuerId);
            identityProviderDTO.setSpEntityId(spIssuerId);
            identityProviderDTO.setUrlSSO(assertionConsumerServiceURL);


        } catch (IOException e) {
            log.error("Error in creating identity provider", e);
            throw new IdentityException("Error in creating identity provider", e);

        } catch (MetadataProviderException e) {
            log.error("Error in creating identity provider" + e);
            throw  new IdentityException("Error in creating identity provider",e);

        }


        return identityProviderDTO;

    }

    /**
     * read SAML object by uploading SAML metadata file
     *
     * @param metadataUrl url of a metadata file
     * @return SAMLSSOServiceProviderDTO object
     */

    public SAMLSSOIdentityProviderDTO readIdentityProvidersFromUrl(String metadataUrl) throws IdentityException {


        SAMLSSOIdentityProviderDTO identityProviderDTO = new SAMLSSOIdentityProviderDTO();
        String assertionConsumerServiceURL = null;

        URI metadataURI;


        try {
            HTTPMetadataProvider httpMetadataProvider = null;
            Timer timer = new Timer();
            HttpClient client = new HttpClient();

            metadataURI = new URI(metadataUrl);
            httpMetadataProvider = new HTTPMetadataProvider(timer, client, metadataURI.toString());
            httpMetadataProvider.setRequireValidMetadata(true);
            httpMetadataProvider.setParserPool(new BasicParserPool());
            httpMetadataProvider.initialize();

            EntityDescriptor entityDescriptor = (EntityDescriptor) httpMetadataProvider.getMetadata();
            String idpIssuerId = entityDescriptor.getEntityID();

            String spIssuerId = entityDescriptor.getIDPSSODescriptor(SAMLConstants.SAML20P_NS).getID();


            for (ArtifactResolutionService acs : entityDescriptor.getIDPSSODescriptor(SAMLConstants.SAML20P_NS)
                    .getArtifactResolutionServices()) {
                assertionConsumerServiceURL = acs.getLocation();

            }


            identityProviderDTO.setIdpEntityId(idpIssuerId);
            identityProviderDTO.setSpEntityId(spIssuerId);
            identityProviderDTO.setUrlSSO(assertionConsumerServiceURL);


        } catch (MetadataProviderException e) {
            log.error("error in uploading identity provider by url" + e.getMessage());
            throw new IdentityException("error in uploading identity provider by url", e);
        } catch (URISyntaxException e) {
            log.error("URI syntax error check validity of a url" + e.getMessage());
            throw new IdentityException("URI syntax error check validity of a url", e);
        }


        return identityProviderDTO;

    }


    /**
     * add SAML metadata file to registry
     *
     * @param fileContent content of a file
     * @param idpEntityId name of the IdentityProvider
     * @return boolean value
     * @throws IdentityException
     */
    public boolean addMetadataSAMLSSOFileResource(String fileContent, String idpEntityId) throws IdentityException {
        IdentityPersistenceManager persistenceManager = null;
        try {

            persistenceManager = IdentityPersistenceManager.getPersistanceManager();
        } catch (IdentityException e) {
            log.error(e + "Adding error service provider metadata file  to registry");
        }
        return persistenceManager.addMetadataIdentityProvider(registry, fileContent, idpEntityId);
    }


}
