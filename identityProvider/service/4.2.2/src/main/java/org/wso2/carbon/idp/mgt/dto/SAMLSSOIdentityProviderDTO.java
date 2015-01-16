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

package org.wso2.carbon.idp.mgt.dto;


import java.io.Serializable;

public class SAMLSSOIdentityProviderDTO implements Serializable {
    private boolean enableSamlWebSSO;
    private boolean enableSamlWebSSOByDefault;
    private String idpEntityId;
    private String spEntityId;
    private String urlSSO;
    private boolean doAuthRequestSigning;
    private boolean doAssertionEncrypt;
    private boolean isDoAssertionSigning;
    private boolean doLogout;
    private boolean doLogoutRequestSigning;
    private boolean doAuthResponseSigning;
    private boolean doUserIdLocation;
    private String aditionalQueryParams;

    public boolean isEnableSamlWebSSO() {
        return enableSamlWebSSO;
    }

    public void setEnableSamlWebSSO(boolean enableSamlWebSSO) {
        this.enableSamlWebSSO = enableSamlWebSSO;
    }

    public boolean isEnableSamlWebSSOByDefault() {
        return enableSamlWebSSOByDefault;
    }

    public void setEnableSamlWebSSOByDefault(boolean enableSamlWebSSOByDefault) {
        this.enableSamlWebSSOByDefault = enableSamlWebSSOByDefault;
    }

    public String getIdpEntityId() {
        return idpEntityId;
    }

    public void setIdpEntityId(String idpEntityId) {
        this.idpEntityId = idpEntityId;
    }

    public String getSpEntityId() {
        return spEntityId;
    }

    public void setSpEntityId(String spEntityId) {
        this.spEntityId = spEntityId;
    }

    public String getUrlSSO() {
        return urlSSO;
    }

    public void setUrlSSO(String urlSSO) {
        this.urlSSO = urlSSO;
    }

    public boolean isDoAuthRequestSigning() {
        return doAuthRequestSigning;
    }

    public void setDoAuthRequestSigning(boolean doAuthRequestSigning) {
        this.doAuthRequestSigning = doAuthRequestSigning;
    }

    public boolean isDoAssertionEncrypt() {
        return doAssertionEncrypt;
    }

    public void setDoAssertionEncrypt(boolean doAssertionEncrypt) {
        this.doAssertionEncrypt = doAssertionEncrypt;
    }

    public boolean isDoAssertionSigning() {
        return isDoAssertionSigning;
    }

    public void setDoAssertionSigning(boolean isDoAssertionSigning) {
        this.isDoAssertionSigning = isDoAssertionSigning;
    }

    public boolean isDoLogout() {
        return doLogout;
    }

    public void setDoLogout(boolean doLogout) {
        this.doLogout = doLogout;
    }

    public boolean isDoLogoutRequestSigning() {
        return doLogoutRequestSigning;
    }

    public void setDoLogoutRequestSigning(boolean doLogoutRequestSigning) {
        this.doLogoutRequestSigning = doLogoutRequestSigning;
    }

    public boolean isDoUserIdLocation() {
        return doUserIdLocation;
    }

    public void setDoUserIdLocation(boolean doUserIdLocation) {
        this.doUserIdLocation = doUserIdLocation;
    }

    public boolean isDoAuthResponseSigning() {
        return doAuthResponseSigning;
    }

    public void setDoAuthResponseSigning(boolean doAuthResponseSigning) {
        this.doAuthResponseSigning = doAuthResponseSigning;
    }

    public String getAditionalQueryParams() {
        return aditionalQueryParams;
    }

    public void setAditionalQueryParams(String aditionalQueryParams) {
        this.aditionalQueryParams = aditionalQueryParams;
    }
}
