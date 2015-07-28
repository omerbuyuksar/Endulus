/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gebze.endulus;

import java.io.InputStream;
import java.util.List;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Ömer
 */

@ManagedBean
@ApplicationScoped
public class IndexBean implements Serializable{
    
    private List<FileModel> files;
    private FileModel selectedFile;
    private SdtpTreeNode root;
    private SdtpTreeNode selectedNode;
    private boolean connected=false;
    private MongoDbService mydb;
    private String console;
    private boolean check;
    private SdtpService sdtpService;
     
    //@ManagedProperty("#{carService}")
    //private CarService service;
    

    public int buttonConnectDb() {
        try {
            mydb.connectToMongoDB("mydb",27017,"192.168.77.25","nico","nico");
            files=mydb.getAllFiles("veriler");
            addMessage("Connected");
            connected=true;
        } catch (Exception e) {
            addMessage("Connection Failed");
        }
        
        return 0;
    }
    
    public int buttonDownload() {
        try {
            
            addMessage("Connected");
        } catch (Exception e) {
            addMessage("Connection Failed");
        }
        
        return 0;
    }
    public void buttonAction() {
        addMessage("button action!");
    }
     
    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    @PostConstruct
    public void init() {
        Set<String> collectionNameList = null;
        List<String> list = new ArrayList();
        mydb=new MongoDbService();
        mydb.setCursorLimit(1500);
        files=new ArrayList<>();
        sdtpService=new SdtpService();
        root = new SdtpTreeNode("Root", null);
        addSdtpNodes();
    }
    private void addSdtpNodes(){
        
        List<JSdtpModel> list=sdtpService.getSdtp();
        
        if(list==null){
            addMessage("Cannot Get SDTP Files");
            return;
        }
        
        for (JSdtpModel list1 : list) {
            root.getChildren().add(new DefaultTreeNode(list1.getKonu()));
        }

    }
    public StreamedContent getDownloadFile(FileModel file){
        InputStream input ;
        String str = null;
        try {
             input = mydb.getFileInputStream(file.getId(),file.getTable());
             str = MongoDbService.displayContentType(file.getFileName());
        } catch (Exception e) {
            e.printStackTrace();
            addMessage("File cannot Download");
            return null;
        }
        return new DefaultStreamedContent(input, str, file.getFileName());
    }
    
    public void onExpandNode(NodeExpandEvent event){
        
        
        List<JSdtpModel> list=sdtpService.findSdtpWithId(selectedNode)
        
        
        
    }
    
    
    
 
    public List<FileModel> getFiles() {
        return files;
    }
 
    public FileModel getSelectedFile() {
        return selectedFile;
    }
    
 
    public void setSelectedFile(FileModel selectedFile) {
        this.selectedFile = selectedFile;
    }
    public void save() {
        addMessage("Success Data saved");
    }
     
    public void update() {
        addMessage("Data updated");
    }
     
    public void delete() {
        addMessage("Data deleted");
    }

    public SdtpTreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(SdtpTreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public SdtpTreeNode getRoot() {
        return root;
    }



    public String getConsole() {
        return console;
    }

    public void setConsole(String console) {
        this.console = console;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
    
    

}
