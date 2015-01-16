<!--
~ Copyright (c) 2005-2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
~
~ WSO2 Inc. licenses this file to you under the Apache License,
~ Version 2.0 (the "License"); you may not use this file except
~ in compliance with the License.
~ You may obtain a copy of the License at
~
~ http://www.apache.org/licenses/LICENSE-2.0
~
~ Unless required by applicable law or agreed to in writing,
~ software distributed under the License is distributed on an
~ "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
~ KIND, either express or implied. See the License for the
~ specific language governing permissions and limitations
~ under the License.
-->

<%@page import="org.wso2.carbon.ui.util.CharacterEncoder"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="org.apache.axis2.context.ConfigurationContext"%>
<%@page import="org.wso2.carbon.CarbonConstants"%>

<%@page import="org.wso2.carbon.identity.application.common.util.IdentityApplicationManagementUtil"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="carbon" uri="http://wso2.org/projects/carbon/taglibs/carbontags.jar"%>
<%@ page import="org.wso2.carbon.idp.mgt.ui.client.IdentityProviderMgtServiceClient" %>
<%@ page import="org.wso2.carbon.idp.mgt.stub.dto.SAMLSSOIdentityProviderDTO" %>
<%@ page import="org.wso2.carbon.idp.mgt.ui.util.IdPManagementUIUtil" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIUtil" %>
<%@ page import="org.wso2.carbon.utils.ServerConstants" %>
<%@ page import="java.util.List" %>
<%@ page import="org.wso2.carbon.identity.application.common.model.idp.xsd.*" %>
<%@ page import="org.wso2.carbon.identity.application.common.model.*" %>
<%@ page import="org.wso2.carbon.identity.application.common.model.idp.xsd.Claim" %>
<%@ page import="org.wso2.carbon.identity.application.common.model.idp.xsd.ClaimMapping" %>
<%@ page import="org.wso2.carbon.identity.application.common.model.idp.xsd.RoleMapping" %>
<%@ page import="org.wso2.carbon.identity.application.common.model.idp.xsd.FederatedAuthenticatorConfig" %>
<%@ page import="org.wso2.carbon.identity.application.common.model.idp.xsd.IdentityProvider" %>
<%@ page import="org.wso2.carbon.identity.application.common.model.idp.xsd.Property" %>
<%@ page import="org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants" %>
<%@ page import="org.apache.axis2.jaxws.utility.JavaUtils" %>
<%@ page import="org.wso2.carbon.identity.application.common.model.idp.xsd.ProvisioningConnectorConfig" %>
<link href="css/idpmgt.css" rel="stylesheet" type="text/css" media="all"/>
<jsp:useBean id="samlSsoIdentityProviderConfigBean"
             type="org.wso2.carbon.idp.mgt.ui.util.IdPManagementUIUtil"
             class="org.wso2.carbon.idp.mgt.ui.util.IdPManagementUIUtil"
             scope="session"/>
<jsp:setProperty name="samlSsoIdentityProviderConfigBean" property="*"/>
<carbon:breadcrumb label="identity.providers" resourceBundle="org.wso2.carbon.idp.mgt.ui.i18n.Resources"
                    topPage="true" request="<%=request%>" />
<jsp:include page="../dialog/display_messages.jsp"/>

<script type="text/javascript" src="../admin/js/main.js"></script>
<%
String idpAction = request.getParameter("idpAction");
     if (idpAction != null && "addIdentityProviderFromUrlTest".equals(idpAction)) {



                            System.out.println("addmetadataurl jsp entered");
                            //System.out.println(request.getContextPath());

                            String urlMetadata = IdPManagementUIUtil.getMultipartParamValue(request,"metadataURL");
                            System.out.println("metadata Url in jsp page "+urlMetadata);
                            //String ssourl = request.getParameter("ssoUrl");
                            //System.out.println(ssourl);



     }


%>
