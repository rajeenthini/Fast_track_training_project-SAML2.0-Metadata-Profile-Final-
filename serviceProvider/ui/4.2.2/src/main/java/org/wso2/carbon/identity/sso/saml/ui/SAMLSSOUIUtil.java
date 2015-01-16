/*
*  Copyright (c) 2005-2013, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.wso2.carbon.identity.sso.saml.ui;

//import org.apache.axis2.databinding.types.xsd.String;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.wso2.carbon.identity.sso.saml.stub.types.SAMLSSOServiceProviderDTO;
import org.wso2.carbon.ui.util.CharacterEncoder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SAMLSSOUIUtil {
    static String filename;
    static String fileContent;

    public static String getTemp() {
        return temp;
    }

    static String temp;

    public static String getFilename() {
        return filename;
    }

    public static void setFilename(String filename) {
        SAMLSSOUIUtil.filename = filename;
    }


    /**
     * Return
     *
     * @param request
     * @param parameter
     * @return
     */
    public static String getSafeInput(HttpServletRequest request, String parameter) {
        return CharacterEncoder.getSafeText(request.getParameter(parameter));
    }

    public static String getFileContent(HttpServletRequest request, String parameter) {
        HttpSession mySession = request.getSession();

        String  fileContent = null;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        try {
            //System.out.println(ServletFileUpload.isMultipartContent(request));
            //System.out.println("testing file data");
            if(ServletFileUpload.isMultipartContent(request)){
                DiskFileItemFactory factory = new DiskFileItemFactory();

                // Configure a repository (to ensure a secure temp location is used)
                ServletContext servletContext = request.getServletContext();
                File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
                factory.setRepository(repository);

                // Create a new file upload handler
                ServletFileUpload upload = new ServletFileUpload(factory);

                // Parse the request
                List<FileItem> items = upload.parseRequest(request);

                // Process the uploaded items
                for (FileItem item : items) {
                    if (!item.isFormField()) {
                        fileContent = processUploadedFile(item);

                        //System.out.println("check 1 " + item.getName());
                        temp = extractFileName(item.getName());
                        //System.out.println("check 2 "+ temp);

                    }
                }

            }
            }
            // Create a factory for disk-based file items
            catch (FileUploadException e) {
            e.printStackTrace();
        }

        return fileContent;
    }

    private static String inputStreamToString(InputStream inputStream) {
        String line;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {

            br = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

    private static String processUploadedFile(FileItem item) {
        String result = null;
        try {
            InputStream uploadedStream = item.getInputStream();
            result = inputStreamToString(uploadedStream);
            uploadedStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }



    private static String  extractFileName(String fileName){
        //System.out.println("substring method working");
        String subfileName;
        int pos = fileName.indexOf(".");
        subfileName =  fileName.substring(0,pos);
        return subfileName;

    }




    public static SAMLSSOServiceProviderDTO[] doPaging(int pageNumber,
                                                       SAMLSSOServiceProviderDTO[] serviceProviderSet) {

        int itemsPerPageInt = SAMLSSOUIConstants.DEFAULT_ITEMS_PER_PAGE;
        SAMLSSOServiceProviderDTO[] returnedServiceProviderSet;

        int startIndex = pageNumber * itemsPerPageInt;
        int endIndex = (pageNumber + 1) * itemsPerPageInt;
        if (serviceProviderSet.length > itemsPerPageInt) {

            returnedServiceProviderSet = new SAMLSSOServiceProviderDTO[itemsPerPageInt];
        } else {
            returnedServiceProviderSet = new SAMLSSOServiceProviderDTO[serviceProviderSet.length];
        }

        for (int i = startIndex, j = 0; i < endIndex && i < serviceProviderSet.length; i++, j++) {
            returnedServiceProviderSet[j] = serviceProviderSet[i];
        }

        return returnedServiceProviderSet;
    }

    public static SAMLSSOServiceProviderDTO[] doFilter(String filter,
                                                       SAMLSSOServiceProviderDTO[] serviceProviderSet) {
        String regPattern = filter.replace("*", ".*");
        ArrayList<SAMLSSOServiceProviderDTO> list = new ArrayList<SAMLSSOServiceProviderDTO>();
        for (SAMLSSOServiceProviderDTO serviceProvider : serviceProviderSet) {
            if (serviceProvider.getIssuer().toLowerCase().matches(regPattern.toLowerCase())) {
                list.add(serviceProvider);
            }
        }
        SAMLSSOServiceProviderDTO[] filteredProviders = new SAMLSSOServiceProviderDTO[list.size()];
        for (int i = 0; i < list.size(); i++) {
            filteredProviders[i] = list.get(i);

        }

        return filteredProviders;
    }

}
