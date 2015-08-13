/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gebze.endulus;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import org.primefaces.component.media.Media;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Ömer
 */
@ManagedBean
@RequestScoped
public class MediaManager {

    
    private MongoDbService service;
    private static final boolean debug=true; 

    public MediaManager() {
        service= new MongoDbService();
        try {
            if(debug)
                service.connectToMongoDB("mydb", 27017, "localhost", "", "");
            else
                service.connectToMongoDB("mydb", 27017, "192.168.77.25", "nico", "nico");
            
        } catch (UnknownHostException ex) {
            Logger.getLogger(MediaManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    public StreamedContent getStream() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
            // So, browser is requesting the media. Return a real StreamedContent with the media bytes.
           
            InputStream input;
            String fileId = context.getExternalContext().getRequestParameterMap().get("fileId");
            String fileTable = context.getExternalContext().getRequestParameterMap().get("fileTable");
            input = service.getFileInputStream(fileId, fileTable);
            return new DefaultStreamedContent(input);
            
        }
    }

}
