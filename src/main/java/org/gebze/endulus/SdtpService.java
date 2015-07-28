/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gebze.endulus;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author mypc
 */
public class SdtpService {

    private Client c;
    private WebResource resource;

    public SdtpService() {
        c=new Client();
    }

    
    

    public List<JSdtpModel> getSdtp() {
        try {
            resource = c.resource("http://test.gebze.bel.tr/NicoExporter/exp")
                    .path("sdtpServis")
                    .path("getRootSdtps");

            ClientResponse response = resource.type(
                    MediaType.APPLICATION_JSON)
                    .post(ClientResponse.class);
            if (response.getStatus() != 201) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }
            String entity = response.getEntity(String.class);
            ArrayList readValue = new ObjectMapper().
                    readValue(entity, ArrayList.class);
            List<JSdtpModel> list = new ArrayList<>();
            for (Object rv : readValue) {
                list.add(new ObjectMapper().convertValue(rv, JSdtpModel.class));
            }
            return list;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public  List<JSdtpModel> findSdtpWithId(String id) {
        try {
            resource = c.resource("http://test.gebze.bel.tr/NicoExporter/exp")
                    .path("sdtpServis")
                    .path("getSubSdtpWithId")
                    .path(id);

            ClientResponse response = resource.type(
                    MediaType.APPLICATION_JSON)
                    .post(ClientResponse.class);
            if (response.getStatus() != 201) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }
            String entity = response.getEntity(String.class);
            ArrayList readValue = new ObjectMapper().
                    readValue(entity, ArrayList.class);
            List<JSdtpModel> list = new ArrayList<>();
            for (Object rv : readValue) {
                list.add(new ObjectMapper().convertValue(rv, JSdtpModel.class));
            }
            return list;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public  List<JSdtpModel> findWithKonu(String konu) {
        try {
            resource = c.resource("http://test.gebze.bel.tr/NicoExporter/exp")
                    .path("sdtpServis")
                    .path("getSdtpWithKonu")
                    .path(konu);

            ClientResponse response = resource.type(
                    MediaType.APPLICATION_JSON)
                    .post(ClientResponse.class);
            if (response.getStatus() != 201) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }
            String entity = response.getEntity(String.class);
            ArrayList readValue = new ObjectMapper().
                    readValue(entity, ArrayList.class);
            List<JSdtpModel> list = new ArrayList<>();
            for (Object rv : readValue) {
                list.add(new ObjectMapper().convertValue(rv, JSdtpModel.class));
            }
            return list;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;

    }
}
