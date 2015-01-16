/*
 * Copyright 2005,2006 WSO2, Inc. http://www.wso2.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.identity.core.dao;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.core.IdentityRegistryResources;
import org.wso2.carbon.identity.sso.saml.dto.SAMLSSOServiceProviderDTO;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.jdbc.utils.Transaction;


public class SAMLSSOMetadataServiceProviderDAO extends AbstractDAO {
    private static final Log log = LogFactory.getLog(SAMLSSOMetadataServiceProviderDAO.class);

    public SAMLSSOMetadataServiceProviderDAO(Registry registry) {
        this.registry = registry;
    }

    @Override
    protected SAMLSSOServiceProviderDTO resourceToObject(Resource resource) {
        return null;
    }


    /**
     * Add the service provider by given metadata file
     *
     * @param fileContent metadata file Content
     * @param issuer      name of the issuer
     * @throws org.wso2.carbon.identity.base.IdentityException
     */
    public boolean addMetadataServiceProvider(String fileContent, String issuer) throws IdentityException {
        String path = null;
        Resource resource;


        if (fileContent != null && issuer != null) {

            path = IdentityRegistryResources.SAML_SSO_METADATA_SERVICE_PROVIDERS_INBOUND + encodePath(issuer);
        }

        boolean isTransactionStarted = Transaction.isStarted();
        try {
            if (registry.resourceExists(path)) {
                if (log.isDebugEnabled()) {
                    log.debug("Service Provider already exists with the same issuer name");

                }
                return false;
            }

            resource = registry.newResource();
            resource.setContent(fileContent);
            resource.setMediaType("metadataFile");


            try {
                if (!isTransactionStarted) {
                    registry.beginTransaction();
                }

                registry.put(path, resource);

                if (!isTransactionStarted) {
                    registry.commitTransaction();
                }

            } catch (RegistryException e) {
                if (!isTransactionStarted) {
                    registry.rollbackTransaction();
                }
                throw e;
            }

        } catch (RegistryException e) {
            log.error("Error While adding Service Provider metadata file", e);
            throw new IdentityException("Error while adding Service Provider metadata file", e);
        }

        if (log.isDebugEnabled()) {
            log.debug("Service Provider metadata file is added successfully.");
        }
        return true;
    }

//    /**
//     * Get the service provider metadata file
//     *
//     * @param filename the unique Identifier of the file
//     * @return file
//     * @throws org.wso2.carbon.identity.base.IdentityException
//     */
//    public Resource getMetadataServiceProvider(String filename) throws IdentityException {
//
//        Resource retrievedFileContent = null;
//        String path = IdentityRegistryResources.SAML_SSO_METADATA_SERVICE_PROVIDERS + encodePath(filename);
//
//        try {
//            if (registry.resourceExists(path)) {
//                retrievedFileContent = registry.get(path);
//            }
//        } catch (RegistryException e) {
//            log.error("Error reading Service Providers resource  from Registry", e);
//            throw new IdentityException("Error reading Service Providers resource from Registry", e);
//        }
//
//        return retrievedFileContent;
//    }

//    /**
//     * Remove the service provider with the given name
//     *
//     * @param filename name of a file
//     * @throws IdentityException
//     */
//    public boolean removeMetadataServiceProvider(String filename) throws IdentityException {
//        String path = IdentityRegistryResources.SAML_SSO_METADATA_SERVICE_PROVIDERS + encodePath(filename);
//        boolean isTransactionStarted = Transaction.isStarted();
//        try {
//            if (registry.resourceExists(path)) {
//                try {
//                    if (!isTransactionStarted) {
//                        registry.beginTransaction();
//                    }
//
//                    registry.delete(path);
//
//                    if (!isTransactionStarted) {
//                        registry.commitTransaction();
//                    }
//
//                    return true;
//
//                } catch (RegistryException e) {
//                    if (!isTransactionStarted) {
//                        registry.rollbackTransaction();
//                    }
//                    throw e;
//                }
//            }
//        } catch (RegistryException e) {
//            log.error("Error removing the service provider metadata file from the registry", e);
//            throw new IdentityException("Error removing the service provider metadata file  from the registry", e);
//        }
//
//        return false;
//    }


    private String encodePath(String issuerName) {
        String encodedStr = new String(Base64.encodeBase64(issuerName.getBytes()));
        return encodedStr.replace("=", "");
    }
}
